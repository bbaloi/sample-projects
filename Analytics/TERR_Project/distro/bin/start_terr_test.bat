set JAVA_HOME=C:\Program Files (x86)\Java\jdk1.7.0_07
set AS_HOME=C:\tibco\as\2.0
set TERR_HOME=C:\Program Files\TIBCO\terr15
set CLASSPATH=C:\tibco\as\2.0\lib\as-common.jar;C:\Program Files\TIBCO\terr15\library\terrJava\java\terrJava.jar;%CLASSPATH%;
set PATH=%JAVA_HOME%\bin;C:\Program Files\TIBCO\terr15\bin\i386;C:\tibco\as\2.0\lib;%PATH%
REM set PATH=%JAVA_HOME%\bin;C:\Program Files\TIBCO\terr15\bin\i386

echo %PATH%

C:\tibco_be\tibcojre\1.7.0\bin\java.exe -classpath .;C:\tibco\as\2.0\lib\as-common.jar;"C:\Program Files\TIBCO\terr15\library\terrJava\java\terrJava.jar";C:\Eclipse\workspace\TERR_Project\distro\lib\terr.events.jar com.tibco.terr.events.tests.LinearModelTest_2