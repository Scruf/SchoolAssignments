import java.awt.Color;
import java.io.IOException;



public class GobbleCloneModel implements ModelListener{

	private GobbleBoard board = new GobbleBoard();
	private ModelListener listener;

	public GobbleCloneModel(){}
	public GobbleBoard getBoard(){
		return board;
	}
	public void setModelListener(ModelListener model){
		this.listener = model;
	}
	public void markAdded(int row,int col, Color color) throws IOException{
		System.out.print("What is going on");
		board.setSpot(row,col,color);
		listener.markAdded(row,col,color);
	}
}