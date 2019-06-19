package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.List;
import java.util.function.Function;

public class QuotedString implements Parser<String> {

    private final String quoteChar;

    public QuotedString(String quoteChar) {
        this.quoteChar = quoteChar;
    }

    public QuotedString() {
        this("\"");
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("quoted string");
    }

    @Override
    public String read(Block block) throws ParseException {
        Literal.literally(quoteChar).parse(block);

        StringBuilder result = new StringBuilder();
        boolean escaped = false;
        while (true) {
            String s = block.getInput().read(1);

            if (s.isEmpty()) {
                throw new ParseException(block);
            } else if (escaped) {
                result.append(s);
                escaped = false;
            } else if (s.equals(quoteChar)) {
                break;
            } else if (s.equals("\\")) {
                escaped = true;
            } else {
                result.append(s);
            }
        }

        return result.toString();
    }

}
