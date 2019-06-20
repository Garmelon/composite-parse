package de.plugh.compositeparse.xml;

import java.util.Map;

public class Prolog {

    private final Map<String, String> attributes;

    public Prolog(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String prettyPrint(boolean newline) {
        StringBuilder result = new StringBuilder();

        result.append("<?xml");

        attributes.forEach((s, s2) -> {
            result
                    .append(" ")
                    .append(s)
                    .append("=\"")
                    .append(s2)
                    .append("\"");
        });

        result.append("?>");

        if (newline) {
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return "Prolog{" +
                "attributes=" + attributes +
                '}';
    }

}
