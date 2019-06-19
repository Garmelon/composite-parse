package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Expression;

public class TextNodeParser implements Parser<TextNode> {

    @Override
    public TextNode read(Block block) throws ParseException {
        String text = new Expression("^[^<]+").parse(block);

        return new TextNode(text);
    }

}
