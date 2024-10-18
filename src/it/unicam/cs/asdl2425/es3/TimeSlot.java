/**
 * 
 */
package it.unicam.cs.asdl2425.es3;

import java.util.Calendar;
// TODO completare gli import se necessario
import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        if(start == null || stop == null) 
            throw new NullPointerException("Il tempo di inizio o fine sono nulli! Inserire valori validi");
        if(start.compareTo(stop) > -1)
            throw new IllegalArgumentException("Tempo di inizio o fine non valido! Il tempo di inizio deve essere antecedente al tempo di fine.");

        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null) 
            return false;
        if(!(obj instanceof TimeSlot))
            return false;

        // Controllo se il tempo start e stop sono uguali
        if(this.start.compareTo(((TimeSlot) obj).getStart()) == 0 && 
            this.stop.compareTo(((TimeSlot) obj).getStop()) == 0)
            return true;

        return false;
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        // TODO implementare
        final int prime = 31;
        int result = 1;
        long temp;

        return (int) Math.random() * 1000;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        if(this == o || this.equals(o)) 
            return 0;

        // Se this.start < o.getStart ritorna -1, l'opposto ritorna 1
        if(this.start.compareTo(o.getStart()) != 0) 
            return this.start.compareTo(o.getStart());

        // Se this.stop < 0.getStop ritorna -1, l'opposto ritorna 1
        return this.stop.compareTo(o.getStop());
    }

    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
        // TODO implementare
        if(o == null)
            throw new NullPointerException("Il TimeSlot passato è nullo! Inserire un TimeSlot valido.");
        
        return -1;
    }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        // TODO implementare
        return false;
    }

    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        String dataStart = this.start.get(Calendar.DAY_OF_MONTH) + "/" + (this.start.get(Calendar.MONTH)+1) + "/" + this.start.get(Calendar.YEAR) 
            + " " + this.start.get(Calendar.HOUR_OF_DAY) + "." + this.start.get(Calendar.MINUTE);
        
        String dataStop = this.stop.get(Calendar.DAY_OF_MONTH) + "/" + (this.stop.get(Calendar.MONTH)+1) + "/" + this.stop.get(Calendar.YEAR) 
            + " " + this.stop.get(Calendar.HOUR_OF_DAY) + "." + this.stop.get(Calendar.MINUTE);

        return "[" + dataStart + " - " + dataStop + "]";
    }

}
