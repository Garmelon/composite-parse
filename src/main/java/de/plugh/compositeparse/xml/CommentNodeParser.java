package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Expression;
import de.plugh.compositeparse.parsers.Literal;

import java.util.List;
import java.util.function.Function;

public class CommentNodeParser implements Parser<CommentNode> {

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("xml comment");
    }

    @Override
    public CommentNode read(Block block) throws ParseException {
        Literal.literally("<!--").parse(block);
        String content = new Expression("^.*(?=--)").parse(block);
        Literal.literally("-->").parse(block);

        return new CommentNode(content);
    }

}
