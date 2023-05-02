import java.util.*;

public class ReturnStructure<PairedHashTable, startState, acceptingStates, alphabetArray, inputStrings> {

    private Hashtable<Pair<Integer, Character>, ArrayList<Integer>> PairedHashTable;
    private String startState;
    private String[] acceptingStates;
    private Character[] alphabetStrings;
    private ArrayList<String> inputStrings;

    public ReturnStructure(Hashtable<Pair<Integer, Character>, ArrayList<Integer>> PairedHashTable, String startState, String[] acceptingStates, Character[] alphabetStrings, ArrayList<String> inputStrings) {
        this.PairedHashTable = PairedHashTable;
        this.startState = startState;
        this.acceptingStates = acceptingStates;
        this.alphabetStrings = alphabetStrings;
        this.inputStrings = inputStrings;
    }

    public Hashtable<Pair<Integer, Character>, ArrayList<Integer>> getPairedHashTable() {
        return PairedHashTable;
    }

    //prints hash table of pairs and list of transition nodes, useful for debugging
    public void printHashTable(Hashtable<Pair<Integer, Character>, ArrayList<Integer>> pairHashTbl)
    {
        //sort keys by state and then alphabet
        List<Pair<Integer, Character>> sortedKeys = new ArrayList<>(pairHashTbl.keySet());
        Collections.sort(sortedKeys, Comparator.comparing((Pair<Integer, Character> pair) -> pair.getState()).thenComparing(pair -> pair.getAlphabet()));

        for (Pair<Integer, Character> currentKey : sortedKeys) 
        {
            System.out.println("State: " + currentKey.getState() + " Alphabet: " + currentKey.getAlphabet());
            System.out.println("Transition nodes: " + pairHashTbl.get(currentKey) + "\n");
        }
    }

    //returns number of states in a complicated way
    public int getNumStates() {
        List<Pair<Integer, Character>> keys = new ArrayList<>(getPairedHashTable().keySet());
        int numLetters = 0;
        int tableSize = getPairedHashTable().size();

        for (Pair<Integer, Character> key : keys) 
        {
            if (key.getState() == 0)
            {
                numLetters++;
            }
        }
        return tableSize / numLetters;
    }

    public String getStartState() {
        return startState;
    }

    public String[] getAcceptingStates() {
        return acceptingStates;
    }

    public Character[] getAlphabetStrings() {
        return alphabetStrings;
    }

    public ArrayList<String> getInputStrings() {
        return inputStrings;
    }

    public void setPairedHashTable(Hashtable<Pair<Integer, Character>, ArrayList<Integer>> PairedHashTable) {
        this.PairedHashTable = PairedHashTable;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public void setAcceptingStates(String[] acceptingStates) {
        this.acceptingStates = acceptingStates;
    }

    public void setInputStrings(ArrayList<String> inputStrings) {
        this.inputStrings = inputStrings;
    }

    public void setAlphabetStrings(Character[] alphabetStrings) {
        this.alphabetStrings = alphabetStrings;
    }

    //prints entire return structure (except input strings) for debugging
    public void printStructure()
    {
        System.out.println("Initial state: " + getStartState() + "\nAccepting states: " + Arrays.toString(getAcceptingStates()) + "\n");
        printHashTable(getPairedHashTable());
        System.out.println("Num states: " + getNumStates());
    }
}
