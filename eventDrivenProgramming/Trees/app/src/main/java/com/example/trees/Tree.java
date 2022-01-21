package com.example.trees;

import java.util.ArrayList;

public class Tree {
    public  ArrayList<Node> array = new ArrayList<>();

    private Node root;

    private float minRotation, maxRotation, minLength, maxLenth, minTrunkWidth, maxTrunkWidth;

    public Tree(float minAngle, float maxAngle, float minLength, float maxLength, float minTrunkWidth, float maxTrunkWidth) {
        this.minRotation = minAngle;
        this.maxRotation = maxAngle;
        this.minLength = minLength;
        this.maxLenth = maxLength;
        this.minTrunkWidth = minTrunkWidth;
        this.maxTrunkWidth = maxTrunkWidth;
    }

    public void generateTree(int depth) {
        root = new Node(null);
        root.rotation = (float) 0.0;
        array.add(root);
        generateTree(root, depth - 1);
        System.out.println();
    }

    private void generateTree(Node node, int depth) {
        if (depth == 0) { return; }
        node.left = new Node(node);
        node.right = new Node(node);

        array.add(node.left);
        array.add(node.right);

        generateTree(node.left, depth - 1);
        generateTree(node.right, depth - 1);
    }

    public ArrayList<Branch> toBranches(int screenWidth, int screenHeight) {
        ArrayList<Branch> branches = new ArrayList<Branch>();
        float trunkWidth = (float) (Math.random() * (maxTrunkWidth - minTrunkWidth + 1) + minTrunkWidth);
        Branch rootBranch = new Branch(screenWidth / 2, screenHeight - 60, screenWidth / 2, screenHeight - 60 - root.length, trunkWidth);
        branches.add(rootBranch);
        toBranches(root.left, rootBranch, branches);
        toBranches(root.right, rootBranch, branches);
        return branches;
    }

    private void toBranches(Node currentNode, Branch lastBranch, ArrayList<Branch> branches) {
        if (currentNode == null) { return; }

        float randomWidth = (float) (Math.random() * (lastBranch.width * 0.5 - lastBranch.width * 0.4 + 1) + lastBranch.width * 0.4);
        if (randomWidth < 2) return;

        float newX = (float) (lastBranch.x2 + (Math.sin(Math.toRadians(currentNode.totalRotation)) * currentNode.length));
        float newY = (float) (lastBranch.y2 - (Math.cos(Math.toRadians(currentNode.totalRotation)) * currentNode.length));

        Branch newBranch = new Branch(lastBranch.x2, lastBranch.y2, newX, newY, randomWidth);
        branches.add(newBranch);

        toBranches(currentNode.left, newBranch, branches);
        toBranches(currentNode.right, newBranch, branches);
    }

    private class Node {
        private Node left;
        private Node right;
        private Node parent;

        private float length = (float) Math.random() * (maxLenth - minLength + 1) + minLength;
        private float rotation = (float) Math.random() * (maxRotation - minRotation + 1) + minRotation;
        private float totalRotation;

        public Node (Node parent) {
            this.parent = parent;
            this.totalRotation = parent != null ? rotation + parent.totalRotation : rotation;
        }
    }
}
