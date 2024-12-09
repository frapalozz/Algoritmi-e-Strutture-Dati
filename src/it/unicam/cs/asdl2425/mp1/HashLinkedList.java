package it.unicam.cs.asdl2425.mp1;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Una classe che rappresenta una lista concatenata con il calcolo degli hash
 * MD5 per ciascun elemento. Ogni nodo della lista contiene il dato originale di
 * tipo generico T e il relativo hash calcolato utilizzando l'algoritmo MD5.
 *
 * <p>
 * La classe supporta le seguenti operazioni principali:
 * <ul>
 * <li>Aggiungere un elemento in testa alla lista</li>
 * <li>Aggiungere un elemento in coda alla lista</li>
 * <li>Rimuovere un elemento dalla lista in base al dato</li>
 * <li>Recuperare una lista ordinata di tutti gli hash contenuti nella
 * lista</li>
 * <li>Costruire una rappresentazione testuale della lista</li>
 * </ul>
 *
 * <p>
 * Questa implementazione include ottimizzazioni come il mantenimento di un
 * riferimento all'ultimo nodo della lista (tail), che rende l'inserimento in
 * coda un'operazione O(1).
 *
 * <p>
 * La classe utilizza la classe HashUtil per calcolare l'hash MD5 dei dati.
 *
 * @param <T>
 *                il tipo generico dei dati contenuti nei nodi della lista.
 * 
 * @author Luca Tesei, Marco Caputo (template), Francesco Palozzi francesco.palozzi@studenti.unicam.it (implementazione)
 * 
 */
public class HashLinkedList<T> implements Iterable<T> {
    private Node head; // Primo nodo della lista

    private Node tail; // Ultimo nodo della lista

    private int size; // Numero di nodi della lista

    private int numeroModifiche; // Numero di modifiche effettuate sulla lista
                                 // per l'implementazione dell'iteratore
                                 // fail-fast

