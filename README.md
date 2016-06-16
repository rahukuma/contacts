Following are included in this project
1. A server storing contact in json format having name,email,phone and address.
2. POST/GET/PUT/DELETE request added.
3. Created test using testng for all the apis.Only single request has been added so far for testing of functional testing.
4. All contacts data are stored under BASE_DIR_TOMCAT/contacts.


What is pending :
1. API response pagination.
2. Test doesn't include many contacts CRUD.Need files and will take some time.
3. Server is accessed on http protocol.Need to integrate SSL to server.

How to build :
1.Start tomcat server(either from here or from apache site with version 8)
2. Under url of pom file for maven tomcat7 plugin, host has to be changed from localhost to appropriate hostname , where tomcat is running. 
3. Run maven command as : mvn clean install tomcat7:deploy -Dmaven.test.skip=true
4. For deployment to heroku : mvn clean install heroku:deploy-war -Dmaven.test.skip=true
    For heroku : appName has to be changed accordingly in pom.xml OR
                  commandline argument can also be used as : -Dheroku.appName=myapp

PS : Has been tested java7,maven3 and maven tomcat7 plugin.


