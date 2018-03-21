package com.abooth.mazechase;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Stack;

public class MazeCanvas extends View {

    //MAZE DIMENSIONS
    public final int COLS = 10;
    public final int ROWS = 9;
    final int N_CELLS = COLS * ROWS;
    final int SIZE = 100;
    final int OFFSET = 100;

    //ARRAY OF MAZE CELLS
    public MazeCell[] board;

    private Paint paint;

    public MazeCanvas(Context context) {

        super(context);

        //TASK 1: DECLARE A MAZE ARRAY OF SIZE N_CELLS TO HOLD THE CELLS
        board = new MazeCell[N_CELLS];

        //TASK 2:  INSTANTIATE CELL OBJECTS FOR EACH CELL IN THE MAZE
        int cellId = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                //STEP 1 GENERATE A MAZE CELL WITH THE X, Y AND CELL ID
                int x = c * SIZE + OFFSET;
                int y = r * SIZE + OFFSET;
                MazeCell cell = new MazeCell(x, y, cellId);

                //STEP 2: PLACE THE CELL IN THE MAZE
                board[cellId] = cell;
                cellId++;
            }
        }

        //TASK 3: SET THE PAINT FOR THE MAZE
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(2.0f);

        //TASK 4: USE A BACKTRACKER METHOD TO BREAK DOWN THE WALLS
        backtrackMaze();

    }

    public void  onDraw(Canvas canvas) {
        //TASK 1: FILL THE CANVAS WITH WHITE PAINT
        canvas.drawRGB(255, 255, 255);

        //TASK 2: DRAW THE LINES FOR EVERY CELL
        for (int i = 0; i < N_CELLS; i++) {
            int x = board[i].x;
            int y = board[i].y;

            if (board[i].north) canvas.drawLine(x, y, x+SIZE, y, paint);
            if (board[i].south) canvas.drawLine(x, y+SIZE, x+SIZE, y+SIZE, paint);
            if (board[i].east) canvas.drawLine(x+SIZE, y, x+SIZE, y+SIZE, paint );
            if (board[i].west) canvas.drawLine(x, y, x, y+SIZE, paint);
        }
    }

    public void backtrackMaze() {
        //TASK 1: CREATE THE BACKTRACKER VARIABLES AND INITIALIZE THEM
        Stack<Integer> stack = new Stack<Integer>();
        int top;

        //TASK 2: VISIT THE FIRST CELL AND PUSH IT ONTO THE STACK
        int visitedCells = 1;   //COUNTS HOW MANY CELLS HAVE BEEN VISITED
        int cellId = 0;         //THE FIRST CELL IN THE MAZE
        board[cellId].visited = true;
        stack.push(cellId);

        //TASK 3: BACKTRACK UNTIL THE CELLS HAVE BEEN VISITED
        while (visitedCells < N_CELLS) {
            //STEP 1: WHICH WALLS CAN BE TAKEN DOWN FOR A GIVEN CELL?
            String possibleWalls = " ";
            if (board[cellId].north == true && cellId >= COLS) {
                if (!board[cellId - COLS].visited) {
                    possibleWalls += "N";
                }
            }
            if (board[cellId].west == true && cellId % COLS != 0) {
                if (!board[cellId - 1].visited) {
                    possibleWalls += "W";
                }
            }
            if (board[cellId].east == true && cellId % COLS != COLS -1) {
                if (!board[cellId + 1].visited) {
                    possibleWalls += "E";
                }
            }
            if (board[cellId].south == true && cellId < COLS * ROWS - COLS) {
                if (!board[cellId + 1].visited) {
                    possibleWalls += "S";
                }
            }

            //STEP 2: RANDOMLY SELECT A RANDOM WALL FROM AVAILABLE WALLS
            if (possibleWalls.length() > 0) {
                int index = Math.round((int)(Math.random()*possibleWalls.length()));
                char randomWall = possibleWalls.charAt(index);

                switch (randomWall) {
                    case 'N':
                        board[cellId].north = false;
                        board[cellId - COLS].south = false;
                        cellId -= COLS;
                        break;
                    case 'S':
                        board[cellId].south = false;
                        board[cellId + COLS].north = false;
                        cellId += COLS;
                        break;
                    case 'E':
                        board[cellId].east = false;
                        board[cellId + 1].west = false;
                        cellId++;
                        break;
                    case 'W':
                        board[cellId].west = false;
                        board[cellId - 1].east = false;
                        cellId--;
                        break;
                }
                board[cellId].visited = true;
                stack.push(cellId);
                visitedCells++;
            }

            //IF THERE ARE NO WALLS TO BUST DOWN, BACKTRACK BY GRABBING THE TOP OF THE STACK
            else {
                top = stack.pop();
                if (top == cellId){
                    cellId = stack.pop();
                    stack.push(cellId);
                }
            }
        }
    }
}