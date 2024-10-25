package it.unicam.cs.asdl2425.es4;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementation: Collective
 *
 */
public class Aula implements Comparable<Aula> {

    /*
     * numero iniziale delle posizioni dell'array facilities. Se viene richiesto
     * di inserire una facility e l'array è pieno questo viene raddoppiato. La
     * costante è protected solo per consentirne l'accesso ai test JUnit
     */
    protected static final int INIT_NUM_FACILITIES = 5;

    /*
     * numero iniziale delle posizioni dell'array prenotazioni. Se viene
     * richiesto di inserire una prenotazione e l'array è pieno questo viene
     * raddoppiato. La costante è protected solo per consentirne l'accesso ai
     * test JUnit.
     */
    protected static final int INIT_NUM_PRENOTAZIONI = 100;

    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    /*
     * Insieme delle facilities di quest'aula. L'array viene creato all'inizio
     * della dimensione specificata nella costante INIT_NUM_FACILITIES. Il
     * metodo addFacility(Facility) raddoppia l'array qualora non ci sia più
     * spazio per inserire la facility.
     */
    private Facility[] facilities;

    // numero corrente di facilities inserite
    private int numFacilities;

    /*
     * Insieme delle prenotazioni per quest'aula. L'array viene creato
     * all'inizio della dimensione specificata nella costante
     * INIT_NUM_PRENOTAZIONI. Il metodo addPrenotazione(TimeSlot, String,
     * String) raddoppia l'array qualora non ci sia più spazio per inserire la
     * prenotazione.
     */
    private Prenotazione[] prenotazioni;

    // numero corrente di prenotazioni inserite
    private int numPrenotazioni;

    /**
     * Costruisce una certa aula con nome e location. Il set delle facilities è
     * vuoto. L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                     il nome dell'aula
     * @param location
     *                     la location dell'aula
     * 
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location) {
        if(nome == null)
            throw new NullPointerException("nome è null! Inserire valore valido.");
        if(location == null)
            throw new NullPointerException("location è null! Inserire valore valido.");
        this.nome = nome;
        this.location = location;

        this.facilities = new Facility[INIT_NUM_FACILITIES];
        this.prenotazioni = new Prenotazione[INIT_NUM_PRENOTAZIONI];

    }

    /*
     * Ridefinire in accordo con equals
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return prime * result + nome.hashCode();
    }

    /* Due aule sono uguali se e solo se hanno lo stesso nome */
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(this == obj)
            return true;
        if(!(obj instanceof Aula))
            return false;
        
        Aula otherAula = (Aula) obj;

        return this.nome.equals(otherAula.getNome());
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        if(o == null)
            throw new NullPointerException("Aula passata è null!");
        
        if(this == o)
            return 0;
        return this.nome.compareTo(o.nome);
    }

    /**
     * @return the facilities
     */
    public Facility[] getFacilities() {
        return this.facilities;
    }

    /**
     * @return il numero corrente di facilities
     */
    public int getNumeroFacilities() {
        return this.numFacilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return this.location;
    }

    /**
     * @return the prenotazioni
     */
    public Prenotazione[] getPrenotazioni() {
        return this.prenotazioni;
    }

    /**
     * @return il numero corrente di prenotazioni
     */
    public int getNumeroPrenotazioni() {
        return this.numPrenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula. Controlla se la facility è già
     * presente, nel qual caso non la inserisce.
     * 
     * @param f
     *              la facility da aggiungere
     * @return true se la facility non era già presente e quindi è stata
     *         aggiunta, false altrimenti
     * @throws NullPointerException
     *                                  se la facility passata è nulla
     */
    public boolean addFacility(Facility f) {
        if(f == null)
            throw new NullPointerException("Facility passata null!");

        // Controllo se non è già stata inserita la stessa facility
        for(int i = 0; i < numFacilities; i++) {
            if(facilities[i].equals(f))
                return false;
        }

        /*
         * Se l'array facilities ha raggiunto il limite 
         * raddoppio la sua dimensione ed inserisco la nuova facility
         * Altrimenti inserisco semplicemente la nuova facility
         */
        if(numFacilities == facilities.length) {
            Facility[] facilityHolder = facilities;
            facilities = new Facility[numFacilities*2];

            for(int i = 0; i < numFacilities; i++)
                facilities[i] = facilityHolder[i];
        }

        facilities[numFacilities++] = f;

        return true;
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
     * 
     * 
     * @param ts
     *               il time slot da controllare
     * 
     * @return true se l'aula risulta libera per tutto il periodo del time slot
     *         specificato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean isFree(TimeSlot ts) {
        if(ts == null)
            throw new NullPointerException("TimeSlot passato null!");

        for(int i = 0; i < numPrenotazioni; i++) {
            if(prenotazioni[i].getTimeSlot().overlapsWith(ts))
                return false;
        }
        
        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare, sono da considerare solo le
     *                                posizioni diverse da null
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Facility[] requestedFacilities) {
        if(requestedFacilities == null) 
            throw new NullPointerException("Il set di facility richieste è null!");

        // Loop sulle requestedFacilities
        for(int i = 0; i < requestedFacilities.length; i++) {
            
            // Controllo se la requestedFacility è null
            if(requestedFacilities[i] == null) continue;

            // Controllo se la requestedFacility è soddisfatta
            for(int j = 0; j < numFacilities; j++) {
                if(facilities[j].equals(requestedFacilities[i]))
                    break;
                /*
                 * Se si è arrivati all'ultimo elemento delle facilites allora vuol dire che 
                 * la requestedfacility non è soddisfatta
                 */
                if(j == numFacilities-1) return false;
            }
        }

        return true;
    }

    /**
     * Prenota l'aula controllando eventuali sovrapposizioni.
     * 
     * @param ts
     * @param docente
     * @param motivo
     * @throws IllegalArgumentException
     *                                      se la prenotazione comporta una
     *                                      sovrapposizione con un'altra
     *                                      prenotazione nella stessa aula.
     * @throws NullPointerException
     *                                      se una qualsiasi delle informazioni
     *                                      richieste è nulla.
     */
    public void addPrenotazione(TimeSlot ts, String docente, String motivo) {
        if(ts == null)
            throw new NullPointerException("TimeSlot è null!");
        if(docente == null)
            throw new NullPointerException("docente è null!");
        if(motivo == null)
            throw new NullPointerException("motivo è null!");

        // Controllo sovrapposizione tempo con altre prenotazioni
        for(int i = 0; i < numPrenotazioni; i++) {
            if(prenotazioni[i].getTimeSlot().overlapsWith(ts))
                throw new IllegalArgumentException("La prenotazione comporta una sovrapposizione con un'altra prenotazione nella stessa aula!");
        }
        

        /*
         * Se non ci sono sovrapposizioni allora si aggiunge la prenotazione 
         * alla lista prenotazioni
         */

         /*
          * Ingrandimento array prenotazioni se raggiunta la massima capienza
          */
        if(numPrenotazioni == prenotazioni.length) {
            Prenotazione[] prenotazioniHolder = prenotazioni;
            prenotazioni = new Prenotazione[numPrenotazioni*2];

            for(int i = 0; i < numPrenotazioni; i++)
                prenotazioni[i] = prenotazioniHolder[i];
        }

        prenotazioni[numPrenotazioni++] = new Prenotazione(this, ts, docente, motivo);
    }
}
