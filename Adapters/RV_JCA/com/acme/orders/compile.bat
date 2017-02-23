set JAVAC=C:\util\java\bin\javac
set JAVA_SDK=C:\tibco\adapter\sdk4\java
set REP=C:\tibco\repository
set RV_DIR=C:\tibco\tibrv
set JAVA_EXAMPLES=C:\TelcoAdapters\Guidelines\examples
set CP=.;%JAVA_SDK%\Maverick4.jar;%JAVA_SDK%\jars\TIBRepoClient4.jar;%JAVA_EXAMPLES%\orders;%RV_DIR%\lib\tibrvj.jar
%JAVAC% -classpath %CP% AccountInfo.java OrderInfo.java OrdersException.java BillingListener.java OrdersApp.java
