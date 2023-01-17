package es.upsa.sbd2;

public class Main {

    public static void main(String[] args) throws Exception {
        try (Dao dao = new PostgresDao("jdbc:postgresql://localhost:5432/upsa", "system", "manager");)
        {
            //Libro libro1 = dao.insertLibro("1111", "El Quijote");
            //Libro libro2 = dao.insertLibro("2222", "La Celestina");
            //Libro libro3 = dao.insertLibro("3333", "El Mago de Oz");
            //Socio socio1= dao.insertSocio("34653475H", "Maria", "c/tulipan", "maria@gmail.com");
            //Libro libro= dao.getLibroByIsbn("3333");
            //libro.cambiarEstado();
            //dao.updateLibro(libro);
            //dao.prestarLibro("34653475H","3333" );
            dao.devolverLibro("3333");
        }
    }

}
