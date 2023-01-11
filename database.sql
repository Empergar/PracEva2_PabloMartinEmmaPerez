CREATE TABLE libros
(
    isbn VARCHAR(12),
    titulo VARCHAR(100),
    estado VARCHAR(7),
    CONSTRAINT "PK_LIBROS"         PRIMARY KEY (isbn)
);

CREATE TABLE socios
(
    nombre VARCHAR(100),
    email VARCHAR(100),
    direccion VARCHAR(100),
    dni VARCHAR(12),
    nprestamos INTEGER,
    CONSTRAINT "PK_SOCIOS"         PRIMARY KEY (dni),
    CONSTRAINT "NUM_NPRESTAMOS"    CHECK ( nprestamos <= 5 )
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
