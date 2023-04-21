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
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], ArrayList<String>>  returnStruct = Parse.ParseStudentB(DFA);
        returnStruct.printStructure();
    }
}
