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

        return new SortingAlgorithmResult<E>(l, quickSort(l, 0, l.size()-1));
    }

    private int quickSort(List<E> unsortedList, int left, int right){
        if(left >= right)
            return 0;

        E pivot = unsortedList.get(right);
        int i = left-1;
        E temp;
        int countCompare = 0;

        for(int j = left; j < right; j++){
            if(unsortedList.get(j).compareTo(pivot) < 1){
                i++;
                temp = unsortedList.get(i);
                unsortedList.set(i, unsortedList.get(j));
                unsortedList.set(j, temp);
            }
            countCompare++;
        }
        i++;
        temp = unsortedList.get(i);
        unsortedList.set(i, unsortedList.get(right));
        unsortedList.set(right, temp);

        countCompare += quickSort(unsortedList, left, i-1);
        countCompare += quickSort(unsortedList, i+1, right);

        return countCompare;
    }

    @Override
    public String getName() {
        return "QuickSort";
    }

}
