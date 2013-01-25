package com.josephbleau.projects;

import com.josephbleau.collections.Queue;

public class QueueSimulator {
	/* Returns a Poisson distributed random value. */
	public static int getPoisson(double lambda) {
	  double L = Math.exp(-lambda);
	  double p = 1.0;
	  int k = 0;

	  do {
	    k++;
	    p *= Math.random();
	  } while (p > L);

	  return k - 1;
	}
	
	public static void main(String[] args) {
		Queue<Integer> queue = new Queue<Integer>();
		
		/* Theorem parameters. */
		double lambda = 10; 
		double T = 5; 
		
		/* Simulation parameters */
		int tick = 0;
		int until_tick = 100000;

		int in_queue = 0;
		double total_queue_sizes = 0;
		
		while(tick++ < until_tick) {
			int new_inserts = getPoisson(lambda);
			
			/* New elements entering the queue. */
			for(int i = 0; i < new_inserts; i++) {
				queue.offer(tick);	
				in_queue++;
			}
			
			/* Remove elements from the front who
			 * will have been serviced by now. */
			while(queue.peek() != null &&
				  tick - queue.peek() >= T ) {
				queue.remove();
				in_queue--;
			}
			
			total_queue_sizes += in_queue;
		}
		
		System.out.println("Little's theorom states that N = lamda * T:");
		System.out.println("----------------------------------------------");
		System.out.println("Simulation ran for: " + (tick-1) + " ticks.");
		System.out.println("Simulation variables: lambda = " + lambda + ", T = " + T);
		System.out.println("Expected outcome: ~" + lambda * T);
		System.out.println("Rounded simulaton outcome: " + Math.round(total_queue_sizes / tick));
		System.out.println("Simulation outcome: " + total_queue_sizes / tick);
		System.out.println("----------------------------------------------");
	}
}
