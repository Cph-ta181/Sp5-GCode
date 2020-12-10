import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;

public class Line extends PApplet implements Figure{
    PApplet pApplet;
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Line(PApplet pApplet, int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.pApplet = pApplet;
    }

    public ArrayList<Integer> getValues(){
        return new ArrayList<Integer>(Arrays.asList(x1, y1, x2, y2));
    }

    public void draw(){
        pApplet.line(x1, y1, x2, y2);
    }
}
