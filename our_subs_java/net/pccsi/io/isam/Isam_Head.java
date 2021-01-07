package  net.pccsi.io.isam;

class Isam_Head {
	static final long	DIS = 0L;
	short	magic_number;
	int	change_indicator;
	int	lock_id;
	int	key_len;
	int	record_size;

	long	begining_of_tree;
	long	rec_avail;
	long	block_avail;
	long	record_count;
}


