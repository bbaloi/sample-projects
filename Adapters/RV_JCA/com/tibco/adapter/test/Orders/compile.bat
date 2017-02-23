set JAVAC=javac
set JAVA_SDK=C:\tibco\adapter\sdk4\java
set REP=C:\tibco\repository
set RV_DIR=C:\tibco\tibrv
set HOME=C:\Tibco\adapter\orders
set CP=.;%JAVA_SDK%\Maverick4.jar;%JAVA_SDK%\TIBRepoClient4.jar;%HOME%;%RV_DIR%\lib\tibrvj.jar;%JARHOME%\orders.jar
%JAVAC% -classpath %CP% OrdersTesterApp.java TimerListener.java OrdersReplyListener.java BillingListener.java OrdersUpdateListener.java 
