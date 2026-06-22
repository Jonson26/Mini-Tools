import pygame
import time
import random
import math

#set rgb colors
lightgray = (211,211,211)
red       = (255,  0,  0)
green     = (  0,255,  0)
blue      = (  0,  0,255)
yellow    = (255,255,  0)
black     = (  0,  0,  0)
white     = (255,255,255)

#override
# override = True
override = False
if override:
    lightgray = black
    red       = white
    green     = white
    blue      = white
    yellow    = white

mode = "START"
delay = 30
scale = 3

pygame.font.init()
font = pygame.font.Font('freesansbold.ttf', 8*scale)

block_w = 32
block_h = 16

blocks = [
#1 2 3 4 5 6 7 8 9101112 
[0,0,4,4,0,0,0,0,4,4,0,0,],
[0,4,2,2,4,0,0,4,2,2,4,0,],
[4,2,2,2,2,4,4,2,2,2,2,4,],
[4,2,2,2,2,2,2,2,2,2,2,4,],
[4,2,2,2,2,2,2,2,2,2,2,4,],
[0,4,2,2,2,2,2,2,2,2,4,0,],
[0,4,2,2,2,2,2,2,2,2,4,0,],
[0,0,4,2,2,2,2,2,2,4,0,0,],
[0,0,4,2,2,2,2,2,2,4,0,0,],
[0,0,0,4,2,2,2,2,4,0,0,0,],
[0,0,0,4,2,2,2,2,4,0,0,0,],
[0,0,0,0,4,2,2,4,0,0,0,0,],
[0,0,0,0,4,2,2,4,0,0,0,0,],
[0,0,0,0,0,4,4,0,0,0,0,0,],
[0,0,0,0,0,0,0,0,0,0,0,0,],
[0,0,0,0,0,0,0,0,0,0,0,0,],
[0,0,0,0,0,0,0,0,0,0,0,0,],
[0,0,0,0,0,0,0,0,0,0,0,0,],
]

levels = [
    [# Level 0
    #1 2 3 4 5 6 7 8 9101112 
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,2,1,2,1,2,1,2,1,2,1,0,],
    [0,1,2,1,2,1,2,1,2,1,2,0,],
    [0,2,1,2,1,2,1,2,1,2,1,0,],
    [0,1,2,1,2,1,2,1,2,1,2,0,],
    [0,2,1,2,1,2,1,2,1,2,1,0,],
    [0,1,2,1,2,1,2,1,2,1,2,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    ],
    [# Level 1
    #1 2 3 4 5 6 7 8 9101112 
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,4,4,4,4,4,4,4,4,4,4,0,],
    [0,3,0,1,0,1,0,1,0,1,3,0,],
    [0,3,1,0,1,0,1,0,1,0,3,0,],
    [0,3,0,1,0,1,0,1,0,1,3,0,],
    [0,3,1,0,1,0,1,0,1,0,3,0,],
    [0,2,2,2,2,2,2,2,2,2,2,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    ],
    [# Level 2
    #1 2 3 4 5 6 7 8 9101112 
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,3,3,3,2,2,2,2,2,2,2,0,],
    [0,3,3,2,2,2,2,2,2,2,2,0,],
    [0,2,2,2,2,2,1,4,1,2,2,0,],
    [0,2,4,2,1,1,4,3,4,1,2,0,],
    [0,4,3,4,1,1,1,4,1,1,1,0,],
    [0,1,4,1,1,1,1,1,1,1,1,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    [0,0,0,0,0,0,0,0,0,0,0,0,],
    ],
]

#set color map
cmap = {
    0 : lightgray,
    1 : green,
    2 : blue,
    3 : yellow,
    4 : red,
    }

paddle_x = 100
paddle_w = 64
paddle_h = 8
paddle_c = yellow
paddle_s = 8

ball_r = 8
ball_c = red
ball_s = 3
ball_x = paddle_x+(paddle_w/2)-ball_r
ball_y = len(blocks)*block_h-paddle_h-ball_r
ball_m_x = ball_s
ball_m_y = -ball_s

score = 0

level = 0
lives = 3

key_left_down = False
key_right_down = False

def load_level(n):
    global blocks
    for y in range(len(blocks)):
        for x in range(len(blocks[y])):
            blocks[y][x] = levels[n%len(levels)][y][x]

def level_sum():
    n = 0
    for y in range(len(blocks)):
        n += sum(blocks[y])
    return n

def draw_blocks(disp):
    for y in range(len(blocks)):
        for x in range(len(blocks[y])):
            pygame.draw.rect(disp, cmap[blocks[y][x]], (block_w*x*scale, block_h*y*scale, block_w*scale, block_h*scale))
            if(blocks[y][x] != 0):
                pygame.draw.rect(disp,              black, (block_w*x*scale, block_h*y*scale, block_w*scale, block_h*scale), scale)

def draw_ball(disp):
    pygame.draw.circle(disp, ball_c, (ball_x*scale, ball_y*scale), ball_r*scale)
    pygame.draw.circle(disp, black, (ball_x*scale, ball_y*scale), ball_r*scale, scale)

def draw_paddle(disp):
    pygame.draw.rect(disp, paddle_c, (paddle_x*scale, (len(blocks)*block_h-paddle_h)*scale, paddle_w*scale, paddle_h*scale))
    pygame.draw.rect(disp, black, (paddle_x*scale, (len(blocks)*block_h-paddle_h)*scale, paddle_w*scale, paddle_h*scale), scale)

def draw_score(disp):
    text = str(score)
    text = "Score: "+(5-len(text))*"0"+text
    text = font.render(text, True, black)
    w = text.get_width()
    disp.blit(text, (len(blocks[0])*block_w*scale-w-scale, scale))
    
