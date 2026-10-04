import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Random;

/**
 * RockPaperScissorsFrame is the GUI for the Rock Paper Scissors game.
 * The player clicks Rock, Paper or Scissors; the computer picks its move
 * with one of five Strategy objects chosen by probability:
 * <ul>
 *   <li>1 - 10: Cheat (external class)</li>
 *   <li>11 - 30: Least Used (inner class)</li>
 *   <li>31 - 50: Most Used (inner class)</li>
 *   <li>51 - 70: Last Used (inner class)</li>
 *   <li>71 - 100: Random (external class)</li>
 * </ul>
 * Least Used, Most Used and Last Used are inner classes because they need
 * the player's move history that this frame keeps.
 */
public class RockPaperScissorsFrame extends JFrame
{
    // player move history used by the inner-class strategies
    private int playerRockCnt = 0;
    private int playerPaperCnt = 0;
    private int playerScissorsCnt = 0;
    private String lastPlayerMove = "";

    // running totals for the stats panel
    private int playerWins = 0;
    private int computerWins = 0;
    private int ties = 0;
    private int gameNum = 0;

    private final Random rnd = new Random();

    // one instance of each strategy
    private final Strategy cheat = new Cheat();
    private final Strategy random = new RandomStrategy();
    private final Strategy leastUsed = new LeastUsed();
    private final Strategy mostUsed = new MostUsed();
    private final Strategy lastUsed = new LastUsed();

    private JButton rockBtn;
    private JButton paperBtn;
    private JButton scissorsBtn;
    private JButton quitBtn;

    private JTextField playerWinsTF;
    private JTextField computerWinsTF;
    private JTextField tiesTF;

    private JTextArea resultsTA;

    private final Font titleFont = new Font("SansSerif", Font.BOLD, 30);
    private final Font buttonFont = new Font("SansSerif", Font.BOLD, 16);
    private final Font statsFont = new Font("SansSerif", Font.BOLD, 18);
    private final Font resultsFont = new Font("Monospaced", Font.PLAIN, 16);

    /**
     * Builds the game window: title, button panel, stats panel and the
     * scrolling results area.
     */
    public RockPaperScissorsFrame()
    {
        super("Rock Paper Scissors Game");

        JPanel mainPnl = new JPanel(new BorderLayout(10, 10));
        mainPnl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPnl = new JPanel(new BorderLayout(10, 10));
        JLabel titleLbl = new JLabel("Rock Paper Scissors", SwingConstants.CENTER);
        titleLbl.setFont(titleFont);
        topPnl.add(titleLbl, BorderLayout.NORTH);
        topPnl.add(createButtonPanel(), BorderLayout.CENTER);
        topPnl.add(createStatsPanel(), BorderLayout.SOUTH);

        mainPnl.add(topPnl, BorderLayout.NORTH);
        mainPnl.add(createResultsPanel(), BorderLayout.CENTER);

        add(mainPnl);
        setSize(820, 760);
        setLocationRelativeTo(null);
    }

