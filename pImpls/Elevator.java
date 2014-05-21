package pImpls;
//import XmlParser;

import pInterfaces.ElevatorInterface;
import pExceptions.*;

import java.util.ArrayList;


/**
 *  Elevator class that handles properties such as the speed, capacity, and floor range of the elevator to be customized.
 */
public class Elevator implements ElevatorInterface, Runnable
{
	/**
	 * The current floor that this Elevator object is on. This value is an internal representation, so it uses ZERO-BASED indexing
	 */
	private int currentFloor;
	
	/**
	 * The direction that this elevator is currently traveling
	 */
	private Direction direction;
	
	/**
	 * How fast it should take the elevator to traverse a floor, measured in milliseconds
	 */
	private int speed;
	
	/**
	 * How fast the Elevator's doors should open, measured in milliseconds
	 */
	private int doorSpeed;
	
	/**
	 * The list of requests that this elevator object should handle as it travels
	 */
	private ArrayList <Integer> requestQueue;
	
	/**
	 * The list of Person objects currently inside of the elevator.
	 */
	private ArrayList<Person> passengerList;
	
	/**
	 * Whether the Elevator's doors are currently open or not. If they are open, then passengers can enter/leave the Elevator
	 */
	private boolean bDoorsOpen;
	
	/**
	 * How many passengers this Elevator can hold
	 */
	private int capacity;
	
	/**
	 * The index representing this Elevator's position in relation to the rest of the Elevators within the simulation. This value is an internal representation, so it uses ZERO-BASED indexing.
	 */
	private int elevatorId;
	
	/**
	 * The thread object that allows each Elevator object to run independently of other Elevator objects
	 */
	private Thread elevatorThread = new Thread(this);
	
	/**
	 * Whether or not the elevator is currently running. Once set to false, it cannot be set back to true
	 */
	private boolean running = true;
	
	/**
	 * The index of the top-most floor that this Elevator can visit. This value is an internal representation, so it uses ZERO-BASED indexing
	 */
	private int maxFloors;
	
	/**
	 * The index of the bottom-most floor that this Elevator can visit. This value is an internal representation, so it uses ZERO-BASED indexing
	 */
	private int minFloors;
	
	
   /**
    * Constructor for Elevator. Takes in the elevator Id, capacity, and max/min floors.
    * @param inId The elevators id. This number cannot be negative and must be independent of another elevators Id.
    * @param inCapacity The total number of passengers the elevator can hold. This number cannot be negative and must not be less than {@ Value}
    * @param inMaxFloors The maximum amount of floors the elevator can access.
    * @param inMinFloors The minimum amount of floors the elevator can access.
    * @throws NegativeCapacityException 
    * @throws NegativeFloorException 
    */
	public Elevator(int inId, int inCapacity, int inMaxFloors, int inMinFloors) throws NegativeCapacityException, NegativeFloorException
	{
		setId(inId);
		setCapacity(inCapacity);
		setMaxFloors(inMaxFloors);
		setMinFloors(inMinFloors);
                
                doorSpeed =  XmlParser.getElevDoorTime();
                
		initializeRequestQueue();
		setInitialDirection();
    	setDefaultFloor(1);
    	createPassengerList();
    	elevatorThread.start();
	}
	