def draw_level(disp):
    text = "LEVEL "+str(level+1)
    text = font.render(text, True, black)
    w = text.get_width()
    disp.blit(text, ((len(blocks[0])*block_w*scale-w)/2, scale))

def draw_text_center(disp, text, c1, c2 = None):
    if c2 == None:
        text = font.render(text, True, c1)
    else:
        text = font.render(text, True, c1, c2)
    textRect = text.get_rect()
    textRect.center = (len(blocks[0])*block_w*scale/2,len(blocks)*block_h*scale/2)
    disp.blit(text, textRect)

def draw_lives(disp):
    for i in range(lives):
        pygame.draw.circle(disp, ball_c, ((i*ball_r+5)*scale, 5*scale), ball_r*scale/2)
        pygame.draw.circle(disp, black, ((i*ball_r+5)*scale, 5*scale), ball_r*scale/2, scale)

def collideCR(c_x, c_y, c_r, r_x, r_y, r_w, r_h):
    testX = c_x
    testY = c_y
    testC = 0
    
    if (c_x < r_x):
        testX = r_x
        testC = 1
    elif (c_x > r_x+r_w): 
        testX = r_x+r_w
        testC = 1
    if (c_y < r_y):
        testY = r_y
        testC = 2
    elif (c_y > r_y+r_h):
        testY = r_y+r_h
        testC = 2

    distX = c_x-testX;
    distY = c_y-testY;
    distSQR = (distX*distX) + (distY*distY);

    if (distSQR <= c_r*c_r):
        return [True, testC]
    return [False, testC]

def do_physics():
    global paddle_x, ball_x, ball_y, ball_m_x, ball_m_y, blocks, lives, score, mode
    #move paddle
    if(key_left_down):
        paddle_x -= paddle_s
    if(key_right_down):
        paddle_x += paddle_s
    ball_x += ball_m_x
    ball_y += ball_m_y
    #block collision
    for y in range(len(blocks)):
        for x in range(len(blocks[y])):
            if(blocks[y][x] != 0):
                res = collideCR(ball_x, ball_y, ball_r, x*block_w, y*block_h, block_w, block_h)
                if(res[0]):
                    score += 10*blocks[y][x]
                    blocks[y][x] -= 1
                    if(res[1]==1):
                        ball_m_x *= -1
                    if(res[1]==2):
                        ball_m_y *= -1
                    if(level_sum()==0):
                        mode = "NEXT"
    #paddle collision
    res = collideCR(ball_x, ball_y, ball_r, paddle_x, len(blocks)*block_h-paddle_h, paddle_w, paddle_h)
    if(res[0]):
        ball_m_x = ball_s*((ball_x-paddle_x)/paddle_w*2-1)
        ball_m_y = -ball_s
    #wall collision
    if ball_x<0 or ball_x>len(blocks[0])*block_w:
        ball_m_x *= -1
    if ball_y<0:
        ball_m_y = ball_s
    if ball_y>len(blocks)*block_h:
        ball_x = paddle_x+(paddle_w/2)-ball_r
        ball_y = len(blocks)*block_h-paddle_h-ball_r
        ball_m_x = ball_s
        ball_m_y = -ball_s
        lives -= 1
        if lives<0:
            mode = "OVER"

def handle_events():
    global key_left_down, key_right_down, run, mode
    events = pygame.event.get()
    for event in events:
        if event.type == pygame.KEYDOWN:
            if event.key == pygame.K_LEFT:
                key_left_down = True
            if event.key == pygame.K_RIGHT:
                key_right_down = True
            if event.key == pygame.K_ESCAPE:
                run = False
            if event.key == pygame.K_F2:
                mode = "NEXT"
        if event.type == pygame.KEYUP:
            if event.key == pygame.K_LEFT:
                key_left_down = False
            if event.key == pygame.K_RIGHT:
                key_right_down = False
                
def handle_any_key():
    global mode
    events = pygame.event.get()
    for event in events:
        if event.type == pygame.KEYDOWN:
            mode = "GAME"
            load_level(level)

#set display
screen = pygame.display.set_mode((len(blocks[0])*block_w*scale,len(blocks)*block_h*scale))

#caption
pygame.display.set_caption("Breakout")


run = True
while run:
    if mode == "GAME":
        draw_blocks(screen)
        draw_paddle(screen)
        draw_ball(screen)
        draw_lives(screen)
        draw_score(screen)
        draw_level(screen)
        pygame.display.update()
        pygame.display.flip()
        do_physics()
        handle_events()
        pygame.event.pump()
        pygame.time.delay(delay)
    elif mode == "OVER":
        draw_blocks(screen)
        draw_score(screen)
        draw_level(screen)
        draw_text_center(screen, "GAME OVER", red, black)
        pygame.display.update()
        pygame.display.flip()
        handle_events()
        pygame.event.pump()
        pygame.time.delay(delay)
    elif mode == "NEXT":
        level += 1
        ball_s += 0.1
        load_level(level)
        ball_x = paddle_x+(paddle_w/2)-ball_r
        ball_y = len(blocks)*block_h-paddle_h-ball_r
        ball_m_x = ball_s
        ball_m_y = -ball_s
        mode = "GAME"
    elif mode == "START":
        draw_blocks(screen)
        draw_text_center(screen, "WELCOME TO BREAKOUT", blue, yellow)
        pygame.display.update()
        pygame.display.flip()
        handle_any_key()
        pygame.event.pump()
        pygame.time.delay(delay)
        
pygame.quit()