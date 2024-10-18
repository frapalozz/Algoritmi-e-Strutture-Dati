package it.unicam.cs.asdl2425.es3;

/**
 * Una prenotazione riguarda una certa aula per un certo time slot.
 * 
 * @author Luca Tesei
 *
 */
public class Prenotazione implements Comparable<Prenotazione> {

    private final String aula;

    private final TimeSlot timeSlot;

    private String docente;

    private String motivo;

    /**
     * Costruisce una prenotazione.
     * 
     * @param aula
     *                     l'aula a cui la prenotazione si riferisce
     * @param timeSlot
     *                     il time slot della prenotazione
     * @param docente
     *                     il nome del docente che ha prenotato l'aula
     * @param motivo
     *                     il motivo della prenotazione
     * @throws NullPointerException
     *                                  se uno qualsiasi degli oggetti passati è
     *                                  null
     */
    public Prenotazione(String aula, TimeSlot timeSlot, String docente,
            String motivo) {
        
        if(aula == null || timeSlot == null || docente == null || motivo == null)
                throw new NullPointerException("Oggetto passato null!");
                
        this.aula = aula;
        this.timeSlot = timeSlot;
        this.docente = docente;
        this.motivo = motivo;
    }

    /**
     * @return the aula
     */
    public String getAula() {
        return aula;
    }

    /**
     * @return the timeSlot
     */
    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    /**
     * @return the docente
     */
    public String getDocente() {
        return docente;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param docente
     *                    the docente to set
     */
    public void setDocente(String docente) {
        this.docente = docente;
    }

    /**
     * @param motivo
     *                   the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /*
     * Due prenotazioni sono uguali se hanno la stessa aula e lo stesso time
     * slot. Il docente e il motivo possono cambiare senza influire
     * sull'uguaglianza.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(!(obj instanceof Prenotazione))
            return false;
        
        if(this.aula.equals(((Prenotazione) obj).getAula()) && this.timeSlot.equals(((Prenotazione) obj).getTimeSlot()))
            return true;
            
        return false;
    }

    /*
     * L'hashcode di una prenotazione si calcola a partire dai due campi usati
     * per equals.
     */
    @Override
    public int hashCode() {
        // TODO implementare
        final int prime = 31;
        int result = 1;
        long temp = this.aula.hashCode();
        
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = this.timeSlot.hashCode();
        result = prime * result + (int) (temp ^ (temp >>> 32));

        return result;
    }

    /*
     * Una prenotazione precede un altra in base all'ordine dei time slot. Se
     * due prenotazioni hanno lo stesso time slot allora una precede l'altra in
     * base all'ordine tra le aule.
     */
    @Override
    public int compareTo(Prenotazione o) {
        /*
         * Controllo precedenza tra timeslot
         * -1: la prenotazione è < della prenotazione passata
         *  1: la prenotazione è > della prenotazione passata
         */
        int timeSlotPrecedence = this.timeSlot.compareTo(o.getTimeSlot());

        if(timeSlotPrecedence != 0)
            return timeSlotPrecedence;
        
        // for loop per controllare l'ordine lessicografico delle aule
        for(int i = 0; i < ((this.aula.length() < o.getAula().length())? this.aula.length(): o.getAula().length()); i++) {
            if(this.aula.charAt(i) < o.getAula().charAt(i))
                return -1;
            else if(this.aula.charAt(i) > o.getAula().charAt(i))
                return 1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return "Prenotazione [aula = " + aula + ", time slot =" + timeSlot
                + ", docente=" + docente + ", motivo=" + motivo + "]";
    }

}
