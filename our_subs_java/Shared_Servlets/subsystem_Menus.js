// subsystem_Menus


function create_asp_Management_Menu() {

  asp_Management_Menu = new jsDOMenu(190);
  with (asp_Management_Menu){
    addMenuItem(new menuItem("Subscriber Organizations", "", root +"/Launcher_Servlet?action=Start&command=/Asp100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Portals", "", root +"/Launcher_Servlet?action=Start&command=/Asp120&parent_Process_Id=0"));
    addMenuItem(new menuItem("Subsystems", "", root +"/Launcher_Servlet?action=Start&command=/Asp200&parent_Process_Id=0"));
    addMenuItem(new menuItem("Programs", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }

    return asp_Management_Menu;
}

function create_system_Admin_Menu() {

    system_Admin_Menu = new jsDOMenu(150);
    with (system_Admin_Menu){
        addMenuItem(new menuItem("Users", "", root +"/Launcher_Servlet?action=Start&command=/Sm100&parent_Process_Id=0"));
        addMenuItem(new menuItem("Jfiles", "", root +"/Launcher_Servlet?action=Start&command=/Jfiles&parent_Process_Id=0"));
        addMenuItem(new menuItem("Security Profiles", "", root +"/Launcher_Servlet?action=Start&command=/Secm100&parent_Process_Id=0"));
    }

    return system_Admin_Menu;
}

function create_ALT_Menu() {

    ALT_Menu = new jsDOMenu(140);
    with (ALT_Menu){
        addMenuItem(new menuItem("Logout", "", root +"/LogoutServlet"));
        addMenuItem(new menuItem("Task List", "", root +"/Launcher_Servlet?action=Start&command=/Cmp100&parent_Process_Id=0"));
        addMenuItem(new menuItem("Run Task", "", root +"/Launcher_Servlet?action=Start&command=/Run_Task&parent_Process_Id=0"));
    }

    return ALT_Menu;
}






function create_enroll_Now_Admin_Menu() {

  enroll_Now_Admin_Menu = new jsDOMenu(200);
  with (enroll_Now_Admin_Menu) {
    //studio, dept, course, semester, teacher, schedule
    addMenuItem(new menuItem("Studios", "", root +"/Launcher_Servlet?action=Start&command=/Ena100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Departments", "", root +"/Launcher_Servlet?action=Start&command=/Ena200&parent_Process_Id=0"));
    addMenuItem(new menuItem("Disciplines", "", root +"/Launcher_Servlet?action=Start&command=/Ena210&parent_Process_Id=0"));
    addMenuItem(new menuItem("Courses", "", root +"/Launcher_Servlet?action=Start&command=/Ena300&parent_Process_Id=0"));
    addMenuItem(new menuItem("CoRequisites", "", root +"/Launcher_Servlet?action=Start&command=/Ena301&parent_Process_Id=0"));
    addMenuItem(new menuItem("PreRequisites", "", root +"/Launcher_Servlet?action=Start&command=/Ena302&parent_Process_Id=0"));
    addMenuItem(new menuItem("Registration Periods", "", root +"/Launcher_Servlet?action=Start&command=/Ena500&parent_Process_Id=0"));
    addMenuItem(new menuItem("Teachers", "", root +"/Launcher_Servlet?action=Start&command=/Ena600&parent_Process_Id=0"));
    addMenuItem(new menuItem("Schedule", "", root +"/Launcher_Servlet?action=Start&command=/Ena700&parent_Process_Id=0"));
    //addMenuItem(new menuItem("Quick Labels", "", root +"/Launcher_Servlet?action=Start&command=/Quick_Labels&parent_Process_Id=0"));
  }

  	//enroll_Now_Admin_Menu.items.Departments.showIcon("icon3", "icon1", "icon2");

        return enroll_Now_Admin_Menu;
}


function create_enroll_Now_Billable_Menu() {

  enroll_Now_Billable_Menu = new jsDOMenu(200);
  with (enroll_Now_Billable_Menu) {
    // fees, discounts, scholarships, products and services
    addMenuItem(new menuItem("Enrollment Fees", "", root +"/Launcher_Servlet?action=Start&command=/Ena503&parent_Process_Id=0"));
    addMenuItem(new menuItem("Tuition Discounts", "", root +"/Launcher_Servlet?action=Start&command=/Ena502&parent_Process_Id=0"));
    addMenuItem(new menuItem("Tuition Scholarships", "", root +"/Launcher_Servlet?action=Start&command=/Ena504&parent_Process_Id=0"));
    addMenuItem(new menuItem("Products", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Services", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }
  return enroll_Now_Billable_Menu;
}


function create_enroll_Now_Registration_Support_Menu() {
    enroll_Now_Registration_Support_Menu = new jsDOMenu(250);
    with(enroll_Now_Registration_Support_Menu ) {
        addMenuItem(new menuItem("Customer Control Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Cs001&parent_Process_Id=0"));
        addMenuItem(new menuItem("AR Terms Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Ar830&parent_Process_Id=0"));
        addMenuItem(new menuItem("Customer Type Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Cs840&parent_Process_Id=0"));
        addMenuItem(new menuItem("Customer Totaling Group Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Cs830&parent_Process_Id=0"));
    }
    return enroll_Now_Registration_Support_Menu;
}


function create_enroll_Now_Registration_Menu() {

  enroll_Now_Registration_Menu = new jsDOMenu(130);
  with (enroll_Now_Registration_Menu) {
    // register
    addMenuItem(new menuItem("Register", "", root +"/Launcher_Servlet?action=Start&command=/Eno210&parent_Process_Id=0"));
    addMenuItem(new menuItem("Course Offering Status Display", "", root +"/Launcher_Servlet?action=Start&command=/Eno400&parent_Process_Id=0"));
    addMenuItem(new menuItem("Support Files", "support_Files", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }

  enroll_Now_Registration_Support_Menu = create_enroll_Now_Registration_Support_Menu();
  enroll_Now_Registration_Menu.items.support_Files.setSubMenu(enroll_Now_Registration_Support_Menu);
  return enroll_Now_Registration_Menu;
}


function create_enroll_Now_Entities_Support_Menu() {
    enroll_Now_Entities_Support_Menu = new jsDOMenu(300);
    with(enroll_Now_Entities_Support_Menu ) {
        // Entity Control Maintenance, Person Contact Type Maintenance
        addMenuItem(new menuItem("Entity Control Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Enty001&parent_Process_Id=0"));
        addMenuItem(new menuItem("Enroll Now Office Control Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Eno001&parent_Process_Id=0"));
        addMenuItem(new menuItem("Organization Type Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Enty010&parent_Process_Id=0"));
        addMenuItem(new menuItem("Organization Contact Type Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Enty011&parent_Process_Id=0"));
    }
    return enroll_Now_Entities_Support_Menu;
}

function create_enroll_Now_Entities_Menu() {

  enroll_Now_Entities_Menu = new jsDOMenu(130);
  with (enroll_Now_Entities_Menu ) {
    // organization, person, employee, vendor, customer, student, teacher
    addMenuItem(new menuItem("Organizations", "", root +"/Launcher_Servlet?action=Start&command=/Enty300&parent_Process_Id=0"));
    addMenuItem(new menuItem("People", "", root +"/Launcher_Servlet?action=Start&command=/Enty100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Customers", "", root +"/Launcher_Servlet?action=Start&command=/Cs100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Families", "", root +"/Launcher_Servlet?action=Start&command=/Enty200&parent_Process_Id=0"));
    addMenuItem(new menuItem("Students", "", root +"/Launcher_Servlet?action=Start&command=/Eno100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Vendors", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Employees", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Teachers", "", root +"/Launcher_Servlet?action=Start&command=/Ena600&parent_Process_Id=0"));
    addMenuItem(new menuItem("Support Files", "support_Files", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }

  enroll_Now_Entities_Support_Menu = create_enroll_Now_Entities_Support_Menu();
  enroll_Now_Entities_Menu.items.support_Files.setSubMenu(enroll_Now_Entities_Support_Menu);
  return enroll_Now_Entities_Menu;
}




function create_enroll_Now_Accounts_Receivable_Menu() {

  enroll_Now_Accounts_Receivable_Menu = new jsDOMenu(180);
  with (enroll_Now_Accounts_Receivable_Menu ) {
    // AR, invoice, payment, credit card, time-payments
    addMenuItem(new menuItem("Credit Cards", "credit_Cards", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Invoices", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Payments", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Time Payments", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }

  enroll_Now_Accounts_Receivable_CC_Menu = create_enroll_Now_Accounts_Receivable_CC_Menu();
  enroll_Now_Accounts_Receivable_Menu.items.credit_Cards.setSubMenu(enroll_Now_Accounts_Receivable_CC_Menu);

  return enroll_Now_Accounts_Receivable_Menu;
}

function create_enroll_Now_Accounts_Receivable_CC_Menu() {

    enroll_Now_Accounts_Receivable_CC_Menu = new jsDOMenu(250);
    with (enroll_Now_Accounts_Receivable_CC_Menu ) {
        addMenuItem(new menuItem("Merchant Accounts", "", root +"/Launcher_Servlet?action=Start&command=/Arcc100&parent_Process_Id=0"));
        addMenuItem(new menuItem("Merchant Account Routings", "", root +"/Launcher_Servlet?action=Start&command=/Arcc101&parent_Process_Id=0"));
    }
    return enroll_Now_Accounts_Receivable_CC_Menu;
}





function create_enroll_Now_Daily_Menu() {

  enroll_Now_Daily_Menu = new jsDOMenu(130);
  with (enroll_Now_Daily_Menu ) {
    // attendance, makup classes, injury tracking
    addMenuItem(new menuItem("Attendance", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Makeup Classes", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Injury Tracking", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }
  return enroll_Now_Daily_Menu;
}


function create_enroll_Now_Eval_Menu() {

  enroll_Now_Eval_Menu = new jsDOMenu(300);
  with (enroll_Now_Eval_Menu ) {
    addMenuItem(new menuItem("Placement Evaluation", "", root +"/Launcher_Servlet?action=Start&command=/Ene100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Evaluation Registration Periods", "", root +"/Launcher_Servlet?action=Start&command=/Ene001&parent_Process_Id=0"));
    addMenuItem(new menuItem("Teacher Placement Eval Status", "", root +"/Launcher_Servlet?action=Start&command=/Ene200&parent_Process_Id=0"));
    addMenuItem(new menuItem("Single Class Evaluation", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Proficiency Reporting", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }
  return enroll_Now_Eval_Menu;
}


function create_enroll_Now_Performance_Menu() {

  enroll_Now_Performance_Menu = new jsDOMenu(250);
  with (enroll_Now_Performance_Menu ) {
    // performances
    addMenuItem(new menuItem("Performable Show", "", root +"/Launcher_Servlet?action=Start&command=/Enp100&parent_Process_Id=0"));
    addMenuItem(new menuItem("Performable Show Characters", "", root +"/Launcher_Servlet?action=Start&command=/Enp101&parent_Process_Id=0"));
    addMenuItem(new menuItem("Venues", "", root +"/Launcher_Servlet?action=Start&command=/Enp700&parent_Process_Id=0"));
    addMenuItem(new menuItem("Performances", "", root +"/Launcher_Servlet?action=Start&command=/Enp200&parent_Process_Id=0"));
  }
  return enroll_Now_Performance_Menu;
}


function create_enroll_Now_Reports_Menu() {

  enroll_Now_Reports_Menu = new jsDOMenu(130);
  with (enroll_Now_Reports_Menu ) {
    // reports
    addMenuItem(new menuItem("Reports", "", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
  }
  return enroll_Now_Reports_Menu;
}




function create_finance_Menu() {

  finance_Menu = new jsDOMenu(180);
  with (finance_Menu){
    addMenuItem(new menuItem("Company Maintenance", "", root +"/Launcher_Servlet?action=Start&command=/Cmp100&parent_Process_Id=0"));
    addMenuItem(new menuItem("General Ledger", "gl", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
        addMenuItem(new menuItem("Accounts Receivable", "ar", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));
    addMenuItem(new menuItem("Accounts Payable", "ap", root +"/Launcher_Servlet?action=Start&command=/nyi&parent_Process_Id=0"));


    addMenuItem(new menuItem("cdw",  "", "code:window.open('http://www.cdw.com', '_blank')"));
    addMenuItem(new menuItem("yahoo",     "", "http://maps.yahoo.com/maps_result?addr=7050+W.+Wilson&csz=60706&country=us&new=1&name=&qty=\" target='_blank'\""));
  }

    enroll_Now_Accounts_Receivable_Menu = create_enroll_Now_Accounts_Receivable_Menu();
    finance_Menu.items.ar.setSubMenu(enroll_Now_Accounts_Receivable_Menu);
    finance_Menu.items.ar.showIcon("icon3", "icon1", "icon2");
    
    return finance_Menu;
}


