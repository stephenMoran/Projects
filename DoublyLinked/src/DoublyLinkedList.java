import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

// -------------------------------------------------------------------------
/**
 *  This class contains the methods of Doubly Linked List.
 *
 *  @author  
 *  @version 13/10/16 18:15
 */

/**
 * Class DoublyLinkedList: implements a *generic* Doubly Linked List.
 * 
 * @param <T>
 *            This is a type parameter. T is used as a class name in the
 *            definition of this class.
 *
 *            When creating a new DoublyLinkedList, T should be instantiated
 *            with an actual class name that extends the class Comparable. Such
 *            classes include String and Integer.
 *
 *            For example to create a new DoublyLinkedList class containing
 *            String data: DoublyLinkedList<String> myStringList = new
 *            DoublyLinkedList<String>();
 *
 *            The class offers a toString() method which returns a
 *            comma-separated sting of all elements in the data structure.
 * 
 */
class DoublyLinkedList<T extends Comparable<T>>
{

	private int size;

	private class DLLNode
	{
		public final T data; // this field should never be updated. It gets its
								// value once from the constructor DLLNode.
		public DLLNode next;
		public DLLNode prev;

		/**
		 * Constructor
		 * 
		 * @param theData
		 *            : data of type T, to be stored in the node
		 * @param prevNode
		 *            : the previous Node in the Doubly Linked List
		 * @param nextNode
		 *            : the next Node in the Doubly Linked List
		 * @return DLLNode
		 */
		public DLLNode(T theData, DLLNode prevNode, DLLNode nextNode)
		{
			data = theData;
			prev = prevNode;
			next = nextNode;
		}
	}

	// Fields head and tail point to the first and last nodes of the list.
	private DLLNode head, tail;

	/**
	 * Constructor
	 * 
	 * @return DoublyLinkedList
	 */
	public DoublyLinkedList()
	{
		head = null;
		tail = null;
		size = 0;
	}

	/**
	 * Tests if the doubly linked list is empty
	 * 
	 * @return true if list is empty, and false otherwise
	 *
	 *         Worst-case asymptotic runtime cost: Theta(1)
	 *
	 *         Justification: Regardless of the input size this function
	 *         requires a fixed number of operations and therefore, has a
	 *         constant runtime.
	 */
	public boolean isEmpty()
	{
		if (head == null && tail == null)
		{
			return true;
		}

		return false;
	}

	/**
	 * Inserts an element in the doubly linked list
	 * 
	 * @param pos
	 *            : The integer location at which the new data should be
	 *            inserted in the list. We assume that the first position in the
	 *            list is 0 (zero). If pos is less than 0 then add to the head
	 *            of the list. If pos is greater or equal to the size of the
	 *            list then add the element at the end of the list.
	 * @param data
	 *            : The new data of class T that needs to be added to the list
	 * @return none
	 *
	 *         Worst-case asymptotic runtime cost: Theta(n)
	 *
	 *         Justification: The worst case for this function is if we are
	 *         inserting before an element in a doubly linked list with a size
	 *         greater than 0 and when we are inserting before an element that
	 *         is not located at the head or tail position of the list. In this
	 *         case the find node function(see below) is called which has
	 *         worst-case runtime of n/2. Therefore, using tilde notation we can
	 *         discard the other operations and the constant(1/2) and say that
	 *         this the worst-case asymptotic runtime for this method is
	 *         Theta(n).
	 */
	public void insertBefore(int pos, T data)
	{
		DLLNode tmp = new DLLNode(data, null, null);

		// heads can't be null when tails is not also

		if (size == 0)
		{
			tail = tmp;
			head = tmp;
		}
		else
		{
			// check if position is in bounds
			if (pos > size)
			{
				pos = size;
			}
			else if (pos < 0)
			{
				pos = 0;
			}

			// insertion
			if (pos == 0)
			{
				tmp.next = head;
				head.prev = tmp;
				head = tmp;
			}
			else if (pos == size)
			{
				tmp.prev = tail;
				tail.next = tmp;
				tail = tmp;
			}
			else
			{
				DLLNode element = findNode(pos);
				element.prev.next = tmp;
				tmp.prev = element.prev;
				element.prev = tmp;
				tmp.next = element;
			}
		}
		size++;
	}

