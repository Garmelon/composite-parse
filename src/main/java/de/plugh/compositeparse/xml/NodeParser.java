package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Options;

public class NodeParser implements Parser<Node> {

    static final String REGEX_NAME = "[:a-zA-Z_][:a-zA-Z0-9_.-]*";

    @Override
    public Node read(Block block) throws ParseException {
        return new Options<>(
                block1 -> new CommentNodeParser().parse(block1),
                block1 -> new ElementNodeParser().parse(block1),
                block1 -> new TextNodeParser().parse(block1)
        ).parse(block);
    }

}
