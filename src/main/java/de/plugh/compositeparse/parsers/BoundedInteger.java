package de.plugh.compositeparse.parsers;

import de.plugh.compositeparse.Block;
import de.plugh.compositeparse.ParseException;
import de.plugh.compositeparse.Parser;

import java.util.List;
import java.util.function.Function;

/**
 * Parses an integer between a lower and upper bound.
 */
public class BoundedInteger implements Parser<Integer> {

    /*
     * @formatter:off
     *
     * Regex breakdown:
     *
     * ^       - Start at the cursor's current position
     * [+-]?   - Optionally match a sign in the beginning of the string
     * \\d+    - Match as many digits as you can find, at least one
     *
     * @formatter:on
     */
    private static final String INTEGER_REGEX = "^[+-]?\\d+";

    private final int min;
    private final int max;

    /**
     * Parse an integer between min and max. The integer is of the format {@code [+-]<digits>}.
     *
     * @param min minimum value of the integer
     * @param max maximum value of the integer
     * @see #between(int, int)
     */
    public BoundedInteger(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Parse an integer. The integer is of the format {@code [+-]<digits>}.
     */
    public BoundedInteger() {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Parse an integer >= min.
     *
     * @param min minimun size of the integer
     * @return the {@link BoundedInteger}
     */
    public static BoundedInteger atLeast(int min) {
        return new BoundedInteger(min, Integer.MAX_VALUE);
    }

    /**
     * Parse an integer <= max.
     *
     * @param max maximum size of the integer
     * @return the {@link BoundedInteger}
     */
    public static BoundedInteger atMost(int max) {
        return new BoundedInteger(Integer.MIN_VALUE, max);
    }

    /**
     * Parse an integer with min <= integer <= max.
     *
     * @param min minimun size of the integer
     * @param max maximum size of the integer
     * @return the {@link BoundedInteger}
     */

    public static BoundedInteger between(int min, int max) {
        return new BoundedInteger(min, max);
    }

    @Override
    public Function<List<Block>, String> getNamingScheme() {
        String description = "integer";

        if (min > Integer.MIN_VALUE && max < Integer.MAX_VALUE) {
            description += " (between " + min + " and " + max + ")";
        } else if (min > Integer.MIN_VALUE) {
            description += " (at least " + min + ")";
        } else if (max < Integer.MAX_VALUE) {
            description += " (at most " + max + ")";
        }

        return Block.label(description);
    }

    @Override
    public Integer read(Block block) throws ParseException {
        String integerString;
        try {
            integerString = new Expression(INTEGER_REGEX).parse(block);
        } catch (ParseException e) {
            // Mask the regex parse exception with our own (the error messages are better
            // this way)
            throw new ParseException(block);
        }

        int integer;
        try {
            integer = Integer.parseInt(integerString);
        } catch (NumberFormatException e) {
            throw new ParseException(block);
        }

        if (integer < min || integer > max) {
            throw new ParseException(block);
        }

        return integer;
    }

}
