package  net.pccsi.io.isam;

import java.io.*;
import java.nio.channels.*;
import net.pccsi.io.*;

public class Isam_File {
	static final int	MAX_LEVELS = 15;

	static int	record_lock_flag;
	static String	high_key;
	static String	save_key;

	Isam_2nd_Head	isam_2nd_head;

	Isam_Node[]	master_blocks = new Isam_Node[MAX_LEVELS];
	Isam_Node	work_block;
	Isam_Node	new_node;
	long[]	master_pos = new long[MAX_LEVELS];
	Isam_Node	node_array;
	int	save_change;
	int	file_lock_flag;

	Isam_Head	info_head;
	Isam_R2_Numbers	info_r2_numbers;

	String	fullPathName;
	String	file_Name;


	RandomAccessFile	randomAccessFile;


	/** 
	* This Constructor is for opening Existing Isam Files.
	* Filenames starting with / will use filename as full path name.
	* Filenames not starting with / will look for filename in $DATAPATH directories.
	*/
	public Isam_File(String file_Name, String mode, int key_length, int record_length) throws FileNotFoundException {
		this.file_Name = new String(file_Name);

		try {
			fullPathName = DataPath.fullPathName(file_Name);
		}catch (FileNotFoundException fnfe){
			throw fnfe;
		}

		try {
			randomAccessFile = RandomAccessFile(fullPathName, mode);
		}catch (FileNotFoundException fnfe){
			throw fnfe;
		}

		init();
	}

	/** 
	* This Constructor is for creating Isam Files.
	* Mode is always rw.
	* Filenames must always be a full path name.
	*/
	public Isam_File(String fullPathName, int key_length, int record_length) throws FileNotFoundException {
		this.file_Name = new String(fullPathName);
		this.fullPathName = new String(fullPathName);

		try {
			randomAccessFile = RandomAccessFile(fullPathName, "rw");
		}catch (FileNotFoundException fnfe){
			throw fnfe;
		}

		init();
	}



	private void init() {

		isam_2nd_head = new Isam_2nd_Head();
		
		for(int i=0; i<MAX_LEVELS; i++){
			master_blocks[i] = new Isam_Node();
		}

		work_block = new Isam_Node();
		new_node = new Isam_Node();

		for(int i=0; i<MAX_LEVELS; i++){
			master_pos[i] = 0L;
		}

		/* nolonger an array, was one per NFILE */
		node_array = new Isam_Node();

		save_change=0;
		file_lock_flag=0;

		info_head = new Isam_Head();
		info_r2_numbers = new Isam_R2_Numbers();


	}




	public int	fkl(){
		return(info_head.key_len);
	}

	public int	frl(){
		return(info_head.record_size);
	}



	public FileLock	lock() throws IOException {
		FileLock filelock = new FileLock(randomAccessFile.getChannel(), 0L, Long.MAX_VALUE, false);
		return(filelock);
	}











/* ================================================================================ */
/* pnv.c

	locking controls for isam access
*/



	int	P_write(int fdes)
	{
		String	mess;
		FileLock header_FileLock;
		/* if whole file locked then return */
		if(file_lock_flag){
			isam_signal_set(); 
			return(0);
		}else{
	again:
			filelock = new FileLock(randomAccessFile.getChannel(), ISAM_HEAD_DIS, sizeof(isam_head), false);
			lock_rec.l_pid=0;
			if(fcntl(fdes,F_SETLKW,&lock_rec) == -1){
				sprintf(mess,"isam_P_write: filename:%s:  fcntl F_SETLKW failed.",isam_fdes_info_rec[fdes].isam_numbers.filename);
				isam_add_log(mess);
				sprintf(mess,"isam_P_write: errno=%d",errno);
				isam_add_log(mess);
				sprintf(mess,"isam_P_write: lock_rec.l_pid=%d",lock_rec.l_pid);
				isam_add_log(mess);
				sprintf(mess,"isam_P_write: lock_rec.l_type=%d",lock_rec.l_type);
				isam_add_log(mess);
				perror("isam_P_write");
				if(errno==46){
					sprintf(mess,"Out of lock's! Waiting 10 seconds to try again.");
					isam_add_log(mess);
					sleep(10);
					goto again;
				}
				return(-1);
			}
			isam_signal_set();
			return(0);
		}
	}

