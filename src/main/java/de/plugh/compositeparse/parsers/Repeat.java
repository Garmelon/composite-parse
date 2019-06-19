package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Repeats a parser a certain amount of times and compiles the results in a {@link List}.
 * <p>
 * Use another parser in-between the main parser to parse separators (e. g. commas).
 *
 * @param <T> return type of the parser
 */
public class Repeat<T> implements Parser<List<T>> {

    private final Parser<?> separator;
    private final Parser<T> parser;
    private final int from;
    private final int to;

    /**
     * Create a new {@link Repeat} parser.
     *
     * @param from      minimum amount of repeats
     * @param to        maximum amount of repeats
     * @param separator the parser that separates the main parser
     * @param parser    the parser to repeatedly use
     */
    public Repeat(int from, int to, Parser<?> separator, Parser<T> parser) {
        // Just in case somebody enters incorrect values, attempt to interpret them as
        // best as possible.
        this.from = Math.max(0, Math.min(from, to));
        this.to = Math.max(0, Math.max(from, to));

        this.separator = separator;
        this.parser = parser;
    }

    /**
     * Create a new {@link Repeat} parser (without separators).
     *
     * @param from   minimum amount of repeats
     * @param to     maximum amount of repeats
     * @param parser the parser to repeatedly use
     */
    public Repeat(int from, int to, Parser<T> parser) {
        this(from, to, null, parser);
    }

    /**
     * Create a new {@link Repeat} parser that repeats zero or more times.
     *
     * @param separator the parser that separates the main parser
     * @param parser    the parser to repeatedly use
     */
    public Repeat(Parser<?> separator, Parser<T> parser) {
        this(0, Integer.MAX_VALUE, separator, parser);
    }

    /**
     * Create a new {@link Repeat} parser that repeats zero or more times (without separator).
     *
     * @param parser the parser to repeatedly use
     */
    public Repeat(Parser<T> parser) {
        this(null, parser);
    }

    /**
     * Repeat a parser at least {@code amount} times.
     *
     * @param <T>       the {@link Repeat}'s type
     * @param amount    how often to repeat the parser
     * @param separator the parser that separates the main parser
     * @param parser    the parser to repeatedly use
     * @return the {@link Repeat}
     */
    public static <T> Repeat<T> atLeast(int amount, Parser<?> separator, Parser<T> parser) {
        return new Repeat<>(amount, Integer.MAX_VALUE, separator, parser);
    }

    /**
     * Repeat a parser at least {@code amount} times (without separator).
     *
     * @param <T>    the {@link Repeat}'s type
     * @param amount how often to repeat the parser
     * @param parser the parser to repeatedly use
     * @return the {@link Repeat}
     */
    public static <T> Repeat<T> atLeast(int amount, Parser<T> parser) {
        return new Repeat<>(amount, Integer.MAX_VALUE, parser);
    }

    /**
     * Repeat a parser at most {@code amount} times.
     *
     * @param <T>       the {@link Repeat}'s type
     * @param amount    how often to repeat the parser
     * @param separator the parser that separates the main parser
     * @param parser    the parser to repeatedly use
     * @return the {@link Repeat}
     */
    public static <T> Repeat<T> atMost(int amount, Parser<?> separator, Parser<T> parser) {
        return new Repeat<>(0, amount, separator, parser);
    }

    /**
     * Repeat a parser at most {@code amount} times (without separator).
     *
     * @param <T>    the {@link Repeat}'s type
     * @param amount how often to repeat the parser
     * @param parser the parser to repeatedly use
     * @return the {@link Repeat}
     */
    public static <T> Repeat<T> atMost(int amount, Parser<T> parser) {
        return new Repeat<>(0, amount, parser);
    }

    /**
     * Repeat a parser exactly {@code amount} times.
     *
     * @param <T>       the {@link Repeat}'s type
     * @param amount    how often to repeat the parser
     * @param separator the parser that separates the main parser
     * @param parser    the parser to repeatedly use
     * @return the {@link Repeat}
     */
    public static <T> Repeat<T> exactly(int amount, Parser<?> separator, Parser<T> parser) {
        return new Repeat<>(amount, amount, separator, parser);
    }

    /**
     * Repeat a parser exactly {@code amount} times (without separator).
     *
     * @param <T>    the {@link Repeat}'s type
     * @param amount how often to repeat the parser
     * @param parser the parser to repeatedly use
     * @return the {@link Repeat}
     */
    public static <T> Repeat<T> exactly(int amount, Parser<T> parser) {
        return new Repeat<>(amount, amount, parser);
    }

    @Override
    public List<T> read(Block block) throws ParseException {
        List<T> results = new ArrayList<>();

        if (from > 0) {
            // The first element, not preceded by a "between"
            results.add(parser.parse(block));

            // All other elements, preceded by a "between"
            results.addAll(parseRequired(block, from - 1));
            results.addAll(parseOptional(block, to - from));
        } else if (to > 0) {
            // The first element, not preceded by a "between"
            try {
                results.add(parser.parse(block));
            } catch (ParseException e) {
                return results; // empty list
            }

            // All other elements, preceded by a "between"
            results.addAll(parseOptional(block, to - 1)); // from == 0
        }

        return results;
    }

    private void parseBetween(Block block) throws ParseException {
        if (separator != null) {
            separator.parse(block);
        }
    }

    private List<T> parseRequired(Block block, int amount) throws ParseException {
        List<T> results = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            parseBetween(block);
            results.add(parser.parse(block));
        }

        return results;
    }

    private List<T> parseOptional(Block block, int amount) {
        List<T> results = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            try {
                T result = ((Parser<T>) block2 -> {
                    parseBetween(block2);
                    return parser.parse(block2);
                }).parse(block);

                results.add(result);
            } catch (ParseException e) {
                break;
            }
        }

        return results;
    }

}
