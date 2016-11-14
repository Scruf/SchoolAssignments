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

<td align="left" valign="baseline"><font size="-2">[4507](http://icasualties.org/Iraq/) + [2387](http://icasualties.org/oef/) = 6894</font></td>

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
Programming Project 4

**Prof. Alan Kaminsky—Fall Semester 2016**  
Rochester Institute of Technology—Department of Computer Science

[Overview](#overview)  
[Poll System](#game)  
[Software Requirements](#softwarereqts)  
[Software Design Criteria](#softwaredesign)  
[Submission Requirements](#submissionreqts)  
[Grading Criteria](#grading)  
[Late Projects](#late)  
[Plagiarism](#plagiarism)  
[Resubmission](#resubmission)

* * *

<a name="overview">

## Overview

</a>

Write Java programs for a network application to learn about network programming with datagrams.

**Help with your project:** I am willing to help you with the design of your project. I am willing to help you debug your project if the code isn't working. However, for help with design or debugging issues _you must come see me in person._ Either visit me during office hours or make an appointment. I will not help you with design or debugging issues via email. If it's the evening of the project deadline and I have gone home, you are on your own. Plan and work ahead so there will be plenty of time for me to help you if necessary.

* * *

<a name="game">

## Poll System

</a>

In the Poll System, there is a Pollster, and there are one or more Respondents. The Pollster poses questions to the Respondents. The Respondents agree or disagree with the questions. The Pollster displays the total number of agree votes and the total number of disagree votes.

### Pollster

The Pollster program is run with this command. It receives datagrams sent to the given pollster host and port by the Responders.

<pre>    $ java Pollster <pollsterhost> <pollsterport>
</pre>

When the Pollster starts up, its graphical user interface looks like this:

![](fig01.png)

There is a text field for entering a question, initially blank; a Go button for starting a poll; a field with the number of agree votes, initially 0; and a field with the number of disagree votes, initially 0.

Here is the **_partial_** code for the Pollster GUI class: [PollsterUI.java](PollsterUI.java)

When you write the Pollster program, you must add any necessary lines of code to the PollsterUI class. **_However, you are not allowed to modify or delete any existing lines of code in the above source file._** When you submit your project, **_you must include the modified PollsterUI.java source file as part of your submission._**

### Responder

The Responder program is run with this command. It sends datagrams to the Pollster at the given pollster host and port. It receives datagrams sent to the given responder host and port by the Pollster.

<pre>    $ java Responder <pollsterhost> <pollsterport> <responderhost> <responderport>
</pre>

When the Responder starts up, its graphical user interface looks like this:

![](fig02.png)

There is a text field for displaying the Pollster's question, initially blank; an Agree button, initially disabled; and a Disagree button, initially disabled.

Here is the **_partial_** code for the Responder GUI class: [ResponderUI.java](ResponderUI.java)

When you write the Responder program, you must add any necessary lines of code to the ResponderUI class. **_However, you are not allowed to modify or delete any existing lines of code in the above source file._** When you submit your project, **_you must include the modified ResponderUI.java source file as part of your submission._**

### Pollster–Responder Interaction

When the Pollster's Go button is clicked, a new poll starts. All existing Responders must update within one second, as follows. If a Responder starts up while a poll is in progress, the Responder's window must update within one second, as follows.

*   The Pollster's question must appear in the Responder's text field.
*   The Responder's Agree button must be enabled and not selected.
*   The Responder's Disagree button must be enabled and not selected.

![](fig03.png)

![](fig04.png)

Whenever a Responder's Agree or Disagree button is clicked, the GUI automatically selects the clicked button and deselects the other button. The Pollster must update within one second, as follows.

*   The agree vote field must display the total number of agree votes from all the Responders.
*   The disagree vote field must display the total number of disagree votes from all the Responders.

![](fig05.png)

![](fig06.png)

When a Responder's window is closed, the Responder program must terminate. If a Responder terminates, the Pollster must detect this within three seconds. The Pollster must then update the vote fields to display the total number of agree and disagree votes from the remaining Responders.

When the Pollster's window is closed, the Pollster program must terminate. If the Pollster terminates, all the Responders must detect this within three seconds. The Responders' windows must then go back to the startup state described above.

If datagrams are dropped or are received out of order, the Pollster and Respondents must _eventually_ display the correct information. (How long it takes to do this is not specified.)

* * *

<a name="softwarereqts">

## Software Requirements

</a>

2.  The Pollster program must be run by typing this command line:

    <pre>java Pollster <pollsterhost> <pollsterport>
    </pre>

    1.  <tt><pollsterhost></tt> is the host name or IP address of the pollster's mailbox.
    2.  <tt><pollsterport></tt> is the port number of the pollster's mailbox.  
    _Note:_ This means that the Pollster program's class must be named <tt>Pollster</tt>, and this class must not be in a package.
3.  If the command line does not have the required number of arguments, if any argument is invalid, if the Pollster program cannot set up a mailbox at the given pollster host and port, or if any other error condition occurs, the Pollster program must print an error message on the console and must terminate. The error message must describe the problem. The wording of the error message is up to you.
4.  The Pollster program must operate as specified above under "Pollster–Responder Interaction."
5.  The Pollster program must not print anything on the console other than error messages as specified above.
6.  The Responder program must be run by typing this command line:

    <pre>java Responder <pollsterhost> <pollsterport> <responderhost> <responderport>
    </pre>

    1.  <tt><pollsterhost></tt> is the host name or IP address of the pollster's mailbox.
    2.  <tt><pollsterport></tt> is the port number of the pollster's mailbox.
    3.  <tt><responderhost></tt> is the host name or IP address of the responder's mailbox.
    4.  <tt><responderport></tt> is the port number of the responder's mailbox.  
    _Note:_ This means that the Responder program's class must be named <tt>Responder</tt>, and this class must not be in a package.
7.  If the command line does not have the required number of arguments, if any argument is invalid, if the Responder program cannot set up a mailbox at the given responder host and port, or if any other error condition occurs, the Responder program must print an error message on the console and must terminate. The error message must describe the problem. The wording of the error message is up to you.
8.  The Responder program must operate as specified above under "Pollster–Responder Interaction."
9.  The Responder program must not print anything on the console other than error messages as specified above.

    _Note: If your programs do not conform **exactly** to Requirements 1 through 8, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

    _Note:_ If you are not sure what the Pollster or the Responder is supposed to do in any particular situation, _please visit me in person._ I will let you run my implementations of the programs.

* * *

<a name="softwaredesign">

## Software Design Criteria

</a>

2.  The Pollster and Responder programs must follow the network programming patterns studied in class.
3.  The network protocol must use UDP for its transport. The messages and encodings are up to you to design.
4.  The Pollster and Responder programs must be designed using object oriented design principles as appropriate.
5.  The Pollster and Responder programs must make use of reusable software components as appropriate.
6.  Each class or interface must include a Javadoc comment describing the overall class or interface.
7.  Each constructor and method within each class or interface must include a Javadoc comment describing the overall constructor or method, the arguments if any, the return value if any, and the exceptions thrown if any.

    _Note:_ See my Java source files which we studied in class for the style of Javadoc comments I'm looking for.

    _Note: If your project's design does not conform to Software Design Criteria 1 through 6, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

* * *

<a name="submissionreqts">

### Submission Requirements

</a>

Your project submission will consist of a ZIP file containing the Java source file for every class and interface in your project, **_including the modified PollsterUI.java and ResponderUI.java source files._** Put all the files into a ZIP file named <tt>"<username>.zip"</tt>, replacing <tt><username></tt> with the user name from your Computer Science Department account. On a Linux system, the command for creating a ZIP file is:

<tt>zip <username>.zip *.java</tt>

Send your ZIP file to me by email at ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu. Include your full name and your computer account name in the email message, and include the ZIP file as an attachment.

When I get your email message, I will extract the contents of your ZIP file into a directory. I will set my Java class path to include the directory where I extracted your files, as well as the Parallel Java 2 Library. I will compile all the Java source files in your program using the JDK 1.8 compiler. I will then send you a reply message acknowledging I received your project and stating whether I was able to compile all the source files. If you have not received a reply within one business day (i.e., not counting weekends), please contact me. Your project is not successfully submitted until I have sent you an acknowledgment stating I was able to compile all the source files.

The submission deadline is Monday, November 28, 2016 at 11:59pm. The date/time at which your email message arrives in my inbox will determine whether your project meets the deadline.

You may submit your project multiple times up until the deadline. I will keep and grade only the most recent successful submission. There is no penalty for multiple submissions.

If you submit your project before the deadline, but I do not accept it (e.g. I can't compile all the source files), and you cannot or do not submit your project again before the deadline, the project will be late (see below). **_I strongly advise you to submit the project several days BEFORE the deadline, so there will be time to deal with any problems that may arise in the submission process._**

* * *

<a name="grading">

## Grading Criteria

</a>

I will grade your project by:

*   (10 points) Evaluating the design of your project, as documented in the Javadoc and as implemented in the source code.
    *   All of the [Software Design Criteria](#softwaredesign) are fully met: 10 points.
    *   Some of the [Software Design Criteria](#softwaredesign) are not fully met: 0 points.
*   (20 points) Running your project. I will have a test script consisting of 20 actions. An "action" consists of starting a Pollster or Responder program, typing text, clicking a button or buttons, or closing a window. I will perform the actions one by one. **_If any action does not produce the required result, I will stop the test script. The number of points is equal to the number of actions successfully completed._**
*   (30 points) Total.

When I run your project, the Java class path will point to the directory with your compiled class files. I will use JDK 1.8 to run your server program and your client program.

I will grade the test script based _solely_ on whether your server and client program produce the correct results as specified in the above [Software Requirements](#softwarereqts). _Any_ deviation from the requirements will stop the test script. This includes errors in the formatting of the displayed messages, such as extra or missing spaces; misspelled words; incorrect capitalization; incorrect, extra, or missing punctuation; and extraneous output not called for in the requirements. The requirements state _exactly_ what the output is supposed to be, and there is no excuse for outputting anything different. If any requirement is unclear, please ask for clarification.

After grading your project I will put your grade and any comments I have in your encrypted grade file. For further information, see the [Course Grading and Policies](../policies.shtml) and the [Encrypted Grades](../encryptedgrades.shtml).

* * *

<a name="late">

## Late Projects

</a>

If I have not received a successful submission of your project by the deadline, your project will be late and will receive a grade of zero. You may request an extension for the project. There is no penalty for an extension. See the Course Policies for my [policy on extensions](../policies.shtml#extensions).

* * *

<a name="plagiarism">

## Plagiarism

</a>

Programming Project 4 must be entirely your own individual work. I will not tolerate plagiarism. If in my judgment the project is not entirely your own work, you will automatically receive, as a minimum, a grade of zero for the assignment. See the Course Policies for my [policy on plagiarism](../policies.shtml#plagiarism).

* * *

<a name="resubmission">

## Resubmission

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

<td align="left" valign="baseline"><font size="-2">[4507](http://icasualties.org/Iraq/) + [2387](http://icasualties.org/oef/) = 6894</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="top"><font size="-1">[Home Page](/~ark/)</font></td>

</tr>

</tbody>

</table>

<font size="-2">Copyright © 2016 Alan Kaminsky. All rights reserved. Last updated 05-Nov-2015. Please send comments to ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu.</font>
