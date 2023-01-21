package es.upsa.sbd2;

import java.util.ArrayList;
import java.util.List;

//<----------------------------------->//
//    -*-Pablo Martin Sanchez-*-       //
//     -*-Emma Perez Garcia-*-         //
// -> Sistemas de Bases de Datos II <- //
//             2022/2023               //
//<----------------------------------->//
public class Main {

    public static void main(String[] args) throws Exception {
        try (Dao dao = new PostgresDao("jdbc:postgresql://localhost:5432/upsa", "system", "manager");)
        {

            //<------------------------------------------->//
            //             Inserccion de libros            //
            //<------------------------------------------->//
            /*
            Libro libro1 = dao.insertLibro("1111", "El Quijote");
            Libro libro2 = dao.insertLibro("2222", "La Celestina");
            Libro libro3 = dao.insertLibro("3333", "El Mago de Oz");
            Libro libro4 = dao.insertLibro("4444", "Guerra y Paz");
            Libro libro5 = dao.insertLibro("5555", "La Casa de Bernarda Alba");
            Libro libro6 = dao.insertLibro("6666", "Luces de bohemia");
            Libro libro7 = dao.insertLibro("7777", "Moby Dick");
            Libro libro8 = dao.insertLibro("8888", "Dune");
            Libro libro9 = dao.insertLibro("9999", "La Montaña Magica");
            */
            //<------------------------------------------->//
            //             Inserccion de socios            //
            //<------------------------------------------->//
            /*
            Socio socio1= dao.insertSocio("70944875H", "Pablo", "Plaza Mayor", "pmartinsa.inf@upsa.es");
            Socio socio2= dao.insertSocio("71062020K", "Emma", "c/Amapola, 12", "eperezga.inf@upsa.es");
            Socio socio3= dao.insertSocio("45456787P", "Carmen", "c/tulipan", "maria@gmail.com");
            Socio socio4= dao.insertSocio("34657778O", "Tomas", "c/tulipan", "maria@gmail.com");
            Socio socio5= dao.insertSocio("43565776J", "Pedro", "c/tulipan", "maria@gmail.com");
            Socio socio6= dao.insertSocio("34546768D", "Rocio", "c/tulipan", "maria@gmail.com");
            Socio socio7= dao.insertSocio("34576878H", "Fernando", "c/tulipan", "maria@gmail.com");
            Socio socio8= dao.insertSocio("23445890R", "Jose", "c/tulipan", "maria@gmail.com");
            Socio socio9= dao.insertSocio("98347574S", "Luisa", "c/tulipan", "maria@gmail.com");
            */
            //<------------------------------------------->//
            //                      BORRAR                 //
            //<------------------------------------------->//
            //Libro libro= dao.getLibroByIsbn("3333");
            //libro.cambiarEstado();
            //dao.updateLibro(libro);

            //<------------------------------------------->//
            //             Prestamos de libros             //
            //<------------------------------------------->//

            /*
            dao.prestarLibro("70944875H","1111" );
            dao.prestarLibro("70944875H","2222" );
            dao.prestarLibro("70944875H","3333" );
            dao.prestarLibro("71062020K","4444" );
            dao.prestarLibro("71062020K","5555" );
            dao.prestarLibro("45456787P","6666" );
            dao.prestarLibro("34657778O","7777" );
            */
            //dao.prestarLibro("70944875H","7777" );
            //<------------------------------------------->//
            //             Devolucion de libros            //
            //<------------------------------------------->//
            /*
            dao.devolverLibro("3333");
            dao.devolverLibro("5555");
            dao.devolverLibro("6666");
            dao.devolverLibro("7777");
            */
            //<------------------------------------------->//
            //             Historico de libros             //
            //<------------------------------------------->//

            List<Prestamo> prestamosLibro7777 = dao.historicoLibro("1010");

            //<------------------------------------------->//
            //             Historico de socios             //
            //<------------------------------------------->//

            List<Prestamo> prestamosSocio71062020K = dao.historicoSocio("70944877Y");

            //<------------------------------------------->//
            //                 Excepciones                 //
            //<------------------------------------------->//
            /*
            Socio socio10= dao.insertSocio("70944875H", "Pablo", "Plaza Mayor", "pmartinsa.inf@upsa.es"); //Insercion de un socio duplicado
            dao.prestarLibro("70944875H","2222" ); //Prestar un libro ya prestado
            dao.prestarLibro("99999999K","8888" ); //Prestar un libro a un socio que no existe
            dao.prestarLibro("70944875H","1112" ); //Prestar un libro que no existe
            dao.devolverLibro("3333"); //Devolucion de un libro devuelto
            dao.devolverLibro("1112"); //Devolucion de un libro que no existe
            List<Prestamo> prestamos2 = dao.historicoSocio("23445890R"); //El socio no ha cogido ningun libro de la biblioteca
            */
        }
    }

}
