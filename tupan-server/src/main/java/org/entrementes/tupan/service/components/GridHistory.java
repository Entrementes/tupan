package org.entrementes.tupan.service.components;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.entrementes.tupan.model.CostDifferentials;
import org.entrementes.tupan.model.TupanState;

public class GridHistory {
	
	private Queue<Double> readHistory;
	
	private Integer bufferSize;
	
	private TupanState state;
	
	public GridHistory(int bufferSize){
		this.readHistory = new CircularFifoQueue<Double>(bufferSize);
		this.bufferSize = bufferSize;
		this.state = TupanState.OK;
	}
	
	public byte[] buildPayload() {
		return buildHistory().toString().getBytes();
	}

	public void add(Double electricalDifferential) {
		this.readHistory.add(electricalDifferential);
	}

	public CostDifferentials buildHistory() {
		CostDifferentials result = new CostDifferentials();
		result.setSystemMessage(state.toString());
		Queue<Double> history = new LinkedList<Double>(this.readHistory);
		Double[] lastDifferentials = readBuffer(history);
		result.setCostDifferentials(lastDifferentials);
		return result;
	}

	public Double[] readBuffer(Queue<Double> history) {
		Double[] lastDifferentials = new Double[this.bufferSize];
		int length = history.size();
		length = length > this.bufferSize ? this.bufferSize : length;
		for(int i = 0; i < length; i++){
			lastDifferentials[i] = history.poll();
		}
		return lastDifferentials;
	}

	public void setState(TupanState state) {
		this.state = state;
	}

}
