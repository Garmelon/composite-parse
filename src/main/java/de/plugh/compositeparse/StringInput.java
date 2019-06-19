package de.plugh.compositeparse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@link StringInput} consists of a {@link String} and a cursor position on that {@link String}.
 * <p>
 * It provides a convenient way to view a {@link String}, in addition to a few useful functions.
 */
public class StringInput {

    private String string;
    private int cursor;

    /**
     * Create a new {@link StringInput} over a {@link String}.
     *
     * @param string the content of the reader
     */
    public StringInput(String string) {
        this.string = string;
        this.cursor = 0;
    }

    private int clampCursor(int position, int delta) {
        /*
         * A cursor can have position string.length() because its position is
         * interpreted as between the characters, not on the characters, similar to
         * python's slicing.
         *
         * Examples, using "|" as the cursor position and "aabc" as the string:
         *
         * |aabc - The cursor is in position 0.
         *
         * aab|c - The cursor is in position 3.
         *
         * aabc| - The cursor is in position 4.
         */

        /*
         * This prevents an overflow/underflow if somebody tries to look(), read() or
         * move() with Integer.MIN_VALUE or Integer.MAX_VALUE (like I did while testing
         * this).
         */
        int minDelta = -position;
        int maxDelta = string.length() - position;
        return position + Math.max(minDelta, Math.min(maxDelta, delta));
    }

    /**
     * @return the cursor position
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * @param cursor the cursor position
     */
    public void setCursor(int cursor) {
        this.cursor = clampCursor(cursor, 0);
    }

    /**
     * Move the cursor a certain amount of characters relative to the cursor's current position. A positive amount moves
     * forward (towards the end of the string), a negative moves backward (towards the beginning of the string).
     *
     * @param amount how many characters to move the cursor by
     */
    public void move(int amount) {
        setCursor(clampCursor(getCursor(), amount));
    }

    /**
     * Read a certain amount of characters relative to the cursor's current position. A positive amount looks forward
     * (towards the end of the string), a negative looks backward (towards the beginning of the string).
     *
     * @param amount how many characters to look up
     * @return the specified section of the string
     */
    public String look(int amount) {
        if (amount >= 0) {
            return string.substring(cursor, clampCursor(cursor, amount));
        } else {
            return string.substring(clampCursor(cursor, amount), cursor);
        }
    }

    /**
     * Combines a {@link #look(int)} and a {@link #move(int)} operation.
     *
     * @param amount how many characters to look up and move
     * @return the specified section of the string
     */
    public String read(int amount) {
        String result = look(amount);
        move(amount);
        return result;
    }

    /**
     * Match and {@link #read(int)} the regex passed, starting at the current cursor position.
     * <p>
     * This returns everything from the current cursor position to the end of the match that was found, so make sure to
     * anchor your regexes (using ^) unless you need all of that.
     *
     * @param regex the regular expression to use
     * @return the string matched (or null, if no match was found)
     */
    public String match(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string.substring(cursor));
        if (matcher.find()) {
            return read(matcher.end());
        } else {
            return null;
        }
    }

    /**
     * @return whether the whole input was consumed
     */
    public boolean complete() {
        return cursor >= string.length();
    }

}
