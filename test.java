public class test {
    public static void main (String[] args) {
        String finalStates = "1";
        String[] finalStatesArray = finalStates.split(",");
        for(int i = 0; i < finalStatesArray.length; i++) {
            System.out.println(finalStatesArray[i]);
        }
    }
}
