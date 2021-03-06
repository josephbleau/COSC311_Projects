package com.josephbleau.projects;

import com.josephbleau.collections.Pair;
import com.josephbleau.collections.PriorityQueue;
import com.josephbleau.collections.Queue;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class LittleSimulation {
    private double N;
    private double lambda;
    private double T;
    
    private int until_tick;  
    
    private ArrayList<Pair<Integer, Double>> dataset;
    
    public LittleSimulation(double lambda, double T, int until_tick) {
        this.lambda = lambda;
        this.T = T;
        this.N = 0;
        this.until_tick = until_tick;
        this.dataset = new ArrayList<>();
    }
    
    /* Accessors & Mutators */
    public void setLambda(double lambda) {
        if(lambda < 0) {
            throw new IllegalStateException();
        }
        
        this.lambda = lambda;
    }   
    public double getLambda() {
        return this.lambda;
    }   
    public void setT(double T) {
        if(lambda < 0) {
            throw new IllegalStateException();
        }
        
        this.T = T;
    }    
    public double getT(){ 
        return this.T;
    }  
    public double getN() {
        return this.N;
    } 
    public void setUntilTick(int until_tick) {
        if(until_tick < 0) {
            throw new IllegalStateException();
        }
        
        this.until_tick = until_tick;
    }
    public int getUntilTick() {
        return this.until_tick;
    }
    public ArrayList<Pair<Integer,Double>> getDataset() {
        return this.dataset;
    }
    
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

    public double runSimulation() {
        return runSimulation(0);
    }
    
    public double runSimulation(int vary) {
        PriorityQueue<Integer> queue = new PriorityQueue<Integer>();

        /* Simulation parameters */
        int tick = 0;
        
        int in_queue = 0;
        double total_queue_sizes = 0;

        while(tick++ < this.until_tick) {              
                int new_inserts = (int) this.lambda;
                int current_T = (int) this.T;
                
                if(vary == 1){
                    new_inserts = getPoisson(this.lambda);
                }
                else if(vary == 2) {
                    current_T = getPoisson(this.T);
                }
                                
                try {
                    /* If current waiting time is zero then there's
                     * no reason to insert these elements, as they
                     * are immediately served upon entering the queue.
                     */
                    if(current_T != 0){
                        /* New elements entering the queue. */
                        for(int i = 0; i < new_inserts; i++) {
                            queue.offer(tick+current_T, tick+current_T);	
                                in_queue++;
                        }
                    }
                    
                    while(queue.peek() <= tick) {
                        queue.poll();
                        in_queue--;
                    }
                }
                catch (NoSuchElementException e)
                {
                    System.out.println(e.getMessage());
                }
                
                total_queue_sizes += in_queue;
                Double present_n = (double)(total_queue_sizes / tick);
                this.dataset.add(new Pair<Integer, Double>(tick, present_n));
        }
	
        this.N = (double)(total_queue_sizes / tick);
        return this.N;
    }
}
