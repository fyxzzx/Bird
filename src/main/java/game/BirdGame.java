package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * 游戏界面
 */
public class BirdGame extends JPanel implements Runnable{
    // 开始图片
    BufferedImage startImage;
    // 结束图片
    BufferedImage gameOverImage;

    // 地面
    Ground ground;
    // 柱子
    Column column1, column2;
    // 小鸟
    Bird bird;

    // 游戏分数
    int score;

    // 游戏状态
    int state;
    // 状态常量
    public static final int START = 0; //开始
    public static final int RUNNING = 1; //运行
    public static final int GAME_OVER = 2; //结束
    // 背景图片
    BufferedImage background;

    //线程
    Thread t;

    public void run() {
        try {//测试多线程
            while (true) {
                switch (state) {
                    case START:
                        // 小鸟做出飞行动作
                        bird.fly();
                        break;
                    case RUNNING:
                        // 小鸟做出飞行动作
                        bird.fly();
                        // 小鸟上下移动一步
                        bird.step();
                        // 检测是否发生碰撞
                        if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)) {
                            state = GAME_OVER;
                        }
                        if(state == GAME_OVER){
                            JOptionPane.showMessageDialog(null, "得分："+score, "就这，就这？",JOptionPane.WARNING_MESSAGE);
                        }
                        break;
                }

                // 重新绘制界面
                repaint();
                // 休眠 1000/60 毫秒
                Thread.sleep(1000 / 60);//30
            }
        }catch (InterruptedException e) {
        }
    }

    public void start (String threadName) {
        if (t == null) {
            t = new Thread (this, threadName);

            t.start ();
        }
    }


    /**
     * 初始化游戏
     */
    public BirdGame() throws Exception {
        // 初始化背景图片
        ClassLoader classLoader = BirdGame.class.getClassLoader();
        background = ImageIO.read(classLoader.getResource("bg.png"));
        // 初始化开始、结束图片
        startImage = ImageIO.read(classLoader.getResource("start.png"));
        gameOverImage = ImageIO.read(classLoader.getResource("gameover.png"));

        // 初始化地面、柱子、小鸟
        ground = new Ground();
        column1 = new Column(1);
        column2 = new Column(2);
        bird = new Bird();

        // 初始化分数
        score = 0;

        // 初始化状态
        state = START;
    }//getClass().getResource("/resources/bg.png")

    /**
     * 绘制界面
     */
    public void paint(Graphics g) {
        // 绘制背景
        g.drawImage(background, 0, 0, null);
        // 绘制地面
        g.drawImage(ground.image, ground.x, ground.y, null);

        // 绘制柱子
        g.drawImage(column1.image, column1.x - column1.width / 2, column1.y
                - column1.height / 2, null);
        g.drawImage(column2.image, column2.x - column2.width / 2, column2.y
                - column2.height / 2, null);

        // 绘制小鸟（旋转坐标系）
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(-bird.alpha, bird.x, bird.y);
        g.drawImage(bird.image,
                bird.x - bird.width / 2, bird.y - bird.height / 2, null);
        g2.rotate(bird.alpha, bird.x, bird.y);

        // 绘制分数
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 40);
        g.setFont(f);
        g.drawString("" + score, 40, 60);
        g.setColor(Color.WHITE);
        g.drawString("" + score, 40 - 3, 60 - 3);

        // 绘制开始与结束界面
        switch (state) {
            case START:
                g.drawImage(startImage, 0, 0, null);
                break;
            case GAME_OVER:
                g.drawImage(gameOverImage, 0, 0, null);
                break;
        }
    }
    // 开始游戏
    public void action() throws Exception {
        // 鼠标***
        MouseListener l = new MouseAdapter() {
            // 鼠标按下事件
            public void mousePressed(MouseEvent e) {
                try {
                    switch (state) {
                        case START:
                            // 在开始状态，按下鼠标则转为运行状态。
                            state = RUNNING;
                            break;
                        case RUNNING:
                            // 在运行状态，按下鼠标则小鸟向上飞行。
                            bird.flappy();
                            break;
                        case GAME_OVER:
                            // 在结束状态，按下鼠标则重置数据，再次转为开始态。
                            column1 = new Column(1);
                            column2 = new Column(2);
                            bird = new Bird();
                            score = 0;
                            state = START;
                            break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        // 将***添加到当前的面板上
        addMouseListener(l);
        start("b1");
        //测试多线程
        while (true) {
            switch (state) {
                case START:
                    // 地面向左移动一步
                    ground.step();
                    break;
                case RUNNING:
                    // 地面向左移动一步
                    if(score<2){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                    }
                    else if(score<4){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=1;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<8){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=2;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<16){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=3;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<24){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=4;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<32){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=5;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<40){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=6;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<50){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=7;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<60){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=8;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else {
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=9;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    // 计算分数
                    if (bird.x == column1.x || bird.x == column2.x) {
                        score++;
                    }
                    // 检测是否发生碰撞
                    if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)) {
                        state = GAME_OVER;
                    }
                    if(state == GAME_OVER){
                        JOptionPane.showMessageDialog(null, "得分："+score, "就这，就这？",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
            }
            // 重新绘制界面
            repaint();
            // 休眠 1000/60 毫秒
            Thread.sleep(1000 / 30);//30
        }
/*
        // 不断的移动与重绘
        while (true) {
            switch (state) {
                case START:
                    // 小鸟做出飞行动作
                    bird.fly();
                    // 地面向左移动一步
                    ground.step();
                    break;
                case RUNNING:
                    // 地面向左移动一步
                    if(score<2){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                    }
                    else if(score<4){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=1;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<8){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=2;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<16){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=3;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<24){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=4;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<32){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=5;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<40){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=6;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<50){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=7;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else if(score<60){
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=8;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    else {
                        ground.step();
                        // 柱子向左移动一步
                        column1.step();
                        column2.step();
                        int sum=9;
                        while(sum>0){
                            if (bird.x == column1.x || bird.x == column2.x) {
                                score++;
                            }
                            column1.step();
                            column2.step();
                            sum--;
                        }
                    }
                    // 小鸟做出飞行动作
                    bird.fly();
                    // 小鸟上下移动一步
                    bird.step();
                    // 计算分数
                    if (bird.x == column1.x || bird.x == column2.x) {
                        score++;
                    }
                    // 检测是否发生碰撞
                    if (bird.hit(ground) || bird.hit(column1) || bird.hit(column2)) {
                        state = GAME_OVER;
                    }
                    if(state == GAME_OVER){
                        JOptionPane.showMessageDialog(null, "得分："+score, "就这，就这？",JOptionPane.WARNING_MESSAGE);
                    }
                    break;
            }
            // 重新绘制界面
            repaint();
            // 休眠 1000/60 毫秒
            Thread.sleep(1000 / 30);//30
        }*/
    }
    /**
     * 启动方法
     */
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        BirdGame game = new BirdGame();
        frame.add(game);
        frame.setTitle("真男人就过一百根柱子");
        frame.setSize(440, 670);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        game.action();
    }

}