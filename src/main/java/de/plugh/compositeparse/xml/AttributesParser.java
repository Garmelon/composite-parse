package de.plugh.compositeparse.xml;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.parsers.Expression;
import de.plugh.compositeparse.parsers.Literal;
import de.plugh.compositeparse.parsers.QuotedString;
import de.plugh.compositeparse.parsers.Repeat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributesParser implements Parser<Map<String, String>> {

    @Override
    public Map<String, String> read(Block block) throws ParseException {
        List<Attribute> attributes = new Repeat<>(block1 -> {
            new Expression("^\\s+").parse(block1);

            String name = new Expression(NodeParser.REGEX_NAME).parse(block1);
            Literal.literally("=").parse(block1);
            String value = new QuotedString().parse(block1);

            return new Attribute(name, value);
        }).parse(block);

        Map<String, String> attributeMap = new HashMap<>();
        attributes.forEach(attribute -> attributeMap.put(attribute.name, attribute.value));

        return attributeMap;
    }

    private class Attribute {

        final String name;
        final String value;

        Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

    }

}
