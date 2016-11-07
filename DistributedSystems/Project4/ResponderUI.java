import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

/**
 * Class ResponderUI provides the graphical user interface for the responder
 * program in the Poll System.
 *
 * @author  Alan Kaminsky
 * @version 05-Nov-2016
 */
public class ResponderUI
	{
	private static final int GAP = 10;

	private JFrame frame;
	private JTextField questionField;
	private ButtonGroup buttons;
	private JToggleButton agreeButton;
	private JToggleButton disagreeButton;

	/**
	 * Construct a new responder UI object.
	 */
	private ResponderUI()
		{
		frame = new JFrame ("Responder");

		JPanel p1 = new JPanel();
		p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
		p1.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		frame.add (p1);

		questionField = new JTextField (40);
		questionField.setEditable (false);
		p1.add (questionField);
		p1.add (Box.createVerticalStrut (GAP));

		JPanel p2 = new JPanel();
		p2.setLayout (new BoxLayout (p2, BoxLayout.X_AXIS));
		p1.add (p2);

		buttons = new ButtonGroup();
		agreeButton = new JToggleButton ("Agree", false);
		buttons.add (agreeButton);
		p2.add (agreeButton);

		disagreeButton = new JToggleButton ("Disagree", false);
		buttons.add (disagreeButton);
		p2.add (disagreeButton);

		frame.pack();
		frame.setVisible (true);
		}

	/**
	 * An object holding a reference to a responder UI object.
	 */
	private static class ResponderUIRef
		{
		public ResponderUI ui;
		}

	/**
	 * Construct a new responder UI object.
	 */
	public static ResponderUI create()
		{
		ResponderUIRef ref = new ResponderUIRef();
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				ref.ui = new ResponderUI();
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
