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
import javax.swing.JButton;

public class GobbleModel extends JPanel {

	private static final int GAP = 10;
	private static final int W = 50;
	private static final int H = 50;
	
	public static final int R = 4;
	public static final int C = 4;
	private GobbleBoard  board; 
	private SpotButton spotButton [][];
	public  GobbleModel(GobbleBoard board){
		super();
		this.board = board;
		Dimension dim = new Dimension(W*GobbleBoard.Cols, W*GobbleBoard.Rows);
		setMinimumSize(dim);
		setPreferredSize(dim);
		setMaximumSize(dim);
		spotButton = new SpotButton[R][C];

	
	}
	public int clickToRow(MouseEvent e){
		System.out.print(e.getY());
		System.out.print("\n");
		return e.getY()/H;
	}
	public int clickToColum(MouseEvent e){
		System.out.print(e.getX());
		System.out.print("\n");
		return e.getX()/W;
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D  g2d =(Graphics2D) g.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Ellipse2D.Double ellipse = new Ellipse2D.Double();
		ellipse.width = 64;
		ellipse.height = 64;
	

		synchronized(board){
			for (int i=0; i<R; i++){
				for(int j=0; j<C; j++){
					SpotButton spot = spotButton[i][j] = new SpotButton();
					spot.setVisible(true);
				}
			}
		}
		
	}

}