import javax.swing.*;
import java.awt.*;

/**
 * Class GobbleUI provides the view object for the network Gobble game.
 *
 * @author  Alan Kaminsky
 * @version 10-Oct-2016
 */
public class GobbleUI
{
    public static final int R = 4;
    public static final int C = 4;

    private JFrame frame;
    private SpotButton[][] spotButton;
    private JButton newGameButton;
    private JTextField messageField;

    private static final int GAP = 10;
    private static final int W = 50;
    private static final int H = 50;
    private static final Dimension PD = new Dimension (C*W, R*H);

    /**
     * Construct a new Gobble UI object.
     *
     * @param  name  Player's name.
     */
    private GobbleUI
    (String name)
    {
        frame = new JFrame ("Gobble -- " + name);
        JPanel p1 = new JPanel();
        p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
        p1.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
        frame.add (p1);

        spotButton = new SpotButton [R] [C];
        JPanel p2 = new JPanel();
        p2.setMaximumSize (PD);
        p2.setMinimumSize (PD);
        p2.setPreferredSize (PD);
        p2.setLayout (new GridLayout (R, C));
        for (int r = 0; r < R; ++ r)
            for (int c = 0; c < C; ++ c)
            {
                SpotButton spot = spotButton[r][c] = new SpotButton();
                spot.setEnabled (false);
                p2.add (spot);
            }
        p1.add (p2);

        newGameButton = new JButton ("New Game");
        newGameButton.setAlignmentX (0.5f);
        newGameButton.setEnabled (false);
        p1.add (Box.createVerticalStrut (GAP));
        p1.add (newGameButton);

        messageField = new JTextField();
        messageField.setEditable (false);
        p1.add (Box.createVerticalStrut (GAP));
        p1.add (messageField);

        frame.pack();
        frame.setVisible (true);
    }

}