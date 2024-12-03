package it.unicam.cs.asdl2425.mp1;

/**
 * Rappresenta un nodo di un albero di Merkle.
 * 
 * @author Luca Tesei, Marco Caputo, Francesco Palozzi francesco.palozzi@studenti.unicam.it
 */
public class MerkleNode {
    private final String hash; // Hash associato al nodo.

    private final MerkleNode left; // Figlio sinistro del nodo.

    private final MerkleNode right; // Figlio destro del nodo.

    /**
     * Costruisce un nodo Merkle foglia con un valore di hash, quindi,
     * corrispondente all'hash di un dato.
     *
     * @param hash
     *                 l'hash associato al nodo.
     */
    public MerkleNode(String hash) {
        this(hash, null, null);
    }

    /**
     * Costruisce un nodo Merkle con un valore di hash e due figli, quindi,
     * corrispondente all'hash di un branch.
     *
     * @param hash
     *                  l'hash associato al nodo.
     * @param left
     *                  il figlio sinistro.
     * @param right
     *                  il figlio destro.
     */
    public MerkleNode(String hash, MerkleNode left, MerkleNode right) {
        this.hash = hash;
        this.left = left;
        this.right = right;
    }

    /**
     * Restituisce l'hash associato al nodo.
     *
     * @return l'hash associato al nodo.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Restituisce il figlio sinistro del nodo.
     *
     * @return il figlio sinistro del nodo.
     */
    public MerkleNode getLeft() {
        return left;
    }

    /**
     * Restituisce il figlio destro del nodo.
     *
     * @return il figlio destro del nodo.
     */
    public MerkleNode getRight() {
        return right;
    }

    /**
     * Restituisce true se il nodo è una foglia, false altrimenti.
     *
     * @return true se il nodo è una foglia, false altrimenti.
     */
    public boolean isLeaf() {
        // TODO implementare
        // Se il nodo è una foglia allora non deve avere figli
        return this.left == null && this.right == null;
    }

    @Override
    public String toString() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        // TODO implementare

        /* due nodi sono uguali se hanno lostesso hash */

        // Se l'oggetto passato è un MerkleNode, allora controlla se il suo hash è uguale a this.hash
        // Altrimenti ritorna false
        return (obj instanceof MerkleNode)? this.hash.equals( ((MerkleNode) obj).getHash() ) : false;
    }

    @Override
    public int hashCode() {
        // TODO implementare

        /* implementare in accordo a equals */
        final int prime = 31;
        int result = 1;
        long temp = this.hash.hashCode();
        
        return prime * result + (int) (temp ^ (temp >>> 32));
    }
}