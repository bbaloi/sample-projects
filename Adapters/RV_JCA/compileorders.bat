call env
cd com\acme\Orders
if ERRORLEVEL 1 GOTO EXCEPTION
call compile
cd %ADHOME%
jar cvf orders.jar com\acme\orders\*.class

cd .\com\tibco\adapter\Orders
call compile
if ERRORLEVEL 1 GOTO EXCEPTION
cd %ADHOME%
jar uvf orders.jar com\tibco\adapter\Orders\*.class
jar uvf orders.jar com\tibco\adapter\Orders\*.properties

cd .\com\tibco\adapter\test\Orders
call compile
if ERRORLEVEL 1 GOTO EXCEPTION
cd %ADHOME%
jar uvf orders.jar com\tibco\adapter\test\Orders\*.class
jar uvf orders.jar com\tibco\adapter\test\Orders\*.properties

move orders.jar lib


:NORMAL
echo OK
goto end

:EXCEPTION
cd %ADHOME%
echo build error!

:end