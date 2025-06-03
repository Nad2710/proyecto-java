import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Biblioteca {
    private static final String LIBROS_JSON = "catalogos_libros.json";
    private static final String HISTORIAL_JSON = "historial.json";
    private List<Libro> libros;
    private List<Prestamo> historial;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public Biblioteca () {
        cargarDatos();
    }

    private void cargarDatos() {
        cargarLibros();
        cargarHistorial();
    }

    private void cargarLibros() {
        try (FileReader reader = new FileReader(LIBROS_JSON)) {
            libros = gson.fromJson(reader, new TypeToken<List<Libro>>(){}.getType());
        } catch (IOException e) {
            System.err.println("Error al cargar libros: " + e.getMessage());
        }
        libros = libros == null ? new ArrayList<>() : libros;
    }

    private void cargarHistorial() {
        try {
            File file = new File(HISTORIAL_JSON);
            if (file.exists()) {
                try (FileReader reader = new FileReader(file)) {
                    historial = gson.fromJson(reader, new TypeToken<List<Prestamo>>(){}.getType());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar historial: " + e.getMessage());
        }
        historial = historial == null ? new ArrayList<>() : historial;
        
        if (!new File(HISTORIAL_JSON).exists()) {
            guardarHistorial();
        }
    }

    private void guardarLibros() {
        try (FileWriter writer = new FileWriter(LIBROS_JSON)) {
            gson.toJson(libros, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar libros: " + e.getMessage());
        }
    }

    private void guardarHistorial() {
        try (FileWriter writer = new FileWriter(HISTORIAL_JSON)) {
            gson.toJson(historial, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar historial: " + e.getMessage());
        }
    }

    public String verDisponibilidad(String busqueda) {
        
        Libro libroPorISBN = buscarLibroPorISBN(busqueda);
        if (libroPorISBN != null) {
            return formatearLibro(libroPorISBN);
        }

        
        List<Libro> librosPorTitulo = buscarLibrosPorTitulo(busqueda);
        
        if (librosPorTitulo.isEmpty()) {
            return "No se encontraron libros con ese criterio de búsqueda.";
        }
        
        if (librosPorTitulo.size() == 1) {
            return formatearLibro(librosPorTitulo.get(0));
        }
        
        
        StringBuilder resultado = new StringBuilder("Se encontraron varios libros:\n");
        for (Libro libro : librosPorTitulo) {
            resultado.append(formatearLibroSimple(libro)).append("\n");
        }
        return resultado.toString();
    }

    public String prestarLibro(String ISBN, String numeroCuenta) {
        Libro libro = buscarLibroPorISBN(ISBN);
        if (libro == null) return "Libro no encontrado.";
        
        if (libro.getEjemplares() <= 0) return "No hay ejemplares disponibles.";
        
        if (buscarPrestamo(ISBN, numeroCuenta) != null) {
            return "El usuario ya tiene prestado este libro.";
        }

        libro.setEjemplares(libro.getEjemplares() - 1);
        historial.add(new Prestamo(ISBN, libro.getTitulo(), numeroCuenta));
        
        guardarLibros();
        guardarHistorial();
        
        return "Libro prestado. Ejemplares restantes: " + libro.getEjemplares();
    }

    public String devolverLibro(String ISBN, String numeroCuenta) {
        Libro libro = buscarLibroPorISBN(ISBN);
        if (libro == null) return "Libro no encontrado.";
        
        Prestamo prestamo = buscarPrestamo(ISBN, numeroCuenta);
        if (prestamo == null) return "Préstamo no encontrado.";
        
        libro.setEjemplares(libro.getEjemplares() + 1);
        historial.remove(prestamo);
        
        guardarLibros();
        guardarHistorial();
        
        return "Libro devuelto. Ejemplares disponibles: " + libro.getEjemplares();
    }

    public List<Prestamo> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    // Métodos auxiliares
    
    private Libro buscarLibroPorISBN(String ISBN) {
        return libros.stream()
            .filter(libro -> libro.getISBN().equals(ISBN))
            .findFirst()
            .orElse(null);
    }

    private List<Libro> buscarLibrosPorTitulo(String titulo) {
        return libros.stream()
            .filter(libro -> libro.getTitulo().toLowerCase().contains(titulo.toLowerCase()))
            .collect(Collectors.toList());
    }

    private Prestamo buscarPrestamo(String ISBN, String numeroCuenta) {
        return historial.stream()
            .filter(p -> p.getISBN().equals(ISBN) && p.getNumeroCuenta().equals(numeroCuenta))
            .findFirst()
            .orElse(null);
    }

    private String formatearLibro(Libro libro) {
        return String.format(
            "Título: %s\nISBN: %s\nAutor: %s\nGénero: %s\nEjemplares disponibles: %d",
            libro.getTitulo(),
            libro.getISBN(),
            libro.getAutor(),
            libro.getGenero(),
            libro.getEjemplares()
        );
    }

    private String formatearLibroSimple(Libro libro) {
        return String.format(
            "- %s (ISBN: %s) - Disponibles: %d",
            libro.getTitulo(),
            libro.getISBN(),
            libro.getEjemplares()
        );
    }

}
