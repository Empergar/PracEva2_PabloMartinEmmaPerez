DROP TABLE prestamos;
DROP TABLE libros;
DROP TABLE socios;

CREATE TABLE libros
(
    isbn VARCHAR(12),
    titulo VARCHAR(100),
    estado VARCHAR(7),

    CONSTRAINT "PK_LIBROS"          PRIMARY KEY (isbn),
    CONSTRAINT "CH_LIBROS_ESTADO"   CHECK ( estado IN ('LIBRE', 'OCUPADO') ),
    --Al especificar el enunciado que son necesarios los datos pedidos, controlamos que al añadir no puedan ser nulos
    CONSTRAINT "NN_LIBROS_TITULO"   CHECK ( titulo IS NOT NULL),
    CONSTRAINT "NN_LIBROS_ESTADO"   CHECK ( estado IS NOT NULL)
);

INSERT INTO libros(isbn, titulo, estado) values ('1111', 'El Quijote', 'LIBRE');

CREATE TABLE socios
(
    dni VARCHAR(12),
    nombre VARCHAR(100),
    email VARCHAR(100),
    direccion VARCHAR(100),
    nprestamos INTEGER,

    CONSTRAINT "PK_SOCIOS"      PRIMARY KEY (dni),
    CONSTRAINT "CH_NPRESTAMOS"  CHECK ( nprestamos <= 5 ),
    --Al especificar el enunciado que son necesarios los datos pedidos, controlamos que al añadir no puedan ser nulos
    CONSTRAINT "NN_SOCIOS_NOMBRE"   CHECK ( nombre IS NOT NULL),
    CONSTRAINT "NN_SOCIOS_EMAIl"   CHECK ( email IS NOT NULL),
    CONSTRAINT "NN_SOCIOS_DIRECCION"   CHECK ( direccion IS NOT NULL),
    CONSTRAINT "NN_SOCIOS_NPRESTAMOS"   CHECK ( nprestamos IS NOT NULL)

);

INSERT INTO socios(dni, nombre, email, direccion, nprestamos) values ('70944875H', 'Pablo', 'pmartinsa.inf@upsa.es', 'Plaza Mayor', 5);

UPDATE socios
    SET  nprestamos = 5, nombre = ''
    WHERE dni = '70944875H';


DELETE FROM socios
WHERE dni = '70944875H';

CREATE TABLE prestamos
(
    isbn VARCHAR(12),
    dni VARCHAR(12),
    fecha_prestamo TIMESTAMP,
    fecha_devolucion TIMESTAMP,
    CONSTRAINT "PK_PRESTAMOS"         PRIMARY KEY (isbn, dni, fecha_prestamo),
    CONSTRAINT "FK_PRESTAMOS_LIBROS"  FOREIGN KEY (isbn) REFERENCES libros(isbn),
    CONSTRAINT "FK_PRESTAMOS_SOCIOS"  FOREIGN KEY (dni) REFERENCES socios(dni),
    --Al especificar el enunciado que son necesarios los datos pedidos, controlamos que al añadir no puedan ser nulos
    CONSTRAINT "NN_PRESTAMOS_FECHAPRESTAMO"   CHECK ( fecha_prestamo IS NOT NULL)

);
