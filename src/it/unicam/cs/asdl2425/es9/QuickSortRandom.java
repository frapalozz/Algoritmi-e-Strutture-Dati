/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.List;
import java.util.Random;

//TODO completare import

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
        // TODO implementare
        if(l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");

        if(l.size() < 2)
            // per ordinare la lista vuota o con un solo elemento non faccio niente
            return new SortingAlgorithmResult<>(l, 0);

        return new SortingAlgorithmResult<>(l, randomQuickSort(l, 0, l.size()-1));
    }

    private int randomQuickSort(List<E> unsortedList, int left, int right){
        if(left >= right){
            return 0;
        }

        int countCompare = 0;
        int rightVal = randomGenerator.nextInt(right+1);
        
        if(rightVal <= left)
            rightVal = left+1;

        E pivot = unsortedList.get(rightVal);
        int i = left-1;
        E temp;

        for(int j = rightVal+1; j <= right; j++) {
            if(unsortedList.get(j).compareTo(pivot) < 1){
                temp = unsortedList.get(j);
                unsortedList.set(j, unsortedList.get(rightVal));
                unsortedList.set(rightVal, temp);
                rightVal = j;
            }
        }
        for(int j = left; j < rightVal; j++){
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
        unsortedList.set(i, unsortedList.get(rightVal));
        unsortedList.set(rightVal, temp);
        
        

        // Sinistra
        countCompare += randomQuickSort(unsortedList, left, i-1);
        // Destra
        countCompare += randomQuickSort(unsortedList, i+1, right);

        return countCompare;
    }

    @Override
    public String getName() {
        return "QuickSortRandom";
    }

}
