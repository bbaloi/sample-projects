@echo off

echo 1)Preparing XPDL for processing....
call add_space.bat ..\xpdl-src\CingularProcesses_v1.xpdl ..\samples\CingularProcesses_converted.xpdl
echo 2)Converting XPDL to BW processes.....
call start_converter.bat > out.txt
echo 3)Adjusting archive
call add_archive.bat "ValidateAddress-main.process,ValidateAddress.process" 