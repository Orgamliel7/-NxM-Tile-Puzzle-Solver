import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 * This class represents DFBnB algorithm
 * @author danie
 *
 */
public class DFBnB extends HeuristicAlgorithm {

	public DFBnB(PuzzleState startPuzzle, boolean withTime) {
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
		Stack<PuzzleNode> L = new Stack<>();
		HashMap<PuzzleNode, PuzzleNode> H = new HashMap<>();
		HashMap<PuzzleNode, Boolean> outMarker = new HashMap<>();
		ArrayList<PuzzleNode> N = new ArrayList<>();
		PuzzleNode currentChild;
		
		L.add(_startPuzzleNode);
		H.put(_startPuzzleNode, _startPuzzleNode);
		int t = Integer.MAX_VALUE;		
		outMarker.put(_startPuzzleNode, false);
		
		while(!L.isEmpty()) {
			PuzzleNode n = L.pop();
			if(outMarker.get(n))
				H.remove(n);//TODO: never gets here
			else {
				outMarker.put(n, true);
				_numOfNodesCreated++;
				L.add(n);
				N = createN(n, outMarker);
				N.sort(new puzzleNodeComperator()); //sort N by f
				Iterator<PuzzleNode> it = N.iterator();
				while(it.hasNext()) {
					currentChild = it.next();
					if(f(currentChild) >= t) {
						N.subList(N.indexOf(currentChild), N.size()).clear();//remove currentChild and after
						break;
					}
					else if(H.containsKey(currentChild)) {
						if(outMarker.get(currentChild))
							it.remove();//TODO: never gets here
						else {								
							if(f(H.get(currentChild)) <= f(currentChild))
								it.remove();
							else {
								L.remove(currentChild);//TODO: never gets here
								H.remove(currentChild);
							}
								
						}
					}
					else if(currentChild.get_data().isGoal()) {
						t = f(currentChild);
						getPathFromGoal(currentChild);
						_cost = currentChild.get_nodeCost();
						N.subList(N.indexOf(currentChild), N.size()).clear();//remove currentChild and after
						break;
					}
					
				} //end while over N
				
				//insert N in a reverse order to L and H
				Collections.reverse(N);
				L.addAll(N);
				for (PuzzleNode puzzleNode : N) H.put(puzzleNode, puzzleNode); //insert into H	
				
			}//end Else
		}//end while
		
		if(_withTime)
			_endTimeInNannoSecconds = System.nanoTime();
		return generateOutput();
	}
	
	private int f(PuzzleNode n) {
		return n.get_nodeCost() + h(n.get_data());
	}
	
	private ArrayList<PuzzleNode> createN(PuzzleNode parent, HashMap<PuzzleNode, Boolean> outMarker) {
		PuzzleNode currentChild = parent.generateChild();
		ArrayList<PuzzleNode> rtn = new ArrayList<>();
		while(currentChild != null) {
			if(!outMarker.containsKey(currentChild))
				outMarker.put(currentChild, false);
			rtn.add(currentChild);
			currentChild = parent.generateChild();
		}
		
		return rtn;
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