	/**
	 * Returns the data stored at a particular position
	 * 
	 * @param pos
	 *            : the position
	 * @return the data at pos, if pos is within the bounds of the list, and
	 *         null otherwise.
	 *
	 *         Worst-case asymptotic runtime cost: Theta(n)
	 *
	 *         Justification: The worst case for this function is if we are
	 *         getting an element in a doubly linked list with a size greater
	 *         than 0 and when we are getting an element that is not located at
	 *         the head or tail position of the list. In this case the find node
	 *         function(see below) is called which has worst-case runtime of
	 *         (n/2). This is the highest factor and therefore, using tilde
	 *         notation we can discard the other operations and the
	 *         constant(1/2) and say that this the worst-case asymptotic runtime
	 *         for this method is Theta(n).
	 */
	public T get(int pos)
	{
		// check if position is in bounds
		if (pos > size - 1 || pos < 0)
		{
			return null;
		}

		DLLNode nodeToFind = findNode(pos);
		return nodeToFind.data;
	}

	/**
	 * Deletes the element of the list at position pos. First element in the
	 * list has position 0. If pos points outside the elements of the list then
	 * no modification happens to the list.
	 * 
	 * @param pos
	 *            : the position to delete in the list.
	 * @return true : on successful deletion, false : list has not been
	 *         modified.
	 *
	 *         Worst-case asymptotic runtime cost: Theta(n)
	 *
	 *         Justification: The worst case for this function is if we are
	 *         deleting an element in a doubly linked list with a size greater
	 *         than 0 and when we are deleting an element that is not located at
	 *         the head or tail position of the list. In the case we must use
	 *         the find node function which has(see below) which has worst-case
	 *         runtime of (n/2).This is the highest factor and therefore, using
	 *         tilde notation we can discard the other operations and the
	 *         constant(1/2) and say that this the worst-case asymptotic runtime
	 *         for this method is Theta(n).
	 */
	public boolean deleteAt(int pos)
	{

		if (size == 0)
		{
			return false;
		}
		else if (pos > size - 1 || pos < 0)
		{
			// check if position is in bounds
			return false;
		}
		else if (size == 1)
		{
			// heads can't be null when tails is not also
			head = null;
			tail = null;
		}
		else
		{
			if (pos == 0)
			{
				head.next.prev = null;
				head = head.next;
			}
			else if (pos == (size - 1))
			{
				tail.prev.next = null;
				tail = tail.prev;
			}
			else
			{
				DLLNode element = findNode(pos);
				element.prev.next = element.next;
				element.next.prev = element.prev;
			}
		}
		size--;
		return true;
	}

	/**
	 * Reverses the list. If the list contains "A", "B", "C", "D" before the
	 * method is called Then it should contain "D", "C", "B", "A" after it
	 * returns.
	 *
	 * Worst-case asymptotic runtime cost: Theta(n)
	 *
	 * Justification: In every case this function has to iterate over each
	 * element of the list to reverse it. Each operation inside the while loop
	 * has a cost of Theta(1). n*Theta(1) = Theta(n) therfore this is the
	 * worst-case asymptotic runtime cost.
	 */
	public void reverse()
	{
		DLLNode temp = head;
		head = tail;
		tail = temp;

		DLLNode element = head;
		DLLNode tempNode;
		while (element != null)
		{
			tempNode = element.prev;
			element.prev = element.next;
			element.next = tempNode;
			element = element.next;
		}
	}

	/*----------------------- STACK */
	/**
	 * This method should behave like the usual push method of a Stack ADT. If
	 * only the push and pop methods are called the data structure should behave
	 * like a stack. How exactly this will be represented in the Doubly Linked
	 * List is up to the programmer.
	 * 
	 * @param item
	 *            : the item to push on the stack
	 *
	 *            Worst-case asymptotic runtime cost: Theta(1)
	 *
	 *            Justification: The Worst-case asymptotic runtime cost for this
	 *            function is Theta(1) because each time something is pushed
	 *            onto the stack it requires a fixed number of operations
	 *            regardless of the size of the input or the existing list.
	 */
	public void push(T item)
	{
		this.insertBefore(0, item);
	}

