package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;
import de.plugh.compositeparse.StringInput;

import java.util.List;
import java.util.function.Function;

/**
 * Parses a string literal from the input (case sensitive).
 * <p>
 * For more flexible input, see {@link Expression}.
 *
 * @param <T> return type of the parser
 */
public class Literal<T> implements Parser<T> {

    private final String literal;
    private final T value;

    /**
     * Consume a string literal from the input and return a value if successful.
     *
     * @param literal the literal to consume from the input
     * @param value   the value to return
     */
    public Literal(String literal, T value) {
        this.literal = literal;
        this.value = value;
    }

    /**
     * Consume a string literal from the input
     *
     * @param literal the literal to consume from the input
     */
    public Literal(String literal) {
        this(literal, null);
    }

    /**
     * Create a {@link Literal} that returns (literally) the literal it consumes.
     * <p>
     * Shorthand for <code>new Literal(literal, literal)</code>
     *
     * @param literal the literal to consume from the input
     * @return the {@link Literal}
     */
    public static Literal<String> literally(String literal) {
        return new Literal<>(literal, literal);
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("\"" + literal + "\"");
    }

    @Override
    public T read(Block block) throws ParseException {
        StringInput input = block.getInput();
        if (input.read(literal.length()).equals(literal)) {
            return value;
        } else {
            throw new ParseException(block);
        }
    }

}
