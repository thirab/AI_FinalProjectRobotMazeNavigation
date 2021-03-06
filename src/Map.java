import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import lejos.robotics.navigation.DifferentialPilot;


public class Map {
	private boolean goalReached=false;
	private int mapWidth = 9;
	private int mapHeight = 7;
	private Cell[][] theMap;
	private char direction = 'n';
	
	//set robot in middle of possible map
	private int xStart = 1;
	private int yStart = 1;
	private int xGoal;
	private int yGoal;
	private int x = xStart;
	private int y = yStart;
	private boolean possible = true;
	private DifferentialPilot robot;
	private double cellDistance;
	private ArrayList<Cell> path;
	

	/**
	 * Map creates a theMap of Cell objects which the robot uses to navigate.
	 * @param c 
	 */
	public Map(DifferentialPilot p, double c){
		
		//set global variables
		robot = p;
		cellDistance=c;
		
		//create a new map made out of cells
		for(int i=0; i<mapWidth;i++){
			for(int j=0; j<mapHeight; j++){
				theMap[i][j] = new Cell(i,j);				
			}
			//set the outer cells to barriers
			theMap[i][0].setObstacle();
			theMap[i][1].move();
			theMap[i][mapHeight].setObstacle();
			theMap[i][mapHeight-1].move();
		}
		//set outer cells to barriers
		for(int j=0; j<mapHeight;j++){
			theMap[0][j].setObstacle();
			theMap[1][j].move();
			theMap[mapWidth][j].setObstacle();
			theMap[mapWidth-1][j].move();
		}
		theMap[xStart][yStart].visit();
		path=new ArrayList<Cell>();
		path.add(getCurrentCell());
	}
	/**
	 * impossible sets the map possibility to be solved to false, and set's the cell options to 0
	 */
	public void impossible(){
		possible=false;
		getCurrentCell().setOptions(0);
		robot.stop();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		//exit the program
		System.exit(0);
	}
	/**
	 * isPossible returns wether or not the map is known to be possibly solveable
	 * @return boolean result
	 */
	public boolean isPossible(){
		return possible;
	}
	/**
	 * adjusts the navigator position right
	 */
	public void turnRight(){
		if(direction == 'n'){
			direction ='e';
		}else if(direction == 'e'){
			direction ='s';
		}else if(direction == 's'){
			direction ='w';
		}else if(direction == 'w'){
			direction ='n';
		}
	}
	/**
	 * adjusts the navigator position left
	 */
	public void turnLeft(){
		if(direction == 'n'){
			direction ='w';
		}else if(direction == 'e'){
			direction ='n';
		}else if(direction == 's'){
			direction ='e';
		}else if(direction == 'w'){
			direction ='s';
		}
	
	}
	/**
	 * adjusts the navigator position forward
	 */
	public void forward(){	
		if(direction == 'n'){
			y++;
		}else if(direction == 'e'){
			x++;
		}else if(direction == 's'){
			y--;
		}else if(direction == 'w'){
			x--;
		}
		
		//TODO debug this, this may not be accurate. The idea is that cells that have been visited should have 1 less move option avaliable
		Cell current = getCurrentCell();
		buildPath(current);
		if(current.visited()){
			current.move();
		}else{
			current.visit();
		}
	}
	
	
	//TODO unsure if this satisfies the assignment it should yield the most effective path to return to the orign. 
	//TODO it doesn't search to create a new path. The only search occurs in forwards navigation. Finding the backwards path
	//TODO is done as the robot navigates through the map. The path is minimized to the most effective possible path assuming
	//that the robot attempted to take the most navigable route and that further path finding would be more costly than simply retracing
	// previously taken steps. 
	/**
	 * buildPath checks if a current cell exists and removes all cells after it 
	 * as the navigator has returned to a previously opened position on the path.
	 * If the navigator has adjacent cells that can cut the path down, do so.
	 * @param c the current cell to add
	 */
	public void buildPath(Cell c){
		
		//if the cell is already within the list remove all cells that come after it.
		int location = path.indexOf(c);
		if(location != -1){
			//TODO this must be debugged not sure if the math is right.
			for(int i =0; i<(path.size()-location); i++){
				path.remove(path.size());
			}
		}else{
			
			// if the cell is not already in the list check it's four adjacent cells. 
			//If any exist in the list take the lowest index, and remove all cells after it.
			//the idea is to join adjacent cells that would otherwise allow the path to loop around.
			
			Cell north = getNorthCell();
			Cell east = getEastCell();
			Cell west = getWestCell();
			Cell south = getSouthCell();
			
			int n = path.indexOf(north);
			int e = path.indexOf(east);
			int w = path.indexOf(west);
			int s = path.indexOf(south);
			int lowestIndex = path.size();
			
			//check if any of the following cells are contained within the path
			if(n!=-1){
				lowestIndex=n;
			}
			if(e!=-1 && e<lowestIndex){
				lowestIndex=e;
			}
			if(s!=-1&& s<lowestIndex){
				lowestIndex=s;
			}
			if(w!=-1&& w<lowestIndex){
				lowestIndex=w;
			}
			
			//take the lowest contained cell and remove all cells in the path after it.
			for(int i =0; i<(path.size()-lowestIndex); i++){
				path.remove(path.size());
			}
			
			//add the current cell to the path
			path.add(c);
		}	
	}
	/**
	 * adjusts the location of the navigator backward
	 */
	public void backward(){
		if(direction == 'n'){
			y--;
		}else if(direction == 'e'){
			x--;
		}else if(direction == 's'){
			y++;
		}else if(direction == 'w'){
			x++;
		}else{
			System.out.println("error there is no direction");
		}
	}
	/**
	 * getDirection returns the direction the navigator is facing
	 * @return char representation of the direction ( n,s,e,w)
	 */
	public char getDirection(){
		return direction;
	}
	/**
	 * gets the cell the navigator is currently on
	 * @return the cell
	 */
	public Cell getCurrentCell(){
		return theMap[x][y];
	}
	/**
	 * gets the West direction cell
	 * @return the cell
	 */
	public Cell getWestCell(){
		return theMap[x-1][y];
	}
	/**
	 * gets the East direction cell
	 * @return the cell
	 */
	public Cell getEastCell(){
		return theMap[x+1][y];
	}
	/**
	 * gets the North direction cell
	 * @return the cell
	 */
	public Cell getNorthCell(){
		return theMap[x][y+1];
	}
	/**
	 * gets the South direction cell
	 * @return the cell
	 */
	public Cell getSouthCell(){
		return theMap[x][y-1];
	}
	
