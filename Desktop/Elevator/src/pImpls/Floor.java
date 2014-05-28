package pImpls;
import pInterfaces.ElevatorInterface;
import pInterfaces.FloorInterface;
import java.util.ArrayList;


//fill this in
/**
 * The floor class represents a building floor containing the floor number, summon elevator buttons, and the modification of people on the floor.  
 *  requests to visit certain floors. Each Elevator object runs in its own thread and will continue to run until manually shut down.
 */
public class Floor implements FloorInterface
{
	private ArrayList<Person> goingUp;
	private ArrayList<Person> goingDown;
	private int floorNumber;
	
	/** EDIT THIS FOR FLOOR
     * Constructor which allows for properties such as the speed, capacity, and floor range of the elevator to be customized.
     * @param paramCapacity The number of people this elevator can hold at a given time. When the elevator hits capacity, no more people may board the elevator.
     * @param numFloors The number of floors that this elevator services. This value cannot be larger than the number of floors in the building that owns it, but it may be smaller
     * @param idNumber The unique identifier number. This number cannot be negative, but it need not be in consecutive order compared to other elevators in the building.
     * @throws NegativeCapacityException if any of the values passed into it are negative
     */
	public Floor(int inFloorId)
	{
		setFloorNumber(inFloorId);
		initializeFloorArrays();
	}
	
	@Override
        
        /** Called when initializing a person on a floor
         * @param Person
         */
	public void addPersonToFloor(Person inPerson)
	{
		if(inPerson.getDestinationFloor() < this.getId())
		{
			goingDown.add(inPerson);
			summonElevator(Direction.DOWN);
		}
		else if(inPerson.getDestinationFloor() > this.getId())
		{
			goingUp.add(inPerson);
			summonElevator(Direction.UP);
		}
	}
	
	@Override
        /** Elevator Call button press method.  
         * @param  Direction UP or Down 
         */
	public void summonElevator(Direction directionToGo) 
	{
		if(directionToGo != Direction.IDLE)
		{
			ElevatorControlModule.getInstance().elevatorCallReceiver(this.getId(), directionToGo);		
		}
	}


	@Override
	public int getId() 
	{
		return this.floorNumber;
	}

	@Override
	public void removeFromFloor(ElevatorInterface elevatorToEnter, Direction directionToGo) 
	{
		if(directionToGo != Direction.IDLE)
		{
			ArrayList<Person> peopleToRemove;
			if(directionToGo == Direction.UP)
			{
				peopleToRemove = this.goingUp;
			}
			else
			{
				peopleToRemove = this.goingDown;
			}
			for (Person person : peopleToRemove )
			{
				elevatorToEnter.addPassenger(person);
			}
		}
	}
	
	private void setFloorNumber(int inNum)
	{
		floorNumber = inNum;
	}

	private void initializeFloorArrays()
	{
		goingUp = new ArrayList<Person>();
		goingDown = new ArrayList<Person>();
	}
}