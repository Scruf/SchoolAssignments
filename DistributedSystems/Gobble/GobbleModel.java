/*server encapsulation*/

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;



public class GobbleModel implements ViewListener{
	private GobbleBoard board = new GobbleBoard();
	private LinkedList<ModelListener> listeners = new LinkedList<ModelListener>();
	public GobbleModel(){}
	
	public synchronized void addMarker(int x, int y,Color color)throws IOException{

	}
}