
package hw2;
import java.util.ArrayList;

public class Grid {
    /* Grid Data Structure
        grid = {{0, 1, 2, 3, 4, 5,..N},{N+1, N+2.....}, {...}, {...}}
     */
    public int totalSites;
    ArrayList<Object> sites = new ArrayList<>();

    public Grid(int N) {
        totalSites = N * N;

        for (int i = 0; i < N; i++) {
            sites.add(new ArrayList<Integer>());
        }
        this.assignIndex(N);
    }

    public void assignIndex(int N) {
        int index = 0;
        for (Object row : this.sites) {
            if (index >= this.totalSites) {
                System.out.print(index + " is larger than gridSize " + this.totalSites);
            }
            for (int i = 0; i < N; i++) {
                ((ArrayList) row).add(index);
                index++;
            }
        }
    }
}
