package  net.pccsi.io.isam;



/* release3 */
class Isam_2nd_Head {
	static final long	DIS = 36L;
	static final long	ACTION_NONE = 0L;
	static final long	ACTION_ADD = 1L;
	static final long	ACTION_DELETE = 2L;
	long	begining_of_2nd_tree;
	long	action; /* none, add, delete */
	long	update_status_flag;
	long	record_link; /* record location */
	String	key;	/* beginning of key */
}


