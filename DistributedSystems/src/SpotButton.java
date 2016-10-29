import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

/**
 * The Class SpotButton.
 */
public class SpotButton
	extends JPanel
	{
	
	/** The Constant L. */
	private static final int L = 3;

	/** The color. */
	private Color color = Color.LIGHT_GRAY;
	
	/** The column. */
	private int row, column;

	/**
	 * Construct a new spot button object.
	 */
	public SpotButton()
		{
		}
	
	/**
	 * Instantiates a new spot button.
	 *
	 * @param row the row
	 * @param column the column
	 */
	public SpotButton(int row, int column)
	{
		this.row = row;
		this.column = column;
	}

	/**
	 * Set this spot button's color.
	 *
	 * @param color the new color
	 */
	public void setColor
		(Color color)
		{
		this.color = color;
		repaint();
		}

	/**
	 * Add the given action listener to this spot button.
	 *
	 * @param listener the listener
	 */
	public void addActionListener
		(ActionListener listener)
		{
		addMouseListener (new MouseAdapter()
			{
			public void mouseClicked (MouseEvent e)
				{
				if (isEnabled())
					listener.actionPerformed (new ActionEvent
						(SpotButton.this, ActionEvent.ACTION_PERFORMED, null));
				}
			});
		}

	/**
	 * Paint this component.
	 *
	 * @param g the g
	 */
	protected void paintComponent
		(Graphics g)
		{
		super.paintComponent (g);
		Graphics gg = g.create();
		int W = getWidth();
		int H = getHeight();
		if (isEnabled())
			{
			gg.setColor (Color.BLACK);
			gg.fillRect (0, 0, W, H);
			gg.setColor (color);
			gg.fillRect (L, L, W - 2*L, H - 2*L);
			}
		else
			{
			gg.setColor (color);
			gg.fillRect (0, 0, W, H);
			}
		}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Gets the row.
	 *
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the column.
	 *
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Sets the row.
	 *
	 * @param row the new row
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Sets the column.
	 *
	 * @param column the new column
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	}
