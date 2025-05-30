import json
import random

temas = ["Programación", "Matemáticas", "Física", "Actuaria", "Literatura"]
libros = [{
    "ISBN": f"978{random.randint(1000000000, 9999999999)}",
    "titulo": f"Libro de {tema} #{i}",
    "autor": f"Autor {random.choice(['A', 'B', 'C'])}",
    "ejemplares": random.randint(1, 15),
    "genero": tema
} for tema in temas for i in range(1, 31)]  # 150 libros (30 por tema)

with open("libros.json", "w") as f:
    json.dump(libros, f, indent=2)