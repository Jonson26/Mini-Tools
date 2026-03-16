from pathlib import Path

def savePPM(img, path):
    ASCII_BITS = '1', '0'
    imagepath = Path(path)
    
    width, height = img.size

    # Convert image data to a list of ASCII bits.
    data = [ASCII_BITS[bool(val)] for val in img.getdata()]
    # Convert that to 2D list (list of character lists)
    data = [data[offset: offset+width] for offset in range(0, width*height, width)]
    
    newpath = findAvailablePath(imagepath, 'ppm')
    
    with open(newpath, 'w') as file:
        file.write('P1\n')
        file.write(f'# Conversion of {imagepath} to PPM format\n')
        file.write(f'{width} {height}\n')
        for row in data:
            file.write(' '.join(row) + '\n')

    print('fini')
    
def saveBMP(img, path):
    imagepath = Path(path)
    newpath = findAvailablePath(imagepath, 'bmp')
    img.save(newpath)
    print('fini')

def adjustImg(img):
    return img.convert('1')  # Convert image to bitmap.

def findAvailablePath(imagepath, ext):
    i = 1
    file_path = Path(f'{imagepath.stem}.{ext}')
    while file_path.exists():
        file_path = Path(f'{imagepath.stem}({i}).{ext}')
        i += 1
    return file_path