package net.adamsmolnik.workflow;

/**
 * @author ASmolnik
 *
 */
public class OutcomeReport {

    private StringBuilder sb = new StringBuilder();

    public void add(String s) {
        sb.append(s).append("\n");
    }

    @Override
    public String toString() {
        return sb.toString();
    }

}
