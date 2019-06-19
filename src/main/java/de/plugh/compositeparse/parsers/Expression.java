package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.StringInput;

import java.util.List;
import java.util.function.Function;

/**
 * Parse a regular expression from the input.
 */
public class Expression implements Parser<String> {

    private final String regex;

    /**
     * Create a new {@link Expression} parser.
     *
     * @param regex the regular expression to use
     * @see StringInput#match(String)
     */
    public Expression(String regex) {
        this.regex = regex;
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("regex \"" + regex + "\"");
    }

    @Override
    public String read(Block block) throws ParseException {
        StringInput input = block.getInput();
        String result = input.match(regex);

        if (result == null) {
            throw new ParseException(block);
        }

        return result;
    }

}
