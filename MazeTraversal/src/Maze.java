public class Maze {
    public String[][] maze;
    private int[] start;
    private int[] end;
    private int[] visited = new int[12];
    private int[][] route = new int[12][12];

    public Maze(String[][] m, int[]s, int[] e){
        maze = m;
        start = s;
        end = e;

        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                System.out.print(maze[i][j] + "  ");
            }
            System.out.println();
        }

        solveMaze(maze, start[0], start[1]);
        System.out.println("\n\n You Win!");
    }


    public void solveMaze(String[][] m, int row, int col){
        //show each step moved
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //start at entrance of maze
        m[col][row] = "X";
        //System.out.println("Current coordinates: (" + row + "," + col + ")");

        //check if row/col is end of maze
        if(row+1 == end[0] && col == end[1]){
            m[col][row] = "X";
            for(int i=0; i<12; i++){
                for(int j=0; j<12; j++){
                    System.out.print(maze[i][j] + "  ");
                }
                System.out.println();
            }
            m[col][row+1] = "X";
            for(int i=0; i<12; i++){
                for(int j=0; j<12; j++){
                    System.out.print(maze[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.println("\n");

            visited[0] = col;
            visited[1] = row;

            return;
        }
        else{
            //if you can go multiple ways, save location
            if((m[col][row+1].equals(".") && m[col+1][row].equals(".")) || (m[col+1][row].equals(".") && m[col-1][row].equals("."))){
                visited[0] = col;
                visited[1] = row;
                //System.out.println("Mark placed at (" + row + "," + col + ")");
            }

            // if you hit a dead end
            if(m[col][row+1] != "." && m[col-1][row] != "." && m[col+1][row] !="." && m[col][row-1] != "."){
                if(m[visited[0]+1][visited[1]].equals(".")){
                    //System.out.println("Move to mark");
                    backtrack(m, row, col);
                }
            }

            if(m[col][row+1].equals(".")){
                //System.out.println("right: " + m[col][row+1]);
                for(int i=0; i<12; i++){
                    for(int j=0; j<12; j++){
                        System.out.print(maze[i][j] + "  ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
                solveMaze(m, row+1, col);
            }
            else if(m[col-1][row].equals(".")){
                //System.out.println("up: " + m[col-1][row]);
                for(int i=0; i<12; i++){
                    for(int j=0; j<12; j++){
                        System.out.print(maze[i][j] + "  ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
                solveMaze(m, row, col-1);
            }
            else if(m[col+1][row].equals(".")){
                //System.out.println("down: " + m[col+1][row]);
                for(int i=0; i<12; i++){
                    for(int j=0; j<12; j++){
                        System.out.print(maze[i][j] + "  ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
                solveMaze(m, row, col+1);
            }
            else if(m[col][row-1].equals(".")){
                //System.out.println("left: " + m[col][row-1]);
                for(int i=0; i<12; i++){
                    for(int j=0; j<12; j++){
                        System.out.print(maze[i][j] + "  ");
                    }
                    System.out.println();
                }
                System.out.println("\n");
                solveMaze(m, row-1, col);
            }
        }
    }

    public void backtrack(String[][] m, int row, int col){
        System.out.println("Current coordinates: (" + row + "," + col + ")");

        if(row == visited[1] && col == visited[0]){
            solveMaze(m, visited[1], visited[0]+1);
        }

        if(visited[0] == end[1] && visited[1]+1 == end[0]){
            return;
        }

        m[col][row] = ".";
        if(m[col][row-1].equals("X")){
            //System.out.println("left: " + m[col][row-1]);
            for(int i=0; i<12; i++){
                for(int j=0; j<12; j++){
                    System.out.print(maze[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.println("\n");
            backtrack(m, row-1, col);
        }
        else if(m[col+1][row].equals("X")){
            //System.out.println("down: " + m[col+1][row]);
            for(int i=0; i<12; i++){
                for(int j=0; j<12; j++){
                    System.out.print(maze[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.println("\n");
            backtrack(m, row, col+1);
        }
        else if(m[col-1][row].equals("X")){
            //System.out.println("up: " + m[col-1][row]);
            for(int i=0; i<12; i++){
                for(int j=0; j<12; j++){
                    System.out.print(maze[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.println("\n");
            backtrack(m, row, col-1);
        }
        else if(m[col][row+1].equals("X")){
            //System.out.println("right: " + m[col][row+1]);
            for(int i=0; i<12; i++){
                for(int j=0; j<12; j++){
                    System.out.print(maze[i][j] + "  ");
                }
                System.out.println();
            }
            System.out.println("\n");
            backtrack(m, row+1, col);
        }
    }
}
