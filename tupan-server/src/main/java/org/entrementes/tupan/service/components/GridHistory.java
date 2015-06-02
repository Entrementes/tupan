package org.entrementes.tupan.service.components;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.entrementes.tupan.model.CostDifferentials;

public class GridHistory {
	
	private Queue<Float> readHistory;
	
	private Integer bufferSize;
	
	public GridHistory(int bufferSize){
		this.readHistory = new CircularFifoQueue<Float>(bufferSize);
		this.bufferSize = bufferSize;
	}
	
	public byte[] buildPayload() {
		Queue<Float> history = new LinkedList<Float>(this.readHistory);
		Float[] lastDifferentials = readBuffer(history);
		return encode(lastDifferentials).getBytes();
	}

	private String encode(Float[] lastDifferentials) {
		StringBuilder encoder = new StringBuilder("[");
		for(int i = 0; i < lastDifferentials.length; i++){
			if(lastDifferentials[i] != null){
				encoder.append(lastDifferentials[i]);
				if(i < lastDifferentials.length - 1 ){
					encoder.append(",");
				}
			}
		}
		encoder.append("]");
		String result = encoder.toString();
		encoder = null;
		return result;
	}

	public void add(Float electricalDifferential) {
		this.readHistory.add(electricalDifferential);
	}

	public CostDifferentials buildHistory() {
		CostDifferentials result = new CostDifferentials();
		Queue<Float> history = new LinkedList<Float>(this.readHistory);
		Float[] lastDifferentials = readBuffer(history);
		result.setCostDifferentials(lastDifferentials);
		return result;
	}

	public Float[] readBuffer(Queue<Float> history) {
		Float[] lastDifferentials = new Float[this.bufferSize];
		int length = history.size();
		length = length > this.bufferSize ? this.bufferSize : length;
		for(int i = 0; i < length; i++){
			lastDifferentials[i] = history.poll();
		}
		return lastDifferentials;
	}

}
