def init_terminal(width, height):
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    _TERMINAL_WIDTH  = width
    _TERMINAL_HEIGHT = height

def cls():
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    print("\n"*_TERMINAL_HEIGHT)

def sep(char):
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    print(char*_TERMINAL_WIDTH)

def print_boxed(line, offset, char):
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    print(
        char+
        clip_line(
            right_pad(line, " ", _TERMINAL_WIDTH),
            offset,
            _TERMINAL_WIDTH-2
        )+
        char
    )

def clip_line(line, offset, maxwidth):
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    maxwidth = min(maxwidth,_TERMINAL_WIDTH)
    line = " "*offset+line
    line = line[:maxwidth]
    return line

def right_pad(line, char, width):
    if len(line)>width:
        return line
    return line+char*(width-len(line))

def print_screen_default(calc):
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    cls()
    sep("-")
    print_boxed("Filip's RPN Calculator", int((_TERMINAL_WIDTH-22-2)/2), "|")
    sep("-")
    max_lines = _TERMINAL_HEIGHT - 6
    needed_lines = calc.STACK_P
    for i in range(max(max_lines-needed_lines,0)):
        print_boxed(" ", 0, "|")
    for i in range(max(needed_lines-max_lines, 0),calc.STACK_P):
        print_boxed(str(i)+" : "+str(calc.STACK[i]), 0, "|")
    sep("-")
    print_boxed("Operations: +-*/ | Functions: sqrt pow | Controls: d:drop h:help q:quit", 0, "|")

def print_screen_help():
    global _TERMINAL_WIDTH, _TERMINAL_HEIGHT
    cls()
    sep("-")
    print_boxed("Filip's RPN Calculator", 0, "|")
    print_boxed("(c) 2026", 0, "|")
    print_boxed("", 0, "|")
    print_boxed("This simple calculator program operates using Reverse Polish Notation.", 0, "|")
    print_boxed("This means, that any number entered into the program is put at the top of the stack,", 0, "|")
    print_boxed("represented as the position at the bottom of the number list.", 0, "|")
    print_boxed("When the user enters a command, the required amount of elements will be pulled from", 0, "|")
    print_boxed("the stack, and the result will be put back instead.", 0, "|")
    print_boxed("", 0, "|")
    print_boxed("Supported mathematical oprators are +, -, * and /, representing respectively addition,", 0, "|")
    print_boxed("subtraction, multiplication and division.", 0, "|")
    print_boxed("", 0, "|")
    print_boxed("In addition to basic mathematical operators, the program also provides several more", 0, "|")
    print_boxed("complex mathematical functions, those being:", 0, "|")
    print_boxed(" * sqrt: calcualtes the square root of the newest element of the list", 0, "|")
    print_boxed(" * pow : raises the second-to-last element to the power of the last element", 0, "|")
    print_boxed("", 0, "|")
    print_boxed("Lastly, the program provides a few control commands:", 0, "|")
    print_boxed(" * d: drop - removes the last element of the list", 0, "|")
    print_boxed(" * h: help - displays this helpful screen", 0, "|")
    print_boxed(" * q: quit - exits the program", 0, "|")
    sep("-")
    input("Press Enter")