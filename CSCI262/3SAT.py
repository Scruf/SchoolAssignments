"""
Name:  Your name, please

The 3SAT problem is as follows:
A 3SAT formula is a list of logic clauses in conjunctive normal form,
where each clause has exactly three terms in disjunctive form and where a
term consists of either a variable symbol V or its logical negation ~V.
A solution to a 3SAT formula consists of assigning truth values to each
of the variables appearing in the formula in such a way that the formula
evaluates to true.  Given an arbitrary 3SAT formula, the 3SAT problem
is to devise an algorithm that provides a solution to the formula or
that determines that the formula has no solution. If n is the number of
variables appearing in the formula, it is not known if there is an
algorithm that solves the 3SAT problem in polynomial time in n.

This program reads line from an input file containing
a 3SAT formula. Each line in the file represents a single clause in the
3SAT forumla: a disjunction of exactly three terms, where a term is a
variable string of the form V or its negation ~V.  The three terms in a
line are separated by commas.  A variable string V starts with a letter
(upper- or lower-case) and can contain zero or more additional letters,
digits, or underscores.

Lines in the file that consist only of whitespace or whose first
non-whitespace character is a '#' should be ignored. The end of the
formula is represented by a line consisting only of the word 'END'.

The program then prints all of the solutions to the 3SAT formula, with
each solution displayed on a single line in the form

    Solution: ...

If there are no solutions, the program prints a single line of the form

    No solution

The program has four functions: read3SAT, genCertificates, testClauses,
and getSolutions. 

The 'read3SAT' function in this program prompts the user for a file name
and reads lines from the file until it reaches a line with the word
'END'. It returns a tuple containing the set of variables that appear in
the formula and a list of clauses of the formula.

Each line of the file contains a clause -- a comma-separated list of
three terms. The read3SAT function creates a list 
containing the three terms of this line. Each such list is then appended
to a list of clauses.  The function also creates a set containing the
variables obtained from the terms: if a term has a leading '~', the
variable is whatever follows the '~' character, otherwise the variable
is the term itself. The read3SAT function returns a tuple containing the
set of variables and the list of clauses. You required to implement the
read3SAT function, which appears as a stub below.

The genCertificates function takes a set of variables and returns
a Python 'generator' -- an object that can be used in a for loop --
that produces all possible certificates, where a certificate is a list
of these variables and their negations. This function is provided to
you. Consider, for example, the set {'x1', 'x2'} of variables, and the
following Python3 code:

    for lst in genCertificates({'x1', 'x2'}):
        print(lst)

The output will be:

    ['x1', 'x2']
    ['~x1', 'x2']
    ['x1', '~x2']
    ['~x1', '~x2']

You are given the code for genCertificates below.

The testClauses function takes one of the certificates that is produced
by genCertificates and the list of clauses returned by read3SAT, and
returns True if the certificate satisfies all of the clauses, and returns
False otherwise.

For a certificate to satisfy a clause, the clause must have at least
one term that belongs to the certificate.  If the certificate satisfies
all of the clauses, the certificate is said to be a solution to the
3SAT formula, and the function returns True. If there is a clause that
does not have at least one term that belongs to the certificate, the
testClauses function will return False.

You are to write the testClauses function. A stub is provided for
you below.

The getSolutions function takes a set of variables and a list of clauses
as returned by read3SAT and prints all certificates that are solutions to
the 3SAT formula. This function uses the genCertificates and testClauses
functions. If a particular certificate is a solution, the getSolutions
function should print a line such as:

    Solution: ['~x1', 'x2']

All the solutions to the 3SAT formula should be printed. If no solutions
are found, the getSolutions function should print the line

    No solution

You are to write the getSolutions function. A stub is provided for
you below.

# example input file
~x1,~x2,~x2
~x1,x2,x2
x1,x1,x2
END

The set of variables in this example is

    {'x1', 'x2'}

and the list of clauses is

    [['~x1', '~x2','~x2'], ['~x1', 'x2', 'x2'], ['x1', 'x1', 'x2']]

For the example 3SAT formula given here, the only solution certificate is

    ['~x1', 'x2']

"""

"""
Prompts for a filename from the user and reads 3SAT clauses from the file.
Returns: ( set of variables, list of clauses )
"""
def read3SAT():
    filename = input("Enter file name: ")
    file = open(filename)
    vars = set()
    clauses = []
    for line in file:
        # prepare data and check for comments/end
        line = line.strip()
        if line[0] == '#':
            continue
        elif line == "END":
            break
        # add clause
        clause = line.split(',')
        clauses.append(clause)
        # add vars
        for var in clause:
            if var.startswith('~'):
                var = var[1:]
            if var not in vars:
                vars.update([var])
    return (vars, clauses)

"""
Parameters:
  certificate - a proposed solution as a list of variable settings as strings
                e.g. ['x1', '~x2']
  clauses - the set of clauses as a list of list of terms as strings:
                e.g. [['~x1','~x2','~x2'],['~x1','x2','x2'],['x1','x1','x2']]
Returns:
  True if this certificate satisfies all clauses in the formula, False otherwise
"""
def testClauses(certificate, clauses):
    for clause in clauses:
        for var in clause:
            if var in certificate:
                # then clause is True and we can check next clause
                break
        else:
            # if vars in clause checked as False then clause is False and certificate is False
            return False
    # if all clauses is True, certificate is True
    return True

"""
Parameter:
    vars - a set of variables as strings
Returns: an iterator producing certificates containing terms of the form
V or ~V for all variables V in the set vars.  Successive calls to
the iterator will go through all possible cases of V and ~V for all
variables. The iterator will terminate after all certificates have
been generated. The terms in the certificates appear in sorted order
by variable name.
"""
def genCertificates(vars):
    vars = sorted(list(vars))
    n = 0
    while True:
        certificate = []
        bits = n
        for v in vars:
             if bits%2 == 1:
                 v = '~' + v
             certificate.append(v)
             bits = bits//2
        if bits == 0:
             yield certificate
             n += 1
        else:
             return # no more certificates
"""
Parameters:
   vars - a set of variables as strings e.g. {'x1', 'x2'}
   clauses - the set of clauses as a list of list of terms as strings:
                e.g. [['~x1','~x2','~x2'],['~x1','x2','x2'],['x1','x1','x2']]

Prints out all certificates that satisfy all clauses in the 3SAT formula
Prints 'No solution' if there is no certificate that satisfies this formula
"""
def getSolutions(vars, clauses):
    sol = False
    for cert in genCertificates(vars):
        if testClauses(cert, clauses):
            print("Solution: {}".format(cert))
            sol = True
    if not sol:
        print("No solution")


def main():
    (vars, clauses) = read3SAT()
    getSolutions(vars, clauses)

if __name__ == '__main__':
    main()
