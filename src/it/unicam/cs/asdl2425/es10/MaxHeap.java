package it.unicam.cs.asdl2425.es10;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che implementa uno heap binario che può contenere elementi non nulli
 * possibilmente ripetuti.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 * @param <E>
 *                il tipo degli elementi dello heap, che devono avere un
 *                ordinamento naturale.
 */
public class MaxHeap<E extends Comparable<E>> {

    /*
     * L'array che serve come base per lo heap
     */
    private ArrayList<E> heap;

    /**
     * Costruisce uno heap vuoto.
     */
    public MaxHeap() {
        this.heap = new ArrayList<E>();
    }

    /**
     * Restituisce il numero di elementi nello heap.
     * 
     * @return il numero di elementi nello heap
     */
    public int size() {
        return this.heap.size();
    }

    /**
     * Determina se lo heap è vuoto.
     * 
     * @return true se lo heap è vuoto.
     */
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    /**
     * Costruisce uno heap a partire da una lista di elementi.
     * 
     * @param list
     *                 lista di elementi
     * @throws NullPointerException
     *                                  se la lista è nulla
     */
    public MaxHeap(List<E> list) {
        if(list == null)
            throw new NullPointerException("list null!");

        // Creazione Heap
        heap = new ArrayList<>(list);

        // Ordina Max-Heap
        for(int i = heap.size()/2-1; i >= 0; i--){
            heapify(i);
        }
    }

    /**
     * Inserisce un elemento nello heap
     * 
     * @param el
     *               l'elemento da inserire
     * @throws NullPointerException
     *                                  se l'elemento è null
     * 
     */
    public void insert(E el) {
        if(el == null)
            throw new NullPointerException("el null!");

        // prendi indice dell'elemento inserito
        int i = heap.size();
        heap.add(el);

        while(i > 0 && heap.get(parentIndex(i)).compareTo(heap.get(i)) < 0){
            // Finchè l'elemento inserito è più grande di suo padre, scambiali
            scambia(i, parentIndex(i));
            i = parentIndex(i);
        }
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio sinistro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int leftIndex(int i) {
        return i*2 + 1;
    }

    /*
     * Funzione di comodo per calcolare l'indice del figlio destro del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int rightIndex(int i) {
        return i*2 + 2;
    }

    /*
     * Funzione di comodo per calcolare l'indice del genitore del nodo in
     * posizione i. Si noti che la posizione 0 è significativa e contiene sempre
     * la radice dello heap.
     */
    private int parentIndex(int i) {
        return (i-1)/2;
    }

    /**
     * Ritorna l'elemento massimo senza toglierlo.
     * 
     * @return l'elemento massimo dello heap oppure null se lo heap è vuoto
     */
    public E getMax() {
        if(heap.size() == 0)
            return null;

        return heap.get(0);
    }

    /**
     * Estrae l'elemento massimo dallo heap. Dopo la chiamata tale elemento non
     * è più presente nello heap.
     * 
     * @return l'elemento massimo di questo heap oppure null se lo heap è vuoto
     */
    public E extractMax() {
        if(heap.size() == 0)
            return null;

        if(heap.size() == 1)
            return heap.remove(0);
        

        // Sposta l'elemento più grande nell'ultima posizione
        scambia(0, heap.size()-1);
        // Estrai l'ultimo elemento
        E max = heap.remove(heap.size()-1);

        // Riordina l'heap
        heapify(0);

        return max;
    }

    /*
     * Ricostituisce uno heap a partire dal nodo in posizione i assumendo che i
     * suoi sottoalberi sinistro e destro (se esistono) siano heap.
     */
    private void heapify(int i) {
        int left = leftIndex(i);
        int right = rightIndex(i);
        int massimo;

        if(left < heap.size() && heap.get(left).compareTo(heap.get(i)) > 0)
            // Se il figlio sinistro è più grande di suo padre, allora salva il suo indice in massimo
            massimo = left;
        else
            // Altrimenti salva l'indice del padre
            massimo = i;

        if(right < heap.size() && heap.get(right).compareTo(heap.get(massimo)) > 0)
            // Se il figlio destro del padre è più grande del massimo, allora salva l'indice del figlio destro
            massimo = right;

        if(massimo != i){
            // Se il massimo non è il padre, allora scambia il padre con il massimo e richiama heapify sull'indice del massimo
            scambia(i, massimo);
            heapify(massimo);
        }
    }

    private void scambia(int a, int b){
        if(a < b){
            E itemA = heap.remove(a);
            E itemB = heap.remove(b-1);
            heap.add(a, itemB);
            heap.add(b, itemA);
        }
        else {
            E itemB = heap.remove(b);
            E itemA = heap.remove(a-1);
            heap.add(b, itemA);
            heap.add(a, itemB);
        }
    }
    
    /**
     * Only for JUnit testing purposes.
     * 
     * @return the arraylist representing this max heap
     */
    protected ArrayList<E> getHeap() {
        return this.heap;
    }
}
