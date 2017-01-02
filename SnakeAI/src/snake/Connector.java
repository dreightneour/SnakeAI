package snake;

//import java.util.Random;

public class Connector ///implements java.lang.Comparable
{
	private int in;
	private int out;
	private double weight;
	private boolean enable;
	
	//Constructor
	public Connector(int in, int out, double weight)
	{
		this.in = in;
		this.weight = weight;
		this.out = out;
		//Random random = new Random();
		//int rand = random.nextInt(2);
		enable = true;
	}
	
	//getters
	public boolean getEnable()
	{
		return enable;
	}
	
	public int getIn()
	{
		return in;
	}
	
	public int getOut()
	{
		return out;
	}
	
	public double getWeight()
	{
		return weight;
	}
	
	//setters
	public void setIn(int in)
	{
		this.in = in;
	}
	
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
	
	public void setOut(int out)
	{
		this.out = out;
	}
	
	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}
	
	//comparer
	public int compareTo(Object o) 
	{
		Connector connect1 = (Connector) o;
		if(out > connect1.out)
		{
			if(in > connect1.in)
			{
				return 1;
			}
			else
			{
				return -1;
			}
				
		}
		else if(out == connect1.out)
		{
			if(in > connect1.in)
			{
				return 1;
			}
			else if(in == connect1.in)
			{
				return 0;
			}
			else
			{
				return -1;
			}
			
		}
		else
		{
			return -1;
		}
			
	}

}
