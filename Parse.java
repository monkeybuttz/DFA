import java.io.*;
import java.util.*;

public class Parse {
    // ParseStudentA should read the file and store the transition table in a hash table
    public static ReturnStructure<Hashtable<Pair<Integer, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> ParseStudentA(File NFA) throws FileNotFoundException {
        Scanner scan = new Scanner(NFA);

        // main data structure
        // key: Pair<current_state, alphabet>
        // value: ArrayList<transition_states>
        Hashtable<Pair<Integer, Character>, ArrayList<Integer>> transitionTable = new Hashtable<Pair<Integer, Character>, ArrayList<Integer>>();

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
            Integer key = Integer.parseInt(lineArray[0].replace(":", ""));
            for(int j = 1; j < lineArray.length; j++) {
                String value = lineArray[j];
                String[] values = value.split("");
                ArrayList<Integer> nodes = new ArrayList<Integer>();
                Pair<Integer, Character> transitions = new Pair<Integer, Character>(key, alphabetArray[j-1]);
                for(int k = 1; k < values.length-1; k++) {
                    String check = values[k];
                    if (Character.isDigit(check.charAt(0)) == true){
                        nodes.add(Integer.parseInt(check));
                    }
                }
                transitionTable.put(transitions, nodes);
            }
        }
        scan.nextLine();

        String startState = scan.nextLine();
        String[] startStateArray = startState.split(" ");
        startState = startStateArray[startStateArray.length - 1];

        String finalStates = scan.nextLine();
        String[] finalStatesArray = finalStates.split(" ");
        finalStates = finalStatesArray[finalStatesArray.length - 1];
        finalStatesArray = finalStates.split(",");

        scan.nextLine();
        scan.nextLine();
        //removed third scan.nextLine() because first string is empty string
        ArrayList<String> input = new ArrayList<String>();
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            input.add(line);
        }
        scan.close();

        ReturnStructure<Hashtable<Pair<Integer, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> returnStructure = new ReturnStructure<>(transitionTable, startState, finalStatesArray, alphabetArray, input);
        return returnStructure;
    }

    public static ReturnStructure<Hashtable<Pair<Integer, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> ParseStudentB(File DFA) throws FileNotFoundException {
        Scanner scan = new Scanner(DFA);

        // main data structure
        // key: Pair<current_state, alphabet>
        // value: ArrayList<transition_states>
        Hashtable<Pair<Integer, Character>, ArrayList<Integer>> transitionTable = new Hashtable<Pair<Integer, Character>, ArrayList<Integer>>();

        String num_of_states = scan.nextLine();
        String[] stateArray = num_of_states.split(" ");
        int states = Integer.parseInt(stateArray[stateArray.length - 1]);

        String letters = scan.nextLine();
        String[] secondLine = letters.split("['' ]+");
        String[] alphabet = Arrays.copyOfRange(secondLine, 1, secondLine.length);
        Character [] alphabetArray = new Character[alphabet.length];
        for(int i = 0; i < alphabet.length; i++) {
            alphabetArray[i] = alphabet[i].charAt(0);
        }
        scan.nextLine();

        for(Integer i = 0; i < states; i++) {
            String line = scan.nextLine();
            String[] lineArray = line.split("['': ]+");
            int key = Integer.parseInt(lineArray[1]);
            for(int j = 2; j < lineArray.length; j++) {
                String value = lineArray[j];
                //String[] values = value.split("");
                ArrayList<Integer> nodes = new ArrayList<Integer>();
                Pair<Integer, Character> transitions = new Pair<Integer, Character>(key, alphabetArray[j-2]);

                if (Character.isDigit(value.charAt(0))) // could  be a problem with new pair format
                {
                    nodes.add(Integer.parseInt(value));
                }

                transitionTable.put(transitions, nodes);
            }
        }
        scan.nextLine();

        String startState = scan.nextLine();
        String[] startStateArray = startState.split(" ");
        startState = startStateArray[startStateArray.length - 1];

        String finalStates = scan.nextLine();
        String[] finalStatesArray = finalStates.split(" ");
        finalStates = finalStatesArray[finalStatesArray.length - 1];
        finalStatesArray = finalStates.split(",");

        scan.nextLine();
        scan.nextLine();
        //removed third scan.nextLine() because first string is empty string
        ArrayList<String> input = new ArrayList<String>();
        while(scan.hasNextLine()) {
            String line = scan.nextLine();
            input.add(line);
        }
        scan.close();

        ReturnStructure<Hashtable<Pair<Integer, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> returnStructure = new ReturnStructure<>(transitionTable, startState, finalStatesArray, alphabetArray, input);
        return returnStructure;
    }

    //method to print a DFA
    public static void printDFA(ReturnStructure<Hashtable<Pair<Integer, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> dfa)
    {
        System.out.print("Sigma:");
        for(char letter : dfa.getAlphabetStrings())
        {
            System.out.print("     " + letter);
        }
        System.out.print("\n------------------------------");

        //sort keys by state and then alphabet
        List<Pair<Integer, Character>> sortedKeys = new ArrayList<>(dfa.getPairedHashTable().keySet());
        Collections.sort(sortedKeys, Comparator.comparing((Pair<Integer, Character> pair) -> pair.getState()).thenComparing(pair -> pair.getAlphabet()));

        int count = 0;
        for (Pair<Integer, Character> currentKey : sortedKeys)
        {
            //every time theres a new letter, print state
            if (count % dfa.getAlphabetStrings().length == 0)
            {
                System.out.print("\n    " + currentKey.getState() + ":");
            }
            System.out.print("     " + dfa.getPairedHashTable().get(currentKey).get(0));
            count++;
        }
        System.out.println("\n------------------------------");
        System.out.println(dfa.getStartState() + ": Initial State");

        String[] accept = dfa.getAcceptingStates();
        Arrays.sort(accept, Comparator.comparingInt(Integer::parseInt));

        for (int i = 0; i < accept.length; i++)
        {
            if (i < (accept.length - 1))
                System.out.print(accept[i] + ",");
            else
                System.out.print(accept[i] + ": Accepting State(s)\n");
        }
    }

    //method to test input strings on DFA and prints parsing results
    public static void testDFAStrings(ReturnStructure<Hashtable<Pair<Integer, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>> dfa)
    {
        ArrayList<String> testStrings = dfa.getInputStrings();
        List<String> acceptingStates = Arrays.asList(dfa.getAcceptingStates());
        Hashtable<Pair<Integer, Character>, ArrayList<Integer>> transitions = dfa.getPairedHashTable();

        int yes = 0;
        int no = 0;

        for(int i = 0; i < testStrings.size(); i++) //each string
        {
            String currState = dfa.getStartState();
            String currString = testStrings.get(i);
            boolean isAccepted = true;

            for (int j = 0; j < currString.length(); j++) //each char
            {
                char currchar = currString.charAt(j);
                Pair<Integer,Character> pair = new Pair<Integer,Character>(Integer.parseInt(currState), currchar);
                
                ArrayList<Integer> transitionStates = transitions.get(pair);
                if (transitionStates == null || transitionStates.isEmpty()) 
                {
                    isAccepted = false;
                    break;
                }

                currState = String.valueOf(transitionStates.get(0));
            }

            if (isAccepted && acceptingStates.contains(currState)) 
            {
                System.out.print("Yes  ");
                yes++;
            } 
            else 
            {
                System.out.print("No   ");
                no++;
            }

            if ((i + 1) % 15 == 0) 
            {
                System.out.println();
            }

        }
        System.out.println("\n\nYes: " + yes + " No: " + no);
    }
}
