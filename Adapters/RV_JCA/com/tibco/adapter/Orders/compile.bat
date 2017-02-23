set JAVAC=javac
set JAVA_SDK=C:\tibco\adapter\sdk4\java
set REP=C:\tibco\repository
set RV_DIR=C:\tibco\tibrv
set HOME=C:\Tibco\adapter
set CP=.;%JAVA_SDK%\Maverick4.jar;%REP%\jars\TIBRepoClient4.jar;%HOME%\orders;%RV_DIR%\lib\tibrvj.jar
%JAVAC% -classpath %CP% AdapterBillingListener.java TibOrdersAdapter.java OperationImpl.java TibOrdersApp.java DataMapper.java OrdersListener.java myHawkAgent.java ORDERS_AeErrors_en_US.java StopListener.java
