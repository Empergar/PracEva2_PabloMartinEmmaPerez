package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.*;

import java.sql.*;

public class PostgresDao implements Dao
{
    private Connection connection;

    public PostgresDao(String url, String username, String password) throws SQLException {
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);

        this.connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public Libro insertarLibro(String isbn, String titulo) throws SQLException, IsbnDuplicadoException, EstadoNoValidoException, TituloObligatorioException, EstadoObligatorioException {
        final String SQL = "INSERT INTO libros(isbn, titulo, estado)"
                + "             VALUES(?,           ?,            ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, Estado.LIBRE.toString());

            preparedStatement.executeUpdate();
            return Libro.builder()
                    .withIsbn(isbn)
                    .withTitulo(titulo)
                    .withEstado(Estado.LIBRE)
                    .build();
        } catch (SQLException sqlException) {
            String message = sqlException.getMessage();
            if (message.contains("PK_LIBROS")) throw new IsbnDuplicadoException();
            if (message.contains("CH_LIBROS_ESTADO")) throw new EstadoNoValidoException();
            if (message.contains("NN_LIBROS_TITULO")) throw new TituloObligatorioException();
            if (message.contains("NN_LIBROS_ESTADO")) throw new EstadoObligatorioException();
            throw sqlException;
        }
    }

    @Override
    public void updateSocio(Socio socio) throws SQLException {

    }

    @Override
    public void updateLibro(Libro libro) throws SQLException, LibroNoEncontradoException {
        final String SQL = "UPDATE libros "
                + "    SET  titulo = ?, estado = ? "
                + "    WHERE isbn = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getEstado().toString());
            preparedStatement.setString(3, libro.getIsbn());

            int count = preparedStatement.executeUpdate();
            if (count == 0) throw new LibroNoEncontradoException();
        }
    }


    @Override
    public Socio insertarSocio(String dni, String nombre, String direccion, String email) {
        return null;
    }

    @Override
    public void prestarLibro(String dni, String isbn) {
        final String SQL = "INSERT INTO prestamos(isbn, dni, fecha_prestamo, fecha_devolucion)"
                + "             VALUES(?,           ?,            ?)";
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
