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
        ArrayList<String> NFAinputStrings = returnStruct.getInputStrings();

        //create the dfa transition table
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> DFAtransitionTable = new Hashtable<Pair<Character, Character>, ArrayList<Integer>>();
        ArrayList<Integer> DFAstartState = new ArrayList<Integer>();
        ArrayList<Integer> DFAfinalStates = new ArrayList<Integer>();

        //create the dfa start state
        ArrayList<Integer> lambdaTransList = new ArrayList<Integer>();
        findLambdaClosure(NFAtransitionTable, Integer.parseInt(NFAstartState), lambdaTransList);
        Collections.sort(lambdaTransList);
        DFAstartState = lambdaTransList;
        System.out.println("DFA start state: " + DFAstartState);

        //set dfa alphabet equal to nfa alphabet besides the lambda transition
        ArrayList<Character> DFAalphabet = new ArrayList<Character>();
        for (int i = 0; i < NFAalphabet.length; i++) {
            if (NFAalphabet[i] != 'L') {
                DFAalphabet.add(NFAalphabet[i]);
            }
        }
        System.out.println("DFA alphabet: " + DFAalphabet);

        //find the dfa start state and all of its transitions and print them out
        ArrayList<Integer> DFAstartStateTransitions = new ArrayList<Integer>();
        for (int i = 0; i < DFAalphabet.size(); i++) {
            DFAstartStateTransitions = findDFAtransition(NFAtransitionTable, DFAstartState.get(0), DFAalphabet.get(i));
            Collections.sort(DFAstartStateTransitions);
            System.out.println("DFA start state transition with " + DFAalphabet.get(i) + ": " + DFAstartStateTransitions);
        }


    }

    //find the union of two state arraylists
    //include the lambda closure of the states
    //make sure the union does not contain duplicates
    //return the union of the two arraylists
    public static ArrayList<Integer> union(ArrayList<Integer> state1, ArrayList<Integer> state2, Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable) {
        ArrayList<Integer> union = new ArrayList<Integer>();
        ArrayList<Integer> lambdaTransList = new ArrayList<Integer>();
        for (int i = 0; i < state1.size(); i++) {
            union.add(state1.get(i));
            findLambdaClosure(NFAtransitionTable, state1.get(i), lambdaTransList);
            for (int j = 0; j < lambdaTransList.size(); j++) {
                if (!union.contains(lambdaTransList.get(j))) {
                    union.add(lambdaTransList.get(j));
                }
            }
        }
        for (int i = 0; i < state2.size(); i++) {
            union.add(state2.get(i));
            findLambdaClosure(NFAtransitionTable, state2.get(i), lambdaTransList);
            for (int j = 0; j < lambdaTransList.size(); j++) {
                if (!union.contains(lambdaTransList.get(j))) {
                    union.add(lambdaTransList.get(j));
                }
            }
        }
        return union;
    }



     // recursive function to find the lambda closure of a state
     // follows the lambda transitions and adds the states to the lambda closure arraylist
     public static void findLambdaClosure(Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable, int state, ArrayList<Integer> lambdaTransList) {

        //sort keys by state and then alphabet
        List<Pair<Character, Character>> sortedKeys = new ArrayList<>(NFAtransitionTable.keySet());
        Collections.sort(sortedKeys, Comparator.<Pair<Character, Character>, Character>comparing(Pair::getState).thenComparing(Pair::getAlphabet));
        for (Pair<Character, Character> currentKey : sortedKeys) 
        {
            if(currentKey.getAlphabet() == 'L'){ 
                if(Character.getNumericValue(currentKey.getState()) == state){
                    for(Integer transitionNodes : NFAtransitionTable.get(currentKey)){
                        if(!lambdaTransList.contains(transitionNodes)){
                            lambdaTransList.add(transitionNodes);
                            findLambdaClosure(NFAtransitionTable, transitionNodes, lambdaTransList);
                        }
                    }
                }

                }
            }
     }

    //finds the transition of a state given an alphabet and lamba closure
    //return the list od states that the state can transition with the given alphabet
    public static ArrayList<Integer> findDFAtransition(Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable, int state, Character DFAalphabet){
        ArrayList<Integer> DFAtransition = new ArrayList<Integer>();
        List<Pair<Character, Character>> sortedKeys = new ArrayList<>(NFAtransitionTable.keySet());
        Collections.sort(sortedKeys, Comparator.<Pair<Character, Character>, Character>comparing(Pair::getState).thenComparing(Pair::getAlphabet));
        for (Pair<Character, Character> currentKey : sortedKeys) 
        {
            if(currentKey.getAlphabet() == DFAalphabet){ 
                if(Character.getNumericValue(currentKey.getState()) == state){
                    for(Integer transitionNodes : NFAtransitionTable.get(currentKey)){
                        if(!DFAtransition.contains(transitionNodes)){
                            DFAtransition.add(transitionNodes);
                        }
                    }
                }

                }
            }
        return DFAtransition;
    }

}

