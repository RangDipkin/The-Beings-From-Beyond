package AI;

import objects.Tile;

public enum Compass implements MovementDesire{
	NORTH,EAST,SOUTH,WEST,NORTHEAST,SOUTHEAST,SOUTHWEST,NORTHWEST;
	
    @Override
	public int[] getCoords(Tile origin) {
		int[] coords = new int[2];
		// int left = origin.getX() - 1;
		// int right = origin.getX() + 1;
		// int up = origin.getY() - 1;
		// int down = origin.getY() + 1;
		
		if (this == NORTH) {
                    coords[0] = origin.getX();
                    coords[1] = origin.getY()-1;
                }
		else if (this == NORTHWEST) {
                    //coords = {origin.getX()-1, origin.getY()-1};
                    coords[0] = origin.getX()-1;
                    coords[1] = origin.getY()-1;
                }
		else if (this == NORTHEAST) {
                    //coords = {origin.getX()+1, origin.getY()-1};
                    coords[0] = origin.getX()+1;
                    coords[1] = origin.getY()-1;
                }
		else if (this == WEST) {
                    //coords = {origin.getX()-1, origin.getY()};}
                    coords[0] = origin.getX()-1;
                    coords[1] = origin.getY();
                }
		else if (this == EAST) {
                    //coords = {origin.getX()+1, origin.getY()};
                    coords[0] = origin.getX()+1;
                    coords[1] = origin.getY();
                }
		else if (this == SOUTH) {
                    //coords = {origin.getX(),   origin.getY()+1};
                    coords[0] = origin.getX();
                    coords[1] = origin.getY()+1;
                }
		else if (this == SOUTHWEST) {
                    //coords = {origin.getX()-1, origin.getY()+1};}
                    coords[0] = origin.getX()-1;
                    coords[1] = origin.getY()+1;
                }
		else if (this == SOUTHEAST) {
                    //coords = {origin.getX()+1, origin.getY()+1};
                    coords[0] = origin.getX()+1;
                    coords[1] = origin.getY()+1;
                }
                else {
                    coords[0] = origin.getX();
                    coords[1] = origin.getY();
                }
		
		return coords;
	}
}