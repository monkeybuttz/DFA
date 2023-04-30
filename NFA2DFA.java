import java.util.*;
import java.io.*;

public class NFA2DFA {
    public static void main(String[] args) throws FileNotFoundException {
        File NFA = new File("x.nfa");
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct = Parse.ParseStudentA(NFA);
        convertNFA2DFA(returnStruct);
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

        //create the lambda closure of the start state
        ArrayList<Integer> lambdaClosure = findLambdaClosure(NFAtransitionTable, 0);
        System.out.println(lambdaClosure);
    


    }

    //union function for the dfa transition table
    public static ArrayList<Integer> union(ArrayList<Integer> a, ArrayList<Integer> b) {
        ArrayList<Integer> union = new ArrayList<Integer>();
        for (int i = 0; i < a.size(); i++) {
            union.add(a.get(i));
        }
        for (int i = 0; i < b.size(); i++) {
            if (!union.contains(b.get(i))) {
                union.add(b.get(i));
            }
        }
        return union;
    }


    // find the start state of the dfa given then nfa start state given the lambda closure of the nfa transition table
    public static String findStartState(String NFAstartState, ArrayList<Integer> lambdaClosure) {
        String startState = NFAstartState;
        for (int i = 0; i < lambdaClosure.size(); i++) {
            startState += lambdaClosure.get(i);
        }
        return startState;
    }


    //find the lambda closure of the state given its nfa transition table
    public static ArrayList<Integer> findLambdaClosure(Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable, Integer state) {
        ArrayList<Integer> lambdaClosure = new ArrayList<Integer>();
        lambdaClosure.add(state);
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for(int i = 0; i < NFAtransitionTable.size(); i++) {
            Pair<Character, Character> key = NFAtransitionTable.keys().nextElement();
            Integer check = Character.getNumericValue(key.getFirst());
            if (check == state && key.getSecond() == 'L') {
                temp = NFAtransitionTable.get(key);
                for (int j = 0; j < temp.size(); j++) {
                    if (!lambdaClosure.contains(temp.get(j))) {
                        lambdaClosure.add(temp.get(j));
                    }
                }
            }
        }
        return lambdaClosure;
    }



}