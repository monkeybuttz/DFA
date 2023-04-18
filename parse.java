import java.io.*;
import java.util.*;

class Pair<state, alphabet>{
    private state state;
    private alphabet alphabet;

    public Pair(state state, alphabet alphabet) {
        this.state = state;
        this.alphabet = alphabet;
    }

    public state getState() {
        return state;
    }

    public alphabet getAlphabet() {
        return alphabet;
    }

    public void setState(state state) {
        this.state = state;
    }

    public void setAlphabet(alphabet alphabet) {
        this.alphabet = alphabet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(state, pair.state) &&
                Objects.equals(alphabet, pair.alphabet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, alphabet);
    }
}

public class parse {

    public static void main(String[] args) throws FileNotFoundException {
        File NFA = new File("x.nfa");
        ParseStudentA(NFA);
    }
    // ParseStudentA should read the file and store the transition table in a hash table
    public static void ParseStudentA(File NFA) throws FileNotFoundException {
        Scanner scan = new Scanner(NFA);

        // main data structure
        // key: Pair<current_state, alphabet>
        // value: ArrayList<transition_states>
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> transitionTable = new Hashtable<Pair<Character, Character>, ArrayList<Integer>>();

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
            Character key = lineArray[0].charAt(0);
            for(int j = 1; j < lineArray.length; j++) {
                String value = lineArray[j];
                String[] values = value.split("");
                ArrayList<Integer> nodes = new ArrayList<Integer>();
                Pair<Character, Character> transitions = new Pair<Character, Character>(key, alphabetArray[j-1]);
                for(int k = 1; k < values.length-1; k++) {
                    String check = values[k];
                    if (Character.isDigit(check.charAt(0)) == true){
                        nodes.add(Integer.parseInt(check));
                    }
                }
                transitionTable.put(transitions, nodes);
            }
        }
        System.out.println(transitionTable.get(new Pair<Character, Character>('0', 'L')));

        // scan.nextLine();
        // String startState = scan.nextLine();
        // String finalStates = scan.nextLine();
        // scan. nextLine();
        scan.close();
    }
}
