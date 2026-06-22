--[[pod_format="raw",created="2025-12-04 12:51:36",modified="2025-12-05 11:57:26",revision=17,xstickers={}]]
--[[
Graphics scaling library
(c)2025 Filip Jamroga (aka Jonson26)
]]--

--8 by 8 bayer dither matrix
_DITHER_MATRIX_8 = {
	{ 0, 32,  8, 40,  2, 32, 10, 42},
	{48, 16, 56, 24, 50, 18, 58, 26},
	{12, 44,  4, 36, 14, 46,  6, 38},
	{60, 28, 52, 20, 62, 30, 54, 22},
	{ 3, 35, 11, 43,  1, 33,  9, 41},
	{51, 19, 59, 27, 49, 17, 57, 25},
	{15, 47,  7, 39, 13, 45,  5, 37},
	{63, 31, 55, 23, 61, 29, 53, 21}
}

--[[
Scales down a sprite
image   : Userdata -- path to the file
factor  : Number   -- downscaling factor
palette : Array    -- current palette as rgb colors
	{[col] = {r=red,g=green,b=blue}}
cutoff  : Number   -- At which colour in the palette to stop looking
dither  : Boolean  -- set to true to dither the resulting image

returns a sprite as userdata
]]--
function downScale(image, factor, palette, cutoff, dither)
	local w, h, out, temp_out
	local f = abs(flr(factor))
	if f==0 then; f=1; end
	w = flr(image:width()/f)
	h = flr(image:height()/f)
	out = userdata("u8", w, h)
	
	for y=0, h do
		for x=0, w do
			local cols = {}
			local index = 0
			for i=0, f do
				for j=0, f do
					local y_l = y*f+j
					local x_l = x*f+i
					if y_l<image:height() and x_l<image:width() then
						local col = palette[image:get(x_l, y_l, 1)]
						cols[index] = col
						index += 1
					end
				end
			end
			local rgb = _average(cols, f*f)
			if dither then; rgb = _dither(rgb, x, y); end
			local c = _closestColor(rgb, palette, cutoff)
			out:set(x, y, c)
		end
	end
	return out
end

--[[
Scales up a sprite
image   : Userdata -- path to the file
factor  : Number   -- upscaling factor
palette : Array    -- current palette as rgb colors
	{[col] = {r=red,g=green,b=blue}}
cutoff  : Number   -- At which colour in the palette to stop looking

returns a sprite as userdata
]]--
function upScale(image, factor, palette, cutoff)
	local w, h, out, temp_out
	local f = abs(flr(factor))
	if f==0 then; f=1; end
	w = image:width()*f
	h = image:height()*f
	out = userdata("u8", w, h)
	
	for y=0, image:height() do
		for x=0, image:width() do
			local c = image:get(x, y)
			for i=0, f do
				for j=0, f do
					local y_l = y*f+j
					local x_l = x*f+i
					if y_l<h and x_l<w then
						out:set(x_l, y_l, c)
					end
				end
			end
		end
	end
	return out
end

--[[
Returns the current palette as an array
	{[col] = {r=red,g=green,b=blue}}
]]--
function getPal()
	local out = {}
	for c = 0, 63, 1 do
		local s = peek4(0x5000 + 4 * c)
		s = string.format("%x", s)
		while #s<6 do
			s = "0"..s
		end
		local red   = tonumber(s:sub(1,2),16)
		local green = tonumber(s:sub(3,4),16)
		local blue  = tonumber(s:sub(5,6),16)
		out[c] = {
			r = red,
			g = green,
			b = blue
		}
	end
	return out
end

function _closestColor(rgb, palette, cutoff)
	local dist_min = 255*255*255
	local c_min = 0
	local r = rgb.r
	local g = rgb.g
	local b = rgb.b
	for c,col in pairs(palette) do
		if c==cutoff then
			break
		end
		dist = (r - col.r)^2+(g - col.g)^2+(b - col.b)^2
		if dist<dist_min then
			dist_min = dist
			c_min = c
		end
	end
	return c_min
end

--Bayer dithering
function _dither(rgb, x, y)
	local v = _DITHER_MATRIX_8[x%8+1][y%8+1]
	rgb.r += v
	rgb.g += v
	rgb.b += v
	return rgb
end

--Colour averaging
function _average(cols, num)
	local i, c, r_l, g_l, b_l 
	i = 0
	c = 0
	r_l = 0
	g_l = 0
	b_l = 0
	while i<num do
		if cols[i]!=nil then
			r_l+=cols[i].r
			g_l+=cols[i].g
			b_l+=cols[i].b
			c+=1
		end
		i+=1
	end
	if c>0 then
		r_l/=c
		g_l/=c
		b_l/=c
	end
	return {r=r_l, g=g_l, b=b_l}
end