import java.util.Scanner;
		
public class FactorialMain 
{
		
	public static Scanner in = new Scanner(System.in);

	public static int calcFactorial(int number)
	{
		int product=1;
			    
		for (int i=number; i>0; i--)
		{
			product = i * product;
		}
			    
		return product;
	}
			
	public static void main(String[] args) 
	{
		int factorialNumber;
		                
		System.out.println("Enter a number to calculate the factorial: ");
		int number = in.nextInt ();
				
		factorialNumber = calcFactorial(number);
				
		System.out.println("The factorial of " + number + " is: " + factorialNumber);
				
	}
}
