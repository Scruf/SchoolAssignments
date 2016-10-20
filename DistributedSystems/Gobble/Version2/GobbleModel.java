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

public class GobbleModel extends JPanel {

	private static final int GAP = 10;
	private static final int W = 50;
	private static final int H = 50;
	
	public static final int R = 4;
	public static final int C = 4;
	private GobbleBoard  board; 

	public  GobbleModel(GobbleBoard board){
		super();
		this.board = board;
		Dimension dim = new Dimension(W*C, H*R);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
	}
	public int clickToRow(MouseEvent e){
		return e.getY()/H;
	}
	public int clickToColum(MouseEvent e){
		return e.getX()/W;
	}
	protected void paintComponent(Graphics g){
		Graphics2D g2d =(Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	}

}