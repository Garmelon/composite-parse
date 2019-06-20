package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Default;
import de.plugh.compositeparse.parsers.Repeat;

import java.util.List;

public class DocumentParser implements Parser<Document> {

    @Override
    public Document read(Block block) throws ParseException {
        Prolog prolog = new Default<>(null, new PrologParser()).parse(block);
        List<Node> nodes = new Repeat<>(new NodeParser()).parse(block);

        return new Document(prolog, nodes);
    }

}
