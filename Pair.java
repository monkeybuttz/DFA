import java.util.*;

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
