import java.util.Date;

public class Prestamo {
    private String ISBN;
    private String titulo;
    private String numeroCuenta;
    private String fechaPrestamo;

    public Prestamo(String ISBN, String titulo, String numeroCuenta) {
        
        this.ISBN = ISBN;
        this.titulo = titulo;
        this.numeroCuenta = numeroCuenta;
        this.fechaPrestamo = new Date().toString();
    }

    // Getters

    public String getISBN() { 
        return ISBN; 
        }

    public String getTitulo() { 
        return titulo; 
        }

    public String getNumeroCuenta() {
         return numeroCuenta; 
        }

    public String getFechaPrestamo() {
         return fechaPrestamo; 
        }


}
