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

    public void incrementarNprestamos()
    {
        if (nprestamos < 0) {
            nprestamos++;
        }
    }
}


