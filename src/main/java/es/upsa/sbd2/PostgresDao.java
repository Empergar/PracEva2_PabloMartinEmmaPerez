package es.upsa.sbd2;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresDao implements Dao
{
    private Connection connection;

    public PostgresDao(String url, String username, String password) throws SQLException {
        Driver driver = new org.postgresql.Driver();
        DriverManager.registerDriver(driver);

        this.connection = DriverManager.getConnection(url, username, password);
    }

    @Override
    public Libro insertarLibro(String isbn, String titulo) {
        return null;
    }

    @Override
    public void close() throws Exception {
        this.connection.close();
    }
}
