

// you can also use imports, for example:
import java.util.*;

/**
 * Nearest Passenger
You are given an n*m 2-D grid, a Grab driver location on the grid and a few passengers locations. Please help the Grab driver to find the nearest passenger to pick up.
Note that:
- each cell in the grid is a string. It’s either X or the string representation of a positive integer.
- cell (i, j) is not reachable if grid[i][j] is 'X'
- cell (i, j) is reachable if grid[i][j] is an integer
- The driver can only move up, down, left or right
- The distance of moving from neighboring cells to (i, j) is defined by the value of grid[i][j], if it’s reachable
- If no passenger is reachable from the driver’s location, output -1
- If multiple passengers are reachable, only output the distance to the nearest passenger
// Examples
grid = [
    ['1', '6', '3', '2', '6'],
    ['1', 'X', '2', '3', '3'],
    ['1', '5', '8', '1', '2'],
    ['1', 'X', '5', 'X', 'X'],
    ['1', 'X', '5', 'X', '1'],
]
// # Input: nearest_passenger(grid: grid, driver: (4, 0), passengers: [(4, 4)])
//   Output: -1
// # Input: nearest_passenger(grid: grid, driver: (3, 0), passengers: [(0, 3)])
//   Output: 14
// # Input: nearest_passenger(grid: grid, driver: (0, 0), passengers: [(4, 0), (0, 1)])
//   Output: 4
// # Input: nearest_passenger(grid: grid, driver: (2, 1), passengers: [(0, 2), (3, 2)])
//   Output: 12
// # Input: nearest_passenger(grid: grid, driver: (2, 3), passengers: [(0, 4), (2, 0), (0, 2)])
//   Output: 8
 * @author ashishkumarlahoti
 *
 */
class GrabTest3 {

    public static void main(String[] args){
        char[][] grid = new char[][]{
        {'1', '6', '3', '2', '6'}, 
        {'1', 'X', '2', '3', '3'},
        {'1', '5', '8', '1', '2'},
        {'1', 'X', '5', 'X', 'X'},
        {'1', 'X', '5', 'X', '1'}};
        System.out.println(nearest_passenger(grid, 3, 0, 0, 3));
    }

    public static int nearest_passenger(char[][] grid,  int driverX, int driverY, int passengerX, int passengerY) {
        return nearest_passenger(grid, driverX+1, driverY, passengerX, passengerY, 0);
    }


    public static int nearest_passenger(char[][] grid,  int driverX, int driverY, int passengerX, int passengerY, int total) {
        if(driverX >= grid[0].length || driverY >= grid.length || driverX < 0 || driverY < 0 || grid[driverX][driverY] == 'X'){
        	System.out.println("terminate");
        	return -1;
        }
        //System.out.println(Arrays.deepToString(grid));
        System.out.println(driverX + " " + driverY + " " + grid[driverX][driverY] + " " + total);
        char value = grid[driverX][driverY];
        int distance = Integer.parseInt(grid[driverX][driverY] + "");
        if(driverX == passengerX && driverY == passengerY){
        	System.out.println("here " + total + " " + distance);
        	System.out.println((total+distance));
            return total+distance;
        }    
        grid[driverX][driverY] = 'X';
        nearest_passenger(grid, driverX+1, driverY, passengerX, passengerY, total+distance);
        nearest_passenger(grid, driverX-1, driverY, passengerX, passengerY, total+distance);
        nearest_passenger(grid, driverX, driverY+1, passengerX, passengerY, total+distance);
        nearest_passenger(grid, driverX, driverY-1, passengerX, passengerY, total+distance);
        grid[driverX][driverY] = value;
        return total;
    }
}
