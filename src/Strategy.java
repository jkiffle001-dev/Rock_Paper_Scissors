/**
 * Strategy is the interface for every way the computer can choose its move
 * (the Strategy Design Pattern). Each strategy returns "R", "P" or "S".
 */
public interface Strategy
{
    /**
     * Picks the computer's move.
     *
     * @param playerMove the player's move this round: "R", "P" or "S"
     * @return the computer's move: "R", "P" or "S"
     */
    public String getMove(String playerMove);
}
