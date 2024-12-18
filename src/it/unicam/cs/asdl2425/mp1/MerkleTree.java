package it.unicam.cs.asdl2425.mp1;

import java.util.*;

/**
 * Un Merkle Tree, noto anche come hash tree binario, è una struttura dati per
 * verificare in modo efficiente l'integrità e l'autenticità dei dati
 * all'interno di un set di dati più ampio. Viene costruito eseguendo l'hashing
 * ricorsivo di coppie di dati (valori hash crittografici) fino a ottenere un
 * singolo hash root. In questa implementazione la verifica di dati avviene
 * utilizzando hash MD5.
 * 
 * @author Luca Tesei, Marco Caputo (template), Francesco Palozzi francesco.palozzi@studenti.unicam.it (implementazione)
 *
 * @param <T>
 *                il tipo di dati su cui l'albero è costruito.
 */
public class MerkleTree<T> {
    /**
     * Nodo radice dell'albero.
     */
    private final MerkleNode root;

    /**
     * Larghezza dell'albero, ovvero il numero di nodi nell'ultimo livello.
     */
    private final int width;

    /**
     * Costruisce un albero di Merkle a partire da un oggetto HashLinkedList,
     * utilizzando direttamente gli hash presenti nella lista per costruire le
     * foglie. Si noti che gli hash dei nodi intermedi dovrebbero essere
     * ottenuti da quelli inferiori concatenando hash adiacenti due a due e
     * applicando direttamente la funzione di hash MD5 al risultato della
     * concatenazione in bytes.
     *
     * @param hashList
     *                     un oggetto HashLinkedList contenente i dati e i
     *                     relativi hash.
     * @throws IllegalArgumentException
     *                                      se la lista è null o vuota.
     */
    public MerkleTree(HashLinkedList<T> hashList) {
        
        if(hashList == null)
            throw new IllegalArgumentException("hashList passato al costruttore è null!");
        if(hashList.getSize() == 0)
            throw new IllegalArgumentException("hashList passato al costruttore è vuota!");

        // Lista contenete i nodi di un certo livello
        List<MerkleNode> nodesLayer = new LinkedList<>();

        // Generazione ultimo livello, nodi foglia
        for (String hash : hashList.getAllHashes()) 
            nodesLayer.add( new MerkleNode(hash) );

        MerkleNode parentNode; // Nodo combinato dei due figli
        String parentHash; // Hash combinato dei due figli del nodo
        int nodesCurrentLevel = nodesLayer.size(); // Numero di nodi nel livello corrente

        while (nodesCurrentLevel > 1) {
            // Ancora non si è arrivati al root

            while (nodesCurrentLevel > 0) {
                // Iterazione per la creazione dei nodi del livello superiore

                if(nodesCurrentLevel > 1){
                    // Abbiamo due nodi per combinare gli hash
                    // Creazione hash combinato 
                    parentHash = HashUtil.computeMD5((nodesLayer.get(0).getHash() + nodesLayer.get(1).getHash()).getBytes());

                    // Creazione nodo padre e rimozione dei suoi nodi figli
                    parentNode = new MerkleNode(parentHash, nodesLayer.remove(0), nodesLayer.remove(0));

                    // Aggiunta nodo padre
                    nodesLayer.add(parentNode);

                    // Togli due nodi al livello corrente
                    nodesCurrentLevel-=2;
                }
                else{
                    // in questo caso l'hash combinato è il nuovo hash calcolato dall'hash del seguente nodo
                    parentHash = HashUtil.computeMD5( nodesLayer.get(0).getHash().getBytes() );
                    
                    // Creazione nodo padre e rimozione figlio
                    parentNode = new MerkleNode(parentHash, nodesLayer.remove(0), null);
                    
                    // Aggiunta nodo padre
                    nodesLayer.add(parentNode);

                    // Togli un nodo al livello corrente
                    nodesCurrentLevel--;
                }
            }

            // Quantità di nodi al livello corrente
            nodesCurrentLevel = nodesLayer.size();
        }

        this.root = nodesLayer.get(0);
        this.width = hashList.getSize();
    }

