import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private BST<String, Integer> bst;
    private Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null input file");
        }

        In inSyn = new In(synsets);
        In inHyp = new In(hypernyms);

        // Use BST (k,v) = (noun, vertex id) for O(log n) search
        bst = new BST<String, Integer>();
        String[] fields;
        int count = 0;
        while (inSyn.hasNextLine()) {
            String line = inSyn.readLine();
            fields = line.split(",");
            bst.put(fields[1], Integer.parseInt(fields[0]));
            count++;
        }
        // Build Direct Graph from vertices
        G = new Digraph(count);
        while (inHyp.hasNextLine()) {
            String line = inHyp.readLine();
            fields = line.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                G.addEdge(v, w);
            }
        }

        // CHECK HERE IF G IS DAG with only one root
        DirectedCycle dc = new DirectedCycle(G);
        if (dc.hasCycle()) throw new IllegalArgumentException("G has a cycle");
        int countRoot = 0;
        for (int i = 0; i < count; i++) {
            if (G.outdegree(i) == 0) countRoot++;
        }
        if (countRoot > 1) throw new IllegalArgumentException("More than one root");


    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return bst.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return (bst.get(word) != null);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("nounA or nounB is not in WordNet");
        }
	int va = bst.get(nounA);
	int vb = bst.get(nounB);

	// light-weigth bfs
	aMarked = new boolean[G.V()];
	aDistTo = new int[G.V()];
	aEdgeTo = new int[G.V()];
	
	bMarked = new boolean[G.V()];
	bDistTo = new int[G.V()];
	bEdgeTo = new int[G.V()];
	
	for (int v = 0; v < G.V(); v++){
		aDistTo[v] = INFINITY;
		bDistTo[v] = INFINITY;
	}
	
	// alternating bfs
	
	Queue<Integer> aq = new Queue<Integer>();
	Queue<Integer> bq = new Queue<Integer>();
	aMarked[va] = true;
	bMarked[vb] = true;
	aDistTo[va] = 0;
	bDistTo[vb] = 0;

	aq.enqueue(va);
	bq.enqueue(vb);

	while (!aq.isEmpty() && !bq.isEmpty()){
		// Next border of A
		int anext = aq.dequeue();
		for (int w: G.adj(anext)){
			if (!aMarked[w]){
				aEdgeTo[w] = anext;
				aDistTo[w] = aDistTo[anext]+1;
				aMarked[w] = true;
				aq.enqueue(w);
			}
			if (bMarked[w]){ //found common ancestor
				break; // has to change, it want multiple ancester
			}
		}
		// Next border of B
		int bnext = bq.dequeue();
		for (int w: G.adj(bnext)){
			if (!bMarked[w]){
				bEdgeTo[w] = bnext;
				bDistTo[w] = bDistTo[bnext]+1;
				bMarked[w] = true;
				bq.enqueue(w);
			}
			if (aMarked[w]){ //found common ancestor
				break; // has to change, it want multiple ancester
			}
		}	
	}	

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return "test";
    }

    // do unit testing of this class
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets, hypernyms);

    }
}
