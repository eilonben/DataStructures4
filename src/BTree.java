// SUBMIT
public class BTree implements BTreeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private BNode root;
	private final int t;

	/**
	 * Construct an empty tree.
	 */
	public BTree(int t) { //
		this.t = t;
		this.root = null;
	}

	// For testing purposes.
	public BTree(int t, BNode root) {
		this.t = t;
		this.root = root;
	}

	@Override
	public BNode getRoot() {
		return root;
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + t;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BTree other = (BTree) obj;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		if (t != other.t)
			return false;
		return true;
	}
	
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////


	@Override
	public Block search(int key) {
		// searches the block with key "key" in the tree
		if (root==null)
			return null;
		return root.search(key);
	}

	@Override
	public void insert(Block b) {
		// a function to insert a new block to this tree. 
		if (root==null){
			root = new BNode(t,b);
			return;
		}
		if (root.getNumOfBlocks()==(2*t-1)){
			//if the root is full and we have to insert - creates a new root with one block and add this root as its child
			BNode newNode = new BNode(t,root);
			newNode.splitChild(0);
			int i= 0;
			if (newNode.getBlockKeyAt(0)<b.getKey())
				i++;
			newNode.getChildAt(i).insertNonFull(b);
			root=newNode;
			return;
		}
		root.insertNonFull(b);
	}

	@Override
	public void delete(int key) {
		if (root!=null)
			root.delete(key);
		if (root.getNumOfBlocks()==0)// if we have deleted the last key of the root
			if (root.isLeaf())
				root=null;
			else
				root=root.getChildAt(0);
	}
	

	@Override
	public MerkleBNode createMBT() { 
		// a function to create the root of the MBT tree out of this tree ( then create all the nodes recursively)
		if (root!=null){
			MerkleBNode MBTroot=root.createHashNode();
			return MBTroot;
		}
		return null;
	}


}
