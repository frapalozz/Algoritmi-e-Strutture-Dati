package it.unicam.cs.asdl2425.es10;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class MatrixMultiplicationSolverTest {

    @Test
    final void testMatrixMultiplicationSolver() {
        assertThrows(NullPointerException.class,
                () -> new MatrixMultiplicationSolver(null));
        ArrayList<Integer> p = new ArrayList<Integer>();
        assertThrows(IllegalArgumentException.class,
                () -> new MatrixMultiplicationSolver(p));
        p.add(30);
        assertThrows(IllegalArgumentException.class,
                () -> new MatrixMultiplicationSolver(p));
        p.add(20);
        MatrixMultiplicationSolver s = new MatrixMultiplicationSolver(p);
        assertEquals(0, s.getOptimalCost());
        assertEquals("A_{0}", s.getOptimalParenthesization());
        p.add(50);
        MatrixMultiplicationSolver s1 = new MatrixMultiplicationSolver(p);
        assertEquals(30 * 20 * 50, s1.getOptimalCost());
        assertEquals("(A_{0} x A_{1})", s1.getOptimalParenthesization());
    }

    @Test
    final void testGetOptimalSolution1() {
        ArrayList<Integer> p = new ArrayList<Integer>();
        p.add(30);
        p.add(35);
        p.add(15);
        p.add(5);
        p.add(10);
        p.add(20);
        p.add(25);
        MatrixMultiplicationSolver s = new MatrixMultiplicationSolver(p);
        assertEquals(15125, s.getOptimalCost());
        assertEquals("((A_{0} x (A_{1} x A_{2})) x ((A_{3} x A_{4}) x A_{5}))", s.getOptimalParenthesization());
    }

    @Test
    final void testGetOptimalSolution2() {
        ArrayList<Integer> p = new ArrayList<Integer>();
        p.add(15);
        p.add(200);
        p.add(10);
        p.add(1050);
        MatrixMultiplicationSolver s = new MatrixMultiplicationSolver(p);
        assertEquals(187500, s.getOptimalCost());
        assertEquals("((A_{0} x A_{1}) x A_{2})", s.getOptimalParenthesization());
    }

}
