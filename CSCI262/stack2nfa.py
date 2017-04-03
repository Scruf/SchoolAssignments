import copy
"""
    @author Egor Kazitski
"""
operations = ['=push', '=print', '=union', '=concat', '=star']  # list of accepted operations


# class for storing any automaton
class Automaton(object):
    """
    class initialization
    nfa_lines: list of string with
    automaton description
    """
    def __init__(self, nfa_lines=()):
        self.states = {}
        self.alphabet = {'.'}
        self.transition = {}
        self.q_0 = ""  # start state
        self.final = {}  # accept states

        if nfa_lines:
            current_line = 0
            for i in range(len(nfa_lines)):
                if nfa_lines[i][0] != '#':
                    current_line = i
                    break

            self.states = nfa_lines[current_line].split()  # read states

            for i in range(current_line + 1, len(nfa_lines)):
                if nfa_lines[i][0] != '#':
                    current_line = i
                    break

            self.alphabet = {'.'}
            if nfa_lines[current_line][0] != '@':  # read alphabet
                alphabet_line = nfa_lines[current_line].split()
                self.alphabet |= set(alphabet_line)

            for i in range(current_line + 1, len(nfa_lines)):
                if nfa_lines[i][0] != '#':
                    current_line = i
                    break

            self.q_0 = nfa_lines[current_line].split()[0]  # read start state

            for i in range(current_line + 1, len(nfa_lines)):
                if nfa_lines[i][0] != '#':
                    current_line = i
                    break

            self.final = []
            if nfa_lines[current_line][0] != '@':  # read accept states
                self.final = nfa_lines[current_line].split()

            for i in range(current_line + 1, len(nfa_lines)):
                if nfa_lines[i][0] == '#' or not nfa_lines[i]:
                    continue

                words = nfa_lines[i].split()  # read transition function
                if not words:
                    continue
                self.transition[words[0] + ',' + words[1]] = words[2:]

    def output(self, out):
      
        """
        outputs automaton to a file
        :param out: output file name
        """
        res = "# File: " + out + "\n# NFA\n# Q_ - the set of states\n"
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
            splitted = list(str(x).split(','))
            res += splitted[0] + " " + splitted[1]
            for i in self.transition[x]:
                res += " " + i
            res += '\n'
        f = open(out, 'w')
        f.write(res)
        f.close()

    def __str__(self):
        """
        use with print function, for example: print(nfa)
        :return: printable version of automaton
        """
        res = "NFA:\nQ = " + str(self.states) + "\nSigma_e = " + str({x for x in self.alphabet if x != '.'}) + \
              "\ndelta = " + str(self.transition) + '\ns = ' + self.q_0 + \
              "\nF = " + str(self.final)
        return res

    def _add_state(self, prefix):
        """
        private function,
        adds prefix to each state of automaton
        :param prefix: prefix to add
        """
        for i in range(len(self.states)):
            self.states[i] = prefix + self.states[i]

        self.q_0 = prefix + self.q_0

        for i in range(len(self.final)):
            self.final[i] = prefix + self.final[i]

        keys = list(self.transition.keys())
        for key in keys:
            new_key = prefix + key
            self.transition[new_key] = []
            for i in range(len(self.transition[key])):
                self.transition[new_key].append(prefix + self.transition[key][i])
            del self.transition[key]

    def transform(self, prefix):
        """
        adds prefix to each state of automaton
        :param prefix: prefix to add
        :return: nfa_transformed transformed automaton
        """
        nfa_transformed = copy.deepcopy(self)
        nfa_transformed._add_state(prefix)
        return nfa_transformed

    def add_epsilon_transitions(self, state):
        """
        adds epsilon transitions from new state and from final states
        to start state
        :param state: new state to add
        """
        self.states.append(state)
        self.transition[state + ', .'] = [self.q_0]
        for s in self.final:
            self.transition[s + ', .'] = [self.q_0]

    def change_start_state(self, state):
        """
        changes start state
        :param state: new start state
        """
        self.q_0 = state