    /**
     * Restituisce il nodo radice dell'albero.
     *
     * @return il nodo radice.
     */
    public MerkleNode getRoot() {
        return root;
    }

    /**
     * Restituisce la larghezza dell'albero.
     *
     * @return la larghezza dell'albero.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Restituisce l'altezza dell'albero.
     *
     * @return l'altezza dell'albero.
     */
    public int getHeight() {

        // Altezza = log_2(nodi foglia)
        return (int) Math.ceil(Math.log(this.width) / Math.log(2));
    }

    /**
     * Restituisce l'indice di un dato elemento secondo l'albero di Merkle
     * descritto da un dato branch. Gli indici forniti partono da 0 e
     * corrispondono all'ordine degli hash corrispondenti agli elementi
     * nell'ultimo livello dell'albero da sinistra a destra. Nel caso in cui il
     * branch fornito corrisponda alla radice di un sottoalbero, l'indice
     * fornito rappresenta un indice relativo a quel sottoalbero, ovvero un
     * offset rispetto all'indice del primo elemento del blocco di dati che
     * rappresenta. Se l'hash dell'elemento non è presente come dato
     * dell'albero, viene restituito -1.
     *
     * @param branch
     *                   la radice dell'albero di Merkle.
     * @param data
     *                   l'elemento da cercare.
     * @return l'indice del dato nell'albero; -1 se l'hash del dato non è
     *         presente.
     * @throws IllegalArgumentException
     *                                      se il branch o il dato sono null o
     *                                      se il branch non è parte
     *                                      dell'albero.
     */
    public int getIndexOfData(MerkleNode branch, T data) {
        
        if(branch == null)
            throw new IllegalArgumentException("branch passato a getIndexOfData() è null!");
        if(!(validateBranch(branch)))
            throw new IllegalArgumentException("branch passato a getIndexOfData() non è parte dell'albero!");
        if(data == null)
            throw new IllegalArgumentException("data passato a getIndexOfData() è null!");

        // trova indice di data dal branch
        return getIndex(branch, HashUtil.dataToHash(data), 0);
    }

    /**
     * Restituisce l'indice di un elemento secondo questo albero di Merkle. Gli
     * indici forniti partono da 0 e corrispondono all'ordine degli hash
     * corrispondenti agli elementi nell'ultimo livello dell'albero da sinistra
     * a destra (e quindi l'ordine degli elementi forniti alla costruzione). Se
     * l'hash dell'elemento non è presente come dato dell'albero, viene
     * restituito -1.
     *
     * @param data
     *                 l'elemento da cercare.
     * @return l'indice del dato nell'albero; -1 se il dato non è presente.
     * @throws IllegalArgumentException
     *                                      se il dato è null.
     */
    public int getIndexOfData(T data) {
        
        if(data == null)
            throw new IllegalArgumentException("data passato a getIndexOfData() è null!");
        
        // Trova indice di data dal nodo root
        return getIndexOfData(this.root, data);
    }

    /**
     * Trova l'indice di un dato elemento.
     * Ricerca ricorsivamente tra tutte le foglie, se la foglia controllata non corrisponde al dato
     * allora viene incrementato l'indice. Se la foglia corrisponde al dato, allora viene salvato l'indice
     * 
     * @param node
     *              Nodo in cui si sta cercando, root dell'albero in cui cercare
     * @param dataHash
     *              Hash da cercare nei nodi foglia
     * @param nodesOnLeft
     *              Nodi alla sinistra del nodo node nel livello attuale
     * 
     * @return l'indice del dato nell'albero; -1 se il dato non è presente.
     */
    private int getIndex(MerkleNode node, String dataHash, int nodesOnLeft){
        // Casi base
        if(node == null)
            return -1;
        if(node.isLeaf() && node.getHash().equals(dataHash)) 
            // Nodo trovato
            return nodesOnLeft;
        

        // Caso ricorsivo
        // Cerca indice di dataHash nel sotto albero sinistro
        int index = getIndex(node.getLeft(), dataHash, nodesOnLeft*2);
        if(index > -1)
            // L'hash cercato si trova nel sotto albero sinistro, quindi ritorna il suo indice
            return index;

        // Cerca indice di dataHash nel sotto albero destro
        return getIndex(node.getRight(), dataHash, nodesOnLeft*2+1);
    }

