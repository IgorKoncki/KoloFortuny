package MainPackage;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Wheel {

    Node head;

    Wheel(Pane pane){
        Text helpTxt = new Text();
        String tab = "\t\t\t\t\t\t\t";
        helpTxt.setText(tab + "500 pts");
        helpTxt.xProperty().bind(pane.widthProperty().divide(2).subtract(280));
        helpTxt.yProperty().bind(pane.heightProperty().subtract(pane.heightProperty().multiply(0.1)).add(10));
        int rotate = 270;
        helpTxt.rotateProperty().setValue(rotate);
        rotate+=10;
        helpTxt.setFont(new Font("Serif",30));
        pane.getChildren().add(helpTxt);
        helpTxt.setFill(Color.GOLD);
        head = new Node(500,helpTxt,null);
        head.next = head;
        for(int i = 0; i<35;i++){
            Node helpNode;
            helpTxt = new Text();
            helpTxt.rotateProperty().setValue(i*10+rotate);
            /*if(i<20){
                helpTxt.setText(tab + " 10 pts");
                helpNode = new Node(10,helpTxt,head.next);
            }else {
                if (i < 30) {
                    helpTxt.setText(tab + " 50 pts");
                    helpNode = new Node(50,helpTxt,head.next);
                } else {
                    helpTxt.setText(tab + "100 pts");
                    helpNode = new Node(100,helpTxt,head.next);
                }
            }*/
            switch(i){
                case 0:case 1:case 3:case 8:case 13:case 18:case 23:case 28:case 33:case 34:
                    helpTxt.setText(tab + "100 pts");
                    helpTxt.setFill(Color.ORANGE);
                    helpNode = new Node(100,helpTxt,head.next);
                    break;
                default:
                    helpTxt.setFill(Color.DARKORANGE);
                    helpTxt.setText(tab + " 10 pts");
                    helpNode = new Node(10,helpTxt,head.next);
            }
            helpTxt.xProperty().bind(pane.widthProperty().divide(2).subtract(280));
            helpTxt.yProperty().bind(pane.heightProperty().subtract(pane.heightProperty().multiply(0.1)).add(10));
            helpTxt.setFont(new Font("Serif",30));
            pane.getChildren().add(helpTxt);
            head.next = helpNode;
        }
    }

    void spin() {
        double helpRotate = head.text.getRotate() - 10;
        helpRotate = helpRotate%360;
        head.text.setRotate(helpRotate);
        for (int i = 0; i < 35; i++) {
            head = head.next;
            helpRotate = head.text.getRotate() - 10;
            helpRotate = helpRotate%360;
            head.text.setRotate(helpRotate);
        }
    }

}
