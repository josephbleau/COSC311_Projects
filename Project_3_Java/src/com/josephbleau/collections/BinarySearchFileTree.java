package com.josephbleau.collections;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.EmptyStackException;
import java.util.Stack;

public class BinarySearchFileTree implements IBinarySearchTree<Integer>
{
	/* Where the file is stored. */
	private String filename;
	private RandomAccessFile accessor;
	
	/* Stack of available positions in our fixed-memory. */
	private Stack<Integer> memMgr;
	
	/* Index into our fixed-memory that represents 
	 * where the root of our tree is stored.
	 */
	private int rootIndex;
	
	private static int END_OF_HEADER = 4;
	private static int INT_BYTES = 4;
	private static int NODE_BYTES = INT_BYTES * 3;
	private int endOfMemStack;
	private long beginOfData;
	
	private boolean verifySignature() throws IOException{
		RandomAccessFile fh = new RandomAccessFile(this.filename, "r");
		fh.seek(0);	
		
		int signature = 0;
		for(int i = 0; i < 4; ++i) {
			signature += fh.readByte();
		}
		
		fh.close();
		
		return (signature == ((byte)'J') + ((byte)'J') + ((byte)'B') + ((byte)'3'));
	}
	
	private void setupMemoryMgr() throws IOException {
		/* Read unused indices from disk:
		 * 		Load into memory all unused / garbage indices that exist
		 * 		in our file. This section is ended by the famous magic 
		 * 		number: 0xDEADBEEF.
		 */
		int END_OF_SECTION = 0xDEADBEEF;	
		this.accessor.seek(END_OF_HEADER);
		int value = 0;
		int offset = 0;
		
		while(value != END_OF_SECTION)
		{
			value = this.accessor.readInt();
			
			if(value == END_OF_SECTION)
				break;
			
			memMgr.push(value);
			++offset;
		}
		
		if(offset == 0) offset = 1;
		
		endOfMemStack = offset * INT_BYTES;
		beginOfData = END_OF_HEADER + endOfMemStack + INT_BYTES; 
	}
		
	public BinarySearchFileTree(String filename, boolean overwrite) throws IOException {
		this.filename = filename;
		File file = new File(this.filename);
		
		this.memMgr = new Stack<Integer>();
		
		if(file.exists() && !overwrite) {	

			this.accessor = new RandomAccessFile(file, "rw");
			
			/* Verify it is a BST file. */
			if(!verifySignature()) {
				throw new IllegalStateException("Signature couldn't be verified.");
			}
					
			/* Root index is the first integer after the signature. */
			this.accessor.seek(END_OF_HEADER);
			this.rootIndex = this.accessor.readInt();
							
			/* Read unused indices from disk: */
			setupMemoryMgr();
			
		} else {
			this.accessor = new RandomAccessFile(file, "rw");
			accessor.setLength(0);
			
			this.memMgr = new Stack<Integer>();
			this.rootIndex = -1;
			
			this.accessor.seek(0);
			
			/* Signature. */
			this.accessor.writeByte((byte)'J');
			this.accessor.writeByte((byte)'J');
			this.accessor.writeByte((byte)'B');
			this.accessor.writeByte((byte)'3');
			this.accessor.writeInt(-1);		/* Root index. */
			this.accessor.writeInt(0xDEADBEEF);	/* End of memory section. */
			
			this.beginOfData = this.accessor.getFilePointer();
		}	
	}
	
	public BinarySearchFileTree(String filename) throws IOException {
		this(filename, false);
	}
	
	private void seekToIndex(int index) throws IOException
	{
		this.accessor.seek(beginOfData + (NODE_BYTES * index));
	}
	
	private BSTNode<Integer> readNodeByIndex(int index) throws IOException
	{
		seekToIndex(index);
		
		BSTNode<Integer> node = new BSTNode<Integer>();
		node.data = this.accessor.readInt();	

		node.leftIndex = this.accessor.readInt();
		node.rightIndex = this.accessor.readInt();
		
		return node;
	}
	
	private void writeNodeByIndex(int index, int value, 
			                      int leftIndex, int rightIndex) 
			                          throws IOException
	{
		seekToIndex(index);
		
		this.accessor.writeInt(value);
		this.accessor.writeInt(leftIndex);
		this.accessor.writeInt(rightIndex);
	}
	
	private void writeRootIndex(int newRootIndex) throws IOException {
		this.accessor.seek(END_OF_HEADER);
		this.accessor.writeInt(newRootIndex);
	}
	
	private int readRootIndex() throws IOException {
		this.accessor.seek(END_OF_HEADER);
		return this.accessor.readInt();
	}
	
	private void writeNodeByIndex(int index, BSTNode<Integer> node) throws IOException {
		writeNodeByIndex(index, node.data, node.leftIndex, node.rightIndex);
	}
	
	private void writeLeftIndexByIndex(int nodeIndex, int leftIndex) throws IOException {
		seekToIndex(nodeIndex);
		this.accessor.readInt(); // Jump past value
		this.accessor.writeInt(leftIndex);
	}
	
