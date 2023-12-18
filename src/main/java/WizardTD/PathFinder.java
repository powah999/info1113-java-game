package WizardTD;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    public List<List<List<int[]>>> shortestpaths;

    public PathFinder(char[][] map){
        this.shortestpaths = findAllPaths(map);
    }

    public static List<List<List<int[]>>> findAllPaths(char[][] map) {
        List<List<int[]>> allPaths = new ArrayList<>();
        int numRows = map.length;
        int numCols = map[0].length;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (map[row][col] == 'X' && isEdgeTile(row, col, numRows, numCols)) {
                    // System.out.println(row);
                    // System.out.println(col);
                    List<int[]> path = new ArrayList<>();
                    boolean[][] visited = new boolean[numRows][numCols];
                    findPathsDFS(map, row, col, visited, path, allPaths);
                }
            }
        }
        
        List<List<List<int[]>>> shortestpathsTemp = new ArrayList<>();

        List<List<int[]>> listpaths = new ArrayList<>();;   
        listpaths.add(allPaths.get(0));

        int[] currentcoords = listpaths.get(0).get(0);
        int shortestlength = listpaths.get(0).size();

        // System.out.println(shortestlength);
        // System.out.println(currentcoords[0] + "," + currentcoords[1]);
        int count = 0;
        
        for(List<int[]> path : allPaths){
        // System.out.println(count);

            if(count == 0){
                count++;
                continue;
            }

            if(path.get(0)[0] != currentcoords[0] || path.get(0)[1] != currentcoords[1] ){
                if(path.get(0)[0] == 3){
                System.out.println("HELLLO\nHELLO\nHELLO");
                System.out.println(count);
                System.out.println(allPaths.size());
                }
                shortestpathsTemp.add(listpaths);

                listpaths = new ArrayList<>();
                listpaths.add(path);
                currentcoords = path.get(0);
                shortestlength = path.size();
            } else{


                if(path.size() < shortestlength){
                    shortestlength = path.size();
                    listpaths.clear();
                    listpaths.add(path);
                } else if(path.size() == shortestlength){
                    listpaths.add(path);
                }
                count++;

            }
            // for(List<int[]> bb: listpaths){
            //     System.out.println("Path:");
            //     for(int[] aa : bb){
            //         System.out.print(aa[0] + ","+ aa[1] + " " + "\n");
            //     }
            // }    
        }
        List<List<int[]>> newpaths = listpaths;
        shortestpathsTemp.add(newpaths);


            
            
            

        return shortestpathsTemp;
       
       
    }

    private static boolean isEdgeTile(int row, int col, int numRows, int numCols) {
        return row == 0 || row == numRows - 1 || col == 0 || col == numCols - 1;
    }

    private static void findPathsDFS(char[][] map, int row, int col, boolean[][] visited, List<int[]> path, List<List<int[]>> allPaths) {
        int numRows = map.length;
        int numCols = map[0].length;
        visited[row][col] = true;
        path.add(new int[]{row, col});
        // System.out.print("new coords: ");
        //         System.out.print(row);
        //         System.out.print(",");
        //         System.out.print(col);
        //         System.out.println("\n");


        if (map[row][col] == 'W') {
            // System.out.println("hello");
            allPaths.add(new ArrayList<>(path));
        } else {

            // System.out.print("Split: ");

            for (int i = 0; i < 4; i++) {
                int newRow = row + dx[i];
                int newCol = col + dy[i];

                if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols &&
                        map[newRow][newCol] == 'X' && !visited[newRow][newCol]) {
                    // System.out.print(newRow + "," + newCol + " ");
                }
            }
            // System.out.println("\n");




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

    public static void main(String[] args) {
        // char[][] map = {
        //     {'B', 'B', 'B', 'X', 'B'},
        //     {'X', 'X', 'X', 'X', 'B'},
        //     {'B', 'B', 'X', 'X', 'B'},
        //     {'B', 'B', 'W', 'X', 'B'},
        //     {'B', 'B', 'B', 'B', 'B'}
        // };

        // List<List<List<int[]>>> allPaths = findAllPaths(map);

        // if (allPaths.isEmpty()) {
        //     System.out.println("No paths found.");
        // } else {
        //     for (List<int[]> path : allPaths) {
        //         System.out.println("Path:");
        //         for (int[] position : path) {
        //             System.out.println("(" + position[0] + ", " + position[1] + ")");
        //         }
        //         System.out.println();
        //     }
        // }


        
    }

    public void printpaths(){
        System.out.println("[");
        for (List<List<int[]>> subpath : this.shortestpaths) {
            int[] temp = subpath.get(0).get(0);
            System.out.println("\n" + temp[0] + "," +temp[1] + "\n");
            System.out.println("  [");
            for (List<int[]> path : subpath) {
                System.out.println("    [");
                System.out.print("      ");
                for (int[] position : path){
                    System.out.print("(" + position[0] + ", " + position[1] + ") ");
                }
                System.out.print("\n");
                System.out.println("    ]");
            }
            System.out.println("  ]");
        }
        System.out.println("]");
    }
}
