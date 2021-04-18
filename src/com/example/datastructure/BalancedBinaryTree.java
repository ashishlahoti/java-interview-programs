package com.example.datastructure;

/**
 * Design a data structure D to support the following operations: 
 * - insert(x, D) – Insert item x into D 
 * - delete(k, D) – Delete the kth smallest element from D 
 * - member(x, D) – Return true iff x ∈ D 
 * All operations must take O(log n) time on an n-element D
 * 
 * Balanced Binary Tree is also known as AVL Tree
 */
public class BalancedBinaryTree {

	public static void main(String[] args) {
		BalancedBinaryTree bst = new BalancedBinaryTree();
		System.out.println(bst.insert(1).key);
		System.out.println(bst.insert(2).key);
		System.out.println(bst.insert(3).key);
		System.out.println(bst.insert(4).key);
		System.out.println(bst.insert(5).key);
		bst.print(bst.root);
		//System.out.println();
		//System.out.println(bst.height(bst.root));
		//System.out.println(bst.size(bst.root));
	}

	TreeNode root = null;

	public TreeNode insert(int val) {
		root = insert(root, val);
		return root;
	}

	TreeNode insert(TreeNode node, int key) { 
		  
        /* 1.  Perform the normal BST insertion */
        if (node == null) 
            return (new TreeNode(key)); 
  
        if (key < node.key) 
            node.left = insert(node.left, key); 
        else if (key > node.key) 
            node.right = insert(node.right, key); 
        else // Duplicate keys not allowed 
            return node; 
        
        return rebalance(node, key);      
    } 
	
	public TreeNode delete(TreeNode node, int key) {
	    if (node == null) {
	        return node;
	    } else if (key < node.key) {
	        node.left = delete(node.left, key);
	    } else if (key > node.key) {
	        node.right = delete(node.right, key);
	    } else { // found the node with key 
	    	// node with only one child or no child  
	        if (node.left == null || node.right == null) {
	            node = (node.left == null) ? node.right : node.left;
	        } else {
	            TreeNode mostLeftChild = mostLeftChild(node.right);
	            node.key = mostLeftChild.key;
	            node.right = delete(node.right, node.key);
	        }
	    }
	    if (node != null) {
	        node = rebalance(node, key);
	    }
	    return node;
	}
	
	public TreeNode find(int key) {
	    TreeNode current = root;
	    while (current != null) {
	        if (current.key == key) {
	            break;
	        }
	        current = current.key < key ? current.right : current.left;
	    }
	    return current;
	}
	
	private TreeNode rebalance(TreeNode node, int key) {
		/* 1. Update height of this ancestor node */
        node.height = 1 + Math.max(height(node.left), height(node.right)); 
  
        /* 2. Get the balance factor of this ancestor node to check whether this node became unbalanced */
        int balance = getBalance(node); 
  
        // If this node becomes unbalanced, then there are 4 cases 
        // Left Left Case 
        /*
			         z                                      y 
			        / \                                   /   \
			       y   T4      Right Rotate (z)          x      z
			      / \          - - - - - - - - ->      /  \    /  \ 
			     x   T3                               T1  T2  T3  T4
			    / \
			  T1   T2
         */
        if (balance > 1 && getBalance(root.left) >= 0) {
        	return rotateRight(node); 
        }
        
        // Left Right Case 
        /*
	             z                               z                           x
			    / \                            /   \                        /  \ 
			   y   T4  Left Rotate (y)        x    T4  Right Rotate(z)    y      z
			  / \      - - - - - - - - ->    /  \      - - - - - - - ->  / \    / \
			T1   x                          y    T3                    T1  T2 T3  T4
			    / \                        / \
			  T2   T3                    T1   T2
         */
        if (balance > 1 && getBalance(root.left) < 0) { 
            node.left = rotateLeft(node.left); 
            return rotateRight(node); 
        } 
  
        // Right Right Case 
        /*
              z                                y
			 /  \                            /   \ 
			T1   y     Left Rotate(z)       z      x
			    /  \   - - - - - - - ->    / \    / \
			   T2   x                     T1  T2 T3  T4
			       / \
			     T3  T4
         */
        if (balance < -1 && getBalance(root.right) <= 0) {
        	return rotateLeft(node); 
        }
   
        // Right Left Case 
        /*
               z                            z                            x
			  / \                          / \                          /  \ 
			T1   y   Right Rotate (y)    T1   x      Left Rotate(z)   z      y
			    / \  - - - - - - - - ->     /  \   - - - - - - - ->  / \    / \
			   x   T4                      T2   y                  T1  T2  T3  T4
			  / \                              /  \
			T2   T3                           T3   T4
         */
        if (balance < -1 && getBalance(root.right) > 0) { 
            node.right = rotateRight(node.right); 
            return rotateLeft(node); 
        } 
  
        /* return the (unchanged) node pointer */
        return node; 
	}
	
	/**
	 *       x                         y
	 *     /   \                     /   \
	 *    y     rX      ---->       lY     x
	 *   / \                             /  \
	 * lY   rY                         rY    rX
	 * @param x
	 * @return x
	 */
	TreeNode rotateRight(TreeNode x) {
	    TreeNode y = x.left;
	    TreeNode rY = y.right;
	    y.right = x;
	    x.left = rY;
	    updateHeight(x);
	    updateHeight(y);	    
	    return y;
	}
	
	
	/**
	 *       x                         y
	 *     /   \                     /   \
	 *    lX    y      ---->        x     rY
	 *         / \                 /  \
	 *        lY   rY             lX  lY
	 * @param x
	 * @return x
	 */
	TreeNode rotateLeft(TreeNode x) {
	    TreeNode y = x.right;
	    TreeNode lY = y.left;
	    y.left = x;
	    x.right = lY;
	    updateHeight(x);
	    updateHeight(y);
	    return y;
	}
	
	private void updateHeight(TreeNode n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }
	
	private int height(TreeNode n) {
        return n == null ? 0 : n.height;
    }
 
    private int getBalance(TreeNode n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

	public int size(TreeNode node) {
		if (node == null) {
			return 0;
		}
		return size(node.left) + size(node.right) + 1;
	}

	public void print(TreeNode node) {
		if (node == null) {
			return;
		}
		print(node.left);
		System.out.print(node.key + " ");
		print(node.right);
	}
	
	private TreeNode mostLeftChild(TreeNode node) {
        TreeNode current = node;
        /* loop down to find the leftmost leaf */
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

	static class TreeNode {
		int key;
		int height;
		TreeNode left;
		TreeNode right;

		TreeNode(int val) {
			this.key = val;
		}
	}
}
