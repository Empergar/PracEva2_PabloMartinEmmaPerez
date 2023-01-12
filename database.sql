CREATE TABLE libros
(
    isbn VARCHAR(12),
    titulo VARCHAR(100),
    estado VARCHAR(7),
    CONSTRAINT "PK_LIBROS"          PRIMARY KEY (isbn),
    CONSTRAINT "CH_LIBROS_ESTADO"   CHECK ( estado IN ('LIBRE', 'OCUPADO') ),
    CONSTRAINT "NN_LIBROS_ESTADO"   CHECK ( estado IS NOT NULL)
);

CREATE TABLE socios
(
    nombre VARCHAR(100),
    email VARCHAR(100),
    direccion VARCHAR(100),
    dni VARCHAR(12),
    nprestamos INTEGER,
    CONSTRAINT "PK_SOCIOS"         PRIMARY KEY (dni),
    CONSTRAINT "CH_NPRESTAMOS"    CHECK ( nprestamos <= 5 )
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
