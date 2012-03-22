package io.iron.ironworker.client.entities;

import java.util.List;

/**
 * User: Julien
 * Date: 22/03/12
 * Time: 17:27
 */
public class Codes {
    private List<Code> codes;

    public List<Code> getCodes() {
        return codes;
    }

    public void setCodes(List<Code> codes) {
        this.codes = codes;
    }

    @Override
    public String toString() {
        return "Codes{" +
                "codes=" + codes +
                '}';
    }
}
