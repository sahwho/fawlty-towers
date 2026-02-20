import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import org.junit.Test;

public class TowersTests {
	@Test
	public void test5Disks() {
		int numDisks = 5;
		
		ArrayList<Stack<Integer>> towers = new ArrayList<Stack<Integer>>();
		Stack<Integer> s1 = new Stack<Integer>();
		Stack<Integer> s2 = new Stack<Integer>();
		Stack<Integer> s3 = new Stack<Integer>();
		
		// initialize disks onto S1
		for (int i=numDisks; i>0; i--) {
			s1.push(i);
		}
		
		towers.add(s1);
		towers.add(s2);
		towers.add(s3);
		
		int destinationIndex = 3; // this is 1-based!!!
		Towers hanoi = new Towers(numDisks);
		ArrayList<String> moves = hanoi.play(destinationIndex);
		
		for (String move: moves) {
			String[] parsed = move.split(","); // should have length 3
			
			assertEquals("the move String is the right length", 3, parsed.length);
			
			int disk = Integer.parseInt(parsed[0].trim());
			
			//source, destination are 0-based
			int source = Integer.parseInt(parsed[1].trim().substring(1))-1;//"S1" --> index 0
			int destination = Integer.parseInt(parsed[2].trim().substring(1))-1;//"S2" --> index 1
			
			Stack<Integer> src = towers.get(source);
			Stack<Integer> dst = towers.get(destination);
			
			//0-based
			int buffer = 3 - (source + destination); // sum indices of all stacks (0,1,2) to get 3
			
			// by subtracting the source and destination indices, we get the index of the remaining stack
			Stack<Integer> bfr = towers.get(buffer);
			
			assertTrue("the disk being moved is on top of the source stack", src.peek()==disk);
			
			//ensure that each stack is valid
			assertTrue("the source stack is valid", isValidStack(src));
			assertTrue("the destination stack is valid", isValidStack(dst));
			assertTrue("the buffer stack is valid", isValidStack(bfr));
			
			//make the move from src --> dst
			dst.push(src.pop());
			
			//ensure that each stack is valid after making the move
			assertTrue("the source stack is valid", isValidStack(src));
			assertTrue("the destination stack is valid", isValidStack(dst));
			assertTrue("the buffer stack is valid", isValidStack(bfr));
		}
		
		// ensure that the stack at the destination index is "full" with n items
		assertTrue("the stack at the destination index (0-based) is full with n items", towers.get(destinationIndex-1).size()==numDisks);
		assertTrue("the stack at the destination index (0-based) is a valid stack", isValidStack(towers.get(destinationIndex-1)));
		
		// there is an edge case where src==dst, where we don't want to assert that src winds up empty:
		if (destinationIndex!=1) {
			assertTrue("when the source is not the destination, the source (index 0) winds up empty", towers.get(0).isEmpty());
			// don't need to check if valid here, as the stack is empty
			
			// to get the index of the "buffer" Stack, just do 3 (0+1+2) - 0 (src) - destinationIndex.
			// the indices are all 0-based, so don't need to do any "-1"s.
			// to see if this works:
			// with src=0, dst=2: 3-0-2 = 1. (this gives us the second stack (index 1), which is correct)
			// with src=0, dst=1: 3-0-1 = 2. (this gives us the third stack (index 2), which is correct)
			
			assertTrue("when the source is not the destination, the buffer stack winds up empty", towers.get(3-(destinationIndex-1)).isEmpty());
		}
	}

	/*
	 * To check if a stack is valid, ensure that each "disk" is "smaller" than the one below it.
	 */
	public static boolean isValidStack(Stack<Integer> s) {
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		ArrayList<Integer> secondary = new ArrayList<Integer>();
		
		while (!s.isEmpty()) {
			Integer val = s.pop();
			buffer.add(val);
			secondary.add(val);
		}
		
		Collections.sort(buffer);
		
		//push back onto s so s doesn't wind up empty, causing an empty stack exception and therefore crashing
		for (int i=secondary.size()-1; i>=0; i--) {
			s.push(secondary.get(i));
		}
		
		return buffer.equals(secondary);
	}
}
