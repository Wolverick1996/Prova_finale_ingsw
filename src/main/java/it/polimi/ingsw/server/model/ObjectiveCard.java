package it.polimi.ingsw.server.model;

/**
 * Interface that contains the attributes common to all objective cards
 *
 * @author Andrea
 */
public interface ObjectiveCard {
    int countPoints(Player player);
    String getName();
    String getDescription();
}