package AI;

import objects.Tile;

public enum Compass implements MovementDesire{
	NORTH,EAST,SOUTH,WEST,NORTHEAST,SOUTHEAST,SOUTHWEST,NORTHWEST;
	
        int[][] CompassMatrix = {
            //x
            {0 , 1, 0, -1, 1, 1, -1, -1},
            //y
            {-1, 0, 1,  0,-1, 1,  1, -1}
        };
    
        @Override
	public int[] getCoords(Tile origin) {
		int[] coords = new int[2];
                
                coords[0] = origin.getX() + CompassMatrix[0][this.ordinal()];
                coords[1] = origin.getY() + CompassMatrix[1][this.ordinal()];
		
		return coords;
	}
}