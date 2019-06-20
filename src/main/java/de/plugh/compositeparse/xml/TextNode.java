package de.plugh.compositeparse.xml;

public class TextNode extends Node {

    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public String prettyPrint(String indent, boolean newline) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return "";
        }

        String result = indent + trimmed;
        if (newline) {
            return result + "\n";
        } else {
            return result;
        }
    }

    @Override
    public String toString() {
        return "TextNode{" +
                "text='" + text + '\'' +
                '}';
    }

}
