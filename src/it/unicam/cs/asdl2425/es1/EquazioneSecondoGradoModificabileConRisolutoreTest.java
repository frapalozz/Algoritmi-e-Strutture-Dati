/**
 * 
 */
package it.unicam.cs.asdl2425.es1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * @author Template: Luca Tesei, Implementation: Collettiva da Esercitazione a
 *         Casa
 *
 */
class EquazioneSecondoGradoModificabileConRisolutoreTest {
    /*
     * Costante piccola per il confronto di due numeri double
     */
    static final double EPSILON = 1.0E-15;

    @Test
    final void testEquazioneSecondoGradoModificabileConRisolutore() {
        // controllo che il valore 0 su a lanci l'eccezione
        assertThrows(IllegalArgumentException.class,
                () -> new EquazioneSecondoGradoModificabileConRisolutore(0, 1,
                        1));
        // devo controllare che comunque nel caso normale il costruttore
        // funziona
        EquazioneSecondoGradoModificabileConRisolutore eq = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 1);
        // Controllo che all'inizio l'equazione non sia risolta
        assertFalse(eq.isSolved());
    }

    @Test
    final void testGetA() {
        double x = 10;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(
                x, 1, 1);
        // controllo che il valore restituito sia quello che ho messo
        // all'interno
        // dell'oggetto
        assertTrue(x == e1.getA());
        // in generale si dovrebbe usare assertTrue(Math.abs(x -
        // e1.getA())<EPSILON) ma in
        // questo caso il valore che testiamo non ha subito manipolazioni quindi
        // la sua rappresentazione sarà la stessa di quella inserita nel
        // costruttore senza errori di approssimazione

    }

    @Test
    final void testSetA() {
        // TODO implementare
        double x = 5;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        e1.setA(x);

        /*
         * Controllo che il nuovo valore di a è quello che ho messo all'interno del setter,
         * e controllo che l'equazione non è ancora risolta
         */
        assertTrue(Math.abs(e1.getA() - x)<EPSILON);
        assertFalse(e1.isSolved());

        /*
         * Controllo che viene lanciata l'eccezione se imposto a = 0
         */
        assertThrows(IllegalArgumentException.class, () -> e1.setA(0));
    }

    @Test
    final void testGetB() {
        // TODO implementare
        double x = 5;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, x, 1);
        /* controllo che il valore restituito sia quello che ho messo all'interno
         * dell'oggetto
         */
        assertTrue(x == e1.getB());
    }

    @Test
    final void testSetB() {
        // TODO implementare
        double x = 5;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        e1.setB(x);

        /*
         * Controllo che il nuovo valore di a è quello che ho messo all'interno del setter,
         * e controllo che l'equazione non è ancora risolta
         */
        assertTrue(e1.getB() == x);
        assertFalse(e1.isSolved());
    }

    @Test
    final void testGetC() {
        // TODO implementare
        double x = 5;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, x);
        /* controllo che il valore restituito sia quello che ho messo all'interno
         * dell'oggetto
         */
        assertTrue(x == e1.getC());
    }

    @Test
    final void testSetC() {
        // TODO implementare
        double x = 5;
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        e1.setC(x);

        /*
         * Controllo che il nuovo valore di a è quello che ho messo all'interno del setter,
         * e controllo che l'equazione non è ancora risolta
         */
        assertTrue(e1.getC() == x);
        assertFalse(e1.isSolved());
    }

    @Test
    final void testIsSolved() {
        // TODO implementare
        EquazioneSecondoGradoModificabileConRisolutore e1 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        // Controllo che l'equazione appena creata non è risolta
        assertFalse(e1.isSolved());

        e1.solve();
        // Controllo che l'equazione è stata risolta dopo aver chiamato il metodo solve();
        assertTrue(e1.isSolved());

        // Cambio c e controllo che l'equazione non è risolta
        e1.setC(5);
        assertFalse(e1.isSolved());
    }

    @Test
    final void testSolve() {
        EquazioneSecondoGradoModificabileConRisolutore e3 = new EquazioneSecondoGradoModificabileConRisolutore(
                1, 1, 3);
        // controllo semplicemente che la chiamata a solve() non generi errori
        e3.solve();
        // i test con i valori delle soluzioni vanno fatti nel test del metodo
        // getSolution()
    }

    @Test
    final void testGetSolution() {
        // TODO implementare
        EquazioneSecondoGradoModificabileConRisolutore e4 = new EquazioneSecondoGradoModificabileConRisolutore(1, 1, 1);
        assertThrows(IllegalStateException.class, () -> e4.getSolution());

        e4.solve();
        e4.getSolution();

        double delta = e4.getB() * e4.getB() - 4 * e4.getA() * e4.getC();
        if(Math.abs(delta) < EPSILON) assertTrue(e4.getSolution().isOneSolution());
        else if(delta < 0) assertTrue(e4.getSolution().isEmptySolution());
        else assertTrue(!e4.getSolution().isEmptySolution() && !e4.getSolution().isOneSolution());
    }

}
