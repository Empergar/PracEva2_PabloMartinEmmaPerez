package es.upsa.sbd2;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws Exception {
        try (Dao dao = new PostgresDao("jdbc:postgresql://localhost:5432/upsa", "system", "manager");)
        {
            //Libro libro1 = dao.insertarLibro("1111", "El Quijote");
            //Libro libro2 = dao.insertarLibro("2222", "La Celestina");
            Libro libro2 = dao.insertarLibro("3333", "El Mago de Oz");
        }
    }

}
