import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class SpotButton
	extends JPanel
	{
	private static final int L = 3;

	private Color color = Color.LIGHT_GRAY;
	
	private int row, column;

	/**
	 * Construct a new spot button object.
	 */
	public SpotButton()
		{
		}
	
	public SpotButton(int row, int column)
	{
		this.row = row;
		this.column = column;
	}

	/**
	 * Set this spot button's color.
	 *
	 * @param  color  Color.
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
	 * @param  listener  Action listener.
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

	public Color getColor() {
		return color;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	}
