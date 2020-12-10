import com.sun.javafx.geom.Vec2d;
import processing.core.PApplet;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class GCodeGenerator extends PApplet{
    private String fileName;
    private ArrayList<Figure> figures;
    private ArrayList<String> cmds = new ArrayList<>();

    public GCodeGenerator(ArrayList<Figure> figures, String fileName){
        this.figures = figures;
        this.fileName = fileName;
    }

    public void generateCode(){
        Vec2d lastCoords = new Vec2d(0, 0);
        for (Figure figure : figures){
            switch (figure.getClass().getTypeName()) {
                case "Line":
                    lastCoords = lineGen((Line)figure, lastCoords);
                    break;
                case "Rect":
                    lastCoords = rectGen((Rect)figure, lastCoords);
                    break;
                case "Circle":
                    lastCoords = circleGen((Circle)figure, lastCoords);
                    break;
                case "Ellipse":
                    lastCoords = ellipseGen((Ellipse)figure, lastCoords);
            }
        }
        writeCmdsToFile();
    }

    private Vec2d ellipseGen(Ellipse ellipse, Vec2d lastCoords){
        //Last location is = start location
        if (ellipse.getValues().get(0) + ellipse.getValues().get(2)/2 != lastCoords.x || -ellipse.getValues().get(1) + ellipse.getValues().get(3)/2 != lastCoords.y){
            cmds.add("G00 X" +
                    ellipse.getValues().get(0) +
                    " Y" +
                    (-ellipse.getValues().get(1) + ellipse.getValues().get(3)/2) + "\n");
        }
        float cx = ellipse.getValues().get(0); //Center X
        float cy = -ellipse.getValues().get(1); //Center Y
        float rx = ellipse.getValues().get(2)/2; //Radius X
        float ry = ellipse.getValues().get(3)/2; //Radius Y

        //Angle b to a
        float angle = atan(rx/ry);
        //d - the difference between the 2 radii from b to a
        float diff = abs(rx - ry);
        Vec2d d = new Vec2d(cx + diff * sin(angle), cy + ry - diff * cos(angle));

        //Distance between d and B
        float lengthDB = sqrt(pow(rx,2) + pow(ry,2))-diff;
        //Halfpoint between d and B
        Vec2d halfDB = new Vec2d(d.x + (lengthDB/2) * sin(angle), d.y - (lengthDB/2) * cos(angle));

        //First center point
        Vec2d centerP1 = new Vec2d(cx, rx/ry * (cx- halfDB.x) + halfDB.y);

        //First end point
        float lengthDiffEndP1 = ((abs((float) centerP1.y) + ry + cy) / sqrt( pow((float) (halfDB.x - centerP1.x), 2) + pow((float) (abs((float) centerP1.y) + halfDB.y),2)));
        Vec2d endP1 = new Vec2d(abs((float)((halfDB.x - cx)*lengthDiffEndP1 + cx)), (abs((float)centerP1.y) + halfDB.y)*lengthDiffEndP1 + centerP1.y);

        //First Arc
        cmds.add("G02 X" +
                endP1.x +
                " Y" +
                endP1.y +
                " J" +
                -(ry - centerP1.y + cy) + "\n");

        // Center point 2
        Vec2d centerP2 = new Vec2d(halfDB.x - ((halfDB.y - cy) / (rx / ry)), cy);

        // Second end point - first mirrored in x-axis
        Vec2d endP2 = new Vec2d(endP1.x, cy - (endP1.y-cy));

        cmds.add("G02 X" +
                endP2.x +
                " Y" +
                endP2.y +
                " I" +
                -(endP1.x - centerP2.x) +
                " J" +
                -(endP1.y - centerP2.y)+ "\n");

        //Center point 3 - center point 1 mirrored in x-axis
        Vec2d centerP3 = new Vec2d(cx, -(centerP1.y-cy) + cy);

        //End point 3
        Vec2d endP3 = new Vec2d(-(endP2.x - cx) +cx , endP2.y);

        cmds.add("G02 X" +
                endP3.x +
                " Y" +
                endP3.y +
                " I" +
                -(endP2.x-centerP3.x) +
                " J" +
                (centerP3.y -endP2.y) + "\n");

        //Center point 4 - centerP2 mirrored in y-axis
        Vec2d centerP4 = new Vec2d(-(centerP2.x - cx) +cx ,centerP2.y);

        //End point 4 - endP1 mirrored in y-axis
        Vec2d endP4 = new Vec2d(endP3.x, endP1.y);

        cmds.add("G02 X" +
                endP4.x +
                " Y" +
                endP4.y +
                " I" +
                -(endP3.x - centerP4.x) +
                " J" +
                -(endP3.y - centerP4.y) + "\n");

        //Center point 5 = center point 1
        //End point 5 = start point
        cmds.add("G02 X" +
                cx +
                " Y" +
                (cy + ry) +
                " I" +
                (-endP4.x + cx) +
                " J" +
                (centerP1.y - endP4.y) + "\n");

        return new Vec2d(ellipse.getValues().get(0), ellipse.getValues().get(1) + ellipse.getValues().get(3)/2);
    }

    private Vec2d circleGen(Circle circle, Vec2d lastCoords){
        //Last location is = start location
        if (circle.getValues().get(0) + circle.getValues().get(2)/2 != lastCoords.x || -circle.getValues().get(1) + circle.getValues().get(2)/2 != lastCoords.y){
            cmds.add("G00 X" +
                    circle.getValues().get(0) +
                    " Y" +
                    (-circle.getValues().get(1) + circle.getValues().get(2)/2) + "\n");
        }
        cmds.add("G02 X" +
                circle.getValues().get(0) +
                " Y" +
                (-circle.getValues().get(1) + circle.getValues().get(2)/2) +
                " J" +
                -circle.getValues().get(2)/2 + "\n");

        return new Vec2d(circle.getValues().get(0), circle.getValues().get(1) + circle.getValues().get(2)/2);
    }

    private Vec2d rectGen(Rect rect, Vec2d lastCoords){
        //Last location is = start location
        if (rect.getValues().get(0) != lastCoords.x || rect.getValues().get(1) != lastCoords.y){
            cmds.add("G00 X" +
                    rect.getValues().get(0) +
                    " Y" +
                    -rect.getValues().get(1) + "\n");
        }
        //Draw from top left to top right
        cmds.add("G01 X" +
                (rect.getValues().get(2) + rect.getValues().get(0)) +
                " Y" +
                (-rect.getValues().get(1)) + "\n");
        //Draw from top right to bottom right
        cmds.add("G01 X" +
                (rect.getValues().get(2) + rect.getValues().get(0)) +
                " Y" +
                (-(rect.getValues().get(3) + rect.getValues().get(1))) +"\n");
        //Draw from bottom right to bottom left
        cmds.add("G01 X" +
                rect.getValues().get(0) +
                " Y" +
                (-(rect.getValues().get(3) + rect.getValues().get(1))) +"\n");
        //Draw from bottom left to top right
        cmds.add("G01 X" +
                rect.getValues().get(0) +
                " Y" +
                (-rect.getValues().get(1)) + "\n");

        return new Vec2d(rect.getValues().get(2), rect.getValues().get(3));
    }

    private Vec2d lineGen(Line line, Vec2d lastCoords){
        //Last location is = start location
        if (line.getValues().get(0) != lastCoords.x || line.getValues().get(1) != lastCoords.y){
            cmds.add("G00 X" +
                    line.getValues().get(0) +
                    " Y" +
                    -line.getValues().get(1) + "\n");
        }
        //Draw line to end pos
        cmds.add("G01 X" +
                line.getValues().get(2) +
                " Y" +
                -line.getValues().get(3) + "\n");

        return new Vec2d(line.getValues().get(2), line.getValues().get(3));
    }

    private void writeCmdsToFile(){
        try {
            File file = new File("./resources/" + fileName+ ".nc");
            FileWriter fileWriter = new FileWriter(file);

            for (String line : cmds){
                fileWriter.write(line);
            }
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
