package com.example.datastructure;

public class BalancedBinaryTree {

	public static void main(String[] args) {
		BalancedBinaryTree bst = new BalancedBinaryTree();
		bst.insert(1);
		bst.insert(2);
		bst.insert(3);
		bst.insert(4);
		bst.print(bst.root);
		System.out.println();
		System.out.println(bst.height(bst.root));
		System.out.println(bst.size(bst.root));
	}
	
	TreeNode root = null;
	
	public TreeNode insert(int val) {
		root = insert(val, root);
		return root;
	}
	
	public TreeNode insert(int val, TreeNode node) {
		if(node == null) {
			return new TreeNode(val);
		}
		if(val < node.val) {
			node.left = insert(val, node.left); 		
		}else if(val > node.val){
			node.right =insert(val, node.right); 	
		}
		
		System.out.println("height: " + height(node));
		System.out.println("balance: " + (height(node.left) - height(node.right)));
		return node;
	}
	
	public int size(TreeNode node) {
		if(node == null) {
			return 0;
		}
		
		return size(node.left) + size(node.right) + 1;
	}
	
	public int height(TreeNode node) {
		if(node == null) {
			return 0;
		}
		
		int lHeight = height(node.left);
		int rHeight = height(node.right);
		return Math.max(lHeight, rHeight) + 1;
	}
	
	public void print(TreeNode node) {
		if(node == null) {
			return;
		}
		print(node.left);
		System.out.print(node.val + " ");;
		print(node.right);
	}
	
	static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode(int val){
			this.val = val;
		}
	}
}
