import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class PieceListener extends MouseAdapter implements ActionListener {
    private int x,y;//点击坐标
    private Graphics g;//画笔
    private int count;//判断下一步是黑子还是白子
    private Graphics2D g2d;//画笔对象
    private int[][] exist;//棋子数组
    private HashMap<String,Integer> map=new HashMap<String,Integer>();//创建集合对象，用途是存储每一种棋子相连对应的权值
    private windows game;//面板;
    private WhoWin win;
    private int flags = 0;//步数
    private ArrayList<pieces> lists = new ArrayList<pieces>();//数组队列   存储的piece类型的对象
    /**
     * 构造方法
     * @param game
     */
    public PieceListener(windows game) {
        this.game=game;
    }
    /**
     * 设置方法，接收数组
     * @param exist 存储棋盘上棋子的位置
     */
    public void setExist(int[][] exist){
        this.exist=exist;
        win=new WhoWin(exist);
    }
    public void reset(){
        count = 1;
        flags = 0;
        for(int r = 0;r<exist.length;r++){
            for(int c = 0;c<exist[r].length;c++){
                exist[r][c] = 0;
            }
        }
        game.repaint();
    }
    public void mouseClicked(MouseEvent e){
        x = e.getX();
        y = e.getY();
        pve(x,y);
    }
    public void pve(int x,int y){
        flags ++;
        g = game.getGraphics();
        g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for (int r = 0;r<Config.ROWS;r++){
            for (int c = 0;c<Config.COLUMNS;c++){
                if(exist[r][c] == 0){//判断该位置上是否有棋子
                    if((Math.abs(x-Config.X0-c*Config.SIZE)<Config.SIZE/3.0)&&(Math.abs(y-Config.Y0-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
                        g2d.setColor(Color.BLACK);
                        exist[r][c]=1;		//记录下了黑色棋子的位置
                        g2d.fillOval(Config.X0+c*Config.SIZE-Config.Piece_SIZE/2,Config.Y0+r*Config.SIZE-Config.Piece_SIZE/2,Config.Piece_SIZE,Config.Piece_SIZE);
                        lists.add(new pieces(r,c));
                        if(win.check() == 1){
                            JOptionPane.showMessageDialog(game,"黑胜");
                            game.removeMouseListener(this);
                            return;
                        }
                        ai(g2d);
                        if(win.check() == -1){
                            JOptionPane.showMessageDialog(game,"白胜");
                            game.removeMouseListener(this);
                            return;
                        }
                    }
                }
            }
        }
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource() instanceof JButton){
            if(e.getActionCommand().equals("开始游戏")){
                MouseListener[] mls = game.getMouseListeners();
                if(mls.length>0){
                    game.removeMouseListener(this);
                }
                reset();
                game.addMouseListener(this);
            }else if (e.getActionCommand().equals("悔棋")){
                if(lists.size()>=2){
                    pieces p = lists.get(lists.size()-2);
                    int r= p.getX();
                    int c = p.getY();
                    exist[r][c] = 0;
                    pieces pAi = lists.get(lists.size()-1);
                    r = pAi.getX();
                    c = pAi.getY();
                    exist[r][c] = 0;
                    lists.remove(lists.size()-1);
                    lists.remove(lists.size()-1);
                    game.repaint();
                }
            }else if (e.getActionCommand().equals("重新开始")){
                reset();
                game.removeMouseListener(this);
            }
        }
    }
    public void ai(Graphics2D g2d){
        g2d.setColor(Color.WHITE);		//设置ai所下棋子的颜色为白色
        int[][] weightArray=new int[Config.ROWS][Config.COLUMNS];//创建一个存储权值的二维数组
        /**
         * 设置每种棋子相连情况下的权值
         */
        map.put("010", 1);
        map.put("0110", 20);
        map.put("01110", 50);
        map.put("011110", 500);
        map.put("0-10", 10);
        map.put("0-1-10", 30);
        map.put("0-1-1-10", 70);
        map.put("0-1-1-1-10", 500);
        map.put("-110", 1);
        map.put("-1110", 10);
        map.put("-11110", 30);
        map.put("-111110", 500);
        map.put("1-10", 1);
        map.put("1-1-10", 10);
        map.put("1-1-1-10", 30);
        map.put("1-1-1-1-10", 500);
        for(int r=0;r<exist.length;r++){	//求出权值  将权值存到数组中
            for(int c=0;c<exist[r].length;c++){
                if(exist[r][c]==0){			//判断是否是空位
                    String code=countHL(r,c);
                    Integer weight = map.get(code);//获取棋子相连情况对应的权值
                    if(null != weight){//判断权值不为null
                        weightArray[r][c] += weight;//累加权值
                    }
                    code=countVU(r,c);
                    weight = map.get(code);//获取棋子相连情况对应的权值
                    if(null != weight){//判断权值不为null
                        weightArray[r][c] += weight;//累加权值
                    }
                    code=countLLU(r,c);
                    weight = map.get(code);//获取棋子相连情况对应的权值
                    if(null != weight){//判断权值不为null
                        weightArray[r][c] += weight;//累加权值
                    }
                    code=countLRU(r,c);
                    weight = map.get(code);//获取棋子相连情况对应的权值
                    if(null != weight){//判断权值不为null
                        weightArray[r][c] += weight;//累加权值
                    }
                }
            }
        }
        int max=weightArray[0][0];                    //找出最大的权值
        for(int r=0;r<weightArray.length;r++){
            for(int c=0;c<weightArray[r].length;c++){
                if(weightArray[r][c]>max){
                    max=weightArray[r][c];
                }
            }
        }
        for(int r=0;r<weightArray.length;r++){				//随机取最大权值处所在的点
            for(int c=0;c<weightArray[r].length;c++){
                if(weightArray[r][c]==max&&exist[r][c]==0){	//权值最大且是空位
                    exist[r][c]=-1;
                    g2d.fillOval(Config.X0+c*Config.SIZE-Config.Piece_SIZE/2,Config.Y0+r*Config.SIZE-Config.Piece_SIZE/2 , Config.Piece_SIZE, Config.Piece_SIZE);
                    lists.add(new pieces(r,c));
                    return;
                }
            }
        }
    }
    private String countHL(int r,int c){
        String code = "0";	//默认记录r,c位置的情况
        int chess = 0;		//记录第一次出现的棋子
        for(int c1=c-1;c1>=0;c1--){		//向左统计
            if(exist[r][c1]==0){		//表示没有棋子的位置
                if(c1+1==c){			//相邻的空位
                    break;
                }else{					//空位不相连
                    code = exist[r][c1] + code;//记录棋子相连的情况
                    break;
                }
            }else{				//表示有棋子
                if(chess==0){	//判断是否是第一次出现棋子
                    code = exist[r][c1] + code;	//记录棋子相连的情况
                    chess = exist[r][c1];		//记录第一次的棋子的颜色
                }else if(chess==exist[r][c1]){	//表示之后的棋子和第一次的棋子颜色一致
                    code = exist[r][c1] + code;	//记录棋子相连的情况
                }else{							//表示之后的棋子和第一次的棋子颜色不同
                    code = exist[r][c1] + code;	//记录棋子相连的情况
                    break;
                }
            }
        }
        return code;
    }

    /**
     * 垂直向上统计棋子相连的情况
     * @param r	行
     * @param c	列
     * @return
     */
    private String countVU(int r,int c){
        String code = "0";					//默认记录r,c位置的情况
        int chess = 0;						//记录第一次出现的棋子
        for(int r1=r-1;r1>=0;r1--){			//向上统计
            if(exist[r1][c]==0){			//表示没有棋子
                if(r1+1==r){				//相邻的空位
                    break;
                }else{						//不相邻的空位
                    code = exist[r1][c] + code;	//记录棋子相连的情况
                    break;
                }
            }else{				//表示有棋子
                if(chess==0){	//判断是否是第一次出现棋子
                    code = exist[r1][c] + code;		//记录棋子相连的情况
                    chess = exist[r1][c];			//记录第一次的棋子的颜色
                }else if(chess==exist[r1][c]){		//表示之后的棋子和第一次的棋子颜色一致
                    code = exist[r1][c] + code;		//记录棋子相连的情况
                }else{		//表示之后的棋子和第一次的棋子颜色不同
                    code = exist[r1][c] + code;		//记录棋子相连的情况
                    break;
                }
            }
        }
        return code;
    }
    /**
     * 正斜(\)   棋子相连统计
     * @param r
     * @param c
     * @return
     */
    private String countLLU(int r,int c){
        String code = "0";		//默认记录r,c位置的情况
        int chess = 0;			//记录第一次出现的棋子
        for(int r1=r-1,c1=c-1;r1>=0&&c1>0;r1--,c1--){//向上统计
            if(exist[r1][c1]==0){	//表示没有棋子
                if(r1+1==r&&c1+1==c){	//相邻的空位
                    break;
                }else{					//不相邻的空位
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                    break;
                }
            }else{		//表示有棋子
                if(chess==0){	//判断是否是第一次出现棋子
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                    chess = exist[r1][c1];		//记录第一次的棋子的颜色
                }else if(chess==exist[r1][c1]){		//表示之后的棋子和第一次的棋子颜色一致
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                }else{					//表示之后的棋子和第一次的棋子颜色不同
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                    break;
                }
            }
        }
        return code;
    }
    /**
     * 反斜(/)   棋子相连的统计
     * @param r
     * @param c
     * @return
     */
    private String countLRU(int r,int c){
        String code = "0";		//默认记录r,c位置的情况
        int chess = 0;			//记录第一次出现的棋子
        for(int r1=r-1,c1=c+1;r1>=0&&c1<Config.COLUMNS;r1--,c1++){
            if(exist[r1][c1]==0){	//表示没有棋子
                if(r1+1==r&&c1-1==c){	//相邻的空位
                    break;
                }else{
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                    break;
                }
            }else{		//表示有棋子
                if(chess==0){//判断是否是第一次出现棋子
                    code = exist[r1][c1] + code;		//记录棋子相连的情况
                    chess = exist[r1][c1];			//记录第一次的棋子的颜色
                }else if(chess==exist[r1][c1]){		//表示之后的棋子和第一次的棋子颜色一致
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                }else{					//表示之后的棋子和第一次的棋子颜色不同
                    code = exist[r1][c1] + code;	//记录棋子相连的情况
                    break;
                }
            }
        }
        return code;
    }
}
