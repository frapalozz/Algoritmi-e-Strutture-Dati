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

        int[] countCompare = {0};
        
        randomQuickSort(l, 0, l.size()-1, countCompare);


        return null;
    }

    private void randomQuickSort(List<E> l, int p, int r, int[] countCompare){
        if(r-p == 1)
            return;

        E pivot = l.get(randomGenerator.nextInt(r));
        int i = p;
        E temp;

        for(int j = p+1; j <= r; j++){
            if(l.get(i).compareTo(pivot) < 1){
                i++;
                temp = l.get(i);
                l.set(i, l.get(j));
                l.set(j, temp);
            }
            countCompare[0]++;
        }
        temp = l.get(i);
        l.set(i, l.get(r));
        l.set(r, temp);

        randomQuickSort(l, p, i, countCompare);
        randomQuickSort(l, i+1, r, countCompare);
    }

    @Override
    public String getName() {
        return "QuickSortRandom";
    }

}
