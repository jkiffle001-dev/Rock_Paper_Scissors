import javax.swing.JFrame;

/**
 * RockPaperScissorsRunner is the main class. It creates the game window
 * and makes it visible.
 */
public class RockPaperScissorsRunner
{
    /**
     * Program entry point.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        RockPaperScissorsFrame frame = new RockPaperScissorsFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
