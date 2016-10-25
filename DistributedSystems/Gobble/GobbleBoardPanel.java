import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GobbleBoardPanel extends JPanel{
	public static final int ROWS = 4;

	public static final int COLS = 4;
	private static final int W = 24;
	private static final int H = 20;
 
	private GobbleBoard board;
	public GobbleBoardPanel(GobbleBoard board){
		super();
		this.board = board;
		Dimension dim = new Dimension(W*GobbleBoard.COLS, W*GobbleBoard.ROWS);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}

	public int clickToRow(MouseEvent e){
		return e.getY()/W;
	}
	public int clickToColom(MouseEvent e){
		return e.getX()/H;
	}

}