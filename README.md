# DAOAutomation
Automate the Spring JDBC code generation with Oracle.
	Make sure that ojdbc6.jar file is downloaded and installed in the local maven repository. 
	Sample command for installing is below:
		mvn install:install-file -Dfile=C:\Users\TRC\Downloads\ojdbc6.jar 
		-DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0.3 -DpomFile=C:\Users\TRC\git\DaoAutomation\pom.xml
