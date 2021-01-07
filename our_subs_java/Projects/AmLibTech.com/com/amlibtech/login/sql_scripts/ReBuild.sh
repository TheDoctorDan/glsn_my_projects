opt="--user=jboss --password=abc123"
opt=""

mysql $opt <installed_subsystems.sql
mysql $opt <subscriber_organization.sql
mysql $opt <subscriber_organization_portal.sql
mysql $opt <user.sql


mysql $opt <subscriber_organization_portal_subsystem.sql
