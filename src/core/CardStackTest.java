package core;

import core.exceptions.InvalidActionException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardStackTest {


    private CardStack testUpStack = new CardStack(Direction.UP);
    private CardStack testDownStack = new CardStack(Direction.DOWN);

    @Before
    public void setUp() throws Exception {
        testUpStack.push(new Card(20));
        testDownStack.push(new Card(80));
    }

    @Test
    public void testDistance10PushUp() throws InvalidActionException {
        testUpStack.push(new Card(10));
    }

    @Test
    public void testDistance10PushDown() throws InvalidActionException {
        testDownStack.push(new Card(90));
    }
}
