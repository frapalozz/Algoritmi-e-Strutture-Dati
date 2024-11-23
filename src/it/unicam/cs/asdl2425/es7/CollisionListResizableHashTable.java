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

    @SuppressWarnings("unchecked")
    @Override
    public boolean contains(Object o) {
        if(o == null)
            throw new NullPointerException("Ogetto passato è null!");

        // Ottieni chiave per elemento o
        int key = this.phf.hash(o.hashCode(), this.getCurrentCapacity());

        // Ricerca oggetto in key
        Node<E> node = (Node<E>) table[key];
        while (node != null) {
            if(node.item.equals(o))
                return true;
            node = node.next;
        }
        
        // Oggetto non presente
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

    @SuppressWarnings("unchecked")
    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException("Elemento passato è null!");

        // chiave per oggetto e
        int key = this.phf.hash(e.hashCode(), this.getCurrentCapacity());
        
        // Controlla se la posizione k contiene già questo elemento
        if(this.contains(e))
            return false;

        // Crea il nuovo nodo e lo aggiungo alla posizione k
        this.table[key] = new Node<E>(e, (Node<E>)this.table[key]);
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
        // Creazione nuova hashTable
        CollisionListResizableHashTable<E> newHashTable = new CollisionListResizableHashTable<E>(phf);
        newHashTable.table = new Object[this.getCurrentCapacity()*2];

        // Passaggio dati da hasTable vecchia -> hasTable nuova
        Iterator<E> iterator = new Itr();
        while (iterator.hasNext()) {
            newHashTable.add(iterator.next());
        }

        // Cambio puntatore table da hashTable vecchia a hashTable nuova
        this.table = newHashTable.table;
        this.modCount++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if(o == null)
            throw new NullPointerException("Elemento passato è null!");

        // chiave per oggetto o
        int key = this.phf.hash(o.hashCode(), this.getCurrentCapacity());
        
        // Creo nodo e nodo precedente
        Node<E> node = (Node<E>) table[key];
        Node<E> previousNode = null;

        // Ricerca nodo da eliminare
        while (node != null) {
            // Trovato elemento da eliminare
            if(node.item.equals(o)) {
                // Se elemento si trova nella prima posizione della lista, allora sposto il puntatore in avanti
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
        if(c == null)
            throw new NullPointerException("Collection passata è null!");

        // Controllo che la hashTable contiene tutti gli elementi
        for (Object element : c) {
            if(!this.contains(element))
                return false;
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if(c == null)
            throw new NullPointerException("Collection passato null!");

        // Aggiunta elementi alla hashTable
        for (E element : c) {
            this.add(element);
        }
        
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if(c == null)
            throw new NullPointerException("Collection passata è null!");

        // Remove di tutti elementi elencati dalla collection
        for (Object element : c) {
            this.remove(element);
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

        private int numeroModificheAtteso;

        // lastKey serve a mantenere l'ultima chiave visitata
        private int lastKey;
        // lastReturnedNode serve a mantenere l'ultimo nodo visitato
        private Node<E> lastReturnedNode;

        private Itr() {
            this.numeroModificheAtteso = modCount;
            this.lastKey = -1;
            this.lastReturnedNode = null;
        }

        @Override
        public boolean hasNext() {
            // Vedo se dopo l'ultimo nodo controllato esiste un altro nodo
            if(this.lastReturnedNode != null && this.lastReturnedNode.next != null)
                return true;

            // Raggiunta fine della hashTable
            if(lastKey+1 >= getCurrentCapacity())
                return false;

            // Se non è ancora stato controllato nessun nodo o lastReturnedNode.next == null allora cerco una key che contiene almeno 1 nodo
            for(int i = lastKey+1; i < getCurrentCapacity(); i++) {
                if(table[i] != null)
                    return true;
            }

            // Se la hashTable non contiene più nessun altro elemento
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
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
            for(int i = lastKey+1; i < getCurrentCapacity(); i++){
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
