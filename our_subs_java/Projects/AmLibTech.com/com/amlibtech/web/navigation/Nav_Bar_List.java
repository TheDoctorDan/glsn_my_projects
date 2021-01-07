/*
 * Nav_Bar.java
 *
 * Created on September 15, 2004, 1:43 PM
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
public class Nav_Bar_List {
    public int	height;
    public String bgcolor;
    public String font_Color;
    public String pixel_Seperator_Image;
    public Nav_Bar_Item[] nav_Bar_Items;

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
    
    /** Creates a new instance of Nav_Bar */
    
    public	Nav_Bar_List(
    int	height,
    String bgcolor,
    String font_Color,
    String pixel_Seperator_Image,
    Nav_Bar_Item[] nav_Bar_Items
    ) {
        this.height = height;
        this.bgcolor = bgcolor;
        this.font_Color = font_Color;
        this.pixel_Seperator_Image = pixel_Seperator_Image;
        this.nav_Bar_Items = nav_Bar_Items;
    }
    
    public String to_Html_Horz_Table() {
        StringBuffer result = new StringBuffer();
        
        result.append("   <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
        result.append("   <tr>\n");
        result.append("           <td valign=top align=\"center\" bgcolor=\"" + this.bgcolor + "\" height=\"28\">\n");
        result.append("           <font font_color=\"" + this.font_Color + "\">\n");
        result.append("                   <table border=0 cellPadding=0 cellSpacing=0 >\n");
        result.append("                   <tbody>\n");
        result.append("                   <tr>\n");
        
        for (int i = 0; i < this.nav_Bar_Items.length; i++) {
            if (i != 0) {
                /* print separator */
                result.append("                           <td width=\"25\" valign=middle>&nbsp;&nbsp;&nbsp;<img height=\"20\" src=\"" + this.pixel_Seperator_Image + "\" width=1  align=middle>&nbsp;&nbsp;&nbsp;</td>\n");
            }
	    result.append("                           <td><a  href=\"" + this.nav_Bar_Items[i].link + "\">" + this.nav_Bar_Items[i].text + "</a></td>\n");
	    /*
            if (my_Page != null && my_Page.endsWith(this.nav_Bar_Items[i].link.substring(3))) {
                result.append("                           <td>" + this.nav_Bar_Items[i].text + "</td>\n");
            } else {
                result.append("                           <td><a  href=\"" + this.nav_Bar_Items[i].link + "\">" + this.nav_Bar_Items[i].text + "</a></td>\n");
            }
	    */
        }
        result.append("                   </tr></tbody></table></font>\n");
        result.append("           </td>\n");
        result.append("   </tr>\n");
        result.append("   </table>\n");
        return result.toString();
        
    }
    
    
}
