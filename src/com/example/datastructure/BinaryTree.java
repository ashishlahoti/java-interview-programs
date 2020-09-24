package com.example.datastructure;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {
	
	public static void main(String[] args){
		Tree tree = new Tree(new Node1(6));
		tree.insert(new Node1(5));
		tree.insert(new Node1(3));
		tree.insert(new Node1(8));
		tree.insert(new Node1(4));
		tree.insert(new Node1(9));
		tree.insert(new Node1(2));
		tree.insert(new Node1(7));
		tree.insert(new Node1(1));
		System.out.println("in order : ");
		tree.inorder();
		System.out.println("\npre order : ");
		tree.preorder();
		System.out.println("\npost order : ");
		tree.postorder();
		System.out.println("\nbreadth first :");
		tree.breadthFirst();
		System.out.println("\ndepth first :");
		tree.depthFirst();
		System.out.println("\nIs BST : " + tree.isBST());
		
	}
}

class Tree {

	private Node1 root = null;
	
	public Tree(Node1 node) {
		this.root = node;
	}

	public Node1 getRoot() {
		return root;
	}

	public void setRoot(Node1 root) {
		this.root = root;
	}
	
	public boolean insert(Node1 node) {
		if(node == null){
			return false;
		}
		return insert(root, node);
	}
	
	private boolean insert(Node1 root, Node1 node) {
		if(node.getValue() > root.getValue()){
			if(root.getRightNode()!=null){
				return insert(root.getRightNode(), node);
			}else{
				root.setRightNode(node);
				return true;
			}
		}else{
			if(root.getLeftNode()!=null){
				return insert(root.getLeftNode(), node);
			}else{
				root.setLeftNode(node);
				return true;
			}
		}
	}
	
	public void inorder(){
		inorder(root);
	}
	
	private void inorder(Node1 node){
		if(node != null){
			inorder(node.getLeftNode());
			System.out.print(node.getValue()+" ");
			inorder(node.getRightNode());
		}
	}
	
	public void preorder(){
		preorder(root);
	}
	
	private void preorder(Node1 node){
		if(node != null){
			System.out.print(node.getValue()+" ");
			preorder(node.getLeftNode());
			preorder(node.getRightNode());
		}
	}
	
	public void postorder(){
		postorder(root);
	}
	
	private void postorder(Node1 node){
		if(node != null){
			postorder(node.getLeftNode());
			postorder(node.getRightNode());
			System.out.print(node.getValue()+" ");
		}
	}
	
	public int size(){
		return size(root);
	}
	
	private int size(Node1 node){
		if(node != null){
			return size(node.getLeftNode()) + 1 + size(node.getRightNode());
		}
		return 0;
	}
	
	public void breadthFirst(){
		breadthFirst(root);
	}
	
	private void breadthFirst(Node1 node){
		Queue<Node1> queue = new LinkedList<Node1>() ;
		if(node != null){
			queue.add(node);
			while(!queue.isEmpty()){
				Node1 iNode = queue.remove();
				System.out.print(iNode.getValue() + " ");
				if(iNode.getLeftNode() != null) {
					queue.add(iNode.getLeftNode());
				}
				if(iNode.getRightNode() != null) {
					queue.add(iNode.getRightNode());
				}
			}
		}
	}
	
	public void depthFirst(){
		depthFirst(root);
	}
	
	private void depthFirst(Node1 node){
		Stack<Node1> stack = new Stack<Node1>() ;
		if(node != null){
			stack.add(node);
			while(!stack.isEmpty()){
				Node1 iNode = stack.pop();
				System.out.print(iNode.getValue() + " ");
				if(iNode.getRightNode() != null) {
					stack.push(iNode.getRightNode());
				}
				if(iNode.getLeftNode() != null) {
					stack.push(iNode.getLeftNode());
				}
			}
		}
	}
	
	public boolean isBST(){
		return isBST(root, null, null);
	}
	
	private boolean isBST(Node1 node, Integer min, Integer max){
		if(node == null) return true;
		if(min != null && node.getValue() < min) return false;
		if(node.getLeftNode() != null && node.getValue() <= node.getLeftNode().getValue()) return false;
		if(max != null && node.getValue() > max) return false;
		if(node.getRightNode() != null && node.getValue() >= node.getRightNode().getValue()) return false;
		return isBST(node.getLeftNode(), min, node.getValue()) && isBST(node.getRightNode(), node.getValue(), max);
	}
}
class Node1 {
	
	private Node1 leftNode;
	private Node1 rightNode;
	private int value;
	
	public Node1(int value) {
		this.value = value;
	}
	
	public Node1 getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(Node1 leftNode) {
		this.leftNode = leftNode;
	}
	public Node1 getRightNode() {
		return rightNode;
	}
	public void setRightNode(Node1 rightNode) {
		this.rightNode = rightNode;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return value+"";
	}
}

