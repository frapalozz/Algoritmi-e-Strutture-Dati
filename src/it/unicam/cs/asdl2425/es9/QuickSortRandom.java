/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.List;
import java.util.Random;

/**
 * Implementazione del QuickSort con scelta della posizione del pivot scelta
 * randomicamente tra le disponibili. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSortRandom<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    private static final Random randomGenerator = new Random();

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");

        if(l.size() < 2)
            // per ordinare la lista vuota o con un solo elemento non faccio niente
            return new SortingAlgorithmResult<>(l, 0);

        return new SortingAlgorithmResult<>(l, randomQuickSort(l, 0, l.size()-1));
    }

    private void random(List<E> unsortedList, int left, int right){

        int pivot = randomGenerator.nextInt(right-left)+left;

        E temp = unsortedList.get(pivot);
        unsortedList.set(pivot, unsortedList.get(right));
        unsortedList.set(right, temp);
    }

    private int randomQuickSort(List<E> unsortedList, int left, int right){
        if(left >= right)
            return 0;

        random(unsortedList, left, right);
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

        countCompare += randomQuickSort(unsortedList, left, i-1);
        countCompare += randomQuickSort(unsortedList, i+1, right);

        return countCompare;
    }

    @Override
    public String getName() {
        return "QuickSortRandom";
    }

}
