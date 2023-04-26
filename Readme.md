### Exception Handling Assignment

##### General file info:
The *Task.java* file contains the main program and the two exception classes.
There are 3 input files for the 3 test cases, and the *test.bat* file tests all 3 of them
The first input file outputs a sorted file correctly, but the other two raise an exception.

##### How the code works:
First the program checks if the input file is .armxl and is non-empty, if one of the previous conditions are false then it raises the proper exception.
Then it creates an instance of the class *Document* from the package *w3c*, it take an .xml file as input and stores it. Then it iterates over all *CONTAINER* tags and stores all *SHORT-NAME* tages in a *String* array, it makes two copies of this array and sorts one of them (*orderedNames*), and leaves the other unsorted (*unOrderedNames*).
Now, we want to make a copy of the *CONTAINER* tags but sorted, so we start by making a copy of the parent tag *AUTOSAR* to append all *CONTAINERT* tags in it. Then we append them one by one depending on the order by which they were sorted in *orderedNames* array, this is done by getting the i-th element in *orderedNames* array and getting the index of this element in *unOrderedNames* array, so it takes this index and gets the correct *CONTAINER* tag from the xml file.
After that it just replaces the old unsorted *AUTOSAR* tag with the sorted one, and writes the output in an .arxml file with the correct name.

**Name:** Ali Mohsen Yehia Ateya
**Code:** 2000289
