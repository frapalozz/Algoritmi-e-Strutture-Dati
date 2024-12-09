/**
 * 
 */
package it.unicam.cs.asdl2425.slides.collections;

/**
 * Eccezione che indica che il massimo numero di conti di una banca è stato
 * raggiunto.
 * 
 * @author Luca Tesei
 *
 */
public class BankDimensionException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 6741919390005964432L;

    /**
     * 
     */
    public BankDimensionException() {
    }

    /**
     * @param message
     */
    public BankDimensionException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public BankDimensionException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public BankDimensionException(String message, Throwable cause) {
        super(message, cause);
    }

}
