package de.plugh.compositeparse;

import java.util.List;
import java.util.function.Function;

/*
 * THIS LIBRARY
 *
 * This parsing system was inspired by haskell's megaparsec library and aims to
 * somewhat recreate the feel and flexibility of megaparsec in java.
 *
 * The main concept of this library is that parsers can be combined into bigger
 * parsers through either sequential parsing or by passing parsers into
 * constructors of other parsers. This happens during parsing and thus can
 * depend on previously parsed input and/or the current state of the program.
 *
 * For combining Parsers to work properly, all Parsers are immutable.
 *
 * BLOCKS
 *
 * While parsing, the library builds up a structure of Blocks representing the
 * structure of the Parsers which have already been tried. This structure serves
 * a dual purpose:
 *
 * 1) Each block stores the StringReader's cursor position when it is created.
 * This allows for the Parsers to backtrack, should a branch fail.
 *
 * 2) Each block holds sub-blocks created by the parsers it consists of. When a
 * ParseException is thrown, this information is used to figure out which syntax
 * was expected at the point of failure. This allows for descriptive error
 * messages which can be very useful in an interactive environment.
 *
 * A structure like this could not be constructed at compile time or cached,
 * because it depends on the input that is being parsed: Depending on the input
 * already parsed, a parser can decide to use different subparsers. Because of
 * this, the structure is created while parsing is occurring.
 *
 * For more info, see the documentation for Block.
 *
 * COMBINING PARSERS
 *
 * The main method of combining parsers is by sequentially calling them one
 * after the other. This is also the easiest way to collect results from the
 * parsers. Loops and conditionals can also be used, as can previously parsed
 * input.
 *
 * In some situations, there are multiple possible "branches" a parser could
 * take. In those cases, the Options parser can try multiple different parsers,
 * backtracking when one of them fails to try the next one.
 *
 * The Default parser can provide a default value in case a parser fails.
 *
 * The Repeat parser can repeat a parser a certain amount of times, with an
 * optional separator parser in-between.
 *
 * One can also manually catch the ParseExceptions. In that case, the cursor
 * position is reset (as if the parser that threw an exception never parsed any
 * input) and another Parser can be used.
 */

/**
 * A {@link Parser} knows how to parse a specific bit of information.
 * <p>
 * {@link Parser}s are usually created by combining multiple smaller parsers. For more information, see the introductory
 * comment in the source file of this class.
 *
 * @param <T> return type of the parser
 */
@FunctionalInterface
public interface Parser<T> {

    /**
     * @return the parser's naming scheme
     */
    default Function<List<Block>, String> getNamingScheme() {
        return Block::first;
    }

    /**
     * Parse a specific bit of information from the input.
     * <p>
     * <b>Do not overwrite this function unless you know what you're doing!</b>
     * <p>
     * <i>This is the function you usually want to call.</i>
     *
     * @param block the calling parser's {@link Block}
     * @return the information it parsed
     * @throws ParseException if the input format was incorrect
     */
    default T parse(Block block) throws ParseException {
        Block subblock = new Block(block, getNamingScheme());
        try {
            return read(subblock);
        } catch (ParseException e) {
            subblock.resetCursor();
            throw e;
        }
    }

    /**
     * The implementation regarding how to parse the specific bit of information.
     * <p>
     * <i>This is the function you usually want to overwrite.</i>
     *
     * @param block the calling parser's {@link Block}
     * @return the information it parsed
     * @throws ParseException if the input format was incorrect
     */
    T read(Block block) throws ParseException;

}
