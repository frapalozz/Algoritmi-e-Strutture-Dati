package it.unicam.cs.asdl2425.es9;

import java.util.List;

// TODO completare import

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
        // TODO implementare
        if(l == null)
            throw new NullPointerException("Tentativo di ordinare una lista null");

        if(l.size() < 2)
            // per ordinare la lista vuota o con un solo elemento non faccio niente
            return new SortingAlgorithmResult<>(l, 0);
        
        int countCompare = 0;
        E appoggio = null;
        int a, b;

        for(int i = 1; i < l.size(); i++){
            a = i;
            b = i-1;
            while (b > -1 && l.get(a).compareTo(l.get(b)) < 0) {
                countCompare++;

                appoggio = l.get(a);
                l.set(a, l.get(b));
                l.set(b, appoggio);

                a--;
                b--;
            }

            if(b > -1)
                countCompare++;
        }

        return new SortingAlgorithmResult<>(l, countCompare);
    }

    public String getName() {
        return "InsertionSort";
    }
}
