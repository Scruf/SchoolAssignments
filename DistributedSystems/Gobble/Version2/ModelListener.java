import java.awt.Color;
import java.io.IOException;


public interface ModelListener{
	public void markAdded(int x,int y, Color color) throws IOException;
}