	int	P_read(int fdes)
	{
		char	mess[200];
		struct flock lock_rec;
		/* if whole file locked then return */
		if(isam_file_lock_flag[fdes]){
			return(0);
		}else{
	again:
			memset(&lock_rec,0,sizeof lock_rec);
			lock_rec.l_type=F_RDLCK;
			lock_rec.l_whence=0;
			lock_rec.l_start=ISAM_HEAD_DIS;
			lock_rec.l_len=sizeof isam_fdes_info_rec[fdes].isam_head;
	#ifndef hpmc68
	#ifndef DEC_ALPHA
	#ifndef linux
			lock_rec.l_sysid=0;
	#endif
	#endif
	#endif
			lock_rec.l_pid=0;
			if(fcntl(fdes,F_SETLKW,&lock_rec) == -1){
				sprintf(mess,"isam_P_read: filename:%s: fcntl F_SETLKW failed.", isam_fdes_info_rec[fdes].isam_numbers.filename);
				isam_add_log(mess);
				perror("isam_P_read");
				sprintf(mess,"isam_P_read: errno=%d",errno);
				isam_add_log(mess);
				if(errno==46){
					sprintf(mess,"Out of lock's! Waiting 10 seconds to try again.");
					isam_add_log(mess);
					sleep(10);
					goto again;
				}
				return(-1);
			}
			return(0);
		}
	}




	int	V_write(int fdes)
	{
		char	mess[200];
		int	ier;
		isam_signal_reset();
		if(isam_file_lock_flag[fdes])
			return(0);
		if (lseek(fdes, ISAM_HEAD_DIS, 0) == -1)
			goto seekerror;
		ier = lockf(fdes, F_ULOCK, sizeof isam_fdes_info_rec[fdes].isam_head);
		return(ier);
	seekerror:
		sprintf(mess, "isam_V_write: filename:%s: lseek error.", isam_fdes_info_rec[fdes].isam_numbers.filename);
		isam_add_log(mess);
		perror("isam_V_write");
				sprintf(mess,"isam_V_write: errno=%d",errno);
				isam_add_log(mess);
		isam_signal_reset();
		return((int)ISAM_ERR_SEEK_ERROR);
	}

	int	isam_V_read(int fdes)
	{
		int	ier;
		char	mess[200];
		if(isam_file_lock_flag[fdes])
			return(0);
		if (lseek(fdes, ISAM_HEAD_DIS, 0) == -1)
			goto seekerror;
		ier = lockf(fdes, F_ULOCK, sizeof isam_fdes_info_rec[fdes].isam_head);
		return(ier);
	seekerror:
		sprintf(mess, "isam_V_read: filename:%s: lseek error.",isam_fdes_info_rec[fdes].isam_numbers.filename);
		isam_add_log(mess);
		perror("isam_V_read");
				sprintf(mess,"isam_V_read: errno=%d",errno);
				isam_add_log(mess);
		return((int)ISAM_ERR_SEEK_ERROR);
	}




================================================================================



	/*
	public boolean	wait_lock(){
	}
	public boolean	unlock(){
	}



	public boolean	r_lock(){
	}
	public boolean	r_unlock(){
	}


	*/


	public long	find(String key) {
		long	recl;
		long	ier;
		Isam_Node * node;
again:
		if (isam_P_read(fdes) != 0)
			return(ISAM_ERR_P_ERR);
		node = isam_initialize(fdes);
		if (node == NULL){
			fprintf(stderr, "isam_find: isam_initialize failed for filename:%s:\n",isam_fdes_info_rec[fdes].isam_numbers.filename);
			perror("isam_find");
			return(-2l);
		}
		strncpy(node->key, key, isam_fdes_info_rec[fdes].isam_head.key_len);
		node->key[isam_fdes_info_rec[fdes].isam_head.key_len] = '\0';
		recl = isam_find_a(fdes, node);
		if (isam_V_read(fdes) != 0)
			return(ISAM_ERR_P_ERR);
		if (recl < 0l) {
			return(recl);
		}
		if (recl == 0L) {
			if (lseek(fdes, 0L, 2) == -1l)
				goto seekerror;
		} else {
			if ((ier = isam_trylock(fdes, recl)) > 0l)
				goto again;
			else if (ier < 0l){
				return(ier);
			}
		}
		return(recl);
	seekerror:
		fprintf(stderr, "isam_find:seek error. filename:%s:\n",isam_fdes_info_rec[fdes].isam_numbers.filename);
		perror("isam_find");
		return(-5L);
	}


	/*
	public long	insert(){
	}

	public long	next(){
	}

	public long	prev(){
	}

	public long	delete(){
	}


	public long	delete_unlock(){
	}


	*/


}


