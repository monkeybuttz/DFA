import java.util.*;
import java.io.*;

public class NFA2DFA {
    public static void main(String[] args) throws FileNotFoundException {
        File NFA = new File("x.nfa");
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct = Parse.ParseStudentA(NFA);
        convertNFA2DFA(returnStruct);
        returnStruct.printStructure();
    }

    // takes the NFA in the return structure and converts it to a DFA and writes it to a file
    public static void convertNFA2DFA(ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct) {
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable = returnStruct.getPairedHashTable();
        String NFAstartState = returnStruct.getStartState();
        String[] NFAfinalStates = returnStruct.getAcceptingStates();
        Character[] NFAalphabet = returnStruct.getAlphabetStrings();
        Integer NFAstates = returnStruct.getNumStates();
        System.out.println(NFAstates);
        ArrayList<String> NFAinputStrings = returnStruct.getInputStrings();
        
        //algorithm to convert NFA transition table to DFA transition table

    


    }
}