	private void writeRightIndexByIndex(int nodeIndex, int rightIndex) throws IOException {
		seekToIndex(nodeIndex);
		this.accessor.readInt(); // Jump past value
		this.accessor.readInt(); // Jump past leftIndex
		this.accessor.writeInt(rightIndex);
	}
		
	private int getNextAvailableIndex() throws IOException {
		if(this.memMgr.size() == 0) {
			int eof = (int) this.accessor.length()-1;
			int index = (eof - END_OF_HEADER - endOfMemStack) / NODE_BYTES;
			
			return index;
		} else {
			return this.memMgr.pop();
		}
	}
	
	public void close() throws IOException {
/* INCOMPLETE, WRITABLE-INDICES STATE IS NOT WRITTEN TO FILE, BUT UNNECCESSARY FOR
 * THIS PROJECT DUE TO THE FACT THAT WE NEVER REUSE A STORED STRUCTURE AFTER
 * A TEST IS COMPLETED.
 */
//		if(memMgr.size() > 0) {
//			/* Create temporary files for holding our memory stack, and 
//			 * our data structure, and then merge them.
//			 */
//			this.accessor.seek(beginOfData);
//						
//			File dfile = new File("./df_tmp");
//			File mfile = new File("./mf_tmp");
//			RandomAccessFile draf = new RandomAccessFile(dfile, "rw");
//			RandomAccessFile mraf = new RandomAccessFile(mfile, "rw");
//			
//			this.accessor.seek(beginOfData);
//			while(this.accessor.getFilePointer() < this.accessor.length()) {
//				draf.writeInt(this.accessor.readInt());
//			}
//			
//			int END_OF_SECTION = 0xDEADBEEF;	
//			this.accessor.seek(END_OF_HEADER);
//			int value = 0;
//		
//			while(value != END_OF_SECTION)
//			{
//				value = this.accessor.readInt();
//				mraf.writeInt(value);
//			}
//			
//			this.accessor.seek(END_OF_HEADER + INT_BYTES);
//			
//			/* Copy back */
//			draf.seek(0);
//			mraf.seek(0);
//			while(mraf.getFilePointer() < mraf.length()) 
//				this.accessor.writeInt(mraf.readInt());
//			this.accessor.writeInt(END_OF_SECTION);
//			while(draf.getFilePointer() < draf.length())
//				this.accessor.writeInt(draf.readInt());
//			
//			
//			/* Tidy up */
//			
//			draf.close();
//			mraf.close();
//			dfile.delete();
//			mfile.delete();
//		}
		this.accessor.close();
	}
	
	/* Overload for insertion, allows creating a new node by offering
	 * just the data value instead of the awkward offer(new BSTNode(data))
	 */
	public boolean offer(Integer d) throws IOException {
		return offer(new BSTNode<Integer>(d));
	}
	
	/* Iterative node insertion. */
	@SuppressWarnings("unchecked")
	private boolean offer(BSTNode<Integer> node) throws IOException {
		if(node == null)
			return false;
		
		try {
			if(readRootIndex() == -1) {
				writeRootIndex(getNextAvailableIndex());
				writeNodeByIndex(readRootIndex(), node);
				return true;
			}
			int curIndex = readRootIndex();
			while(true) {
				BSTNode<Integer> curNode = readNodeByIndex(curIndex);

				if(node.data < curNode.data) {
					if(curNode.leftIndex == -1) {
						int leftIndex = getNextAvailableIndex();
						writeLeftIndexByIndex(curIndex, leftIndex);
						writeNodeByIndex(leftIndex, node);
						return true;
					} else {
						curIndex = curNode.leftIndex;
					}
				} else  {
					if(curNode.rightIndex == -1) {
						int rightIndex = getNextAvailableIndex();
						writeRightIndexByIndex(curIndex, rightIndex);
						writeNodeByIndex(rightIndex, node);
						return true;
					} else {
						curIndex = curNode.rightIndex;
					}
				}
			}
		}
		catch(IndexOutOfBoundsException e){
			return false;
		}
		catch(EmptyStackException e) {
			return false;
		}
	}
	
	/* Helper function: Re-establishes relationships appropriately when a 
	 * node is being deleted who only had one child.
	 */
	private void promoteSingleChild(int parentIndex, int curIndex, int childIndex) throws IndexOutOfBoundsException, IOException {
		if(childIndex == -1) {
			return;
		}
				
		if(parentIndex == -1) {
			writeRootIndex(childIndex);
		} else {
			BSTNode<Integer> parentNode = readNodeByIndex(parentIndex);

			if(curIndex == parentNode.leftIndex){
				writeLeftIndexByIndex(parentIndex, childIndex);
			} else if(curIndex == parentNode.rightIndex) {
				writeRightIndexByIndex(parentIndex, childIndex);
			}
		}
	}
	
