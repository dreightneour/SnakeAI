package snake;

import java.util.Comparator;

public class SpeciesComparator implements Comparator<Species>
{

	@Override
	public int compare(Species o1, Species o2) {
		
		int one = 0;
		int two = 0;
		int disjoint = 0;
		//int excess = 0;
		double weights = 0.0;
		//boolean disjointOne = true;
		
		
		int n = o1.getConnections().size();
		int nn = o2.getConnections().size();
		

		int holder = 0;
		
		if(n > nn)
		{
			
			for(int i = 0; i < n; i++)
			{
				int k = holder;
				//int itt = 0;
				boolean found = false;
				if(nn > 0)
				{
					
				
					while(o2.getConnections().get(k).getOut() <=  o1.getConnections().get(i).getOut() && k < nn && found == false)
					{
						
						if(o2.getConnections().get(k).getOut() ==  o1.getConnections().get(i).getOut() && o2.getConnections().get(k).getIn() ==  o1.getConnections().get(i).getIn())
						{
							found = true;
							break;
						}
						k++;
						if(k == nn)
						{
							break;
						}
					}
					if(found == true)
					{
						one++;
						two++;

						weights = weights + Math.abs(o1.getConnections().get(i).getWeight() - o2.getConnections().get(k).getWeight());
						if(weights != 0)
						{
							//int b = 0;
						}
						holder = k;
						
					}
					else
					{
						one++;
						disjoint++;
					}
				}

			}				
			if(two < o2.getConnections().size())
			{
				disjoint += (o2.getConnections().size() - two);
			}
		}
		else
		{
			for(int i = 0; i < nn; i++)
			{
				int k = holder;
				//int itt = 0;
				boolean found = false;
				if(n > 0)
				{
					
					while(o1.getConnections().get(k).getOut() <=  o2.getConnections().get(i).getOut() && k < n && found == false)
					{
						
						if(o1.getConnections().get(k).getOut() ==  o2.getConnections().get(i).getOut() && o1.getConnections().get(k).getIn() ==  o2.getConnections().get(i).getIn())
						{
							found = true;
							break;
						}
						k++;
						if( k == n)
						{
							break;
						}
						
					}
					if(found == true)
					{
						one++;
						two++;
						weights = weights + Math.abs(o2.getConnections().get(i).getWeight() - o1.getConnections().get(k).getWeight());
						if(weights != 0)
						{
							//int b = 0;
						}
						holder = k;

						
					}
					else
					{
						two++;
						disjoint++;
					}
				}

			}				
			if(one < o1.getConnections().size())
			{
				disjoint += (o1.getConnections().size() - one);
			}
			n = nn;
		}
		
		if(n == 0)
		{
			n = 1;
		}
		double total = disjoint / n + weights * .4;

		if(total <= 1.5)
		{
			return 1;
		}
		return -1;
		//return 0;
	}
	
}

