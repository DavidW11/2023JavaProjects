import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;

public class WordNet {
    // stores the sysnsets associated with each integer index
    private LinearProbingHashST<Integer, String[]> synsets;
    // stores the integer indices (of the synset) where each noun string
    // is located
    private RedBlackBST<String, Queue<Integer>> locations;
    // shortest common ancestor object used to find shortest common ancestor
    // and the length away
    private ShortestCommonAncestor sca;


    // constructor takes the name of the two input files
    public WordNet(String synsetsName, String hypernymsName) {
        if (synsetsName == null || hypernymsName == null) {
            throw new IllegalArgumentException();
        }
        synsets = new LinearProbingHashST<Integer, String[]>();
        locations = new RedBlackBST<String, Queue<Integer>>();
        int numSynsets = readSynsets(synsetsName);
        Digraph wordNet = buildDigraph(hypernymsName, numSynsets);
        sca = new ShortestCommonAncestor(wordNet);
    }

    // reads the synset file and fills out the synsets and locations data
    // structures
    private int readSynsets(String file) {
        if (file == null) {
            throw new IllegalArgumentException();
        }
        In synsetFile = new In(file);
        String line;
        while (synsetFile.hasNextLine()) {
            line = synsetFile.readLine();
            int i = Integer.parseInt(line.split(",")[0]);
            String[] synset = line.split(",")[1].split(" ");
            synsets.put(i, synset);
            for (String s : synset) {
                if (!locations.contains(s)) {
                    locations.put(s, new Queue<Integer>());
                }
                locations.get(s).enqueue(i);
            }
        }
        return synsets.size();
    }

    // builds the digraph by adding adjecency bewteen sysnets and their hypernyms
    // which is eventually used as a parameter in the shortestcommonancestor
    // object
    private Digraph buildDigraph(String file, int numSynsets) {
        if (file == null || numSynsets <= 0) {
            throw new IllegalArgumentException();
        }
        Digraph temp = new Digraph(numSynsets);
        In hypernymFile = new In(file);
        String[] hypernymTemp;
        while (hypernymFile.hasNextLine()) {
            hypernymTemp = hypernymFile.readLine().split(",");
            int i = Integer.parseInt(hypernymTemp[0]);
            for (int j = 1; j < hypernymTemp.length; j++) {
                temp.addEdge(i, Integer.parseInt(hypernymTemp[j]));
            }
        }
        return temp;
    }

    // returns the set of all WordNet nouns
    public Iterable<String> nouns() {
        return locations.keys();
    }

    // returns whether or not the redblackBST contains the given noun
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return locations.contains(word);
    }


    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new IllegalArgumentException();
        }
        if (!(isNoun(noun1) && isNoun(noun2))) {
            throw new IllegalArgumentException();
        }
        // calls the shortest common ancestors method
        int commonAncestor = sca.ancestorSubset(locations.get(noun1),
                                                locations.get(noun2));
        String[] temp = synsets.get(commonAncestor);
        return String.join(" ", temp);

    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!(isNoun(noun1) && isNoun(noun2))) {
            throw new IllegalArgumentException();
        }
        if (noun1 == null || noun2 == null) {
            throw new IllegalArgumentException();
        }
        // calls the shortest common ancestors method
        return sca.lengthSubset(locations.get(noun1), locations.get(noun2));
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet net = new WordNet("synsets.txt", "hypernyms.txt");

        // print nouns
        int i = 0;
        for (String s : net.nouns()) {
            System.out.println(s);
            i++;
            if (i % 100 == 0) {
                break;
            }
        }

        System.out.println("Is djifsdjlkfsdjkjlfsd a noun? [False]: " +
                                   net.isNoun("djifsdjlkfsdjkjlfsd"));
        System.out.println("Is pizza a noun? [True]: " +
                                   net.isNoun("pizza"));
        System.out.println("Is Italy a noun? [True]: " +
                                   net.isNoun("Italy"));

        System.out.println("Number of Distinct Nouns: " + net.locations.size());
        System.out.println("Shortest common ancestors of pizza and Italy: " +
                                   net.sca("pizza", "Italy"));
        System.out.println("Distance between pizza and Italy: " +
                                   net.distance("pizza", "Italy"));
    }
}