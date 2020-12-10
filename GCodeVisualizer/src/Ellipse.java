import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;

public class Ellipse extends PApplet implements Figure{
    PApplet pApplet;
    public int x;
    public int y;
    public int rx;
    public int ry;

    public Ellipse(PApplet pApplet, int x, int y, int rx, int ry) {
        this.x = x;
        this.y = y;
        this.rx = rx;
        this.ry = ry;
        this.pApplet = pApplet;
    }

    public ArrayList<Integer> getValues(){
        return new ArrayList<Integer>(Arrays.asList(x, y, rx, ry));
    }

    public void draw(){
        pApplet.ellipse(x, y, rx, ry);
    }
}
