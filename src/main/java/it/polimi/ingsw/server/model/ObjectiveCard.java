package it.polimi.ingsw.server.model;

/**
 * Interface that contains the attributes common to all objective cards
 *
 * @author Andrea
 */
public interface ObjectiveCard {

    /**
     * Receives a player as a parameter and calls methods to count points related to the card
     *
     * @param player: player whose points the method must calculate
     * @return the amount of points earned the card in play
     * @author Matteo
     */
    int countPoints(Player player);

    /**
     * Returns card's name
     *
     * @return card's name
     * @author Matteo
     */
    String getName();

    /**
     * Returns card's description
     *
     * @return card's description
     * @author Matteo
     */
    String getDescription();

}