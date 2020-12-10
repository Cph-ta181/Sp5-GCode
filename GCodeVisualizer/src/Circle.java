import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Arrays;

public class Circle extends PApplet implements Figure{
    PApplet pApplet;
    public int x;
    public int y;
    public int dia;

    public Circle(PApplet pApplet, int x, int y, int dia) {
        this.x = x;
        this.y = y;
        this.dia = dia;
        this.pApplet = pApplet;
    }

    public ArrayList<Integer> getValues(){
        return new ArrayList<Integer>(Arrays.asList(x, y, dia));
    }

    public void draw(){
        pApplet.circle(x, y, dia);
    }
}
