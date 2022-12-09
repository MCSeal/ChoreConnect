package LeapYear;
import java.time.*;

public class LeapYear 
{
	
	public static long numOfDays(int year)
	{
		LocalDate dateBefore = LocalDate.of(2022, 4, 21);
		LocalDate dateAfter = LocalDate.of(2022, 4, 25);
	
		Period period = Period.between(dateBefore, dateAfter);
		long daysDiff = Math.abs(period.getDays());	

		return daysDiff;
	}
	
	public static boolean isLeapYear(int year)
	{
		boolean leapYearFlag = false;
		long daysDiff;
		
		daysDiff = numOfDays(year);
		
		if (daysDiff == 366)
			leapYearFlag = true;
		
		else if ((year % 4 == 0) && (year % 100 != 0))
			leapYearFlag = true;
		
		else if (year % 400 == 0)
            leapYearFlag = true;
		
		return leapYearFlag;
	}
}