	/**
	 * obstacleFound is called when an obstacle has been found infront of the robot. 
	 * obstacleFound locates the obstacle and handles it. 
	 */
	public void obstacleFound(){
		if(direction == 'n'){
			handleObstacleFound(x,y+1);
		}else if(direction == 'e'){
			handleObstacleFound(x+1,y);
		}else if(direction == 's'){
			handleObstacleFound(x,y-1);
		}else if(direction == 'w'){
			handleObstacleFound(x-1,y);
		}
	}
	
	/**
	 * rotateToBestDirection changes the navigators direction to face the optimal best cell
	 */
	public void rotateToBestDirection(){
		Cell best = getBest();
		Cell current = getCurrentCell();
		if(best.getX() > current.getX()){
			faceEast();
		}else if(best.getX() < current.getX()){
			faceWest();
		}else if(best.getY() > current.getY()){
			faceNorth();
		}else if(best.getY() < current.getY()){
			faceSouth();
		}
	}
	
	/**
	 * handleObstacleFound at x,y co-ordinates is called when a obstacle is sensed so that other cells adjacent can also be updated. 
	 * @param xC co-ordinates
	 * @param yC co-ordinates
	 */
	public void handleObstacleFound(int xC, int yC){
		theMap[xC][yC].setObstacle();
		theMap[xC-1][yC].move();
		theMap[xC+1][yC].move();
		theMap[xC][yC-1].move();
		theMap[xC][yC+1].move();	
	}
	
	/**
	 * rightIsValid checks if it is valid to move right
	 * @return boolean result
	 */
	public boolean rightIsValid(){
		boolean valid = false;
		turnRight();
		valid = forwardIsValid();
		turnLeft();
		return valid;
	}
	
	/**
	 * leftIsValid checks if it is valid to move left
	 * @return boolean result
	 */
	public boolean leftIsValid(){
		boolean valid = false;
		turnLeft();
		valid = forwardIsValid();
		turnRight();
		return valid;
	}
	
	/**
	 * backIsValid checks if it is valid to move backwards
	 * @return boolean result
	 */
	public boolean backIsValid(){
		boolean valid = false;
		turnLeft();
		turnLeft();
		valid = forwardIsValid();
		turnRight();
		turnRight();
		return valid;
	}
	
	/**
	 * forwardIsValid checks if the cell infront of the robot is valid
	 * @return boolean result
	 */
	public boolean forwardIsValid(){
		if(direction == 'n' && isValidCell(x,y+1)){
			return true;
		}else if(direction == 'e' && isValidCell(x+1,y)){	
			return true;
		}else if(direction == 's' && isValidCell(x,y-1)){	
			return true;
		}else if(direction == 'w' && isValidCell(x-1,y)){
			return true;
		}
			// if the cell infront of the robot is invalid (it is either an obstacle or it has no valid moves)
			return false;
	}
	
