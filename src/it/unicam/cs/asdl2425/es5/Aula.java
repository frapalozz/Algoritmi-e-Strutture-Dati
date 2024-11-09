package it.unicam.cs.asdl2425.es5;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import java.util.Iterator;

/**
 * Un oggetto della classe aula rappresenta una certa aula con le sue facilities
 * e le sue prenotazioni.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 */
public class Aula implements Comparable<Aula> {
    // Identificativo unico di un'aula
    private final String nome;

    // Location dell'aula
    private final String location;

    // Insieme delle facilities di quest'aula
    private final Set<Facility> facilities;

    // Insieme delle prenotazioni per quest'aula, segue l'ordinamento naturale
    // delle prenotazioni
    private final SortedSet<Prenotazione> prenotazioni;

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
            throw new NullPointerException("nome è null! Inserire valore valido");
        if(location == null)
            throw new NullPointerException("location è null! Inserire valore valido");
        
        this.nome = nome;
        this.location = location;
        this.facilities = new HashSet<Facility>();
        this.prenotazioni = new TreeSet<Prenotazione>();
    }

    /**
     * Costruisce una certa aula con nome, location e insieme delle facilities.
     * L'aula non ha inizialmente nessuna prenotazione.
     * 
     * @param nome
     *                       il nome dell'aula
     * @param location
     *                       la location dell'aula
     * @param facilities
     *                       l'insieme delle facilities dell'aula
     * @throws NullPointerException
     *                                  se una qualsiasi delle informazioni
     *                                  richieste è nulla
     */
    public Aula(String nome, String location, Set<Facility> facilities) {
        if(nome == null)
            throw new NullPointerException("nome è null! Inserire valore valido");
        if(location == null)
            throw new NullPointerException("location è null! Inserire valore valido");
        if(facilities == null)
            throw new NullPointerException("facilities è null! Inserire valore valido");
        
        this.nome = nome;
        this.location = location;
        this.facilities = facilities;
        this.prenotazioni = new TreeSet<Prenotazione>();
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
        if(!(obj instanceof Aula))
            return false;
        
        Aula otherAula = (Aula) obj;
        if(this == otherAula)
            return true;

        return this.nome.equals(otherAula.getNome());
    }

    /* L'ordinamento naturale si basa sul nome dell'aula */
    @Override
    public int compareTo(Aula o) {
        return this.nome.compareTo(o.getNome());
    }

    /**
     * @return the facilities
     */
    public Set<Facility> getFacilities() {
        return facilities;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the prenotazioni
     */
    public SortedSet<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    /**
     * Aggiunge una faciltity a questa aula.
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
            throw new NullPointerException("La facility passata è null! Inserire valore valido");
        
        return facilities.add(f);
    }

    /**
     * Determina se l'aula è libera in un certo time slot.
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
            throw new NullPointerException("time slot passato è null! Inserire valore valido");

        // Creazione iteratore di prenotazioni
        Iterator<Prenotazione> itPrenotazioni = prenotazioni.iterator();
        Prenotazione prenNext = null;

        /*
         * Cerca prenotazione finché non arrivo ad una prenotazione 
         * che segue il time slot specificato
         */
        while (itPrenotazioni.hasNext()) {
            prenNext = itPrenotazioni.next();

            if(prenNext.getTimeSlot().overlapsWith(ts))
                return false;
            
            /*
             * Se il time slot specificato è più grande del time slot
             * della prenotazione visualizzata, allora si esce dal loop
             */
            if(prenNext.getTimeSlot().compareTo(ts) > 0)
                break;
        }

        return true;
    }

    /**
     * Determina se questa aula soddisfa tutte le facilities richieste
     * rappresentate da un certo insieme dato.
     * 
     * @param requestedFacilities
     *                                l'insieme di facilities richieste da
     *                                soddisfare
     * @return true se e solo se tutte le facilities di
     *         {@code requestedFacilities} sono soddisfatte da questa aula.
     * @throws NullPointerException
     *                                  se il set di facility richieste è nullo
     */
    public boolean satisfiesFacilities(Set<Facility> requestedFacilities) {
        if(requestedFacilities == null)
            throw new NullPointerException("requestedFacilities è null! Inserire valore valido");

        // Creazione iteratore per requested facilities
        Iterator<Facility> iterReqFacility = requestedFacilities.iterator();
        Facility facility = null;

        while (iterReqFacility.hasNext()) {
            facility = iterReqFacility.next();

            // Se non si ha una facility allora false
            if(!facilities.contains(facility))
                return false;
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
            throw new NullPointerException("time slot passato è null! Inserire valore valido");
        if(docente == null)
            throw new NullPointerException("docente passato è null! Inserire valore valido");
        if(motivo == null)
            throw new NullPointerException("motivo passato è null! Inserire valore valido");
        
        // Controllo spazio libero per prenotazione
        if(!isFree(ts))
            throw new IllegalArgumentException("Sovrapposizione di prenotazioni!");
        
        // Aggiunta prenotazione
        prenotazioni.add(new Prenotazione(this, ts, docente, motivo));
    }

    /**
     * Cancella una prenotazione di questa aula.
     * 
     * @param p
     *              la prenotazione da cancellare
     * @return true se la prenotazione è stata cancellata, false se non era
     *         presente.
     * @throws NullPointerException
     *                                  se la prenotazione passata è null
     */
    public boolean removePrenotazione(Prenotazione p) {
        if(p == null)
            throw new NullPointerException("prenotazione passata è null! Inserire valore valido");
        
        return prenotazioni.remove(p);
    }

    /**
     * Rimuove tutte le prenotazioni di questa aula che iniziano prima (o
     * esattamente in) di un punto nel tempo specificato.
     * 
     * @param timePoint
     *                      un certo punto nel tempo
     * @return true se almeno una prenotazione è stata cancellata, false
     *         altrimenti.
     * @throws NullPointerException
     *                                  se il punto nel tempo passato è nullo.
     */
    public boolean removePrenotazioniBefore(GregorianCalendar timePoint) {
        if(timePoint == null)
            throw new NullPointerException("timePoint è null! Inserire valore valido");
            
        // Creazione iteratore e flag
        Iterator<Prenotazione> iterPrenotazioni = prenotazioni.iterator();
        Prenotazione pren = null;
        boolean flag = false;

        while (iterPrenotazioni.hasNext()) {
            pren = iterPrenotazioni.next();
            int point = pren.getTimeSlot().getStart().compareTo(timePoint);

            /*
             * Se il tempo controllato è minore di timePoint
             * allora rimuovi prenotazione altrimenti esce dal loop 
             */
            if(point <= 0){
                iterPrenotazioni.remove();
                flag = true;
                continue;
            }

            break;
            
        }

        return flag;
    }
}
