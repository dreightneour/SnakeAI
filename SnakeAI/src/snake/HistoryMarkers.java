package snake;

import java.util.ArrayList;
//import java.util.HashMap;

public class HistoryMarkers 
{
	private ArrayList<Connector> markers = new ArrayList<Connector>();
	
	public boolean checkExist(Connector connect)
	{
		for(int i = 0; i < markers.size(); i++)
		{
			if(markers.get(i).getOut() == connect.getOut() && markers.get(i).getIn() == connect.getIn())
			{
				return true;
			}
		}		
		return false;
	}
	
	public void add(Connector connect)
	{
		markers.add(connect);
	}
	
	public Integer getMarker(Connector mark)
	{
		for(int i = 0; i < markers.size(); i++)
		{
			if(mark == markers.get(i))
			{
				return i;
			}
		}
		return -1;
	}
	
	public int size()
	{
		return markers.size();
	}
	
	public Connector get(int i)
	{
		return markers.get(i);
	}
	public HistoryMarkers()
	{
		//markers = new HashMap();
	}
}