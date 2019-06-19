package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.List;
import java.util.function.Function;

/**
 * Try a parser and return its value, or a default value if the parser fails.
 * <p>
 * This parser will never throw a {@link ParseException}.
 *
 * @param <T> return type of the parser
 */
public class Default<T> implements Parser<T> {

    private final T value;
    private final Parser<T> parser;

    /**
     * Create a new {@link Default} parser.
     *
     * @param value  the value to return
     * @param parser the parser to try
     */
    public Default(T value, Parser<T> parser) {
        this.value = value;
        this.parser = parser;
    }

    /**
     * Create a new {@link Default} parser.
     *
     * @param parser the parser to try
     */
    public Default(Parser<T> parser) {
        this(null, parser);
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        // There is always a block 0 because of Default's read() implementation.
        return blocks -> blocks.get(0).getName();
    }

    @Override
    public T read(Block block) {
        try {
            return parser.parse(block);
        } catch (ParseException ignored) {
            return value;
        }
    }

}
