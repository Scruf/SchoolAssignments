/*
	encapsulate state of the Gobble Game
*/

import java.awt.Color;


public class GobbleBoard {
	public static final int Rows = 4;
	public static final int Cols = 5;
	public Color spot[][] = new Color[Rows][Cols];

	public GobbleBoard(){}
	public synchronized void setSpot(int row, int col , Color color){
		spot[row][col] = color;
	}
	public synchronized Color getSport(int row,int col, Color color){
		return spot[row][col];
	}
}