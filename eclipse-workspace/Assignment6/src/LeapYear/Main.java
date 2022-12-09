package LeapYear;

import java.util.Scanner;

public class Main 
{
	public static Scanner in = new Scanner(System.in);

	public static void main(String[] args) 
	{
		System.out.println("Enter a year: ");
		int year = in.nextInt ();
		
		boolean decideLeapYear = LeapYear.isLeapYear(year);
		
		if (decideLeapYear)
			System.out.println("yes, " + year + " is a leap year");
		else
			System.out.println("no, " + year + " is not a leap year");
		
	}

}
