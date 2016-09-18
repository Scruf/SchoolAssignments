<table border="0" cellpadding="4" cellspacing="0" width="100%" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="top">

<table border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="baseline"><font size="-1">**Alan Kaminsky**</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">Department of Computer Science</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">Rochester Institute of Technology</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">[4505](http://icasualties.org/Iraq/) + [2384](http://icasualties.org/oef/) = 6889</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="top"><font size="-1">[Home Page](/~ark/)</font></td>

</tr>

</tbody>

</table>

<table border="0" cellpadding="4" cellspacing="0" width="100%" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="center">

<table border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="baseline"><font size="-1">**Concepts of Parallel and Distributed Systems**</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">CSCI 251-01, -02, -03</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">Fall Semester 2016</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="baseline"><font size="-1">[Course Page](/~ark/251/)</font></td>

</tr>

</tbody>

</table>

## CSCI 251—Concepts of Parallel and Distributed Systems  
Programming Project 1

**Prof. Alan Kaminsky—Fall Semester 2016**  
Rochester Institute of Technology—Department of Computer Science

[Overview](#overview)  
[Generalized Fibonacci Number](#system)  
[Software Requirements](#softwarereqts)  
[Software Design Criteria](#softwaredesign)  
[Submission Requirements](#submissionreqts)  
[Grading Criteria](#grading)  
[Late Projects](#late)  
[Plagiarism](#plagiarism)  
[Resubmission](#resubmission)

* * *

<a name="overview">

### Overview

</a>

Write a Java program that uses multiple threads to learn about thread creation, execution, synchronization, and termination.

**Help with your project:** I am willing to help you with the design of your project. I am willing to help you debug your project if the code isn't working. However, for help with design or debugging issues _you must come see me in person._ Either visit me during office hours or make an appointment. I will not help you with design or debugging issues via email. If it's the evening of the project deadline and I have gone home, you are on your own. Plan and work ahead so there will be plenty of time for me to help you if necessary.

* * *

<a name="system">

### Generalized Fibonacci Number

</a>

The generalized Fibonacci number _f_<sub>_a,b_</sub>(_n_), _n_ ≥ 0, with parameters _a_ and _b_, is defined recursively as

> _f_<sub>_a,b_</sub>(0) = _a_  
> _f_<sub>_a,b_</sub>(1) = _b_  
> _f_<sub>_a,b_</sub>(_n_) = _f_<sub>_a,b_</sub>(_n_−1) + _f_<sub>_a,b_</sub>(_n_−2)

One way to compute _f_<sub>_a,b_</sub>(_n_) uses an array **F** that stores all the intermediate values from _f_<sub>_a,b_</sub>(0) to _f_<sub>_a,b_</sub>(_n_). Here is an example showing how _f_<sub>5,7</sub>(9) is computed:

<center>![](fig01.png)</center>

You will write a multithreaded Java program to compute the generalized Fibonacci number specified by the parameters _a_ and _b_ and the argument _n_, using the array-based approach described above. The program gets the parameters from the command line. The program prints _f_<sub>_a,b_</sub>(_n_). Here is an example computing _f_<sub>5,7</sub>(100):

<pre>$ java Fib 5 7 100
3574188916427609250655
</pre>

Because the Fibonacci number is an exponential function of _n_, you cannot use type <tt>int</tt> or type <tt>long</tt> to do the calculations. If you do, the calculations will overflow, and the program will get the wrong answer. Instead, you must use class java.math.BigInteger. The argument _n_, however, will be type <tt>int</tt>.

* * *

<a name="softwarereqts">

### Software Requirements

</a>

2.  The program must be run by typing this command line:

    <pre>    java Fib <a> <b> <n>
    </pre>

    1.  The first argument is the parameter _a_; it must be a big integer (class java.math.BigInteger).
    2.  The second argument is the parameter _b_; it must be a big integer (class java.math.BigInteger).
    3.  The third argument is the argument _n_; it must be an integer ≥ 0 (type <tt>int</tt>).  
    _Note:_ This means that the program's class must be named <tt>Fib</tt>, and this class must not be in a package.
3.  If the command line does not have the required number of arguments, or if any argument is erroneous, the program must print a usage message on the console and must exit. The usage message must describe the problem. The wording of the usage message is up to you.
4.  The program must print one line on the console.
    1.  The line's contents must be the number _f_<sub>_a,b_</sub>(_n_).
    2.  There must be no space characters before the number or after the number.
    3.  The output must be terminated by a newline.
5.  If the program encounters any error condition, the program must print an error message on the console and must exit. The error message must describe the problem. The wording of the error message is up to you.

    _Note: If your program's output does not conform **exactly** to Software Requirements 1 through 4, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

* * *

<a name="softwaredesign">

### Software Design Criteria

</a>

2.  The program must consist of the following classes, and no other classes.

    2.  A monitor class containing the **F** array. The monitor class must have the following public methods, and no other public methods.

        *   Constructor <tt>(int n)</tt>. This constructor initializes the monitor to calculate the generalized Fibonacci number for argument _n_.
        *   <tt>void putValue (int i, BigInteger value)</tt>. This method puts the given <tt>value</tt> into array element <tt>i</tt>. It is an error if a value has already been put into array element <tt>i</tt>.
        *   <tt>BigInteger getValue (int i)</tt>. This method returns the value stored in array element <tt>i</tt>. This method does not return until array element <tt>i</tt> has been put.
    3.  A thread that prints the output.
    4.  A thread that computes _one and only one_ array element and stores it in the monitor. There is a separate instance of this thread _for each array element,_ including array elements 0 and 1.
    5.  A main program. The main program must create the above objects. The main program must start the threads _in this order:_ first the output printing thread, then the thread that computes the last array element, then the thread that computes the next-to-last array element, and so on backwards through the array elements. The main program must _not_ wait for the threads to terminate. The main program _must not do anything else._
3.  The program must follow the thread programming patterns studied in class.
4.  The program must be designed using object oriented design principles as appropriate.
5.  The program must make use of reusable software components as appropriate.
6.  Each class or interface must include a Javadoc comment describing the overall class or interface.
7.  Each method within each class or interface must include a Javadoc comment describing the overall method, the arguments if any, the return value if any, and the exceptions thrown if any.

    _Note:_ See my Java source files which we studied in class for the style of Javadoc comments I'm looking for.

    _Note: If your program's design does not conform to Software Design Criteria 1 through 6, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

* * *

<a name="submissionreqts">

### Submission Requirements

</a>

Your project submission will consist of a ZIP file containing the Java source file for every class and interface in your project. Put all the source files into a ZIP file named <tt>"<username>.zip"</tt>, replacing <tt><username></tt> with the user name from your Computer Science Department account. On a Linux system, the command for creating a ZIP file is:

<tt>zip <username>.zip *.java</tt>

Send your ZIP file to me by email at ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu. Include your full name and your computer account name in the email message, and include the ZIP file as an attachment.

When I get your email message, I will extract the contents of your ZIP file into a directory. I will set my Java class path to include the directory where I extracted your files, as well as the Parallel Java 2 Library. I will compile all the Java source files in your program using the JDK 1.8 compiler. I will then send you a reply message acknowledging I received your project and stating whether I was able to compile all the source files. If you have not received a reply within one day, please contact me. Your project is not successfully submitted until I have sent you an acknowledgment stating I was able to compile all the source files.

The submission deadline is Wednesday, September 14, 2016 at 11:59pm. The date/time at which your email message arrives in my inbox will determine whether your project meets the deadline.

You may submit your project multiple times up until the deadline. I will keep and grade only the most recent successful submission. There is no penalty for multiple submissions.

If you submit your project before the deadline, but I do not accept it (e.g. I can't compile all the source files), and you cannot or do not submit your project again before the deadline, the project will be late (see below). **_I strongly advise you to submit the project several days BEFORE the deadline, so there will be time to deal with any problems that may arise in the submission process._**

* * *

<a name="grading">

### Grading Criteria

</a>

I will grade your project by:

*   (10 points) Evaluating the design of your program, as documented in the Javadoc and as implemented in the source code.
    *   All of the [Software Design Criteria](#softwaredesign) are fully met: 10 points.
    *   Some of the [Software Design Criteria](#softwaredesign) are not fully met: 0 points.
*   (20 points) Running your project. There will be twenty test cases, each worth 1 point. For each test case, if the program runs using the command line in Requirement 1 and the program produces the correct output, the test case will get 1 point, otherwise the test case will get 0 points. "Correct output" means "output fulfills _all_ the [Software Requirements](#softwarereqts) _exactly._"
*   (30 points) Total.

When I run your program, the Java class path will point to the directory with your compiled class files, as well as the Parallel Java 2 Library. I will use JDK 1.8 to run your program.

I will grade the test cases based _solely_ on whether your program produces the correct output as specified in the above [Software Requirements](#softwarereqts). _Any_ deviation from the requirements will result in a grade of 0 for the test case. This includes errors in the formatting (such as extra spaces), incorrect uppercase/lowercase, output lines not terminated with a newline, extra newline(s) in the output, and extraneous output not called for in the requirements. The requirements state _exactly_ what the output is supposed to be, and there is no excuse for outputting anything different. If any requirement is unclear, please ask for clarification.

If there is a defect in your program and that same defect causes multiple test cases to fail, I will deduct points for _every_ failing test case. The number of points deducted does _not_ depend on the size of the defect; I will deduct the same number of points whether the defect is 1 line, 10 lines, 100 lines, or whatever.

After grading your project I will put your grade and any comments I have in your encrypted grade file. For further information, see the [Course Grading and Policies](../policies.shtml) and the [Encrypted Grades](../encryptedgrades.shtml).

* * *

<a name="late">

### Late Projects

</a>

If I have not received a successful submission of your project by the deadline, your project will be late and will receive a grade of zero. You may request an extension for the project. There is no penalty for an extension. See the Course Policies for my [policy on extensions](../policies.shtml#extensions).

* * *

<a name="plagiarism">

### Plagiarism

</a>

Programming Project 1 must be entirely your own individual work. I will not tolerate plagiarism. If in my judgment the project is not entirely your own work, you will automatically receive, as a minimum, a grade of zero for the assignment. See the Course Policies for my [policy on plagiarism](../policies.shtml#plagiarism).

* * *

<a name="resubmission">

### Resubmission

</a>

If you so choose, you may submit a revised version of your project after you have received the grade for the original version. However, if the original project was not successfully submitted by the (possibly extended) deadline or was not entirely your own work (i.e., plagiarized), you are not allowed to submit a revised version. Submit the revised version via email in the same way as the original version. I will accept a resubmission up until three days after the grades for the original version are released. The resubmission deadline will be announced on the [What's New](../whatsnew.shtml) page and in the [Course Schedule](../schedule.shtml). You may resubmit your project multiple times up until the deadline; I will keep and grade only the most recent successful resubmission; there is no penalty for multiple resubmissions. I will grade the revised version using the same criteria as the original version, then I will subtract 3 points (10% of the maximum possible points) as a resubmission penalty. The revised grade will replace the original grade, even if the revised grade is less than the original grade.

<table border="0" cellpadding="4" cellspacing="0" width="100%" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="center">

<table border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="baseline"><font size="-1">**Concepts of Parallel and Distributed Systems**</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">CSCI 251-01, -02, -03</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">Fall Semester 2016</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="baseline"><font size="-1">[Course Page](/~ark/251/)</font></td>

</tr>

</tbody>

</table>

<table border="0" cellpadding="4" cellspacing="0" width="100%" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="top">

<table border="0" cellpadding="0" cellspacing="0" bgcolor="#CCCCCC">

<tbody>

<tr>

<td align="left" valign="baseline"><font size="-1">**Alan Kaminsky**</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">Department of Computer Science</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">Rochester Institute of Technology</font></td>

<td align="left" valign="baseline"><font size="-2">•</font></td>

<td align="left" valign="baseline"><font size="-2">[4505](http://icasualties.org/Iraq/) + [2384](http://icasualties.org/oef/) = 6889</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="top"><font size="-1">[Home Page](/~ark/)</font></td>

</tr>

</tbody>

</table>

<font size="-2">Copyright © 2016 Alan Kaminsky. All rights reserved. Last updated 24-Aug-2016. Please send comments to ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu.</font>