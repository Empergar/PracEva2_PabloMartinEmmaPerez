package es.upsa.sbd2;

public interface Dao extends AutoCloseable
{
    Libro insertarLibro(String isbn, String titulo);

    /*
    insertarSocio();
    prestarLibro();
    devolverLibro();
    historicoLibro();
    historicoSocio();
     */

}
