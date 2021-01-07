/*
 * Nav_Tab_List.java
 *
 * Created on September 15, 2004, 1:51 PM
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

import javax.servlet.http.*;

/**
 *
 * @author  dgleeson
 */
public class Nav_Tab_List {
    public int height;
    public Nav_Tab_Item[] nav_Tab_Items;

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

    /** Creates a new instance of Nav_Tab_List */
    public Nav_Tab_List(int height, Nav_Tab_Item[]nav_Tab_Items) {
	this.height = height;
	this.nav_Tab_Items = nav_Tab_Items;
    };

    public String to_Html_Horz_Table(HttpSession session) {
	StringBuffer result = new StringBuffer();
	int found_Tab_Index = -1;

	String section = get_Tab_Section(session);
        
	result.append("    <table border=0 cellspacing=0 cellpadding=0>\n");
	result.append("        <tr>\n");

	for (int i = 0; i < this.nav_Tab_Items.length; i++) {
	    if (i == 0) {
		result.append("        <td valign=bottom align=right>\n");
	    } else {
		result.append("        <td>\n");
	    }

	    result.append("<a href=\"" + this.nav_Tab_Items[i].link + "\"><img src=\"" + this.nav_Tab_Items[i].image + "\"  height=" + this.height + " border=0 align=\"absbottom\" ></a>\n");

	    if (section == null)
		found_Tab_Index = 0;
	    else {
		if (this.nav_Tab_Items[i].section_directory.equals(section))
		    found_Tab_Index = i;
	    }
	  
	    result.append("</td>\n");
	}
	result.append("        </tr>\n");
	result.append("    </table>\n");

	if (found_Tab_Index != -1) {
	    Nav_Bar_List nav_Bar_List = this.nav_Tab_Items[found_Tab_Index].nav_Bar_List;
	    result.append(nav_Bar_List.to_Html_Horz_Table());
	}

	return result.toString();
    }

    public static void set_Tab_Section(String section, HttpSession session) {
	session.setAttribute("s_Nav_Tab_Section", section);
    }
    
    public String get_Tab_Section(HttpSession session) {
	return (String) session.getAttribute("s_Nav_Tab_Section");
    }

    public String get_Color(HttpSession session){
	String section = get_Tab_Section(session);
        	int found_Tab_Index = -1;

	for (int i = 0; i < this.nav_Tab_Items.length; i++) {
              if (section == null)
		found_Tab_Index = 0;
	    else {
		if (this.nav_Tab_Items[i].section_directory.equals(section))
		    found_Tab_Index = i;
	    }
        }
	if (found_Tab_Index != -1) {
	    Nav_Bar_List nav_Bar_List = this.nav_Tab_Items[found_Tab_Index].nav_Bar_List;
            return nav_Bar_List.bgcolor;
        }else{
            return "#FFCC44";
        }
    }
}
