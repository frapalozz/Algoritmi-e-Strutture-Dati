/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.ArrayList;
import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        if(l == null)
            throw new NullPointerException();

        if(l.size() < 2)
            return new SortingAlgorithmResult<>(l, 0);


        int[] countCompare = {0};

        mergeSort(l, 0, l.size()-1, countCompare);

        return new SortingAlgorithmResult<>(l, countCompare[0]);
    }

    private void mergeSort(List<E> l, int left, int right, int[] countCompare){
        if(left < right){
            int center = (left + right)/2;
            mergeSort(l, left, center, countCompare);
            mergeSort(l, center+1, right, countCompare);
            merge(l, left, center, right, countCompare);
        }
    }

    private void merge(List<E> l, int left, int center, int right, int[] countCompare){
        int i = left;
        int j = center+1;
        int k = 0;
        List<E> b = new ArrayList<>();

        while (i <= center && j <= right) {
            if(l.get(i).compareTo(l.get(j)) <= 0){
                k++;
                b.add(l.get(i++));
            }
            else{
                k++;
                b.add(l.get(j++));
            }
                
            countCompare[0]++;
        }

        while (i <= center) {
            k++;
            b.add(l.get(i++));
        }

        while (j <= right) {
            k++;
            b.add(l.get(i++));
        }

        for(k = left; k <= right; k++){
            l.add(k, b.get(k-left));
        }
    }

    public String getName() {
        return "MergeSort";
    }
}
