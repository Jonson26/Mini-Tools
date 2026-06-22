--[[pod_format="raw",created="2025-02-13 12:45:48",modified="2025-02-13 13:08:30",revision=28]]
year=2026

function calculateGregorianEaster(Y)
	a=Y%19
	b=flr(Y/100)
	c=Y%100
	d=flr(b/4)
	e=b%4
	f=flr((b+8)/25)
	g=flr((8*b+13)/25)
	h=(19*a+b-d-g+15)%30
	i=flr(c/4)
	k=c%4
	l=(32+2*e+2*i-h-k)%7
	m=flr((a+11*h+19*l)/433)
	n=flr((h+l-7*m+90)/25)
	p=(h+l-7*m+33*n+19)%32
	
	easterDate = {
		year = Y,
		month = n,
		day = p
	}
	
	return easterDate
end

for k,v in pairs(calculateGregorianEaster(year)) do
	print(k..": "..v)
end