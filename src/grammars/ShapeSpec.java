package grammars;

import java.util.ArrayList;

public class ShapeSpec {
    ArrayList<String> labels;
    ArrayList<Vertex> vertices;
    
    public ShapeSpec(ArrayList<String> inLabels, ArrayList<Vertex> inVertices) {
        labels = inLabels;
        vertices = inVertices;
    }
}
