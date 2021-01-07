// Title: Time Selector 
// Description: returns selected time value as a string

var oldTime;
var time_Selector_Answer;

function time_selector(str_Time_Answer, str_Old_Time, base_URL) {
	oldTime=str_Old_Time;
	time_Selector_Answer=str_Time_Answer;

	

	var str_buffer = new String (


"<html>\n" +
"<head>\n" +
"<title>Time</title>\n" +
"<script language=\"javascript\">\n" +
"<!--\n" +
"// Old Time and Current Time\n" +
"	if (window.opener && window.opener.oldTime)\n" +
"		oldTime=window.opener.oldTime;\n" +
"	else\n" +
"		oldTime=\"12:00:00 AM\";\n" +
"	curTime=oldTime;\n" +
"	setCurHMSMFromCurTime();\n" +
"\n" +
"\n" +
"// Functions\n" +
"\n" +
"	function transferTime () {\n" +
"		setCurTimeFromCurHMSM();\n" +
"		if (window.opener){\n" +
"			window.opener." + str_Time_Answer + ".value=curTime;\n" +
"		}\n" +
"	}\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"// -->\n" +
"</script>\n" +
"<style type=\"text/css\">\n" +
"td {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt}\n" +
"input {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 8pt}\n" +
"</style>\n" +
"</head>\n" +
"<body bgcolor=\"White\" onLoad=\"setTimeFromValue(curTime);setPreTime(); \">\n" +
"<form>\n" +
"\n" +
"<table border=0 cellspacing=2 cellpadding=0>\n" +
"<tbody bgcolor=\"#4682B4\" >\n" +
"<tr>\n" +
"\n" +
"<td valign=top>\n" +
"\n" +
"   <table>\n" +
"   <tbody bgcolor=\"#4682B4\" >\n" +
"   <tr>\n" +
"   	<td>\n" +
"\n" +
"	   <table cellspacing=1 border=1>\n" +
"	   <tbody bgcolor=\"white\" >\n" +
"	   <tr>\n" +
"	   <td bgcolor=\"#87CEFA\" colspan=3 align=center>Hour</td>\n" +
"	   </tr>\n");

for(i=1;i<=12;i++){
	if((i-1)%3==0)
		str_buffer +="	   <tr>\n";
	if(i<10)
		hour="0" + i;
	else
		hour=i;

	str_buffer += "	       <td align=\"right\"> <a href=\"javascript:setCurHourFromValue('" + hour + "')\">" + hour + "</a> </td>\n";

	if((i-1)%3==2)
		str_buffer += "	   </tr>\n";

}

str_buffer += "	   </tbody>\n" +
"	   </table>\n" +
"	</td>\n" +
"	</tr>\n" +
"	<tr>\n" +
"	<td>\n" +
"		<table cellspacing=1 border=1>\n" +
"		<tbody bgcolor=\"white\" >\n" +
"		   <tr>\n" +
"		       <td bgcolor=\"#87CEFA\" align=center colspan=2>Meridian</td>\n" +
"		   </tr>\n" +
"		   <tr>\n" +
"		       <td align=\"center\"> <a href=\"javascript:setCurMeridianFromValue('AM')\">AM</a> </td>\n" +
"		       <td align=\"center\"> <a href=\"javascript:setCurMeridianFromValue('PM')\">PM</a> </td>\n" +
"		   </tr>\n" +
"		</tbody>\n" +
"		</table>\n" +
"\n" +
"	</td>\n" +
"	</tr>\n" +
"\n" +
"    </tbody>\n" +
"    </table>\n" +
"\n" +
"</td>\n" +
"\n" +
"\n" +
"<td valign=top>\n" +
"   <table cellspacing=1 border=1>\n" +
"   <tbody bgcolor=\"white\" >\n" +
"   <tr>\n" +
"   <td bgcolor=\"#87CEFA\" colspan=10 align=center>Minute</td>\n" +
"   </tr>\n";

for(i=0;i<=59;i++){
	if(i%10==0)
		str_buffer += "   <tr>\n";
	if(i<10)
		minute="0" + i;
	else
		minute=i;

	str_buffer += "       <td align=\"right\"> <a href=\"javascript:setCurMinuteFromValue('" + minute + "')\">" + minute + "</a> </td>\n";

	if(i%10==9)
		str_buffer +="   </tr>\n";
}

str_buffer += "   </tbody>\n" +
"   </table>\n" +
"</td>\n" +
"\n" +
"\n" +
"\n" +
"\n" +
"<td valign=top>\n" +
"   <table cellspacing=1 border=1>\n" +
"   <tbody bgcolor=\"white\" >\n" +
"   <tr>\n" +
"   <td bgcolor=\"#87CEFA\" colspan=10 align=center>Second</td>\n" +
"   </tr>\n";

for(i=0;i<=59;i++){
	if(i%10==0)
		str_buffer += "   <tr>\n";
	if(i<10)
		second="0" + i;
	else
		second=i;

	str_buffer += "       <td align=\"right\"> <a href=\"javascript:setCurSecondFromValue('" + second + "')\">" + second + "</a> </td>\n";
	if(i%10==9)
		str_buffer += "   </tr>\n";
}

str_buffer +="   </tbody>\n" +
"   </table>\n" +
"</td>\n" +
"\n" +
"\n" +
"\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>\n" +
"\n" +
"\n" +
"<table border=0 cellpadding=0 cellspacing=2 >\n" +
"<tbody>\n" +
"<tr>\n" +
"	<td width=40>\n" +
"	&nbsp;\n" +
"	</td>\n" +
"	<td valign=top align=right>\n" +
"		Time :<input id=\"hour\" type=\"text\" size=2 maxlength=2 value=\"12\" onChange=\"setFromIn()\">\n" +
"	</td>\n" +
"	<td>\n" +
"		: <input id=\"minute\" type=\"text\" size=2 maxlength=2 value=\"00\" onChange=\"setFromIn()\">\n" +
"	</td>\n" +
"	<td>\n" +
"		: <input id=\"second\" type=\"text\" size=2 maxlength=2 value=\"00\" onChange=\"setFromIn()\">\n" +
"	</td>\n" +
"	<td>\n" +
"		<input id=\"meridian\" type=\"text\" size=2 maxlength=2 value=\"AM\" onChange=\"setFromIn()\">\n" +
"	</td>\n" +
"	<td>\n" +
"	&nbsp;\n" +
"	</td>\n" +
"\n" +
"	<td colspan=5 align=center valign=bottom>\n" +
"	<input type=\"button\" value=\"&nbsp;&nbsp;&nbsp;OK&nbsp;&nbsp;&nbsp;\" onClick=\"transferTime();window.close()\">\n" +
"	<input type=\"button\" value=\"Cancel\" onClick=\"window.close()\">\n" +
"	<input type=\"button\" value=\"&nbsp;&nbsp;Now&nbsp;&nbsp;\" onClick=\"setFromClock()\">\n" +
"	</td>\n" +
"</tr>\n" +
"</tbody>\n" +
"</table>\n" +
"\n" +
"</form>\n" +
"</body>\n" +
"</html>\n";






	//var vWinCal = window.open("","Time Selector", "width=500,height=250,status=no,resizable=yes,top=200,left=200");
	var vWinCal = window.open("","Time Selector", "width=500,height=250");

	vWinCal.opener = self;
	var calc_doc = vWinCal.document;
	calc_doc.write (str_buffer);
	calc_doc.close();

}



