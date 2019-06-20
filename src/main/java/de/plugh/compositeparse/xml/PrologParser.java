package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Literal;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class PrologParser implements Parser<Prolog> {

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("xml prolog");
    }

    @Override
    public Prolog read(Block block) throws ParseException {
        Literal.literally("<?xml").parse(block);
        Map<String, String> attributes = new AttributesParser().parse(block);
        Literal.literally("?>").parse(block);

        return new Prolog(attributes);
    }

}
