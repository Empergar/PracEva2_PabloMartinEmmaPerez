package es.upsa.sbd2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class Socio
{
    private String dni;
    private String nombre;
    private String email;
    private String direccion;
    private int nprestamos;

    //Incremento del numero de prestamos
    public void incrementarNprestamos()
    {
        if (nprestamos >= 0) {
            nprestamos++;
        }
    }
    //Decremento del numero de prestamos
    public void decrementarNprestamos()
    {
        if (nprestamos > 0) {
            nprestamos--;
        }
    }

    @Override
    public String toString() {
        return "Socio{" +
                "dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", nprestamos=" + nprestamos +
                '}';
    }
}


