
import java.awt.Color;
import java.awt.Font;
import java.util.Scanner;

public class SieveOfEratosthenes

{
	public static final double MARGIN = 0.05;
	static double xText;
	static double yText;
	static double lengthOfHalfSquare ;
	static double spacing ;
	static double x;
	static double y;
	static double sizeOfSquares;
	static int primeCount = 0;
	static double numberSquaresInRow;
	static double numberOfPrimesInRow;
	static int primeRowCount = 1;
	static double sizeOfText;
	
	public static void main(String[] args)
	{
		
			// Program finds all the prime numbers between 2 and a user inputed integer.
			//Then creates a graphical simulation of this process.
			System.out.println("Please enter an Integer >= 2");
			Scanner newScanner = new Scanner(System.in);
			
			if (newScanner.hasNextInt())
			{
				
				int numberLimit = newScanner.nextInt();
				if(numberLimit > 0)
				{
					setupBoxes(numberLimit);
					displaynumbers2toN(numberLimit);
					int[][] numberSequence = sieve(numberLimit);
					String primeNumbers = nonCrossedOutSubseqToString(numberSequence);
					System.out.println("\nPrime numbers = " + primeNumbers);
				}
				else
				{
					System.out.println("\nInvalid entry, the number must be >= 2");
				}
			} 
			else
			{
				System.out.println("\nInvalid entry");
			}
			System.out.println("Goodbye.");
			newScanner.close();
	}

	public static int[][] createSquence(int numberLimit)
	{
		int numberSequence[][] = new int[numberLimit - 1][2];
		if (numberSequence != null)
		{
			for (int index = 0; index < numberSequence.length; index++)
			{
				numberSequence[index][0] = index + 2;
			}

		}
		return numberSequence;
	}

	public static void crossOutHigherMultiples(int[][] numberSequence, int currentNumber)
	{
		if(numberSequence != null)
		{
			int currentNumberIndex = currentNumber - 2;
			if ((currentNumberIndex >= 0 && currentNumberIndex < numberSequence.length)
					&& (numberSequence[currentNumberIndex][1] != 1))
			{
				primeCount++;
				for (int index2 = currentNumberIndex + 1; index2 < numberSequence.length; index2++)
				{
					if(numberSequence[index2][1] != 1)
					{
						int possibleMultiple = numberSequence[index2][0];
						if (possibleMultiple % currentNumber == 0)
						{
							
							numberSequence[index2][1] = 1;
							displayComposite(possibleMultiple,primeCount, numberSequence.length);
						}
					}
				}
				sequenceToString(numberSequence);
			}
		}
	}

	public static String sequenceToString(int[][] numberSequence)
	{
		String numberString = "";
		if(numberSequence != null)
		{
	
			for(int index = 0; index < numberSequence.length; index++)
			{
				if ((numberSequence[index][1]) == 1)
				{
					numberString += "[" + numberSequence[index][0] + "]";
				} 
				else
				{
					numberString += numberSequence[index][0];
				}
				
				if (index == numberSequence.length - 1)
				{
					numberString += ".";
				}
				else
				{
					numberString += ",";
				}
			}
			System.out.println("\n" + numberString);
		}
		return numberString;
	}

	public static String nonCrossedOutSubseqToString(int[][] numberSequence)
	{
		int primeCount2 = 0;
		String primeList = "";
		if(numberSequence != null)
		{
			for (int index = 0; index < numberSequence.length; index++)
			{
				if ((numberSequence[index][1]) == 0)
				{
					primeCount2++;
					primeList += numberSequence[index][0] + ",";
				}
			}
			setupPrimeDisplay(primeCount2);
			for(int index2 = 0; index2 < numberSequence.length; index2++)
			{
				if(numberSequence[index2][1] == 0)
				{
					
					displayPrime(numberSequence[index2][0], primeCount2,numberSequence.length);
				  
				}
			}
		}
		return primeList.substring(0, primeList.length() - 1) + ".";
	}

	public static String createNumberString(int[][] numberSequence)
	{
		String allNumbers = "";
		for (int index = 0; index < numberSequence.length; index++)
		{
			if (index == numberSequence.length - 1)
			{
				allNumbers += numberSequence[index][0] + ".";
			} 
			else
			{
				allNumbers += numberSequence[index][0] + ",";
			}
		}
		return allNumbers;
	}

	public static int[][] sieve(int numberLimit)
	{
		int[][] numberSequence = createSquence(numberLimit);
		String allNumbers = createNumberString(numberSequence);
		System.out.println(allNumbers);
		
		int numberLimitRoot = (int) Math.sqrt(Double.valueOf(numberSequence.length + 1));
		for (int index = 0; numberSequence[index][0] <= numberLimitRoot; index++)
		{ 
			int currentNumber = numberSequence[index][0];
			crossOutHigherMultiples(numberSequence, currentNumber);
		}
		return numberSequence;
	}
    
	public static void displayNumber(int currentNumber, int numberLimit, Color boxColor )
	{
		if(currentNumber <= numberLimit + 1)
		{
			String currentNumberString = Integer.toString(currentNumber);
			StdDraw.setPenColor(boxColor);
			if(x > 0.7)
			{
				y = y - (spacing + sizeOfSquares);
				x = MARGIN;
			}
			StdDraw.filledSquare(x, y, lengthOfHalfSquare);
			StdDraw.setPenColor(Color.BLACK);
		    StdDraw.square(x, y, lengthOfHalfSquare);
			StdDraw.text(x, y, currentNumberString); 
			x = x + spacing + sizeOfSquares;
		}
	}	
	
