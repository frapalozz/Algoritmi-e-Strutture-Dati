package it.unicam.cs.asdl2425.es7;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente
         * 
         */
        if(o == null)
            throw new NullPointerException("Ogetto passato è null!");

        int key = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        if(table[key] == null)
            return false;

        Iterator<E> iterator = new Itr();
        while (iterator.hasNext()) {
            if(iterator.next().equals(o))
                return true;
        }
        
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui inserire
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve inserire l'elemento o
         * nella lista concatenata lì presente. Se vuota, si crea la lista
         * concatenata e si inserisce l'elemento, che sarà l'unico.
         * 
         */
        // ATTENZIONE, si inserisca prima il nuovo elemento e poi si controlli
        // se bisogna fare resize(), cioè se this.size >
        // this.getCurrentThreshold()
        if(e == null)
            throw new NullPointerException("Elemento passato è null!");

        int key = this.phf.hash(e.hashCode(), this.getCurrentCapacity());

        // Controlla se nella posizione key non c'è ancora nessun elemento
        if(this.table[key] == null){
            this.table[key] = new Node<E>(e, null);
            this.size++;

            // Resize se necessario
            if(this.size > this.getCurrentThreshold())
                this.resize();

            this.modCount++;
            return true;
        }
        
        // Controlla se la posizione k contiene già questo elemento
        if(this.contains(e))
            return false;

        // Crea il nuovo nodo e lo aggiungo alla posizione k
        Node<E> nuovoElemento = new Node<E>(e, (Node<E>)this.table[key]);
        this.table[key] = nuovoElemento;
        this.size++;

        // Resize se necessario
        if(this.size > this.getCurrentThreshold())
            this.resize();

        this.modCount++;
        return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
        // TODO implementare
        CollisionListResizableHashTable<E> newHashTable = new CollisionListResizableHashTable<E>(phf);
        newHashTable.table = new Object[this.getCurrentCapacity()*2];

        Iterator<E> iterator = new Itr();
        while (iterator.hasNext()) {
            newHashTable.add(iterator.next());
        }

        this.table = newHashTable.table;
        this.modCount++;
    }

    @Override
    public boolean remove(Object o) {
        // TODO implementare
        /*
         * ATTENZIONE: usare l'hashCode dell'oggetto e la funzione di hash
         * primaria passata all'atto della creazione: il bucket in cui cercare
         * l'oggetto o è la posizione
         * this.phf.hash(o.hashCode(),this.getCurrentCapacity)
         * 
         * In questa posizione, se non vuota, si deve cercare l'elemento o
         * utilizzando il metodo equals() su tutti gli elementi della lista
         * concatenata lì presente. Se presente, l'elemento deve essere
         * eliminato dalla lista concatenata
         * 
         */
        // ATTENZIONE: la rimozione, in questa implementazione, **non** comporta
        // mai una resize "al ribasso", cioè un dimezzamento della tabella se si
        // scende sotto il fattore di bilanciamento desiderato.

        int key = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        if(this.table[key] == null)
            return false;
        
        // Creo nodo e nodo precedente
        Node<E> node = (Node<E>) table[key];
        Node<E> previousNode = null;

        // Ricerca nodo da eliminare
        while (node != null) {
            // Trovato item
            if(node.item.equals(o)) {
                // Se item si trova nella prima posizione della lista, allora sposto il puntatore in avanti
                if(previousNode == null)
                    table[key] = node.next;
                
                // Se item non si trova nella prima posizione, allora il nodo precedente deve puntare al nodo successivo di quello da eliminare
                else {
                    previousNode = node.next;
                }
                this.size--;
                this.modCount++;
                return true;
            }

            previousNode = node;
            node = node.next;
        }

        // elemento non trovato nella hashTable, non presente
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo
        // contains
        if(c == null)
            throw new NullPointerException("Collection passata è null!");

        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext()) {
            if(!this.contains(iterator.next()));
                return false;
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo add
        if(c == null)
            throw new NullPointerException("Collection passato null!");
            
        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext()) {
            this.add((E) iterator.next());
        }
        
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        // TODO implementare
        // utilizzare un iteratore della collection e chiamare il metodo remove
        Iterator<?> iterator = c.iterator();
        while (iterator.hasNext()) {
            this.remove(iterator.next());
        }

        return true;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {

        // TODO inserire le variabili che servono

        private int numeroModificheAtteso;

        private int lastKey;
        private Node<E> lastReturnedNode;

        private Itr() {
            // TODO implementare il resto
            this.numeroModificheAtteso = modCount;
            this.lastKey = -1;
            this.lastReturnedNode = null;
        }

        @Override
        public boolean hasNext() {
            // TODO implementare

            // Vedo se dopo l'ultimo nodo controllato esiste un altro nodo
            if(this.lastReturnedNode != null && this.lastReturnedNode.next != null)
                return true;

            if(lastKey+1 == table.length)
                return false;

            // Se non è ancora stato controllato nessun nodo o lastReturnedNode.next == null allora cerco una key che contiene almeno 1 nodo
            for(int i = lastKey+1; i < table.length; i++) {
                if(table[i] != null)
                    return true;
            }

            // Se la hashTable non contiene più nessun altro elemento
            return false;
        }

        @Override
        public E next() {
            // TODO implementare

            // Controllo concorrenza
            if(modCount != this.numeroModificheAtteso)
                throw new ConcurrentModificationException("Modifica hash table durante iteratore!");

            // Controllo hasNext()
            if(!hasNext())
                throw new NoSuchElementException("Richiesta di next quando hasNext è falso");

            // C'è sicuramente un next
            // Vedo se dopo l'ultimo nodo controllato esiste un altro nodo
            if(this.lastReturnedNode != null && this.lastReturnedNode.next != null) {
                this.lastReturnedNode = this.lastReturnedNode.next;
                return this.lastReturnedNode.item;
            }

            // Se non è ancora stato controllato nessun nodo o lastReturnedNode.next == null allora cerco una key che contiene almeno 1 nodo
            for(int i = lastKey+1; i < table.length; i++){
                if(table[i] != null)
                    lastKey = i;
            }
            return ((Node<E>) table[lastKey]).item;
        }

    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
