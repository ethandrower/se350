/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//add a comment here. 

package UnitTests.factoryTests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pImpls.*;
import pFactories.*;
import pInterfaces.ElevatorInterface;

/**
 *
 * @author Stouny
 */
public class ElevatorFactoryTest {
    private int elevatorId;
    private int capacity;
    private int maxFloors;
    private int minFloors;
    
    public ElevatorFactoryTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        elevatorId = 1;
        capacity = 10;
        maxFloors = 10;
        minFloors = 1;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of createElevator method, of class ElevatorFactory.
     */
    @Test
    public void testCreateElevator() {
        System.out.println("createElevator");
        ElevatorInterface expResult = new Elevator(elevatorId, capacity, maxFloors, minFloors);
        ElevatorInterface result = ElevatorFactory.createElevator(elevatorId, capacity, maxFloors, minFloors);
        ElevatorInterface failResult = ElevatorFactory.createElevator(elevatorId+1, capacity+1, maxFloors+1, minFloors+1);
        assertEquals(expResult.getElevatorId(), result.getElevatorId());
        assertEquals(expResult.getCapacity(), result.getCapacity());
        assertFalse(failResult.getCapacity()==expResult.getCapacity());
        assertFalse(failResult.getElevatorId()==expResult.getElevatorId());

    }
    
}
