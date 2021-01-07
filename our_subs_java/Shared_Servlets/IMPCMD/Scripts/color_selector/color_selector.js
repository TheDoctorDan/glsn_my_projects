// Title: Color Selector 
// Description: returns selected Employing_Company_Id and Description

var oldColor;
var answer;

function color_selector(str_Color_Answer, str_Old_Color, base_URL) {
	oldColor=str_Old_Color;
	answer=str_Color_Answer;

	
	var q ="" + base_URL + "/scripts/color_selector/ColorEditor.html";
	var vWinCal = window.open(q,'Color Selector','height=350,width=390,dependent=yes,directories=no,location=no,menubar=no,resizable=yes,scrollbars=no,status=no,toolbar=no');

}

function setColor(a) {
	//document.getElementById("blah").bgColor=document.forms[0].elements[0].value=a;
	//if(a.substr(1,1) == "#")
		//document.Maint_Client_Form.division_Manager_Color.value=a;
	//else
		//document.Maint_Client_Form.division_Manager_Color.value="#" + a;
	if(a.substr(1,1) == "#")
		document.getElementById("blah2").value=a;
	else
		document.getElementById("blah2").value="#" + a;
	document.getElementById("blah").bgColor=a;
	oldColor=a;
}

setColor("FFFFFF");
