import processing.core.PApplet;

import java.io.*;
import java.util.Scanner;

public class Visualizer extends PApplet {
    private String fileName;

    public Visualizer(String fileName){
        this.fileName = fileName;
    }

    public void settings(){
        size(1500, 1500);
    }

    public void draw(){
        noFill();
        File file = new File("./resources/" + fileName + ".nc");
        Scanner sc = null;
        float offsetX = 0;
        float offsetY = 0;
        float currentX = offsetX;
        float currentY = offsetY;
        int sketchScale = 1;
        float x = 0;
        float y = 0;
        float i = 0;
        float j = 0;
        float rad = 0;
        float centerX;
        float centerY;


        float angle1;
        float angle2;

        try {
            BufferedReader br = new BufferedReader( new FileReader(file));
            String line;

            while((line = br.readLine()) != null) {
                String[] args = line.split(" ");
                switch (args[0]) {
                    case "G00": case "G0":
                        for (int index = 1; index < args.length; index++) {
                            switch (args[index].charAt(0)) {
                                case 'X':
                                    x = parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'Y':
                                    y = -parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                default:
                                    break;
                            }
                        }
                        stroke(0, 0, 255);
                        strokeWeight(1);
                        line(currentX * 1, currentY * 1, (offsetX + x) * 1, (offsetX + y) * 1);
                        currentX = (offsetX + x);
                        currentY = (offsetY + y);
                        break;

                    case "G01": case "G1":
                        for (int index = 1; index < args.length; index++) {
                            switch (args[index].charAt(0)) {
                                case 'X':
                                    x = parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'Y':
                                    y = -parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                default:
                                    break;
                            }
                        }
                        stroke(0, 255, 0);
                        line(currentX * 1, currentY * 1, (offsetX + x) * 1, (offsetY + y) * 1);
                        currentX = (offsetX + x);
                        currentY = (offsetY + y);
                        break;

                    case "G02": case "G2":
                        for (int index = 1; index < args.length; index++) {
                            switch (args[index].charAt(0)) {
                                case 'X':
                                    x = parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'Y':
                                    y = -parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'I':
                                    i = parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'J':
                                    j = -parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                default:
                                    break;
                            }
                        }
                        centerX = currentX+i;
                        centerY = currentY+j;


                        angle1 = 0;
                        angle2 = 0;


                        // Angle 2 - End Angle
                        if (centerY - (y+offsetY) < 0){ //Negative Y-Axis
                            if (centerX - (offsetX+x) < 0){ // Negative Y, Positive X
                                angle2 = atan(abs(centerY - (offsetY+y)) / abs(centerX - (offsetX+x)));
                            }
                            else{ //Negative Y, Negative X
                                angle2 = atan(abs(centerX - (offsetX+x)) / abs(centerY - (offsetY+y)))+HALF_PI;
                            }
                        }
                        else{ //Positive Y
                            if (centerX - (offsetX+x) < 0){ //Positive Y, Positive X
                                angle2 = atan(abs(centerX - (offsetX+x)) / abs(centerY - (offsetY+y)))+PI+HALF_PI;
                            }
                            else{ //Positive Y, Negative X
                                angle2 = atan(abs(centerY - (offsetY+y)) / abs(centerX - (offsetX+x)))+PI;
                            }
                        }


                        // Angle 1 - Start Angle
                        if (centerY - currentY < 0){ //Negative Y
                            if (centerX - currentX < 0){ //Negative y, Positive X
                                angle1 = atan(abs(currentY - centerY) / abs(currentX - centerX));
                            }
                            else{ //Negative y, Negative X
                                angle1 = atan(abs(currentX - centerX) / abs(currentY - centerY)) + HALF_PI;
                            }
                        }
                        else{ //Positive Y
                            if (centerX - currentX < 0){ //Positive Y, Positive X
                                angle1 = atan(abs(currentX - centerX) / abs(currentY - centerY)) + PI + HALF_PI;
                                if (angle1 > angle2){
                                    angle1 -= TAU;
                                }
                            }
                            else{ //Positive Y, Negative X
                                angle1 = atan(abs(currentY - centerY) / abs(currentX - centerX)) + PI;
                            }
                        }


                        rad = sqrt(pow(abs(currentX-centerX), 2) + pow(abs(currentY-centerY), 2));
                        if (angle1 == angle2){
                            stroke(0, 255, 0);
                            arc(centerX, centerY, rad*2, rad*2, 0, 2*PI);
                        }

                        noFill();
                        stroke(0, 255, 0);
                        arc(centerX, centerY, rad*2, rad*2, angle1, angle2);
                        currentX = (offsetX + x);
                        currentY = (offsetY + y);
                        break;

                    case "G03": case "G3":
                        for (int index = 1; index < args.length; index++) {
                            switch (args[index].charAt(0)) {
                                case 'X':
                                    x = parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'Y':
                                    y = -parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'I':
                                    i = parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                case 'J':
                                    j = -parseFloat(args[index].substring(1))*sketchScale;
                                    break;
                                default:
                                    break;
                            }
                        }
                        centerX = currentX+i;
                        centerY = currentY+j;


                        // Angle 2 - End Angle
                        if (centerY - currentY < 0){ //Negative Y
                            if (centerX - currentX < 0){ //Negative y, Positive X
                                angle2 = atan(abs(currentY - centerY) / abs(currentX - centerX));
                            }
                            else{ //Negative y, Negative X
                                angle2 = atan(abs(currentX - centerX) / abs(currentY - centerY)) + HALF_PI;
                            }
                        }
                        else{ //Positive Y
                            if (centerX - currentX < 0){ //Positive Y, Positive X
                                angle2 = atan(abs(currentX - centerX) / abs(currentY - centerY)) + PI + HALF_PI;
                            }
                            else{ //Positive Y, Negative X
                                angle2 = atan(abs(currentY - centerY) / abs(currentX - centerX)) + PI;
                            }
                        }


                        // Angle 1 - Start Angle
                        if (centerY - (y+offsetY) < 0){ //Negative Y-Axis
                            if (centerX - (offsetX+x) < 0){ // Negative Y, Positive X
                                angle1 = atan(abs(centerY - (offsetY+y)) / abs(centerX - (offsetX+x)));
                            }
                            else{ //Negative Y, Negative X
                                angle1 = atan(abs(centerX - (offsetX+x)) / abs(centerY - (offsetY+y)))+HALF_PI;
                            }
                        }
                        else{ //Positive Y
                            if (centerX - (offsetX+x) < 0){ //Positive Y, Positive X
                                angle1 = atan(abs(centerX - (offsetX+x)) / abs(centerY - (offsetY+y)))+PI+HALF_PI;
                                if (angle2 > angle1){
                                    //angle2 -= TAU;
                                }
                            }
                            else{ //Positive Y, Negative X
                                angle1 = atan(abs(centerY - (offsetY+y)) / abs(centerX - (offsetX+x)))+PI;
                            }
                        }

                        rad = sqrt(pow(abs(currentX-centerX), 2) + pow(abs(currentY-centerY), 2));
                        if (angle1 == 0 && angle2 == 0){
                            stroke(0, 255, 0);
                            arc(centerX, centerY, rad*2, rad*2, 0, 2*PI);
                        }

                        noFill();
                        stroke(0, 255, 0);
                        arc(centerX, centerY, rad*2, rad*2, angle1, angle2);
                        currentX = (offsetX + x);
                        currentY = (offsetY + y);
                        break;

                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mousePressed(){
        background(64);
    }


}
