package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Expression;
import de.plugh.compositeparse.parsers.Literal;

public class CommentNodeParser implements Parser<CommentNode> {

    @Override
    public CommentNode read(Block block) throws ParseException {
        Literal.literally("<!--").parse(block);
        String content = new Expression("^.*(?=--)").parse(block);
        Literal.literally("-->").parse(block);

        return new CommentNode(content);
    }

}
