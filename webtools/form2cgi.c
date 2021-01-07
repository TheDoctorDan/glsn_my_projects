/* form2cgi.c */
/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1996, 1997, 1998, 1999, 2000, 2001, 2002       |
|       Gleeson and Associates                                                 |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       Gleeson And Associates                                                 |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/


#include "win.h"

char	token[4096];
int	token_len;

int	global_last_token_type_index;

form2cgi_get_token()
{
	int	c;
	int	in_word;

	in_word = 0;

	token_len = 0;

	while (1) {
		c = getc(fp);
		if (in_word) {
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || 
			    (c >= '0' && c <= '9')) {
				token[token_len++] = c;
			} else {
				ungetc(c, fp);
				token[token_len] = 0;
#if DEBUG
				wmprint(window, 0, at_li - 1, "token:word :%s:\n", token);
				window_refresh();
#endif
				return(global_last_token_type_index = TOKEN_TYPE_WORD);
			}

		} else {
			if (feof(fp)) {
#if DEBUG
				wmprint(window, 0, at_li - 1, "token:EOF:\n");
				window_refresh();
#endif
				return(global_last_token_type_index = TOKEN_TYPE_EOF);
			} else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || c == '#') {
				token[token_len++] = c;
				in_word = 1;
			} else {
				switch (c) {
				case ' ':
				case '\t':
					break;
				case '\n':
#if DEBUG
					wmprint(window, 0, at_li - 1, "token:EOL:\n");
					window_refresh();
#endif
					return(global_last_token_type_index = TOKEN_TYPE_EOL);
				default:
					wmprint(window, 0, at_li - 1, "unexpected character:%c:\n", c);
					window_refresh();
					fx_scan_failure();
					return(global_last_token_type_index = -1);
				}
			}
		}
	}
}



main(int argc, char **argv)
{
}
