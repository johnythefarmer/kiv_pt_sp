package cz.chmelokvas.util;

import java.util.PriorityQueue;

/**
 * Prioritni fronta
 */
public class KeyPriorityQueue<E> {
	
	@SuppressWarnings("hiding")
	private final class Item<E> implements Comparable<Item<E>>{
		private final float key;
		private final E value;
		
		public Item(float key, E value) {
			this.key = key;
			this.value = value;
		}

		public float getKey() {
			return key;
		}

		public E getValue() {
			return value;
		}

		@Override
		public int compareTo(Item<E> o) {
			return Float.compare(key, o.getKey());
		}
		
		
	}
	
	private final PriorityQueue<Item<E>> queue;
	
	public KeyPriorityQueue(){
		this.queue = new PriorityQueue<Item<E>>();
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
	
	public void add(float d, E value){
		queue.add(new Item<E>(d,value));
	}
	
	public E poll(){
		return queue.poll().getValue();
	}
}