// Functions


	function setTimeFromValue(value) {
		curTime=value;
		setCurHMSMFromCurTime();
	}



	function setCurHMSMFromCurTime () {
		curHour=curTime.substr(0,2);
		curMinute=curTime.substr(3,2);
		curSecond=curTime.substr(6,2);
		curMeridian=curTime.substr(9,2);
	}

	function setCurTimeFromCurHMSM () {
		curTime=curHour + ":" + curMinute + ":" + curSecond + " " + curMeridian;
	}




	function setCurHourFromValue(value) {
		curHour=value;
		update_doc();
	}

	function setCurMinuteFromValue(value) {
		curMinute=value;
		update_doc();
	}

	function setCurSecondFromValue(value) {
		curSecond=value;
		update_doc();
	}

	function setCurMeridianFromValue(value) {
		curMeridian=value;
		update_doc();
	}



	function setFromIn () {
		curHour=document.getElementById("hour").value;
		curMinute=document.getElementById("minute").value;
		curSecond=document.getElementById("second").value;
		curMeridian=document.getElementById("minute").value;
		setCurTimeFromCurHMSM();
	}

	function setFromClock () {
	        var dt_datetime = new Date();
		var hour =dt_datetime.getHours();
		if(hour>=12){
			curMeridian="PM";
			hour -= 12;
		}else{
			curMeridian="AM";
		}
		if(hour==0)
			hour=12;
		if(hour<10)
			curHour="0" + hour;
		else
			curHour=hour;
		if(dt_datetime.getMinutes() <10)
			curMinute="0" + dt_datetime.getMinutes();
		else
			curMinute=dt_datetime.getMinutes();
		if(dt_datetime.getSeconds() <10)
			curSecond="0" + dt_datetime.getSeconds()
		else
			curSecond=dt_datetime.getSeconds()
		update_doc();
	}


	function update_doc() {
		document.getElementById("hour").value=curHour;
		document.getElementById("minute").value=curMinute;
		document.getElementById("second").value=curSecond;
		document.getElementById("meridian").value=curMeridian;
		setCurTimeFromCurHMSM();
	}

	function setPreTime () {
		document.getElementById("hour").value=curHour;
		document.getElementById("minute").value=curMinute;
		document.getElementById("second").value=curSecond;
		document.getElementById("meridian").value=curMeridian;
	}

