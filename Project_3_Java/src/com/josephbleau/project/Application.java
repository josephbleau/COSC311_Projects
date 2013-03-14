package com.josephbleau.project;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

import com.josephbleau.collections.*;

public class Application {

	public static class Point {
		public int x;
		public int y;
		
		Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	
	static public<T> void test(boolean useFileTree, int iters) throws Exception {
		/* Configurable variables */
		int maxNumNodes = 1024;	            /* Maximum sized tree to be tested. */
		int numNodeGrowthFactor = 2;        /* Rate of growth in tree sizes to be tested. */
		int maxValueSize = 500;	            /* The maximum actual data value that will be stored in the tree. */
		int numIterationsPerSize = iters;      /* How many times you want to iterate over each size (smoothes the curve.)*/
		
		/* Do not touch anything below! */
		ArrayList<Point> dataPoints = new ArrayList<>();
		Random random = new Random();
		double totalAVGExecTime = 0;
		int numNodes = 2;		
		int curIterationAtSize = 0;		
		
		System.out.println("Beginning BST removal benchmarks: ");
		while(numNodes <= maxNumNodes) {
			IBinarySearchTree<Integer> bst = null;
			
			if(useFileTree)
				bst = new BinarySearchFileTree("test.txt", true);
			else
				bst = new BinarySearchTree<Integer>(maxNumNodes);
			
			ArrayList<Integer> insertedValues = new ArrayList<>();
		
			double totalExecutionTime = 0;
			int totalRealDeletes = 0;
			
			for(int i = 0; i < numNodes; ++i) {
				Integer r = random.nextInt(maxValueSize);
				insertedValues.add(r);
				bst.offer(r);
			}

			while(insertedValues.isEmpty() == false) {
				
				int randomIndex = random.nextInt(insertedValues.size());				
				Integer chosenValue = insertedValues.get(randomIndex);
				insertedValues.remove(randomIndex);
				long startExecution = System.nanoTime();
				boolean success = bst.remove(chosenValue);
				long endExecution = System.nanoTime();
				long executionTime = endExecution - startExecution;
				if(success) {
					totalRealDeletes++;
					totalExecutionTime += executionTime;
				}
			}
			
			if(useFileTree){
				((BinarySearchFileTree) bst).close();
			}
			
			if(curIterationAtSize == numIterationsPerSize) {
				Point p = new Point(numNodes, (int)(totalAVGExecTime/numIterationsPerSize));
				System.out.println("["+numNodes+" nodes]["+numIterationsPerSize+" iters] AVG Exec: " + p.y);

				dataPoints.add(p);
				
				if(numNodes <= 0)
					numNodes = numNodeGrowthFactor;
				
				numNodes *= numNodeGrowthFactor;
				curIterationAtSize = 0;
				totalAVGExecTime = 0;
			} else {
				totalAVGExecTime += (int)(totalExecutionTime/totalRealDeletes);
				curIterationAtSize++;
			}
		}
		
		/* Create a copy-and-pastable string for plotting 
		 * in Wolfram Alpha
		 */
				
		System.out.println("Wolfram Alpha: ");
		String wolframURL = "http://www.wolframalpha.com/input/?i=";
		String wolfram = "ListPlot[{";
		for(int i = 0; i < dataPoints.size(); ++i) {
			wolfram += "{" + dataPoints.get(i).x + "," + dataPoints.get(i).y + "}";
			if(i != dataPoints.size()-1)
				wolfram += ",";
		}
		wolfram += "}]";
		
		System.out.println("\tWolfram command: " + wolfram);
		System.out.println("\tLazy link: " + wolframURL + wolfram);
	}
	
	public static void main(String[] args) {
		try {
			test(false, 5000);	    // Test memory implementation
			test(true, 500);		// Test file based implementation

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
