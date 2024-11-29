package it.unicam.cs.asdl2425.es8;

/**
 * Classe che definisce solo una ADTConsList vuota.
 * 
 * @author Luca Tesei
 *
 * @param <E> non significativo per la lista vuota
 */
public final class EmptyList<E> implements ADTConsList<E> {

    public boolean isEmpty() {
        return true;
    }

    public E first() {
        throw new IllegalStateException(
                "Tentativo di accedere alla testa di una lista vuota");
    }

    public ADTConsList<E> rest() {
        throw new IllegalStateException(
                "Tentativo di accedere alla coda di una lista vuota");
    }

    public ADTConsList<E> cons(E first) {
        return new ConsList<E>(first);
    }

}
