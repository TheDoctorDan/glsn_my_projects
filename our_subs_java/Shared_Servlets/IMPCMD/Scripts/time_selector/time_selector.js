// Title: Time Selector 
// Description: returns selected time value as a string

var wasOldTime;
var time_Selector_Answer;


function time_selector(str_Time_Answer, str_Old_Time, base_URL) {
	wasOldTime
	=
	str_Old_Time;
	
	time_Selector_Answer
	=
	str_Time_Answer;




	msg
	=
	window.open(
	"",
	"TimeSelector",
	"width=500,height=250,resizable"
	);






	msg.document.write("<html>\n");
	msg.document.write("<head>\n");
	msg.document.write("<title>Time</title>\n");
	msg.document.write("<script language=\"javascript\">\n");
	msg.document.write("<!--\n");
	msg.document.write("// Old Time and Current Time\n");
//	msg.document.write("	if (window.opener && window.opener.wasOldTime)\n");
//	msg.document.write("	if (window.opener.wasOldTime)\n");
//	msg.document.write("		oldTime=window.opener.wasOldTime;\n");
//	msg.document.write("	else\n");
//	msg.document.write("		oldTime=\"12:00:00 AM\";\n");
	msg.document.write("		oldTime=\"" + wasOldTime + "\";\n");
	msg.document.write("	curTime=oldTime;\n");
	msg.document.write("	setCurHMSMFromCurTime();\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("// Functions\n");
	msg.document.write("\n");
	msg.document.write("	function setTimeFromValue(value) {\n");
	msg.document.write("		curTime=value;\n");
	msg.document.write("		setCurHMSMFromCurTime();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("	function setCurHMSMFromCurTime () {\n");
	msg.document.write("		curHour=curTime.substr(0,2);\n");
	msg.document.write("		curMinute=curTime.substr(3,2);\n");
	msg.document.write("		curSecond=curTime.substr(6,2);\n");
	msg.document.write("		curMeridian=curTime.substr(9,2);\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function setCurTimeFromCurHMSM () {\n");
	msg.document.write("		curTime=curHour + \":\" + curMinute + \":\" + curSecond + \" \" + curMeridian;\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("	function setCurHourFromValue(value) {\n");
	msg.document.write("		curHour=value;\n");
	msg.document.write("		update_Doc();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function setCurMinuteFromValue(value) {\n");
	msg.document.write("		curMinute=value;\n");
	msg.document.write("		update_Doc();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function setCurSecondFromValue(value) {\n");
	msg.document.write("		curSecond=value;\n");
	msg.document.write("		update_Doc();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function setCurMeridianFromValue(value) {\n");
	msg.document.write("		curMeridian=value;\n");
	msg.document.write("		update_Doc();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("	function setFromIn () {\n");
	msg.document.write("		curHour=document.getElementById(\"hour\").value;\n");
	msg.document.write("		curMinute=document.getElementById(\"minute\").value;\n");
	msg.document.write("		curSecond=document.getElementById(\"second\").value;\n");
	msg.document.write("		curMeridian=document.getElementById(\"minute\").value;\n");
	msg.document.write("		setCurTimeFromCurHMSM();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function setFromClock () {\n");
	msg.document.write("	        var dt_datetime = new Date();\n");
	msg.document.write("		var hour =dt_datetime.getHours();\n");
	msg.document.write("		if(hour>=12){\n");
	msg.document.write("			curMeridian=\"PM\";\n");
	msg.document.write("			hour -= 12;\n");
	msg.document.write("		}else{\n");
	msg.document.write("			curMeridian=\"AM\";\n");
	msg.document.write("		}\n");
	msg.document.write("		if(hour==0)\n");
	msg.document.write("			hour=12;\n");
	msg.document.write("		if(hour<10)\n");
	msg.document.write("			curHour=\"0\" + hour;\n");
	msg.document.write("		else\n");
	msg.document.write("			curHour=hour;\n");
	msg.document.write("		if(dt_datetime.getMinutes() <10)\n");
	msg.document.write("			curMinute=\"0\" + dt_datetime.getMinutes();\n");
	msg.document.write("		else\n");
	msg.document.write("			curMinute=dt_datetime.getMinutes();\n");
	msg.document.write("		if(dt_datetime.getSeconds() <10)\n");
	msg.document.write("			curSecond=\"0\" + dt_datetime.getSeconds();\n");
	msg.document.write("		else\n");
	msg.document.write("			curSecond=dt_datetime.getSeconds();\n");
	msg.document.write("		update_Doc();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("	function transferTime () {\n");
	msg.document.write("		setCurTimeFromCurHMSM();\n");
	msg.document.write("		if (window.opener){\n");
	msg.document.write("			window.opener." + str_Time_Answer + ".value=curTime;\n");
	msg.document.write("		}\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function update_Doc() {\n");
	msg.document.write("		document.getElementById(\"hour\").value=curHour;\n");
	msg.document.write("		document.getElementById(\"minute\").value=curMinute;\n");
	msg.document.write("		document.getElementById(\"second\").value=curSecond;\n");
	msg.document.write("		document.getElementById(\"meridian\").value=curMeridian;\n");
	msg.document.write("		setCurTimeFromCurHMSM();\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("	function setPreTime (value) {\n");
	msg.document.write("		curTime=value;\n");
	msg.document.write("		setCurHMSMFromCurTime();\n");
	msg.document.write("		document.getElementById(\"hour\").value=curHour;\n");
	msg.document.write("		document.getElementById(\"minute\").value=curMinute;\n");
	msg.document.write("		document.getElementById(\"second\").value=curSecond;\n");
	msg.document.write("		document.getElementById(\"meridian\").value=curMeridian;\n");
	msg.document.write("	}\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("// -->\n");
	msg.document.write("</script>\n");
	msg.document.write("<style type=\"text/css\">\n");
	msg.document.write("td {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt}\n");
	msg.document.write("input {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt}\n");
	msg.document.write("</style>\n");
	msg.document.write("</head>\n");
	msg.document.write("<body bgcolor=\"White\" onLoad=\"setCurHMSMFromCurTime();setPreTime()\">\n");
	msg.document.write("<form>\n");
	msg.document.write("\n");
	msg.document.write("<table border=0 cellspacing=2 cellpadding=0>\n");
	msg.document.write("<tbody bgcolor=\"#4682B4\" >\n");
	msg.document.write("<tr>\n");
	msg.document.write("\n");
	msg.document.write("<td valign=top>\n");
	msg.document.write("\n");
	msg.document.write("   <table>\n");
	msg.document.write("   <tbody bgcolor=\"#4682B4\" >\n");
	msg.document.write("   <tr>\n");
	msg.document.write("   	<td>\n");
	msg.document.write("\n");
	msg.document.write("	   <table cellspacing=1 border=1>\n");
	msg.document.write("	   <tbody bgcolor=\"white\" >\n");
	msg.document.write("	   <tr>\n");
	msg.document.write("	   <td bgcolor=\"#87CEFA\" colspan=3 align=center>Hour</td>\n");
	msg.document.write("	   </tr>\n");

	for(i=1;i<=12;i++){
		if((i-1)%3==0)
			msg.document.write("	   <tr>\n");
		if(i<10)
			hour="0" + i;
		else
			hour=i;

			msg.document.write("	       <td align=\"right\"> <a href=\"javascript:setCurHourFromValue('" + hour + "')\"><font color=\"black\" face=\"tahoma, verdana\" size=2>" + hour + "</font></a> </td>\n");

		if((i-1)%3==2)
			msg.document.write("	   </tr>\n");

	}


	msg.document.write("	   </table>\n");
	msg.document.write("	</td>\n");
	msg.document.write("	</tr>\n");
	msg.document.write("	<tr>\n");
	msg.document.write("	<td>\n");
	msg.document.write("		<table cellspacing=1 border=1>\n");
	msg.document.write("		<tbody bgcolor=\"white\" >\n");
	msg.document.write("		   <tr>\n");
	msg.document.write("		       <td bgcolor=\"#87CEFA\" align=center colspan=2>Meridian</td>\n");
	msg.document.write("		   </tr>\n");
	msg.document.write("		   <tr>\n");
	msg.document.write("		       <td align=\"center\"> <a href=\"javascript:setCurMeridianFromValue('AM')\"><font color=\"black\" face=\"tahoma, verdana\" size=2>AM</font></a> </td>\n");
	msg.document.write("		       <td align=\"center\"> <a href=\"javascript:setCurMeridianFromValue('PM')\"><font color=\"black\" face=\"tahoma, verdana\" size=2>PM</font></a> </td>\n");
	msg.document.write("		   </tr>\n");
	msg.document.write("		</tbody>\n");
	msg.document.write("		</table>\n");
	msg.document.write("\n");
	msg.document.write("	</td>\n");
	msg.document.write("	</tr>\n");
	msg.document.write("\n");
	msg.document.write("    </tbody>\n");
	msg.document.write("    </table>\n");
	msg.document.write("\n");
	msg.document.write("</td>\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("<td valign=top>\n");
	msg.document.write("   <table cellspacing=1 border=1>\n");
	msg.document.write("   <tbody bgcolor=\"white\" >\n");
	msg.document.write("   <tr>\n");
	msg.document.write("   <td bgcolor=\"#87CEFA\" colspan=10 align=center>Minute</td>\n");
	msg.document.write("   </tr>\n");

	for(i=0;i<=59;i++){
		if(i%10==0)
			msg.document.write("   <tr>\n");
		if(i<10)
			minute="0" + i;
		else
			minute=i;

			msg.document.write("       <td align=\"right\"> <a href=\"javascript:setCurMinuteFromValue('" + minute + "')\"><font color=\"black\" face=\"tahoma, verdana\" size=2>" + minute + "</font></a> </td>\n");

		if(i%10==9)
			msg.document.write("   </tr>\n");
	}

	msg.document.write("   </table>\n");
	msg.document.write("</td>\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("<td valign=top>\n");
	msg.document.write("   <table cellspacing=1 border=1>\n");
	msg.document.write("   <tbody bgcolor=\"white\" >\n");
	msg.document.write("   <tr>\n");
	msg.document.write("   <td bgcolor=\"#87CEFA\" colspan=10 align=center>Second</td>\n");
	msg.document.write("   </tr>\n");

	for(i=0;i<=59;i++){
		if(i%10==0)
			msg.document.write("   <tr>\n");
		if(i<10)
			second="0" + i;
		else
			second=i;

		msg.document.write("       <td align=\"right\"> <a href=\"javascript:setCurSecondFromValue('" + second + "')\"><font color=\"black\" face=\"tahoma, verdana\" size=2>" + second + "</font></a> </td>\n");
		if(i%10==9)
			msg.document.write("   </tr>\n");
	}

	msg.document.write("   </table>\n");
	msg.document.write("</td>\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("</tr>\n");
	msg.document.write("</tbody>\n");
	msg.document.write("</table>\n");
	msg.document.write("\n");
	msg.document.write("\n");
	msg.document.write("<table border=0 cellpadding=0 cellspacing=2 >\n");
	msg.document.write("<tbody>\n");
	msg.document.write("<tr>\n");
	msg.document.write("	<td width=40>\n");
	msg.document.write("	&nbsp;\n");
	msg.document.write("	</td>\n");
	msg.document.write("	<td valign=top align=right>\n");

	wasCurHour=wasOldTime.substr(0,2);
	wasCurMinute=wasOldTime.substr(3,2);
	wasCurSecond=wasOldTime.substr(6,2);
	wasCurMeridian=wasOldTime.substr(9,2);

	msg.document.write("		Time :<input name=\"hour\" id=\"hour\" type=\"text\" size=2 maxlength=2 value=\"" + wasCurHour + "\" onChange=\"setFromIn()\">\n");
	msg.document.write("	</td>\n");
	msg.document.write("	<td>\n");
	msg.document.write("		: <input name=\"minute\" id=\"minute\" type=\"text\" size=2 maxlength=2 value=\"" + wasCurMinute + "\" onChange=\"setFromIn()\">\n");
	msg.document.write("	</td>\n");
	msg.document.write("	<td>\n");
	msg.document.write("		: <input name=\"second\" id=\"second\" type=\"text\" size=2 maxlength=2 value=\"" + wasCurSecond + "\" onChange=\"setFromIn()\">\n");
	msg.document.write("	</td>\n");
	msg.document.write("	<td>\n");
	msg.document.write("		<input name=\"meridian\" id=\"meridian\" type=\"text\" size=2 maxlength=2 value=\"" + wasCurMeridian + "\" onChange=\"setFromIn()\">\n");
	msg.document.write("	</td>\n");
	msg.document.write("	<td>\n");
	msg.document.write("	&nbsp;\n");
	msg.document.write("	</td>\n");
	msg.document.write("\n");
	msg.document.write("	<td colspan=5 align=center valign=bottom>\n");
	msg.document.write("	<input type=\"button\" value=\"&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;\" onClick=\"transferTime();window.close()\">\n");
	msg.document.write("	<input type=\"button\" value=\"Cancel\" onClick=\"window.close()\">\n");
	msg.document.write("	<input type=\"button\" value=\"&nbsp;&nbsp;Now&nbsp;&nbsp;\" onClick=\"setFromClock()\">\n");
	msg.document.write("	</td>\n");
	msg.document.write("</tr>\n");
	msg.document.write("</tbody>\n");
	msg.document.write("</table>\n");
	msg.document.write("\n");
	msg.document.write("</form>\n");
	msg.document.write("</body>\n");
	msg.document.write("</html>\n");






}


