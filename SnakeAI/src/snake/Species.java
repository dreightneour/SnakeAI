package snake;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Species 
{
	private ArrayList<Connector> connection;
	private HistoryMarkers marks;
	private int fitness;
	//private double bias;
	private int genome;
	private int nodes;
	private ArrayList<Node> nodeAL;
	private int name;
	private int inputSize;
	private int outputSize;
	
	private double stale;
	
	Random random = new Random();
	
	public void initiate(HistoryMarkers mark, int name)
	{
		//random.setSeed(0);
		//marks = mark;
		this.name = name;
		connection = new ArrayList<Connector>();
		int thing;
		nodes = 0;
		nodeAL = new ArrayList<Node>();
		inputSize = 8;
		outputSize = 4;
		marks = mark;
		fitness = 0;
		
		stale = 0;
		
		for(int i = 0; i < 5; i++)
		{
			thing = random.nextInt(6);
			if(thing == 0)
			{
				i = 5;
			}
			else
			{
				boolean exists = true;
				while(exists == true)
				{
					
				
					int input = random.nextInt(8);
					int output = random.nextInt(4) + 1000;
					double weight = (random.nextDouble() * 2) - 1;
					exists = false;
					for(int j = 0; j < connection.size(); j++)
					{
						if(connection.get(j).getOut() == output && connection.get(j).getIn() == input)
						{
							//connection.add(new Connector(input, output, weight));
							//connection.get(connection.size() - 1).setEnable(true);
							exists = true;
							break;
						}
					}
					if(exists == false)
					{
						connection.add(new Connector(input, output, weight));
						connection.get(connection.size() - 1).setEnable(true);
					}
				}
				if(marks.checkExist(connection.get(i)) == false)
				{
					marks.add(connection.get(i));
				}
			}
		}
		
	}
	
	
	public void load(String loader, HistoryMarkers mark) throws IOException
	{
		
		connection = new ArrayList<Connector>();
		//int thing;
		nodes = 0;
		nodeAL = new ArrayList<Node>();
		inputSize = 8;
		outputSize = 4;
		marks = mark;
		fitness = 0;
		
		
		File f = new File(loader);
		String saveS = new String();//reader.read;
		if (f.exists()) {
			FileReader reader = new FileReader(f);
			//BufferedReader buffer = new BufferedReader(reader);
		    while (true){
		        int c = reader.read();
		
		        if (c < 0) {
		            break;
		        }
		
		        char ch = (char) c;
		        saveS = saveS + ch;
		
		    }
		
		    reader.close();
		    boolean start = false;
		    int starter = 0;
		    int ender = 0;
		    int item = 0;
		    int in = 0;
		    int out= 0;
		    double weight = 0;
		    for(int i= 0; i < saveS.length() - 1;i++)
		    {	
		    	if(!saveS.substring(i, i + 1).equals(" ") && !saveS.substring(i, i + 1).equals("\n"))
		    	{
		    		if(start == false)
		    		{
		    			starter = i;
		    			start = true;
		    		    for(int j= i; j < saveS.length() - 1;j++)
		    		    {	
		    		    	if(saveS.substring(j, j + 1).equals(" ") || saveS.substring(j, j + 1).equals("\n"))
		    		    	{
		    		    		ender = j;
				    			if(item == 0)
				    			{
				    				in = Integer.parseInt(saveS.substring(starter, ender));
				    				item++;
				    				start = false;
				    				i = j;
				    				break;
				    				
				    			}
				    			else if(item == 1)
				    			{
				    				out = Integer.parseInt(saveS.substring(starter, ender));
				    				item++;
				    				start = false;
				    				i = j;
				    				break;
				    				
				    			}
				    			else if(item == 2)
				    			{
				    				weight = Double.parseDouble(saveS.substring(starter, ender));
				    				item = 0;
				    				start = false;
				    				connection.add(new Connector(in, out, weight));
				    				i = j;
				    				if(in > inputSize - 1 || out < 1000)
				    				{
				    					nodes++;
				    					nodeAL.add(new Node());
				    				}
				    				break;
				    			}
		    		    	}
		    		    }
		    		}
		    	}
		    }
		    	
		} 
	}
	
	public double[] run(double[] inputs, HistoryMarkers mark)
	{
		double output[] = new double[4];
		
		//ArrayList.sort(connection, new CustomComparator())
		Collections.sort(connection, new CustomComparator());
		//connection.sort();

		double current = 0;
		for(int i = 0; i < connection.size(); i++)
		{
			if(connection.get(i).getEnable())
			{
				if(i + 1 < connection.size())
				{
					if(connection.get(i).getOut() == connection.get(i + 1).getOut())
					{
						if(connection.get(i).getIn() < inputSize)
						{
							current += inputs[connection.get(i).getIn()] * connection.get(i).getWeight();
						}
						else
						{
							current += nodeAL.get(connection.get(i).getIn() - inputSize).getWeight() * connection.get(i).getWeight();
						}
					}
					else if(current > 0)
					{
						if(connection.get(i).getIn() < inputSize)
						{
							current += inputs[connection.get(i).getIn()] * connection.get(i).getWeight();
						}
						else
						{
							
							current += nodeAL.get(connection.get(i).getIn() - inputSize).getWeight() * connection.get(i).getWeight();
						}
						if(connection.get(i).getOut() >= 1000)
						{
							output[connection.get(i).getOut() - 1000] = current;
						}
						else
						{
							if(connection.get(i).getIn() < inputSize)
							{
								nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(inputs[connection.get(i).getIn()] * connection.get(i).getWeight());
							}
							else
							{
								nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(nodeAL.get(connection.get(i).getIn() - inputSize).getWeight() * connection.get(i).getWeight());
							}
							
							nodeAL.get(connection.get(i).getOut() - inputSize).sigmoid();
						}
						
						current = 0;
					}
					else
					{
						if(connection.get(i).getOut() >= 1000)
						{
							// = inputs[connection.get(i).getIn()] * connection.get(i).getWeight();
							if(connection.get(i).getIn() < inputSize)
							{
								output[connection.get(i).getOut() - 1000] += inputs[connection.get(i).getIn()] * connection.get(i).getWeight();
							}
							else
							{
								output[connection.get(i).getOut() - 1000] += nodeAL.get(connection.get(i).getIn() - inputSize).getWeight() * connection.get(i).getWeight();
							}
						}
						else
						{
							if(connection.get(i).getIn() < inputSize)
							{
								/*if(connection.get(i).getOut() - inputSize < 0)
								{
									int b = 0;
									b=b + 1;
								}*/
								nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(inputs[connection.get(i).getIn()] * connection.get(i).getWeight());
							}
							else
							{
								nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(nodeAL.get(connection.get(i).getIn() - inputSize).getWeight() * connection.get(i).getWeight());
							}
							nodeAL.get(connection.get(i).getOut() - inputSize).sigmoid();// = 
						}
						
					}
				}
				else
				{
					if(current > 0)
					{
						if(connection.get(i).getIn() < inputSize)
						{
							current += inputs[connection.get(i).getIn()] * connection.get(i).getWeight();
						}
						else
						{
							current += nodeAL.get(connection.get(i).getIn() - 8).getWeight() * connection.get(i).getWeight();
						}
						
						if(connection.get(i).getOut() >= 1000)
						{
							output[connection.get(i).getOut() - 1000] = current;
						}
						else
						{
							nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(inputs[connection.get(i).getIn()] * connection.get(i).getWeight());
							nodeAL.get(connection.get(i).getOut() - inputSize).sigmoid();
						}
						
						current = 0;
					}
					else
					{
						if(connection.get(i).getOut() >= 1000)
						{
							if(connection.get(i).getIn() < inputSize)
							{
								output[connection.get(i).getOut() - 1000] = inputs[connection.get(i).getIn()] * connection.get(i).getWeight();
							}
							else
							{
								output[connection.get(i).getOut() - 1000] = nodeAL.get(connection.get(i).getIn() - 8).getWeight() * connection.get(i).getWeight();
							}
							
						}
						else
						{
							if(connection.get(i).getIn() < inputSize)
							{
								nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(inputs[connection.get(i).getIn()] * connection.get(i).getWeight());
							}
							else
							{
								nodeAL.get(connection.get(i).getOut() - inputSize).setWeight(nodeAL.get(connection.get(i).getIn() - 8).getWeight() * connection.get(i).getWeight());
							}
							
							nodeAL.get(connection.get(i).getOut() - inputSize).sigmoid();// = 
						}
						
					}
				}
			}
		}
		
		for(int i = 0; i < 4; i++)
		{
			output[i] = 1/(1 + Math.pow(Math.E, -4.9 * output[i]));
		}
		
		return output;
	}
	
	public void clear()
	{
		connection.clear();
		nodes = 0;
		fitness = 0;
		nodeAL.clear();
	}
	
	public void mutate(HistoryMarkers mark)
	{
		int ran = random.nextInt(100);
		
		if(ran < 80)
		{
			for(int i = 0; i < connection.size(); i++)
			{	
				int ran4 = random.nextInt(2);
				if(ran4 == 0)
				{
					int ran2 = random.nextInt(10);
					if(ran2 < 9)
					{	
						double ran3;
	
							ran3 = (random.nextDouble() / 2) - .25;
							connection.get(i).setWeight(connection.get(i).getWeight() + ran3); 
						}
					
					else
					{
						double ran3;
						ran3 = random.nextDouble();
						connection.get(i).setWeight(ran3); 
	
					}	
				}
				
			}
		}
		else if(ran < 83)
		{
			//System.exit(0);
			if(connection.size() > 0)
			{
				int ran2 = random.nextInt(connection.size());
				Connector connection2 = new Connector(connection.get(ran2).getIn(), connection.get(ran2).getOut(), connection.get(ran2).getWeight());
				connection.get(ran2).setEnable(false);
				Connector connect1 = new Connector(inputSize + nodes, connection2.getOut(), connection2.getWeight());
				connection.add(connect1);
				Connector connect2 = new Connector(connection2.getIn(), inputSize + nodes, 1.0);
				connection.add(connect2);
				nodeAL.add(new Node());
				nodes++;
				
				if(marks.checkExist(connect1) == false)
				{
					marks.add(connect1);
				}
				
				if(marks.checkExist(connect2) == false)
				{
					marks.add(connect2);
				}
			}
			
			
		}
		else if(ran < 88)
		{
			//System.exit(0);
			int ran2 = 0;
			int ran3 = 0;
			boolean exists = false;
			Connector connect = new Connector(ran2, ran3, 1.0); 
			//int k = 0;
			if(connection.size() < inputSize * outputSize + (inputSize * nodes + outputSize * nodes))
			{
				
				while(ran2 >= ran3 || exists == true || ran3 <= 0)
				{
					//k++;
					exists = false;
					ran2 = random.nextInt(inputSize + nodes);
					ran3 = random.nextInt(outputSize + nodes);
					if(ran3 < outputSize)
					{
						ran3 += 1000;
					}
					else
					{
						ran3 = ran3 + 4;
					}
					connect = new Connector(ran2, ran3, random.nextDouble() * 2 - 1);
					for(int i = 0; i < connection.size(); i++)
					{
						if(connect.getOut() == connection.get(i).getOut() && connect.getIn() == connection.get(i).getIn())
						{
							exists = true;
							break;
						}
					}
				}			
				connection.add(connect);
				if(marks.checkExist(connect) == false)
				{
					marks.add(connect);
				}
			}

			
		}
			
	}
	
	public void genetic(Species species1, Species species2, HistoryMarkers mark)
	{
		clear();
		marks = mark;
		if(species2.getMax() >= species1.getMax() && species1.getConnections().size() == 0)
		{
			Species hold = new Species();
			hold = species1;
			species1 = species2;
			species2 = hold;
		}
		int [] numbers1 = new int[species1.getConnections().size()];
		int [] numbers2 = new int[species2.getConnections().size()];
		ArrayList<Integer> nodesArr = new ArrayList<Integer>();
		int num1 = 0;
		int num2 = 0;
		//nodes = species1.getNodes();
		//if(nodes < species2.getNodes())
		//{
		//	nodes = species2.getNodes();
		//}
		for(int i = 0; i < marks.size(); i ++)
		{
			for(int j = 0; j < species1.getConnections().size(); j++)
			{
				if(species1.getConnections().get(j).getOut() == marks.get(i).getOut() && species1.getConnections().get(j).getIn() == marks.get(i).getIn())
				{
					numbers1[num1] = i;
					num1++;
					break;
				}
			}
			
			
			for(int j = 0; j < species2.getConnections().size(); j++)
			{
				if(species2.getConnections().get(j).getOut() == marks.get(i).getOut() && species2.getConnections().get(j).getIn() == marks.get(i).getIn())
				{
					numbers2[num2] = i;
					num2++;
					break;
				}
			}
		}
		
		//if(numbers1[species1.getConnections().size() - 1] >= numbers2[species2.getConnections().size() - 1])
		//{
			num1 = 0;
			num2 = 0;
			boolean there = false;
			boolean over = false;
			int higher = 0;
			/*if(species2.getConnections().size() > 0 && species1.getConnections().size() > 0)
			{
				higher = numbers1[species1.getConnections().size() - 1];
			
				if(numbers2[species2.getConnections().size() - 1] > numbers1[species1.getConnections().size() - 1])
				{
					higher = numbers2[species2.getConnections().size() - 1];
				}
			}*/
			if(species1.getConnections().size() > 0)
			{
				higher = numbers1[species1.getConnections().size() - 1];
			}
			if(numbers2.length <= 0)
			{
				
				numbers2 = new int[1];
				numbers2[0] = -1;
			}
			if(higher > 0)
			{
				for(int i = 0; i <= higher; i++)//i < numbers1[species1.getConnections().size() - 1]; i ++)
				{		
					if(num2 == numbers2.length && numbers2.length > 0)
					{
						num2--;
					}
					if(num1 > numbers1.length)
					{
						num1--;
					}
					if(numbers1[num1] == i)
					{
						there  = true;
	
						num1++;					
						
					}
					
					if(numbers2[num2] == i)
					{
						over = true;
						num2++;
	
					}
					
					if(there == true)
					{
						if(over == true)
						{
							int b = random.nextInt(3);
							if(b < 2)
							{
								//connection.add(species1.getConnections().get())
								for(int j = 0; j < species1.getConnections().size(); j++)
								{
									if(species1.getConnections().get(j).getOut() == (mark.get(numbers1[num1 - 1])).getOut() &&species1.getConnections().get(j).getIn() == (mark.get(numbers1[num1 - 1])).getIn() )
									{
										connection.add(new Connector(species1.getConnections().get(j).getIn(), species1.getConnections().get(j).getOut(), species1.getConnections().get(j).getWeight()));
										if(species1.getConnections().get(j).getIn() >= inputSize && species1.getConnections().get(j).getIn() < 1000)
										{
											if(!nodesArr.contains(species1.getConnections().get(j).getIn()))
											{
												nodesArr.add(species1.getConnections().get(j).getIn());
											}
										}
										if(species1.getConnections().get(j).getOut() >= inputSize && species1.getConnections().get(j).getOut() < 1000)
										{
											if(!nodesArr.contains(species1.getConnections().get(j).getOut()))
											{
												nodesArr.add(species1.getConnections().get(j).getOut());
											}
										}
										break;
									}
								}
							}
							else
							{
								for(int j = 0; j < species2.getConnections().size(); j++)
								{
									if(species2.getConnections().get(j).getOut() == mark.get(numbers2[num2 - 1]).getOut() && species2.getConnections().get(j).getIn() == mark.get(numbers2[num2 - 1]).getIn())
									{
										connection.add(new Connector(species2.getConnections().get(j).getIn(), species2.getConnections().get(j).getOut(), species2.getConnections().get(j).getWeight()));
										if(species2.getConnections().get(j).getIn() >= inputSize && species2.getConnections().get(j).getIn() < 1000)
										{
											if(!nodesArr.contains(species2.getConnections().get(j).getIn()))
											{
												nodesArr.add(species2.getConnections().get(j).getIn());
											}
										}
										if(species2.getConnections().get(j).getOut() >= inputSize && species2.getConnections().get(j).getOut() < 1000)
										{
											if(!nodesArr.contains(species2.getConnections().get(j).getOut()))
											{
												nodesArr.add(species2.getConnections().get(j).getOut());
											}
										}
										break;
									}
								}
							}
						}
						else
						{
							for(int j = 0; j < species1.getConnections().size(); j++)
							{
								if(species1.getConnections().get(j).getOut() == (mark.get(numbers1[num1 - 1])).getOut() &&species1.getConnections().get(j).getIn() == (mark.get(numbers1[num1 - 1])).getIn())
								{
									connection.add(new Connector(species1.getConnections().get(j).getIn(), species1.getConnections().get(j).getOut(), species1.getConnections().get(j).getWeight()));
									if(species1.getConnections().get(j).getIn() >= inputSize && species1.getConnections().get(j).getIn() < 1000)
									{
										if(!nodesArr.contains(species1.getConnections().get(j).getIn()))
										{
											nodesArr.add(species1.getConnections().get(j).getIn());
										}
									}
									if(species1.getConnections().get(j).getOut() >= inputSize && species1.getConnections().get(j).getOut() < 1000)
									{
										if(!nodesArr.contains(species1.getConnections().get(j).getOut()))
										{
											nodesArr.add(species1.getConnections().get(j).getOut());
										}
									}
									break;
								}
							}
						}
					}
					/*else
					{
						if(over == true)
						{
							for(int j = 0; j < species2.getConnections().size(); j++)
							{
								if(species2.getConnections().get(j).getOut() == mark.get(numbers2[num2 - 1]).getOut() && species2.getConnections().get(j).getIn() == mark.get(numbers2[num2 - 1]).getIn())
								{
									connection.add(new Connector(species2.getConnections().get(j).getIn(), species2.getConnections().get(j).getOut(), species2.getConnections().get(j).getWeight()));
									if(species2.getConnections().get(j).getIn() >= inputSize && species2.getConnections().get(j).getIn() < 1000)
									{
										if(!nodesArr.contains(species2.getConnections().get(j).getIn()))
										{
											nodesArr.add(species2.getConnections().get(j).getIn());
										}
									}
									if(species2.getConnections().get(j).getOut() >= inputSize && species2.getConnections().get(j).getOut() < 1000)
									{
										if(!nodesArr.contains(species2.getConnections().get(j).getOut()))
										{
											nodesArr.add(species2.getConnections().get(j).getOut());
										}
									}
									break;
								}
							}
						}
					}
					*/
					over = false;
					there = false;
				}
			}
		//else
		//{
			
		//}'
			for(int i = 0; i < nodesArr.size(); i++)
			{
				nodeAL.add(new Node());
			}
			if(connection.size() == 0)
			{
				int b  = 0;
			}
			
			setStale(0);
		
	}
	
	public double compareGenome(Species o2) {
		
		int one = 0;
		int two = 0;
		int disjoint = 0;
		//int excess = 0;
		double weights = 0.0;
		//boolean disjointOne = true;
		
		
		int n = connection.size();
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
					
				
					while(o2.getConnections().get(k).getOut() <=  connection.get(i).getOut() && k < nn && found == false)
					{
						
						if(o2.getConnections().get(k).getOut() ==  connection.get(i).getOut() && o2.getConnections().get(k).getIn() ==  connection.get(i).getIn())
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

						weights = weights + Math.abs(connection.get(i).getWeight() - o2.getConnections().get(k).getWeight());
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
				else
				{
					disjoint = n;
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
					
					while(connection.get(k).getOut() <=  o2.getConnections().get(i).getOut() && k < n && found == false)
					{
						
						if(connection.get(k).getOut() ==  o2.getConnections().get(i).getOut() && connection.get(k).getIn() ==  o2.getConnections().get(i).getIn())
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
						weights = weights + Math.abs(o2.getConnections().get(i).getWeight() - connection.get(k).getWeight());
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
				else
				{
					disjoint = nn;
				}

			}				
			if(one < connection.size())
			{
				disjoint += (connection.size() - one);
			}
			n = nn;
		}
		
		if(n == 0)
		{
			n = 1;
		}
		double total = (double)disjoint / (double)n + (double)weights * 0.4;
		if(total == 0)
		{
			int b = 0;
		}
		return total;
		
		//return 0;
	}
	
	public ArrayList<Connector> getConnections()
	{
		return connection;	
	}
	
	public int getNodes()
	{
		return nodes;
	}
	
	public HistoryMarkers getMark()
	{
		return marks;
	}
	
	public void setMax(int fitness)
	{
		if(fitness > this.fitness)
		{
			this.fitness = fitness;
			setStale(0);
		}
		
		if(fitness == 0)
		{
			this.fitness = 0;
		}
		
	}
	
	public int getMax()
	{
		return fitness;
	}
	
	public void setStale(double staleness)
	{
		stale = staleness;
	}
	
	public double getStale()
	{
		return stale;
	}
	
	public void setGenome(int genome)
	{
		this.genome = genome;
	}
	
	public int getGenome()
	{
		return genome;
	}
	public int getName()
	{
		return name;
	}
	
}
