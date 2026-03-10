import random

x = int(input("Pin length: "))

pin = ""
for i in range(x):
    pin += str(random.randint(0, 9))

print(pin)
input()