   /**
    * Simulates the act of calling an elevator.  Only accepted if floor is on the elevator's path
    * @param floorNum The floor number that will be added to the queue. It need not be in consecutive order compared to other elevators in the building.
    * If the request is already present inside the requests queue, nothing happens
    * NOTE: this method uses ONE-BASED indexing, which means that zero corresponds to an invalid request.
    */
	@Override
	public void addFloorToQueue(int floorNum) 
	{
		int internalFloorNum = floorNum - 1;
		if(requestQueue.contains(internalFloorNum))
		{
			//the floor already exists in the queue, so there is nothing that needs to be done
			return;
		}
		
		if(this.currentFloor == this.maxFloors)
		{
			this.direction = Direction.DOWN;
		}
		else if(this.currentFloor == this.minFloors)
		{
			this.direction = Direction.UP;
		}
		
        switch (this.direction) 
        {
        case UP:
           if (internalFloorNum > this.currentFloor)
            {
	           synchronized(this)
	           {
	        	   requestQueue.add(internalFloorNum);
	        	   notifyAll();
	           }
	           System.out.println("Request for floor " + floorNum + " was added to elevator: " + ( this.getElevatorId() + 1 ));
            }
           else
           {
        	   System.out.println("Request for floor " + floorNum + " was rejected by the elevator: " + ( this.getElevatorId() + 1 ));
           }
           break;
        case DOWN:
            if (internalFloorNum < this.currentFloor)
            {
            	synchronized(this)
            	{
            		requestQueue.add(internalFloorNum);
            		notifyAll();
            	}
                System.out.println("Request for floor " + floorNum + " was added to elevator: " + ( this.getElevatorId() + 1 ));
            }
            else
            {
            	System.out.println("Request for floor " + floorNum + " was rejected by the elevator: " + ( this.getElevatorId() + 1 ));
            }
            break;
        case IDLE:
        	synchronized(this)
        	{
        		requestQueue.add(internalFloorNum);
        		notifyAll();
        	}
            System.out.println("Request for floor " + floorNum + " was added to elevator: " + ( this.getElevatorId() + 1 ));
            if ( currentFloor < internalFloorNum)
            {
            	direction = Direction.UP;
            }
            else
            {
            	//compare to see if the floorNumber is greater than the current floor
            	//If true then assign the elevator to the request.
            	direction = Direction.DOWN;
            }
            break;
        }
	}

   /**
    * Add a passenger to the elevator.  Adds person object.
    * @param inPassenger The number of people being removed from the elevator.This number cannot be negative, and should be added to the passenger list.
    * @throws NullPassengerException if inPassenger is set to null
    */
	@Override
	public synchronized void addPassenger(Person inPassenger) throws NullPassengerException
	{
		if(inPassenger == null)
		{
			throw new NullPassengerException("The passenger object that is being added to the elevator is null!");
		}
		this.passengerList.add(inPassenger);
		this.addFloorToQueue(inPassenger.getDestinationFloor());
	}


   /**
 	* Add multiple passengers to the elevator.
    * @param inPassengers The number of people being added to the elevator.This number cannot be negative, and all passengers should be added to the list.
    * @throws NullPassengerException if any of the passengers contained within inPassengers is null
    */
	@Override
	public synchronized void addPassengers(ArrayList<Person> inPassengers) throws NullPassengerException
	{
		if(inPassengers.contains(null))
		{
			throw new NullPassengerException("The passenger object that is being added to the elevator is null!");
		}
		this.passengerList.addAll(inPassengers);
    }
	
	/**
	 * Allows for the doors of the elevator to be opened. A 500 ms wait time is added to allow for the removal and addition of passenger(s)
	 * @throws InterruptedException if the elevator doors have any interence, then print a stackTrace.
	 */
	@Override
	public synchronized void openDoors() 
	{
		this.bDoorsOpen = true;
		 try
		 {
			 ElevatorControlModule.getInstance().elevatorDoorsOpened(this, this.currentFloor);
			 
			 //find any passengers who are supposed to get off on this floor and remove them
			 for( int i = 0; i < this.passengerList.size(); ++i)
			 {
				 if(this.passengerList.get(i).getDestinationFloor() == this.currentFloor)
				 {
					 this.passengerList.remove(i);
				 }
			 }
			 wait(doorSpeed);
	     	}
		 catch (InterruptedException | NegativeFloorException | NegativeCapacityException | NegativeElevatorException e)
		 {
			e.printStackTrace();
		 }
	}

