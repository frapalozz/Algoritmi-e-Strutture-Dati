package it.unicam.cs.asdl2425.es2;

/**
 * Uno scassinatore è un oggetto che prende una certa cassaforte e trova la
 * combinazione utilizzando la "forza bruta".
 * 
 * @author Luca Tesei
 *
 */
public class Burglar {

    private CombinationLock combinationLock;

    private int attemps;

    /**
     * Costruisce uno scassinatore per una certa cassaforte.
     * 
     * @param aCombinationLock
     * @throw NullPointerException se la cassaforte passata è nulla
     */
    public Burglar(CombinationLock aCombinationLock) {
        if(aCombinationLock == null) 
            throw new NullPointerException("La cassaforte passata è nulla!");

        this.combinationLock = aCombinationLock;
    }

    /**
     * Forza la cassaforte e restituisce la combinazione.
     * 
     * @return la combinazione della cassaforte forzata.
     */
    public String findCombination() {
        attemps = 0;
        String combination = "AAA";

        while (true) {
            attemps++;
            // Imposto manopola
            combinationLock.setPosition(combination.charAt(0));
            combinationLock.setPosition(combination.charAt(1));
            combinationLock.setPosition(combination.charAt(2));

            // Provo ad aprire
            combinationLock.open();
            if(combinationLock.isOpen()) return combination;
            
            // Controllo se la terza terza posizione non superi Z
            if(combination.charAt(2) == 'Z') {
                // Controllo se la seconda posizione non superi Z
                if(combination.charAt(1)  == 'Z') {
                    // Controllo se la terza posizione non superi Z
                    if(combination.charAt(0)  == 'Z') return null;
                    else combination = "" + (char) (combination.charAt(0) + 1) + "AA";
                }
                else combination = "" + combination.charAt(0) + (char) (combination.charAt(1) + 1) + "A";
            }
            else combination = "" + combination.charAt(0) + combination.charAt(1) + (char) (combination.charAt(2) + 1);
        }
    }

    /**
     * Restituisce il numero di tentativi che ci sono voluti per trovare la
     * combinazione. Se la cassaforte non è stata ancora forzata restituisce -1.
     * 
     * @return il numero di tentativi che ci sono voluti per trovare la
     *         combinazione, oppure -1 se la cassaforte non è stata ancora
     *         forzata.
     */
    public long getAttempts() {
        if(attemps == 0) return -1;
        return attemps;
    }
}
