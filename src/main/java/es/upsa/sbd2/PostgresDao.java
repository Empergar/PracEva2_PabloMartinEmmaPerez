package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PostgresDao implements Dao
{
    private Connection connection;

    //COMENTAR ESTO BIEN
    public PostgresDao(String url, String username, String password) throws SQLException
    {
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);

        this.connection = DriverManager.getConnection(url, username, password);
    }

    //<------------------------------------------->//
    //             Inserccion de libro             //
    //<------------------------------------------->//
    //Funcionalidad  que permite incluir un nuevo libro. Será necesario pasarle como parámetros el ISNB del libro y su TITULO.
    @Override
    public Libro insertLibro(String isbn, String titulo) throws SQLException, LibroDuplicatedException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException
    {
        //Sentencia SQL
        final String SQL = "INSERT INTO libros(isbn, titulo, estado)"
                + "             VALUES(?,           ?,            ?)";

        //Se establece el parámetro designado en el valor de Java dado (String).
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, Estado.LIBRE.toString());

            //Se ejecuta la decalracion SQL
            preparedStatement.executeUpdate();

            //Se devuelve el libro que requiere la funcion
            return Libro.builder()
                    .withIsbn(isbn)
                    .withTitulo(titulo)
                    .withEstado(Estado.LIBRE)
                    .build();

         //Si hay algun error se propagan las excepciones
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

    //<------------------------------------------->//
    //             Inserccion del socio            //
    //<------------------------------------------->//
    //Funcionalidad que permite incluir un nuevo socio en la base de datos. Como parámetros necesitará que se le pase el DNI, NOMBRE, DIRECCION y EMAIL del socio
    @Override
    public Socio insertSocio(String dni, String nombre, String direccion, String email) throws SQLException, SocioDuplicatedException, RequiredNombreException, RequiredDireccionException, RequiredEmailException
    {
        //Sentencia SQL
        final String SQL = "INSERT INTO socios(dni, nombre, direccion, email, nprestamos)"
                         + "            VALUES( ? ,    ?,       ?,       ?  ,     ?     )";

        //Se establece el parámetro designado en el valor de Java dado (String).
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
            preparedStatement.setString(1, dni);
            preparedStatement.setString(2, nombre);
            preparedStatement.setString(3, direccion);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, 0);

            //Se ejecuta la declaracion SQL
            preparedStatement.executeUpdate();

            //Se devuelve el socio que requiere la funcion
            return Socio.builder()
                    .withDni(dni)
                    .withNombre(nombre)
                    .withDireccion(direccion)
                    .withEmail(email)
                    .withNprestamos(0)
                    .build();

        //Si hay algun error se propagan las excepciones
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

    //<------------------------------------------->//
    //           Inserccion de prestamo            //
    //<------------------------------------------->//
    //Se inserta un nuevo prestamo en la tabla de prestamos. Sus parametros son el isbn del libro y el dni del socio
    @Override
    public Prestamo insertPrestamo(String isbn, String dni)
            throws SQLException, LibroPrestamoDuplicatedException, LibroNotFoundException, SocioNotFoundException,
            RequiredFechaPrestamoException
    {
        //Sentencia SQL
        final String SQL = "INSERT INTO prestamos(isbn, dni, fecha_prestamo, fecha_devolucion)"
                         + "    VALUES(?,                ?,                 ?,              ?)";

        //Se establece el parámetro designado en el valor de Java dado (String).
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, isbn);
            preparedStatement.setString(2, dni);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)));
            preparedStatement.setTimestamp(4, null);

            //Se ejecuta la sentencia SQL de inserccion
            preparedStatement.executeUpdate();

            //Se devuelve el prestamo que requiere la funcion ---> su fecha de prestamo queda a la actual del sistema y la de devolucion a null
            return Prestamo.builder()
                    .withIsbn(isbn)
                    .withDni(dni)
                    .withFechaPrestamo(LocalDateTime.now())
                    .withFechaDevolucion(null)
                    .build();

        //Si hay algun error se propagan las excepciones
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

    //<------------------------------------------->//
    //            Seleccion de objetos             //
    //<------------------------------------------->//
    //Obtencion de un libro a través de su isbn
    @Override
    public Libro getLibroByIsbn(String isbn) throws SQLException, LibroNotFoundException
    {
        //Sentencia SQL
        final String SQL =  "SELECT l.isbn, l.titulo, l.estado"
                          + "   FROM libros l                             "
                          + "   WHERE l.isbn = ? ";

        //Se establece el parámetro designado en el valor de Java dado (String).
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            //Se establece el parámetro designado en el valor de Java dado (String).
            preparedStatement.setString(1, isbn);

            //Se ejecuta la sentencia SQL de seleccion
            try (ResultSet resultSet = preparedStatement.executeQuery() )
            {
                //Se va deplazando entre filas
                if (resultSet.next())
                {
                    //Se devuelve el libro que requiere la funcion, pasando la posicion
                    return Libro.builder()
                            .withIsbn(resultSet.getString(1))
                            .withTitulo(resultSet.getString(2))
                            .withEstado(Estado.getEstado(resultSet.getString(3)))
                            .build();

                }
                else
                {
                    //Si no hay coincidencias en los libro se propaga la siguiente excepcion
                    throw  new LibroNotFoundException();
                }
            }
        }
    }

    //Obtencion de un socio a través de un dni
    @Override
    public Socio getSocioByDni(String dni) throws SQLException, SocioNotFoundException
    {
        //Sentencia SQL
        final String SQL =  "SELECT s.dni, s.nombre, s.email, s.direccion, s.nprestamos"
                          + "   FROM socios s "
                          + "   WHERE s.dni = ? ";

        //Se establece el parámetro designado en el valor de Java dado (String).
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            //Se establece el parámetro designado en el valor de Java dado (String).
            preparedStatement.setString(1, dni);

            //Se ejecuta la sentencia SQL de seleccion
            try (ResultSet resultSet = preparedStatement.executeQuery() )
            {
                //Se va desplazando entre las filas
                if (resultSet.next())
                {
                    //Se devuelve el socio que requiere la funcion
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
                    //Si no se encuentra el socio que se busca se propaga la siguiente excepcion
                    throw  new SocioNotFoundException();
                }
            }
        }
    }

    //Obtencion de una lista de prestamos a traves del isbn del libro
    @Override
    public List<Prestamo> getPrestamosByIsbn(String isbn) throws SQLException, PrestamoNotFoundException
    {
        //Lista a devolver de los distintos prestamos
        List<Prestamo> prestamos = new ArrayList<>();

        //Sentencia SQL
        final String SQL =  "SELECT p.isbn, p.dni, p.fecha_prestamo, p.fecha_devolucion "
                         +  "   FROM prestamos p "
                         +  "   WHERE p.isbn = ? "
                         + "    ORDER BY p.fecha_prestamo DESC ";


        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            //Se establece el parámetro designado en el valor de Java dado (String).
            preparedStatement.setString(1, isbn);

            //Se ejecuta la sentencia SQL de seleccion
            try (ResultSet resultSet = preparedStatement.executeQuery() )
            {
                //Se va desplazando entre las filas
                if (resultSet.next())
                {

                    do {
                        //Se trata la fecha de devolucion, ya que un resultset  no puede ser nulo y el prestamo puede no estar devuelto
                        Timestamp fechaDevolucion = resultSet.getTimestamp(4);
                        LocalDateTime fechaDevolucionNullable = (fechaDevolucion != null) ? fechaDevolucion.toLocalDateTime() : null;

                        //Se añade el prestamo que coincida con lo solictado en la funcion
                        prestamos.add(Prestamo.builder()
                                .withIsbn(resultSet.getString(1))
                                .withDni(resultSet.getString(2))
                                .withFechaPrestamo(resultSet.getTimestamp(3).toLocalDateTime())
                                .withFechaDevolucion(fechaDevolucionNullable)
                                .build());
                    } while (resultSet.next());
                    //Se devuelve la lista de prestamos
                    return prestamos;
                }
                else
                {
                    //Si no se encuentra ninguno de los prestamos se propaga la siguiente excecpcion
                    throw  new PrestamoNotFoundException();
                }
            }
        }
    }
    @Override
    public List<Prestamo> getPrestamosByDni(String dni) throws SQLException, PrestamoNotFoundException
    {
        //Lista a devolver de los distintos prestamos
        List<Prestamo> prestamos = new ArrayList<>();

        //Sentencia SQL
        final String SQL =  "SELECT p.isbn, p.dni, p.fecha_prestamo, p.fecha_devolucion "
                         +  "   FROM prestamos p "
                         +  "   WHERE p.dni = ? "
                         +  "    ORDER BY p.fecha_prestamo DESC, p.fecha_devolucion DESC ";


        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            //Se establece el parámetro designado en el valor de Java dado (String).
            preparedStatement.setString(1, dni);

            //Se ejecuta la sentencia SQL de seleccion
            try (ResultSet resultSet = preparedStatement.executeQuery() )
            {
                //Se va desplazando entre las filas
                if (resultSet.next())
                {

                    do {
                        //Se trata la fecha de devolucion, ya que un resultset  no puede ser nulo y el prestamo puede no estar devuelto
                        Timestamp fechaDevolucion = resultSet.getTimestamp(4);
                        LocalDateTime fechaDevolucionNullable = (fechaDevolucion != null) ? fechaDevolucion.toLocalDateTime() : null;

                        //Se añade el prestamo que coincida con lo solictado en la funcion
                        prestamos.add(Prestamo.builder()
                                .withIsbn(resultSet.getString(1))
                                .withDni(resultSet.getString(2))
                                .withFechaPrestamo(resultSet.getTimestamp(3).toLocalDateTime())
                                .withFechaDevolucion(fechaDevolucionNullable)
                                .build());
                    } while (resultSet.next());
                    //Se devuelve la lista de prestamos
                    return prestamos;
                }
                else
                {
                    //Si no se encuentra ninguno de los prestamos se propaga la siguiente excecpcion
                    throw  new PrestamoNotFoundException();
                }
            }
        }
    }
    //<------------------------------------------->//
    //        Actualizacion  de parametros         //
    //<------------------------------------------->//
    @Override
    public void updateLibro(Libro libro) throws SQLException, LibroNotFoundException, EstadoNotValidException, RequiredTituloException, RequiredEstadoException {

        //Sentencia SQL
        final String SQL = "UPDATE libros "
                         + "    SET  titulo = ?, estado = ? "
                         + "    WHERE isbn = ? ";

        //Se establece el parámetro designado en el valor de Java dado (String).
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setString(2, libro.getEstado().toString());
            preparedStatement.setString(3, libro.getIsbn());

            //Se ejecuta la sentencia SQL
            int count = preparedStatement.executeUpdate();
            //Si no se encuentra/actualiza ningun libro se propaga la siguiente excepcion
            if (count == 0) throw new LibroNotFoundException();

        //Si hay algun error se propagan las excepciones
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
        //Sentencia SQL
        final String SQL = "UPDATE socios "
                         + "    SET  nombre = ?, email = ?, direccion = ?, nprestamos = ? "
                         + "    WHERE dni = ? ";

        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            //Se establece el parámetro designado en el valor de Java dado (String).
            preparedStatement.setString(1, socio.getNombre());
            preparedStatement.setString(2, socio.getEmail());
            preparedStatement.setString(3, socio.getDireccion());
            preparedStatement.setInt(4, socio.getNprestamos());
            preparedStatement.setString(5, socio.getDni());

            //Se ejecuta la sentencia SQL
            int count = preparedStatement.executeUpdate();
            //Si no se encuentra/actualiza ningun socio se propaga la siguiente excepcion
            if (count == 0) throw new SocioNotFoundException();

        //Si hay algun error se propagan las excepciones
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

    public void updatePrestamo(Prestamo prestamo) throws SQLException, PrestamoNotFoundException, LibroNotFoundException, SocioNotFoundException
    {
        //Sentencia SQL
        final String SQL = "UPDATE prestamos "
                         + "    SET fecha_devolucion = ? "
                         + "    WHERE isbn = ? AND dni = ? AND fecha_prestamo = ?  ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL))
        {
            //Se establece el parámetro designado en el valor de Java dado.
            preparedStatement.setTimestamp(1, Timestamp.valueOf(prestamo.getFechaDevolucion()));
            preparedStatement.setString(2, prestamo.getIsbn());
            preparedStatement.setString(3, prestamo.getDni());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(prestamo.getFechaPrestamo()));

            //Se ejecuta la sentencia de SQL
            int count = preparedStatement.executeUpdate();
            //Si no se encuentra/actualiza ningun libro se propaga la siguiente excepcion
            if (count == 0) throw new PrestamoNotFoundException();

        } catch (SQLException sqlException)
        {
            String message = sqlException.getMessage();
            if (message.contains("FK_PRESTAMOS_LIBROS")) throw new LibroNotFoundException();
            if (message.contains("FK_PRESTAMOS_SOCIOS")) throw new SocioNotFoundException();

            throw sqlException;
        }
    }
    //<------------------------------------------->//
    //             Prestamo de libros            //
    //<------------------------------------------->//
    //A través de esta funcionalidad se prestará un libro a un socio. Se le pasará como parámetros: el DNI del socio y el ISBN del libro
    @Override
    public void prestarLibro(String dni, String isbn) throws LibroNotFoundException, SQLException,
            SocioNotFoundException, RequiredTituloException, RequiredEstadoException, EstadoNotValidException,
            NprestamosNotValidException, RequiredDireccionException, RequiredEmailException,
            RequiredNprestamosException, RequiredNombreException, RequiredFechaPrestamoException,
            LibroPrestamoDuplicatedException {
        //Se obtiene el libro a prestar buscado por su isbn
        Libro libroPrestar = getLibroByIsbn(isbn);

        //Se comprueba que el estado del libro sea Libre
        if (libroPrestar.getEstado().equals(Estado.LIBRE))
        {
            //Se obtiene el socio a traves de su dni
            Socio socioPrestamo = getSocioByDni(dni);

            //Se comprueba que el numero de prestamos sea menor que 5
            if (socioPrestamo.getNprestamos() < 5) {
                //Si es asi, se cambia el estado del libro
                libroPrestar.cambiarEstado();
                //La funcion updateLibro actualiza los valores del libro a prestar y comprueba que esten correctos los parametros
                updateLibro(libroPrestar);

                //Se incrementa el numero de prestamos
                socioPrestamo.incrementarNprestamos();
                //La funcion updateSocio actualiza los valores del socio que realiza el prestamo y comprueba que esten
                // correctos los parametros que se le pasan
                updateSocio(socioPrestamo);

                //Se inserta el prestamo
                insertPrestamo(isbn, dni);
            } else {
                //Si el numero es 5 se propaga la siguiente excepcion
                throw new NprestamosNotValidException();
            }
        } else
        {
            //Si el estado del libro a prestar ya es ocupado se propaga la siguiente excepcion
            throw new EstadoNotValidException();
        }
    }

    //<------------------------------------------->//
    //             Devolucion de libros            //
    //<------------------------------------------->//
    //A través de esta funcionalidad se procederá a la devolución de un libro. Como parámetro hay que pasar únicamente el ISBN del libro a devolver
    @Override
    public void devolverLibro(String isbn) throws SQLException, LibroNotFoundException, SocioNotFoundException, PrestamoNotFoundException, RequiredTituloException, RequiredEstadoException, EstadoNotValidException, NprestamosNotValidException, RequiredDireccionException, RequiredEmailException, RequiredNprestamosException, RequiredNombreException {

        //Se busca el libro a traves de su isbn
        Libro librodevuelto = getLibroByIsbn(isbn);
        //Obtenemos el primer miembto de la lista ya que al estar ordenada de manera descendente la fecha de prestamo
        //el primer prestamo de la lista será el que se encuentre activo, en caso de que alguno lo este
        Prestamo prestamoByIsbn = getPrestamosByIsbn(isbn).get(0);

        //Compruebo que el libro no se haya devuelto y por lo tanto haya un prestamo que se pueda devolver
        if (librodevuelto.getEstado().equals(Estado.OCUPADO)) {
            //Se comprueba que la fecha  no sea nula
            if (prestamoByIsbn.getFechaDevolucion() == null) {

                //se actualiza la fecha de la devolucion
                prestamoByIsbn.cambiarFechaDevolucion();
                //Se cambia el estado del libro a Libre
                librodevuelto.cambiarEstado();

                //Se obtiene el socio a traves de su dni
                Socio socio = getSocioByDni(prestamoByIsbn.getDni());
                //Se comprueba que el numero de prestamo sea mayor que 0 para que no haya numeros negativos
                if (socio.getNprestamos() > 0) {

                    //Se decrementa el numero de los prestamos
                    socio.decrementarNprestamos();

                    //Se actualizan todos los datos en las tablas
                    updatePrestamo(prestamoByIsbn);
                    updateLibro(librodevuelto);
                    updateSocio(socio);
                } else {
                    //Si el numero de prestamos es 0 se propaga la siguiente excepcion
                    throw new NprestamosNotValidException();
                }
            }
        }else{
            //Si el libro estaba libre ya se propaga la siguiente excepcion
            throw new EstadoNotValidException();
        }
    }

    //<------------------------------------------->//
    //             Historico de libros            //
    //<------------------------------------------->//
    @Override
    public List<Prestamo> historicoLibro(String isbn) throws SQLException, PrestamoNotFoundException, SocioNotFoundException
    {
        Libro libroPrestamo;
        List<Prestamo> prestamosByIsbn = new ArrayList<>();

        //Obtenemos el socio con sus datos obtenido por medio de su dni
        try {
            libroPrestamo = getLibroByIsbn(isbn);

            prestamosByIsbn = getPrestamosByIsbn(isbn);

            //QUITAR FOREACH ORDENANDO EN EL GETPRESTAMO Y COMPROBAR SI ES DESC O ASC
            //Titulos
            System.out.println("<------------------------------------------->\n       " +
                    "HISTORICO LIBRO ISBN: " + isbn +
                    "\n<------------------------------------------->\n");
            //Muestro los datos del libro
            System.out.println(libroPrestamo + "\n");

            //Recorremos la lista de prestamos del libro
            for (Prestamo prestamo: prestamosByIsbn)
            {
                Socio socioPrestamo = getSocioByDni(prestamo.getDni());
                //Se muestran por pantalla
                System.out.println("\tPrestamo{Nombre del Socio=" + socioPrestamo.getNombre() + prestamo.toStringHistoricoLibro());
            }
        } catch (LibroNotFoundException e) {
            //Tratamos la exception si no lo encuentra
            System.out.println("ERROR el libro con isbn " + isbn + " no existe");
        }

        //Se devuelve la lista de prestamos
        return prestamosByIsbn;
    }

    //<------------------------------------------->//
    //             Historico de socios            //
    //<------------------------------------------->//
    //A través de esta funcionalidad se devolverá una lista conteniendo los préstamos que ha realizado el socio cuyo CODIGO se pasa como parámetro
    @Override
    public List<Prestamo> historicoSocio(String dni) throws SQLException, PrestamoNotFoundException, LibroNotFoundException
    {
        Socio socioPrestamo;
        List<Prestamo> prestamosByDni = new ArrayList<>();

        //Obtenemos el socio con sus datos obtenido por medio de su dni
        try {
            socioPrestamo = getSocioByDni(dni);

            //Creamos la lista de prestamos a traves de una funcion que recoge los prestamos por medio del dni
            prestamosByDni = getPrestamosByDni(dni);

            //Titulos
            System.out.println("<------------------------------------------->\n       " +
                    "HISTORICO SOCIO DNI: " + dni +
                    "\n<------------------------------------------->\n");
            //Muestro los datos del socio
            System.out.println(socioPrestamo +"\n");

            //Recorremos la lista de prestamos del socio
            for (Prestamo prestamo: prestamosByDni) {

                Libro libro= getLibroByIsbn(prestamo.getIsbn());
                //Se muestran por pantalla
                System.out.println("\tPrestamo{ Titulo="+libro.getTitulo() + prestamo.toStringHistoricoSocio());
            }
        } catch (SocioNotFoundException e) {
            //Tratamos la exception si no lo encuentra
            System.out.println("ERROR el socio con dni " + dni + " no existe");
            return null;
        }

        //Se devuelve la lista de prestamos
        return prestamosByDni;
    }
    @Override
    public void close() throws Exception
    {
        this.connection.close();
    }
}
