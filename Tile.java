import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.scene.layout.*;

public class Tile{

	int xcoor;
	int ycoor;

	public Tile(){
		Rectangle tile = new Rectangle();
		Text value = new Text();
		
		tile.setWidth(50);
		tile.setHeight(50);
	}
	
	public void setTileCoor(int xcoor, int ycoor){
		this.setX(xcoor);
		this.setY(ycoor);
		System.out.println("set" + xcoor + " and " + ycoor);
	}
}