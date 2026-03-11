# Task S1.05

## Level 1 Exercise 1
**Description**:
Create a Java class that manages a collection of
library books.

## Level 1 Exercise 2
**Description**:
Add to the previous exercise's class the
functionality to list a directory tree with the
content of all its levels (recursively), so it is
printed on screen in alphabetical order within each level,
also indicating whether it is a directory (D)
or a file (F), and its last modification date.

## Level 1 Exercise 3
**Description**:
Modify the previous exercise. Now, instead of showing
the result on screen, save the result in a TXT file.

## Level 1 Exercise 4
**Description**:
Add the functionality to read any TXT file and
display its contents in the console.

## Level 1 Exercise 5
**Description**:
Now the program must serialize a Java object to a .ser file
and then deserialize it.

## Level 2 Exercise 1
**Description**:
Run exercise 3 from the previous level by parameterizing
all methods in a configuration file.

You can use a Java Properties file, or the Apache Commons
Configuration library if you prefer.

From the previous exercise, parameterize the following:

    Directory to read.
    Name and directory of the resulting TXT file.

## Level 3 Exercise 1
**Description**:
In this final level you will dive into a key concept in 
computer security: data encryption.

You need to create a utility that can encrypt and decrypt 
files, using one of the most widely used algorithms in the 
real world: AES (Advanced Encryption Standard), in ECB or
CBC mode, with PKCS5Padding.

You may use the standard Java libraries (javax.crypto) or 
explore more powerful alternatives like org.apache.commons.crypto.

The goal is to understand how to protect sensitive 
information with symmetric cryptography and apply it to 
real cases such as the files generated in previous exercises.
This exercise prepares you for professional environments 
where security and privacy are essential.

## Technologies
- Backend: Java-Maven.

## Installation and Execution
1. Clone the repository: `git clone https://github.com/Quint3in/Tasca_S1.05.git`
2. Go into the project folder: `cd Tasca_S1.05`
3. Build the module you want, for example:
   `mvn -pl n1exercici1 -am package`
4. Run it:
   `java -cp n1exercici1/target/classes org.example.Main`
