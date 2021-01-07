/*
 * SQLUtil.java
 *
 * 
 */

package com.amlibtech.util;

import java.util.*;
import java.sql.*;

public class SQLUtil{

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

    public static synchronized String getHtmlRows(ResultSet results)
    throws SQLException{
        StringBuffer htmlRows = new StringBuffer();
        ResultSetMetaData metaData = results.getMetaData();
        int columnCount = metaData.getColumnCount();

        htmlRows.append("<tr>");
        for (int i = 1; i <= columnCount; i++)
            htmlRows.append("<td><b>" + metaData.getColumnName(i) + "</td>");
        htmlRows.append("</tr>");

        while (results.next()){
            htmlRows.append("<tr>");
            for (int i = 1; i <= columnCount; i++)
                htmlRows.append("<td>" + results.getString(i) + "</td>");
        }
        htmlRows.append("</tr>");
        return htmlRows.toString();
    }

    public static synchronized String encode(String s){
        if (s == null) return s;
        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < sb.length(); i++){
            char ch = sb.charAt(i);
            if (ch == 39){  // 39 is the ASCII code for an apostrophe
                sb.insert(i++, "'");
            }
            if (ch == 92){  // 92 is the ASCII code for a backslash
                sb.insert(i++, "\\");
            }
        }
        return sb.toString();
    }

}

