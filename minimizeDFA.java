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

        //get num states
        int numStates = returnStruct.getNumStates();

        //get accepting states
        List<String> acceptingStates = Arrays.asList(returnStruct.getAcceptingStates());
        System.out.println(acceptingStates.toString());

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
