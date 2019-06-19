package de.plugh.compositeparse;

/**
 * A {@link BlockException} is thrown when actions are executed on a malformed {@link Block} structure.
 */
@SuppressWarnings("serial") // This exception does not need to be serialised.
public class BlockException extends RuntimeException {

    /**
     * Create a new {@link BlockException}.
     *
     * @param reason what went wrong
     */
    public BlockException(String reason) {
        super(reason);
    }

}