	public static void displaynumbers2toN(int numberLimit)
	{
		Color allNumbers = Color.WHITE;
		for(int number = 2; number <= numberLimit; number++)
		{
			displayNumber(number, numberLimit, allNumbers);
		}
	}
	
	public static void setupBoxes(int numberLimit)
	{
		StdDraw.setCanvasSize(800,800);
		StdDraw.setPenColor(Color.BLACK);
		Font largerText = new Font("Sans Serif", Font.BOLD, 28);
		StdDraw.setFont(largerText);
		StdDraw.text(0.85,0.98,"Prime numbers :");
		
		//box scaling
		numberSquaresInRow = Math.round( Math.sqrt(numberLimit));
		double divisor = numberSquaresInRow + (numberSquaresInRow / 4) ;
		sizeOfSquares = (0.7 / divisor);
		spacing = sizeOfSquares/ 6.0;
		x = MARGIN + sizeOfSquares + spacing + lengthOfHalfSquare;
		y = 1 - MARGIN;
		lengthOfHalfSquare = sizeOfSquares / 2;
		
		//font of numbers in boxes
		Font normal = new Font("Sans Serif", Font.PLAIN, (int)(500 * sizeOfSquares));
		StdDraw.setFont(normal);
	
	}
	
	public static void setupPrimeDisplay(int primeCount2)
	{
		//text scaling
		numberOfPrimesInRow = Math.sqrt(primeCount2);
		double textDivisor = numberOfPrimesInRow * 2;
		sizeOfText = 0.3 / textDivisor;
		xText = 0.73;
		yText = 0.91;
		Font primeFont = new Font("Sans Serif", Font.PLAIN, (int)(sizeOfText * 1000));
		StdDraw.setFont(primeFont);
	}
	
	public static void displayComposite(int composite, int primeCount,int numberLimit)
	{
		Color compositeColor;
		switch(primeCount)
		{
		case 1:
			 compositeColor = StdDraw.RED;
			break;
		case 2:
			 compositeColor = StdDraw.BLUE;
			break;
		case 3:
			 compositeColor = StdDraw.CYAN;
			break;
		case 4:	
			compositeColor = StdDraw.GREEN;
			break;
		case 5:
			compositeColor = StdDraw.YELLOW;
			break;
		case 6:
			compositeColor = StdDraw.PINK;
			break;
		case 7:
			compositeColor = StdDraw.ORANGE;
			break;
		default:
			compositeColor = StdDraw.MAGENTA;
		}
		
		//calculating what row the square we want to color is in
		double rowIndex = getRowIndex (composite);
		//calculating what column the square we want to color is in
		double columnIndex = getColumnIndex(composite, rowIndex);
		//finding x and y co-ordinates of square 
		y = 1 - (MARGIN + (rowIndex - 1.0) * sizeOfSquares + (rowIndex -1)* spacing);
		x = (MARGIN + (((columnIndex - 1.0) * sizeOfSquares)+ (columnIndex -1)* spacing));
		displayNumber(composite, numberLimit, compositeColor );
	}
	
	public static void displayPrime(int primeNumber,int primeCount,int numberLimit )
	{
		Color primeColor = StdDraw.LIGHT_GRAY;
		double rowIndex = getRowIndex (primeNumber);
		double columnIndex = getColumnIndex(primeNumber, rowIndex);
		y = 1 - (MARGIN + (rowIndex - 1.0) * sizeOfSquares + (rowIndex -1)* spacing);
		x = (MARGIN + (((columnIndex - 1.0) * sizeOfSquares)+ (columnIndex -1)* spacing));
		displayNumber(primeNumber,numberLimit,primeColor);
		
		if(primeRowCount > numberOfPrimesInRow)
		{
			yText = yText - ((spacing*2) + sizeOfText);
			xText = 0.73;
			primeRowCount = 1;
		}
		primeRowCount++;
		String primeString = Integer.toString(primeNumber);
		StdDraw.text(xText, yText, primeString);
		StdDraw.show(50);
		//to account for increasing 0's
		//1229 prime numbers under 10,000
		if(primeCount >= 1229) 
		{
			xText = xText + (spacing*5) + sizeOfText;
		}
		//168 prime numbers under 1000
		else if(primeCount >= 168)
		{
			xText = xText + (spacing*4) + sizeOfText;
		}
		//25 prime numbers before 100
		else if(primeCount >= 25 )
		{
			xText = xText + (spacing*3.3) + sizeOfText;
		}
		else
		{
			xText = xText + (spacing*2) + sizeOfText;
		}
	}
	
	public static double getRowIndex (int currentNumber)
	{
		double rowIndex = (currentNumber / numberSquaresInRow);
		if(rowIndex <= 1)
		{
			rowIndex = 1;
		}
		else if((rowIndex % 1 )!= 0)
		{
			rowIndex = (int) rowIndex;
			rowIndex++;
		}
		return rowIndex;
	}
	
	public static double getColumnIndex(int currentNumber,double rowIndex)
	{
		double columnIndex =  (currentNumber - ((rowIndex - 1) * numberSquaresInRow));
		if(columnIndex == 0)
		{
			columnIndex =   currentNumber / rowIndex;
		}
		else if((columnIndex % 1 ) != 0)
		{
			columnIndex = 1;
		}
		return columnIndex;
	}
}
