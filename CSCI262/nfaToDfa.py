#!/usr/bin/env python3
#@author Egor Kozitski


# class for storing any automaton
class Automaton(object):
    """
    class initialization
    file_name: input file name with
    description automaton
    """
    def __init__(self, file_name):
        f = open(file_name)
        lines = f.read().splitlines()
        print(lines)
        self.states = {}
        self.alphabet = {'.'}
        self.transition = {}
        self.q_0 = ""  # start state
        self.final = {}  # accept states

        current_line = 0
        for i in range(len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.states = lines[current_line].split()  # read states

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.alphabet = {'.'}
        if lines[current_line][0] != '@':  # read alphabet
            alphabet_line = lines[current_line].split()
            self.alphabet |= set(alphabet_line)

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.q_0 = lines[current_line].split()[0]  # read start state

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.final = []
        if lines[current_line][0] != '@':  # read accept states
            self.final = lines[current_line].split()

        for i in range(current_line + 1, len(lines)):
            try:
                if lines[i][0] == '#' or not lines[i]:
                    continue
            except IndexError as ie:
                1+1

            words = lines[i].split()  # read transition function
            if not words:
                continue
            self.transition[words[0] + ',' + words[1]] = words[2:]

    def output(self, out):
        """
        outputs automaton to a file
        :param out: output file name
        """
        res = "# Q_ - the set of states\n" + str(self.states) + "\n# Sigma_ ­ the alphabet\n" + str(self.alphabet) + \
              '\n# q_0_ ­ the start state\n' + self.q_0 + "\n# F_ ­ the set of accept states\n" + str(self.final) + \
              "\n# delta_ ­ the transition function\n"
        for x in self.transition:
            res += str(x) + "\n"

        print(res)

    def __str__(self):
        """
        use with print function, for example: print(nfa)
        :return: printable version of automaton
        """
        res = "NFA:\nQ = " + str(self.states) + "\nSigma_e = " + str({x for x in self.alphabet if x != '.'}) + \
              "\ndelta = " + str(self.transition) + '\ns = ' + self.q_0 + \
              "\nF = " + str(self.final)
        return res


# class for storing deterministic finite automaton
# inherited from Automaton
class DFA(Automaton):
    def output(self, out):
        """
        outputs dfa to a file
        :param out: output file name
        """
        res = "# File: " + out + "\n# DFA\n# Q_ - the set of states\n"
        for q in self.states:
            res += q + ' '
        res = res[0:-1]
        res += "\n# Sigma_ ­ the alphabet\n"
        for a in self.alphabet:
            res += a + ' '
        res = res[0:-1]
        res += '\n# q_0_ ­ the start state\n' + self.q_0 + "\n# F_ ­ the set of accept states\n"
        for f in self.final:
            res += f + ' '
        res = res[0:-1]
        res += "\n# delta_ ­ the transition function\n"
        for x in self.transition:
            res += str(x).split(',')[0] + " " + str(x).split(',')[1] + " " + self.transition[x] + "\n"
        f = open(out, 'w')
        f.write(res)
        f.close()

    def __str__(self):
        """
        use with print function, for example: print(dfa)
        :return: printable version of automaton
        """
        res = "DFA:\nQ = " + str(self.states) + "\nSigma_ = " + str(self.alphabet) + \
              "\ndelta = " + str(self.transition) + '\ns = ' + self.q_0 + \
              "\nF = " + str(self.final)
        return res

    def clean_states(self):
        """
        removes unreachable states
        """
        to_remove = []
        for s in self.states:
            if s != self.q_0 and s not in self.transition.values():
                to_remove.append(s)
        for s in to_remove:
            if s in self.final:
                self.final.remove(s)

            self.states.remove(s)


def subsets(my_set):
    """
    :param my_set: input set of elements
    :return: set of all possible subsets
    of the input set
    """
    result = [[]]
    for x in my_set:
        result += [y + [x] for y in result]
    for x in result:
        x.sort()
    return result


def nfa_states_to_dfa(states):
    """
    :param states: nfa states
    :return: dfa states
    """
    states_temp = list(states)
    result = subsets(states_temp)
    s = {'@'}
    for state in result:
        temp = ""
        for x in state:
            temp += '-' + x
        temp = temp[1:]
        s.add(temp)

    if '' in s:
        s.remove('')
    return s


def get_dfa_start_state(nfa_start, nfa_transition):
    """
    :param nfa_start: nfa start state
    :param nfa_transition: nfa transition function
    :return: dfa start state
    """
    to_visit = [nfa_start]
    visited = []
    while True:
        if not to_visit:
            break
        e_reachable = nfa_transition.get(to_visit[0] + ',.', 0)
        visited.append(to_visit.pop(0))
        if e_reachable == 0:
            continue
        for x in e_reachable:
            if x not in visited and x not in to_visit:
                to_visit.append(x)
    visited_set = set(visited)
    visited = list(visited_set)
    visited.sort()
    res = visited[0]
    for x in range(1, len(visited)):
        res += '-' + visited[x]

    return res


def get_accept_states_dfa(dfa_states, nfa_accept_states):
    """
    :param dfa_states: dfa states
    :param nfa_accept_states: nfa accept states
    :return: dfa accept states
    """
    res = set()
    for x in dfa_states:
        for y in nfa_accept_states:
            if y in x:
                res.add(x)
    return res


def get_new_state(current_state, a, transition):
    """
    :param current_state: current dfa state
    :param a: element from the alphabet
    :param transition: nfa transition function
    :return: created new state
    """
    _R = current_state.split('-')
    result = []
    for q in _R:
        key = q + ',' + a
        _states = []
        if key in transition.keys():
            _states = transition[key]

        for s in _states:
            temp = get_dfa_start_state(s, transition)
            if not temp:
                continue
            temp = temp.split('-')
            if s not in result:
                result.append(s)
            for x in temp:
                if x not in result:
                    result.append(x)
    if not result:
        return "@"
    if len(result) == 1:
        return result[0]
    result = set(result)
    result = list(result)
    result.sort()
    res = ""
    for state in result:
        res += state + '-'

    return res[0:-1]


def get_dfa_transition(_dfa, nfa_transition):
    """
    :param _dfa: input dfa with no transition function yet
    :param nfa_transition: nfa transition function
    :return: dfa transition function
    """
    to_visit = [_dfa.q_0]
    visited = []
    sigma_ = {}
    while to_visit:
        current = to_visit.pop(0)
        visited.append(current)
        for a in _dfa.alphabet:
            rule = current + ',' + a
            new_state = get_new_state(current, a, nfa_transition)
            if not new_state:
                continue
            sigma_[rule] = new_state
            if new_state not in visited and new_state not in to_visit:
                to_visit.append(new_state)
    return sigma_


# ask for input file name
in_filename = input("NFA specification file name: ")

# creates non-deterministic automaton from file
nfa = Automaton(in_filename)

# creates dfa similar to nfa
dfa = DFA(in_filename)

# removes epsilon from dfa alphabet
dfa.alphabet.remove('.')

# prints nfa
print(nfa)

# finds dfa states
dfa.states = nfa_states_to_dfa(nfa.states)

# gets dfa start state
dfa.q_0 = get_dfa_start_state(nfa.q_0, nfa.transition)

# finds dfa accepted states
dfa.final = get_accept_states_dfa(dfa.states, nfa.final)

# calculates dfa transition function
dfa.transition = get_dfa_transition(dfa, nfa.transition)

# removes unreachable states from dfa
dfa.clean_states()

# print dfa
print(dfa)

# asks for output file name
out_filename = input("Output file name (Equivalent DFA): ")

# confirms output action
print("Writing to file: " + out_filename)

# outputs dfa
dfa.output(out_filename)
