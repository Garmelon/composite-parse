package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.Pair;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Decision;
import de.plugh.compositeparse.parsers.Expression;
import de.plugh.compositeparse.parsers.Literal;
import de.plugh.compositeparse.parsers.Repeat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElementNodeParser implements Parser<ElementNode> {

    private static final String REGEX_NAME = "[:a-zA-Z_][:a-zA-Z0-9_.-]*";

    @Override
    public ElementNode read(Block block) throws ParseException {
        Literal.literally("<").parse(block);
        String name = new Expression(REGEX_NAME).parse(block);

        Map<String, String> attributes = new AttributesParser().parse(block);

        List<Node> subnodes = new Decision<List<Node>>(
                // First, try to find a close tag
                new Pair<>(
                        block1 -> {
                            new Repeat<>(Literal.literally(" ")).parse(block1);
                            Literal.literally("/>").parse(block1);
                            return null;
                        },
                        block1 -> new ArrayList<>()
                ),
                // If that fails, actually parse the contents and the rest
                new Pair<>(
                        Literal.literally(">"),
                        block1 -> {
                            List<Node> foundSubnodes = new Repeat<>(new NodeParser()).parse(block);
                            Literal.literally("</" + name + ">").parse(block1);
                            return foundSubnodes;
                        }
                )
        ).parse(block);

        return new ElementNode(name, attributes, subnodes);
    }

}
