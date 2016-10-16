import java.awt.Color;
import java.io.IOException;


public interface ViewListener{
	public void addMarker(int x,int y,Color color) throws IOException;
}