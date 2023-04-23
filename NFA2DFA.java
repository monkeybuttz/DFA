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
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> transitionTable = returnStruct.getPairedHashTable();
        String startState = returnStruct.getStartState();
        String[] finalStates = returnStruct.getAcceptingStates();
        ArrayList<String> inputStrings = returnStruct.getInputStrings();
        
        //algorithm to convert NFA transition table to DFA transition table




    }
}
