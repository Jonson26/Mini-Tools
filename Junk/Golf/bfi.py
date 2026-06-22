import sys
for c in sys.argv[1:]:
 a=[0]*99;b=[0]*9;i=j=k=0
 while k<len(c):
  z=ord(c[k]);k+=1
  if z=='>':i+=1
  if z=='<':i-=1
  if z=='+':a[i]+=1
  if z=='-':a[i]-=1
  if z=='.':print(chr(a[i]),end='')
  if z=='[':
   if a[i]>0:b[j]=k-1;j+=1
   else:
    l=1
    while l>0:
     k+=1
     if c[k]==']':l-=1
     if c[k]=='[':l+=1
    k+=1
  if z==']':j-=1;k=b[j]