/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pImpls;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pInterfaces.ElevatorInterface;

/**
 *
 * @author Stouny
 */
public class ElevatorTest {
    public Elevator elevator;
    public boolean isrunning = true;
    public static final int FLOOR_ONE = 1;
    public static final int FLOOR_TEN = 10;
    public static final int BASEMENT = -3;
    private boolean bDoorsOpen = true;
    private ArrayList<Person> passengerList;
    public ElevatorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        elevator = new Elevator(0,10, 15, 0);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of addFloorToQueue method, of class Elevator.
     */
    @Test
    public void addFloorToQueueTest(){
        elevator.addFloorToQueue(FLOOR_ONE);
        assertSame(FLOOR_ONE, );
       }

    /**
     * Test of addPassenger method, of class Elevator.
     */
    @Test
    public void addPassengerTest() {
        Person passenger = new Person(1);
        createPassengerList.add(1,2,3);
        System.out.println("addPassenger");
        Person inPassenger = null;
        Elevator instance = null;
        instance.addPassenger(inPassenger);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addPassengers method, of class Elevator.
     */
    @Test
    public void testAddPassengers() {
        System.out.println("addPassengers");
        passengerList.add(1);
        Person[] inPeople = null;
        Elevator instance = null;
        instance.addPassengers(inPeople);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of openDoors method, of class Elevator.
     */
    @Test
    public void openDoorsTest() {
        System.out.println("openDoors");
        elevator.openDoors();
        assertEquals(true,elevator.isOpen());
        try
        {
            if (elevator.isOpen() != true)
                fail("The doors will not open");
        }catch( IllegalArgumentException e ) {
        }
    }

    /**
     * Test of closeDoors method, of class Elevator.
     */
    @Test
    public void closeDoorsTest() {
        System.out.println("closeDoors");
        elevator.closeDoors();
        assertEquals(false,elevator.isOpen());
        if (elevator.isOpen() != false)
            fail("The doors will not close");
    }

    /**
     * Test of getDirection method, of class Elevator.
     */
    @Test
    public void getDirectionTest() {
        System.out.println("getDirection");
        Elevator instance = new Elevator(1, 10, 1, 1);
        Direction direction = Direction.IDLE;
        Direction result = instance.getDirection();
        assertEquals(direction, result);
        
    }

    /**
     * Test of removePassenger method, of class Elevator.
     */
    @Test
    public void testRemovePassenger() {
        System.out.println("removePassenger");
        passengerList p1 = new passengerList(1,0,20,150);
        Passenger p2 = new Passenger(2,5,10,150);
        Elevator instance = new Elevator(10, 5.0, 0.1, 4.0);
        instance.setDirection(Direction.UP);
        instance.setService(0, true);
        instance.setService(5, true);
        instance.setService(10, true);
        instance.setService(20,true);
        instance.setTarget(0);
        instance.setClock(new Clock(1000));
        instance.addPassenger(p1);
        instance.closeDoors();
        instance.setTarget(5);
        instance.setClock(new Clock(2000));
        instance.addPassenger(p2);
        instance.closeDoors();
        instance.setTarget(20);
        instance.setClock(new Clock(3000));
        instance.removePassenger(p1);
        assertEquals(false, instance.containsPassenger(p1));
        assertEquals(true, instance.containsPassenger(p2));
    }
    /**
     * Test of removePassengers method, of class Elevator.
     */
    @Test
    public void testRemovePassengers() {
        System.out.println("removePassengers");
        ArrayList<Person> inPeople = null;
        Elevator instance = null;
        instance.removePassengers(inPeople);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCapacity method, of class Elevator.
     */
    @Test
    public void testGetCapacity() {
        System.out.println("getCapacity");
        Elevator instance = new Elevator(1,10,10,1);
        int testResult = 10;
        int result = instance.getCapacity();
        assertEquals(testResult, result);

        try {
            if (elevator.getCapacity()==0)
                fail("Capacity cannot be 0.");
        } catch( IllegalArgumentException e ) {
        }

        try {
            if (elevator.getCapacity()<0)
                fail("Capacity cannot be negative.");
        } catch( IllegalArgumentException e ) {
        }
    }

    /**
     * Test of getPassengers method, of class Elevator.
     */
    @Test
    public void getPassengersTest() {
        System.out.println("getPassengers");
        Elevator gPassengers = new Elevator(1,10,10,1);
        ArrayList<Person> passengers = new ArrayList<Person>();
        ArrayList<Person> result = gPassengers.getPassengers();
        assertEquals(passengers, result);
        // TODO review the generated test code and remove the default call to fail.
        try{
        if (passengers == null)
            fail("No Passengers in this elevator");
        }catch(IllegalArgumentException e){
        }
    }

    /**
     * Test of getElevatorId method, of class Elevator.
     */
    @Test
    public void getElevatorIdTest() {
        System.out.println("getElevatorId");
        Elevator eID = new Elevator(1,10,10,1);
        int elevatorId = eID.getElevatorId();
        assertEquals(elevatorId,eID.getElevatorId());
    }

    /**
     * Test of shutDown method, of class Elevator.
     */
    @Test
    public void testShutDown() {
        System.out.println("shutDown");
        elevator.shutDown();
        assertEquals(false,elevator.isRunning());
    }

    /**
     * Test of run method, of class Elevator.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        Elevator instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}