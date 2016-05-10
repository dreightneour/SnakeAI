package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeAIPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final int WIDTH = 625;
	private static final int HEIGHT = 625;
	
	private double[] outputs;
	private double[] inputs;
	
	private ArrayList<Point> snake;
	private ArrayList<Point> snake2;
	
	private ArrayList<Species> species;
	
	private int snakeDirection;
	private int snakeOldDir;
	private int current;
	private int gen;
	private int runs;
	private static final int pop = 150;
	private int fps = 1; 
	private String test;
	private Timer timer;
	
	private Point fruit;
	
	private int topScore;
	private int score;
	private int remaining;
	private String scoreString;
	private String remString;
	private String currentString;
	private String fitString;
	private String nameString;
	private String speciesString;
	
	private Random random1 = new Random();
	private Random random2 = new Random();
	private TreeMap<Integer, ArrayList<Integer>> speciesMap;
	private ArrayList<Integer> average;
	private ArrayList<Integer> arraySize;
	private ArrayList<String> arraySizeS;
	private int board[][];
	
	private int minSnake = 5;
	
	private HistoryMarkers mark;
	
	Date date = new Date();
	DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

	// constructor
	public SnakeAIPanel(){
		//set all the stuff to what I want to start with
		setPreferredSize(new Dimension(WIDTH * 2, HEIGHT));
		setBackground(Color.BLACK);
		speciesMap = new TreeMap<Integer, ArrayList<Integer>>();
		speciesMap.put(0, new ArrayList<Integer>());
		arraySizeS = new ArrayList<String>();
		snakeDirection = 0;
		snake = new ArrayList<Point>();
		snake2 = new ArrayList<Point>();
		arraySize = new ArrayList<Integer>();
		outputs = new double[4];
		inputs = new double[8];
		mark = new HistoryMarkers();
		
		remaining = 100;
		scoreString = new String();
		remString = new String();
		currentString = new String();
		fitString = new String();
		test = new String();
		
		board = new int[25][25];
		//random1.setSeed(2000);
		
		snake.add(new Point(12,13));
		fruit = pickFruit();
		score = 0;
		topScore = 0;
		average = new ArrayList<Integer>();
		mark = new HistoryMarkers();
		species = new ArrayList<Species>();
		species.add(new Species());
		species.get(0).initiate(mark, 0);
		//speciesMap.put(1, new ArrayList<Integer>());
		//speciesMap.get(1).add(0);
		current = 0;
		gen = 0;
		runs = 0;
		
		
		Scanner in = new Scanner(System.in);
		if(test.equals(""))
		{
			
			System.out.println("Test/load?");
			test = in.nextLine();
			test.toLowerCase();
		}
		
		this.timer = new Timer(fps, new ActionListener() { 
			@Override 
			public void actionPerformed(ActionEvent e) { 
				try {
					handleTimerTick();
				} catch (InterruptedException | IOException e1) {
					
					e1.printStackTrace();
				} 
			}

		});
		/*
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				
					case KeyEvent.VK_S:
						try {
							saveSpecies(149);
						} catch (IOException e1) {
							// 
							e1.printStackTrace();
						}
						break;
						
						
					case KeyEvent.VK_ENTER:
						Scanner in = new Scanner(System.in);
						fps = 1000/in.nextInt();
						break;
						
						
					case KeyEvent.VK_L:
						try {
							saveSpecies(149);
						} catch (IOException e1) {
							// 
							e1.printStackTrace();
						}
						break;
				}
			}
		});*/
		startTimer();
		//reset;
			
		/*
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleMouseClick(e);
			}
		});
		
	}
	
	
	private void handleMouseClick(MouseEvent e) {
	 
		//System.exit(0);
		*/
	}
	
	private void reset() throws IOException
	{
		species.get(current).setMax(score * 2 + 100 - remaining);
		snakeDirection = 0;
		snake = new ArrayList<Point>();
		snake2 = new ArrayList<Point>();

		remaining = 100;
		board = new int[25][25];
		//random1.setSeed(2000);
		
		snake.add(new Point(12,13));
		fruit = pickFruit();
		score = 0;
		
		minSnake = 5;
		
		
		current++;
		//gen++;
		if(current < pop)
		{			
			if(test.equals("load"))
			{
				Scanner in = new Scanner(System.in);
				System.out.println("file?");
				String tests = in.nextLine();
				Species loader = new Species();
				loader.load(tests, mark);
				species.add(loader);
				timer.setDelay(1000/3);
			}
			if(gen == 0 && test.equals("test"))
			{
				species.add(new Species());
				species.get(current).initiate(mark, current);
				//speciesMap.get(1).add(current);
				mark = species.get(current).getMark();
			}
		}
		else
		{
			//System.exit(0);
			save();
			

			//@SuppressWarnings("resource")

			if(test.equals("test"))
			{
				runs--;
				if(runs == 0)
				{
					Scanner in = new Scanner(System.in);
					System.out.println("How many runs");
					runs = in.nextInt();
					System.out.println("How many fps");
					fps = 1000/in.nextInt();
				}
				
			}

			//timer.setDelay(100);
			gen++;
			for(int i  = 0; i < pop; i ++)
			{
				double staleish = ((pop - i) / pop);
				if(staleish > 1)
				{
					staleish = 1;
				}
				if(staleish < .25)
				{
					staleish = .25;
				}
				species.get(i).setStale(species.get(i).getStale() + staleish);
				if(species.get(i).getStale() >= 10)
				{
					species.get(i).setMax(0);
				}
			}
			//Collections.sort(species, new CustomComparatorSpecies());
			
			/*double sum = 0;
			int ran1 = 0;
			int ran2 = 0;
			double ran1V = 0.0;
			double ran2V = 0;
			double ranItV = 0;
			int ranIt = 0;
			int[] choose = new int[pop/2];
			int chooser;
			if(gen <= 10)
			{
				chooser = species.get(3 * pop/4).getMax();
			}
			else
			{
				chooser = species.get(pop/2).getMax();
			}
			double sumChoose = 0.0;
			for(int i = 0; i < pop; i++)
			{
				sum += species.get(i).getMax();
			}
			for(int i = 0; i < pop /2; i++)
			{
				choose[i] = chooser - species.get(i).getMax();
				sumChoose += choose[i];
			}
			/*
			/*for(int i = 0; i < (pop / 10); i++)
			{
				//species.get(i).genetic(species.get((species.size() - (i + 1))), species.get((species.size() - (pop/10 * 2) + i)), mark);
				for(int j = 0; j < pop; j++)
				{
					ran1V = random2.nextDouble() * sum;
					ran1V = ran1V - species.get(j).getMax();
					if(ran1V <= 0)
					{
						ran1 = j;
						break;
					}
				}
				for(int j = 0; j < pop; j++)
				{
					ran2V = random2.nextDouble() * sum;
					ran2V = ran2V - species.get(j).getMax();
					if(ran2V <= 0)
					{
						ran2 = j;
						break;
					}
				}
				for(int j = 0; j < pop / 2; j++)
				{
					ranItV = random2.nextDouble() * sumChoose;
					ranItV = ranItV - choose[j];
					if(ranItV <= 0)
					{
						ranIt = j;
						break;
					}
				}
				int genomeC = species.get(ranIt).getGenome();
				int nameC = species.get(ranIt).getName();
				species.get(ranIt).genetic(species.get(ran1), species.get(ran2), mark);
				species.get(ranIt).setGenome(0);
				speciesMap.get(genomeC).remove(nameC);
				
			}
			*/
			//species.get(0).genetic(species.get(species.size() - 1), species.get(species.size() - 2), mark);
			for(int i = 0; i < 10; i++)
			{
				pickGenetic();
			}
			for(int i = (pop/10); i < pop - 1; i++)
			{	
				double b = random1.nextDouble();
				if(b*100 < 25)
				{
					species.get(i).mutate(mark);
					mark = species.get(i).getMark();
					//current = 0;
				}
				
			}
			current = 0;
			int sss;
			int currentSSS = 1;
			//boolean currentSSSB = false;
			boolean done = false;
			//for(int i = 0; i < pop; i++)
			//{
				//species.get(i).setGenome(0);
			//}
			//if(gen == 1)
			//{
			//	speciesMap.clear();
			//}
			
			for(int i = pop - 1; i >= 0; i--)
			{
				//TODO: make speciation ACTUALLY DO SOMETHING 
				SpeciesComparator sCompare = new SpeciesComparator();
				
				//currentSSSB = false;
				if(species.get(i).getGenome() == 0)
				{
					species.get(i).setGenome(currentSSS);
					
					currentSSS++;
					done = true;
					for(int j = i; j >= 0; j--)
					{
						sss = 0;
						if(i != j && species.get(j).getGenome() == 0)
						{
							sss = sCompare.compare(species.get(i), species.get(j));
						}
						
						if(sss == 1)
						{
							
							species.get(j).setGenome(species.get(i).getGenome());
							/*if(speciesMap.size() < species.get(j).getGenome() + 1)
							{
								speciesMap.put(species.get(j).getGenome(), new ArrayList<Integer>());
								speciesMap.get(species.get(j).getGenome()).add(species.get(j).getName());
							}
							else
							{
								if(species.get(j).getGenome() != 1)
								{
									int b = 0;
								}

								speciesMap.get(species.get(j).getGenome()).add(species.get(j).getName());
							}
							*/
							
						}
						if(species.get(j).getGenome() == 0 && done == true)
						{
							done = false;
						}
					}
					if(done == true)
					{
						break;
					}
				}

				
			}
			speciesMap.clear();
			speciesMap.put(0, new ArrayList<Integer>());
			int highest = 0;
			for(int i = 0; i < pop; i ++)
			{
				if(highest < species.get(i).getGenome())
				{
					highest = species.get(i).getGenome();
				}
			}
			for(int i = 0; i <= highest; i++)
			{
				speciesMap.put(i, new ArrayList<Integer>());
				
			}
			for(int i = 0; i < pop; i ++)
			{
				speciesMap.get(species.get(i).getGenome()).add(species.get(i).getName());
				
			}
			arraySize.clear();
			arraySizeS.clear();
			for(int i = 0; i <= highest; i++)
			{
				arraySize.add(speciesMap.get(i).size());
			}
			if(arraySize.size() > 10)
			{
				int b = 0;
			}
			Collections.sort(species, new CustomComparatorSpecies());
			if(topScore < species.get(pop - 1).getMax())
			{
				saveSpecies(pop - 1);
				topScore = species.get(pop - 1).getMax();
				gen = gen;
			}
			Collections.sort(species, new CustomComparatorSpecies3());
		}
		
		
		startTimer();
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	protected void handleTimerTick() throws InterruptedException, IOException  {
		inputs[0] = (snake.get(0).getX() - fruit.getX()) / 25.0;
		inputs[1] = (snake.get(0).getY() - fruit.getY()) / 25.0;
		
		if(snakeDirection == 0)
		{
			inputs[2] = 0;
			inputs[3] = -1;
		}
		
		if(snakeDirection == 1)
		{
			inputs[2] = 1;
			inputs[3] = 0;
		}
		
		if(snakeDirection == 2)
		{
			inputs[2] = 0;
			inputs[3] = 1;
		}
		
		if(snakeDirection == 3)
		{
			inputs[2] = -1;
			inputs[3] = 0;
		}
		
		
		if(snake.get(0).getY() != 0)
		{
			inputs[4] = board[snake.get(0).getX()][snake.get(0).getY() - 1];
		}
		else
		{
			inputs[4] = -1;
		}
		
		if(snake.get(0).getX() != 24)
		{
			inputs[5] = board[snake.get(0).getX() + 1][snake.get(0).getY()];
		}
		else
		{
			inputs[5] = -1;
		}
		
		if(snake.get(0).getY() != 24)
		{
			inputs[6] = board[snake.get(0).getX()][snake.get(0).getY() + 1];
		}
		else
		{
			inputs[6] = -1;
		}
		
		if(snake.get(0).getX() != 0)
		{
			inputs[7] = board[snake.get(0).getX() - 1][snake.get(0).getY()];
		}
		else
		{
			inputs[7] = -1;
		}
		
		snakeOldDir = snakeDirection;
		
		snakeDirection = pickDir();//random2.nextInt(4);
		
		if(snakeOldDir % 2 == snakeDirection % 2)
		{
			snakeDirection = snakeOldDir;
		}
		
		snake2.clear();
		
		for(int i = 0; i < snake.size(); i++)
		{
			snake2.add(new Point(snake.get(i).getX(), snake.get(i).getY()));
		}
		//snake2 = snake;
		
		if(snakeDirection == 0)
		{
			snake.get(0).setY(snake.get(0).getY() - 1);
		}
		
		if(snakeDirection == 1)
		{
			snake.get(0).setX(snake.get(0).getX() + 1);
		}
		
		if(snakeDirection == 2)
		{
			snake.get(0).setY(snake.get(0).getY() + 1);
		}
		
		if(snakeDirection == 3)
		{
			snake.get(0).setX(snake.get(0).getX() - 1);
		}
		
		for(int i = 1; i < snake.size(); i++)
		{
			snake.set(i, snake2.get(i - 1));
		}
		if(snake.size() < minSnake)
		{
			snake.add(snake2.get(snake2.size() - 1));
		}
		remaining--;
		if(Collision() == true)
		{
			//System.out.println(score);
			//System.out.println(remaining);
			//if(score * 2 + (100 - remaining) > species.get(current).getMax())
			//{
			//		species.get(current).setMax(score * 2 + (100 - remaining));
			//}
			timer.stop();
			//Thread.sleep(500);
			reset();
			//System.exit(0);
		}
		if(remaining == 0)
		{
			//System.out.println(score);
			//System.out.println(remaining);
			//if(score * 2 + (100 - remaining) > species.get(current).getMax())
			//{
			//	species.get(current).setMax(score * 2 + (100 - remaining));
			//}
			
			timer.stop();
			//Thread.sleep(500);
			reset();
			//System.exit(0);
		}
		
		
		if(snake.get(0).getX() == fruit.getX() && snake.get(0).getY() == fruit.getY())
		{
			//Font f;
			//f.getFontName();
			if(minSnake > snake.size())
			{
				pickFruit();
				minSnake++;
			}
			else
			{
				pickFruit();
				snake.add(snake2.get(snake2.size() - 1));
			}

		}
		if(gen > 3)
		{
			arraySizeS.clear();
			for(int i = 0; i < arraySize.size(); i++)
			{
				arraySizeS.add(String.valueOf(arraySize.get(i)));
			}
		}
		
		nameString = String.valueOf(species.get(current).getName());
		scoreString = String.valueOf(score);
		remString = String.valueOf(remaining);
		currentString = String.valueOf(current);
		fitString = String.valueOf(species.get(current).getMax());
		speciesString = String.valueOf(species.get(current).getGenome());
		
		
		repaint();
	}

	private int pickDir()
	{
		//for(int i = 0; i < 4; i++)
		//{
		//	outputs[i] = random2.nextFloat();
		//}
		//int dir = random2.nextInt(4);
		int dir = 0;
		outputs = species.get(current).run(inputs, mark);
		double high = outputs[0];
		for(int i = 1; i < 4; i++)
		{
			if(high < outputs[i])
			{
				high = outputs[i];
				dir = i;
			}
		}
		return dir;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g); // paint background
		
		g.setColor(Color.WHITE);
		for(int i = 25; i <= 625; i+=25)
		{
			g.drawLine(0, i, 625, i);
			g.drawLine(i, 0, i, 625);
			
		}
		
		g.setColor(Color.GREEN);
		g.fillRect(snake.get(0).getX() * 25 + 5, snake.get(0).getY() * 25 + 5, 15, 15);
		
		g.setColor(Color.BLUE);
		for(int i = 1; i < snake.size() - 1; i++)
		{
			g.fillRect(snake.get(i).getX() * 25 + 5, snake.get(i).getY() * 25 + 5, 15, 15);
		}
		g.setColor(Color.CYAN);
		g.fillRect(snake.get(snake.size() - 1).getX() * 25 + 5, snake.get(snake.size() - 1).getY() * 25 + 5, 15, 15);
		
		g.setColor(Color.RED);
		g.fillRect(fruit.getX() * 25 + 5, fruit.getY() * 25 + 5, 15, 15);
		
		g.setColor(Color.WHITE);
		for(int i = 0; i < 8; i++)
		{
			g.fillRect(750, 30 + i * 75, 30, 30);
		}
		
		g.setColor(Color.CYAN);
		for(int i = 0; i < species.get(current).getNodes(); i++)
		{
			g.fillRect(820, 40+60*i, 20, 20);
		}
		
		g.setColor(Color.WHITE);
		for(int i = 0; i < 4; i++)
		{
			g.fillRect(1150, 50 + i * 150, 50, 50);
		}
		
		ArrayList<Connector> connection = species.get(current).getConnections();
		int x = 0;
		int x2 = 0;
		int y = 0;
		int y2 = 0;
		
		for(int i = 0; i < connection.size(); i++)
		{
			if(connection.get(i).getIn() < inputs.length)
			{
				x = 780;
			}
			else
			{
				x = 840; //+ 40 * (connection.get(i).getIn() - inputs.length);
			}
			
			if(connection.get(i).getOut() >= 1000)
			{
				x2 = 1150;
			}
			else
			{
				x2 = 820;// + 40 * (connection.get(i).getOut() - inputs.length);
			}
			
			
			
			if(connection.get(i).getIn() < inputs.length)
			{
				y = (int) (45 + connection.get(i).getIn() * 75);
			}
			else
			{
				y = (int) (50 + (connection.get(i).getIn() - inputs.length) * 60);
			}
			
			if(connection.get(i).getOut() >= 1000)
			{
				y2 = (int) (75 + (connection.get(i).getOut() - 1000) * 150);
			}
			else
			{
				y2 = (int) (50 + (connection.get(i).getOut() - inputs.length) * 60);
			}
			
			if(connection.get(i).getOut() < 1000 || connection.get(i).getIn() >= inputs.length)
			{
				g.setColor(Color.CYAN);
			}
			g.drawLine(x, y, x2, y2);//connection.get(i)
			g.setColor(Color.WHITE);
		}
		
		g.drawString(scoreString, 650, 600);
		g.drawString(remString, 650, 575);
		g.drawString(currentString, 650, 550);
		g.drawString(fitString, 650, 525);
		g.drawString(speciesString, 650, 500);
		g.drawString(nameString, 650, 475);
		if(gen > 2)
		{
			for(int i = 0; i < arraySizeS.size(); i++)
			{
				g.drawString(arraySizeS.get(i), 650, 0 + i * 25);
			}
		}
		
	}
	
	public boolean Collision()
	{
		if(snake.size() > minSnake)
		{
			snake2.remove(snake2.size() - 1);
		}
		/*if(snake2.size() > 0)
		{
			
			if(snake2.contains(snake.get(0)))
			{
				return true;
			}
			if(snake.get(0).getX() < 0 || snake.get(0).getY() < 0 || snake.get(0).getX() > 25 || snake.get(0).getY() > 25 )
			{
				return true;
			}
		}
		*/
		for(int i = 0; i < snake2.size(); i++)
		{
			if(snake.get(0).getX() == snake2.get(i).getX())
			{
				if(snake.get(0).getY() == snake2.get(i).getY())
				{
					return true;
				}
			}
		}
		
		if(snake.get(0).getX() > 24 || snake.get(0).getY() > 24 || snake.get(0).getX()< 0 || snake.get(0).getY() < 0)
		{
			return true;
		}
		return false;
	}
	
	public Point pickFruit()
	{
		ArrayList<Integer> possible = new ArrayList<Integer>();
		if(snake.size() >= minSnake)
		{
			for(int i = 0; i < 25; i++)
			{
				for(int j = 0; j < 25; j++)
				{
					if(board[i][j] == 0)
					{
						possible.add(i*25 + j);
					}
				}
			}
		}
		else
		{
			for(int i = 0; i < 25; i++)
			{
				for(int j = 0; j < 25; j++)
				{
					if(board[i][j] == 0 && i != 12 && j != 13 && j != 12)
					{
						possible.add(i*25 + j);
					}
				}
			}
		}

		
		int num = random1.nextInt(possible.size());
		//fruitX = random1.nextInt(24);
		//fruitY = random2.nextInt(24);
		
		fruit = new Point(possible.get(num) % 25, possible.get(num) / 25);
		score += remaining;
		remaining = 100;
		return fruit;
	}
	
	private void save() throws IOException
	{
		Collections.sort(species, new CustomComparatorSpecies());
		String genSaveS = "saveStuff/AverageGen" + dateFormat.format(date) + ".txt";// + date;// + gen + ".txt";
		File f = new File(genSaveS);
		String saveS = new String();//reader.read;
		if (f.exists() && gen > 0) {
		    // the file exists
		
			FileReader reader = new FileReader(f);
			
	        while (true){
	            int c = reader.read();
	
	            if (c < 0) {
	                break;
	            }
	
	            char ch = (char) c;
	            saveS = saveS + ch;
	
	        }
	        reader.close();
			FileWriter write = new FileWriter(f);
			double sum = 0;
			for(int i = 0; i < pop; i++)
			{
				sum += species.get(i).getMax();
				
			}
			sum = sum / pop;
			
			String sumS = String.valueOf(sum);
			
			saveS = saveS + "\n" + sumS + "\t" + species.get(pop - 1).getMax() ;
			write.write(saveS);
			write.close();
		} else {
		    // the file has not been created yet
			
			FileWriter write = new FileWriter(f);
			double sum = 0;
			for(int i = 0; i < pop; i++)
			{
				sum += species.get(i).getMax();
				
			}
			sum = sum / pop;
			
			String sumS = String.valueOf(sum);
			saveS = saveS + "\n" + sumS + "\t" + species.get(pop - 1).getMax();
			write.write(saveS);
			write.close();
		}
	}
	
	
	private void saveSpecies(int number) throws IOException
	{
		//Collections.sort(species, new CustomComparatorSpecies());
		String genSave = "saveStuff/TopSpecies" + dateFormat.format(date) + gen + ".txt";// + date;// + gen + ".txt";
		File f = new File(genSave);
		String saveS = new String();//reader.read;
		/*    // the file exists
			FileWriter write = new FileWriter(f);
			double sum = 0;
			for(int i = 0; i < pop; i++)
			{
				sum += species.get(i).getMax();
				
			}
			sum = sum / pop;
			
			String sumS = String.valueOf(sum);
			
			saveS = saveS + "\n" + sumS + "\t" + species.get(pop - 1).getMax() ;
			write.write(saveS);
			write.close();
			*/
		
	    // the file has not been created yet
		
		FileWriter write = new FileWriter(f);

		for(int i = 0; i < species.get(number).getConnections().size(); i++)
		{
			saveS = saveS + "\n" + species.get(number).getConnections().get(i).getIn() + " " + + species.get(number).getConnections().get(i).getOut() + " " + species.get(number).getConnections().get(i).getWeight();
		}
		
		saveS = saveS + "\ndone\n";
		write.write(saveS);
		write.close();
	
	}
	
	private void pickGenetic()
	{
		
		Collections.sort(species, new CustomComparatorSpecies3());
		int total = 0;
		if(speciesMap.size() > 1)
		{
			average.clear();
			//while(average.size()<speciesMap.size())
			//{
			//	average.add(average.size());
			//}
			average.add(0);
			for(int i = 1; i < speciesMap.size(); i++)
			{
				average.add(0);
				if(speciesMap.get(i).size() > 0)
				{
					
					for(int j = 0; j < speciesMap.get(i).size(); j++)
					{
						
						average.set(i, average.get(i) + species.get(speciesMap.get(i).get(j)).getMax()); //= average.get(i) + species.get(speciesMap.get(i).get(j));
					}
						
					
					average.set(i, average.get(i) /  speciesMap.get(i).size());
					int div = (int)Math.ceil(speciesMap.get(i).size() / 10.0);
					average.set(i, average.get(i) /  div);
					total += average.get(i);
				}
			}
			Random rand = new Random();
			int randomness = rand.nextInt(total);
			//randomness = total * randomness;
			int iter = -1;
			while( randomness >= 0)
			{
				iter++;
				randomness -= average.get(iter);
			}
			//iter--;
			int ran1 = rand.nextInt(speciesMap.get(iter).size());
			int ran2 = rand.nextInt(speciesMap.get(iter).size());
			// = rand.nextInt(bound)
			if(iter == 1)
			{
				int b = 0;
			}
			
			
			
			int sum =0;
			double ranItV = 0;
			int ranIt = 0;
			int[] choose = new int[pop/2];
			int chooser;
			if(gen <= 10)
			{
				chooser = species.get(3 * pop/4).getMax();
			}
			else
			{
				chooser = species.get(pop/2).getMax();
			}
			double sumChoose = 0.0;
			for(int i = 0; i < pop; i++)
			{
				sum += species.get(i).getMax();
			}
			for(int i = 0; i < pop /2; i++)
			{
				choose[i] = chooser - species.get(i).getMax();
				sumChoose += choose[i];
			}
			for(int j = 0; j < pop / 2; j++)
			{
				ranItV = random2.nextDouble() * sumChoose;
				ranItV = ranItV - choose[j];
				if(ranItV <= 0)
				{
					ranIt = j;
					break;
				}
			}
					
			//int genomeC = species.get(ranIt).getGenome();
			int nameC = species.get(ranIt).getName();
			//if(ranIt >= 150 || ran1 >= 150 || ran2 >= 150)
			//{
			//	int b = 0;
			//}
			species.get(ranIt).genetic(species.get(ran1), species.get(ran2), mark);
			if(ran1 == ran2)
			{
				species.get(ranIt).mutate(mark);
			}
			//species.get(ranIt).setGenome(iter);
			
			
		}
		else
		{
			double sum = 0;
			int ran1 = 0;
			int ran2 = 0;
			double ran1V = 0.0;
			double ran2V = 0;
			double ranItV = 0;
			int ranIt = 0;
			int[] choose = new int[pop/2];
			int chooser;
			if(gen <= 10)
			{
				chooser = species.get(3 * pop/4).getMax();
			}
			else
			{
				chooser = species.get(pop/2).getMax();
			}
			double sumChoose = 0.0;
			for(int i = 0; i < pop; i++)
			{
				sum += species.get(i).getMax();
			}
			for(int i = 0; i < pop /2; i++)
			{
				choose[i] = chooser - species.get(i).getMax();
				sumChoose += choose[i];
			}
			//species.get(i).genetic(species.get((species.size() - (i + 1))), species.get((species.size() - (pop/10 * 2) + i)), mark);
			for(int j = 0; j < pop; j++)
			{
				ran1V = random2.nextDouble() * sum;
				ran1V = ran1V - species.get(j).getMax();
				if(ran1V <= 0)
				{
					ran1 = j;
					break;
				}
			}
			for(int j = 0; j < pop; j++)
			{
				ran2V = random2.nextDouble() * sum;
				ran2V = ran2V - species.get(j).getMax();
				if(ran2V <= 0)
				{
					ran2 = j;
					break;
				}
			}
			for(int j = 0; j < pop / 2; j++)
			{
				ranItV = random2.nextDouble() * sumChoose;
				ranItV = ranItV - choose[j];
				if(ranItV <= 0)
				{
					ranIt = j;
					break;
				}
			}
			int genomeC = species.get(ranIt).getGenome();
			int nameC = species.get(ranIt).getName();
			species.get(ranIt).genetic(species.get(ran1), species.get(ran2), mark);
			species.get(ranIt).setGenome(ranIt);
			if(genomeC != 1)
			{
				int b = 0;
				b = 1 + b;
			}
			else
			{
				if(nameC >= speciesMap.size())
				{
					int b = 0;
				}
				//speciesMap.get(genomeC).remove(nameC);
			}
			
		}

	}
	private void pickGenome(ArrayList<Integer> changedSpecies)
	{
		
	}
}
