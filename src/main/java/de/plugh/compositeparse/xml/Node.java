package de.plugh.compositeparse.xml;

public abstract class Node {

    public abstract String prettyPrint(String indent, boolean newline);

    public String prettyPrint() {
        return prettyPrint("", false);
    }

}
