package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.List;
import java.util.function.Function;

/**
 * Attaches a name to a parser.
 * <p>
 * This can be useful for naming lambda parsers, or renaming existing parsers.
 *
 * @param <T> return type of the parser
 */
public class Label<T> implements Parser<T> {

    private final String name;
    private final Parser<T> parser;

    /**
     * Create a new {@link Label} parser.
     *
     * @param name   the parser's new name
     * @param parser the parser to rename
     */
    public Label(String name, Parser<T> parser) {
        this.name = name;
        this.parser = parser;
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block.label(name);
    }

    @Override
    public T read(Block block) throws ParseException {
        return parser.parse(block);
    }

}
