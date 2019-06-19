package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Try a few parsers in order (backtracking if a parser fails) and return the result of the first successful parser.
 *
 * @param <T> return type of the parser
 */
public class Options<T> implements Parser<T> {

    private final List<Parser<T>> parsers;

    /**
     * Create a new {@link Options} from all passed parsers.
     *
     * @param parsers the parsers to try.
     */
    @SafeVarargs
    public Options(Parser<T>... parsers) {
        this.parsers = new ArrayList<>();
        for (Parser<T> parser : parsers) {
            this.parsers.add(parser);
        }
    }

    /**
     * Create a new {@link Options} from a list of parsers.
     *
     * @param parsers the parsers to try.
     */
    public Options(List<Parser<T>> parsers) {
        this.parsers = new ArrayList<>(parsers);
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block::alternative;
    }

    @Override
    public T read(Block block) throws ParseException {
        for (Parser<T> parser : parsers) {
            try {
                return parser.parse(block);
            } catch (ParseException ignored) {
            }
        }

        throw new ParseException(block);
    }

}
