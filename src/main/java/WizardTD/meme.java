package WizardTD;

import java.util.ArrayList;
import java.util.List;

public class meme {
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};


    public static List<List<int[]>> findAllPaths(char[][] map) {
        List<List<int[]>> allPaths = new ArrayList<>();
        int numRows = map.length;
        int numCols = map[0].length;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (map[row][col] == 'X' && isEdgeTile(row, col, numRows, numCols)) {
                    List<int[]> path = new ArrayList<>();
                    boolean[][] visited = new boolean[numRows][numCols];
                    findPathsDFS(map, row, col, visited, path, allPaths);
                }
            }
        }

        return allPaths;
    }

    private static boolean isEdgeTile(int row, int col, int numRows, int numCols) {
        return row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1;
    }

    private static void findPathsDFS(char[][] map, int row, int col, boolean[][] visited, List<int[]> path, List<List<int[]>> allPaths) {
        int numRows = map.length;
        int numCols = map[0].length;
        visited[row][col] = true;
        path.add(new int[]{row, col});
      
        if (map[row][col] == 'W') {
            allPaths.add(new ArrayList<>(path));
        } else {

            for (int i = 0; i < 4; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols &&
                        map[newRow][newCol] == 'X' && !visited[newRow][newCol]) {
                    findPathsDFS(map, newRow, newCol, visited, path, allPaths);
                } else if(newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols &&
                        map[newRow][newCol] == 'W' && !visited[newRow][newCol]){
                            findPathsDFS(map, newRow, newCol, visited, path, allPaths);
                }
                
            }
        }

        path.remove(path.size() - 1);
        visited[row][col] = false;
    }

    public meme(char[][] map) {
        // char[][] map = {
        //     {'B', 'B', 'B', 'X', 'B'},
        //     {'X', 'X', 'X', 'X', 'B'},
        //     {'B', 'B', 'X', 'X', 'B'},
        //     {'B', 'B', 'W', 'X', 'B'},
        //     {'B', 'B', 'B', 'B', 'B'}
        // };

        List<List<int[]>> allPaths = findAllPaths(map);

        if (allPaths.isEmpty()) {
            System.out.println("No paths found.");
        } else {
            for (List<int[]> path : allPaths) {
                System.out.println("Path:");
                for (int[] position : path) {
                    System.out.println("(" + position[0] + ", " + position[1] + ")");
                }
                System.out.println();
            }
        }


    
}

}