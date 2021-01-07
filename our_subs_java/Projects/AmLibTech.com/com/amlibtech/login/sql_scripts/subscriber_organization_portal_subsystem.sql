USE TMI;

DROP TABLE IF EXISTS subscriber_organization_portal_subsystem;


CREATE TABLE `subscriber_organization_portal_subsystem` (
	`subscriber_Organization_Id` varchar(30) NOT NULL default '',
	`portal_Name` varchar(30) NOT NULL default '',
	`subsystem_Id` varchar(30) NOT NULL default '',
        PRIMARY KEY  (`subscriber_Organization_Id`, `portal_Name`, `subsystem_Id`)
) TYPE=InnoDB;


INSERT INTO subscriber_organization_portal_subsystem VALUES ('TMI','Office','Company_Management');
INSERT INTO subscriber_organization_portal_subsystem VALUES ('TMI','Office','Security_Management');
INSERT INTO subscriber_organization_portal_subsystem VALUES ('TMI','Office','Site_Management');


