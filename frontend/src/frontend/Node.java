package frontend;

import java.util.*;

public class Node {
    private String usrID;
    private ArrayList<Node> adjacents;

    public Node(String id) {
        usrID = id;
        adjacents = new ArrayList<>();
    }

    public void addAdjacent(Node n) {
        adjacents.add(n);
    }

    public String getID() {
        return usrID;
    }

    public List<Node> getAdjacents() {
        return adjacents;
    }

    public boolean has(Node n) {
        return adjacents.contains(n);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return this.usrID.equals(((Node) o).usrID);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return usrID.hashCode();
    }

    @Override
    public String toString() {
        return usrID;
    }
}
