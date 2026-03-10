import os

import display as disp
import calc

w, h = os.get_terminal_size()
# disp.init_terminal(80, 40)
disp.init_terminal(w, h)
calc.init_calc()

run = True
while run:
    disp.print_screen_default(calc)
    x = input("?:")
    if x=="q":
        run = False
    elif x=="h":
        disp.print_screen_help()
    elif x=="d":
        calc.pop()
    elif x=="sqrt":
        calc.sqrt()
    elif x=="pow":
        calc.pow()
    elif x=="+":
        calc.add()
    elif x=="-":
        calc.sub()
    elif x=="*":
        calc.mul()
    elif x=="/":
        calc.div()
    else:
        try:
            calc.push(float(x))
        except ValueError:
            print("Not a float")