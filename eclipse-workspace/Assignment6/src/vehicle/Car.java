package vehicle;

public class Car extends Vehicle 
{
	public Car(int no_of_wheels, int no_of_seats) {
		super(no_of_wheels, no_of_seats);
	}

	void sound()
	{
		System.out.println("clunk..cluck!");
	}
}
