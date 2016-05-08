package snake;

import java.util.Comparator;

public class CustomComparator implements Comparator<Connector>
{

	@Override
	public int compare(Connector o1, Connector o2) {
		// TODO Auto-generated method stub
		if(o1.getOut() == o2.getOut())
		{
			return o1.getIn() - o2.getIn();
		}
		return o1.getOut() - o2.getOut();
		
		//return 0;
	}
	
}
