package com.example.datastructure;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {
	
	public static void main(String[] args){
		Tree tree = new Tree(new Node(6));
		tree.insert(new Node(5));
		tree.insert(new Node(3));
		tree.insert(new Node(8));
		tree.insert(new Node(4));
		tree.insert(new Node(9));
		tree.insert(new Node(2));
		tree.insert(new Node(7));
		tree.insert(new Node(1));
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

	private Node root = null;
	
	public Tree(Node node) {
		this.root = node;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}
	
	public boolean insert(Node node) {
		if(node == null){
			return false;
		}
		return insert(root, node);
	}
	
	private boolean insert(Node root, Node node) {
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
	
	private void inorder(Node node){
		if(node != null){
			inorder(node.getLeftNode());
			System.out.print(node.getValue()+" ");
			inorder(node.getRightNode());
		}
	}
	
	public void preorder(){
		preorder(root);
	}
	
	private void preorder(Node node){
		if(node != null){
			System.out.print(node.getValue()+" ");
			preorder(node.getLeftNode());
			preorder(node.getRightNode());
		}
	}
	
	public void postorder(){
		postorder(root);
	}
	
	private void postorder(Node node){
		if(node != null){
			postorder(node.getLeftNode());
			postorder(node.getRightNode());
			System.out.print(node.getValue()+" ");
		}
	}
	
	public int size(){
		return size(root);
	}
	
	private int size(Node node){
		if(node != null){
			return size(node.getLeftNode()) + 1 + size(node.getRightNode());
		}
		return 0;
	}
	
	public void breadthFirst(){
		breadthFirst(root);
	}
	
	private void breadthFirst(Node node){
		Queue<Node> queue = new LinkedList<Node>() ;
		if(node != null){
			queue.add(node);
			while(!queue.isEmpty()){
				Node iNode = queue.remove();
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
	
	private void depthFirst(Node node){
		Stack<Node> stack = new Stack<Node>() ;
		if(node != null){
			stack.add(node);
			while(!stack.isEmpty()){
				Node iNode = stack.pop();
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
	
	private boolean isBST(Node node, Integer min, Integer max){
		if(node == null) return true;
		if(min != null && node.getValue() < min) return false;
		if(node.getLeftNode() != null && node.getValue() <= node.getLeftNode().getValue()) return false;
		if(max != null && node.getValue() > max) return false;
		if(node.getRightNode() != null && node.getValue() >= node.getRightNode().getValue()) return false;
		return isBST(node.getLeftNode(), min, node.getValue()) && isBST(node.getRightNode(), node.getValue(), max);
	}
}
class Node {
	
	private Node leftNode;
	private Node rightNode;
	private int value;
	
	public Node(int value) {
		this.value = value;
	}
	
	public Node getLeftNode() {
		return leftNode;
	}
	public void setLeftNode(Node leftNode) {
		this.leftNode = leftNode;
	}
	public Node getRightNode() {
		return rightNode;
	}
	public void setRightNode(Node rightNode) {
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

