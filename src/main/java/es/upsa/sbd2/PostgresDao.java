package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.*;

import java.sql.*;
import java.time.LocalDateTime;

public class PostgresDao implements Dao
{
    private Connection connection;

    public PostgresDao(String url, String username, String password) throws SQLException
    {
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);

        this.connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public Libro insertLibro(String isbn, String titulo) throws SQLException, LibroDuplicatedException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException
    {
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
        } catch (SQLException sqlException)
        {
            String message = sqlException.getMessage();
            if (message.contains("PK_LIBROS")) throw new LibroDuplicatedException();
            if (message.contains("CH_LIBROS_ESTADO")) throw new EstadoNotValidException();
            if (message.contains("NN_LIBROS_TITULO")) throw new RequiredTituloException();
            if (message.contains("NN_LIBROS_ESTADO")) throw new RequiredEstadoException();
            throw sqlException;
        }
    }

    @Override
    public Socio insertSocio(String dni, String nombre, String direccion, String email) throws SQLException, SocioDuplicatedException, RequiredNombreException, RequiredDireccionException, RequiredEmailException
    {
        final String SQL = "INSERT INTO socios(dni, nombre, direccion, email, nprestamos)"
                         + "            VALUES( ? ,    ?,       ?,       ?  ,     ?     )";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, direccion);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, 0);

            preparedStatement.executeUpdate();

            return Socio.builder()
                    .withDni(dni)
                    .withNombre(nombre)
                    .withDireccion(direccion)
                    .withEmail(email)
                    .withNprestamos(0)
                    .build();

        } catch (SQLException sqlException)
        {
            String message = sqlException.getMessage();
            if (message.contains("PK_SOCIOS")) throw new SocioDuplicatedException();
            if (message.contains("NN_SOCIOS_NOMBRE")) throw new RequiredNombreException();
            if (message.contains("NN_SOCIOS_DIRECCION")) throw new RequiredDireccionException();
            if (message.contains("NN_SOCIOS_EMAIl")) throw new RequiredEmailException();
            throw sqlException;
        }
    }

    @Override
    public Prestamo insertPrestamo(String isbn, String dni)
            throws SQLException, LibroPrestamoDuplicatedException, LibroNotFoundException, SocioNotFoundException,
            RequiredFechaPrestamoException
    {
        final String SQL = "INSERT INTO prestamos(isbn, dni, fecha_prestamo, fecha_devolucion)"
                         + "    VALUES(?,                ?,                 ?,              ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, dni);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setTimestamp(4, null);

            preparedStatement.executeUpdate();

            return Prestamo.builder()
                    .withIsbn(isbn)
                    .withDni(dni)
                    .withFechaPrestamo(LocalDateTime.now())
                    .withFechaDevolucion(null)
                    .build();
        } catch (SQLException sqlException)
        {
            String message = sqlException.getMessage();
            if (message.contains("PK_PRESTAMOS"))               throw  new LibroPrestamoDuplicatedException();
            if (message.contains("FK_PRESTAMOS_LIBROS"))        throw  new LibroNotFoundException();
            if (message.contains("FK_PRESTAMOS_SOCIOS"))        throw  new SocioNotFoundException();
            if (message.contains("NN_PRESTAMOS_FECHAPRESTAMO")) throw  new RequiredFechaPrestamoException();
            throw sqlException;
        }
    }

    @Override
    public Libro getLibroByIsbn(String isbn) throws SQLException, LibroNotFoundException
    {
        final String SQL =  "SELECT l.isbn, l.titulo, l.estado"
                          + "   FROM libros l                             "
                          + "   WHERE l.isbn = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, isbn);
            try (ResultSet resultSet = preparedStatement.executeQuery() )
            {
                if (resultSet.next())
                {
                    return Libro.builder()
                            .withIsbn(resultSet.getString(1))
                            .withTitulo(resultSet.getString(2))
                            .withEstado(Estado.getEstado(resultSet.getString(3)))
                            .build();

                }
                else
                {
                    throw  new LibroNotFoundException();
                }
            }
        }
    }

    @Override
    public Socio getSocioByDni(String dni) throws SQLException, SocioNotFoundException
    {
        final String SQL =  "SELECT s.dni, s.nombre, s.email, s.direccion, s.nprestamos"
                          + "   FROM socios s "
                          + "   WHERE s.dni = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, dni);
            try (ResultSet resultSet = preparedStatement.executeQuery() )
            {
                if (resultSet.next())
                {
                    return Socio.builder()
                            .withDni(resultSet.getString(1))
                            .withNombre(resultSet.getString(2))
                            .withEmail(resultSet.getString(3))
                            .withDireccion(resultSet.getString(4))
                            .withNprestamos(resultSet.getInt(5))
                            .build();

                }
                else
                {
                    throw  new SocioNotFoundException();
                }
            }
        }
    }

    @Override
    public void updateLibro(Libro libro) throws SQLException, LibroNotFoundException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException {
        final String SQL = "UPDATE libros "
                         + "    SET  titulo = ?, estado = ? "
                         + "    WHERE isbn = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getEstado().toString());
            preparedStatement.setString(3, libro.getIsbn());

            int count = preparedStatement.executeUpdate();
            if (count == 0) throw new LibroNotFoundException();
        } catch (SQLException sqlException)
        {
            String message = sqlException.getMessage();
            if (message.contains("CH_LIBROS_ESTADO")) throw new EstadoNotValidException();
            if (message.contains("NN_LIBROS_TITULO")) throw new RequiredTituloException();
            if (message.contains("NN_LIBROS_ESTADO")) throw new RequiredEstadoException();
            throw sqlException;
        }
    }

    @Override
    public void updateSocio(Socio socio) throws SQLException, SocioNotFoundException, NprestamosNotValidException,
            RequiredNprestamosException, RequiredNombreException, RequiredEmailException,
            RequiredDireccionException
    {
        final String SQL = "UPDATE socios "
                         + "    SET  nombre = ?, email = ?, direccion = ?, nprestamos = ? "
                         + "    WHERE dni = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setString(2, socio.getEmail());
            preparedStatement.setString(3, socio.getDireccion());
            preparedStatement.setInt(4, socio.getNprestamos());
            preparedStatement.setString(5, socio.getDni());


            int count = preparedStatement.executeUpdate();
            if (count == 0) throw new SocioNotFoundException();
        } catch (SQLException sqlException)
        {
            String message = sqlException.getMessage();
            if (message.contains("NN_SOCIOS_NPRESTAMOS")) throw new RequiredNprestamosException();
            if (message.contains("CH_NPRESTAMOS")) throw new NprestamosNotValidException();
            if (message.contains("NN_SOCIOS_NOMBRE")) throw new RequiredNombreException();
            if (message.contains("NN_SOCIOS_EMAIl")) throw new RequiredEmailException();
            if (message.contains("NN_SOCIOS_DIRECCION")) throw new RequiredDireccionException();
            throw sqlException;
        }
    }

    @Override
    public void prestarLibro(String dni, String isbn) throws LibroNotFoundException, SQLException,
            SocioNotFoundException, RequiredTituloException, RequiredEstadoException, EstadoNotValidException,
            NprestamosNotValidException, RequiredDireccionException, RequiredEmailException,
            RequiredNprestamosException, RequiredNombreException, RequiredFechaPrestamoException,
            LibroPrestamoDuplicatedException {
        Libro libroPrestar = getLibroByIsbn(isbn);

        if (libroPrestar.getEstado().equals(Estado.LIBRE))
        {
            Socio socioPrestamo = getSocioByDni(dni);

            if (socioPrestamo.getNprestamos() < 5) {
                libroPrestar.cambiarEstado();
                //La funcion updateLibro actualiza los valores del libro a prestar y comprueba que esten correctos los parametros
                updateLibro(libroPrestar);

                socioPrestamo.incrementarNprestamos();
                //La funcion updateSocio actualiza los valores del socio que realiza el prestamo y comprueba que esten
                // correctos los parametros que se le pasan
                updateSocio(socioPrestamo);

                insertPrestamo(isbn, dni);
            } else {
                throw new NprestamosNotValidException();
            }
        } else
        {
            throw new EstadoNotValidException();
        }
    }

    @Override
    public void close() throws Exception
    {
        this.connection.close();
    }
}