	/* Helper function: finds the left-most child of the given node. */
	private BSTNode<Integer> getLeftmostChildNode(BSTNode<Integer> searchNode) throws IndexOutOfBoundsException, IOException{
		if(searchNode == null)
			return null;
		if(searchNode.leftIndex == -1)
			return searchNode;
		
		while(searchNode.leftIndex != -1) {
			if(searchNode.leftIndex == -1)
				return searchNode;
			else
				searchNode = readNodeByIndex(searchNode.leftIndex);
		}
		
		return searchNode;
	}
	
	/* Helper function: finds the right-most child of the given node. */
	private BSTNode<Integer> getRightmostChildNode(BSTNode<Integer> searchNode) throws IndexOutOfBoundsException, IOException{
		if(searchNode == null)
			return null;
		if(searchNode.rightIndex == -1)
			return searchNode;

		while(searchNode.rightIndex != -1) {
			if(searchNode.rightIndex == -1)
				return searchNode;
			else
				searchNode = readNodeByIndex(searchNode.rightIndex);
		}
		
		return searchNode;
	}
	
	/* Helper function: finds the left-most child of the given node. */
	private int getLeftmostChildIndex(BSTNode<Integer> searchNode) throws IndexOutOfBoundsException, IOException{	
		int index = -1;
		
		while(searchNode.leftIndex != -1) {
			if(searchNode.leftIndex == -1)
				return index;
			else{
				searchNode = readNodeByIndex(searchNode.leftIndex);
				index = searchNode.leftIndex;
			}
		}
		
		return index;
	}
	
	/* Helper function: finds the left-most child of the given node. */
	private int getRightmostChildIndex(BSTNode<Integer> searchNode) throws IndexOutOfBoundsException, IOException{	
		int index = -1;
		
		while(searchNode.rightIndex != -1) {
			if(searchNode.rightIndex == -1)
				return index;
			else{
				searchNode = readNodeByIndex(searchNode.rightIndex);
				index = searchNode.rightIndex;
			}
		}
		
		return index;
	}
	
	/* Helper function: removes the relationship between a child and its parent. */
	private void unlinkFromParent(int parentIndex, int curIndex) throws IndexOutOfBoundsException, IOException{
		if(parentIndex == -1) {
			writeRootIndex(-1);
		}
		else{
			BSTNode<Integer> parentNode = readNodeByIndex(parentIndex);
			
			if(parentNode.leftIndex == curIndex) {				
				writeLeftIndexByIndex(parentIndex, -1);
			} else {
				writeRightIndexByIndex(parentIndex, -1);
			}
		}
	}
	
	/* An iterative node deletion. */
	private boolean remove(int lastIndex, int curIndex) throws IOException {
		try {
			BSTNode<Integer> curNode = readNodeByIndex(curIndex);
			
			if(curNode.leftIndex != -1 && curNode.rightIndex == -1) {	
				promoteSingleChild(lastIndex, curIndex, curNode.leftIndex);	
			} else if( curNode.rightIndex != -1 && curNode.leftIndex == -1) {
				promoteSingleChild(lastIndex, curIndex, curNode.rightIndex);	
			} else if( curNode.leftIndex == -1 && curNode.rightIndex == -1 ) {
				unlinkFromParent(lastIndex, curIndex);
			} else { // Node has two children.
				BSTNode<Integer> lastNode = null;
				
				if(lastIndex != -1)
					lastNode = readNodeByIndex(lastIndex);				

				if(lastIndex == -1) {
					BSTNode<Integer> rootNode = readNodeByIndex(readRootIndex());
					BSTNode<Integer> rightNode = readNodeByIndex(rootNode.rightIndex);
					
					writeRootIndex(curNode.leftIndex);
					memMgr.push(rootNode.rightIndex);
					offer(rightNode);
				}
				else if(lastNode.leftIndex == curIndex) {
					int searchIndex = getRightmostChildIndex(curNode);
					writeLeftIndexByIndex(searchIndex, curNode.leftIndex);
					writeLeftIndexByIndex(lastIndex, curNode.rightIndex);
				} else {
					int searchIndex = getLeftmostChildIndex(curNode);
					writeRightIndexByIndex(searchIndex, curNode.rightIndex);
					writeRightIndexByIndex(searchIndex, curNode.leftIndex);
				}
			}
			
			this.memMgr.push(curIndex);
			return true;
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("Exception!");
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/* Iterative node search for removal. */
	public boolean remove(Integer d) throws IOException {
		if(readRootIndex() == -1)
			return false;
		
		int lastIndex = -1;
		int curIndex = readRootIndex();
			
		
		while(curIndex != -1) {
			BSTNode<Integer> curNode = readNodeByIndex(curIndex);
		
			if(d.equals(curNode.data)) {
				return remove(lastIndex, curIndex);
			} else if(d < curNode.data) {
				lastIndex = curIndex;
				curIndex = curNode.leftIndex;
			} else if(d > curNode.data) {
				lastIndex = curIndex;
				curIndex = curNode.rightIndex;
			}
		}
		
		return false;
	}
}