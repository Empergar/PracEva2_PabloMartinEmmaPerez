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
    CONSTRAINT "NN_LIBROS_NPRESTAMOS"   CHECK ( nprestamos IS NOT NULL),
    CONSTRAINT "NN_LIBROS_NOMBRE"   CHECK ( nombre IS NOT NULL),
    CONSTRAINT "NN_LIBROS_DIRECCION"   CHECK ( direccion IS NOT NULL)

);

CREATE TABLE prestamos
(
    isbn VARCHAR(12),
    dni VARCHAR(12),
    fecha_prestamo TIMESTAMP,
    fecha_devolucion TIMESTAMP,
    CONSTRAINT "PK_PRESTAMOS"         PRIMARY KEY (isbn, dni),
    CONSTRAINT "FK_PRESTAMOS_LIBROS"  FOREIGN KEY (isbn) REFERENCES libros(isbn),
    CONSTRAINT "FK_PRESTAMOS_SOCIOS"  FOREIGN KEY (dni) REFERENCES socios(dni)
);
