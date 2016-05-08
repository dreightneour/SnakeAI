package snake;

import java.util.Comparator;

public class CustomComparatorSpecies2 implements Comparator<Species>
{

	public int compare(Species o1, Species o2) {
		// TODO Auto-generated method stub
		return o1.getName() - o2.getName();
		
		//return 0;
	}
	
}
