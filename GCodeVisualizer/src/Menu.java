import processing.core.PApplet;

public class Menu extends PApplet {
    String fileName = "";

    public void settings(){
        size(1500, 1200);
    }


    public void draw(){
        background(100);
        textSize(30);
        fill(255);
        text("Name of the file you want to import/export", width/2-315, height/2-200);
        fileNameUI();
        buttons();
    }

    private void buttons(){
        noFill();
        rect(width/2-275, height/2-75, 250, 40);
        rect(width/2+25, height/2-75, 250, 40);
        textSize(20);
        fill(255);
        text("Show Tool path", width/2-260, height/2-50);
        text("Draw Sketch", width/2+40, height/2-50);
    }

    private void fileNameUI(){
        textSize(20);
        fill(200);
        if (fileName == "") {
            text("file_name...", width / 2 - 265, height / 2 - 150);
        }
        text(fileName, width / 2 - 265, height / 2 - 150);
        noFill();
        rect(width/2-275, height/2-175, 550, 40);
    }

    public void keyPressed(){
        if (keyCode == 8 && fileName.length() > 0){
            fileName = fileName.substring(0, fileName.length()-1);
        }
        else if(keyCode >= 65 && keyCode <= 90 || keyCode >= 48 && keyCode <= 57){
            fileName += key;
        }
    }

    public void mousePressed(){
        if (mouseX > width/2-275 && mouseX < width/2-25 && mouseY > height/2-75 && mouseY < height/2-35){
            Visualizer visualizer = new Visualizer(fileName);
            PApplet.runSketch(new String[]{"Visualizer"}, visualizer);
        }
        else if(mouseX > width/2+25 && mouseX < width/2+275 && mouseY > height/2-75 && mouseY < height/2-35){
            Draw draw = new Draw(fileName);
            PApplet.runSketch(new String[]{"Draw"} , draw);
        }
    }

}
