package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.StringInput;

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
            StringInput input = block.getInput();
            String s = input.read(1);

            if (s.isEmpty()) {
                throw new ParseException(block);
            } else if (escaped) {
                result.append(s);
                escaped = false;
            } else if (s.equals(quoteChar)) {
                input.move(-1);
                break;
            } else if (s.equals("\\")) {
                escaped = true;
            } else {
                result.append(s);
            }
        }

        Literal.literally(quoteChar).parse(block);

        return result.toString();
    }

}
