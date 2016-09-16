
Name :Egor name ...
Time spent on assignment : 48 hours +- 6 hours time spent 

Solution
The problem was solved by modifying eval.c file. I added new value environment for locals. Variable is getting passed through eval.c . Majority of changes were done in the set and var where the semanics for requiring local variable was added to the same as gloabl. The variables in the local envirionment were initialized to 0. 


 