    /**
     * Creates the panel with the Rock, Paper, Scissors and Quit buttons.
     * One ActionListener is shared by the three game buttons.
     *
     * @return the button panel
     */
    private JPanel createButtonPanel()
    {
        JPanel buttonPnl = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPnl.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 2), "Choose your move"));

        rockBtn = makeButton("Rock", "rock.png");
        paperBtn = makeButton("Paper", "paper.png");
        scissorsBtn = makeButton("Scissors", "scissors.png");
        quitBtn = makeButton("Quit", "quit.png");

        ActionListener moveListener = new MoveListener();
        rockBtn.addActionListener(moveListener);
        paperBtn.addActionListener(moveListener);
        scissorsBtn.addActionListener(moveListener);

        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));

        buttonPnl.add(rockBtn);
        buttonPnl.add(paperBtn);
        buttonPnl.add(scissorsBtn);
        buttonPnl.add(quitBtn);
        return buttonPnl;
    }

    /**
     * Makes a button with a label under an image loaded from the src folder.
     *
     * @param text      the button text
     * @param imageFile the image file name in src
     * @return the new button
     */
    private JButton makeButton(String text, String imageFile)
    {
        JButton btn = new JButton(text);
        URL url = getClass().getResource("/" + imageFile);
        if (url != null)
        {
            Image img = new ImageIcon(url).getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            btn.setIcon(new ImageIcon(img));
        }
        btn.setFont(buttonFont);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Creates the stats panel: player wins, computer wins and ties in
     * read-only text fields.
     *
     * @return the stats panel
     */
    private JPanel createStatsPanel()
    {
        JPanel statsPnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        statsPnl.setBorder(BorderFactory.createTitledBorder("Stats"));

        playerWinsTF = makeStatField();
        computerWinsTF = makeStatField();
        tiesTF = makeStatField();

        statsPnl.add(makeStatLabel("Player Wins:"));
        statsPnl.add(playerWinsTF);
        statsPnl.add(makeStatLabel("Computer Wins:"));
        statsPnl.add(computerWinsTF);
        statsPnl.add(makeStatLabel("Ties:"));
        statsPnl.add(tiesTF);
        return statsPnl;
    }

    private JLabel makeStatLabel(String text)
    {
        JLabel lbl = new JLabel(text, SwingConstants.RIGHT);
        lbl.setFont(statsFont);
        return lbl;
    }

    private JTextField makeStatField()
    {
        JTextField tf = new JTextField("0", 4);
        tf.setEditable(false);
        tf.setFont(statsFont);
        tf.setHorizontalAlignment(JTextField.CENTER);
        return tf;
    }

    /**
     * Creates the results panel: a JTextArea in a JScrollPane that gets one
     * line appended for every game played this session.
     *
     * @return the results panel
     */
    private JPanel createResultsPanel()
    {
        JPanel resultsPnl = new JPanel(new BorderLayout());
        resultsPnl.setBorder(BorderFactory.createTitledBorder("Results"));
        resultsTA = new JTextArea(12, 50);
        resultsTA.setEditable(false);
        resultsTA.setFont(resultsFont);
        resultsPnl.add(new JScrollPane(resultsTA), BorderLayout.CENTER);
        return resultsPnl;
    }

    /**
     * The single listener for the Rock, Paper and Scissors buttons. It uses
     * the ActionEvent source to find the player's move, then plays a game.
     */
    private class MoveListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae)
        {
            String playerMove;
            if (ae.getSource() == rockBtn)
            {
                playerMove = "R";
            }
            else if (ae.getSource() == paperBtn)
            {
                playerMove = "P";
            }
            else
            {
                playerMove = "S";
            }
            playGame(playerMove);
        }
    }

    /**
     * Plays one game: picks a strategy by probability, gets the computer's
     * move, decides the winner, updates the stats and appends the result.
     * The player's move is recorded after the computer has chosen, so the
     * history strategies only see earlier rounds.
     *
     * @param playerMove "R", "P" or "S"
     */
    private void playGame(String playerMove)
    {
        int prob = rnd.nextInt(100) + 1;   // 1 - 100 inclusive
        Strategy strategy;
        String strategyName;

        if (prob <= 10)
        {
            strategy = cheat;
            strategyName = "Cheat";
        }
        else if (prob <= 30)
        {
            strategy = leastUsed;
            strategyName = "Least Used";
        }
        else if (prob <= 50)
        {
            strategy = mostUsed;
            strategyName = "Most Used";
        }
        else if (prob <= 70 && !lastPlayerMove.isEmpty())
        {
            strategy = lastUsed;
            strategyName = "Last Used";
        }
        else
        {
            // 71 - 100, or Last Used on the very first round (no last move yet)
            strategy = random;
            strategyName = "Random";
        }

        String computerMove = strategy.getMove(playerMove);
        recordPlayerMove(playerMove);
        gameNum++;

        String result;
        if (playerMove.equals(computerMove))
        {
            ties++;
            result = name(playerMove) + " vs. " + name(computerMove) + " (Tie! Computer: " + strategyName + ")";
        }
        else if (beats(playerMove, computerMove))
        {
            playerWins++;
            result = describe(playerMove, computerMove) + " (Player wins! Computer: " + strategyName + ")";
        }
        else
        {
            computerWins++;
            result = describe(computerMove, playerMove) + " (Computer wins! Computer: " + strategyName + ")";
        }

        resultsTA.append(String.format("Game %2d: %s%n", gameNum, result));
        resultsTA.setCaretPosition(resultsTA.getDocument().getLength());

        playerWinsTF.setText(String.valueOf(playerWins));
        computerWinsTF.setText(String.valueOf(computerWins));
        tiesTF.setText(String.valueOf(ties));
    }

    /**
     * Updates the player's move counts and last move.
     *
     * @param playerMove "R", "P" or "S"
     */
    private void recordPlayerMove(String playerMove)
    {
        switch (playerMove)
        {
            case "R":
                playerRockCnt++;
                break;
            case "P":
                playerPaperCnt++;
                break;
            default:
                playerScissorsCnt++;
                break;
        }
        lastPlayerMove = playerMove;
    }

    /**
     * @return true if move a beats move b
     */
    private static boolean beats(String a, String b)
    {
        return (a.equals("R") && b.equals("S"))
                || (a.equals("P") && b.equals("R"))
                || (a.equals("S") && b.equals("P"));
    }

    /**
     * @param move "R", "P" or "S"
     * @return the move that beats it
     */
    private static String beaterOf(String move)
    {
        switch (move)
        {
            case "R":
                return "P";
            case "P":
                return "S";
            default:
                return "R";
        }
    }

    /**
     * @return the full name of a move, e.g. "Rock"
     */
    private static String name(String move)
    {
        switch (move)
        {
            case "R":
                return "Rock";
            case "P":
                return "Paper";
            default:
                return "Scissors";
        }
    }

    /**
     * Builds the sentence for a winning move, e.g. "Rock breaks Scissors".
     *
     * @param winner the winning move
     * @param loser  the losing move
     * @return the description
     */
    private static String describe(String winner, String loser)
    {
        String verb;
        switch (winner)
        {
            case "R":
                verb = " breaks ";
                break;
            case "P":
                verb = " covers ";
                break;
            default:
                verb = " cuts ";
                break;
        }
        return name(winner) + verb + name(loser);
    }

    /**
     * Picks one of the moves whose count equals the target; ties between
     * equal counts are broken at random.
     */
    private String pickByCount(boolean least)
    {
        int[] counts = {playerRockCnt, playerPaperCnt, playerScissorsCnt};
        String[] moves = {"R", "P", "S"};
        int target = counts[0];
        for (int c : counts)
        {
            target = least ? Math.min(target, c) : Math.max(target, c);
        }
        String picked;
        do
        {
            int i = rnd.nextInt(3);
            picked = (counts[i] == target) ? moves[i] : null;
        } while (picked == null);
        return picked;
    }

    /**
     * Least Used (inner class): assumes the player will next throw the
     * symbol they have used the least, and plays the symbol that beats it.
     */
    private class LeastUsed implements Strategy
    {
        @Override
        public String getMove(String playerMove)
        {
            return beaterOf(pickByCount(true));
        }
    }

    /**
     * Most Used (inner class): assumes the player will throw the symbol
     * they use the most, and plays the symbol that beats it.
     */
    private class MostUsed implements Strategy
    {
        @Override
        public String getMove(String playerMove)
        {
            return beaterOf(pickByCount(false));
        }
    }

    /**
     * Last Used (inner class): plays the symbol the player used on the last
     * round (tit-for-tat). Never used on the first round.
     */
    private class LastUsed implements Strategy
    {
        @Override
        public String getMove(String playerMove)
        {
            return lastPlayerMove;
        }
    }
}
