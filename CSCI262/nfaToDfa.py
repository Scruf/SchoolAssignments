import string


class Automaton(object):
    def __init__(self, file_name):
        f = open(file_name)
        lines = f.readlines()
        self.states = {}
        self.alphabet = {'.'}
        self.transition = {}
        self.q_0 = ""
        self.final = {}

        current_line = 0
        for i in range(len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.states = lines[current_line].split()

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.alphabet = lines[current_line].split()

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.q_0 = lines[current_line].split()[0]

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] != '#':
                current_line = i
                break

        self.final = lines[current_line].split()

        for i in range(current_line + 1, len(lines)):
            if lines[i][0] == '#':
                continue

            words = lines[i].split()
            self.transition[words[0] + ',' + words[1]] = words[2:]

    def output(self, out):
        res = "# Q_ - the set of states\n" + str(self.states) + "\n# Sigma_ ­ the alphabet\n" + str(self.alphabet) + \
              '\n# q_0_ ­ the start state\n' + self.q_0 + "\n# F_ ­ the set of accept states\n" + str(self.final) + \
              "\n# delta_ ­ the transition function\n"
        for x in self.transition:
            res += str(x) + "\n"

        print(res)

    def __str__(self):
        res = "NFA:\nQ = " + str(self.states) + "\nSigma_e = " + str(self.alphabet) + \
              "\ndelta = " + str(self.transition) + '\ns = ' + self.q_0 + \
              "\nF = " + str(self.final)
        return res


class DFA(Automaton):
    def output(self, out):
        res = "# File: " + out + "\n# DFA\n# Q_ - the set of states\n" + str(
            self.states) + "\n# Sigma_ ­ the alphabet\n" + str(self.alphabet) + \
              '\n# q_0_ ­ the start state\n' + self.q_0 + "\n# F_ ­ the set of accept states\n" + str(self.final) + \
              "\n# delta_ ­ the transition function\n"
        for x in self.transition:
            res += str(x).split(',')[0] + " " + str(x).split(',')[1] + " " + self.transition[x] + "\n"
        f = open(out, 'w')
        f.write(res)
        f.close()

    def __str__(self):
        res = "DFA:\nQ = " + str(self.states) + "\nSigma_e = " + str(self.alphabet) + \
              "\ndelta = " + str(self.transition) + '\ns = ' + self.q_0 + \
              "\nF = " + str(self.final)
        return res


def subsets(my_set):
    result = [[]]
    for x in my_set:
        result += [y + [x] for y in result]
    return result


def nfa_states_to_dfa(states):
    states_temp = list(states)
    result = subsets(states_temp)#[sublist for size in range(1, len(states_temp) + 1) for sublist in
             # (states_temp[x:x + size] for x in range(len(states_temp) - size + 1))
             # ]
    s = {'@'}
    for state in result:
        temp = ""
        for x in state:
            temp += '-' + x
        temp = temp[1:]
        s.add(temp)
    return s


def get_dfa_start_state(nfa_start, nfa_transtion):
    e_reachable = nfa_transtion.get(nfa_start + ',.', 0)
    if e_reachable == 0:
        return nfa_start
    res = nfa_start
    for x in e_reachable:
        res += '-' + x

    return res


def get_accept_states_dfa(dfa_states, nfa_accept_states):
    res = set()
    for x in dfa_states:
        for y in nfa_accept_states:
            if y in x:
                res.add(x)
    return res


def get_new_state(current_state, a, transition):
    _R = current_state.split('-')
    result = []
    for q in _R:
        key = q + ',' + a
        _states = []
        if key in transition.keys():
            _states = transition[key]

        for s in _states:
            temp = s + ',.'
            if temp in transition:
                result.extend(transition[temp])
    if not result:
        return ""
    if len(result) == 1:
        return result[0]
    result.sort()
    res = ""
    for state in result:
        res += state + '-'

    return res[0:-1]


def get_dfa_transition(_dfa, nfa_transition):
    to_visit = [_dfa.q_0]
    visited = []
    sigma_ = {}
    while to_visit:
        current = to_visit[0]
        visited.extend(current)
        to_visit.pop(0)
        for a in _dfa.alphabet:
            rule = current + ',' + a
            new_state = get_new_state(current, a, nfa_transition)
            if not new_state:
                continue
            sigma_[rule] = new_state
            if new_state not in visited and new_state not in to_visit:
                to_visit.extend(new_state)
    return sigma_


in_filename = input("NFA specification file name: ")
nfa = Automaton(in_filename)
dfa = DFA(in_filename)
print(nfa)
dfa.states = nfa_states_to_dfa(nfa.states)
dfa.q_0 = get_dfa_start_state(nfa.q_0, nfa.transition)
dfa.final = get_accept_states_dfa(dfa.states, nfa.final)
dfa.transition = get_dfa_transition(dfa, nfa.transition)


out_filename = input("Output file name (Equivalent DFA): ")
print("Writing to file: " + out_filename)
print(dfa)
dfa.output(out_filename)
