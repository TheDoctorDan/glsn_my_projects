/* JIsam.java */

package com.amlibtech.io;


public class JIsam {

	public JIsam()
	{
	}


    private static final String[] Copyright= {
    "|       Copyright (c) 1985 thru 2001, 2002, 2003, 2004, 2005, 2006, 2007       |",
    "|       American Liberator Technologies                                        |",
    "|       All Rights Reserved                                                    |",
    "|                                                                              |",
    "|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |",
    "|       American Liberator Technologies                                        |",
    "|       The copyright notice above does not evidence any                       |",
    "|       actual or intended publication of such source code.                    |"
    };


	// Native method Declaration
	public native int	dclose(int fdes);

	public native int	dopen(String  prog, String file, String mode);

	public native int	err_dopen(String prog, String file, String mode);

	public native int	err_dopen_create_isam_temp(String prog, String prefix_filename, int key_len, int record_size);



	native int	create(String file_name, int key_len, int record_size);

	native int	setup_new_file(int	fdes, int key_len, int record_size);

	native int	initialize_alloc();



	native long	insert(int fdes, String key);
	native long	next(int fdes, String key);
	native long	prev(int fdes, String key);

	native int	fkl(int fdes);
	native int	frl(int fdes);

	native int	lock(int fdes);
	native void	unlock(int fdes);
	native int	wait_lock(int fdes);


	native int	r_lock(int fdes, long where, int area);
	native void	r_unlock(int fdes, long where, int area);
	native void	print_lock_list();


	/*
	native long	delete_unlock(int fdes, String key);
	native long	find_lock_read(int fdes, String key, Isam_Data_Record record);
	native long	find_read(int fdes, String key, Isam_Data_Record record);
	native long	insert_write(int fdes, String key, Isam_Data_Record record);
	native long	insert_write_then_lock(int fdes, String key, Isam_Data_Record record);
	native long	insert_write_unlock(int fdes, String key, Isam_Data_Record record);
	native long	next_lock_read(int fdes, String key, Isam_Data_Record record);
	native long	next_lock_read_limit(int fdes, String key, Isam_Data_Record record, String string);
	native long	next_lock_read_match(int fdes, String key, Isam_Data_Record record, int position, String string);
	native long	next_lock_read_range(int fdes, String key, Isam_Data_Record record, String first, String last);
	native long	next_read(int fdes, String key, Isam_Data_Record record);
	native long	next_read_limit(int fdes, String key, Isam_Data_Record record, String string);
	native long	next_read_match(int fdes, String key, Isam_Data_Record record, int position, String string);
	native long	next_read_range(int fdes, String key, Isam_Data_Record record, String first, String last);
	native long	prev_lock_read(int fdes, String key, Isam_Data_Record record);
	native long	prev_lock_read_limit(int fdes, String key, Isam_Data_Record record, String string);
	native long	prev_lock_read_match(int fdes, String key, Isam_Data_Record record, int position, String string);
	native long	prev_lock_read_range(int fdes, String key, Isam_Data_Record record, String first, String last);
	native long	prev_read(int fdes, String key, Isam_Data_Record record);
	native long	prev_read_limit(int fdes, String key, Isam_Data_Record record, String string);
	native long	prev_read_match(int fdes, String key, Isam_Data_Record record, int position, String string);
	native long	prev_read_range(int fdes, String key, Isam_Data_Record record, String first, String last);
	*/

}
