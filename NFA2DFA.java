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

        //find the dfa start state and all of its transitions and print them out
        //add the start state and its transitions to the dfa transition table
        ArrayList<Integer> DFAstartStateTransitions = new ArrayList<Integer>();
        for (int i = 0; i < DFAalphabet.size(); i++) {
            DFAstartStateTransitions = findDFAtransition(NFAtransitionTable, DFAstartState.get(0), DFAalphabet.get(i));
            Collections.sort(DFAstartStateTransitions);
            DFAtransitionTable.put(new Pair<Character, Character>(Character.forDigit(DFAstartState.get(0), 10), DFAalphabet.get(i)), DFAstartStateTransitions);
        }

        //grab each of the transitions from the start state and make each list a new state
        //if the state is not in the dfa transition table, add it

        //create a list of states that have been visited
        ArrayList<ArrayList<Integer>> visitedStates = new ArrayList<ArrayList<Integer>>();
        visitedStates.add(DFAstartState);

        //create a list of states that have not been visited
        ArrayList<ArrayList<Integer>> unvisitedStates = new ArrayList<ArrayList<Integer>>();
        unvisitedStates.add(DFAstartState);

        //create a list of states that have been added to the dfa transition table
        ArrayList<ArrayList<Integer>> addedStates = new ArrayList<ArrayList<Integer>>();
        addedStates.add(DFAstartState);

        //create a list of states that have not been added to the dfa transition table
        ArrayList<ArrayList<Integer>> notAddedStates = new ArrayList<ArrayList<Integer>>();
        
        //while there are still unvisited states
        while (unvisitedStates.size() > 0) {
            //grab the first unvisited state
            ArrayList<Integer> currentState = unvisitedStates.get(0);
            //remove the state from the unvisited states
            unvisitedStates.remove(0);
            //add the state to the visited states
            visitedStates.add(currentState);
            //for each alphabet in the dfa alphabet
            for (int i = 0; i < DFAalphabet.size(); i++) {
                //find the transition of the current state with the current alphabet
                ArrayList<Integer> currentTransition = findDFAtransition(NFAtransitionTable, currentState.get(0), DFAalphabet.get(i));
                //sort the transition
                Collections.sort(currentTransition);
                //if the transition is not empty
                if (currentTransition.size() > 0) {
                    //if the transition is not in the visited states
                    if (!visitedStates.contains(currentTransition)) {
                        //add the transition to the unvisited states
                        unvisitedStates.add(currentTransition);
                        //add the transition to the added states
                        addedStates.add(currentTransition);
                        //add the transition to the dfa transition table
                        DFAtransitionTable.put(new Pair<Character, Character>(Character.forDigit(currentState.get(0), 10), DFAalphabet.get(i)), currentTransition);
                    }
                    //if the transition is in the visited states
                    else {
                        //add the transition to the dfa transition table
                        DFAtransitionTable.put(new Pair<Character, Character>(Character.forDigit(currentState.get(0), 10), DFAalphabet.get(i)), currentTransition);
                    }
                }
            }
        }

        //print out the dfa transition table in the correct format
        System.out.println("DFA transition table: " );
        for (int i = 0; i < DFAalphabet.size(); i++) {
            System.out.print("\t" + DFAalphabet.get(i));
        }
        System.out.println();
        for (int i = 0; i < addedStates.size(); i++) {
            System.out.print(addedStates.get(i) + "\t");
            for (int j = 0; j < DFAalphabet.size(); j++) {
                System.out.print(DFAtransitionTable.get(new Pair<Character, Character>(Character.forDigit(addedStates.get(i).get(0), 10), DFAalphabet.get(j))) + "\t");
            }
            System.out.println();
        }

        //find the dfa final states
        ArrayList<ArrayList<Integer>> DFAfinalStates = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < addedStates.size(); i++) {
            for (int j = 0; j < NFAfinalStates.length; j++) {
                if (addedStates.get(i).contains(Integer.parseInt(NFAfinalStates[j]))) {
                    DFAfinalStates.add(addedStates.get(i));
                }
            }
        }

        //print out the dfa final states
        System.out.println("DFA final states: " + DFAfinalStates);

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

