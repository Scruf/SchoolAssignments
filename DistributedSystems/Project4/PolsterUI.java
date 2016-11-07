import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * Class PollsterUI provides the graphical user interface for the pollster
 * program in the Poll System.
 *
 * @author  Alan Kaminsky
 * @version 05-Nov-2016
 */
public class PollsterUI
	{
	private static final int GAP = 10;

	private JFrame frame;
	private JTextField questionField;
	private JButton goButton;
	private JTextField agreeCountField;
	private JTextField disagreeCountField;

	/**
	 * Construct a new pollster UI object.
	 */
	private PollsterUI()
		{
		frame = new JFrame ("Pollster");

		JPanel p1 = new JPanel();
		p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
		p1.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		frame.add (p1);

		JPanel p2 = new JPanel();
		p2.setLayout (new BoxLayout (p2, BoxLayout.X_AXIS));
		p1.add (p2);

		questionField = new JTextField (40);
		p2.add (questionField);
		p2.add (Box.createHorizontalStrut (GAP));

		goButton = new JButton ("Go");
		p2.add (goButton);
		p1.add (Box.createVerticalStrut (GAP));

		JPanel p3 = new JPanel();
		p3.setLayout (new BoxLayout (p3, BoxLayout.X_AXIS));
		p1.add (p3);

		p3.add (new JLabel ("Agree:"));
		agreeCountField = new JTextField (5);
		agreeCountField.setEditable (false);
		Dimension d = agreeCountField.getPreferredSize();
		agreeCountField.setMinimumSize (d);
		agreeCountField.setMaximumSize (d);
		p3.add (agreeCountField);
		p3.add (Box.createHorizontalStrut (2*GAP));

		p3.add (new JLabel ("Disagree:"));
		disagreeCountField = new JTextField (5);
		disagreeCountField.setEditable (false);
		d = disagreeCountField.getPreferredSize();
		disagreeCountField.setMinimumSize (d);
		disagreeCountField.setMaximumSize (d);
		p3.add (disagreeCountField);

		frame.pack();
		frame.setVisible (true);
		}

	/**
	 * An object holding a reference to a pollster UI object.
	 */
	private static class PollsterUIRef
		{
		public PollsterUI ui;
		}

	/**
	 * Construct a new pollster UI object.
	 */
	public static PollsterUI create()
		{
		PollsterUIRef ref = new PollsterUIRef();
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				ref.ui = new PollsterUI();
				}
			});
		return ref.ui;
		}

	/**
	 * Execute the given runnable object on the Swing thread.
	 */
	private static void onSwingThreadDo
		(Runnable task)
		{
		try
			{
			SwingUtilities.invokeAndWait (task);
			}
		catch (Throwable exc)
			{
			exc.printStackTrace (System.err);
			System.exit (1);
			}
		}
	}