    /**
     * Sottopone a validazione un elemento fornito per verificare se appartiene
     * all'albero di Merkle, controllando se il suo hash è parte dell'albero
     * come hash di un nodo foglia.
     *
     * @param data
     *                 l'elemento da validare
     * @return true se l'hash dell'elemento è parte dell'albero; false
     *         altrimenti.
     */
    public boolean validateData(T data) {
        
        // Se data è presente nell'albero, allora il suo indice è più grande di 1
        return getIndexOfData(data) > -1;
    }

    /**
     * Sottopone a validazione un dato sottoalbero di Merkle, corrispondente
     * quindi a un blocco di dati, per verificare se è valido rispetto a questo
     * albero e ai suoi hash. Un sottoalbero è valido se l'hash della sua radice
     * è uguale all'hash di un qualsiasi nodo intermedio di questo albero. Si
     * noti che il sottoalbero fornito può corrispondere a una foglia.
     *
     * @param branch
     *                   la radice del sottoalbero di Merkle da validare.
     * @return true se il sottoalbero di Merkle è valido; false altrimenti.
     */
    public boolean validateBranch(MerkleNode branch) {
        
        // Creazione lista contenente i nodi in cui cercare partendo dal root
        List<MerkleNode> list = new LinkedList<>();
        list.add(this.root);


        while(!list.isEmpty()) {
            // Finchè la lista list ha qualche nodo allora bisogna controllare se un hash corrisponde a
            // quello del branch

            if(list.get(0).equals(branch))
                // Branch trovato 
                return true;

            
            if(!list.get(0).isLeaf()) {
                // Aggiunta figli del nodo corrente alla lista, e rimozione del nodo
                if(list.get(0).getLeft() != null && list.get(0).getRight() != null){
                    // Figlio destro e sinistro
                    list.add( list.get(0).getLeft() );
                    list.add( list.remove(0).getRight() );
                }

                else
                    // Solo figlio sinistro
                    list.add( list.remove(0).getLeft() );
            }
            else
                // Rimozione foglia
                list.remove(0);
        }

        // Branch non trovato
        return false;
    }

    /**
     * Sottopone a validazione un dato albero di Merkle per verificare se è
     * valido rispetto a questo albero e ai suoi hash. Grazie alle proprietà
     * degli alberi di Merkle, ciò può essere fatto in tempo costante.
     *
     * @param otherTree
     *                      il nodo radice dell'altro albero di Merkle da
     *                      validare.
     * @return true se l'altro albero di Merkle è valido; false altrimenti.
     * @throws IllegalArgumentException
     *                                      se l'albero fornito è null.
     */
    public boolean validateTree(MerkleTree<T> otherTree) {
        
        if(otherTree == null)
            throw new IllegalArgumentException("otherTree passato a validateTree è null!");

        // Se questo albero e quello passato hanno lo stesso hash allora
        // significa che l'albero passato è valido
        return this.root.equals(otherTree.getRoot());
    }

    /**
     * Trova gli indici degli elementi di dati non validi (cioè con un hash
     * diverso) in un dato Merkle Tree, secondo questo Merkle Tree. Grazie alle
     * proprietà degli alberi di Merkle, ciò può essere fatto confrontando gli
     * hash dei nodi interni corrispondenti nei due alberi. Ad esempio, nel caso
     * di un singolo dato non valido, verrebbe percorso un unico cammino di
     * lunghezza pari all'altezza dell'albero. Gli indici forniti partono da 0 e
     * corrispondono all'ordine degli elementi nell'ultimo livello dell'albero
     * da sinistra a destra (e quindi l'ordine degli elementi forniti alla
     * costruzione). Se l'albero fornito ha una struttura diversa, possibilmente
     * a causa di una quantità diversa di elementi con cui è stato costruito e,
     * quindi, non rappresenta gli stessi dati, viene lanciata un'eccezione.
     *
     * @param otherTree
     *                      l'altro Merkle Tree.
     * @throws IllegalArgumentException
     *                                      se l'altro albero è null o ha una
     *                                      struttura diversa.
     * @return l'insieme di indici degli elementi di dati non validi.
     */
    public Set<Integer> findInvalidDataIndices(MerkleTree<T> otherTree) {
        
        if(otherTree == null)
            throw new IllegalArgumentException("otherTree passato a findInvalidDataIndices è null!");
        if(this.width != otherTree.getWidth())
            throw new IllegalArgumentException("La struttura di otherTree non corrisponde alla struttura di questo albero");

        // Set di indici non validi
        Set<Integer> invalidDataSet = new HashSet<>();
        // Trova tutti gli indici non validi ricorsivamente
        findInvalidDataIndicesRecursive(this.root, otherTree.getRoot(), 0, invalidDataSet);

        return invalidDataSet;
    }

