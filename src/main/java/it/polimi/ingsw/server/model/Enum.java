package it.polimi.ingsw.server.model;

import java.util.Random;

/**
 * Enum is the class which contains every enum classes and methods, created to simplify the design
 * To use this class type *extends Enum*
 *
 * @author Matteo
 */
public class Enum {

    /**
     * Color class defines the 5 colors of dice and the RESET value
     *
     * @author Riccardo
     */
    public enum Color {
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m");

        public static final String RESET = "\u001B[0m";

        private String escape;

        /**
         * Sets the specified color
         *
         * @param escape: string containing color to set
         * @author Riccardo
         */
        Color (String escape) {
            this.escape = escape;
        }

        /**
         * Used to print colored output
         *
         * @author Riccardo
         */
        public String escape() {
            return escape;
        }

        /**
         * Generates a random color of the 5 possible
         *
         * @return the random color generated
         * @author Riccardo
         */
        public static Color getRandomColor() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

}