@ECHO OFF
del disgustingActual.txt 

REM compile the code into the bin folder
javac  ..\src\seedu\addressbook\DisgustingAddressbook.java -d ..\bin

REM run the program, feed commands from input.txt file and redirect the output to the actual.txt
java -classpath ..\bin seedu.addressbook.DisgustingAddressBook < input.txt > disgustingActual.txt

REM compare the output to the expected output
FC disgustingActual.txt expected.txt