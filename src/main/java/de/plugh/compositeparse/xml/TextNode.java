package de.plugh.compositeparse.xml;

public class TextNode extends Node {

    private final String text;

    public TextNode(String text) {
        this.text = text;
    }

    @Override
    public String prettyPrint(String indent, boolean newline) {
        if (newline) {
            return indent + text.trim() + "\n";
        } else {
            return indent + text.trim();
        }
    }

    @Override
    public String toString() {
        return "TextNode{" +
                "text='" + text + '\'' +
                '}';
    }

}
