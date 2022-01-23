package controllers.ReservacionesExt;

/**
 * Created on 5/25/2017.
 * Interface for reading and writing unmanaged resources.
 */
interface Dao<T> extends AutoCloseable {

    T read() throws Exception;

    void write(T in) throws Exception;
}
