import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class BlockBoard {
	
	private Block[][] blockMatrix, previewMatrix;
	private static final int 
		WIDTH = 10,
		HEIGHT = 24,
		BLOCKWIDTH = 10,
		BLOCKHEIGHT = 10,
                PREVIEWSIZE = 10;
	private Component c, previewC;
	private final int 
		startingShapeX = 6,
		startingShapeY = 2;
	private Shape shapeCursor, previewCursor;
        private JTetris parent;
        private boolean running = true;
	//private final Shape shapeBox = new Shape(Color.green);
        
        private final ArrayList 
            shapel = new ArrayList(2),
            shapeT = new ArrayList(4),
            shapeBox = new ArrayList(1),
            shapeBigL = new ArrayList(4),
            shapeLeftZag = new ArrayList(2),
            shapeRightZag = new ArrayList(2);
        
        private ArrayList currentShapeList, nextShapeList;
        
        private int shapeIndex = 0;
	
	public BlockBoard(Component c, JTetris j, Component previewC) {
		this.c = c;
                this.previewC = previewC;
                
                parent = j;
	
		blockMatrix = new Block[WIDTH][HEIGHT];
                previewMatrix = new Block[5][5];
		
		for(int i = 0; i < blockMatrix.length; i ++)
			for(int ii = 0; ii < blockMatrix[0].length; ii ++)
				blockMatrix[i][ii] = new Block(new Dimension(BLOCKWIDTH, BLOCKHEIGHT));
                
                for(int i = 0; i < previewMatrix.length; i ++)
                        for(int ii = 0; ii < previewMatrix[0].length; ii ++)
                                previewMatrix[i][ii] = new Block(new Dimension(PREVIEWSIZE,PREVIEWSIZE));
		
		createShapes();
                
                newGame();
	}
        
        public void newGame() {
                
                Random rnd = new Random();
                
                resetBlocks(blockMatrix);
                
                currentShapeList = getNewShapeList();
                nextShapeList = getNewShapeList();
		shapeCursor = (Shape)currentShapeList.get(rnd.nextInt(currentShapeList.size()));		/*************************/
                previewCursor = (Shape)nextShapeList.get(0);
                gamePauseOff();
                hitBottom();
        }
	
	public boolean draw(Block[][] blocks) {
		Graphics g = c.getGraphics();
                
		if(g == null)
			return false;
		else {
			if(shapeCursor != null)
				shapeCursor.draw();
				
			drawBlocks(g, blocks);
			
			return true;
		}
	}
        
        public boolean draw() {
		Graphics g = c.getGraphics();
                
		if(g == null)
			return false;
		else {
			if(shapeCursor != null)
				shapeCursor.draw();
				
			drawBlocks(g, blockMatrix);
			
			return true;
		}
	}
        
        public boolean drawPreview() {
                Graphics gg = previewC.getGraphics();
                
                if(gg == null)
                        return false;
                else {
                        if(previewCursor != null)
                                previewCursor.draw(previewMatrix, 2, 2);
                        
                        drawBlocks(gg, previewMatrix);
                        //gg.setColor(Color.black);
                        //gg.drawOval(14,14,14,14);
                        
                        return true;
                }
        }
	
	private void drawBlocks(Graphics g, Block[][] blocks) {
		for(int yi = 0; yi < blocks[0].length; yi ++)
			for(int xi = 0; xi < blocks.length; xi ++)
				blocks[xi][yi].draw(g.create(xi * 10, yi * 10, 10, 10));
	}
	
	private void resetBlocks(Block[][] blockMatrix) {
		for(int i = 0; i < blockMatrix.length; i ++)
			for(int ii = 0; ii < blockMatrix[0].length; ii ++)
				blockMatrix[i][ii].setOff();
				//blockMatrix[i][ii].setOn(Color.blue);
	}
        
	private void resetBlocks(Block[][] blockMatrix, Color newColor) {
		for(int i = 0; i < blockMatrix.length; i ++)
			for(int ii = 0; ii < blockMatrix[0].length; ii ++)
				blockMatrix[i][ii].setOff(newColor);
	}
	
	//***************Rewrite inputs later***************
	public void inputLeft() {
                if(!running)
                        return;
                
		shapeCursor.moveLeft();
	}
	
	public void inputRight() {
                if(!running)
                        return;
                
		shapeCursor.moveRight();
	}
	
	public void inputDown() {                
                if(!running)
                        return;
                
		shapeCursor.moveDown();
	}
	
        public void inputSpace() {
                if(!running)
                        return;
                
                rotate();
        }
        
        public void rotate() {
                
                int x, y, shapeIndex;
                Shape shapeCursor;
                
                if(currentShapeList.size() < 2)
                    return;
                /*
                 *Note to self:
                 *If a shapelist has only one element,
                 *Don't bother even trying to rotate it
                 *The next cursor will point to the same
                 *Shape instance and everything will suck
                 */
                
                if(this.shapeIndex >= currentShapeList.size() - 1)
                        shapeIndex = 0;
                else
                        shapeIndex = this.shapeIndex + 1;
                
                x = this.shapeCursor.getX();
                y = this.shapeCursor.getY();
                
                
                shapeCursor = (Shape) currentShapeList.get(shapeIndex);
                
                this.shapeCursor.clearShape(this.shapeCursor.getX(), this.shapeCursor.getY());
                
                if(shapeCursor.assertMove(x,y)) {
                        shapeCursor.moveTo(x,y);
                        this.shapeCursor.resetShape();
                        this.shapeIndex = shapeIndex;
                        this.shapeCursor = shapeCursor;
                        shapeCursor = null;
                } else {
                        this.shapeCursor.draw();
                        shapeCursor = null;
                }
        }
        
        public void gamePauseOn() {
                running = false;
                /*Graphics g = c.getGraphics();
                System.out.println(g);
                g.setColor(Color.blue);
                g.drawOval(23,23,23,23);*/
        }
        
        public void gamePauseOff() {
                running = true;
        }
        
        private void gameOver() {
                Graphics g = c.getGraphics();
                
                shapeCursor.clearShape(shapeCursor.getX(), shapeCursor.getY());
                shapeCursor = null;
                
                for(int yi = 0; yi < blockMatrix[0].length; yi ++)
                        for(int xi = 0; xi < blockMatrix.length; xi ++) {
                                blockMatrix[xi][yi].setOn(Color.red);
                        }
                
                drawBlocks(g, blockMatrix);
                running = false;
                parent.gameOver();
        }
        
        private ArrayList getNewShapeList() {
                Random rnd = new Random();
                
                ArrayList shape = null;
                
		switch(rnd.nextInt(6)) {
			case 0: shape = shapeT; break;
			case 1: shape = shapeBigL; break;
			case 2: shape = shapel; break;
			case 3: shape = shapeBox; break;
			case 4: shape = shapeLeftZag; break;
			case 5: shape = shapeRightZag; break;
		}
                
                return shape;
        }
	
	private void hitBottom() {
                
                Random rnd = new Random();
	
                shapeCursor.resetShape();
                shapeCursor = null;
                
                resetBlocks(previewMatrix, Color.lightGray);
                previewCursor = null;
                
                
                currentShapeList = nextShapeList;
                nextShapeList = getNewShapeList();
                
                
                
                shapeIndex = rnd.nextInt(currentShapeList.size());
                
                //Asign cursor to new shape graph
                shapeCursor = (Shape) currentShapeList.get(shapeIndex);
                previewCursor = (Shape) nextShapeList.get(0);
                //Make sure newly created shape has room to exist
                if(!(shapeCursor.assertMove(shapeCursor.getX(),shapeCursor.getY())))
                        gameOver();
                
                watchLine();
	}
        
        private void watchLine() {
                if(!running)
                        return;
                
                for(int yi = 0; yi < blockMatrix[0].length; yi ++) {
                        boolean isFound = false;
                        for(int xi = 0; xi < blockMatrix.length; xi ++) {
                                if(blockMatrix[xi][yi].isOff()) {
                                        isFound = true;
                                        break;
                                }
                        }
                        if(!isFound) {
                                //inform containging frame
                                parent.gotLine();
                                
                                //Make the row glow for a little first
                                for(int xii = 0; xii <= blockMatrix.length - 1; xii ++)
                                        blockMatrix[xii][yi].setGlowing(true);
                                
                                parent.suspend();
                                
                                for(int i = 0; i < 30; i++)
                                        draw();
                                
                                parent.unSuspend();
                                
                                for(int xii = 0; xii <= blockMatrix.length - 1; xii ++)
                                        blockMatrix[xii][yi].setGlowing(false);
                                
                                shapeCursor.clearShape(shapeCursor.getX(),shapeCursor.getY());
                                for(int yii = yi; yii >= 0; yii --)  {
                                        for(int xii = 0; xii <= blockMatrix.length - 1; xii ++) {
                                                if(yii > 0) {
                                                        if(blockMatrix[xii][yii - 1].isOff())
                                                              blockMatrix[xii][yii].setOff();
                                                        else if(!(blockMatrix[xii][yii - 1].isOff()))
                                                              blockMatrix[xii][yii].setOn();
                                                        blockMatrix[xii][yii].setColor(blockMatrix[xii][yii - 1].getColor());
                                                } else {
                                                        blockMatrix[xii][yii].setOff();
                                                }
                                        }
                                }
                                shapeCursor.draw();
                        } else {
                                isFound = false;
                        }
                }
        }
	
	private void createShapes() {
		//Link together Shape Grahps to make different shape collections.
		
		//************************
		//MAKE THIS MORE EFFICIENT
		//************************
		
		//First shape, "Big L"
                //First aspect:
		//  #
		//  #
		//  ##
                shapeBigL.add(
                    new Shape(Color.blue).
                        newUp().
                        getParent().
                        newDown().
                        newRight().
                        getHead()
                    );
                //Second aspect:
                // ###
                // #
                shapeBigL.add(
                    new Shape(Color.blue).
                        newRight().
                        getParent().
                        newLeft().
                        newDown().
                        getHead()
                    );
                //Third aspect:
                // ##
                //  #
                //  #
                shapeBigL.add(
                    new Shape(Color.blue).
                        newUp().
                        newLeft().
                        getHead().
                        newDown().
                        getHead()
                );
                //Fourth aspect:
                //   #
                // ###
                shapeBigL.add(
                    new Shape(Color.blue).
                        newLeft().
                        getParent().
                        newRight().
                        newUp().
                        getHead()
                );
			
		//Shape "little L"
                //Add first aspect:
                //  #
                //  #
                //  #
                //  #
                shapel.add(
                    new Shape(Color.red).
                        newUp().
                        getParent().
                        newDown().
                        newDown().
                        getHead()
                    );
                //Add second aspect:
                // ####
                shapel.add(
                    new Shape(Color.red).
                        newLeft().
                        getParent().
                        newRight().
                        newRight().
                        getHead()
                    );

		//Shape "Box"
                // ##
                // ##
                shapeBox.add(
                    new Shape(Color.green).
                        newRight().
                        newDown().
                        newLeft().
                        getHead()
                    );
			
		//Shape "Left zig-zag"
                //First aspect:
                //  #
                // ##
                // # 
                shapeLeftZag.add(
                    new Shape(Color.red).
                        newUp().
                        getParent().
                        newLeft().
                        newDown().
                        getHead()
                    );
                //Second aspect:
                // ##
                //  ##
                shapeLeftZag.add(
                    new Shape(Color.red).
                        newUp().
                        newLeft().
                        getHead().
                        newRight().
                        getHead()
                    );
			
		//Shape "Right zig-zag"
                //First aspect:
                // #
                // ##
                //  #
                shapeRightZag.add(
                    new Shape(Color.yellow).
                        newUp().
                        getParent().
                        newRight().
                        newDown().
                        getHead()
                    );
                //Second aspect:
                //  ##
                // ##
                shapeRightZag.add(
                    new Shape(Color.yellow).
                        newUp().
                        newRight().
                        getHead().
                        newLeft().
                        getHead()
                    );
			
		//Shape that looks like an upside-down "Upper-case T"
                //First aspect:
                // #
                // ##
                // #
                shapeT.add(
                    new Shape(Color.orange).
                        newUp().
                        getParent().
                        newRight().
                        getParent().
                        newDown().
                        getHead()
                    );
                //Second aspect:
                // ###
                //  #
                shapeT.add(
                    new Shape(Color.orange).
                        newLeft().
                        getParent().
                        newDown().
                        getParent().
                        newRight().
                        getHead()
                    );
                //Third aspect:
                //  #
                // ##
                //  #
                shapeT.add(
                    new Shape(Color.orange).
                        newUp().
                        getParent().
                        newLeft().
                        getParent().
                        newDown().
                        getHead()
                    );
                //Fourth aspect:
                //  #
                // ###
                shapeT.add(
                    new Shape(Color.orange).
                        newUp().
                        getParent().
                        newLeft().
                        getParent().
                        newRight().
                        getHead()
                    );
	}
	
	public String toString() {
		String r = new String();
		for(int yi = 0; yi < blockMatrix[0].length; yi ++) {
			for(int xi = 0; xi < blockMatrix.length; xi ++)
				if(blockMatrix[xi][yi].isOff())
					r = new String(r + "0");
				else
					r = new String(r + "1");
			
			r = new String(r + "\n");
		}
		
		return r;
	}
        
        public boolean getRunning() {
                return running;
        }
        
	private class Shape {
		//Shape is a Node that stores the brick shape.
		//The entry point through which all access to the graph
		//Is the first graph created, which will have its parent pointer
		//set to null.
		//
		//By passing a graphics context into the head node, or a pointer
		//to the Block matrix, the entire graph can do collision detection
		//and drawing, recursevilly.
		private Shape up;
		private Shape down;
		private Shape left;
		private Shape right;
		private Shape parent;
		private Shape head;
		private int x;
		private int y;		//Add way to override this later!!
		
		/****************************
		Upgrade to Java 1.5 to get
		propper enumerated type support!@
		****************************/
		
		private final int DIRECTION_DOWN = 0;
		private final int DIRECTION_LEFT = 1;
		private final int DIRECTION_RIGHT = 2;
		private Color shapeColor = Color.gray;
	
		public Shape(Color shapeColor) {
			head = this;
			x = startingShapeX;
			y = startingShapeY;
			this.shapeColor = shapeColor;
		}
		
		private Shape(Shape parent) {
			this.parent = parent;
			this.head = parent.getHead();
			this.shapeColor = parent.getColor();
		}
		
		public Color getColor() {
			return shapeColor;
		}
		
		public void resetShape() {
			x = startingShapeX;
			y = startingShapeY;
		}
		
		public Shape getLeft() {
			return left;
		}
		
		public Shape getRight() {
			return right;
		}
		
		public Shape getUp() {
			return up;
		}
		
		public Shape getDown() {
			return down;
		}
		
		public Shape getParent() {
			return parent;
		}
		
		public Shape newLeft() {
			left = new Shape(this);
			return left;
		}
		
		public Shape newRight() {
			right = new Shape(this);
			return right;
		}
		
		public Shape newUp() {
			up = new Shape(this);
			return up;
		}
		
		public Shape newDown() {
			down = new Shape(this);
			return down;
		}
		
		public void draw() {
				
				if(parent != null) {
					System.out.println("Can't call draw on leaf node");
					return;
				}
								
				blockMatrix[x][y].setOn(shapeColor);
				
				if(left != null)
					left.draw(x - 1, y);
				if(right != null)
					right.draw(x + 1, y);
				if(up != null)
					up.draw(x, y - 1);
				if(down != null)
					down.draw(x, y + 1);
		}
                
		public void draw(Block[][] matrix,int x, int y) {
				
				/*if(parent != null) {
					System.out.println("Can't call draw on leaf node");
					return;
				}*/
								
                                //System.out.println("Drawing to: X-"+x+" Y-"+y);
				matrix[x][y].setOn(shapeColor);
				
				if(left != null)
					left.draw(matrix, x - 1, y);
				if(right != null)
					right.draw(matrix, x + 1, y);
				if(up != null)
					up.draw(matrix, x, y - 1);
				if(down != null)
					down.draw(matrix, x, y + 1);
		}
		
		public void draw(int x, int y) {

				blockMatrix[x][y].setOn(shapeColor);

				if(left != null)
					left.draw(x - 1, y);
				if(right != null)
					right.draw(x + 1, y);
				if(up != null)
					up.draw(x, y - 1);
				if(down != null)
					down.draw(x, y + 1);
		}
		
		public String toString() {
			String r = new String();

			if(parent == null)
				r = new String(r + "This node is the Head.\n");
			else
				r = new String(r + "This node is not the Head\n");

			if(left == null)
				r = new String(r + "Left: empty\n");
			else
				r = new String(r + "Left: points\n");
			
			if(right == null)
				r = new String(r + "Right: empty\n");
			else
				r = new String(r + "Right: points\n");

			if(up == null)
				r = new String(r + "Up: empty\n");
			else
				r = new String(r + "Up: points\n");

			if(down == null)
				r = new String(r + "Down: empty\n");
			else
				r = new String(r + "Down: points\n");
				
			if(head != null)
				r = new String(r + "This node knows where its head is");
			else
				r = new String(r + "This node DOES NOT know where its head is");
			
			return r;
		}
		
		public Shape getHead() {
			return head;
		}
		
		public void clearShape(int x, int y) {
				
				blockMatrix[x][y].setOff();
				
				if(left != null)
					left.clearShape(x - 1, y);
				if(right != null)
					right.clearShape(x + 1, y);
				if(up != null)
					up.clearShape(x, y - 1);
				if(down != null)
					down.clearShape(x, y + 1);
		}
		
		public void moveDown() {
			if(parent != null) {
				System.out.println("Can't call move() on leaf node!");
				return;
			}
			
			move(DIRECTION_DOWN);
		}
		
		public void moveLeft() {
			if(parent != null) {
				System.out.println("Can't call move() on leaf node!");
				return;
			}
			
			move(DIRECTION_LEFT);
		}
		
		public void moveRight() {
			if(parent != null) {
				System.out.println("Can't call move() on leaf node!");
				return;
			}
			
			move(DIRECTION_RIGHT);
		}

		private void move(int direction) {
                        if(!running)
                                return;
			
                        drawPreview();
                        
			if(direction == DIRECTION_DOWN) {
				clearShape(x, y);
				if(assertMove(x, y + 1)) {
					y ++;
				} else {
					draw(x, y);
					hitBottom();
				}
			}
			
			if(direction == DIRECTION_LEFT) {
				clearShape(x, y);
				if(assertMove(x - 1, y)) {
					x --;
				}
			}
			
			if(direction == DIRECTION_RIGHT) {
				clearShape(x, y);
				if(assertMove(x + 1, y)) {
					x ++;
				}
			}
		}
                
                public void moveTo(int x, int y) {
                        clearShape(this.x, this.y);
                        if(assertMove(x,y)) {
                                this.x = x;
                                this.y = y;
                        } else {
                                draw(this.x, this.y);
                        }
                }
                
                
		public boolean assertMove(int x, int y) {
				
				if(y > (blockMatrix[0].length - 1))
					return false;
				
				if(x < 0 || (x > blockMatrix.length - 1))
					return false;
				
				if(!(blockMatrix[x][y].isOff()))
					return false;
				
				if(left != null)
					if(!(left.assertMove(x - 1, y)))
						return false;
                                
				if(right != null)
					if(!(right.assertMove(x + 1, y)))
						return false;
                                
				if(up != null)
					if(!(up.assertMove(x, y - 1)))
						return false;
                                
				if(down != null)
					if(!(down.assertMove(x, y + 1)))
						return false;
						
			return true;
		}
                
                public int getX() {
                        return x;
                }
                
                public int getY() {
                        return y;
                }
                
	}
	
	private class BlockCursor {
		public BlockCursor() {}
	}
	
	private class Block {
	
		private Dimension size;
		private boolean empty;
		private Color bgColor;
		private Color blockColor;
		private int width;
		private int height;
                private Color offColor = Color.white;
                private boolean glowing = false;
	
		public Block(Dimension size) {
			this.size = size;
			empty = true;
			//width = (int)size.getWidth();
			//height = (int)size.getHeight();
                        /**************
                         **************
                         *FIX THIS LATER
                         */
                        width = 10;
                        height = 10;
			Color blockColor;

			/**************************************
				GO BACK AND MAKE COLOR CONTROLABLE
			**************************************/
			
			bgColor = Color.white;
		}
		
		public void setOn(Color blockColor) {
			this.blockColor = blockColor;
			empty = false;
		}
                
                public void setOn() {
                        this.blockColor = Color.black;
                        empty = false;
                }
		
		public void setOff() {
			this.blockColor = Color.white; // = null;
			empty = true;
		}
                
		public void setOff(Color color) {
			this.blockColor = color; // = null;
                        offColor = color;
			empty = true;
		}
		
		public boolean isOff() {
			if(empty == true)
				return true;
			else
				return false;
		}
                
		public Dimension getSize() {
			return size;
		}
                
                public void setDefaultColor(Color color) {
                        offColor = color;
                }
		
		public int getwidth() {
			return (int)size.getWidth();
		}
		
		public int getHeight() {
			return (int)size.getHeight();
		}
                
                public Color getColor() {
                        return blockColor;
                }
		
		//Could be troublesome.
		//Go back and reexamine later in program life.
		
		public void draw(Graphics g) {
                    
                        Random rnd = new Random();
                    
			if(empty) {
				g.setColor(offColor);
				g.fillRect(0, 0, width, height);
			} else if(!glowing){
				g.setColor(blockColor);
				g.fillRect(0, 0, width - 1, height - 1);
				g.setColor(Color.black);
				g.drawRect(0, 0, width - 1, height - 1);
			} else {
                                g.setColor(
                                            new Color(
                                                rnd.nextInt(127) + 128,
                                                rnd.nextInt(127) + 128,
                                                rnd.nextInt(127) + 128,
                                                rnd.nextInt(127) + 128
                                        )
                                );
                                g.fillRect(0,0,width, height);
                        }
		}
                
                public void setGlowing(boolean set) {
                        if(set)
                                glowing = true;
                        else
                                glowing = false;
                }
                
                public void setColor(Color blockColor) {
                        this.blockColor = blockColor;
                }
                
	}
}