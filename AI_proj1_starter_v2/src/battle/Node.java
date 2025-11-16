package battle;


public class Node {


    public int value;
    public int[][] armyA;
    public int[][] armyB;
    public Node parent;
    public Node bestChild;
    public String action;
    public char playerTurn;


    public Node(int[][] armyA, int[][] armyB, Node parent, String action, char playerTurn) {
        this.armyA = armyA;
        this.armyB = armyB;
        this.parent = parent;
        this.action = action;
        this.playerTurn = playerTurn;
    }

    public int[][] getArmyA() {
        return armyA;
    }

    public int[][] getArmyB() {
        return armyB;
    }
    public Node getParent() {
        return parent;
    }

    public String getAction() {
        return action;
    }

    public char getPlayerTurn() {
        return playerTurn;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getBestChild() {
        return bestChild;
    }

    public void setBestChild(Node bestChild) {
        this.bestChild = bestChild;
    }

    public boolean isTerminal() {
        int healthSum = 0;
        for (int[] healths : armyA) {
            healthSum += healths[0];
        }

        if (healthSum == 0) {
            return true;
        }
        healthSum = 0;
        for (int[] healths : armyB) {
            healthSum += healths[0];
        }
        if (healthSum == 0) {
            return true;
        }
        return false;
    }
}