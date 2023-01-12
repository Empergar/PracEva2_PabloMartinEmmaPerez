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
    private String nombre;
    private String email;
    private String direccion;
    private String dni;
    private int nprestamos;
}
