package it.unicam.cs.asdl2425.es2;

/**
 * Un oggetto cassaforte con combinazione ha una manopola che può essere
 * impostata su certe posizioni contrassegnate da lettere maiuscole. La
 * serratura si apre solo se le ultime tre lettere impostate sono uguali alla
 * combinazione segreta.
 * 
 * @author Luca Tesei
 */
public class CombinationLock {

    private String combination;

    private String lockPosition;

    private boolean open;

    private boolean lockPositionUnchanged;
    /**
     * Costruisce una cassaforte <b>aperta</b> con una data combinazione
     * 
     * @param aCombination
     *                         la combinazione che deve essere una stringa di 3
     *                         lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public CombinationLock(String aCombination) {
        checkNewCombination(aCombination);

        this.combination = aCombination;
        this.lockPosition = aCombination;
        this.open = true;
    }

    /**
     * Imposta la manopola su una certaposizione.
     * 
     * @param aPosition
     *                      un carattere lettera maiuscola su cui viene
     *                      impostata la manopola
     * @throws IllegalArgumentException
     *                                      se il carattere fornito non è una
     *                                      lettera maiuscola dell'alfabeto
     *                                      inglese
     */
    public void setPosition(char aPosition) {
        if(aPosition < 'A' || aPosition > 'Z')
            throw new IllegalArgumentException("Carattere non valido! Inserire una lettera maiuscola dell'alfabeto Inglese.");

        this.lockPosition = "" + this.lockPosition.charAt(1) + this.lockPosition.charAt(2) + aPosition;
        this.lockPositionUnchanged = false;
    }

    /**
     * Tenta di aprire la serratura considerando come combinazione fornita le
     * ultime tre posizioni impostate. Se l'apertura non va a buon fine le
     * lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     */
    public void open() {
        if(this.lockPositionUnchanged || this.lockPosition.charAt(0) == '0') return;

        if(this.lockPosition.equals(this.combination)) this.open = true;
        else {
            this.lockPositionUnchanged = true;
            this.lockPosition = "000";
        }
    }

    /**
     * Determina se la cassaforte è aperta.
     * 
     * @return true se la cassaforte è attualmente aperta, false altrimenti
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Chiude la cassaforte senza modificare la combinazione attuale. Fa in modo
     * che se si prova a riaprire subito senza impostare nessuna nuova posizione
     * della manopola la cassaforte non si apre. Si noti che se la cassaforte
     * era stata aperta con la combinazione giusta le ultime posizioni impostate
     * sono proprio la combinazione attuale.
     */
    public void lock() {
        this.open = false;
        this.lockPositionUnchanged = true;
    }

    /**
     * Chiude la cassaforte e modifica la combinazione. Funziona solo se la
     * cassaforte è attualmente aperta. Se la cassaforte è attualmente chiusa
     * rimane chiusa e la combinazione non viene cambiata, ma in questo caso le
     * le lettere impostate precedentemente non devono essere considerate per i
     * prossimi tentativi di apertura.
     * 
     * @param aCombination
     *                         la nuova combinazione che deve essere una stringa
     *                         di 3 lettere maiuscole dell'alfabeto inglese
     * @throw IllegalArgumentException se la combinazione fornita non è una
     *        stringa di 3 lettere maiuscole dell'alfabeto inglese
     * @throw NullPointerException se la combinazione fornita è nulla
     */
    public void lockAndChangeCombination(String aCombination) {

        checkNewCombination(aCombination);

        if(open){
            this.combination = aCombination;
            this.open = false;
        }
        
        this.lockPositionUnchanged = true;
    }

    // Ho creato questo nuovo metodo per non ripetere due volte il controllo della nuova combinazione
    private void checkNewCombination(String aCombination) {
        if(aCombination == null) 
            throw new NullPointerException("La combinazione inserita è nulla! Inserire una combinazione valida");

        if(aCombination.length() != 3)
            throw new IllegalArgumentException("La combinazione inserita deve essere una stringa di 3 lettere maiuscole dell'alfabeto");

        for(int i = 0; i < aCombination.length(); i++) {
            if((int) aCombination.charAt(i) < 'A' || (int) aCombination.charAt(i) > 'Z')
                throw new IllegalArgumentException("Combinazione inserita non valida! Inserire 3 lettere maiuscole dell'alfabeto Inglese.");
        }
    }
}