
set CLASSPATH=%CLASSPATH%;..\lib\jpcap.jar
set PATH=C:\Program Files\Java\jdk1.6.0_02\bin;%PATH%;..\lib\Jpcap.dll

java -jar ../lib/tibco-sip-sniffer.jar -props_file ../conf/sip_sniffer.properties