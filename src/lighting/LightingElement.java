package lighting;

public class LightingElement {
    int color;
    int intensity;
    
    public LightingElement(int color, int intensity){
        this.color = color;
        this.intensity = intensity;
    }
    
    public int getColor(){
        return color;
    }
    
    public int getIntensity() {
        return intensity;
    }
}
