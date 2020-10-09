package com.example.datastructure;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BinaryTree {

	public static void main(String[] args) {
		Tree tree = new Tree(6);
		tree.insert(4);
		tree.insert(3);
		tree.insert(5);
		tree.insert(8);
		tree.insert(9);
		tree.insert(7);
		tree.insert(1);
		tree.insert(2);

		Tree p = new Tree(1);
		p.insert(2);

		Tree q = new Tree(1);
		q.insert(3);

		tree.root.left.right.right = new TreeNode(11);
		tree.root.left.right.right.right = new TreeNode(12);
		System.out.println("Is_Same_Tree:\t" + Tree.isSameTree(p.root, q.root));
		System.out.println("Is BST:  \t" + tree.isBST());
		System.out.println("Is BST:  \t" + tree.isBST());
		System.out.println();
		System.out.println("Max_Depth:\t" + tree.height(tree.root));
		System.out.println("Min_Depth:\t" + tree.minDepth(tree.root));
		System.out.print("In_Order:\t");
		tree.inOrderTraversal(tree.root);
		System.out.print("\nPre_Order:\t");
		tree.preOrderTraversal(tree.root);
		System.out.print("\nPost_Order:\t");
		tree.postOrderTraversal(tree.root);
		System.out.print("\nBreadth_First:\t");
		tree.breadthFirstTraversal(tree.root);
		System.out.print("\nDepth_First:\t");
		tree.depthFirstTraversal(tree.root);
		System.out.print("\nLevel_Order:\t");
		tree.levelOrderTraversal(tree.root, 3);
		System.out.println("\n Tree");
		tree.print(tree.root);
	}
}

class Tree {

	public TreeNode root = null;

	public Tree(int val) {
		this.root = new TreeNode(val);
	}

	public void insert(int val) {
		insert(this.root, val);
	}

	public void insert(TreeNode root, int val) {
		if (val <= root.val) {
			if (root.left == null) {
				root.left = new TreeNode(val);
			} else {
				insert(root.left, val);
			}
		} else {
			if (root.right == null) {
				root.right = new TreeNode(val);
			} else {
				insert(root.right, val);
			}
		}
	}

	public void inOrderTraversal(TreeNode node) {
		if (node != null) {
			inOrderTraversal(node.left);
			System.out.print(node.val + " ");
			inOrderTraversal(node.right);
		}
	}

	public void preOrderTraversal(TreeNode node) {
		if (node != null) {
			System.out.print(node.val + " ");
			preOrderTraversal(node.left);
			preOrderTraversal(node.right);
		}
	}

	public void postOrderTraversal(TreeNode node) {
		if (node != null) {
			postOrderTraversal(node.left);
			postOrderTraversal(node.right);
			System.out.print(node.val + " ");
		}
	}

	public int size(TreeNode node) {
		if (node != null) {
			return size(node.left) + 1 + size(node.right);
		}
		return 0;
	}

	public int height(TreeNode root) {
		if (root == null) {
			return 0;
		}
		return Math.max(height(root.left), height(root.right)) + 1;
	}

	public int minDepth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		if (root.left == null && root.right == null) {
			return 1;
		}
		if (root.left == null) {
			return minDepth(root.right) + 1;
		}
		if (root.right == null) {
			return minDepth(root.left) + 1;
		}
		return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
	}

	public void breadthFirstTraversal(TreeNode node) {
		Queue<TreeNode> queue = new LinkedList<TreeNode>();
		if (node != null) {
			queue.add(node);
			while (!queue.isEmpty()) {
				TreeNode iNode = queue.remove();
				System.out.print(iNode.val + " ");
				if (iNode.left != null) {
					queue.add(iNode.left);
				}
				if (iNode.right != null) {
					queue.add(iNode.right);
				}
			}
		}
	}

	int diameter = 1;

	public int diameter(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int lDepth = diameter(root.left);
		int rDepth = diameter(root.right);
		diameter = Math.max(diameter, lDepth + rDepth + 1);
		return Math.max(lDepth, rDepth) + 1;
	}

	/**
	 * Same as Pre_Order Traversal
	 */
	public void depthFirstTraversal(TreeNode node) {
		Stack<TreeNode> stack = new Stack<TreeNode>();
		if (node != null) {
			stack.add(node);
			while (!stack.isEmpty()) {
				TreeNode iNode = stack.pop();
				System.out.print(iNode.val + " ");
				if (iNode.right != null) {
					stack.push(iNode.right);
				}
				if (iNode.left != null) {
					stack.push(iNode.left);
				}
			}
		}
	}

	public void levelOrderTraversal(TreeNode root, int level) {
		if (root == null || level <= 0) {
			return;
		} else if (level == 1) {
			System.out.print(root.val + " ");
		} else if (level > 1) {
			levelOrderTraversal(root.left, level - 1);
			levelOrderTraversal(root.right, level - 1);
		}
	}

	public boolean isBST() {
		return isBST(root, null, null);
	}

	private boolean isBST(TreeNode node, Integer min, Integer max) {
		if (node == null)
			return true;
		int val = node.val;
		if (min != null && val <= min) {
			return false;
		}
		;
		if (max != null && val >= max) {
			return false;
		}
		;
		return isBST(node.left, min, val) && isBST(node.right, val, max);
	}

	public static boolean isSameTree(TreeNode p, TreeNode q) {
		if (p == null && q == null) {
			return true;
		}
		if (p == null || q == null) {
			return false;
		}
		if (p.val != q.val) {
			return false;
		}

		return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
	}

	public void print(TreeNode node) {
		int h = height(node);
		for (int i = 1; i <= h; i++) {
			levelOrderTraversal(node, i);
			System.out.println();
		}
	}

}

class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	public TreeNode(int val) {
		this.val = val;
	}

	TreeNode(int val, TreeNode left, TreeNode right) {
		this.val = val;
		this.left = left;
		this.right = right;
	}

	@Override
	public String toString() {
		return val + "";
	}
}
