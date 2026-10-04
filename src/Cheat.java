/**
 * Cheat is an external Strategy class. It looks at the move the player
 * already made and picks the symbol that beats it. The game only uses this
 * strategy about 10% of the time.
 */
public class Cheat implements Strategy
{
    /**
     * @param playerMove the player's move: "R", "P" or "S"
     * @return the move that beats the player's move
     */
    @Override
    public String getMove(String playerMove)
    {
        String computerMove;
        switch (playerMove)
        {
            case "R":
                computerMove = "P";   // paper covers rock
                break;
            case "P":
                computerMove = "S";   // scissors cut paper
                break;
            case "S":
                computerMove = "R";   // rock breaks scissors
                break;
            default:
                computerMove = "X";
                break;
        }
        return computerMove;
    }
}
