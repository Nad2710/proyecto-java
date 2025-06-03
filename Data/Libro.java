public class Libro {
    private String ISBN;  
    private String titulo;
    private String autor;
    private int ejemplares;
    private String genero;

    // En este ocupamos el get y el set 
    
    public String getISBN() { 
        return ISBN; 
        }

    public void setISBN(String ISBN) { 
        this.ISBN = ISBN; 
        }

    public String getTitulo() { 
        return titulo;
        }

    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
        }
    
    public String getAutor() { 
        return autor; 
        }

    public void setAutor(String autor) { 
        this.autor = autor; 
        }
    
    public int getEjemplares() { 
        return ejemplares; 
        }

    public void setEjemplares(int ejemplares) { 
        this.ejemplares = ejemplares; 
        }
    
    public String getGenero() { 
        return genero; 
        }

    public void setGenero(String genero) { 
        this.genero = genero; 
        }

}