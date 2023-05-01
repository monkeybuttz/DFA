import java.util.Objects;

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

    //helps to compare pairs with pairs
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(state, pair.state) && Objects.equals(alphabet, pair.alphabet);
    }

    //makes sure hash code for objects with multiple fields 
    //is same for objects with same fields
    @Override
    public int hashCode() {
        return Objects.hash(state, alphabet);
    }
}