    public HashLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
        this.numeroModifiche = 0;
    }

    /**
     * Restituisce il numero attuale di nodi nella lista.
     *
     * @return il numero di nodi nella lista.
     */
    public int getSize() {
        return size;
    }

    /**
     * Rappresenta un nodo nella lista concatenata.
     */
    private class Node {
        String hash; // Hash del dato

        T data; // Dato originale

        Node next;

        Node(T data) {
            this.data = data;
            this.hash = HashUtil.dataToHash(data);
            this.next = null;
        }
    }

    /**
     * Aggiunge un nuovo elemento in testa alla lista.
     *
     * @param data
     *                 il dato da aggiungere.
     */
    public void addAtHead(T data) {

        // Creazione nodo da aggiungere
        Node newNode = new Node(data);

        // Se la lista è vuota allora crea coda e testa
        if(this.size == 0){
            this.head = newNode;
            this.tail = newNode;
        }
        // Se la lista non è vuota allora sostituisco la head con il nuovo nodo
        else{
            newNode.next = this.head;
            this.head = newNode;
        }

        // Aumenta size numero modifiche
        this.size++;
        this.numeroModifiche++;
    }

    /**
     * Aggiunge un nuovo elemento in coda alla lista.
     *
     * @param data
     *                 il dato da aggiungere.
     */
    public void addAtTail(T data) {

        // Creazione nodo da aggiungere
        Node newNode = new Node(data);

        // Se la lista è vuota allora crea coda e testa
        if(this.size == 0){
            this.head = newNode;
            this.tail = newNode;
        }
        // Se la lista non è vuota allora sostituisco la tail con il nuovo nodo
        else {
            this.tail.next = newNode;
            this.tail = newNode;
        }

        // Aumenta size numero modifiche
        this.size++;
        this.numeroModifiche++;
    }

    /**
     * Restituisce un'ArrayList contenente tutti gli hash nella lista in ordine.
     *
     * @return una lista con tutti gli hash della lista.
     */
    public ArrayList<String> getAllHashes() {

        // Creazione ArrayList 
        ArrayList<String> arrayHashes = new ArrayList<>();
        
        // Puntatore al nodo head
        Node currentNode = this.head;

        // Iterazione su tutti i nodi della lista
        while (currentNode != null) {
            // Inserimento hash del nodo corrente nel ArrayList
            arrayHashes.add(currentNode.hash);
            
            // Puntatore currentNode al nodo seguente
            currentNode = currentNode.next;
        }

        return arrayHashes;
    }

    /**
     * Costruisce una stringa contenente tutti i nodi della lista, includendo
     * dati e hash. La stringa dovrebbe essere formattata come nel seguente
     * esempio:
     * 
     * <pre>
     *     Dato: StringaDato1, Hash: 5d41402abc4b2a76b9719d911017c592
     *     Dato: SteringaDato2, Hash: 7b8b965ad4bca0e41ab51de7b31363a1
     *     ...
     *     Dato: StringaDatoN, Hash: 2c6ee3d301aaf375b8f026980e7c7e1c
     * </pre>
     *
     * @return una rappresentazione testuale di tutti i nodi nella lista.
     */
    public String buildNodesString() {

        // Creazione stringBuffer
        StringBuffer nodesString = new StringBuffer();

        // Puntatore al nodo head
        Node currentNode = this.head;

        // Iterazione sui nodi della lista
        while (currentNode != null) {
            // Crea il testo del seguente nodo e lo aggiunge alla stringa nodesString
            nodesString.append("Dato: " + currentNode.data + ", Hash: " + currentNode.hash + "\n");

            // Puntatore currentNode al nodo seguente
            currentNode = currentNode.next;
        }

        return nodesString.toString();
    }

    /**
     * Rimuove il primo elemento nella lista che contiene il dato specificato.
     *
     * @param data
     *                 il dato da rimuovere.
     * @return true se l'elemento è stato trovato e rimosso, false altrimenti.
     */
    public boolean remove(T data) {

        // nodo osservato
        Node currentNode = this.head;
        // nodo precedente a quello attualmente ossvato
        Node previousNode = null;   

        // Itera per trovare il nodo da eliminare
        while (currentNode != null && !currentNode.data.equals(data)) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }

        // Nodo non presente
        if(currentNode == null)
            return false;

        // nodo da eliminare nella testa
        if(previousNode == null){
            // Unico nodo nella lista, allora svuoto la lista
            if(this.size == 1) {
                this.head = null;
                this.tail = null;
            }
            
            // Non è unico nodo nella lista, allora la head punta al nodo dopo la testa corrente
            else this.head = currentNode.next;
        }

        // nodo da eliminare non in testa
        else{
            // nodo da eliminare in coda
            if(currentNode.next == null){
                previousNode.next = null;
                this.tail = previousNode;
            }
            // nodo da eliminare nel mezzo
            else
                previousNode.next = currentNode.next;
        }

        this.numeroModifiche++;
        this.size--;
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        return new Itr();
    }

    /**
     * Classe che realizza un iteratore fail-fast per HashLinkedList.
     */
    private class Itr implements Iterator<T> {

        // expectedModCount serve per controllo fail-fast
        private int expectedModCount;
        // ultimo nodo ritornato da next()
        private Node lastReturnedNode;

        private Itr() {

            // Setup numero modifiche per fail-fast e ultimo nodo ritornato
            this.expectedModCount = HashLinkedList.this.numeroModifiche;
            this.lastReturnedNode = null;
        }

        @Override
        public boolean hasNext() {

            if (this.lastReturnedNode == null)
                // inizio iterazione, quindi controlla se esiste la testa
                return HashLinkedList.this.head != null;

            // Controlla se esiste il nodo seguente
            return this.lastReturnedNode.next != null;
        }

        @Override
        public T next() {

            // Controllo se durante l'iterazione è stata modificata la lista (fail-fast)
            if (this.expectedModCount != HashLinkedList.this.numeroModifiche)
                throw new ConcurrentModificationException("Lista modificata");
            
            // Controllo elemento successivo
            if (!hasNext())
                throw new NoSuchElementException("Richiesta next senza nessun elemento successivo");

            // C'è sicuramente un altro elemento
            // aggiorno lastReturnedNode e restituisco l'elemento successivo
            if (this.lastReturnedNode == null) {
                // sono all’inizio della lista
                this.lastReturnedNode = HashLinkedList.this.head;
                return this.lastReturnedNode.data;
            }

            // non sono all’inizio della lista
            this.lastReturnedNode = this.lastReturnedNode.next;
            return this.lastReturnedNode.data;
            
        }
    }
}