package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.*;

import java.sql.SQLException;

public interface Dao extends AutoCloseable
{
    Libro insertarLibro(String isbn, String titulo) throws SQLException, IsbnDuplicadoException, EstadoNoValidoException, TituloObligatorioException, EstadoObligatorioException;

    public void updateSocio(Socio socio) throws SQLException;

    //Función que actualiza el estado del libro de OCUPADO a LIBRE y viceversa
    public void updateLibro(Libro libro) throws SQLException, LibroNoEncontradoException;

    Socio insertarSocio(String dni, String nombre, String direccion, String email);

    void prestarLibro(String dni, String isbn);
    /*
    devolverLibro();
    historicoLibro();
    historicoSocio();
     */

}
