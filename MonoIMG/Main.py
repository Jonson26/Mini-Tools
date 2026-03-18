import tkinter as tk
import filegen
from pathlib import Path
from PIL import ImageTk, Image

# To get the dialog box to open when required 
from tkinter import filedialog

def open_img():
    global path, img
    # Select the Imagename  from a folder 
    path = openfilename()

    # opens the image
    img = Image.open(path)
    
    width, height = img.size
    
    if(width>640 or height>400):
        # resize the image and apply a high-quality down sampling filter
        img.thumbnail((640, 400), Image.LANCZOS)
    
    img = filegen.adjustImg(img)

    # PhotoImage class is used to add image to widgets, icons etc
    img2 = ImageTk.PhotoImage(img)
 
    # create a label
    panel = tk.Label(root, image = img2)
    
    # set the image as img 
    panel.image = img2
    panel.grid(row = 2, column = 1, columnspan = 24)
    
def openfilename():
    # open file dialog box to select image
    # The dialogue box has a title "Open"
    filename = filedialog.askopenfilename(title ='open')
    return filename
    
def savePPM():
    global path, img
    filegen.savePPM(img, path)
    
def saveBMP():
    global path, img
    filegen.saveBMP(img, path)

path = ""

# Create a window
root = tk.Tk()

# Set Title as Image Loader
root.title("Monochrome image generator")

# Set the resolution of window
root.geometry("720x540")

# Allow Window to be resizable
root.resizable(width = True, height = True)

# Create a button and place it into the window using grid layout
btn = tk.Button(root, text ='open image', command = open_img).grid(
                                        row = 1, column = 1, columnspan = 4)
btn = tk.Button(root, text ='save bmp', command = saveBMP).grid(
                                        row = 1, column = 5, columnspan = 4)
btn = tk.Button(root, text ='save ppm', command = savePPM).grid(
                                        row = 1, column = 9, columnspan = 4)
root.mainloop()