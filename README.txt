Tutorial to run Amsterdam

Either open folder "AppMetaTestingFramework" in NetBeans and fix all the referencing error (all the libraries are stored as .jar files in /lib directory), or compile the java files under /src folder with a classpath parameter of ".\lib\JSAP-2.1.jar;.\lib\jdom.jar;.\lib\weka.jar;."

In NetBeans, running options are setup already, simply right click project name in navigator and select run. To run from console, after compiling all the .java files, run SimpleParser .class file with the classpath specified above.

E.g. from console
java -cp .\lib\JSAP-2.1.jar;.\lib\jdom.jar;.\lib\weka.jar;. SimpleParser "naivebayestest.xml"
or
java -cp .\lib\JSAP-2.1.jar;.\lib\jdom.jar;.\lib\weka.jar;. SimpleParser "simpleTest"
or
replace the last substring to the name of any test descriptor file you've written.


It takes one single argument about the file path (relative path) you want to parse. If the file passing in is an XML file, it will take MetaDescriptorParser to parse it, otherwise it will see it as a test descriptor in simplified syntax, and transform it to an XML first then parse it to generate the .java test file.

Upon finishing, a test file is generated under /test directory, the test file name is the same as your test descriptor file name, only in .java extension. Compile it with classpath ".\lib\JSAP-2.1.jar;.\lib\jdom.jar;.\lib\weka.jar;." then run it (no argument needed). It will print out the test progress and result to STDOUT (System.out).


METAMORPHIC FUNCTIONS SUPPORTED
permute: e.g. permute(input1)
inverse: e.g. inverse(input1)
negate: 
e.g. negate(input1, "attr", 3) -- meaning negate the value of the third attribute for all instances,      negate(input2, "inst", 20) -- negate the value of all the attributes for the 20th instance	
The second string parameter can be "attr" for attribute, or "inst" for instance. Use extreme caution when using "negate" function, as negating non-numeric values like nominal, String, character will cause error.
