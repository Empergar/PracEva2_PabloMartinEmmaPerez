package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

public interface Dao extends AutoCloseable
{
    Libro insertLibro(String isbn, String titulo) throws SQLException, LibroDuplicatedException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException;

    Socio insertSocio(String dni, String nombre, String direccion, String email) throws RequiredNombreException, RequiredDireccionException, RequiredEmailException, SocioDuplicatedException, SQLException;

    Prestamo insertPrestamo(String isbn, String dni) throws LibroPrestamoDuplicatedException, LibroNotFoundException, SocioNotFoundException, RequiredFechaPrestamoException, SQLException;

    Libro getLibroByIsbn(String isbn) throws SQLException, LibroNotFoundException;

    Socio getSocioByDni(String dni) throws SQLException, SocioNotFoundException;

    //Función que actualiza los parametros de un libro
    public void updateLibro(Libro libro) throws SQLException, LibroNotFoundException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException;

    //Función que actualiza los parametros de un socio
    public void updateSocio(Socio socio) throws SQLException, SocioNotFoundException, NprestamosNotValidException, RequiredNprestamosException, RequiredNombreException, RequiredEmailException, RequiredDireccionException;

    void prestarLibro(String dni, String isbn) throws LibroNotFoundException, SQLException, SocioNotFoundException, RequiredTituloException, RequiredEstadoException, EstadoNotValidException, NprestamosNotValidException, RequiredDireccionException, RequiredEmailException, RequiredNprestamosException, RequiredNombreException, RequiredFechaPrestamoException, LibroPrestamoDuplicatedException;
    /*
    devolverLibro();
    historicoLibro();
    historicoSocio();
     */

}
