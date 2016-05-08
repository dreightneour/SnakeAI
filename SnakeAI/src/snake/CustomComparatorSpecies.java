package snake;

import java.util.Comparator;

public class CustomComparatorSpecies implements Comparator<Species>
{

	public int compare(Species o1, Species o2) {
		// TODO Auto-generated method stub
		return o1.getMax() - o2.getMax();
		
		//return 0;
	}
	
}
