import math

def init_calc():
    global STACK, STACK_P
    STACK = [0.0]*256
    STACK_P = 0

def push(num):
    global STACK, STACK_P
    STACK[STACK_P] = num
    STACK_P += 1

def pop():
    global STACK, STACK_P
    if STACK_P > 0:
        STACK_P -= 1
        return STACK[STACK_P]
    return None

def add():
    global STACK, STACK_P
    if STACK_P > 1:
        b = pop()
        a = pop()
        try:
            c = a + b
        except OverflowError:
            c = float('inf')*a
        push(c)

def sub():
    global STACK, STACK_P
    if STACK_P > 1:
        b = pop()
        a = pop()
        try:
            c = a - b
        except OverflowError:
            c = float('inf')*a
        push(c)

def mul():
    global STACK, STACK_P
    if STACK_P > 1:
        b = pop()
        a = pop()
        try:
            c = a * b
        except OverflowError:
            c = float('inf')*a
        push(c)

def div():
    global STACK, STACK_P
    if STACK_P > 1:
        b = pop()
        a = pop()
        try:
            c = a / b
        except ZeroDivisionError:
            c = float('NaN')
        except ValueError:
            c = float('NaN')
        except OverflowError:
            c = float('inf')*a
        push(c)

def sqrt():
    global STACK, STACK_P
    if STACK_P > 0:
        a = pop()
        try:
            c = math.sqrt(a)
        except ValueError:
            c = float('NaN')
        except OverflowError:
            c = float('inf')*a
        push(c)

def pow():
    global STACK, STACK_P
    if STACK_P > 1:
        b = pop()
        a = pop()
        try:
            c = math.pow(a, b)
        except ValueError:
            c = float('NaN')
        except OverflowError:
            c = float('inf')*a
        push(c)