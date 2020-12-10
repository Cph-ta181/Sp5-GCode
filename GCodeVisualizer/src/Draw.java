import processing.core.PApplet;

import java.util.ArrayList;

public class Draw extends PApplet {
    String fileName;

    public Draw(String fileName){
        this.fileName = fileName;
    }

    public void settings(){
        size(1500, 1200);
    }
    int currentTool = 1;
    int pressCount = 0;
    int press1X;
    int press1Y;
    int press2X;
    int press2Y;
    boolean drawing = true;
    ArrayList<Figure> figures = new ArrayList<>();

    public void draw(){
        background(150);
        noFill();
        toolWindow();
        drawALlFigures();
        if (pressCount == 1){
            drawCurrentFigure();
        }
        if (!drawing){
            noLoop();
        }
    }
    private void drawCurrentFigure(){
        strokeWeight(1);
        if (currentTool == 1){
            line(press1X, press1Y, mouseX, mouseY);
        }
        if (currentTool == 2){
            rect(press1X, press1Y,mouseX - press1X, mouseY - press1Y);
        }
        if (currentTool == 3){
            int radius = (int)sqrt(pow(press1X-mouseX,2) + pow(press1Y - mouseY,2));
            circle(press1X, press1Y, radius*2);
        }
        if (currentTool == 4){
            int rX = abs(mouseX - press1X)*2;
            int rY = abs(mouseY - press1Y)*2;
            ellipse(press1X, press1Y, rX, rY);
        }
    }

    private void drawALlFigures(){
        strokeWeight(1);
        for (Figure figure: figures){
            figure.draw();
        }
    }

    private void toolWindow(){
        strokeWeight(2);
        textSize(20);
        if (currentTool == 1){
            stroke(0, 255, 0);
        }
        text('1', width-80, 70);
        line(width-50, 50, width -50, 75);
        stroke(0, 0, 0);

        if (currentTool == 2){
            stroke(0, 255, 0);
        }
        text('2', width-80, 155);
        rect(width-60, 125, 20, 50);
        stroke(0);

        if (currentTool == 3){
            stroke(0, 255, 0);
        }
        text('3', width-80, 230);
        circle(width-50, 225, 25);
        stroke(0);

        if (currentTool == 4){
            stroke(0, 255, 0);
        }
        text('4', width-80, 305);
        ellipse(width-50, 300, 10,25);
        stroke(0);

        rect(width-105, 478, 95, 30);
        text("Generate", width-100, 500);
        text('9', width-125, 500);

        text("X Pos: " + mouseX + ", Y Pos: " + mouseY, width-250, height-25);
    }

    public void keyPressed(){
        if (pressCount == 0) {
            if (key == '1') {
                currentTool = 1;
            }
            if (key == '2') {
                currentTool = 2;
            }
            if (key == '3') {
                currentTool = 3;
            }
            if (key == '4') {
                currentTool = 4;
            }
            if (key == '9'){
                drawing = false;
                new GCodeGenerator(figures, fileName).generateCode();
                exit();
            }
        }
    }

    public void mousePressed(){
        if (currentTool == 1){
            if (pressCount == 0) {
                press1X = mouseX;
                press1Y = mouseY;
            }
            else {
                press2X = mouseX;
                press2Y = mouseY;
                figures.add(new Line(this, press1X, press1Y, press2X, press2Y));
            }
            pressCount = (pressCount+1)%2;
        }

        if (currentTool == 2){
            if (pressCount == 0) {
                press1X = mouseX;
                press1Y = mouseY;
            }
            else {
                press2X = mouseX - press1X;
                press2Y = mouseY - press1Y;
                figures.add(new Rect(this, press1X, press1Y, press2X, press2Y));
            }
            pressCount = (pressCount+1)%2;
        }

        if (currentTool == 3){
            if (pressCount == 0) {
                press1X = mouseX;
                press1Y = mouseY;
            }
            else {
                int radius = (int)sqrt(pow(press1X-mouseX,2) + pow(press1Y - mouseY,2));
                figures.add(new Circle(this, press1X, press1Y, radius*2));
            }
            pressCount = (pressCount+1)%2;
        }

        if (currentTool == 4){
            if (pressCount == 0) {
                press1X = mouseX;
                press1Y = mouseY;
            }
            else {
                int rX = abs(mouseX - press1X);
                int rY = abs(mouseY - press1Y);
                figures.add(new Ellipse(this, press1X, press1Y, rX*2, rY*2));
            }
            pressCount = (pressCount+1)%2;
        }
    }
}
