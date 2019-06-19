package de.plugh.compositeparse.xml;

public class CommentNode extends Node {

    private final String content;

    public CommentNode(String content) {
        this.content = content;
    }

    @Override
    public String prettyPrint(String indent, boolean newline) {
        if (newline) {
            return indent + "<!--" + content + "-->" + "\n";
        } else {
            return indent + "<!--" + content + "-->";
        }
    }

    @Override
    public String toString() {
        return "CommentNode{" +
                "content='" + content + '\'' +
                '}';
    }

}
