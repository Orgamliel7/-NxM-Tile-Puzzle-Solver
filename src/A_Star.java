import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * This class is an implementation for A* algorithm
 * @author daniel
 *
 */
public class A_Star extends HeuristicAlgorithm {

	public A_Star(PuzzleState startPuzzle, boolean withTime) {
		super(startPuzzle, withTime);
	}

	@Override
	public Output solve() {
		if(_withTime)
			_startTimeInNannoSecconds = System.nanoTime();
		if(_startPuzzleNode.get_data().isGoal()) {
			_path = "";
			if(_withTime)
				_endTimeInNannoSecconds = System.nanoTime();
			return generateOutput();		
		}
		Comparator<PuzzleNode> comparator = new puzzleNodeComperator();
		PriorityQueue<PuzzleNode> L = new PriorityQueue<>(comparator);
		HashMap<PuzzleNode, Boolean> openList = new HashMap<>();
		HashMap<PuzzleNode, Boolean> closedList = new HashMap<>();
		
		L.add(_startPuzzleNode);
		openList.put(_startPuzzleNode, true);
		
		while(!L.isEmpty()) {
			PuzzleNode n = L.remove();
			openList.remove(n);
			_numOfNodesCreated++;
			if(n.get_data().isGoal()) {
				getPathFromGoal(n);
				_cost = n.get_nodeCost();
				break;
			}
			
			closedList.put(n, true);
			PuzzleNode currentChild = n.generateChild();
			while(currentChild != null) {
				if(!closedList.containsKey(currentChild) && !openList.containsKey(currentChild)) {
					L.add(currentChild);
					openList.put(currentChild, true);
				}
				
				else if(openList.containsKey(currentChild)) {
					PuzzleNode tempNode = findInQueue(L, currentChild);
					if(tempNode.get_nodeCost() > currentChild.get_nodeCost()) {
						L.remove(tempNode);
						L.add(currentChild);
					}
				}			
				currentChild = n.generateChild();
			}
			
		}
	
		if(_withTime)
			_endTimeInNannoSecconds = System.nanoTime();
		return generateOutput();
	}
	
	//finds a puzzlenode in priorityQueue. return null if not found
	private PuzzleNode findInQueue(PriorityQueue<PuzzleNode> L, PuzzleNode n) {
		for (Iterator iterator = L.iterator(); iterator.hasNext();) {
			PuzzleNode puzzleNode = (PuzzleNode) iterator.next();
			if(puzzleNode.equals(n)) {
				return puzzleNode;
			}
		}
		return null;
	}
	
	//this comperator compares 2 puzzle nodes by their f value
	public class puzzleNodeComperator implements Comparator<PuzzleNode> {
	    @Override
	    public int compare(PuzzleNode x, PuzzleNode y) {
	    	
	    	int xTotal = x.get_nodeCost() + h(x.get_data());
	    	int yTotal = y.get_nodeCost() + h(y.get_data());
	    	
	        if (xTotal < yTotal) {
	            return -1;
	        }
	        if (xTotal > yTotal) {
	            return 1;
	        }
	        if(xTotal == yTotal) {
	        	if(x.get_operatorCreatorRank() < y.get_operatorCreatorRank())
	        		return -1;
	        	if(x.get_operatorCreatorRank() > y.get_operatorCreatorRank())
	        		return 1;
	        	
	        }
	        return 0;
	    }
	}

}
