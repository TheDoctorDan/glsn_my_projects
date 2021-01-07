#! /bin/sh
# proc_tables.sh
#
# Fri Jul 15 14:46:54 CDT 2005
#

database="ALTASP"
output_dir="/usr/gleeson/our_subs/java/Projects/AmLibTech.com/com/amlibtech/login/sql_scripts/Generated"

{
while true
	do
	read table lowercase uppercase parent index_option
	if [ -z "$table" ]
	then
		exit 0
	fi
	echo "l" $lowercase
	echo

	jsp_gen_code.sh $database $output_dir $table $lowercase $uppercase $parent $index_option
done 
} <<xxx
subscriber_organization	subscriber_organization	Subscriber_Organization
user	user	User

xxx


