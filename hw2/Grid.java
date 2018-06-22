
package hw2;
import java.util.ArrayList;

public class Grid {
    public int gridTileSize;
    ArrayList<Object> grid = new ArrayList<>();

    public Grid(int N) {
        gridTileSize = N * N;
        for (int i = 0; i < N; i++) {
            grid.add(new ArrayList<Integer>());
        }
        this.assignIndex(N);
    }

    public void assignIndex(int N){
        int index  = 0;
        for (Object row : this.grid) {
            if (index >= this.gridTileSize) {
                System.out.print(index + " is larger than gridSize " + this.gridTileSize);
            }
            for (int i = 0; i < N; i++){
                ((ArrayList)row).add(index);
                System.out.print(index);
                index ++;
            }
        }
    }

//    public void main(String[] args){
//        Grid newGrid = new Grid(10);
//    }
}
