package de.plugh.compositeparse;

/**
 * This exception is thrown when a parser encounters incorrect input. It contains a bit of information about the
 * failure:
 * <p>
 * The name of the parser that failed.
 * <p>
 * A few characters of context, ending at the position where the parser failed.
 */
@SuppressWarnings("serial") // This exception does not need to be serialised.
public class ParseException extends Exception {

    private final String name;
    private final String context;

    /**
     * Create a new {@link ParseException} at a block.
     *
     * @param block the block to take the extra information from
     */
    public ParseException(Block block) {
        name = block.getName();
        context = block.getContext();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    @Override
    public String getMessage() {
        return getContext() + "<- expected: " + getName();
    }

}
