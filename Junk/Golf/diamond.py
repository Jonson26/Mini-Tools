r=range
def f(x,m):
 if x==m:return str(m)
 return str(x)+f(x+1,m)+str(x)
for j in r(9):
 for i in list(r(1,j+1))+list(r(j+1,0,-1)):print(" "*(10-i)+f(1,i))
 print()