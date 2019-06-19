package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.Parser;

import java.util.List;
import java.util.function.Function;

/**
 * Consumes no input and returns a constant value.
 *
 * @param <T> return type of the parser
 */
public class Constant<T> implements Parser<T> {

    private T value;

    /**
     * Create a new {@link Constant} parser.
     *
     * @param value the value to return
     */
    public Constant(T value) {
        this.value = value;
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label("constant");
    }

    @Override
    public T read(Block block) {
        return value;
    }

}