	/**
	 * Allows for the doors of the elevator to be close. A 500 ms wait time is added to allow for the removal and addition of passenger(s)
	 */
	@Override
	public synchronized void closeDoors() 
	{
		this.bDoorsOpen = false;
		
		//time to close doors, add a wait
    	try
    	{
    		wait(doorSpeed);
    	}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

   /**
    * Accessor for the elevator's current direction.
    * @return The current direction of this elevator object.
    */
	@Override
	public Direction getDirection()
	{
		return this.direction;
	}
	
   /**
    * Remove a specific passenger from the elevator.
    * @param inPassenger The passenger that will be removed from the elevator. inPassenger's id must be inside of the passengerList and should not be negative.
    * @throws PassengerNotFoundException if the passenger passed isn't in the passenger list.
    */
	@Override
	public synchronized void removePassenger(Person inPassenger) throws PassengerNotFoundException
	{            
	     if  (!passengerList.contains(inPassenger))
	     {
	    	 throw new PassengerNotFoundException("The passenger object that was meant to be removed is not present in the elevator!");
	     }
	     passengerList.remove(inPassenger);
	}
	
   /**
    * Remove multiple passengers from the elevator.
    * @param inPeople The passengers that will be removed from the elevator. Each value in inPeople must be inside of the passengerList and should not be negative.
    * @throws NegativePassengerException throws an exception if the passenger value passed is negative.
    * @throws InvalidIndexException throws an exception if the passenger passed isn't in the passenger list.
    */
	@Override
	public synchronized void removePassengers(ArrayList <Person> inPeople) throws PassengerNotFoundException
	{
		if(!this.passengerList.containsAll(inPeople))
		{
			throw new PassengerNotFoundException("At least one of the passenger objects that were meant to be removed are not present in the elevator!");
		}
		this.passengerList.removeAll(inPeople);			
	}

   /**
    * Accessor for the capacity of this elevator object.
    * @return the number of people that the elevator object can hold.
    */
	@Override
	public int getCapacity() 
	{	
		return this.capacity;
	}

   /**
    * Retrieves all passengers in the passengerList.
    * @return the collection of passengers in the list as an ArrayList.
    */
	@Override
	public ArrayList<Person> getPassengers() 
	{
		return this.passengerList;
	}
	
   /**
    * Retrieves the elevator's id.
    * @return returns the id that corresponds to the elevator that requested this method.
    */
	@Override
	public int getElevatorId() 
	{
		return this.elevatorId;
	}
	
   /**
	* Queries the state of the elevator's doors.
    * @return returns true if the doors are open or false if the doors are closed.
    */
	@Override
	public boolean isOpen() 
	{
		return bDoorsOpen;
	}
    
   /**
    * Accessor for the running state of the elevator.
    * @return returns true if the elevator is currently running and accepting floor requests or false if the system is inactive.
    */
	@Override
	public boolean isRunning() 
	{
    	return running;
	}
	
   /**
    * Stops the elevator from running immediately without returning to its default floor. Once shut down, it cannot be started up again.
    */
	@Override
	public void shutDown()
	{
		this.running = false;
	}
	
   /**
    * Starts the elevator system process. Creates a timer to record the events of the elevator.
    */
	@Override
	public void run()
	{
		try
		{
			long tStart = System.currentTimeMillis();
			
			System.out.println("Elevator " + ( getElevatorId() + 1 ) + " has started");
		    running = true;
	        while (running)
	        {
	        	
	        	// if current floor is in request queue.
	        	
	        	if (requestQueue.contains(this.currentFloor))
	        	{
	        	  this.openDoors();
	        	  this.closeDoors();
	        	  requestQueue.remove((Integer)this.currentFloor);
	        	}
	        	
	        	// if queue is empty  switch to idle.
	        	if (requestQueue.isEmpty())
	        	{
	        		this.direction = Direction.IDLE;
	        		tStart = System.currentTimeMillis();
	        		
	        	}
	        	
				synchronized(this)
				{
					switch (direction)
					{
					case IDLE:
						wait(10000);
						tStart = System.currentTimeMillis() - tStart;
				
						//only add a new request (and add an entry to the log) if the elevator is idle and isn't already at its default floor
						if (tStart >= 10001 && this.currentFloor != 1)
						{
							System.out.println("Elevator " + ( getElevatorId() + 1 ) + " has been idle for 10 seconds. Returning to floor 1");
							addFloorToQueue(1);
						}
						
						break;
					case UP:
						tStart = System.currentTimeMillis();
						wait(500);
						if(this.currentFloor < this.maxFloors)
						{
							this.currentFloor++;
				            System.out.println("Elevator " + ( getElevatorId() + 1 ) + " passing floor " + ( currentFloor + 1 ) );
						}
						else if(this.currentFloor == this.maxFloors)
						{
							this.direction = Direction.IDLE;
						}
			
						break;
					case DOWN:
						tStart = System.currentTimeMillis();
						wait(500);
						if(this.currentFloor > this.minFloors)
						{
							this.currentFloor--;
				            System.out.println("Elevator " + ( getElevatorId() + 1 ) + " passing floor " + ( currentFloor + 1 ) );
						}
						else if(this.currentFloor == this.minFloors)
						{
							this.direction = Direction.IDLE;
						}
					}
				}
	        	}
		}
		catch(InterruptedException e)
        {
        	e.printStackTrace();
		}
	}
	
   /**
    * Initializes the collection which holds the floor requests that this elevator object should respond to.
    * Will not create a new collection object if one exists already
    */
	private synchronized void initializeRequestQueue()
	{
		if(this.requestQueue == null)
		{
			this.requestQueue = new ArrayList<Integer>();
		}
	}
	
   /**
    * Sets the identifier number assigned to this elevator.
    * private variable - only to be used to give an id to an elevator.
    * @param inId The unique identifier number. This number need not be in consecutive order compared to other elevators in the building, but it must be no larger than the 
    * maximum number of elevators in the simulation environment.
    */
	private void setId(int inId)
	{
		this.elevatorId = inId;
	}
	
   /**
    * Sets the maximum number of floors.
    * private variable - only to be used to handle the maximum elevator floors.
    * @param inMaxFloors The total number of floors the elevator can visit.
    */
	private void setMaxFloors(int inMaxFloors)
	{
		this.maxFloors = inMaxFloors - 1;
	}

   /**
    * Sets the minimum number of floors.
    * private variable - only to be used to handle the minimum elevator floors.
    * @param inMinFloors The lowest numbered floor the elevator can visit.
    * @throws NegativeFloorException if inMinFloors is less than 0 (using ZERO-BASED indexing)
    */
	private void setMinFloors(int inMinFloors) throws NegativeFloorException
	{
		if(inMinFloors < 1)
		{
			throw new NegativeFloorException("Attempting to set the minimum floor value of an elevator to a negative value! (inMinFloors: " + inMinFloors + ")");
		}
		this.minFloors = inMinFloors - 1;
	}
	
   /**
    * Sets the initial direction for the elevator to IDLE.
    * private variable - only to be used to give a default direction to an elevator.
    */
	private void setInitialDirection()
	{
		this.direction = Direction.IDLE;
	}
	
   /**
    * Sets the initial capacity for the elevator.
    * private variable - only to be used to give a default direction to an elevator.
    * @param inCap the total capacity limit the elevator can hold.
    * @throws NegativeCapacityException if inCap is negative
    */
	private void setCapacity(int inCap) throws NegativeCapacityException
	{
		if(inCap < 1)
		{
			throw new NegativeCapacityException("Attempted to set the capacity of the elevator to a negative number! (inCap: " + inCap + ")");
		}
		this.capacity = inCap;
	}

   /**
    * Sets the initial floor for the elevator.
    * private variable - only to be used to give a default floor to an elevator.
    * @param floor The default floor level that will be assigned to an elevator.
    */	
	private void setDefaultFloor(int floor)
    {
		//TODO: this shouldn't be touching currentFloor. There should be a defaultFloor member that this method will modify instead
    	this.currentFloor = floor;
	}
    	
   /**
    * Creates the passengerList to be used with the elevators.
    * private variable - only to be used create the passengerList.
    */    
	private void createPassengerList()
	{
		passengerList = new ArrayList<Person>();
	}

	/**
	 * Accessor which retrieves the current floor of this elevator
	 * @return the index of the current floor that the elevator is on.
	 */
	@Override
	public int getCurrentFloor()
	{
		return this.currentFloor;
	}
}
