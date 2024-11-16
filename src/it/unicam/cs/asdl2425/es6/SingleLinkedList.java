package it.unicam.cs.asdl2425.es6;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Lista concatenata singola che non accetta valori null, ma permette elementi
 * duplicati. Le seguenti operazioni non sono supportate:
 * 
 * <ul>
 * <li>ListIterator<E> listIterator()</li>
 * <li>ListIterator<E> listIterator(int index)</li>
 * <li>List<E> subList(int fromIndex, int toIndex)</li>
 * <li>T[] toArray(T[] a)</li>
 * <li>boolean containsAll(Collection<?> c)</li>
 * <li>addAll(Collection<? extends E> c)</li>
 * <li>boolean addAll(int index, Collection<? extends E> c)</li>
 * <li>boolean removeAll(Collection<?> c)</li>
 * <li>boolean retainAll(Collection<?> c)</li>
 * </ul>
 * 
 * L'iteratore restituito dal metodo {@code Iterator<E> iterator()} è fail-fast,
 * cioè se c'è una modifica strutturale alla lista durante l'uso dell'iteratore
 * allora lancia una {@code ConcurrentMopdificationException} appena possibile,
 * cioè alla prima chiamata del metodo {@code next()}.
 * 
 * @author Luca Tesei
 *
 * @param <E>
 *                il tipo degli elementi della lista
 */
public class SingleLinkedList<E> implements List<E> {

    private int size;

    private Node<E> head;

    private Node<E> tail;

    private int numeroModifiche;

    /**
     * Crea una lista vuota.
     */
    public SingleLinkedList() {
        this.size = 0;
        this.head = null;
        this.tail = null;
        this.numeroModifiche = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. E' dichiarata static perché
     * gli oggetti della classe Node<E> non hanno bisogno di accedere ai campi
     * della classe principale per funzionare.
     */
    private static class Node<E> {
        private E item;

        private Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }

    }

    /*
     * Classe che realizza un iteratore per SingleLinkedList.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la lista è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     * 
     * La classe è non-static perché l'oggetto iteratore, per funzionare
     * correttamente, ha bisogno di accedere ai campi dell'oggetto della classe
     * principale presso cui è stato creato.
     */
    private class Itr implements Iterator<E> {

        private Node<E> lastReturned;

        private int numeroModificheAtteso;

        private Itr() {
            // All'inizio non è stato fatto nessun next
            this.lastReturned = null;
            this.numeroModificheAtteso = SingleLinkedList.this.numeroModifiche;
        }

        @Override
        public boolean hasNext() {
            if (this.lastReturned == null)
                // sono all'inizio dell'iterazione
                return SingleLinkedList.this.head != null;
            else
                // almeno un next è stato fatto
                return lastReturned.next != null;

        }

        @Override
        public E next() {
            // controllo concorrenza
            if (this.numeroModificheAtteso != SingleLinkedList.this.numeroModifiche) {
                throw new ConcurrentModificationException(
                        "Lista modificata durante l'iterazione");
            }
            // controllo hasNext()
            if (!hasNext())
                throw new NoSuchElementException(
                        "Richiesta di next quando hasNext è falso");
            // c'è sicuramente un elemento di cui fare next
            // aggiorno lastReturned e restituisco l'elemento next
            if (this.lastReturned == null) {
                // sono all’inizio e la lista non è vuota
                this.lastReturned = SingleLinkedList.this.head;
                return SingleLinkedList.this.head.item;
            } else {
                // non sono all’inizio, ma c’è ancora qualcuno
                lastReturned = lastReturned.next;
                return lastReturned.item;
            }

        }

    }

