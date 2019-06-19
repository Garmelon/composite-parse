package de.plugh.compositeparse.xml;

import java.util.List;
import java.util.Map;

public class ElementNode extends Node {

    private final String name;
    private final Map<String, String> attributes;
    private final List<Node> subnodes;

    public ElementNode(String name, Map<String, String> attributes,
                       List<Node> subnodes) {
        this.name = name;
        this.attributes = attributes;
        this.subnodes = subnodes;
    }

    @Override
    public String prettyPrint(String indent, boolean newline) {
        StringBuilder result = new StringBuilder()
                .append(indent)
                .append("<")
                .append(name);

        attributes.forEach((s, s2) -> {
            result
                    .append(" ")
                    .append(s)
                    .append("=\"")
                    .append(s2) // TODO escape
                    .append("\"");
        });

        if (subnodes.isEmpty()) {
            result.append("/>");
        } else {
            result.append(">");

            if (subnodes.size() == 1 && subnodes.get(0) instanceof TextNode) {
                result.append(subnodes.get(0).prettyPrint("", false));

                result
                        .append("</")
                        .append(name)
                        .append(">");
            } else {
                result.append("\n");

                subnodes.forEach(node -> result.append(node.prettyPrint(indent + "  ", true)));

                result
                        .append(indent)
                        .append("</")
                        .append(name)
                        .append(">");
            }
        }

        if (newline) {
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return "ElementNode{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                ", subnodes=" + subnodes +
                '}';
    }

}
