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
Programming Project 3

**Prof. Alan Kaminsky—Fall Semester 2016**  
Rochester Institute of Technology—Department of Computer Science

[Overview](#overview)  
[Gobble](#game)  
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

Write a Java client program and server program for a network application to learn about network programming with sockets.

**Help with your project:** I am willing to help you with the design of your project. I am willing to help you debug your project if the code isn't working. However, for help with design or debugging issues _you must come see me in person._ Either visit me during office hours or make an appointment. I will not help you with design or debugging issues via email. If it's the evening of the project deadline and I have gone home, you are on your own. Plan and work ahead so there will be plenty of time for me to help you if necessary.

* * *

<a name="game">

## Gobble

</a>

In the game of Gobble, there are two players, the Red Worm and the Blue Worm, and a 4×4 array of cells containing food. The worms alternate turns. At its turn, a worm moves to an adjacent cell containing food and gobbles that food. If the game reaches a state where one worm has one or more adjacent cells containing food and the other worm has no adjacent cells containing food, the former worm wins the game. If the game reaches a state where both worms have no adjacent cells containing food, the game is a draw.

### Network Game

For this project you will implement the client program and the server program for a Gobble network game application. The network application uses TCP sockets for communication, with a protocol that you design. The network application is a multi-session multi-client application; the server supports any number of simultaneous sessions; each session consists of two players (clients) playing against each other. The server does not participate in the game; the server is just a glorified game board.

The server program is run with this command:

<pre>    $ java GobbleServer <host> <port>
</pre>

The client program is run with this command:

<pre>    $ java Gobble <host> <port> <playername>
</pre>

When a client starts up, it sets up a socket connection to the server at the given host and port. If the server has no sessions, or if every session has two players, the server creates a new session and adds the client to that session; the client then waits to start playing until another client joins the session. Otherwise, the server has a session with one waiting client, and the server adds the client to that session; the two clients then start playing each other.

### Client GUI

Once the game is underway, the client's graphical user interface looks like this:

![](https://cs.rit.edu/~ark/251/p3/fig02a.png)

The window's title is <tt>"Gobble -- <playername>"</tt> where <tt><playername></tt> is replaced with the player's name. The window displays a 4×4 array of cells. A red cell indicates the Red Worm's position. A blue cell indicates the Blue Worm's position. An empty cell indicates that the food there has been eaten. A yellow cell indicates food. A yellow cell with a black border indicates that the cell is enabled; that is, the worm can move to that cell. A worm can move up, down, left, or right to a cell containing food; a worm cannot move off the board. There is also a "New Game" button for starting a new game and a text field for displaying messages.

Here is the **_partial_** code for the client GUI class: [GobbleUI.java](GobbleUI.java)

When you write the client program, you must add any necessary lines of code to the GobbleUI class. **_However, you are not allowed to modify or delete any existing lines of code in the above source file._** When you submit your project, **_you must include the modified GobbleUI.java source file as part of your submission._**

Class GobbleUI uses the following custom Swing widget: [SpotButton.java](SpotButton.java)

You can call methods on an instance of class SpotButton:

*   <tt>setColor(color)</tt> sets the color of the button.
*   <tt>setVisible(true)</tt> shows the button.
*   <tt>setVisible(false)</tt> hides the button.
*   <tt>setEnabled(true)</tt> enables the button.
*   <tt>setEnabled(false)</tt> disables the button.
*   <tt>addActionListener(actionListener)</tt> adds an action listener to the button; the Swing thread calls the action listener's <tt>actionPerformed()</tt> method when the button is clicked.

**_However, you are not allowed to modify the SpotButton class in any way, and you are not allowed to replace the SpotButton class with your own Swing widget class._**

Here is a game session between Alex and Blake, showing what appears on each player's GUI. The server is running on localhost port 5678. Alex types this command:

<pre>    $ java Gobble localhost 5678 Alex
</pre>

The server puts Alex in a new game session; Alex is waiting for a partner. Alex's GUI window appears. Alex is the Red Worm; the first player to join a session is always the Red Worm. The Red Worm is initially located in the top right cell. There is food in every cell except the top right and bottom left cells, and all food cells are disabled. The New Game button is disabled. The message field says "Waiting for partner".

If she changes her mind about playing, at this point Alex can close the window, the game session terminates, and the client program terminates.

![](https://cs.rit.edu/~ark/251/p3/fig01.png)

Blake types this command:

<pre>    $ java Gobble localhost 5678 Blake
</pre>

The server puts Blake into the game session with Alex. Blake's GUI window appears. Blake is the Blue Worm; the second player to join a session is always the Blue Worm. The Blue Worm is initially located in the bottom left cell. It is Alex's turn; the Red Worm always goes first. In Alex's window, two food cells are enabled showing where Alex can move; the New Game button is enabled; and the message field says "Your turn". In Blake's window, all food cells are disabled (because it isn't Blake's turn); the New Game button is enabled; and the message field says "Alex's turn".

At any time, either player can click the New Game button, and the game session goes back to this initial state.

![](https://cs.rit.edu/~ark/251/p3/fig02a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig02b.png)

Alex clicks the enabled food cell below the Red Worm's position. Alex moves down one cell, revealing the empty cell behind, and eats the food in the new cell. Both windows show the new game state. Now it's Blake's turn. In Alex's window, no food cells are enabled, and the message field says "Blake's turn". In Blake's window, two food cells are enabled, and the message field says "Your turn".

![](https://cs.rit.edu/~ark/251/p3/fig03a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig03b.png)

Blake moves right.

![](https://cs.rit.edu/~ark/251/p3/fig04a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig04b.png)

Alex moves down.

![](https://cs.rit.edu/~ark/251/p3/fig05a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig05b.png)

Blake moves right.

![](https://cs.rit.edu/~ark/251/p3/fig06a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig06b.png)

Alex moves left.

![](https://cs.rit.edu/~ark/251/p3/fig07a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig07b.png)

Blake moves right (the only move he can make) and loses the game, because there are no cells containing food up, down, left or right from the Blue Worm's position, but there are food cells adjacent to the Red Worm's position. Alex's message field says "You won!" Blake's message field says "Alex won!"

![](https://cs.rit.edu/~ark/251/p3/fig08a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig08b.png)

Alex (or Blake) clicks the New Game button, and the game session goes back to the initial state. The players do this sequence of moves:

*   Alex moves left.
*   Blake moves up.
*   Alex moves down.
*   Blake moves right.
*   Alex moves down.
*   Blake moves down.
*   Alex moves right.
*   Blake moves right.
*   Alex moves down.

At this point the game is a draw, because neither worm is adjacent to a food cell. Each player's message field says "Draw!"

![](https://cs.rit.edu/~ark/251/p3/fig09a.png)    ![](https://cs.rit.edu/~ark/251/p3/fig09b.png)

The two players keep playing in this fashion. If either player closes the window (at any point), the game session terminates, the other player's window closes as well, and both client programs terminate.

* * *

<a name="softwarereqts">

## Software Requirements

</a>

2.  The server program must be run by typing this command line:

    <pre>java GobbleServer <host> <port>
    </pre>

    1.  <tt><host></tt> is the host name or IP address of the server.
    2.  <tt><port></tt> is the port number of the server.  
    _Note:_ This means that the server program's class must be named <tt>GobbleServer</tt>, and this class must not be in a package.
3.  If the command line does not have the required number of arguments, if any argument is invalid, if the server program cannot listen for connections at the given host and port, or if any other error condition occurs, the server program must print an error message on the console and must terminate. The error message must describe the problem. The wording of the error message is up to you.
4.  The server program must manage multiple game sessions as specified above under "Network Game."
5.  The server program must not print anything on the console other than error messages as specified above.
6.  The client program must be run by typing this command line:

    <pre>java Gobble <host> <port> <playername>
    </pre>

    1.  <tt><host></tt> is the host name or IP address of the server.
    2.  <tt><port></tt> is the port number of the server.
    3.  <tt><playername></tt> is the name of the player.  
    _Note:_ This means that the client program's class must be named <tt>Gobble</tt>, and this class must not be in a package.
7.  If the command line does not have the required number of arguments, if any argument is invalid, if the client program cannot connect to the server at the given host and port, or if any other error condition occurs, the client program must print an error message on the console and must terminate. The error message must describe the problem. The wording of the error message is up to you.
8.  The client program must display and operate the graphical user interface specified above under "Client GUI."
9.  The client program must not print anything on the console other than error messages as specified above.

    _Note: If your programs do not conform **exactly** to Requirements 1 through 8, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

    _Note:_ If you are not sure what the client or the server is supposed to do in any particular situation, _please visit me in person._ I will let you run my implementations of the client program and the server program.

* * *

<a name="softwaredesign">

## Software Design Criteria

</a>

2.  The client and server programs must follow the network programming patterns studied in class.
3.  The network protocol must use TCP for its transport. The messages and encodings are up to you to design.
4.  The client and server programs must be designed using object oriented design principles as appropriate.
5.  The client and server programs must make use of reusable software components as appropriate.
6.  Each class or interface must include a Javadoc comment describing the overall class or interface.
7.  Each constructor and method within each class or interface must include a Javadoc comment describing the overall constructor or method, the arguments if any, the return value if any, and the exceptions thrown if any.

    _Note:_ See my Java source files which we studied in class for the style of Javadoc comments I'm looking for.

    _Note: If your program's design does not conform to Software Design Criteria 1 through 6, **you will lose credit** on your project._ See the [Grading Criteria](#grading) below.

* * *

<a name="submissionreqts">

### Submission Requirements

</a>

Your project submission will consist of a ZIP file containing the Java source file for every class and interface in your project, **_including the modified GobbleUI.java source file._** Put all the files into a ZIP file named <tt>"<username>.zip"</tt>, replacing <tt><username></tt> with the user name from your Computer Science Department account. On a Linux system, the command for creating a ZIP file is:

<tt>zip <username>.zip *.java</tt>

Send your ZIP file to me by email at ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu. Include your full name and your computer account name in the email message, and include the ZIP file as an attachment.

When I get your email message, I will extract the contents of your ZIP file into a directory. I will set my Java class path to include the directory where I extracted your files, as well as the Parallel Java 2 Library. **_I will delete_** your version of SpotButton.java (if you included it) and replace it with the version linked off this web page. I will compile all the Java source files in your program using the JDK 1.8 compiler. I will then send you a reply message acknowledging I received your project and stating whether I was able to compile all the source files. If you have not received a reply within one business day (i.e., not counting weekends), please contact me. Your project is not successfully submitted until I have sent you an acknowledgment stating I was able to compile all the source files.

The submission deadline is Monday, October 31, 2016 at 11:59pm. The date/time at which your email message arrives in my inbox will determine whether your project meets the deadline.

You may submit your project multiple times up until the deadline. I will keep and grade only the most recent successful submission. There is no penalty for multiple submissions.

If you submit your project before the deadline, but I do not accept it (e.g. I can't compile all the source files), and you cannot or do not submit your project again before the deadline, the project will be late (see below). **_I strongly advise you to submit the project several days BEFORE the deadline, so there will be time to deal with any problems that may arise in the submission process._**

* * *

<a name="grading">

## Grading Criteria

</a>

I will grade your project by:

*   (10 points) Evaluating the design of your program, as documented in the Javadoc and as implemented in the source code.
    *   All of the [Software Design Criteria](#softwaredesign) are fully met: 10 points.
    *   Some of the [Software Design Criteria](#softwaredesign) are not fully met: 0 points.
*   (20 points) Running your project. I will have a test script consisting of 20 actions. An "action" consists of starting a client or server program, clicking a button or buttons, or closing a window. I will perform the actions one by one. **_If any action does not produce the required result, I will stop the test script. The number of points is equal to the number of actions successfully completed._**
*   (30 points) Total.

When I run your program, the Java class path will point to the directory with your compiled class files. I will use JDK 1.8 to run your server program and your client program.

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

Programming Project 3 must be entirely your own individual work. I will not tolerate plagiarism. If in my judgment the project is not entirely your own work, you will automatically receive, as a minimum, a grade of zero for the assignment. See the Course Policies for my [policy on plagiarism](../policies.shtml#plagiarism).

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

<td align="left" valign="baseline"><font size="-2">[4506](http://icasualties.org/Iraq/) + [2384](http://icasualties.org/oef/) = 6890</font></td>

</tr>

</tbody>

</table>

</td>

<td align="right" valign="top"><font size="-1">[Home Page](/~ark/)</font></td>

</tr>

</tbody>

</table>

<font size="-2">Copyright © 2016 Alan Kaminsky. All rights reserved. Last updated 11-Oct-2015. Please send comments to ark<font size="1" color="#FFFFFF">­</font>@<font size="1" color="#FFFFFF">­</font>cs.rit.edu.</font>