    /**
     * Trova ricorsivamente gli indici degli elementi di dati non validi in un
     * dato Merkle Tree, secondo questo Merkle Tree.
     *
     * @param node
     *                           il nodo corrente da validare.
     * @param otherNode
     *                           il nodo corrispondente nell'altro albero da
     *                           validare.
     * @param nodesOnLeft
     *                           il numero di nodi a sinistra del nodo corrente
     *                           nel suo livello.
     * @param invalidIndices
     *                           l'insieme di indici degli elementi di dati non
     *                           validi.
     */
    private void findInvalidDataIndicesRecursive(MerkleNode node, MerkleNode otherNode, int nodesOnLeft, Set<Integer> invalidIndices) {

        if(node.isLeaf()) {
            // Nodo foglia quindi controllo se non valido
            if(!node.equals(otherNode))
                // Aggiunta indice a set
                invalidIndices.add(nodesOnLeft);

            return;
        }

        // Lato sinistro diverso
        if(!node.getLeft().equals(otherNode.getLeft())) 
            // Raddoppio dei nodi foglia a sinistra
            findInvalidDataIndicesRecursive(node.getLeft(), otherNode.getLeft(), nodesOnLeft*2, invalidIndices);

        // Lato destro diverso
        if(node.getRight() != null){
            if(!node.getRight().equals(otherNode.getRight()))
                // Raddoppio nodi foglia a sinistra + 1
                findInvalidDataIndicesRecursive(node.getRight(), otherNode.getRight(), nodesOnLeft*2 + 1, invalidIndices);
        }
        
    }

    /**
     * Restituisce la prova di Merkle per un dato elemento, ovvero la lista di
     * hash dei nodi fratelli di ciascun nodo nel cammino dalla radice a una
     * foglia contenente il dato. La prova di Merkle dovrebbe fornire una lista
     * di oggetti MerkleProofHash tale per cui, combinando l'hash del dato con
     * l'hash del primo oggetto MerkleProofHash in un nuovo hash, il risultato
     * con il successivo e così via fino all'ultimo oggetto, si possa ottenere
     * l'hash del nodo padre dell'albero. Nel caso in cui, in determinati
     * step della prova non ci siano due hash distinti da combinare, l'hash viene
     * comunque ricalcolato sulla base dell'unico hash disponibile.
     *
     * @param data
     *                 l'elemento per cui generare la prova di Merkle.
     * @return la prova di Merkle per il dato.
     * @throws IllegalArgumentException
     *                                      se il dato è null o non è parte
     *                                      dell'albero.
     */
    public MerkleProof getMerkleProof(T data) {
        
        if(data == null)
            throw new IllegalArgumentException("data passato a getMerkleProof è null!");
        if(!this.validateData(data))
            throw new IllegalArgumentException("data passato a getMerkleProof non è parte dell'albero!");

        // Lista dei nodi. [Foglia -> ... -> Root]
        List<MerkleNode> path = new ArrayList<>();
        // Generazione path
        getPathToRoot(this.root, HashUtil.dataToHash(data), path);

        // Generazione MerkleProof
        return merkleProofGenerator(path, this.getHeight());
    }

