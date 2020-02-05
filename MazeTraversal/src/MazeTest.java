public class MazeTest {
    public static String[][] maze = new String[12][12];
    private static int[] start = new int[2];
    private static int[] end = new int[2];

    public static void main(String[] args){
        mazeEx2();
        Maze m = new Maze(maze, start, end);

    }

    public static void mazeEx(){
        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                if(j==0){
                    if(i==2){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==1){
                    if(i==1 || i==2 || i==4 || i==6 || i==8 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==2){
                    if(i==1 || i==4 || i==6 || i==7 || i==8 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==3){
                    if(i==1 || i==2 || i==3 || i==4 || i==8 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==4){
                    if(i==4 || i==5 || i==6 || i==7 || i==8 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==5){
                    if(i==1 || i==2 || i==3 || i==8 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==6){
                    if(i==1 || i==3 || i==5 || i==6 || i==7 || i==8 || i==9 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==7){
                    if(i==1 || i==3 || i==8){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==8){
                    if(i==1 || i==3 || i==4 || i==5 || i==6 || i==7 || i==8 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==9){
                    if(i==1 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==10){
                    if(i==1 || i==2 || i==3 || i==4 || i==5 || i==6 || i==7 || i==8 || i==9 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==11){
                    if(i==4){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
            }
        }

        start[0] = 0;
        start[1] = 2;
        end[0] = 11;
        end[1] = 4;
    }

    public static void mazeEx2(){
        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                if(j==0){
                    if(i==4){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==1){
                    if(i==4 || i==9){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==2){
                    if(i==1 || i==2 || i==4 || i==6 || i==7 || i==8 || i==9){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==3){
                    if(i==2 || i==4 || i==8){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==4){
                    if(i==2 || i==4 || i==5 || i==6 || i==7 || i==8){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==5){
                    if(i==2 || i==8 || i==9 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==6){
                    if(i==1 || i==2 || i==3 || i==4 || i==5 || i==6 || i==8){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==7){
                    if(i==2 || i==6 || i==7 || i==8){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==8){
                    if(i==1 || i==2 || i==3 || i==4){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==9){
                    if(i==1 || i==4 || i==5 || i==6 || i==7 || i==8 || i==9 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==10){
                    if(i==7 || i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
                else if(j==11){
                    if(i==10){
                        maze[i][j] = ".";
                    }
                    else{
                        maze[i][j] = "#";
                    }
                }
            }
        }
        start[0] = 0;
        start[1] = 4;
        end[0] = 11;
        end[1] = 10;
    }
}
