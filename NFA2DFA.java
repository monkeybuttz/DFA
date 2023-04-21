import java.util.*;
import java.io.*;

public class NFA2DFA {
    public static void main(String[] args) throws FileNotFoundException {
        File NFA = new File("x.nfa");
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], ArrayList<String>>  returnStruct = Parse.ParseStudentA(NFA);
        returnStruct.printStructure();
    }
}
