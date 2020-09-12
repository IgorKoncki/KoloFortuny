package MainPackage;

import javafx.scene.control.Label;

public class HiddenLetterLabel
    extends Label {
    char text;
    boolean isShowing;

    public HiddenLetterLabel(char text){
        super();
        this.text = text;
        isShowing=false;
    }

    public HiddenLetterLabel(){
        super();
    }

    void showLetter(){
        isShowing=true;
        this.setText("" + text);
    }

    char getLetter(){
        return text;
    }

    boolean isLetterShown(){
        return isShowing;
    }


}