	/**
	 * If only the push and pop methods are called the data structure should behave
	 * like a stack.
	 * 
	 * @return the last item inserted with a push; or null when the list is
	 *         empty.
	 *
	 *         Worst-case asymptotic runtime cost: Theta(1)
	 *
	 *         Justification: The Worst-case asymptotic runtime cost for this
	 *         function is Theta(1). Both the get and delete methods called have
	 *         a worst case runtime of Theta (1) with the input they are given.
	 *         However, we just take the highest factor. They are both the same
	 *         so the cost is 1. Each time something is popped from the stack it
	 *         requires a fixed number of operations regardless of the size of
	 *         the input or the existing list.
	 */
	public T pop()
	{
		T item = this.get(0);
		this.deleteAt(0);
		return item;
	}

	/*----------------------- QUEUE */


	 * If only the enqueue and dequeue methods are called the data structure
	 * should behave like a FIFO queue.
	 * 
	 * @param item
	 *            : the item to be enqueued to the stack
	 *
	 *            Worst-case asymptotic runtime cost: Theta(1)
	 *
	 *            Justification: The Worst-case asymptotic runtime cost for this
	 *            function is Theta(1) because each time something is enqueued
	 *            it requires a fixed number of operations regardless of the
	 *            size of the input or the existing list. When the insert before
	 *            method is called it executes the exact same operations every
	 *            time.
	 */
	public void enqueue(T item)
	{
		this.insertBefore(0, item);
	}

	/**
	 * If only the enqueue and dequeue methods are called the data structure
	 * should behave like a FIFO queue.
	 * 
	 * @return the earliest item inserted with a push; or null when the list is
	 *         empty.
	 *
	 *         Worst-case asymptotic runtime cost: Theta(1)
	 *
	 *         Justification: The Worst-case asymptotic runtime cost for this
	 *         function is Theta(1). Both the get and delete methods called have
	 *         a worst case runtime of Theta (1) with the input they are given.
	 *         However, we just take the highest factor. They are both the same
	 *         so the cost is 1. Each time something is dequeued from the stack
	 *         it requires a fixed number of operations regardless of the size
	 *         of the input or the existing list.
	 */

	public T dequeue()
	{
		T item = this.get(size - 1);
		this.deleteAt(size - 1);
		return item;
	}

	/**
	 * @return a string with the elements of the list as a comma-separated list,
	 *         from beginning to end
	 *
	 *         Worst-case asymptotic runtime cost: Theta(n)
	 *
	 *         Justification: We know from the Java documentation that
	 *         StringBuilder's append() method runs in Theta(1) asymptotic time.
	 *         We assume all other method calls here (e.g., the iterator methods
	 *         above, and the toString method) will execute in Theta(1) time.
	 *         Thus, every one iteration of the for-loop will have cost
	 *         Theta(1). Suppose the doubly-linked list has 'n' elements. The
	 *         for-loop will always iterate over all n elements of the list, and
	 *         therefore the total cost of this method will be n*Theta(1) =
	 *         Theta(n).
	 */
	public String toString()
	{
		StringBuilder s = new StringBuilder();
		boolean isFirst = true;

		// iterate over the list, starting from the head
		for (DLLNode iter = head; iter != null; iter = iter.next)
		{
			if (!isFirst)
			{
				s.append(",");
			}
			else
			{
				isFirst = false;
			}
			s.append(iter.data.toString());
		}

		return s.toString();
	}

	/**
	 * @return a node in a doubly linked list at a given position Worst-case
	 *         
	 *         asymptotic runtime cost: Theta(n)
	 * 
	 *         Justification: Calling node.next or node.prev has a cost of 1. In
	 *         the worst case when searching for an element we have to iterate
	 *         over each element in the list i.e. n*theta(1). However, by
	 *         comparing the position to the midway point in the list we can
	 *         determine what half of the list the element is in. By halving the
	 *         problem size this method has a n/2 runtime. Using tilde notation
	 *         we can discard the other operations and the constant(1/2) and say
	 *         that this the worst-case asymptotic runtime for this method is
	 *         Theta(n).
	 * 
	 * 
	 */
	private DLLNode findNode(int pos)
	{
		DLLNode nodeToFind = null;

		int midway = this.size / 2;
		if ((pos + 1) <= midway)
		{
			nodeToFind = head;
			for (int i = 0; i < pos; i++)
			{
				nodeToFind = nodeToFind.next;
			}
		}
		else
		{
			nodeToFind = tail;
			for (int i = this.size - 1; i > pos; i--)
			{
				nodeToFind = nodeToFind.prev;
			}
		}
		return nodeToFind;
	}

}
