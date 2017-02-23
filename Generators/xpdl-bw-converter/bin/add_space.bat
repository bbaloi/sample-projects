@echo off

echo arg 1 = %1
echo arg 2 = %2

sed "s^><^> <^g" %1 > %2
