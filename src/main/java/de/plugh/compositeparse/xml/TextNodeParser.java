package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Expression;

import java.util.List;
import java.util.function.Function;

public class TextNodeParser implements Parser<TextNode> {

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("text");
    }

    @Override
    public TextNode read(Block block) throws ParseException {
        String text = new Expression("^[^<]+").parse(block);

        return new TextNode(text);
    }

}
