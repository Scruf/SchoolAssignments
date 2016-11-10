import java.awt.Color;

public class GobbleBoard {
	SpotButton [][] spot;
	GobbleBoard(SpotButton [][] spots){
		this.spot = spots;
	}
	public void setSpot(int r,int c, Color color){
		spot[r][c].setColor(color);
	}
}