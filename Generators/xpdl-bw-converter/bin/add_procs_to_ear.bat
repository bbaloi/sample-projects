@echo off

echo arg 1 = %1
echo arg 2 = %2
echo arg 3 = %3
echo arg 4 = %4

sed -e "s^</processProperty>^,%1</processProperty>^g" -e "s^<processProperty/>^<processProperty>%1</processProperty>^g" %3 >  %4

sed -e "s^###^,^g" %4 > %2
