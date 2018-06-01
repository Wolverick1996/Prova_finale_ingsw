package it.polimi.ingsw.server.model;

/**
 * Interface that contains the attributes common to all objective cards
 *
 * @author Matteo
 */
public interface ObjectiveCard {
    public int countPoints(Player player);
    public String getName();
    public String getDescription();
}