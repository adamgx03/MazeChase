package com.abooth.mazechase;

/**
 * Created by aboot on 3/21/2018.
 */

public class MazeCell {

    public int x;
    public int y;
    public int id;
    public boolean visited;
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;

    //NEW CELLS ARE INSTANTIATED WITH ALL WALLS INTACT

    public MazeCell(int xPos, int yPos, int cellId) {
        x = xPos;
        y = yPos;
        visited = true;
        north = true;
        south = true;
        east = true;
        west = true;
    }
}