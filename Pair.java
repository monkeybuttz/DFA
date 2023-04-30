
public class Pair<state, alphabet>{
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

    //get first element of the pair
    public state getFirst() {
        return state;
    }

    //get second element of the pair
    public alphabet getSecond() {
        return alphabet;
    }
}
