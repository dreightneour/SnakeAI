package snake;

import java.util.Comparator;

public class CustomComparatorSpecies3 implements Comparator<Species>
{

	public int compare(Species o1, Species o2) {
		// TODO Auto-generated method stub
		return o1.getGenome() - o2.getGenome();
		
		//return 0;
	}
	
}
