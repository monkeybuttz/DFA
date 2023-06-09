import java.util.*;
import java.io.*;

public class NFA2DFA {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        //check proper arguments were provided
        if (args.length != 1)
        {
            System.out.println("Usage: java NFA2DFA file.nfa");
            System.exit(0);
        }
        String inputFile = args[0];
        File NFA = new File(inputFile);
        ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct = Parse.ParseStudentA(NFA);
        convertNFA2DFA(returnStruct, NFA);
    }

    // takes the NFA in the return structure and converts it to a DFA and writes it to a file
    public static void convertNFA2DFA(ReturnStructure<Hashtable<Pair<Character, Character>, ArrayList<Integer>>, String, String[], Character[], ArrayList<String>>  returnStruct, File NFA) throws IOException {
        try{
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> NFAtransitionTable = returnStruct.getPairedHashTable();
        String NFAstartState = returnStruct.getStartState();
        Character[] NFAalphabet = returnStruct.getAlphabetStrings();
        String[] NFAacceptingStates = returnStruct.getAcceptingStates();

        //get input strings from file
        ArrayList<String> inputStrings = returnStruct.getInputStrings();

        //create the dfa transition table
        Hashtable<Pair<Character, Character>, ArrayList<Integer>> DFAtransitionTable = new Hashtable<Pair<Character, Character>, ArrayList<Integer>>();
        ArrayList<Integer> DFAstartState = new ArrayList<Integer>();

        //create the dfa start state
        ArrayList<Integer> lambdaTransList = new ArrayList<Integer>();
        findLambdaClosure(NFAtransitionTable, Integer.parseInt(NFAstartState), lambdaTransList);
        Collections.sort(lambdaTransList);
        DFAstartState = lambdaTransList;

        //set dfa alphabet equal to nfa alphabet besides the lambda transition
        ArrayList<Character> DFAalphabet = new ArrayList<Character>();
        for (int i = 0; i < NFAalphabet.length; i++) {
            if (NFAalphabet[i] != 'L') {
                DFAalphabet.add(NFAalphabet[i]);
            }
        }

        //create the dfa states list
        ArrayList<ArrayList<Integer>> DFAstates = new ArrayList<ArrayList<Integer>>();
        DFAstates.add(DFAstartState);

        //find the transitions for the dfa start state
        //find the lambda closure of each transition
        //get the union of the transitions
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
            DFAtransitionTable.put(new Pair<Character, Character>(Character.forDigit(DFAstates.indexOf(DFAstartState), 10), DFAalphabet.get(i)), DFAtransition);
            if (!DFAstates.contains(DFAtransition)) {
                DFAstates.add(DFAtransition);
            }
        }

        //do the same for the rest of the dfa states until all states have been visited
        // if the state has already been visited, dont visit it again
        // if there is no transition for a state, add a transition to a dead state
        for (int i = 0; i < DFAstates.size(); i++) {
            for (int j = 0; j < DFAalphabet.size(); j++) {
                ArrayList<Integer> DFAtransition = new ArrayList<Integer>();
                for (int k = 0; k < DFAstates.get(i).size(); k++) {
                    ArrayList<Integer> state1 = findDFAtransition(NFAtransitionTable, DFAstates.get(i).get(k), DFAalphabet.get(j));
                    for (int l = 0; l < state1.size(); l++) {
                        ArrayList<Integer> lambdaTransList2 = new ArrayList<Integer>();
                        findLambdaClosure(NFAtransitionTable, state1.get(l), lambdaTransList2);
                        Collections.sort(lambdaTransList2);
                        state1 = union(state1, lambdaTransList2, NFAtransitionTable);
                    }
                    DFAtransition = union(DFAtransition, state1, NFAtransitionTable);
                }
                
                Collections.sort(DFAtransition);
                //System.out.println("DFA transition for " + DFAstates.get(i) + " with alphabet " + DFAalphabet.get(j) + ": " + DFAtransition);
                if (!DFAstates.contains(DFAtransition)) {
                    DFAstates.add(DFAtransition);
                }
                DFAtransitionTable.put(new Pair<Character, Character>(Character.forDigit(i, 10), DFAalphabet.get(j)), DFAtransition);
            }
        }

        //find the accepting states for the dfa
        // find the dfa accepting states by checking if the dfa state contains an nfa accepting state
        ArrayList<Integer> DFAacceptingState = new ArrayList<Integer>();
        for (int i = 0; i < DFAstates.size(); i++) {
            for (int j = 0; j < DFAstates.get(i).size(); j++) {
                for (int k = 0; k < NFAacceptingStates.length; k++) {
                    if (DFAstates.get(i).get(j) == Integer.parseInt(NFAacceptingStates[k]) && !DFAacceptingState.contains(i)) {
                        DFAacceptingState.add(i);
                    }
                }
            }
        }

        //write the dfa to a new file
        writeDFA(DFAtransitionTable, DFAstates, DFAalphabet, DFAstartState, DFAacceptingState, inputStrings, NFA);
    }
    catch (IOException e) {
        e.printStackTrace();
    }
    }

    //write the dfa to a new file 
    public static void writeDFA(Hashtable<Pair<Character, Character>, ArrayList<Integer>> DFAtransitionTable, ArrayList<ArrayList<Integer>> DFAstates, ArrayList<Character> DFAalphabet, ArrayList<Integer> DFAstartState, ArrayList<Integer> DFAacceptingState, ArrayList<String> testStrings, File NFAfile) throws IOException{
        try {
            //have the file be the same name as the nfa file with .dfa extension
            String fileName = NFAfile.getName();
            fileName = fileName.substring(0, 1);
            fileName = fileName + ".dfa";
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("|Q|: " + DFAstates.size());
            bufferedWriter.newLine();
            bufferedWriter.write("Sigma:");
            for (int i = 0; i < DFAalphabet.size(); i++) {
                bufferedWriter.write("     " + DFAalphabet.get(i));
            }
            bufferedWriter.newLine();
            bufferedWriter.write("------------------------------");
            bufferedWriter.newLine();
            
            // write out the each of the states and their transitions
            for (int i = 0; i < DFAstates.size(); i++) {
                bufferedWriter.write( "\t" + i + ":");
                for (int j = 0; j < DFAalphabet.size(); j++) {
                    if (DFAtransitionTable.get(new Pair<Character, Character>(Character.forDigit(i, 10), DFAalphabet.get(j))) == null) {
                        bufferedWriter.write("    " + DFAstates.size());
                    } else {
                        bufferedWriter.write("     " + DFAstates.indexOf(DFAtransitionTable.get(new Pair<Character, Character>(Character.forDigit(i, 10), DFAalphabet.get(j)))));
                    }
                 }
                bufferedWriter.newLine();
            }
            bufferedWriter.write("------------------------------");
            bufferedWriter.newLine();
            bufferedWriter.write("Initial State: " + DFAstates.indexOf(DFAstartState));
            bufferedWriter.newLine();
            bufferedWriter.write("Accepting State(s): ");
            for (int i = 0; i < DFAacceptingState.size(); i++) {
                if(i == DFAacceptingState.size() - 1)
                    bufferedWriter.write(DFAacceptingState.get(i) + "");
                else
                    bufferedWriter.write(DFAacceptingState.get(i) + ",");

            }
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("-- Input strings for testing -----------");
            bufferedWriter.newLine();
            for (int i = 0; i < testStrings.size(); i++) {
                bufferedWriter.write(testStrings.get(i));
                bufferedWriter.newLine();
            }
            
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
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
     // sort the list and dont contain any duplicates make sure to add the state itself to the lambda closure
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
        Collections.sort(lambdaTransList);
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

