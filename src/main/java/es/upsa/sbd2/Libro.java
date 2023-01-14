package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.DataNotValidException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class Libro
{
    private String isbn;
    private String titulo;
    private Estado estado;

    public void cambiarEstado()
    {
        switch (estado){
            case LIBRE:
                estado = Estado.OCUPADO;
                break;
            case OCUPADO:
                estado = Estado.LIBRE;
                break;
            default: throw new DataNotValidException();
        }
    }
}
