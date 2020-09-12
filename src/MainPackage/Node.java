package MainPackage;

import javafx.scene.text.Text;

public class Node {
    public int points;
    public Text text;
    public Node next;

    Node(int points,Text text,Node next){
        this.points = points;
        this.text = text;
        this.next = next;
    }
}
