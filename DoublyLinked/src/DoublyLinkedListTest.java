import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 * Test class for Doubly Linked List
 *
 * @author
 * @version 13/10/16 18:15
 */
@RunWith(JUnit4.class)
public class DoublyLinkedListTest
{
	// ~ Constructor ........................................................
	@Test
	public void testConstructor()
	{
		new DoublyLinkedList<Integer>();
	}

	// ~ Public Methods ........................................................

	// ----------------------------------------------------------
	/**
	 * Check if the insertBefore works
	 */
	@Test
	public void testInsertBefore()
	{
		// test non-empty list
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(0, 1);
		testDLL.insertBefore(1, 2);
		testDLL.insertBefore(2, 3);

		testDLL.insertBefore(0, 4);
		assertEquals("Checking insertBefore to a list containing 3 elements at position 0", "4,1,2,3",
				testDLL.toString());
		testDLL.insertBefore(1, 5);
		assertEquals("Checking insertBefore to a list containing 4 elements at position 1", "4,5,1,2,3",
				testDLL.toString());
		testDLL.insertBefore(2, 6);
		assertEquals("Checking insertBefore to a list containing 5 elements at position 2", "4,5,6,1,2,3",
				testDLL.toString());
		testDLL.insertBefore(-1, 7);
		assertEquals(
				"Checking insertBefore to a list containing 6 elements at position -1 - expected the element at the head of the list",
				"7,4,5,6,1,2,3", testDLL.toString());
		testDLL.insertBefore(7, 8);
		assertEquals(
				"Checking insertBefore to a list containing 7 elemenets at position 8 - expected the element at the tail of the list",
				"7,4,5,6,1,2,3,8", testDLL.toString());
		testDLL.insertBefore(700, 9);
		assertEquals(
				"Checking insertBefore to a list containing 8 elements at position 700 - expected the element at the tail of the list",
				"7,4,5,6,1,2,3,8,9", testDLL.toString());

		// test empty list
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(0, 1);
		assertEquals(
				"Checking insertBefore to an empty list at position 0 - expected the element at the head of the list",
				"1", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(10, 1);
		assertEquals(
				"Checking insertBefore to an empty list at position 10 - expected the element at the head of the list",
				"1", testDLL.toString());
		testDLL = new DoublyLinkedList<Integer>();
		testDLL.insertBefore(-10, 1);
		assertEquals(
				"Checking insertBefore to an empty list at position -10 - expected the element at the head of the list",
				"1", testDLL.toString());
	}
	
	/**
	 * Check if the IsEmpty method works
	 */
	@Test
	public void testIsEmpty()
	{
		// when list is empty
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		assertTrue(testDLL.isEmpty());

		// when list isn't empty
		testDLL.insertBefore(0, 1);
		assertFalse(testDLL.isEmpty());
	}

	/**
	 * Check if the get method works
	 */
	@Test
	public void testGet()
	{

		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();

		// test empty
		assertNull(testDLL.get(0));

		// test inserting element to empty list
		testDLL.insertBefore(0, 1);
		int expected = 1;
		int result = testDLL.get(0);
		assertEquals(expected, result);

		// test non_empty multi element
		testDLL.insertBefore(0, 2);
		testDLL.insertBefore(0, 3);
		testDLL.insertBefore(0, 4);
		testDLL.insertBefore(0, 5);
		testDLL.insertBefore(0, 6);
		testDLL.insertBefore(0, 7);
		testDLL.insertBefore(0, 8);

		int expectedMulti = 4;
		int resultMulti = testDLL.get(4);
		assertEquals(expectedMulti, resultMulti);

		// test position out of bounds
		assertNull(testDLL.get(-1));

	}

	/**
	 * Check if the deleteAt method works
	 */
	@Test
	public void testDeleteAt()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		// test size = 0
		assertFalse(testDLL.deleteAt(0));

		// test size = 1
		testDLL.insertBefore(0, 1);
		assertTrue(testDLL.deleteAt(0));

		// test deleting head element
		testDLL.insertBefore(0, 1);
		testDLL.insertBefore(0, 2);
		testDLL.insertBefore(0, 3);
		assertTrue(testDLL.deleteAt(0));

		// test deleting tail element
		testDLL.insertBefore(0, 3);
		assertTrue(testDLL.deleteAt(2));

		// test deleting middle element
		testDLL.insertBefore(0, 3);
		testDLL.insertBefore(0, 4);
		assertTrue(testDLL.deleteAt(2));

		// test out of bounds
		assertFalse(testDLL.deleteAt(7));
		assertFalse(testDLL.deleteAt(-1));
	}

	/**
	 * Check if the reverse method works
	 */
	@Test
	public void testReverse()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();

		//creating list
		testDLL.insertBefore(0, 1);
		testDLL.insertBefore(0, 2);
		testDLL.insertBefore(0, 3);
		testDLL.insertBefore(0, 4);
		testDLL.insertBefore(0, 5);
		testDLL.insertBefore(0, 6);
		
		//reverse list
		testDLL.reverse();
		assertEquals("Checking reverse on a list containing 6 elements", "1,2,3,4,5,6", testDLL.toString());
	}

	
	/**
	 * Check if the push method works
	 */
	@Test
	public void testPush()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();

		// first element being pushed onto stack
		testDLL.push(2);
		assertEquals("Checking push to a statck containing no elements", "2", testDLL.toString());

		// pushing other elements
		testDLL.push(3);
		testDLL.push(4);
		assertEquals("Checking push to a statck already containing elements", "4,3,2", testDLL.toString());

	}

	
	/**
	 * Check if the pop method works
	 */
	@Test
	public void testPop()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
		// pop last element
		testDLL.push(1);
		int first = testDLL.pop();
		int expected = 1;

		assertEquals(expected, first);

		// pop multiple elements
		testDLL.push(1);
		testDLL.push(2);
		testDLL.push(3);
		testDLL.push(4);

		first = testDLL.pop();
		expected = 4;

		assertEquals(expected, first);

	}

	
	/**
	 * Check if the enqueue method works
	 */
	@Test
	public void testEnqueue()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();

		// first element being enqueued
		testDLL.enqueue(2);
		assertEquals("Enqueuing first element", "2", testDLL.toString());

		// enqueuing other elements
		testDLL.enqueue(3);
		testDLL.enqueue(4);
		assertEquals("Enqueuing other elements", "4,3,2", testDLL.toString());
	}

	
	/**
	 * Check if the dequeue method works
	 */
	@Test
	public void testDequeue()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();

		// dequeuing when size == 0
		assertNull(testDLL.dequeue());

		// dequeuing when size == 1
		testDLL.enqueue(1);
		int item = testDLL.dequeue();
		int expected = 1;
		assertEquals(expected, item);

		// dequeuing multiple elements
		testDLL.enqueue(1);
		testDLL.enqueue(2);
		testDLL.enqueue(3);
		testDLL.enqueue(4);

		item = testDLL.dequeue();
		item = testDLL.dequeue();
		expected = 2;
		assertEquals(expected, item);
	}

	/**
	 * Check if the toString method works
	 */
	@Test
	public void testToString()
	{
		DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();

		// test string method with one element
		testDLL.insertBefore(0, 1);
		assertEquals("Testing string method with one element", "1", testDLL.toString());

		// test string with multiple elements
		testDLL.insertBefore(0, 2);
		testDLL.insertBefore(0, 3);
		assertEquals("Testing string method for multiple elements", "3,2,1", testDLL.toString());

	}

}
