package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.List;
import java.util.function.Function;

/**
 * Returns a value when the end of the input has been reached, fails otherwise.
 *
 * @param <T> return type of the parser
 */
public class EndOfInput<T> implements Parser<T> {

    private final T value;

    /**
     * Create a new {@link EndOfInput} parser.
     *
     * @param value the value to return
     */
    public EndOfInput(T value) {
        this.value = value;
    }

    /**
     * Create a new {@link EndOfInput} parser that always returns {@code null}.
     */
    public EndOfInput() {
        this(null);
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("end of input");
    }

    @Override
    public T read(Block block) throws ParseException {
        if (block.getInput().complete()) {
            return value;
        } else {
            throw new ParseException(block);
        }
    }

}
