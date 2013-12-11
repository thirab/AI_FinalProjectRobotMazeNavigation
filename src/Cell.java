
/**
 * 
 * @author tai-lanhirabayashi & Jackie
 *
 */
public class Cell {
	private int x;
	private int y;
	private boolean barrier = false;
	private boolean visited = false;
	private int options = 4;
	private boolean path= false;
	private boolean checked = false;
	
	/**
	 * Cell creates a new cell value with x,y cordinates.
	 * @param xCord the x co-ordinate of the cell
	 * @param yCord the y co-ordinate of the cell
	 */
	public Cell(int xCord, int yCord){
		x=xCord;
		y=yCord;
	}
	
	/**
	 * Set's the cell to being on the robots path
	 */
	public void onPath(){
		path = true;
	}
	
	/**
	 * Set's the robot to no longer being on the path
	 */
	public void offPath(){
		path=false;
	}
	
	
	/**
	 * getX returns the x value of the cell
	 * @return the integer x value
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * getY returns the y value of the cell
	 * @return the integer y value
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * setOptions allows the map to manually set the number of options a cell has
	 * @param o
	 */
	public void setOptions(int o){
		options = o;
	}
	
	/**
	 * wasChecked returns the boolean answer to wether or not the cell has been checked to be an obstacle
	 * @return boolean if the cell is an obstacle
	 */
	public boolean wasChecked(){
		return checked;
	}
	
	/**
	 * Set the cell to having been checked
	 */
	public void check(){
		checked = true;
	}
	/**
	 * visit set's the cell to having been visited. 
	 */
	public void visit(){
		visited=true;
	}
	
	/**
	 * move means that there is 1 fewer direction option of the robot. 
	 */
	public void move(){
		options--;
	}

	/**
	 * setObstacle set's the cell to contain an obstacle
	 */
	public void setObstacle(){
		options=0;
		barrier=true;
	}
	/**
	 * isObstacle gets the cell's obstacle status: true it is known to contain an obstacle, false it does not.
	 * @return boolean is the cell an obstacle cell
	 */
	public boolean isObstacle() {
		return barrier;
	}
	/**
	 * visited returns the boolean variable visited.
	 * It describes wether or not the cell has been visited by the robot.
	 * @return boolean if the cell has been visited before
	 */
	public boolean visited(){
		return visited;
	}
	/**
	 * optionsAvaliable returns the options variable for directions the robot can move
	 * @return the number of valid moves avaliable
	 */
	public int optionsAvaliable(){
		if(barrier){
			return 0;
		}
		return options;
	}

}
