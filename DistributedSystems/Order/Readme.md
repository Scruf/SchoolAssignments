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

<td align="left" valign="baseline"><font size="-2">[4506](http://icasualties.org/Iraq/) + [2384](http://icasualties.org/oef/) = 6890</font></td>

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
Programming Project 2

**Prof. Alan Kaminsky—Spring Semester 2016**  
Rochester Institute of Technology—Department of Computer Science

[Overview](#overview)  
[Order of a Generator](#system)  
[Software Requirements](#softwarereqts)  
[Software Design Criteria](#softwaredesign)  
[Compiling and Running Your Parallel Program](#compiling)  
[Submission Requirements](#submissionreqts)  
[Grading Criteria](#grading)  
[Late Projects](#late)  
[Plagiarism](#plagiarism)  
[Resubmission](#resubmission)

* * *

<a name="overview">

### Overview

</a>

Write a Java program using the [Parallel Java 2 Library](/~ark/pj2.shtml) to learn about multicore parallel programming.

**Help with your project:** I am willing to help you with the design of your project. I am willing to help you debug your project if the code isn't working. However, for help with design or debugging issues _you must come see me in person._ Either visit me during office hours or make an appointment. I will not help you with design or debugging issues via email. If it's the evening of the project deadline and I have gone home, you are on your own. Plan and work ahead so there will be plenty of time for me to help you if necessary.

* * *

<a name="system">

### Order of a Generator

</a>

Consider a **prime number** _p_ and a number _g_ in the range 1 ≤ _g_ ≤ _p_−1. The **modular power** operation, _g_<sup>_n_</sup> (mod _p_) for _n_ ≥ 1, is computed by taking _n_ copies of _g_, multiplying them together, dividing the result by _p_, and keeping the remainder.

If we compute successive modular powers of _g_ — that is, _g_<sup>1</sup> (mod _p_), _g_<sup>2</sup> (mod _p_), _g_<sup>3</sup> (mod _p_), and so on — eventually the answer will be 1 for some _n_. We know that _g_<sup>_p_−1</sup> (mod _p_) = 1; this is Fermat's Little Theorem. However, it's possible that _g_<sup>_n_</sup> (mod _p_) = 1 for some values of _n_ less than _p_−1.

We are interested in the _smallest_ value of _n_ such that _g_<sup>_n_</sup> (mod _p_) = 1. In the language of group theory, _g_ is called a **generator,** and this value of _n_ is the **order** of the generator.

For example, for _p_ = 23 (a prime) and _g_ = 5, the successive powers 5<sup>_n_</sup> (mod 23) are 5, 2, 10, 4, 20, 8, 17, 16, 11, 9, 22, 18, 21, 13, 19, 3, 15, 6, 7, 12, 14, and 1. Because 22 is the smallest value of _n_ for which 5<sup>_n_</sup> (mod 23) = 1, the order of the generator 5 is 22.

For another example, for _p_ = 23 (a prime) and _g_ = 2, the successive powers 2<sup>_n_</sup> (mod 23) are 2, 4, 8, 16, 9, 18, 13, 3, 6, 12, 1, 2, 4, 8, 16, 9, 18, 13, 3, 6, 12, and 1. Because 11 is the smallest value of _n_ for which 2<sup>_n_</sup> (mod 23) = 1, the order of the generator 2 is 11.

You will write a Java program using the [Parallel Java 2 Library](/~ark/pj2.shtml) that is given _p_ and _g_ on the command line, computes the order of _g_ **_in parallel,_** and prints the answer. For example:

<pre>$ java pj2 Order 23 5
22
$ java pj2 Order 23 2
11
</pre>

The program will need to compute the modular power operation. Do this using the <tt>modPow()</tt> method of class java.math.BigInteger.

The program will also need to check whether _p_ is prime before proceeding. Do this using the <tt>isProbablePrime()</tt> method of class java.math.BigInteger. Call that method with a <tt>certainty</tt> argument of 64.

* * *

<a name="softwarereqts">

### Software Requirements

</a>

2.  The program must be run by typing this command line:

    <pre>    java pj2 Order <p> <g>
    </pre>

    1.  <tt><p></tt> is the modulus; it must be a prime number (type <tt>int</tt>) ≥ 2.
    2.  <tt><g></tt> is the generator; it must be an integer (type <tt>int</tt>) in the range 1 ≤ _g_ ≤ _p_−1.

    _Note:_ This means that the program's class must be named <tt>Order</tt>, this class must extend class Task, and this class must not be in a package.

3.  If the command line does not have the required number of arguments, or if any argument is erroneous, the program must print an error message on the console and must exit. The error message must describe what the problem is. The wording of the error message is up to you.
4.  The program must test all exponents _n_ in the range 1 ≤ _n_ ≤ _p_−1 to find the order of _g_.
5.  The program must print the order of _g_ on the console. There must be no whitespace at the beginning or end of the output line. The output line must be terminated with a newline.

    _Note: If your program's output does not conform **exactly** to Requirements 1 through 4, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

* * *

<a name="softwaredesign">

### Software Design Criteria

</a>

2.  The program must be a parallel program written with the [Parallel Java 2 Library](/~ark/pj2.shtml).
3.  The program must partition the computation among multiple threads so as to experience a speedup when run on a multicore parallel computer.
4.  The program must follow the parallel programming patterns studied in class.
5.  The program must be designed using object oriented design principles as appropriate.
6.  The program must make use of reusable software components as appropriate.
7.  Each class or interface must include a Javadoc comment describing the overall class or interface.
8.  Each constructor and method within each class or interface must include a Javadoc comment describing the overall constructor or method, the arguments if any, the return value if any, and the exceptions thrown if any.

    _Note:_ See my Java source files which we studied in class for the style of Javadoc comments I'm looking for.

    _Note: If your program's design does not conform to Software Design Criteria 1 through 7, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

* * *

<a name="compiling">

### Compiling and Running Your Parallel Program

</a>

You must use JDK 1.7 or higher to compile your program with the Parallel Java 2 Library.

You must set the Java class path to include the directory with your source files and the Parallel Java 2 Library JAR file.

To compile and run your program on the CS Department computers, use these commands:

<pre>$ exec bash
$ export CLASSPATH=.:/home/fac/ark/public_html/pj2.jar
$ javac Order.java
$ java pj2 Order 23 5
</pre>

The first command puts you in the <tt>bash</tt> shell. The second command sets up the <tt>CLASSPATH</tt> variable to include the current directory and the Parallel Java 2 Library JAR file. The third command compiles your program. The fourth command runs your program (of course, put in the command line arguments you want).

If you prefer to work on your own personal computer, you must install JDK 1.7 or higher as well as the Parallel Java 2 Library. For that you are on your own. See the [Parallel Java 2 Library](/~ark/pj2.shtml) page for download, installation, and usage instructions.

For this project you will _not_ run your program on the CS Department's parallel computers (<tt>nessie</tt>, <tt>champ</tt>, <tt>kraken</tt>, <tt>tardis</tt>).

* * *

<a name="submissionreqts">

### Submission Requirements

</a>

Your project submission will consist of a ZIP file containing the Java source file for every class and interface in your project. Put all the source files into a ZIP file named <tt>"<username>.zip"</tt>, replacing <tt><username></tt> with the user name from your Computer Science Department account. On a Linux system, the command for creating a ZIP file is:

<tt>zip <username>.zip *.java</tt>

**_DO NOT include the Parallel Java 2 Library in your ZIP file!_**

Send your ZIP file to me by email at ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu. Include your full name and your computer account name in the email message, and include the ZIP file as an attachment.

When I get your email message, I will extract the contents of your ZIP file into a directory. I will set my Java class path to include the directory where I extracted your files, as well as the Parallel Java 2 Library. I will compile all the Java source files in your program using the JDK 1.8 compiler. I will then send you a reply message acknowledging I received your project and stating whether I was able to compile all the source files. If you have not received a reply within one business day (i.e., not counting weekends), please contact me. Your project is not successfully submitted until I have sent you an acknowledgment stating I was able to compile all the source files.

The submission deadline is Wednesday, October 5, 2016 at 11:59pm. The date/time at which your email message arrives in my inbox will determine whether your project meets the deadline.

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

I will grade the test cases based _solely_ on whether your program produces the correct output as specified in the above [Software Requirements](#softwarereqts). _Any_ deviation from the requirements will result in a grade of 0 for the test case. This includes errors in the formatting (such as extra spaces), incorrect uppercase/lowercase, output lines not terminated with a newline, and extraneous output not called for in the requirements. The requirements state _exactly_ what the output is supposed to be, and there is no excuse for outputting anything different. If any requirement is unclear, please ask for clarification.

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

Programming Project 2 must be entirely your own individual work. I will not tolerate plagiarism. If in my judgment the project is not entirely your own work, you will automatically receive, as a minimum, a grade of zero for the assignment. See the Course Policies for my [policy on plagiarism](../policies.shtml#plagiarism).

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

<td align="left" valign="baseline"><font size="-2">[4506](http://icasualties.org/Iraq/) + [2384](http://icasualties.org/oef/) = 6890</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="top"><font size="-1">[Home Page](/~ark/)</font></td>

</tr>

</tbody>

</table>

<font size="-2">Copyright © 2016 Alan Kaminsky. All rights reserved. Last updated 20-Sep-2016. Please send comments to ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu.</font>
