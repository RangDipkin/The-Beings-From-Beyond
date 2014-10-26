package grammars;

import java.util.ArrayList;

public class ShapeRule {
    String name;
    String label;
    ArrayList<Constant> constants;
    ArrayList<Constraint> constraints;
    
    ArrayList<ShapeSpec> output;
    
    public ShapeRule(String inName, String inLabel, ArrayList<Constant> inConstants, 
            ArrayList<Constraint> inConstraints, ArrayList<ShapeSpec> inOutput) {
        name = inName;
        label = inLabel;
        constants = inConstants;
        constraints = inConstraints;
        output = inOutput;
    }
}
