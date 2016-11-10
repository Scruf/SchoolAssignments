import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GobbleBoardPanel extends JPanel{
	private GobbleBoard board;
	private static final int W = 50;
	private static final int H = 50;
	public GobbleBoardPanel(GobbleBoard board)
	{
		super();
		this.board = board;
		Dimension dim = new Dimension(W*4,W*4);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setBackground(new Color(0.8f,1.0f,0.8f));
	}
	public int clickToRow(MouseEvent e){
		return e.getY()/W;
	}
	public int clickToColumn(MouseEvent e){
		return e.getX()/W;

	}
}