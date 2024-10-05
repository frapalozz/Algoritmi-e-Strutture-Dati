/**
 * 
 */
package it.unicam.cs.asdl2425.es1;

/**
 * Un oggetto di questa classe permette di rappresentare una equazione di
 * secondo grado e di trovarne le soluzioni reali. I valori dei parametri
 * dell'equazione possono cambiare nel tempo. All'inizio e ogni volta che viene
 * cambiato un parametro la soluzione dell'equazione non esiste e deve essere
 * calcolata con il metodo <code>solve()</code>. E' possibile sapere se al
 * momento la soluzione dell'equazione esiste con il metodo
 * <code>isSolved()</code>. Qualora la soluzione corrente non esista e si tenti
 * di ottenerla verrà lanciata una eccezione.
 * 
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
public class EquazioneSecondoGradoModificabileConRisolutore {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    private static final double EPSILON = 1.0E-15;

    private double a;

    private double b;

    private double c;

    private boolean solved;

    private SoluzioneEquazioneSecondoGrado lastSolution;

    /**
     * Costruisce una equazione di secondo grado modificabile. All'inizio
     * l'equazione non è risolta.
     * 
     * 
     * @param a
     *              coefficiente del termine x^2, deve essere diverso da zero.
     * @param b
     *              coefficiente del termine x
     * @param c
     *              termine noto
     * @throws IllegalArgumentException
     *                                      se il parametro <code>a</code> è
     *                                      zero
     * 
     */
    public EquazioneSecondoGradoModificabileConRisolutore(double a, double b,
            double c) {
        // TODO implementare 

        if (Math.abs(a) < EPSILON) // controllo se uguale a zero
            throw new IllegalArgumentException("L'equazione di secondo grado non può avere coefficiente a uguale a zero");
        this.a = a;
        this.b = b;
        this.c = c;
        solved = false;
    }

    /**
     * @return il valore corrente del parametro a
     */
    public double getA() {
        return a;
    }

    /**
     * Cambia il parametro a di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     * 
     * @param a
     *              il nuovo valore del parametro a
     * @throws IllegalArgumentException
     *                                      se il nuovo valore è zero
     */
    public void setA(double a) {
        // TODO implementare

        this.a = a;

        if(a == 0) throw new IllegalArgumentException("L'equazione di secondo grado non può avere coefficiente = 0!");
        solved = false;
    }

    /**
     * @return il valore corrente del parametro b
     */
    public double getB() {
        return b;
    }

    /**
     * Cambia il parametro b di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     * 
     * @param b
     *              il nuovo valore del parametro b
     */
    public void setB(double b) {
        // TODO implementare 

        this.b = b;
        solved = false;
    }

    /**
     * @return il valore corrente del parametro c
     */
    public double getC() {
        return c;
    }

    /**
     * Cambia il parametro c di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     * 
     * @param c
     *              il nuovo valore del parametro c
     */
    public void setC(double c) {
        // TODO implementare 

        this.c = c;
        this.solved = false;
    }

    /**
     * Determina se l'equazione, nel suo stato corrente, è già stata risolta.
     * 
     * @return true se l'equazione è risolta, false altrimenti
     */
    public boolean isSolved() {
        return solved;
    }

    /**
     * Risolve l'equazione risultante dai parametri a, b e c correnti. Se
     * l'equazione era già stata risolta con i parametri correnti non viene
     * risolta di nuovo.
     */
    public void solve() {
        // TODO implementare

        if(!solved) {
            double delta = b * b - 4 * a * c;
            // delta == 0
            if (Math.abs(delta) < EPSILON)
                // ritorna la soluzione con due valori coincidenti
                lastSolution =  new SoluzioneEquazioneSecondoGrado(new EquazioneSecondoGrado(a, b, c), (-b) / (2 * a));
            // delta < 0
            else if (delta < 0)
                // ritorna la soluzione vuota
                lastSolution = new SoluzioneEquazioneSecondoGrado(new EquazioneSecondoGrado(a, b, c));
            // delta > 0
            else {
                double tmp = Math.sqrt(delta);
                // ritorna le due soluzioni calcolate
                lastSolution = new SoluzioneEquazioneSecondoGrado(new EquazioneSecondoGrado(a, b, c),
                        (-b + tmp) / (2 * a),
                        (-b - tmp) / (2 * a));   
            }

            solved = true;
        }
    }

    /**
     * Restituisce la soluzione dell'equazione risultante dai parametri
     * correnti. L'equazione con i parametri correnti deve essere stata
     * precedentemente risolta.
     * 
     * @return la soluzione
     * @throws IllegalStateException
     *                                   se l'equazione risulta non risolta,
     *                                   all'inizio o dopo il cambiamento di
     *                                   almeno uno dei parametri
     */
    public SoluzioneEquazioneSecondoGrado getSolution() {
        // TODO implementare

        if(solved) {
            return lastSolution;
        }
        
        throw new IllegalStateException("L'equazione non è ancora stata risolta!");
    }

}
