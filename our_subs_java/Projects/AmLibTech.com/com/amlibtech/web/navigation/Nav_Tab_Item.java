/*
 * Nav_Tab_Item.java
 *
 * Created on September 15, 2004, 1:49 PM
 */

/*
|------------------------------------------------------------------------------|
|       Copyright (c) 1985 thru 1998, 1999, 2000, 2001, 2002, 2003, 2004       |
|       American Liberator Technologies                                        |
|       All Rights Reserved                                                    |
|                                                                              |
|       THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF                         |
|       American Liberator Technologies                                        |
|       The copyright notice above does not evidence any                       |
|       actual or intended publication of such source code.                    |
|------------------------------------------------------------------------------|
*/

package com.amlibtech.web.navigation;

/**
 *
 * @author  dgleeson
 */
public class Nav_Tab_Item {
    	public String	image;
	public String	section_directory;
	public String	link;
	public Nav_Bar_List	nav_Bar_List;

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
    
    /** Creates a new instance of Nav_Tab_Item */


	public	Nav_Tab_Item(
		String image,
		String section_directory,
		String link,
		Nav_Bar_List nav_Bar_List
	) {
		this.image = image;
		this.section_directory = section_directory;
		this.link = link;
		this.nav_Bar_List = nav_Bar_List;
	}


    
}
