package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.Pair;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Decide which parser to use from a list of "body" parsers and their "head"s.
 * <p>
 * If a "head" parses successfully, the corresponding "body" parser must be successful, otherwise a parse exception is
 * raised. If no "head" is successful, the {@link Decision} parser fails too.
 *
 * @param <T> return type of the parser
 */
public class Decision<T> implements Parser<T> {

    private final List<Pair<Parser<?>, Parser<T>>> pairs;

    /**
     * Create a new {@link Decision} parser from all passed arguments, which are "head"-"body" pairs.
     *
     * @param pairs multiple "head"-"body" pairs
     */
    @SafeVarargs
    public Decision(Pair<Parser<?>, Parser<T>>... pairs) {
        this.pairs = new ArrayList<>();
        Collections.addAll(this.pairs, pairs);
    }

    /**
     * Create a new {@link Decision} parser from a list of "head"-"body" pairs.
     *
     * @param pairs a list of "head"-"body" pairs
     */
    public Decision(List<Pair<Parser<?>, Parser<T>>> pairs) {
        this.pairs = new ArrayList<>(pairs);
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        return Block::alternative;
    }

    @Override
    public T read(Block block) throws ParseException {
        for (Pair<Parser<?>, Parser<T>> pair : pairs) {
            try {
                pair.getFirst().parse(block);
            } catch (ParseException e) {
                continue;
            }

            return pair.getSecond().parse(block);
        }

        throw new ParseException(block);
    }

}
