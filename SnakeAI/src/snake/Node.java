package snake;

public class Node 
{
	private double weight;
	
	public double getWeight()
	{
		return weight;
	}
	public void setWeight(double weight)
	{
		this.weight = weight;
	}
	public void sigmoid()
	{
		weight = 1/(1 + Math.pow(Math.E, -4.9 * weight));
	}
}
