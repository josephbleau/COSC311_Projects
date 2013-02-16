package com.josephbleau.collections;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

public class PriorityQueue<T> {  
    private HashMap<Integer, Queue<T>> queue;
    
    public PriorityQueue()
    {
        queue = new HashMap<>();
    }
    
    public boolean offer(Integer priorityLevel, T d)
    {
        if(queue.containsKey(priorityLevel))
        {
            queue.get(priorityLevel).offer(d);
        }
        else
        {
            queue.put(priorityLevel, new Queue<T>());
            queue.get(priorityLevel).offer(d);
        }
        
        return true;
    }
    
    private Queue<T> getFirstQueue()
    {
        Set<Integer> keySet = queue.keySet();
        TreeSet<Integer> sortedKeySet = new TreeSet<>(keySet);
        
        try {
            Integer i = sortedKeySet.first(); // May throw an exception if hashmap is empty.
            Queue<T> firstQueue =  queue.get(i);
        
            if(firstQueue.isEmpty())
            {
                queue.remove(i);
                return getFirstQueue();
            }
            else {
                return firstQueue;
            }
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public T peek()
    {
        Queue<T> firstQueue = getFirstQueue();
        if(firstQueue != null) {
            return firstQueue.peek();
        }

        return null;
    }
    
    public T poll() throws NoSuchElementException
    {
        Queue<T> firstQueue = getFirstQueue();
        if(firstQueue != null)
        {
            T item = firstQueue.poll();
            
            return item;
        }
        
        return null;
    }
    
    public void remove()
    {
        Queue<T> firstQueue = getFirstQueue();
        if(firstQueue != null)
        {
            firstQueue.remove();
        }
    }
}
