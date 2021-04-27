
public class Pair<L,R> {

	private L _left;
	private R _right;

	public Pair(L left, R right) {
		this._left = left;
		this._right = right;
	}
	public Pair() {
		this._left = null;
		this._right = null;
	}

	public L getLeft() { return _left; }
	public void setLeft(L left) {_left = left; }
	public R getRight() { return _right; }
	public void setRight(R right) {_right = right; }

	
	@Override
	public int hashCode() { return _left.hashCode() ^ _right.hashCode(); }

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) return false;
		Pair pairo = (Pair) o;
		return this._left.equals(pairo.getLeft()) &&
				this._right.equals(pairo.getRight());
	}
	@Override
	public String toString() {
		return "(" + _left + ", " + _right + ")";
	}
	
	

}
