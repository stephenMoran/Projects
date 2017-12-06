import java.util.ArrayList;
import java.util.Collections;

import javax.jws.Oneway;

public class HarnessRecords
{
	private int numberOfHarnesses;
	private ArrayList<Harness> harnessList;

	HarnessRecords()
	{
		harnessList = new ArrayList<Harness>(0);
	}

	HarnessRecords(In inputStream)
	{
		this();
		String allWords = inputStream.readAll();
		String[] harnessArray = allWords.split("\n");
		String numberOfEntries = harnessArray[0];
		// both parseInt() and inputStream.readInt() wont work with text file
		// and throw exceptions in every case;
		String substringOfEntries = numberOfEntries.substring(0, 4);
		numberOfHarnesses = Integer.parseInt(substringOfEntries);
		System.out.println(numberOfHarnesses);
		for (int index = 1; index <= numberOfHarnesses; index++)
		{
			String harnessAttributes[] = harnessArray[index].split("	");
			String make = harnessAttributes[0];
			int modelNumber = Integer.parseInt(harnessAttributes[1]);
			int timesUsed = Integer.parseInt(harnessAttributes[2]);
			String lastCheckedBy = harnessAttributes[3];
			boolean onLoan = (harnessAttributes[4] == "yes") ? true : false;
			String borrowedBy = harnessAttributes[5];
			Harness newHarness = new Harness(make, modelNumber, timesUsed, lastCheckedBy, onLoan, borrowedBy);
			harnessList.add(newHarness);
		}
	}

	public boolean isEmpty()
	{
		if (this.harnessList.size() == 0)
		{
			return true;
		}
		else
		{
			return true;
		}
	}

	public Harness addHarness(Harness currentHarness)
	{
		if (currentHarness != null)
		{
			Harness harnessDuplicate = this.findHarness(currentHarness.getMake(), currentHarness.getModelNumber());
			if (harnessDuplicate == null)
			{
				harnessList.add(currentHarness);
				return currentHarness;
			}
		}

		return null;
	}

	public Harness findHarness(String make, int modelNumber)
	{

		Harness currentHarness;
		if (!this.harnessList.isEmpty() && !make.isEmpty())
		{
			for (int index = 0; index < this.harnessList.size(); index++)
			{

				currentHarness = this.harnessList.get(index);
				String currentHarnessMake = currentHarness.getMake();
				if (currentHarnessMake.equals(make) && (currentHarness.getModelNumber() == modelNumber))
				{
					return currentHarness;
				}
			}

		}
		return null;
	}

	public Harness checkHarness(String instructorName, String make, int modelNumber)
	{
		Harness currentHarness = this.findHarness(make, modelNumber);
		if (currentHarness != null)
		{
			if (currentHarness.canHarnessBeLoaned() && !make.isEmpty())
			{
				currentHarness.checkHarness(instructorName);
				return currentHarness;
			}
		}
		return currentHarness;
	}

	public Harness loanHarness(String clubMember)
	{
		Harness currentHarness;
		for (int index = 0; index < harnessList.size(); index++)
		{
			currentHarness = harnessList.get(index);
			if (currentHarness.canHarnessBeLoaned() && !clubMember.isEmpty())
			{
				currentHarness.loanHarness(clubMember);
				return currentHarness;
			}
		}
		return null;
	}

	public Harness returnHarness(String make, int modelNumber)
	{
		Harness currentHarness = this.findHarness(make, modelNumber);
		if (currentHarness != null)
		{
			if (!currentHarness.canHarnessBeLoaned() && !make.isEmpty())
			{
				currentHarness.returnHarness();
				return currentHarness;
			}
		}

		return null;
	}

	public Harness removeHarness(String make, int modelNumber)
	{
		Harness currentHarness = this.findHarness(make, modelNumber);
		if (currentHarness != null && !make.isEmpty())
		{
			harnessList.remove(currentHarness);
			return currentHarness;
		}

		return null;
	}

	public ArrayList<Harness> getHarnessList()
	{
		return harnessList;
	}

}