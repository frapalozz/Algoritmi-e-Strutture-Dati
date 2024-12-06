package it.unicam.cs.asdl2425.es9;

import java.util.List;

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");

        if(l.size() < 2)
            // per ordinare la lista vuota o con un solo elemento non faccio niente
            return new SortingAlgorithmResult<>(l, 0);
        
        int countCompare = 0;
        E appoggio = null;
        int attuale, precedente;

        for(int i = 1; i < l.size(); i++){
            attuale = i;
            precedente = i-1;
            while (precedente > -1 && l.get(attuale).compareTo(l.get(precedente)) < 0) {
                countCompare++;

                appoggio = l.get(attuale);
                l.set(attuale, l.get(precedente));
                l.set(precedente, appoggio);

                attuale--;
                precedente--;
            }

            if(precedente > -1)
                countCompare++;
        }

        return new SortingAlgorithmResult<>(l, countCompare);
    }

    public String getName() {
        return "InsertionSort";
    }
}
