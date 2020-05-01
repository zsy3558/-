public class WhoWin {
    private int[][]	exist;
    public WhoWin(int exist[][]){
        this.exist=exist;
    }
    public int check(){
        if((rowWin()==1)||(columnWin()==1)||(rightSideWin()==1)||(leftSideWin()==1)){
            return 1;
        }else if((rowWin()==-1)||(columnWin()==-1)||(rightSideWin()==-1)||(leftSideWin()==-1)){
            return -1;
        }
        return 0;
    }
    /**
     * 以行的方式赢
     */
    public int rowWin(){
        for(int i=0;i<Config.ROWS;i++){
            for(int j=0;j<Config.COLUMNS-4;j++){
                if(exist[i][j]==-1){
                    if(exist[i][j+1]==-1&&exist[i][j+2]==-1&&exist[i][j+3]==-1&&exist[i][j+4]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i][j+1]==1&&exist[i][j+2]==1&&exist[i][j+3]==1&&exist[i][j+4]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    /**
     * 以列的方式赢
     */
    public int columnWin(){
        for(int i=0;i<Config.ROWS-4;i++){
            for(int j=0;j<Config.COLUMNS;j++){
                if(exist[i][j]==-1){
                    if(exist[i+1][j]==-1&&exist[i+2][j]==-1&&exist[i+3][j]==-1&&exist[i+4][j]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i+1][j]==1&&exist[i+2][j]==1&&exist[i+3][j]==1&&exist[i+4][j]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    /**
     * 斜的方式赢
     */
    public int rightSideWin(){  //正斜
        for(int i=0;i<Config.ROWS-4;i++){
            for(int j=0;j<Config.COLUMNS-4;j++){
                if(exist[i][j]==-1){
                    if(exist[i+1][j+1]==-1&&exist[i+2][j+2]==-1&&exist[i+3][j+3]==-1&&exist[i+4][j+4]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i+1][j+1]==1&&exist[i+2][j+2]==1&&exist[i+3][j+3]==1&&exist[i+4][j+4]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    public int leftSideWin(){   //反斜
        for(int i=4;i<Config.ROWS;i++){
            for(int j=0;j<Config.COLUMNS-4;j++){
                if(exist[i][j]==-1){
                    if(exist[i-1][j+1]==-1&&exist[i-2][j+2]==-1&&exist[i-3][j+3]==-1&&exist[i-4][j+4]==-1){
                        return -1;
                    }
                }
                if(exist[i][j]==1){
                    if(exist[i-1][j+1]==1&&exist[i-2][j+2]==1&&exist[i-3][j+3]==1&&exist[i-4][j+4]==1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
}