	/**
	 * isValid tests to see if a cell is an obstacle or has valid moves.
	 * @param xC x co-ordinate
	 * @param yC y co-ordinate
	 * @return
	 */
	public boolean isValidCell(int xC, int yC){
		if(theMap[xC][yC].isObstacle() || theMap[xC][yC].optionsAvaliable() == 0){
			return false;
		}
		return true;
	}
	/**
	 * goal returns wether or not the goal (white cell) has been reached
	 * @return boolean result
	 */
	public boolean goal(){
		return goalReached;
	}
	/**
	 * wall Sets all cells infront of the robot to being obstacles (ex's, off the board, or wrong direction cases)
	 * This is used when a loud sound tells the robot it is going in the wrong direction
	 */
	public void wall(){
		
		//TODO check logic
		if(direction == 'n'){
			for(int i=0; i<mapWidth;i++){
				theMap[i][y+1].setObstacle();
			}
		}else if(direction == 'e'){
			for(int i=0; i<mapHeight;i++){
				theMap[x+1][i].setObstacle();
			}
		}else if(direction == 's'){
			for(int i=0; i<mapWidth;i++){
				theMap[i][y-1].setObstacle();
			}
		}else if(direction == 'w'){
			for(int i=0; i<mapHeight;i++){
				theMap[x-1][i].setObstacle();
			}
		}else{
			System.out.println("error there is no direction");
		}
	}
	
	/**
	 * getBest returns the adjacent cell with the highest number of move options avaliable
	 * @return the best cell
	 */
	public Cell getBest() {
		Cell best = getNorthCell();
		int value = best.optionsAvaliable();
		if (getEastCell().optionsAvaliable() > value) {
			best = getEastCell();
		} else if (getWestCell().optionsAvaliable() > value) {
			best = getWestCell();
		} else if (getSouthCell().optionsAvaliable() > value) {
			best = getSouthCell();
		}
		return best;
	}
	

	/**
	 * faceWest directs the navigator to face west and adjusts the map
	 */
	public void faceWest() {
		if (getDirection() == 'n') {
			turnLeft();
			robot.rotate(-90);
		} else if (getDirection() == 'e') {
			turnRight();
			robot.rotate(90);
			turnRight();
			robot.rotate(90);

		} else if (getDirection() == 's') {
			turnRight();
			robot.rotate(90);
		}
	}
	/**
	 * faceEast directs the navigator to face east and adjusts the map
	 */
	public void faceEast() {
		if (getDirection() == 'n') {
			turnRight();
			robot.rotate(90);

		} else if (getDirection() == 's') {
			turnLeft();
			robot.rotate(-90);
		} else if (getDirection() == 'w') {
			turnRight();
			robot.rotate(90);
			turnRight();
			robot.rotate(90);
		}
	}

	/**
	 * faceNorth directs the navigator to face north and adjusts the map
	 */
	public void faceNorth() {
		if (getDirection() == 'w') {
			turnRight();
			robot.rotate(90);
		} else if (getDirection() == 'e') {
			turnLeft();
			robot.rotate(-90);
		} else if (getDirection() == 's') {
			turnRight();
			robot.rotate(90);
			turnRight();
			robot.rotate(90);
		}
	}

	/**
	 * faceSouth directs the navigator to face south and adjusts the map
	 */
	public void faceSouth() {
		if (getDirection() == 'n') {
			turnRight();
			robot.rotate(90);
			turnRight();
			robot.rotate(90);
		} else if (getDirection() == 'e') {
			turnRight();
			robot.rotate(90);
		} else if (getDirection() == 'w') {
			turnLeft();
			robot.rotate(-90);
		}
	}
	
	/**
	 * MazeWon occurs when a white cell has been found.
	 * This begins the navigators path back to the start cell.
	 */
	public void MazeWon() {
		goalReached=true;	
		stop();
		System.out.println("I'm playing the win song!");
		//lejos.nxt.Sound.beepSequenceUp();
		//lejos.nxt.Sound.beepSequenceUp();
		//lejos.nxt.Sound.beepSequenceUp();
		xGoal=x;
		yGoal=y;
		moveBack();
	}
	
	/**
	 * When a sound is heard by the robot, create a wall in the direction the robot was heading.
	 */
	public void SoundHeard(){
		System.out.println("Shut up guys! You're being loud");
		stop();
		lejos.nxt.Sound.beepSequenceUp();
		wall();
		rotateToBestDirection();
	}
	
	/**
	 * moveBack moves along the path, and navigates back to the start cell
	 */
	public void moveBack(){
		x=xGoal;
		y=yGoal;
		
		if(path == null){
			System.out.println("There is no path left");
		}else{
			while(path.size()!=1){
		
			Cell current = (Cell) path.remove(path.size());
			Cell future = (Cell) path.get(path.size());
			if(current.getX()>future.getX()){
				faceWest();
				forward();
				robot.travel(cellDistance);
			}else if(current.getX()<future.getX()){
				faceEast();
				forward();
				robot.travel(cellDistance);
			}else if(current.getY() > future.getX()){
				faceSouth();
				forward();
				robot.travel(cellDistance);
			}else  if(current.getY() < future.getX()){
				faceNorth();
				forward();
				robot.travel(cellDistance);
			}
		}
			stop();
			System.out.println("Maze solved");
		}
		
	}

	/**
	 * instructs the navigator to wander within the map
	 */
	public void wander() {
		if(forwardIsValid()){
			forward();
			robot.travel(cellDistance);
		}else{
			rotateToBestDirection();
		}
	}

	/**
	 * instructs the navigator to stop
	 */
	public void stop() {
		robot.stop();
	}
	
	

}
