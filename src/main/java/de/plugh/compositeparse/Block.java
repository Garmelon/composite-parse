package de.plugh.compositeparse;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link Block} represents a single {@link Parser#parse(Block)} call. It helps implement back tracking and useful
 * error messages.
 * <p>
 * Whenever a {@link Parser}'s parse function is called, it creates a new {@link Block} and passes it along to all parse
 * calls of its sub-parsers. It also registers the {@link Block} it created with its super-parser's {@link Block}.
 * <p>
 * Each {@link Block} additionally saves the input's cursor position when it was created. This way, backtracking can be
 * achieved by walking up the {@link Block} tree and setting the input's cursor position to a higher {@link Block}'s
 * saved cursor position.
 * <p>
 * In addition to that, each {@link Block} remembers a naming scheme that operates on the {@link Block}'s sub-blocks.
 * This naming scheme is used to create a useful error message when a {@link ParseException} is thrown.
 */
public class Block {

    private static final int CONTEXT_LOOKBACK = 24;
    private final int initialCursor;
    private List<Block> subblocks;
    private Function<List<Block>, String> namingScheme;
    private StringInput input;

    private Block(Function<List<Block>, String> namingScheme, StringInput input) {
        subblocks = new ArrayList<>();
        this.namingScheme = namingScheme;

        this.input = input;
        initialCursor = input.getCursor();
    }

    /**
     * Create a top-level block from an input {@link String}.
     *
     * @param text the input {@link String}
     */
    public Block(String text) {
        this(Block::alternative, new StringInput(text));
    }

    /**
     * Create a new block as a sub-block of an existing block.
     *
     * @param superblock   the block that this block is a child to
     * @param namingScheme the naming scheme to use for {@link #getName()}
     */
    Block(Block superblock, Function<List<Block>, String> namingScheme) {
        this(namingScheme, superblock.input);

        superblock.register(this);
    }

    /*
     * Naming schemes
     */

    /**
     * Use the name of the first sub-block (useful for sequential blocks).
     *
     * @param blocks a block's sub-blocks
     * @return the first sub-block's name
     */
    public static String first(List<Block> blocks) {
        if (blocks.size() > 0) {
            return blocks.get(0).getName();
        } else {
            throw new BlockException("No subblocks found");
        }
    }

    /**
     * UCombine all sub-blocks' names using "or" (useful for optional parsers).
     *
     * @param blocks a block's sub-blocks
     * @return all sub-blocks' names, joined with "or"
     */
    public static String alternative(List<Block> blocks) {
        if (blocks.size() > 0) {
            return blocks.stream().map(Block::getName).collect(Collectors.joining(" or "));
        } else {
            throw new BlockException("No subblocks found");
        }
    }

    /**
     * Always return a constant name.
     *
     * @param name a block's sub-blocks
     * @return the name
     */
    public static Function<List<Block>, String> label(String name) {
        return ignored -> name;
    }

    private void register(Block subblock) {
        subblocks.add(subblock);
    }

    /**
     * @return the input {@link StringInput}
     */
    public StringInput getInput() {
        return input;
    }

    /**
     * @return the name
     */
    public String getName() {
        return namingScheme.apply(subblocks);
    }

    /**
     * Reset the input {@link StringInput}'s cursor to this block's initial cursor position
     */
    public void resetCursor() {
        input.setCursor(initialCursor);
    }

    /**
     * @return a few characters from before this block's initial cursor position
     */
    public String getContext() {
        int currentCursor = input.getCursor();

        input.setCursor(initialCursor);
        String context = input.look(-CONTEXT_LOOKBACK);

        input.setCursor(currentCursor);

        return "..." + context;
    }

}
