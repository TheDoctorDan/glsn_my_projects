/* adjust_lilo_conf.c */
/*  arg is version # */

#include <stdio.h>
#include <string.h>



char	*justify_with_tabs(char *dmy)
{
	char	*fp, *bp;
	fp = dmy;
	while (*fp == ' ' || *fp == '	')
		fp++;
	bp = fp;
	while (*bp)
		bp++;
	if (bp != fp) {
		bp--;
		while ((*bp == ' ' || *bp == '	' ) && bp >= fp)
			bp--;
		bp++;
		*bp = '\0';
	}
	strcpy(dmy, fp);
	return(dmy);
}




extern char	*mktemp(char *);




main(int argc, char **argv)
{
	int	main_err=0;
	char	mess[200];
	int	i;

	char	*version_num;
	char	HD_BASE_DIR[500];

	char	INSTALLING_VERSIONS[500];
	FILE	*finstalling_versions;

	char	LOCAL_LILO_CONF[500];
	FILE	*flocal_lilo_conf;

	char	in_line[501];
	int	found;

	char	justified_in_line[501];

	int	image_line;
	char	image_text[100][501];


	char	work_preamble_filename[100];
	FILE	*fwork_preamble;


	char	work_epilogue_filename[100];
	FILE	*fwork_epilogue;

	char	command[300];

	int	need_smp, found_image, captured_image, dmy_bool;

	int	found_initrd;

	printf("\n\nAdjust lilo.conf\n\n");

	strcpy(work_preamble_filename, "/var/tmp/");
	strcat(work_preamble_filename, "Wadjust_lilo_confXXXXXX");
	mktemp(work_preamble_filename);
	fwork_preamble = fopen(work_preamble_filename, "w+");
	if (fwork_preamble == NULL) {
		sprintf(mess, "adjust_lilo_conf: Can't open file: %s\n", work_preamble_filename);
		perror(mess);
		exit(1);
	}

	strcpy(work_epilogue_filename, "/var/tmp/");
	strcat(work_epilogue_filename, "Wadjust_lilo_confXXXXXX");
	mktemp(work_epilogue_filename);
	fwork_epilogue = fopen(work_epilogue_filename, "w+");
	if (fwork_epilogue == NULL) {
		sprintf(mess, "adjust_lilo_conf: Can't open file: %s\n", work_epilogue_filename);
		perror(mess);
		fclose(fwork_preamble);
		unlink(work_preamble_filename);
		exit(1);
	}




	if(argc !=2){
		fprintf(stderr,"Usage: adjust_lilo_conf version_number\n");
		main_err++;
		goto end;
	}
	version_num=argv[1];


	/*
	version_num=2.2.17-14
	version_num=2.2.17-14smp
	*/

	strcpy(HD_BASE_DIR, "/var/tmp/rpm_updates/RedHat_Kernels");


	strcpy(INSTALLING_VERSIONS, HD_BASE_DIR);
	strcat(INSTALLING_VERSIONS, "/INSTALLING_VERSIONS");

	strcpy(LOCAL_LILO_CONF, HD_BASE_DIR);
	strcat(LOCAL_LILO_CONF, "/LOCAL_LILO_CONF");


	finstalling_versions = fopen(INSTALLING_VERSIONS,"r");
	if(finstalling_versions == NULL){
		fprintf(stderr, "Can't open file:%s:\n", INSTALLING_VERSIONS);
		main_err++;
		goto end;
	}


	found=0;
	while(!feof(finstalling_versions)){
		fgets(in_line, 500, finstalling_versions);
		if(feof(finstalling_versions))
			break;
		if(in_line[strlen(in_line) - 1] == '\n')
			in_line[strlen(in_line) - 1] = '\0';
		if(strcmp(in_line, version_num) ==0)
			found=1;
	}
	fclose(finstalling_versions);

	if (found == 0){
		fprintf(stderr, "Invalid version number passed on command line.\n");
		main_err++;
		goto end;
	}


	flocal_lilo_conf=fopen(LOCAL_LILO_CONF,"r");
	if(flocal_lilo_conf == NULL){
		sprintf(command, "cp /etc/lilo.conf %s", LOCAL_LILO_CONF);
		system(command);
		flocal_lilo_conf=fopen(LOCAL_LILO_CONF,"r");
		if(flocal_lilo_conf == NULL){
			fprintf(stderr,"Can not open file:%s:\n", LOCAL_LILO_CONF);
			main_err++;
			goto end;
		}
	}


	if(strstr(version_num,"smp")!= NULL){
		need_smp=1;
	}else{
		need_smp=0;
	}


	found_image=0;
	captured_image=0;

	while(!feof(flocal_lilo_conf)){
		fgets(in_line, 500, flocal_lilo_conf);
		if(feof(flocal_lilo_conf))
			break;
		if(in_line[strlen(in_line) - 1] == '\n')
			in_line[strlen(in_line) - 1] = '\0';

		strcpy(justified_in_line, in_line);
		justify_with_tabs(justified_in_line);

		if(found_image == 0){
			dmy_bool=0;
			if(need_smp){
				if (strncmp(justified_in_line, "image=/boot/vmlinuz", 19) == 0 &&
				    strncmp(&justified_in_line[strlen(justified_in_line)-3], "smp", 3) ==0)
					dmy_bool=1;
			}else{
				if (strncmp(justified_in_line, "image=/boot/vmlinuz", 19) == 0 &&
				    strncmp(&justified_in_line[strlen(justified_in_line)-3], "smp", 3) !=0)
					dmy_bool=1;
			}

			if (dmy_bool){
				found_image=1;
				image_line=0;
				strcpy(image_text[image_line++], in_line);
			}else{
				/* preamble */
				if (strncmp(justified_in_line, "default", 7)==0){
					fprintf(fwork_preamble, "# adjust_linux_conf commenting out default\n");
					fprintf(fwork_preamble, "# you can put it back.\n");
					fprintf(fwork_preamble, "#");
				}
				fprintf(fwork_preamble, "%s\n", in_line);
			}
		}else{
			if(captured_image == 0){
				/* save image */
				if (strncmp(justified_in_line, "image", 5) == 0 || strncmp(justified_in_line, "#", 1) == 0){
					captured_image=1;
					/* epilogue */
					fprintf(fwork_epilogue, "%s\n", in_line);
				}else{
					strcpy(image_text[image_line++],in_line);
				}
			}else{
				/* epilogue */
				fprintf(fwork_epilogue,"%s\n",in_line);
			}
		}
	}

	fclose(flocal_lilo_conf);

	fflush(fwork_preamble);
	rewind(fwork_preamble);
				
	fflush(fwork_epilogue);
	rewind(fwork_epilogue);

	flocal_lilo_conf=fopen(LOCAL_LILO_CONF,"w");
	if(flocal_lilo_conf == NULL){
		fprintf(stderr,"Can not re-open file:%s:\n", LOCAL_LILO_CONF);
		main_err++;
		goto end;
	}

	/* preamble */
	while(!feof(fwork_preamble)){
		fgets(in_line, 500, fwork_preamble);
		if(feof(fwork_preamble))
			break;
		fputs(in_line, flocal_lilo_conf);
	}
	fprintf(flocal_lilo_conf,"\n");


	/* new version */
	found_initrd=0;
	fprintf(flocal_lilo_conf,"#new version\n");
	fprintf(flocal_lilo_conf, "image=/boot/vmlinuz-%s\n", version_num);
	for(i=1;i<image_line;i++){
		strcpy(in_line, image_text[i]);
		strcpy(justified_in_line, in_line);
		justify_with_tabs(justified_in_line);
		if(strncmp(justified_in_line, "initrd", 6) ==0){
			fprintf(flocal_lilo_conf, "\tinitrd=/boot/initrd-%s.img\n", version_num);
			found_initrd=1;
		}else{
			fprintf(flocal_lilo_conf, "%s\n", image_text[i]);
		}
	}
	if(!found_initrd)
		fprintf(flocal_lilo_conf, "initrd=/bin/initrd-%s\n", version_num);
	fprintf(flocal_lilo_conf,"\n");

	/* old version */
	fprintf(flocal_lilo_conf,"#old version\n");
	for(i=0;i<image_line;i++){
		strcpy(in_line, image_text[i]);
		strcpy(justified_in_line, in_line);
		justify_with_tabs(justified_in_line);
		if(strncmp(justified_in_line, "label", 5) ==0){
			fprintf(flocal_lilo_conf, "\t%s.old\n", justified_in_line);
		}else{
			fprintf(flocal_lilo_conf, "%s\n", image_text[i]);
		}
	}

	fprintf(flocal_lilo_conf,"\n");
	while(!feof(fwork_epilogue)){
		fgets(in_line, 500, fwork_epilogue);
		if(feof(fwork_epilogue))
			break;
		fputs(in_line, flocal_lilo_conf);
	}
	fprintf(flocal_lilo_conf,"\n");
	fflush(flocal_lilo_conf);
	fclose(flocal_lilo_conf);

end:

	fclose(fwork_preamble);
	unlink(work_preamble_filename);
	fclose(fwork_epilogue);
	unlink(work_epilogue_filename);

	return(main_err);
}


