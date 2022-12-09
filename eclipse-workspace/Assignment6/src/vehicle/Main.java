package vehicle;

public class Main
{
	public static void main(String[] args) 
	{
                Car myCar = new Car(5,4);

		Motorcycle myMotorcycle = new Motorcycle(2,2);		

		System.out.println("My car has " + myCar.no_of_seats + " seats.");
		System.out.println("My motorcycle has " +myMotorcycle.no_of_seats + " seats.");
		
		System.out.println("My car has " +myCar.no_of_wheels + " wheels.");
		System.out.println("My motorcycle has " +myMotorcycle.no_of_wheels + " wheels.");
		
		System.out.print("My car makes the sound: ");
		myCar.sound();
		
		System.out.print("My motorcycle makes the sound: ");
		myMotorcycle.sound();
	}

}