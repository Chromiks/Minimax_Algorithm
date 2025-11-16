package battle;


import java.util.ArrayList;
import java.util.List;

public class BattleSolver {


    public Node initialNode;
    public int nodesExpanded = 0;




    public  String  solve(String initialStateString, boolean ab, boolean visualize){
        initialNode = getInitialNode(initialStateString);
        int score = minimax(initialNode);
        String plan = writePlan(initialNode);
        return (plan + ";"+ score + ";" + nodesExpanded);
    }

    public  Node getInitialNode(String initialStateString) {
        String[] split = initialStateString.split(";");
        String[] statsA = split[0].split(",");
        String[] statsB = split[1].split(",");
        int armyASize = statsA.length / 2;
        int[][] armyA = new int[armyASize][2];
        int armyBSize = statsB.length / 2;
        int[][] armyB = new int[armyBSize][2];
        for (int i = 0; i < armyASize; i++) {
            armyA[i][0] = Integer.parseInt(statsA[2 * i]);
            armyA[i][1] = Integer.parseInt(statsA[2 * i + 1]);
        }
        for (int i = 0; i < armyBSize; i++) {
            armyB[i][0] = Integer.parseInt(statsB[2 * i]);
            armyB[i][1] = Integer.parseInt(statsB[2 * i + 1]);
        }
        char startingPlayer = split[2].charAt(0);
        return  new Node(armyA, armyB, null, null, startingPlayer);
    }

    public  int computeUtility(Node node, char startingPlayer) {
        int sumA = 0;
        int sumB = 0;
        for (int[] healths : node.armyA) sumA += healths[0];
        for (int[] healths : node.armyB) sumB += healths[0];
        if (startingPlayer == 'A') {
            return sumB == 0 ? sumA : -sumB;
        } else {
            return sumA == 0 ? sumB : -sumA;
        }
    }
    public  List<Node> getChildren(Node parent) {
        List<Node> children = new ArrayList<>();
        if(parent.getPlayerTurn() == 'A'){
            for(int i = 0; i < parent.armyA.length; i++){
                if (parent.armyA[i][0] <= 0) continue;
                for(int j = 0; j < parent.armyB.length; j++){
                    if (parent.armyB[j][0] <= 0) continue;
                    int[][] newArmyA = new int[parent.armyA.length][2];
                    int[][] newArmyB = new int[parent.armyB.length][2];
                    for (int k = 0; k < parent.armyA.length; k++) newArmyA[k] = parent.armyA[k].clone();
                    for (int k = 0; k < parent.armyB.length; k++) newArmyB[k] = parent.armyB[k].clone();
                    newArmyB[j][0] = Math.max(0, newArmyB[j][0] - parent.armyA[i][1]);
                    String action = "A(" + i + "," + j + ")";
                    Node child = new Node(newArmyA, newArmyB, parent, action, 'B');
                    children.add(child);
                }
            }
        } else {
            for(int i = 0; i < parent.armyB.length; i++){
                if (parent.armyB[i][0] <= 0) continue;
                for(int j = 0; j < parent.armyA.length; j++){
                    if (parent.armyA[j][0] <= 0) continue;
                    int[][] newArmyA = new int[parent.armyA.length][2];
                    int[][] newArmyB = new int[parent.armyB.length][2];
                    for (int k = 0; k < parent.armyA.length; k++) newArmyA[k] = parent.armyA[k].clone();
                    for (int k = 0; k < parent.armyB.length; k++) newArmyB[k] = parent.armyB[k].clone();
                    newArmyA[j][0] = Math.max(0, newArmyA[j][0] - parent.armyB[i][1]);
                    String action = "B(" + i + "," + j + ")";
                    Node child = new Node(newArmyA, newArmyB, parent, action, 'A');
                    children.add(child);
                }
            }
        }
        return children;
    }

    public  int  minimax(Node node){
        if (node.isTerminal()){
            node.setValue(computeUtility(node, initialNode.getPlayerTurn()));
            return node.value;
        }
        nodesExpanded += 1;
        Node bestChild = null;
        if (node.getPlayerTurn()== initialNode.getPlayerTurn()){

            int maxValue = Integer.MIN_VALUE;
            for (Node child : getChildren(node)) {
                int childValue = minimax(child);
                if (childValue > maxValue) {
                    maxValue = childValue;
                    bestChild = child;
                }
            }
            node.setBestChild(bestChild);
            node.setValue(maxValue);
            return maxValue;
        } else{
            int minValue = Integer.MAX_VALUE;
            for (Node child : getChildren(node)) {
                int childValue = minimax(child);
                if (childValue < minValue) {
                    minValue = childValue;
                    bestChild = child;
                }
            }
            node.setBestChild(bestChild);
            node.setValue(minValue);
            return minValue;
        }
    }

    public  String writePlan(Node root){
        StringBuilder plan = new StringBuilder();
        Node currentNode = root;
        while (!(currentNode.isTerminal())){
            plan.append(currentNode.getBestChild().getAction()).append(",");
            currentNode = currentNode.getBestChild();
        }
        if (plan.length() > 0) plan.setLength(plan.length() - 1);
        return plan.toString();
    }

    public static void main(String[] args) {
        BattleSolver solver = new BattleSolver();

        String initialState = "3,1,4,2;3,5;A;";
        String result = solver.solve(initialState, false, false);

        System.out.println("Solve result: " + result);
    }
}