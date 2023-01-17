package es.upsa.sbd2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    public void cambiarFechaDevolucion (){

        fechaDevolucion= LocalDateTime.now();
    }
}
