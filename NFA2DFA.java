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

        //create the dfa transition table
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> DFAtransitionTable = new Hashtable<Pair<Character, Character>, ArrayList<Integer>>();
        ArrayList<Integer> DFAstartState = new ArrayList<Integer>();

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

        //create the dfa states list
        ArrayList<ArrayList<Integer>> DFAstates = new ArrayList<ArrayList<Integer>>();
        DFAstates.add(DFAstartState);

        //find the transitions for the dfa start state
        //find the lambda closure of each transition
        //get the union of the lambda closure and the transition
        //print out the transitions for the dfa start state
        for (int i = 0; i < DFAalphabet.size(); i++) {
            ArrayList<Integer> DFAtransition = new ArrayList<Integer>();
            for (int j = 0; j < DFAstartState.size(); j++) {
                ArrayList<Integer> state1 = findDFAtransition(NFAtransitionTable, DFAstartState.get(j), DFAalphabet.get(i));
                for (int k = 0; k < state1.size(); k++) {
                    ArrayList<Integer> lambdaTransList2 = new ArrayList<Integer>();
                    findLambdaClosure(NFAtransitionTable, state1.get(k), lambdaTransList2);
                    Collections.sort(lambdaTransList2);
                    state1 = union(state1, lambdaTransList2, NFAtransitionTable);
                }
                DFAtransition = union(DFAtransition, state1, NFAtransitionTable);
            }
            Collections.sort(DFAtransition);
            System.out.println("DFA transition for " + DFAstartState + " with alphabet " + DFAalphabet.get(i) + ": " + DFAtransition);
            DFAtransitionTable.put(new Pair<Character, Character>(Character.forDigit(DFAstates.indexOf(DFAstartState), 10), DFAalphabet.get(i)), DFAtransition);
            if (!DFAstates.contains(DFAtransition)) {
                DFAstates.add(DFAtransition);
            }
        }

        
    }

    //combine 2 arraylists into 1
    // sort the list and dont contain any duplicates
    public static ArrayList<Integer> union(ArrayList<Integer> state1, ArrayList<Integer> state2, Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable) {
        ArrayList<Integer> union = new ArrayList<Integer>();
        for (int i = 0; i < state1.size(); i++) {
            if (!union.contains(state1.get(i))) {
                union.add(state1.get(i));
            }
        }
        for (int i = 0; i < state2.size(); i++) {
            if (!union.contains(state2.get(i))) {
                union.add(state2.get(i));
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

