/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.List;

// TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare

        if(l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");

        if(l.size() < 2)
            // per ordinare la lista vuota o con un solo elemento non faccio niente
            return new SortingAlgorithmResult<>(l, 0);

        int[] countCompare = {0};
        
        quickSort(l, 0, l.size()-1, countCompare);

        return new SortingAlgorithmResult<>(l, 0);
    }

    private void quickSort(List<E> l, int p, int r, int[] countCompare){
        if(r-p <= 1)
            return;

        E pivot = l.get(r);
        int i = p-1;
        E temp;

        for(int j = p; j <= r; j++){
            if(i == -1 || l.get(i).compareTo(pivot) < 1){
                i++;
                temp = l.get(i);
                l.set(i, l.get(j));
                l.set(j, temp);
            }
            countCompare[0]++;
        }

        quickSort(l, p, i, countCompare);
        quickSort(l, i+1, r, countCompare);
    }

    @Override
    public String getName() {
        return "QuickSort";
    }

}
