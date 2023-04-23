import java.util.*;

public class ReturnStructure<PairedHashTable, startState, acceptingStates, alphabetArray, inputStrings> {

    private Hashtable<Pair<Character, Character>, ArrayList<Integer>> PairedHashTable;
    private String startState;
    private String[] acceptingStates;
    private Character[] alphabetStrings;
    private ArrayList<String> inputStrings;

    public ReturnStructure(Hashtable<Pair<Character, Character>, ArrayList<Integer>> PairedHashTable, String startState, String[] acceptingStates, Character[] alphabetStrings, ArrayList<String> inputStrings) {
        this.PairedHashTable = PairedHashTable;
        this.startState = startState;
        this.acceptingStates = acceptingStates;
        this.alphabetStrings = alphabetStrings;
        this.inputStrings = inputStrings;
    }

    public Hashtable<Pair<Character, Character>, ArrayList<Integer>> getPairedHashTable() {
        return PairedHashTable;
    }

    //prints hash table of pairs and list of transition nodes, useful for debugging
    public void printHashTable(Hashtable<Pair<Character, Character>, ArrayList<Integer>> pairHashTbl)
    {
        List<Pair<Character, Character>> sortedKeys = new ArrayList<>(pairHashTbl.keySet());
        Collections.sort(sortedKeys, Comparator.<Pair<Character, Character>, Character>comparing(Pair::getState).thenComparing(Pair::getAlphabet));

        for (Pair<Character, Character> currentKey : sortedKeys) 
        {
            System.out.println("State: " + currentKey.getState() + " Alphabet: " + currentKey.getAlphabet());
            System.out.println("Transition nodes: " + pairHashTbl.get(currentKey) + "\n");
        }
    }

    //returns number of states in a complicated way
    public int getNumStates() {
        List<Pair<Character, Character>> keys = new ArrayList<>(getPairedHashTable().keySet());
        int numLetters = 0;

        for (Pair<Character, Character> key : keys) 
        {
            if (key.getState() == '0')
            {
                numLetters++;
            }
        }
        return getPairedHashTable().size() / numLetters;
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

    public void setPairedHashTable(Hashtable<Pair<Character, Character>, ArrayList<Integer>> PairedHashTable) {
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
        //System.out.println(getInputStrings().toString());
    }
}
