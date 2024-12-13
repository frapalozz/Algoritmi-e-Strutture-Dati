/**
 * 
 */
package it.unicam.cs.asdl2425.es10;

import java.util.List;

/**
 * Classe che implementa un algoritmo di ordinamento basato su heap.
 * 
 * @author Template: Luca Tesei, Implementation: collettiva
 *
 */
public class HeapSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {

        int heapSize = l.size()-1;
        int countCompare = 0;

        // Crea Max-Heap
        countCompare += buildMaxHeap(l);
        
        // Ordinamento
        for(int i = heapSize; i > 0; i--){
            scambia(0, i, l);
            countCompare += heapify(0, heapSize--, l);
        }

        return new SortingAlgorithmResult<>(l, countCompare);
    }

    private int buildMaxHeap(List<E> l){
        int countCompare = 0;

        // Costruisci Max-Heap
        for(int i = l.size()/2-1; i >= 0; i--){
            countCompare += heapify(i, l.size(), l);
        }

        return countCompare;
    }

    private int heapify(int i, int end, List<E> l) {
        int left = i*2+1; // Figlio sinistro
        int right = i*2+2;// Figlio destro
        int massimo, countCompare = 0;

        if(left < end && l.get(left).compareTo(l.get(i)) > 0)
            // Se il figlio sinistro è più grande di suo padre, allora salva il suo indice in massimo
            massimo = left;

        else
            // Altrimenti salva l'indice del padre
            massimo = i;
        countCompare++;
            

        if(right < end && l.get(right).compareTo(l.get(massimo)) > 0)
            // Se il figlio destro del padre è più grande del massimo, allora salva l'indice del figlio destro
            massimo = right;
        countCompare++;

        if(massimo != i){
            // Se il massimo non è il padre, allora scambia il padre con il massimo e richiama heapify sull'indice del massimo
            scambia(i, massimo, l);
            countCompare += heapify(massimo, end, l);
        }

        return countCompare;
    }

    private void scambia(int a, int b, List<E> l){
        E itemA = l.remove(a);
        E itemB = l.remove(b-1);
        l.add(a, itemB);
        l.add(b, itemA);
    }

    @Override
    public String getName() {
        return "HeapSort";
    }

}
