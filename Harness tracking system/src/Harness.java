public class Harness
{
	private String make;
	private int modelNumber;
	private int timesUsed;
	private String lastCheckedBy;
	boolean onLoan;
	private String borrowedBy;

	Harness(String make, int modelNumber, int timesUsed, String lastCheckedBy, boolean onLoan, String borrowedBy)
	{
		this.make = make;
		this.modelNumber = modelNumber;
		this.timesUsed = timesUsed;
		this.lastCheckedBy = lastCheckedBy;
		this.onLoan = onLoan;
		this.borrowedBy = borrowedBy;
	}

	Harness(String make, int modelNumber, String lastCheckedBy)
	{
		this(make, modelNumber, 0, lastCheckedBy, false, "N/A");
	}

	public void checkHarness(String instructorName)
	{
		if (!this.onLoan && !instructorName.isEmpty())
		{
			this.lastCheckedBy = instructorName;
			this.timesUsed = 0;
		}

	}

	public boolean canHarnessBeLoaned()
	{
		if (this.onLoan)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public void loanHarness(String borrowedBy)
	{
		if (this.canHarnessBeLoaned() && !borrowedBy.isEmpty())
		{
			this.borrowedBy = borrowedBy;
			this.onLoan = true;
		}
	}

	public void returnHarness()
	{
		this.onLoan = false;
		this.borrowedBy = "N/A";
	}

	// Override
	public String toString()
	{
		String harnessString = ("Make : " + this.make + "  \nModel Number : " + this.modelNumber + "  \nTimes used : "
				+ this.timesUsed + "  \nLast checked by : " + this.lastCheckedBy + "  \nOn Loan : "
				+ ((onLoan) ? "" + "Yes" : "No") + "  \nBorrowed by : " + borrowedBy);

		return harnessString;
	}

	public String getMake()
	{
		return this.make;
	}

	public int getModelNumber()
	{
		return this.modelNumber;
	}

	public int getTimesUsed()
	{
		return this.timesUsed;
	}

	public String getLastCheckedBy()
	{
		return this.lastCheckedBy;
	}

	public String getBorrowedBy()
	{
		return this.borrowedBy;
	}
}