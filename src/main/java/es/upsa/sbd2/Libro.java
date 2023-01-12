package es.upsa.sbd2;

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
}
