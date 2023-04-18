import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class parse {

    public static void main(String[] args) throws FileNotFoundException {
        File NFA = new File("x.nfa");
        ParseStudentA(NFA);
    }
    // ParseStudentA should read the file and store the transition table in a hash table
    public static void ParseStudentA(File NFA) throws FileNotFoundException {
        Scanner scan = new Scanner(NFA);
        
        Hashtable<Character, Integer> transitions = new Hashtable<Character, Integer>();
        Hashtable<Integer, Hashtable<Character, Integer>> transitionTable = new Hashtable<Integer, Hashtable<Character, Integer>>();

        String num_of_states = scan.nextLine();
        String[] stateArray = num_of_states.split(" ");
        int states = Integer.parseInt(stateArray[stateArray.length - 1]);

        String letters = scan.nextLine();
        String[] secondLine = letters.split(" ");
        String[] alphabet = Arrays.copyOfRange(secondLine, 1, secondLine.length);
        Character [] alphabetArray = new Character[alphabet.length+1];
        for(int i = 0; i < alphabet.length; i++) {
            alphabetArray[i] = alphabet[i].charAt(0);
        }
        alphabetArray[alphabetArray.length - 1] =  'L';
        scan.nextLine();

        for(Integer i = 0; i < states; i++) {
            String line = scan.nextLine();
            String[] lineArray = line.split(" ");
            char key = lineArray[0].charAt(0);
            for(int j = 1; j < lineArray.length; j++) {
                String value = lineArray[j];
                String[] values = value.split("");
                for(int k = 1; k < values.length-1; k++) {
                    String check = values[k];
                    if (Character.isDigit(check.charAt(0)) == true){
                        transitions.put(alphabetArray[j-1], Integer.parseInt(check));
                    }
                }
            }
            System.out.println(transitions.get('a'));
            transitionTable.put(i, transitions);
        }
        scan.nextLine();
        String startState = scan.nextLine();
        String finalStates = scan.nextLine();
        scan. nextLine();
        scan.close();
    }
}
