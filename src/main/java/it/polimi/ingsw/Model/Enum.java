package it.polimi.ingsw.Model;
import java.util.Random;

public class Enum {

    //Here you can find every enum, created to simplify the design
    //To use this *extends enum*

    public enum Color{
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        PURPLE("\u001B[35m");

        public static final String RESET = "\u001B[0m";

        private String escape;

        Color(String escape) {
            this.escape = escape;
        }

        public String escape() {
            return escape;
        }

        public static Color getRandomColor() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }

    public enum Placement{
        SCHEME,
        RESERVE,
        ROUNDTRACK,
        BAG
    }

    public enum Values{

    }

}
