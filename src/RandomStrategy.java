import java.util.Random;

/**
 * RandomStrategy is an external Strategy class that picks Rock, Paper or
 * Scissors at random, as in the normal game. (It is named RandomStrategy
 * so it does not clash with java.util.Random.)
 */
public class RandomStrategy implements Strategy
{
    private static final String[] MOVES = {"R", "P", "S"};
    private final Random rnd = new Random();

    /**
     * @param playerMove the player's move (not used by this strategy)
     * @return a random move: "R", "P" or "S"
     */
    @Override
    public String getMove(String playerMove)
    {
        return MOVES[rnd.nextInt(MOVES.length)];
    }
}
