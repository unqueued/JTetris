import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.Toolkit.*;
import java.awt.*;

public class JTetris extends javax.swing.JPanel implements KeyListener {

	BlockBoard blockBoard;
	JPanel gamePanel;
	JPanel titlePanel;
        JPanel previewPanel;
        int gameSpeed;
        Timer driver;
        JTetris top;
        JLabel message;
        JLabel scoreLabel;
        int level;
        Font textFont;
        About a;
        boolean suspended;
        int score;

	public JTetris() {
                a = new About(this);
                top = this;
                
                initVars();
                
                textFont = new Font("Serif", Font.PLAIN, 11);
                
                previewPanel = new PreviewPanel();
		gamePanel = new GamePanel();
		titlePanel = new TitlePanel();
                
                
                driver = new Timer(gameSpeed, new TimeListener());
                driverOn();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setPreferredSize(new Dimension(240, 350));
                
                setBackground(Color.lightGray);
                
		add(titlePanel);
		add(gamePanel);
                
		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
	}
        
        public void initVars() {
                score = 0;
                gameSpeed = 600;
                level = 1;
                
                if(scoreLabel == null)
                        scoreLabel = new JLabel("Score: "+score);
                else
                        scoreLabel.setText("Score: "+score);
                
                if(message == null)
                        message = new JLabel("Level: "+level);
                else
                        message.setText("Level: "+level);
                
                suspended = false;
        }
	
	public static void main(String[] args) {                
		JFrame f = new JFrame("jTetris");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.getContentPane().add(new JTetris());
		f.setResizable(false);
		
		f.pack();
		f.show();
	}
        
        public void gameOver() {
                setMessage("Game Over!");
        }
        
        private void setMessage(String message) {
                this.message.setText(message);
        }
        
        public void gotLine() {
                score += 10;
                scoreLabel.setText("Score "+score);
                levelCheck();
        }
        
        private void levelCheck() {
                if(score > level*100) {
                        level ++;
                        setMessage("Level: "+level);
                        if(gameSpeed > 0)
                            driver.setDelay(gameSpeed -= 100);
                }
        }
	
	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {
                //System.out.print(e.getKeyCode()+" ");
                if(!suspended) {
                        if(e.getKeyCode() == 40)	//**** Go back and use Java constants
                                blockBoard.inputDown();
                        if(e.getKeyCode() == 37)
                                blockBoard.inputLeft();
                        if(e.getKeyCode() == 39)
                                blockBoard.inputRight();
                        if(e.getKeyCode() == 32 || e.getKeyCode() == 38)
                                blockBoard.inputSpace();
                        if(e.getKeyCode() == 81)
                                die();
                        if(e.getKeyCode() == 78)
                                newGame();
                        if(e.getKeyCode() == 80)
                                pause();
                        if(e.getKeyCode() == 65)
                                a.show();
                        
                        blockBoard.draw();
                }
		
	}
        
        public void die() {
                System.exit(0);
        }
        
        public void newGame() {
                initVars();
                repaint();
                blockBoard.gamePauseOff();
                blockBoard.newGame();
        }
        
        private void pause() {
                if(blockBoard.getRunning()) {
                        pauseOn();
                } else {
                        pauseOff();
                }
        }
        
        public void pauseOn() {
                blockBoard.gamePauseOn();
        }
        
        public void pauseOff() {
                blockBoard.gamePauseOff();
        }
        
        public void suspend() {
                pauseOn();
                suspended = true;
        }
        
        public void unSuspend() {
                pauseOff();
                suspended = false;
        }
        
        private void driverOn() {
                if(driver == null)
                        driver = new Timer(gameSpeed, new TimeListener());

                driver.start();
        }
        
        private void driverOff() {
                driver.stop();
        }
	
	public void keyReleased(KeyEvent e) {}
        
        private class TimeListener implements ActionListener {
                public void actionPerformed(ActionEvent e) {
                        blockBoard.inputDown();
                        blockBoard.draw();
                }
        }
	
	private class GamePanel extends JPanel {
            
		private GamePanel() {
			
                        setBackground(Color.lightGray);
			add(new ScorePanel());
			add(new BoardPanel());
		}
		
		private class BoardPanel extends JPanel {
			private BoardPanel() {
				setBackground(Color.white);
				setPreferredSize(new Dimension(100, 240));
				
				blockBoard = new BlockBoard(this, top, previewPanel);
			}
		}
		
		private class ScorePanel extends JPanel {
			private ScorePanel() {
                                JLabel textLabel;
                                
                                textLabel = new JLabel(
                                    "<html>"+
                                    "<b>q</b> - Quit<br>"+
                                    "<b>n</b> - New Game<br>"+
                                    "<b>p</b> - Pause<br>"+
                                    "<b>a</b> - About<br>"+
                                    "<b>s</b> - High scores<br>"+
                                    "<br>"+
                                    "Next Piece:"+
                                    "</html>"
                                    );
                                
                                textLabel.setFont(textFont);
                                textLabel.setForeground(Color.black);
                                
                                message.setFont(textFont);
                                message.setForeground(Color.black);
                                
                                scoreLabel.setFont(textFont);
                                scoreLabel.setForeground(Color.black);
                                //message.setBorder(BorderFactory.createLineBorder(Color.red, 1));
                                
				setBackground(Color.lightGray);
				setPreferredSize(new Dimension(80, 240));
                                
                                add(scoreLabel);
                                add(message);
                                add(textLabel);
                                add(previewPanel);
                                
                        }
		}
	}
	
	private class TitlePanel extends JPanel {
		private TitlePanel() {
                        setBackground(Color.lightGray);
			setMaximumSize(new Dimension(100, 50));
			add(new JLabel("jTetris"));
		}
	}
        
        private class PreviewPanel extends JPanel {
                private PreviewPanel() {
                        setPreferredSize(new Dimension(50,50));
                        setBackground(Color.lightGray);
                }
        }
}