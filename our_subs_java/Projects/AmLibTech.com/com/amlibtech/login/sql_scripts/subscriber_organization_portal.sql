USE ALTASP;

DROP TABLE IF EXISTS subscriber_organization_portal;



CREATE TABLE `subscriber_organization_portal` (
	`subscriber_Organization_Id` varchar(30) NOT NULL default '',
	`portal_Name` varchar(30) NOT NULL default '',
	`destination_Url` varchar(100) NOT NULL default '',
	`menu_Js_Path` varchar(50) NOT NULL default '',
	`login_Method` enum('User', 'Customer', 'Vendor') NOT NULL default 'User',
	`web_Root` varchar(50) NOT NULL default '',
	`css_Root` varchar(50) NOT NULL default '',
	`use_Std_Css_Theme` bool NOT NULL,
	`custom_Css_Theme_Name` varchar(40) NOT NULL default '',
        PRIMARY KEY  (`subscriber_Organization_Id`, `portal_Name`)
) TYPE=InnoDB;


