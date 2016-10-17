import java.awt.Color;
public class GobbleBoard{

public static final int ROWS = 4;

public static final int COLS = 4;

private Color [][] spot = new Color [ROWS][COLS];


public GobbleBoard(){}

	public synchronized Color getSpot(int x,int y)
      {
      	return spot[x][y];
      }
	public synchronized void setSpot(int x,int y,Color color)
      {
      	spot[x][y] = color;
      }
}