# def read_regexp(file_name):
#    f = open(file_name)
#    return f.readlines()[0]


# class for storing stack
class Stack(object):
    def __init__(self, file_name):
        """
        class initialization
        :param file_name: name of file
        with stack description
        """
        self.aut_stack = []
        self.aut_to_push = []
        self.operations = []
        self._read(file_name)

    def _read(self, file_name):
        """
        reads stack from file
        :param file_name: name of file
        with stack description
        """
        f = open(file_name)
        lines = f.readlines()
        begin = 0
        end = 0
        while end < len(lines):
            op = ''
            for l in lines[begin:]:
                end += 1
                op = l.split()[0]
                if op in operations:
                    self.operations.append(op)
                    break
            if op == '=push':
                nfa = Automaton(lines[begin:end - 1])
                self.aut_to_push.append(nfa)
            begin = end
        f.close()

    def star(self):
        """
        applies star operator
        """
        nfa = self.aut_stack.pop()

        nfa_star = nfa.transform('X')
        nfa_star.add_epsilon_transitions('S')
        nfa_star.change_start_state('S')
        nfa_star.final.append('S')

        self.aut_stack.append(nfa_star)

    def push(self):
        """
        adds automatons from queue to the stack
        """
        self.aut_stack.append(self.aut_to_push.pop())

    def concat(self):
        """
        concatenation of two
         top automatons
        """
        nfa2 = self.aut_stack.pop()
        nfa1 = self.aut_stack.pop()

        nfa1_star = nfa1.transform('X')
        nfa2_star = nfa2.transform('Y')

        nfa_concat = Automaton()
        nfa_concat.final = nfa2_star.final
        nfa_concat.q_0 = nfa1_star.q_0
        nfa_concat.states = list(set(nfa1_star.states).union(nfa2_star.states))
        nfa_concat.alphabet = list(set(nfa1_star.alphabet).union(nfa2_star.alphabet))
        nfa_concat.transition = dict(nfa1_star.transition, **nfa2_star.transition)
        for a in nfa1_star.final:
            key = a + ', .'
            if nfa_concat.transition.get(key, 0) == 0:
                nfa_concat.transition[key] = [nfa2_star.q_0]
            else:
                nfa_concat.transition[key].append(nfa2_star.q_0)

        self.aut_stack.append(nfa_concat)

    def union(self):
        """
        union of two top automatons
         in the stack
        """
        nfa2 = self.aut_stack.pop()
        nfa1 = self.aut_stack.pop()

        nfa1_star = nfa1.transform('X')
        nfa2_star = nfa2.transform('Y')

        nfa_union = Automaton()
        nfa_union.states = list(set(nfa1_star.states).union(nfa2_star.states))
        nfa_union.states.append('S')
        nfa_union.alphabet = list(set(nfa1_star.alphabet).union(nfa2_star.alphabet))
        nfa_union.final = list(set(nfa1_star.final).union(nfa2_star.final))
        nfa_union.change_start_state('S')
        nfa_union.transition = dict(nfa1_star.transition, **nfa2_star.transition)
        nfa_union.transition['S, .'] = [nfa1_star.q_0, nfa2_star.q_0]

        self.aut_stack.append(nfa_union)

    def print(self):
        """
        prints out resulting nfa,
        stops the interpreter
        :return: resulting nfa
        """
        nfa = self.aut_stack.pop()
        return nfa


def stack2nfa(stack):
    """
    converts stack to nfa
    :param stack: input stack
    :return: output nfa
    """
    for op in stack.operations:

        if op == '=push':
            stack.push()

        if op == '=star':
            stack.star()

        if op == '=concat':
            stack.concat()

        if op == '=union':
            stack.union()

        if op == '=print':
            return stack.print()


# main function
if __name__ == '__main__':

    in_filename = input("Input file name: ")


    out_filename = input("Output file name: ")
   
    stack_ = Stack(in_filename)
    nfa_ = stack2nfa(stack_)
   
    nfa_.output(out_filename)
