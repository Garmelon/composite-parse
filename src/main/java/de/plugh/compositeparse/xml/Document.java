package de.plugh.compositeparse.xml;

import java.util.List;

public class Document {

    private final Prolog prolog;
    private final List<Node> nodes;

    public Document(Prolog prolog, List<Node> nodes) {
        this.prolog = prolog;
        this.nodes = nodes;
    }

    public String prettyPrint() {
        StringBuilder result = new StringBuilder();

        if (prolog != null) {
            result.append(prolog.prettyPrint(true));
        }

        nodes.forEach(node -> result.append(node.prettyPrint()));

        return result.toString();
    }

    @Override
    public String toString() {
        return "Document{" +
                "prolog=" + prolog +
                ", nodes=" + nodes +
                '}';
    }

}
