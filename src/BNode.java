import java.util.ArrayList;

//SUBMIT
public class BNode implements BNodeInterface {

	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	// ///////////////////BEGIN DO NOT CHANGE ///////////////////
	private final int t;
	private int numOfBlocks;
	private boolean isLeaf;
	private ArrayList<Block> blocksList;
	private ArrayList<BNode> childrenList;

	/**
	 * Constructor for creating a node with a single child.<br>
	 * Useful for creating a new root.
	 */
	public BNode(int t, BNode firstChild) {
		this(t, false, 0);
		this.childrenList.add(firstChild);
	}

	/**
	 * Constructor for creating a <b>leaf</b> node with a single block.
	 */
	public BNode(int t, Block firstBlock) {
		this(t, true, 1);
		this.blocksList.add(firstBlock);
	}

	public BNode(int t, boolean isLeaf, int numOfBlocks) {
		this.t = t;
		this.isLeaf = isLeaf;
		this.numOfBlocks = numOfBlocks;
		this.blocksList = new ArrayList<Block>();
		this.childrenList = new ArrayList<BNode>();
	}

	// For testing purposes.
	public BNode(int t, int numOfBlocks, boolean isLeaf, ArrayList<Block> blocksList, ArrayList<BNode> childrenList) {
		this.t = t;
		this.numOfBlocks = numOfBlocks;
		this.isLeaf = isLeaf;
		this.blocksList = blocksList;
		this.childrenList = childrenList;
	}

	@Override
	public int getT() {
		return t;
	}

	@Override
	public int getNumOfBlocks() {
		return numOfBlocks;
	}

	@Override
	public boolean isLeaf() {
		return isLeaf;
	}

	@Override
	public ArrayList<Block> getBlocksList() {
		return blocksList;
	}

	@Override
	public ArrayList<BNode> getChildrenList() {
		return childrenList;
	}

	@Override
	public boolean isFull() {
		return numOfBlocks == 2 * t - 1;
	}

	@Override
	public boolean isMinSize() {
		return numOfBlocks == t - 1;
	}

	@Override
	public boolean isEmpty() {
		return numOfBlocks == 0;
	}

	@Override
	public int getBlockKeyAt(int indx) {
		return blocksList.get(indx).getKey();
	}

	@Override
	public Block getBlockAt(int indx) {
		return blocksList.get(indx);
	}

