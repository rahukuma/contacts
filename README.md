Following are included in this project
1. A server storing contact in json format having name,email,phone and address.
2. POST/GET/PUT/DELETE request added.
3. Created test using testng for all the apis.Only single request has been added so far for testing of functional testing.
4. All contacts data are stored under BASE_DIR_TOMCAT/contacts.


What is pending :
1. API response pagination.
2. Test doesn't include many contacts CRUD.Need files and will take some time.

How to build :
1.Start tomcat server(either from here or from apache site with version 8)
2. Run maven command as : mvn clean install tomcat7:deploy

PS : Has been tested java7,maven3 and maven tomcat7 plugin.


