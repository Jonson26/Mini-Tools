import pygame
import time
import random
import math

#set program flags
#modes: "GAME", "DISPLAY_TEST", "INPUT_TEST"
mode = "GAME"

#set rgb colors
lightgray = (211,211,211)
brick     = (188, 74, 60)
darkgreen = (  1, 50, 32)
black     = (  0,  0,  0)
white     = (255,255,255)

#set square size
size = 16

#set board dimensions
b_w = 60
b_h = 60

#init board
#0 - empty square
#1 - building
#2 - infested square
#3 - barricade
global board
# board = [
    # [0, 1, 1, 0, 1, 1, 1, 0, 1, 1],
    # [0, 1, 0, 0, 1, 0, 0, 0, 0, 0],
    # [0, 1, 0, 0, 1, 0, 0, 2, 0, 1],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 1],
    # [0, 1, 0, 0, 1, 0, 1, 1, 0, 0],
    # [0, 1, 0, 1, 1, 0, 1, 1, 0, 1],
    # [0, 1, 0, 1, 0, 0, 0, 0, 0, 1],
    # [0, 1, 0, 0, 0, 0, 0, 0, 0, 1],
    # [0, 1, 1, 1, 0, 1, 1, 1, 0, 1],
    # [0, 0, 0, 0, 0, 0, 0, 0, 0, 1]
    # ]
board = [ [0]*b_w for i in range(b_h)]

prevboard = [ [0]*b_w for i in range(b_h)]

#set color map
cmap = {
    0 : lightgray,
    1 : brick,
    2 : darkgreen
    }
    
#set patterns
pat1 = [
    [-1, -1], [0, -1], [1, -1],
    [-1,  0], [0,  0], [1,  0],
    [-1,  1], [0,  1], [1,  1],
]
pat2 = [
              [0, -1],
    [-1,  0], [0,  0], [1,  0],
              [0,  1],
]
pat3 = [
                        [0, -2],
              [-1, -1], [0, -1], [1, -1],
    [-2,  0], [-1,  0], [0,  0], [1,  0], [2,  0],
              [-1,  1], [0,  1], [1,  1],
                        [0,  2],
]

def genboard_1():
    global board
    board = [ [0]*b_w for i in range(b_h)]
    for i in range(random.randint(b_w*int(math.log2(b_w*b_h)), (b_w+b_h)*int(math.log2(b_w*b_h)))):
        board[random.randint(0, b_h-1)][random.randint(0, b_w-1)] = 1
    board[random.randint(0, b_h-1)][random.randint(0, b_w-1)] = 2
    
def genboard_2():
    global board
    board = [ [0]*b_w for i in range(b_h)]
    for y in range(b_h):
        if random.randint(0,10)>3:
            for x in range(b_w):
                if random.randint(0,10)>3:
                    board[y][x] = 1
    board[random.randint(0, b_h-1)][random.randint(0, b_w-1)] = 2

def genboard_3(n):
    global board
    board = [ [0]*b_w for i in range(b_h)]
    y = 0
    while y<b_h:
        y_step = min(random.randint(2, 6), b_h-y)
        x = 0
        while x<b_w:
            x_step = min(random.randint(2, 6), b_w-x)
            for i in range(y_step):
                for j in range(x_step):
                    board[y+i][x+j] = 1
            x+=x_step
            x+=min(random.randint(1, 2), b_w-x)
        y+=y_step
        y+=min(random.randint(1, 3), b_h-y)
    for i in range(n):
        y = random.randint(0, b_h-1)
        x = random.randint(0, b_w-1)
        while board[y][x]!=0:
            y = random.randint(0, b_h-1)
            x = random.randint(0, b_w-1)
        board[y][x] = 2

#draw board
def drawBoard(disp):
    for x in range(0, b_w):
        for y in range(0, b_h):
            if board[x][y] == 3:
                drawBarricade(disp, x, y)
            else:
                pygame.draw.rect(disp, cmap[board[x][y]], (size*y, size*x, size, size))

#draw barricade
def drawBarricade(disp, x, y):
    pygame.draw.rect(disp, white, (size*y, size*x, size, size))
    pygame.draw.line(disp, black, (y*size, x*size), ((y+1)*size, (x+1)*size))
    pygame.draw.line(disp, black, (y*size, (x+1)*size), ((y+1)*size, x*size))

#check neighbour
def checkNeighbour(x, y, c, pat):
    c_o = 0
    for a in range(len(pat)):
        i = pat[a][0]
        j = pat[a][1]
        x_l = x + i
        y_l = y + j
        #print(x_l, y_l)
        if x_l>=0 and x_l<b_w and y_l>=0 and y_l<b_h:
            if board[x_l][y_l] == c:
                c_o+=1
    return c_o

#update board
def updateBoard(pat):
    newboard = [ [0]*b_w for i in range(b_h)]
    for x in range(0, b_w):
        for y in range(0, b_h):
            c = board[x][y]
            newboard[x][y] = c
            if c == 0:
                n = checkNeighbour(x, y, 2, pat)
                if n>0:
                    newboard[x][y] = 2
    for i in range(0, b_w):
        for j in range(0, b_h):
            prevboard[i][j] = board[i][j]
            board[i][j] = newboard[i][j]

def checkGrowth():
    pg=0
    cg=0
    for i in range(0, b_w):
        for j in range(0, b_h):
            if prevboard[i][j] == 2: pg+=1
            if     board[i][j] == 2: cg+=1
    return pg != cg

#player place barricade
def playerPlaceBarricade():
    x = -1
    y = -1
    
    clicked = False
    while not clicked:
        events = pygame.event.get()
        for event in events:
            if event.type == pygame.MOUSEBUTTONUP:
                x, y = pygame.mouse.get_pos()
                x = int(x/size)
                y = int(y/size)
                if not( x<0 or x>=b_w or y<0 or y>=b_h):
                    if board[y][x] == 0:
                        clicked = True
    print (x, y)
    board[y][x] = 3

#set display
screen = pygame.display.set_mode((b_w*size+100,b_h*size))

#caption
pygame.display.set_caption("Zombie invasion")

genboard_3(5)

if mode == "GAME":
    #for i in range(10):
    while checkGrowth():
        drawBoard(screen)
        pygame.display.update()
        playerPlaceBarricade()
        drawBoard(screen)
        pygame.display.update()
        #updateBoard(pat2)
        updateBoard(pat1)
        time.sleep(0.5)
    input(".")
elif mode == "DISPLAY_TEST":
    drawBoard(screen)
    pygame.display.update()
    input()
elif mode == "INPUT_TEST":
    drawBoard(screen)
    pygame.display.update()
    input()
pygame.quit()