    /*
     * Una lista concatenata è uguale a un'altra lista se questa è una lista
     * concatenata e contiene gli stessi elementi nello stesso ordine.
     * 
     * Si noti che si poteva anche ridefinire il metodo equals in modo da
     * accettare qualsiasi oggetto che implementi List<E> senza richiedere che
     * sia un oggetto di questa classe:
     * 
     * obj instanceof List
     * 
     * In quel caso si può fare il cast a List<?>:
     * 
     * List<?> other = (List<?>) obj;
     * 
     * e usando l'iteratore si possono tranquillamente controllare tutti gli
     * elementi (come è stato fatto anche qui):
     * 
     * Iterator<E> thisIterator = this.iterator();
     * 
     * Iterator<?> otherIterator = other.iterator();
     * 
     * ...
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (!(obj instanceof SingleLinkedList))
            return false;
        SingleLinkedList<?> other = (SingleLinkedList<?>) obj;
        // Controllo se entrambe liste vuote
        if (head == null) {
            if (other.head != null)
                return false;
            else
                return true;
        }
        // Liste non vuote, scorro gli elementi di entrambe
        Iterator<E> thisIterator = this.iterator();
        Iterator<?> otherIterator = other.iterator();
        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            E o1 = thisIterator.next();
            // uso il polimorfismo di Object perché non conosco il tipo ?
            Object o2 = otherIterator.next();
            // il metodo equals che si usa è quello della classe E
            if (!o1.equals(o2))
                return false;
        }
        // Controllo che entrambe le liste siano terminate
        return !(thisIterator.hasNext() || otherIterator.hasNext());
    }

    /*
     * L'hashcode è calcolato usando gli hashcode di tutti gli elementi della
     * lista.
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        // implicitamente, col for-each, uso l'iterator di questa classe
        for (E e : this)
            hashCode = 31 * hashCode + e.hashCode();
        return hashCode;
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
        if(o == null)
            throw new NullPointerException("Elemento passato null!");

        // for-each con iteratore di questa classe, implicitamente
        for (E e : this) 
            if(e.equals(o))
                return true;

        return false;

        // Ricerca con iteratore, esplecitamente
        /* Iterator<E> iterator = new Itr();
        
        while (iterator.hasNext()) 
            if(iterator.next().equals(o))
                return true;
        
        return false; */
    }

    @Override
    public boolean add(E e) {
        if(e == null)
            throw new NullPointerException("Elemento passato null!");


        Node<E> newTail = new Node<E>(e, null);

        // Se la linkedList è vuota allora crea la head
        if(this.size == 0) {
            this.head = newTail;
            this.tail = newTail;
        }
        // Se la linkList non è vuota allora aggiungi elemento alla coda
        else {
            this.tail.next = newTail;
            this.tail = newTail;
        }
        
        this.numeroModifiche++;
        this.size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if(o == null)
            throw new NullPointerException("elemento passato null!");

        if(this.size == 0) 
            return false;
        
        
        Node<E> node = this.head;
        Node<E> previousNode = node;

        // Controlla se uguale a Head
        if(this.head.item.equals(o)) {
            if(this.size == 1) {
                this.head = null;
                this.tail = null;
            }
            else this.head = node.next;
            
            this.numeroModifiche++;
            this.size--;
            return true;
        }

        node = node.next;

        // Cerca elemento dentro linkedList
        while (node != null) {

            if(node.item.equals(o)) {
                
                // Se siamo nella coda
                if(node.next == null) {
                    previousNode.next = null;
                    this.tail = previousNode;
                }
                // Se non siamo nella coda
                else 
                    previousNode = node.next;

                this.numeroModifiche++;
                this.size--;
                return true;
            }

            previousNode = node;
            node = node.next;
        }
    
        return false;
    }

    @Override
    public void clear() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.numeroModifiche++;
    }

    @Override
    public E get(int index) {
        if(index >= this.size || index < 0) 
            throw new IndexOutOfBoundsException("Index fuori range!");

        Iterator<E> iterator = new Itr();
        for(int i = 0; i < index; i++)
            iterator.next();

        return iterator.next();
    }

    @Override
    public E set(int index, E element) {
        if(element == null)
            throw new NullPointerException("element è null!");

        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("Index fouri range!");
        
        this.numeroModifiche++;

        // Elemento da settare a fine lista
        if(index == this.size-1) {
            E oldItem = this.tail.item;
            this.tail.item = element;
            return oldItem;
        }

        // raggiungi nodo al punto index
        Node<E> node = this.head;
        for(int i = 0; i < index; i++) 
            node = node.next;

        // Prendi vecchio elemento e sostituisci
        E oldItem = node.item;
        node.item = element;
        
        return oldItem;
    }

    @Override
    public void add(int index, E element) {
        if(element == null)
            throw new NullPointerException("element è null!");

        if(index > this.size || index < 0)
            throw new IndexOutOfBoundsException("Index fouri range!");

        // Se primo elemento della lista, o elemento da aggiungere a fine lista
        if(this.size == 0 || index == this.size) {
            this.add(element);
            return;
        }

        // Elemento da giungere nel mezzo
        Node<E> node = this.head;
        Node<E> previousNode = null;
        for(int i = 0; i < index; i++){
            previousNode = node;
            node = node.next;
        }

        // Controlla se bisogna aggiungere elemento ad index 0
        if(previousNode == null) 
            this.head = new Node<E>(element, node.next);
        else previousNode.next = new Node<E>(element, node);

        this.size++;
        this.numeroModifiche++;
    }

    @Override
    public E remove(int index) {
        if(index >= this.size || index < 0)
            throw new IndexOutOfBoundsException("Index fouri range!");

        E item = null;

        // Se lista da 1 elemento
        if(this.size == 1) {
            item = this.head.item;
            this.clear();
            return item;
        }

        // Elemento da rimuovere a inizio lista
        if(index == 0) {
            item = this.head.item;
            this.head = this.head.next;
        }
        // Elemento da rimuovere a fine lista
        else if(index == this.size) {
            item = this.tail.item;

            Node<E> node = this.head;
            for(int i = 1; i < index; i++) 
                node = node.next;
            this.tail = node;
            node.next = null;
        }
        // Elemento da rimuovere nel mezzo
        else {
            Node<E> node = this.head;
            Node<E> nextNode = null;
            for(int i = 1; i < index; i++) 
                node = node.next;
            
            item = node.next.item;
            nextNode = node.next.next;

            node.next = nextNode;
        }

        this.size--;
        this.numeroModifiche++;
        return item;
    }

    @Override
    public int indexOf(Object o) {
        if(o == null)
            throw new NullPointerException("oggetto passato è null!");
        
        int index = 0;
        // cerca indice elemento == o
        for (E e : this) {
            if(e.equals(o))
                return index;
            index++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        if(o == null)
            throw new NullPointerException("elemento passato null!");

        int index = -1;
        int i = 0;
        // cerca indice elemento == o
        for (E e : this) {
            if(e.equals(o))
                index = i;
            i++;
        }

        return index;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[this.size];
        Iterator<E> iterator = new Itr();

        for(int i = 0; i < this.size; i++) 
            array[i] = iterator.next();
        
        return array;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata.");
    }
}
