package snake;

public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setX(int xIsh)
	{
		x = xIsh;
	}
	
	public void setY(int yIsh)
	{
		y = yIsh;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