    /**
     * Restituisce la prova di Merkle per un dato branch, ovvero la lista di
     * hash dei nodi fratelli di ciascun nodo nel cammino dalla radice al dato
     * nodo branch, rappresentativo di un blocco di dati. La prova di Merkle
     * dovrebbe fornire una lista di oggetti MerkleProofHash tale per cui,
     * combinando l'hash del branch con l'hash del primo oggetto MerkleProofHash
     * in un nuovo hash, il risultato con il successivo e così via fino
     * all'ultimo oggetto, si possa ottenere l'hash del nodo padre dell'albero.
     * Nel caso in cui in determinati step della prova non ci siano due
     * hash distinti da combinare, l'hash deve comunque essere ricalcolato sulla base
     * dell'unico hash disponibile.
     *
     * @param branch
     *                   il branch per cui generare la prova di Merkle.
     * @return la prova di Merkle per il branch.
     * @throws IllegalArgumentException
     *                                      se il branch è null o non è parte
     *                                      dell'albero.
     */
    public MerkleProof getMerkleProof(MerkleNode branch) {
        
        if(branch == null)
            throw new IllegalArgumentException("branch passato a getMerkleProof null!");
        if(!this.validateBranch(branch))
            throw new IllegalArgumentException("branch passato a getMerkleProof non è parte dell'albero!");
        
        // Lista dei nodi. [Branch -> ... -> Root]
        List<MerkleNode> path = new ArrayList<>();
        // Generazione path
        getPathToRoot(this.root, branch.getHash(), path);
        
        // Generazione MerkleProof
        return merkleProofGenerator(path, path.size()-1);
    }

    /**
     * Genera la prova di Merkle per un dato nodo
     * 
     * @param path
     *              Lista della path dal nodo per cui bisogna fare la MerkleProof
     *              fino al root
     * @param size
     *              Dimensione della MerkleProof
     * 
     * @return MerkleProof per il nodo iniziale della lista path
     */
    private MerkleProof merkleProofGenerator(List<MerkleNode> path, int size) {

        MerkleProof merkleProof = new MerkleProof(this.root.getHash(), size);

        // Nodo figlio e nodo padre
        MerkleNode child = null, parent = null;

        for (MerkleNode merkleNode : path) {
            if(parent == null){
                // Nodo foglia per cui creare il proof
                parent = merkleNode;
                continue;
            }

            child = parent;      // Children
            parent = merkleNode; // Parent

            if(parent.getLeft() == child)
                // Il figlio sinistro del parent corrisponde al nodo precedente visitato
                // Quindi il fratello si trova a destra, se il fratello è null allora il suo hash è la stringa vuota
                merkleProof.addHash( (parent.getRight() != null)? parent.getRight().getHash() : "", false);
            else
                // Il figlio destro del parent corrisponde al nodo precedente visitato
                // Quindi il fratello si trova a sinistra
                merkleProof.addHash( parent.getLeft().getHash(), true);
        }
        
        return merkleProof;
    }

    /**
     * Metodo ricorsivo che restituisce il cammino da un dato nodo con il dato hash al
     * dato nodo. Se l'hash fornito non è presente
     * nell'albero come hash di un discendente, viene restituita una lista vuota.
     * 
     * @param currentNode
     *                  Il nodo corrente da cui iniziare la ricerca
     * @param dataHash
     *                  L'hash del dato da cercare
     * @param path
     *                  La lista dal nodo che contiene dataHash fino al root
     */
    public void getPathToRoot(MerkleNode currentNode, String dataHash, List<MerkleNode> path) {

        // Caso base 
        if(currentNode.getHash().equals(dataHash)){
            // hash nodo foglia uguale a hash da cercare
            path.add(currentNode);
            return;
        }
        else if(currentNode.isLeaf())
            return;
            

        // Caso ricorsivo
        // Aggiungi la strada sinistra
        getPathToRoot(currentNode.getLeft(), dataHash, path);
        if(!path.isEmpty()){
            // La lista non è più vuota, quindi la path è a sinistra
            path.add(currentNode);
            return;
        }
            

        if(currentNode.getRight() != null)
            // Aggiungi la strada destra
            getPathToRoot(currentNode.getRight(), dataHash, path);
            
        if(!path.isEmpty())
            // La lista non è più vuota, quindi la path è a destra
            path.add(currentNode);
    }
}