	@Override
	public BNode getChildAt(int indx) {
		return childrenList.get(indx);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blocksList == null) ? 0 : blocksList.hashCode());
		result = prime * result + ((childrenList == null) ? 0 : childrenList.hashCode());
		result = prime * result + (isLeaf ? 1231 : 1237);
		result = prime * result + numOfBlocks;
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
		BNode other = (BNode) obj;
		if (blocksList == null) {
			if (other.blocksList != null)
				return false;
		} else if (!blocksList.equals(other.blocksList))
			return false;
		if (childrenList == null) {
			if (other.childrenList != null)
				return false;
		} else if (!childrenList.equals(other.childrenList))
			return false;
		if (isLeaf != other.isLeaf)
			return false;
		if (numOfBlocks != other.numOfBlocks)
			return false;
		if (t != other.t)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BNode [t=" + t + ", numOfBlocks=" + numOfBlocks + ", isLeaf=" + isLeaf + ", blocksList=" + blocksList
				+ ", childrenList=" + childrenList + "]";
	}

	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////
	// ///////////////////DO NOT CHANGE END///////////////////

	@Override
	public Block search(int key) {
		// A simple function that finds recursively and returns a block with a specified key. If the key does not exist - returns null
		int i = 0;
		while (i < numOfBlocks && key > getBlockAt(i).getKey()) {
			i++;
		}
		if (i < numOfBlocks && key == getBlockAt(i).getKey())
			return getBlockAt(i);
		if (isLeaf)
			return null;
		return getChildAt(i).search(key);
	}

	@Override
	public void insertNonFull(Block d) {
		// A function that inserts recursively(until it reaches a leaf) a specified Block to a non full (numOfBlocks<2t-1) leaf node 
		int i = 0;
		if (isLeaf) {
			while (i < numOfBlocks && getBlockAt(i).getKey() < d.getKey())
				i++;
			blocksList.add(i, d);
			numOfBlocks++;
			return;
		}
		while (i < numOfBlocks && getBlockAt(i).getKey() < d.getKey())
			i++;
		if (getChildAt(i).getNumOfBlocks() == (2 * t - 1)) {
			splitChild(i);
			if (getBlockKeyAt(i) < d.getKey())
				i++;
			getChildAt(i).insertNonFull(d);
		} else
			getChildAt(i).insertNonFull(d);

	}

	/**
	 * Splits the child node at childIndex into 2 nodes.
	 * 
	 * @param childIndex
	 */
	public void splitChild(int childIndex) {
		BNode y = getChildAt(childIndex);
		BNode z = new BNode(t, y.isLeaf(), t - 1);
		// coping the blocks to the new node
		for (int j = 0; j < t - 1; j++) {
			z.getBlocksList().add(y.getBlockAt(j + t));
		}
		// coping the children to the new node
		if (!(y.isLeaf())) {
			for (int j = 0; j < t; j++) {
				z.childrenList.add(y.getChildAt(j + t));
			}
		}
		// moving the right children of the father node 1 slot to the right
		childrenList.add(getChildAt(childrenList.size() - 1));
		for (int j = childrenList.size() - 2; j > childIndex; j--) {
			childrenList.set(j + 1, getChildAt(j));
		}
		childrenList.set(childIndex + 1, z);

		if (!(isEmpty())) {
			blocksList.add(getBlockAt(numOfBlocks - 1));
			for (int j = numOfBlocks - 1; j >= childIndex; j--) {
				blocksList.set(j + 1, getBlockAt(j));
			}
			blocksList.set(childIndex, y.getBlockAt(t - 1));
		} else {
			blocksList.add(0, y.getBlockAt(t - 1));
		}

		numOfBlocks = numOfBlocks + 1;
		if (!(y.isLeaf())) {
			y.getChildrenList().subList(t, 2 * t).clear();
		}
		y.getBlocksList().subList(t - 1, 2 * t - 1).clear();
		y.numOfBlocks = t - 1;

	}

	public int findKey(int key) {
		int index = 0;
		while (index < numOfBlocks && getBlockAt(index).getKey() < key) {
			index++;
		}
		return index;
	}

	@Override
	public void delete(int key) {
		
		// Deletes a Block with a specified key from the tree. if the Block doesn't exist - does nothing.
		
		int index = findKey(key);
		if (index < numOfBlocks && getBlockAt(index).getKey() == key) {
			// if the key exists in this node
			if (isLeaf) {
				deleteFromLeaf(index);
				return;
			} else {
				deleteFromNonLeaf(index);
				return;
			}

		}
		if (isLeaf)
			// the key does not exist in the node and it's a leaf = the key does not exist in the tree.
			return;
		if (getChildAt(index).numOfBlocks < t)
			// if the child we need to delete from is minimal - we need to shift to it or to merge it
			shiftOrMergeChild(index);
		if (index > numOfBlocks)
			// if we merged the two last children, the child at "index" does not exist.
			getChildAt(index - 1).delete(key);
		else
			getChildAt(index).delete(key);

	}

	public void deleteFromLeaf(int index) {
		
		// deletes a block with specified index from a leaf node
		
		blocksList.remove(index);
		numOfBlocks--;
	}

	public void deleteFromNonLeaf(int index) {
		
		// deleting a block from non leaf
		
		int key = getBlockKeyAt(index);
		if (getChildAt(index).numOfBlocks > t - 1) {
			// If the left child is not minimal - we find the predecessor of the block we want to delete, swap them and then delete recursively the predecessor 
			Block pred = findPredecessor(index);
			blocksList.set(index, pred);
			getChildAt(index).delete(pred.getKey());
			return;
		}
		if (getChildAt(index + 1).numOfBlocks > t - 1) {
			// If the left child is minimal, we check the same for the right child and the block's successor
			Block succ = findSuccessor(index);
			blocksList.set(index, succ);
			getChildAt(index + 1).delete(succ.getKey());
			return;
		}
		// If both of the children are minimal, we have to merge them.
		mergeChildWithRightSibling(index);
		getChildAt(index).delete(key);

	}

	public Block findPredecessor(int index) { 
		// a function that finds a predecessor to the block at(index) in the subtree
		BNode tmp = getChildAt(index);
		while (!tmp.isLeaf) {
			tmp = tmp.getChildAt(tmp.getNumOfBlocks());
		}
		return tmp.getBlockAt(tmp.getNumOfBlocks() - 1);
	}

	public Block findSuccessor(int index) {
		// a function that finds a successor to the block at(index) in the subtree
		BNode tmp = getChildAt(index + 1);
		while (!tmp.isLeaf) {
			tmp = tmp.getChildAt(0);
		}
		return tmp.getBlockAt(0);
	}

	public void mergeChildWithRightSibling(int index) {
		BNode child = getChildAt(index);
		BNode sibling = getChildAt(index + 1);
		child.blocksList.add(getBlockAt(index));
		// adding the "separating" block in the father to the merged node

		for (int i = 0; i < sibling.numOfBlocks; i++)
			// adding all the sibling blocks to the child
			child.blocksList.add(sibling.getBlockAt(i));
		if (!child.isLeaf)
			for (int i = 0; i <= sibling.numOfBlocks; i++)
				// adding all the children of the sibling to the child
				child.childrenList.add(sibling.getChildAt(i));
		this.childrenList.remove(index+1);
		child.numOfBlocks = child.numOfBlocks + sibling.numOfBlocks + 1;
		this.numOfBlocks--;
		this.blocksList.remove(getBlockAt(index));
	}

	private void shiftOrMergeChild(int index) {
		// a function that shifts or merges the child at (index)
		if (index != 0 && getChildAt(index - 1).numOfBlocks > t - 1) {
			shiftFromLeftSibling(index);
			return;
		}
		if (index != numOfBlocks && getChildAt(index + 1).numOfBlocks > t - 1) {
			shiftFromRightSibling(index);
			return;
		}
		if (index != numOfBlocks)
			// Since we only merge with right sibling, we have to check if the merge target is the most right child. if it is - merge the index-1 child with index child
			mergeChildWithRightSibling(index);
		else
			mergeChildWithRightSibling(index - 1);
	}

	private void shiftFromLeftSibling(int index) {
		// A functions that takes the last block of the child's left sibling to the father, and then takes it's successor from the father to the child
		BNode child = getChildAt(index);
		BNode sibling = getChildAt(index - 1);
		child.blocksList.add(0, this.getBlockAt(index - 1));
		if (!sibling.isLeaf) {
			child.childrenList.add(0, sibling.getChildAt(sibling.numOfBlocks));
			sibling.childrenList.remove(sibling.getChildAt(sibling.numOfBlocks));
		}
		blocksList.set(index - 1, sibling.getBlockAt(sibling.numOfBlocks - 1));
		sibling.blocksList.remove(sibling.numOfBlocks - 1);
		sibling.numOfBlocks--;
		child.numOfBlocks++;

	}

	private void shiftFromRightSibling(int index) {
		// A functions that takes the first block of the child's right sibling to the father, and then takes it's predecessor from the father to the child
		BNode child = getChildAt(index);
		BNode sibling = getChildAt(index + 1);
		child.blocksList.add(this.getBlockAt(index));
		if (!sibling.isLeaf) {
			child.childrenList.add(sibling.getChildAt(0));
			sibling.childrenList.remove(sibling.getChildAt(0));
		}
		blocksList.set(index , sibling.getBlockAt(0));
		sibling.blocksList.remove(0);
		sibling.numOfBlocks--;
		child.numOfBlocks++;
	}

	@Override
	public MerkleBNode createHashNode() {
		// a function that creates a MerkleBNode out of this node, with the adequate hash code
		ArrayList<byte[]> blockDataList = new ArrayList<byte[]>();
		for (Block b : blocksList)
			blockDataList.add(b.getData());
		if (isLeaf) {
			byte[] hashSignature = HashUtils.sha1Hash(blockDataList);
			MerkleBNode output = new MerkleBNode(hashSignature);
			return output;
		} else {
			ArrayList<MerkleBNode> MBTChildrenList = new ArrayList<MerkleBNode>();
			for (BNode child : childrenList)
				MBTChildrenList.add(child.createHashNode());
			ArrayList<byte[]> combinedHash = new ArrayList<byte[]>();
			int i = 0;
			while (i < blockDataList.size()) {
				combinedHash.add(MBTChildrenList.get(i).getHashValue());
				combinedHash.add(blockDataList.get(i));
				i++;
			}
			combinedHash.add(MBTChildrenList.get(i).getHashValue());
			byte[] hashSignature = HashUtils.sha1Hash(combinedHash);
			MerkleBNode output = new MerkleBNode(hashSignature, isLeaf, MBTChildrenList);
			return output;
		}
	}
}