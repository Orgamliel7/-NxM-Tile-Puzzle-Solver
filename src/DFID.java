import java.util.HashMap;

/**
 * This class is the implementation of DFID algorithm
 * @author danie
 *
 */
public class DFID extends Algorithm {

	public DFID(PuzzleState startPuzzle, boolean withTime) {
		super(startPuzzle, withTime);
	}


	@Override
	public Output solve() {
		HashMap<PuzzleNode, Boolean> visited = new HashMap<>();
		if(_withTime)
			_startTimeInNannoSecconds = System.nanoTime();
		if(_startPuzzleNode.get_data().isGoal()) {
			_path = "";
			if(_withTime)
				_endTimeInNannoSecconds = System.nanoTime();
			return generateOutput();		
		}
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			Pair<PuzzleNode, Boolean> currentAns = DFS(_startPuzzleNode, i, visited);
			_numOfNodesCreated++;

			if(currentAns.getLeft() != null) { //if found goal
				_cost = currentAns.getLeft().get_nodeCost();
				getPathFromGoal(currentAns.getLeft());
				break;
			}
			else if(!currentAns.getRight()) {//if no path
				break;
			}
			_startPuzzleNode = new PuzzleNode(_startPuzzleNode.get_data());
			visited = new HashMap<>();
		}
		if(_withTime)
			_endTimeInNannoSecconds = System.nanoTime();
		return generateOutput();
	}

	private Pair<PuzzleNode, Boolean> DFS(PuzzleNode node, int depth, HashMap<PuzzleNode, Boolean> visited) {
		visited.put(node, true);
		if(depth == 0) {
			visited.remove(node);
			_numOfNodesCreated++;
			if(node.get_data().isGoal())
				return new Pair<PuzzleNode, Boolean>(node, true);
			else {
				return new Pair<PuzzleNode, Boolean>(null, true);
			}
		}

		boolean anyRemaining = false;
		PuzzleNode currentChild = node.generateChild();
		while(currentChild != null) {
			if(visited.containsKey(currentChild)) {
				currentChild = node.generateChild();
				continue;
			}
			Pair<PuzzleNode, Boolean> rtn = DFS(currentChild, depth-1, visited);
			if(rtn.getLeft() != null) {
				rtn.setRight(true);
				return rtn;
			}
			if(rtn.getRight())
				anyRemaining = true;
			currentChild = node.generateChild();
		}
		visited.remove(node);
		return new Pair<PuzzleNode, Boolean>(null, anyRemaining);
	}

}
