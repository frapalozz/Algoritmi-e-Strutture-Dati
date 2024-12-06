package it.unicam.cs.asdl2425.mp1;

import java.util.*;

// TODO inserire solo gli import della Java SE che si ritengono necessari

/**
 * Un Merkle Tree, noto anche come hash tree binario, è una struttura dati per
 * verificare in modo efficiente l'integrità e l'autenticità dei dati
 * all'interno di un set di dati più ampio. Viene costruito eseguendo l'hashing
 * ricorsivo di coppie di dati (valori hash crittografici) fino a ottenere un
 * singolo hash root. In questa implementazione la verifica di dati avviene
 * utilizzando hash MD5.
 * 
 * @author Luca Tesei, Marco Caputo, Francesco Palozzi francesco.palozzi@studenti.unicam.it
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
        // TODO implementare
        if(hashList == null)
            throw new IllegalArgumentException("hashList null!");
        if(hashList.getSize() == 0)
            throw new IllegalArgumentException("hashList vuota!");

        // Lista contenete i nodi partendo dall'ultimo livello
        ArrayList<MerkleNode> arrayMerkle = new ArrayList<>();

        // Creazione nodi foglia
        for (String hash : hashList.getAllHashes()) {
            arrayMerkle.add(new MerkleNode(hash));
        }

        ArrayList<MerkleNode> momentaryArray = new ArrayList<>();

        while (arrayMerkle.size() > 1) {
            for(int i = 0; i < arrayMerkle.size(); i+=2){
                
                if(arrayMerkle.size() > i+1){
                    // Abbiamo due nodi per combinare gli hash

                    // Creazione hash combinato 
                    String hashCombinato = HashUtil.computeMD5((arrayMerkle.get(i).getHash() + arrayMerkle.get(i+1).getHash()).getBytes());

                    // Creazione nodo combinato
                    MerkleNode combNode = new MerkleNode(hashCombinato, arrayMerkle.get(i), arrayMerkle.get(i+1));

                    // Aggiunta nodo combinato all'array momentaneo
                    momentaryArray.add(combNode);
                }
                else 
                    // Il seguente nodo non ha un nodo fratello con cui essere combinato
                    momentaryArray.add(arrayMerkle.get(i));
            }
            // Sostituisco la lista con i nuovi nodi combinati
            arrayMerkle = new ArrayList<>(momentaryArray);
            momentaryArray.clear();
        }

        this.root = arrayMerkle.get(0);
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
        // TODO implementare

        // Altezza = log_2(width)
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
        // TODO implementare
        if(branch == null)
            throw new IllegalArgumentException("branch null!");
        if(!(validateBranch(branch)))
            throw new IllegalArgumentException("branch non è parte dell'albero!");
        if(data == null)
            throw new IllegalArgumentException("data null!");

        // Hash di data
        String dataHash = HashUtil.dataToHash(data);
        
        int[] i = {-1, 0};
        // trova indice di data
        getIndex(branch, dataHash, i);

        return i[0];
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
        // TODO implementare
        if(data == null)
            throw new IllegalArgumentException("data null!");
        
        // Ottieni indice di data partendo da root
        return getIndexOfData(this.root, data);
    }

    /*
     * getIndex() serve a trovare l'indice di un dato elemento
     * Ricerca ricorsivamente tra tutte le foglie, se la foglia controllata non corrisponde al dato
     * allora viene incrementato l'indice. Se la foglia corrisponde al dato, allora viene impostato l'indice
     */
    private void getIndex(MerkleNode node, String data, int[] i){

        if(i[0] == i[1])
            // è stato trovato l'indice
            return;

        if(node.isLeaf()){
            // Caso base
            // Il seguente nodo è una foglia

            if(data.equals(node.getHash())) {
                // L'hash corrisponde a quello di data, quindi salvo il suo indice
                i[0] = i[1];
            }
            else {
                // La seguente foglia non ha lo stesso hash di data, quindi incremento indice
                i[1]++;
            }
                
            return;
        }

        // Caso ricorsivo
        // Non è ancora stato trovato la foglia con hash data
        // Cerco nei nodi a sinistra
        getIndex(node.getLeft(), data, i);
        // Cerco nei nodi a destra
        getIndex(node.getRight(), data, i);
        
        return;
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
        // TODO implementare
        
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
        // TODO implementare
        LinkedList<MerkleNode> list = new LinkedList<>();
        list.add(this.root);

        LinkedList<MerkleNode> intermediaryList = new LinkedList<>();
        // Finchè la lista ha qualche nodo allora bisogna vedere se qualche hash corrisponde a
        // quello del branch
        while(!list.isEmpty()) {
            for (MerkleNode node : list) {
                // Hash trovato
                if(node.getHash().equals(branch.getHash()))
                    return true;
                
                // Se non è nodo foglia allora nella successiva iterazione si andrà a 
                // controllare ai suoi nodi figli
                if(!node.isLeaf()) {
                    intermediaryList.add(node.getLeft());
                    intermediaryList.add(node.getRight());
                }
            }
            // Passo la nuova lista e svuoto la lista momentanea
            list = new LinkedList<>(intermediaryList);
            intermediaryList.clear();
        }
        
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
        // TODO implementare
        if(otherTree == null)
            throw new IllegalArgumentException("otherTree null!");

        // Se questo albero e quello passato hanno lo stesso hash allora
        // significa che l'albero passato è valido
        return this.root.getHash().equals(otherTree.root.getHash());
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
        // TODO implementare
        if(otherTree == null)
            throw new IllegalArgumentException("otherTree null!");
        if(this.width != otherTree.getWidth())
            throw new IllegalArgumentException("struttura di otherTree diversa diversa rispetto albero corrente!");

        Set<Integer> invalidDataSet = new HashSet<>();
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
    private void findInvalidDataIndicesRecursive(MerkleNode node,
            MerkleNode otherNode, int nodesOnLeft,
            Set<Integer> invalidIndices) {
        // TODO implementare

        // Nodo foglia quindi controllo se non valido
        if(node.isLeaf()) {
            if(!node.getHash().equals(otherNode.getHash()))
                // Aggiunta indice a set
                invalidIndices.add(nodesOnLeft);
            return;
        }

        // Lato sinistro diverso
        if(!node.getLeft().getHash().equals(otherNode.getLeft().getHash())) 
            findInvalidDataIndicesRecursive(node.getLeft(), otherNode.getLeft(), nodesOnLeft, invalidIndices);

        // Lato destro diverso
        if(!node.getRight().getHash().equals(otherNode.getRight().getHash())){
            int height = 0;
            MerkleNode flag = node.getLeft();

            while (flag.getLeft() != null) {
                height++;
                flag = flag.getLeft();
            }

            int nodesLeft = nodesOnLeft+(int)Math.pow(2, height);

            findInvalidDataIndicesRecursive(node.getRight(), otherNode.getRight(), nodesLeft, invalidIndices);
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
        // TODO implementare
        if(data == null)
            throw new IllegalArgumentException("data null!");
        if(!this.validateData(data))
            throw new IllegalArgumentException("data non è parte dell'albero!");

        List<MerkleNode> lista = getPathToDescendant(this.root, HashUtil.dataToHash(data));

        return merkleProofGenerator(lista, this.getHeight());
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
        // TODO implementare
        if(branch == null)
            throw new IllegalArgumentException("branch null!");
        if(!this.validateBranch(branch))
            throw new IllegalArgumentException("branch non è parte dell'albero!");
        
        List<MerkleNode> lista = getPathToDescendant(this.root, branch.getHash());
        
        return merkleProofGenerator(lista, lista.size()-1);
    }

    private MerkleProof merkleProofGenerator(List<MerkleNode> listNode, int size) {

        MerkleProof merkleProof = new MerkleProof(this.root.getHash(), size);

        ListIterator<MerkleNode> invertedIterator = listNode.listIterator(listNode.size());

        int i = size - listNode.size();
        while (i >= 0) {
            merkleProof.addHash("", false);
            i--;
        }

        MerkleNode previous = null;
        MerkleNode current = null;
        while (invertedIterator.hasPrevious()) {
            if(current == null){
                current = invertedIterator.previous();
                continue;
            }

            previous = current;
            current = invertedIterator.previous();
            

            if(current.getLeft() == previous)
                merkleProof.addHash(current.getRight().getHash(), false);
            else
                merkleProof.addHash(current.getLeft().getHash(), true);
        }
        
        return merkleProof;
    }

    /**
     * Metodo ricorsivo che restituisce il cammino da un dato nodo a un suo
     * discendente contenente un dato hash. Se l'hash fornito non è presente
     * nell'albero come hash di un discendente, viene restituito null.
     *
     * @param currentNode
     *                        il nodo corrente da cui iniziare la ricerca.
     * @param dataHash
     *                        l'hash del dato da cercare.
     * @return il cammino da un nodo a un discendente contenente il dato hash;
     *         null se l'hash non è presente.
     */
    public List<MerkleNode> getPathToDescendant(MerkleNode currentNode,
            String dataHash) {
        // TODO implementare
        List<MerkleNode> lista = new ArrayList<>();

        // Caso base 
        if(currentNode.getHash().equals(dataHash)){
            lista.add(0, currentNode);
            return lista;
        }

        if(currentNode.isLeaf())
            return lista;
            
        // Caso ricorsivo
        // Aggiungi la strada sinistra
        lista.addAll(0, getPathToDescendant(currentNode.getLeft(), dataHash));
        if(!lista.isEmpty()){
            lista.add(0, currentNode);
            return lista;
        }
            

        // Aggiungi la strada destra
        lista.addAll(0, getPathToDescendant(currentNode.getRight(), dataHash));
        if(!lista.isEmpty())
            lista.add(0, currentNode);

        return lista;
    }
    
    // TODO inserire eventuali metodi privati per fini di implementazione 
}