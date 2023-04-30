// This program takes a DFA from a text file and minimizes it to an
// optimal DFA. The optimal DFA gets written to a new text file.
// By Kaden Hargrove
// IT 328 Section 1
// Programming Assignment 2 + 3
// Student B
// 4/14/23

import java.util.*;
import java.io.*;

public class minimizeDFA {
    //construct matrix
    //determine indistinguishables
    //combine indistinguishable states
    //reconstruct DFA
    //you win
    public static void main(String[] args) throws FileNotFoundException {
        //check proper arguments were provided
        if (args.length != 1)
        {
            System.out.println("Usage: java minimizeDFA file.DFA");
            System.exit(0);
        }
        
        String inputFile = args[0];
        File DFA = new File(inputFile);

        //parse original dfa file and print
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct = Parse.ParseStudentB(DFA);
        System.out.println("DFA from " + inputFile + ":\n");
        Parse.printDFA(returnStruct);
        System.out.println("\nParsing results of strings attached in " + inputFile + ":");
        Parse.testDFAStrings(returnStruct);

        //find min dfa and print
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> minDFA = findMinDFA(returnStruct);
        System.out.println("\n\nMinimized DFA from " + inputFile + ":\n");
        Parse.printDFA(minDFA);
        System.out.println("\nParsing results of strings attached in " + inputFile + ":\n");
        Parse.testDFAStrings(minDFA);

        System.out.println("\n|Q| " + returnStruct.getNumStates() + " -> " + minDFA.getNumStates());
    }

    public static ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> findMinDFA(ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> returnStruct)
    {
        //save stuff from return structure
        int numStates = returnStruct.getNumStates();
        List<String> acceptingStates = Arrays.asList(returnStruct.getAcceptingStates());
        List<Character> letters = Arrays.asList(returnStruct.getAlphabetStrings());
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> transitions = returnStruct.getPairedHashTable();

        //create matrix to find min DFA
        boolean [][] matrix = new boolean[numStates][numStates];

        //intialize matrix to true for all pairs of states where one is accepting 
        //and other is rejecting (must be distinguishable)
        //true == distinguishable
        //false == indistinguishable
        for (int i = 0; i < numStates; i++)
        {
            for (int j = 0; j < numStates; j++)
            {
                //indistinguishable from self
                if (i != j)
                {
                    boolean isIaccepting = acceptingStates.contains(Integer.toString(i));
                    boolean isJaccepting = acceptingStates.contains(Integer.toString(j));
                    if (isIaccepting != isJaccepting) {
                        matrix[i][j] = true;
                    }
                }
            }
        }

        //if there is any unmarked pairs check each node in the pair for each input alphabet
        //if a pair leads to a marked pair, then mark the pair you checked
        //if not, leave the pair unmarked
        //repeat this for all unmarked pairs until no markings can be made
        int nodesMarked = 1;
        while (nodesMarked > 0)
        {
            nodesMarked = 0;
            for (int i = 0; i < matrix.length; i++)
            {
                for (int j = 0; j < i; j++)
                {
                    if (!matrix[i][j])
                    {
                        //inside unmarked pairs
                        //for all letters, compare transitions of each state at each letter
                        for (char letter : letters)
                        {
                            Pair<Character, Character> iPair = new Pair<Character, Character>((Character) Integer.toString(i).charAt(0), letter);
                            Pair<Character, Character> jPair = new Pair<Character, Character>((Character) Integer.toString(j).charAt(0), letter);
                            
                            if (transitions.containsKey(iPair) && transitions.containsKey(jPair))
                            {
                                //if matrix is true at transition states of pair at letter
                                //then original pair of states is distinguisable
                                int tempI = transitions.get(iPair).get(0);
                                int tempJ = transitions.get(jPair).get(0);
                                if (matrix[tempI][tempJ])
                                {
                                    matrix[i][j] = true;
                                    nodesMarked++; //increment for the while loop
                                    break; //exit for each loop because nodes are distinguishable
                                }
                            }
                        }
                    }
                }
            }
        }
        
        //now combine any pairs that are indistinguishable into same state (create new return struct DFA)
        // Step 1: Create a mapping of old states to new states
        Map<Integer, Integer> stateMap = new HashMap<>();
        for (int i = 0; i < numStates; i++) {
            stateMap.put(i, i);
        }

        // Step 2: Iterate over the matrix and merge indistinguishable states
        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < i; j++) {
                if (!matrix[i][j]) {
                    // Combine states i and j
                    int newState = Math.min(stateMap.get(i), stateMap.get(j));
                    stateMap.put(i, newState);
                    stateMap.put(j, newState);
                }
            }
        }

        // Step 3: Create a new set of states for the minimized DFA
        Set<Integer> newStates = new HashSet<>(stateMap.values());

        // Step 4: Create a new set of accepting states for the minimized DFA
        Set<Integer> newAcceptingStates = new HashSet<>();
        for (String acceptingState : acceptingStates) 
        {
            int state = Integer.parseInt(acceptingState);
            //if old accepting state is still a state in the new dfa, add it to new accepting states
            if (newStates.contains(stateMap.get(state))) {
                newAcceptingStates.add(stateMap.get(state));
            }
        }

        // Step 5: Create a new set of transitions for the minimized DFA
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> newTransitions = new Hashtable<>();
        List<Pair<Character, Character>> sortedKeys = new ArrayList<>(transitions.keySet());
        Collections.sort(sortedKeys, Comparator.<Pair<Character, Character>, Character>comparing(Pair::getState).thenComparing(Pair::getAlphabet)); //sort keys by state and then alphabet
        
        int count = 0;

        for (Pair<Character, Character> pair : sortedKeys) 
        {
            ArrayList<Integer> temp = new ArrayList<>();
            int oldState = pair.getState() - '0';
            int newState = transitions.get(pair).get(0);

            //if old state still exists in new states (is distinguishable), add new (destination) state to array list 
            if (newStates.contains(stateMap.get(oldState))) 
            {
                temp.add(stateMap.get(newState));
                newTransitions.put(new Pair<>(pair.getState(), pair.getAlphabet()), temp);
                count++;

                if (count % letters.size() == 0)
                {
                    int removeState = stateMap.get(oldState);
                    newStates.remove(removeState);
                }
            }
        }

        //convert accept states integer set to string array
        String[] newAcceptStates = new String[newAcceptingStates.size()];
        int index = 0;
        for (Integer element : newAcceptingStates) 
        {
            newAcceptStates[index] = String.valueOf(element);
            index++;
        }

        // Step 6: Create a new ReturnStructure object for the minimized DFA
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> minimizedDFA = new ReturnStructure<>(newTransitions, returnStruct.getStartState(), newAcceptStates, returnStruct.getAlphabetStrings(), returnStruct.getInputStrings());
        return minimizedDFA;
    }

    //debug print matrix (lower half of triangle)
    public static void printMatrix(boolean[][] matrix) 
    {
        for (int i = 0; i < matrix.length; i++) 
        {
            for (int j = 0; j <= i; j++) 
            {
                if (matrix[i][j])
                    System.out.print("1");
                else
                    System.out.print("0");
                    
                System.out.print(" ");
            }
            System.out.println();
        }
    }
    
}
