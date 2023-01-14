package es.upsa.sbd2;

import es.upsa.sbd2.Exceptions.DataNotValidException;

public enum Estado
{
    LIBRE, OCUPADO;

    public static Estado getEstado(String estado)
    {
        for (Estado est: Estado.values())
        {
            if (est.toString().equals(estado))
            {
                return est;
            }
        }
        throw new DataNotValidException();
    }
}
