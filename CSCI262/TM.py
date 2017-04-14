from os.path import isfile, split

class Command:

    def __init__(self, start_state, check_symb, end_state, write_symb, move_direc):
        '''constructor for Command class'''
        self.start_state = start_state
        self.end_state = end_state
        self.check = check_symb
        self.move_direc = move_direc
        self.write_symb = write_symb

    def execute(self, tape, position):
        '''this method implements executing of command'''
        tape[position] = self.write_symb
        if self.move_direc == "L" and position == 0:
            next_position = 0
        elif self.move_direc == "L":
            next_position = -1
        elif position + 1 == len(tape):
            tape.append('u')
            next_position = 1
        else:
            next_position = 1
        return self.end_state, next_position



class TM:

    def __init__(self, **parameters_dict):
        '''constructor for TM class'''
        self.states = parameters_dict['states']
        self.input_alphabet = parameters_dict['alphabet_in']
        self.output_alphabet = parameters_dict['alphabet_out']
        self.current_state = parameters_dict['start_state']
        self.accept_state = parameters_dict['accept_state']
        self.reject_state = parameters_dict['reject_state']
        self.commands = [Command(*string.split()) for string in parameters_dict['commands']]
        self.tape = list()
        self.current_position = 0

    def __str__(self):
        '''this function returns string representation of current state of TM'''
        return ''.join(self.tape[:self.current_position])+self.current_state+''.join(self.tape[self.current_position:])


    def set_tape(self, _input):
        '''insert _input in left side of the tape'''
        for symbol in _input:
            self.tape.append(symbol)

    def run(self):
        '''this method implements working loop of TM'''
        while True:
            print(self)
            command = [com for com in self.commands if com.start_state == self.current_state and
                       self.tape[self.current_position] == com.check]
            if len(command) == 0:
                self.current_state = self.reject_state
                if self.current_position + 1 == len(self.tape):
                    self.tape.append('u')
                self.current_position += 1
            else:
                new_state, move = command[0].execute(self.tape, self.current_position)
                self.current_state, self.current_position = new_state, self.current_position + move
            if self.current_state == self.reject_state or self.current_state == self.accept_state:
                break
        print(self)



def open_file():
    '''this function prompts user to input file name or path to the file with TM specification'''
    while True:
        file_name = input("Input path to the file with TM specification : ")
        try:
            assert isfile(file_name)
            return open(file_name), file_name
        except AssertionError:
            print('Path or file name is incorrect, there is no such file')


def parse_specification(_file):
    '''this function reads file and parses it and then returns specification of TM as a dictionary'''
    rows = _file.read().strip('\n').split('\n')
    data = [row.strip() for row in rows if row.strip() != '' and row.strip()[0] != '#']
    specification = {}
    specification['states'] = data[0].split()
    specification['alphabet_in'] = data[1].split()
    specification['alphabet_out'] = data[2].split()
    specification['start_state'] = data[3]
    specification['accept_state'] = data[4]
    specification['reject_state'] = data[5]
    specification['commands'] = data[6:]
    return specification


def main():
    '''this function implements logic of program'''
    file, file_name = open_file()
    specification = parse_specification(file)
    print('Turing Machine specification file name: ' + split(file_name)[1])

    while True:
        machine = TM(**specification)
        new_tape = input('-->')
        if new_tape == '':
            print('Goodbye')
            break
        machine.set_tape(new_tape)
        machine.run()

if __name__  == '__main__':
    main()
