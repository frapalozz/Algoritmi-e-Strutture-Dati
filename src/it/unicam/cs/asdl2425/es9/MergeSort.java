/**
 * 
 */
package it.unicam.cs.asdl2425.es9;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null)
            throw new NullPointerException();

        if(l.size() < 2)
            return new SortingAlgorithmResult<>(l, 0);

        return new SortingAlgorithmResult<>(l, mergeSort(l, 0, l.size()-1));
    }

    private int mergeSort(List<E> l, int left, int right){

        int countCompare = 0;
        // Se la sinistra è più piccola della destra allora non eseguire il mergeSort
        if(left < right){
            // Prendi il mezzo dell'array
            int center = (left + right)/2;

            // Esegui mergeSort a sinistra
            countCompare += mergeSort(l, left, center);
            // Esegui mergeSort a destra
            countCompare += mergeSort(l, center+1, right);
            // Unisci ordinando
            countCompare += merge(l, left, center, right);
        }

        return countCompare;
    }

    private int merge(List<E> unsortedList, int left, int center, int right){
        if(left >= right)
            // Se la sinistra è uguale o maggiore di desta allora non eseguire il merge
            return 0;

        int countCompare = 0; // contatore controlli
        int indexLeft = left; // Indice lato sinistro
        int indexRight = center+1; // Indice lato destro
        List<E> sortedList = new ArrayList<>();
        int k = 0;

        // Compara destra e sinistra
        while (indexLeft <= center && indexRight <= right) {
            if(unsortedList.get(indexLeft).compareTo(unsortedList.get(indexRight)) <= 0)
                // Se nell'indice nel lato sinistro il valore è più piccolo, allora aggiungilo all'array
                sortedList.add(unsortedList.get(indexLeft++));
            else
                // Se nell'indice nel lato destro il valore è più piccolo, allora aggiungilo all'array
                sortedList.add(unsortedList.get(indexRight++));
                
            countCompare++;
        }

        // Aggiungi tutti i valori rimanenti nel lato sinistro
        while (indexLeft <= center) 
            sortedList.add(unsortedList.get(indexLeft++));

        // Aggiungi tutti i valori rimanenti nel lato destro
        while (indexRight <= right) 
            sortedList.add(unsortedList.get(indexRight++));

        // Aggiungi modifiche all'array principale
        for(int i = left; i <= right; i++)
            unsortedList.set(i, sortedList.get(k++));

            return countCompare;
    }

    public String getName() {
        return "MergeSort";
    }
}
