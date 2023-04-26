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
        File DFA = new File("X.dfa");
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct = Parse.ParseStudentB(DFA);
        returnStruct.printStructure();

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
        System.out.println();
        printMatrix(matrix);

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
                        System.out.println("Checking [" + i + "][" + j + "]");

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
                                    System.out.println("Marked [" + i + "][" + j + "]\n");
                                    matrix[i][j] = true;
                                    nodesMarked++; //increment for the while loop
                                    break; //exit for each loop because nodes are distinguishable
                                }
                            }
                        }
                    }
                }
            }
            printMatrix(matrix);
        }

        //now combines any pairs that are indistinguishable into same state

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
