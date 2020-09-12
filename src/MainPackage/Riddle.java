package MainPackage;

import java.io.Serializable;

public class Riddle
    implements Serializable {
    private boolean inUse;
    private String text;

    Riddle(String text, boolean inUse){
        this.text = text;
        this.inUse = inUse;
    }
    Riddle(String text){
        this.text = text;
        this.inUse = false;
    }
    void setInUse(boolean inUse){
        this.inUse = inUse;
    }

    boolean getInUse(){
        return inUse;
    }

    String getText(){
        return text;
    }



}
