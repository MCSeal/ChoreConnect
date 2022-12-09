package vehicle;

public class Motorcycle extends Vehicle
{

	public Motorcycle(int no_of_wheels, int no_of_seats) {
		super(no_of_wheels, no_of_seats);

	}

	void sound()
	{
		System.out.println("vroom...vroom!");
	}
}
