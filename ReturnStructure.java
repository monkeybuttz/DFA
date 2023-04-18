import java.util.*;

public class ReturnStructure<PairedHashTable, startState, acceptingStates, inputStrings> {

    private Hashtable<Pair<Character, Character>, ArrayList<Integer>> PairedHashTable;
    private String startState;
    private String[] acceptingStates;
    private String[] inputStrings;

    public ReturnStructure(Hashtable<Pair<Character, Character>, ArrayList<Integer>> PairedHashTable, String startState, String[] acceptingStates, String[] inputStrings) {
        this.PairedHashTable = PairedHashTable;
        this.startState = startState;
        this.acceptingStates = acceptingStates;
        this.inputStrings = inputStrings;
    }

    public Hashtable<Pair<Character, Character>, ArrayList<Integer>> getPairedHashTable() {
        return PairedHashTable;
    }

    public String getStartState() {
        return startState;
    }

    public String[] getAcceptingStates() {
        return acceptingStates;
    }

    public String[] getInputStrings() {
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

    public void setInputStrings(String[] inputStrings) {
        this.inputStrings = inputStrings;
    }
}
