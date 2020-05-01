import javax.swing.*;
import java.awt.*;

public class windows extends JPanel {
    private PieceListener pl;
    private int[][] exist=new int[Config.ROWS][Config.COLUMNS];
    public void showUI(){
        JFrame frame = new JFrame();
        frame.setTitle("五子棋");
        frame.setSize(750,650);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(3);
        pl = new PieceListener(this);
        centerPanel(frame);
        rightPanel(frame);

        frame.setVisible(true);
        pl.setExist(exist);
    }

    private void rightPanel(JFrame frame) {
        JPanel rPanel = new JPanel();
        rPanel.setBackground(Color.GRAY);
        rPanel.setPreferredSize(new Dimension(150,600));
        rPanel.setLayout(new FlowLayout());
        String[] buttonArray = {"先手","后手","悔棋","重新开始"};
        for(int i = 0;i<buttonArray.length;i++){
            JButton button = new JButton(buttonArray[i]);
            button.setPreferredSize(new Dimension(100,50));
            rPanel.add(button);
            button.addActionListener(pl);
            frame.add(rPanel,BorderLayout.EAST);
        }


    }

    private void centerPanel(JFrame frame) {
        this.setBackground(Color.ORANGE);
        frame.add(this);
    }
    public void drawTable(Graphics g){
        for(int r = 0;r<Config.ROWS;r++){
            g.drawLine(Config.X0,Config.Y0+r*Config.SIZE,Config.X0+(Config.COLUMNS-1)*Config.SIZE, Config.Y0+r*Config.SIZE);
        }
        for (int c = 0;c<Config.COLUMNS;c++){
            g.drawLine(Config.X0+c*Config.SIZE,Config.Y0,Config.X0+c*Config.SIZE,Config.Y0+(Config.ROWS-1)*Config.SIZE);
        }
    }

    public void reDrawPiece(Graphics g){
        Graphics2D g2d = (Graphics2D) g;//转为Graphics2D   后面要为画笔设置颜色
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        for(int r = 0;r<Config.ROWS;r++){
            for(int c = 0;c<Config.COLUMNS;c++){
                if(exist[r][c] != 0){
                    if(exist[r][c] == 1){
                        g2d.setColor(Color.BLACK);
                        g2d.fillOval(Config.X0+c*Config.SIZE-Config.Piece_SIZE/2,Config.Y0+r*Config.SIZE-Config.Piece_SIZE/2,Config.Piece_SIZE,Config.Piece_SIZE);
                    }else if(exist[r][c] == -1){
                        g2d.setColor(Color.WHITE);
                        g2d.fillOval(Config.X0+c*Config.SIZE-Config.Piece_SIZE/2,Config.Y0+r*Config.SIZE-Config.Piece_SIZE/2,Config.Piece_SIZE,Config.Piece_SIZE);
                    }
                }
            }
        }
    }
    public void paint(Graphics g){
        super.paint(g);
        drawTable(g);
        reDrawPiece(g);
    }
    public static void main(String[] args){
        windows game = new windows();
        game.showUI();
    }
}
