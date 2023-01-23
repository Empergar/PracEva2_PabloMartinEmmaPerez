package es.upsa.sbd2;

import es.upsa.sbd2.Clases.Libro;
import es.upsa.sbd2.Clases.Prestamo;
import es.upsa.sbd2.Clases.Socio;
import es.upsa.sbd2.Exceptions.*;

import java.sql.SQLException;
import java.util.List;

public interface Dao extends AutoCloseable
{
    //Inserccion de un libro en la biblioteca
    Libro insertLibro(String isbn, String titulo) throws SQLException, LibroDuplicatedException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException;

    //Inserccion de un socio en la biblioteca
    Socio insertSocio(String dni, String nombre, String direccion, String email) throws RequiredNombreException, RequiredDireccionException, RequiredEmailException, SocioDuplicatedException, SQLException;

    //Inserccion de un prestamo en la biblioteca
    Prestamo insertPrestamo(String isbn, String dni) throws LibroPrestamoDuplicatedException, LibroNotFoundException, SocioNotFoundException, RequiredFechaPrestamoException, SQLException;

    //Obtener un libro a traves de su isbn
    Libro getLibroByIsbn(String isbn) throws SQLException, LibroNotFoundException;

    //Obtener un socio a traves de su dni
    Socio getSocioByDni(String dni) throws SQLException, SocioNotFoundException;

    //Obtener la lista de prestamos de un libro a traves de su isbn
    List<Prestamo> getPrestamosByIsbn(String isbn) throws SQLException, LibroNotFoundException, PrestamoNotFoundException;

    //Obtener la lista de prestamos de un libro a traves del dni del socio
    List<Prestamo> getPrestamosByDni(String dni) throws SQLException, PrestamoNotFoundException;

    //Función que actualiza los parametros de un libro
    public void updateLibro(Libro libro) throws SQLException, LibroNotFoundException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException;

    //Función que actualiza los parametros de un socio
    public void updateSocio(Socio socio) throws SQLException, SocioNotFoundException, NprestamosNotValidException, RequiredNprestamosException, RequiredNombreException, RequiredEmailException, RequiredDireccionException;

    //Funcion que presta un libro a un socio
    void prestarLibro(String dni, String isbn) throws LibroNotFoundException, SQLException, SocioNotFoundException, RequiredTituloException, RequiredEstadoException, EstadoNotValidException, NprestamosNotValidException, RequiredDireccionException, RequiredEmailException, RequiredNprestamosException, RequiredNombreException, RequiredFechaPrestamoException, LibroPrestamoDuplicatedException;

    //Funcion que devuelve un libro prestado
    void devolverLibro(String isbn) throws SQLException, LibroNotFoundException, SocioNotFoundException, PrestamoNotFoundException, RequiredTituloException, RequiredEstadoException, EstadoNotValidException, NprestamosNotValidException, RequiredDireccionException, RequiredEmailException, RequiredNprestamosException, RequiredNombreException;

    //Historico de prestamos de un libro concreto
    List<Prestamo> historicoLibro(String isbn) throws LibroNotFoundException, SQLException, PrestamoNotFoundException, SocioNotFoundException;

    //Historio de prestamos de un socio concreto
     List<Prestamo> historicoSocio(String dni) throws SocioNotFoundException, SQLException, PrestamoNotFoundException, LibroNotFoundException;

}
