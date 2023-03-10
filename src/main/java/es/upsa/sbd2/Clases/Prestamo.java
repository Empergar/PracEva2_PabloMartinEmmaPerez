package es.upsa.sbd2.Clases;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class Prestamo
{
    private String isbn;
    private String dni;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;

    //Actualizacion de la fecha de devolucion
    public void cambiarFechaDevolucion (){

        fechaDevolucion= LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    public String toStringHistoricoLibro()
    {
        return " | Dni=" + dni +
                " | FechaPrestamo=" + fechaPrestamo +
                " | FechaDevolucion=" + ((fechaDevolucion!=null)?fechaDevolucion:"En prestamo") +
                '}';
    }

    public String toStringHistoricoSocio()
    {

        return " | Isbn=" + isbn  +
                " | FechaPrestamo=" + fechaPrestamo +
                " | FechaDevolucion=" + ((fechaDevolucion!=null)?fechaDevolucion:"En prestamo") +
                '}';
    }
}
