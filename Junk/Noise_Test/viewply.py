import pyvista_miniply
mesh = pyvista_miniply.read_as_mesh(".\\build\\out\\mesh.ply")
print(mesh)
mesh.plot()
