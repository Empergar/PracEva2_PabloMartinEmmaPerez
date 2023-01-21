package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.DataNotValidException;

public enum Estado
{
    LIBRE, OCUPADO;

    //Funcion que obtiene el estado del libro
    public static Estado getEstado(String estado)
    {
        //Se recorren los posibles valores (libre - ocupado)
        for (Estado est: Estado.values())
        {
            //Si coincide con alguno
            if (est.toString().equals(estado))
            {
                //Se devuelve el estado
                return est;
            }
        }
        //Si no se encuentra se propaga un RunTimeException (Culpa del programador)
        throw new DataNotValidException();
    }
}
