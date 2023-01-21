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

    //Funcion que cambia automaticamente el estado de un libro
    public void cambiarEstado()
    {
        //Se comprueba el estadp
        switch (estado){
            case LIBRE:
                //Si el estado es libre pasa a ocupado
                estado = Estado.OCUPADO;
                break;
            case OCUPADO:
                //Si el estado es ocupado pasa a libre
                estado = Estado.LIBRE;
                break;
            default: throw new DataNotValidException();
        }
    }

    @Override
    public String toString() {
        return "Libro{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", estado=" + estado +
                '}';
    }
}
