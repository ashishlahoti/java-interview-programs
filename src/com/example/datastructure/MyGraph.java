package com.example.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyGraph<T> {

	private Map<Vertex<T>, List<Vertex<T>>> adjVertices;

	public void addVertex(T val) {
		adjVertices.putIfAbsent(new Vertex<T>(val), new ArrayList<>());
	}

	public void removeVertex(T val) {
		Vertex<T> v = new Vertex<>(val);
		adjVertices.values().stream().forEach(e -> e.remove(v));
		adjVertices.remove(v);
	}

	public void addEdge(T val1, T val2) {
		Vertex<T> v1 = new Vertex<>(val1);
		Vertex<T> v2 = new Vertex<>(val2);
		adjVertices.get(v1).add(v2);
		adjVertices.get(v2).add(v1);
	}

	public void removeEdge(T val1, T val2) {
		Vertex<T> v1 = new Vertex<>(val1);
		Vertex<T> v2 = new Vertex<>(val2);
		List<Vertex<T>> eV1 = adjVertices.get(v1);
		List<Vertex<T>> eV2 = adjVertices.get(v2);
		if (eV1 != null)
			eV1.remove(v2);
		if (eV2 != null)
			eV2.remove(v1);
	}

	List<Vertex<T>> getAdjVertices(T val) {
		return adjVertices.get(new Vertex<T>(val));
	}

}
