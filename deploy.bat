set tomcat=d:\work\tomcat
set app=d

call %tomcat%\bin\shutdown.bat
 
ping -n 10 localhost
 
rmdir %tomcat%\webapps\%app% /S /Q
del %tomcat%\webapps\%app%.war /Q
 
call %tomcat%\bin\startup.bat
 
mvn clean install