import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import lejos.robotics.navigation.DifferentialPilot;

/**
 * 
 * @author tai-lanhirabayashi & Jackie
 *
 */
public class Map {
	private boolean goalReached=false;
	private int mapWidth = 10;
	private int mapHeight = 7;
	private Cell[][] theMap = new Cell[mapWidth][mapWidth];
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
	private Stack<Cell> path;	
	private boolean isMoving = false;
	

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
			
			//no preset walls
			theMap[i][mapHeight-1].setObstacle();
			theMap[i][mapHeight-2].move();
		}
		//set outer cells to barriers
		for(int j=0; j<mapHeight;j++){
			theMap[0][j].setObstacle();
			theMap[1][j].move();
			theMap[mapWidth-1][j].setObstacle();
			theMap[mapWidth-2][j].move();
		}
<<<<<<< HEAD
		theMap[xStart][yStart].visit(); //would we need to set options here? what if there were two directions from start cell
		path=new ArrayList<Cell>();
		path.add(getCurrentCell());
=======
		theMap[xStart][yStart].visit();
		path=new Stack<Cell>();
		path.push(getCurrentCell());
>>>>>>> e0adf0a9abc1e16640325cc0b75731facbe83c4b
	}
	
	/**
	 * instructs the navigator to wander within the map
	 */
<<<<<<< HEAD
	public void impossible(){
		possible=false;
		getCurrentCell().setOptions(0);
		robot.stop();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		//exit the program
		System.exit(0);
=======
	public void wander() {
		if(getBest()!=getForward()){
			rotateToBestDirection();
		}else{
			robot.travel(cellDistance);
			forward();
		}
		//System.out.println("Facing: " + direction);
		
	}

	private Cell getForward() {
		if(direction == 'n'){
			return theMap[x][y+1];
		}else if(direction == 'e'){
			return theMap[x+1][y];
		}else if(direction == 's'){
			return theMap[x][y-1];
		}
			return theMap[x-1][y];
<<<<<<< HEAD
		
>>>>>>> 69fb029410704742586999c92ef238ee3136cc3a
=======
>>>>>>> e0adf0a9abc1e16640325cc0b75731facbe83c4b
	}

	/**
	 * rotateToBestDirection changes the navigators direction to face the optimal best cell
	 */
	public void rotateToBestDirection(){
		//System.out.println("Finding the best");
		Cell best = getBest();
		Cell current = getCurrentCell();
		if(best!= null){
		if(best.getX() > current.getX()){
			System.out.println("go east");
			faceEast();
		}else if(best.getX() < current.getX()){
			System.out.println("go west");
			faceWest();
		}else if(best.getY() > current.getY()){
			System.out.println("Go north");
			faceNorth();
		}else{
			System.out.println("go south");
			faceSouth();
			}
		}else{
			System.out.println("There are no options");
			//impossible();
		}
	}
	
	/**
	 * getBest returns the adjacent cell with the highest number of move options avaliable
	 * @return the best cell
	 */
	public Cell getBest() {
		Cell best = getNorthCell();
		Cell east = getEastCell();
		Cell south = getSouthCell();
		Cell west = getWestCell();
		if(best == null){
			System.out.println("there is no north cell");
			best = east;
		}
		if(best == null){
			System.out.println("there is no east cell");
			best = south;
		}
		if(best == null){
			System.out.println("there is no south cell");
			best = west;
		}
		if(best != null){
			int value = best.optionsAvaliable();
			if (east!= null && east.optionsAvaliable() > value) {
				best = east;
				value=best.optionsAvaliable();
			}
			if (west != null && west.optionsAvaliable() > value) {
				best =west;
				value=best.optionsAvaliable();
			}
			if (south != null && south.optionsAvaliable() > value) {
				best = south;
				value=best.optionsAvaliable();
			}
			return best;
		}
		System.out.println("theres something wrong, there are no moves");
		return null;
	}

	/**
	 * adjusts the navigator position right
	 */
	public void turnRight(){
		System.out.println("turn right");
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
		System.out.println("Turn left");
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
		getCurrentCell().move();
		System.out.println("I was at"+ " " + x + " , "+ y);
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
		//move options depend on barriers, so cells shouldnt automatically start with 4. will fix
		//
		Cell current = getCurrentCell();
		System.out.println("going to" + x + " : "+ y);
				//"direction " + direction);
		buildPath(current);
//		if(current.visited()){
//			current.move();
//		}else{
//			current.visit();
//		}
		current.visit();
	}
	
	public void returnHome(){
		getCurrentCell().move();
		System.out.println("I was at"+ " " + x + " , "+ y);
		if(direction == 'n'){
			y++;
		}else if(direction == 'e'){
			x++;
		}else if(direction == 's'){
			y--;
		}else if(direction == 'w'){
			x--;
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
<<<<<<< HEAD
		
		//if the cell is already within the list remove all cells that come after it.
		int location = path.indexOf(c); 	
		if(location != -1){ //why are you removing all the cells after it, shouldn't you remove each one as you get to it?
			//in case one of the following cells has another option
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
=======
//		
//		//if the cell is already within the list remove all cells that come after it.
//		int location = path.indexOf(c);
//		if(location != -1){
//			System.out.println("Ive been here before");
//			//TODO this must be debugged not sure if the math is right.
//			for(int i =0; i<(path.size()-location); i++){
//				path.remove(path.size());
//			}
//		}else{
//			
//			// if the cell is not already in the list check it's four adjacent cells. 
//			//If any exist in the list take the lowest index, and remove all cells after it.
//			//the idea is to join adjacent cells that would otherwise allow the path to loop around.
//			
//			Cell north = getNorthCell();
//			Cell east = getEastCell();
//			Cell west = getWestCell();
//			Cell south = getSouthCell();
//			
//			int n = path.indexOf(north);
//			int e = path.indexOf(east);
//			int w = path.indexOf(west);
//			int s = path.indexOf(south);
//			int lowestIndex = path.size();
//			
//			//check if any of the following cells are contained within the path
//			if(n!=-1){
//				lowestIndex=n;
//			}
//			if(e!=-1 && e<lowestIndex){
//				lowestIndex=e;
//			}
//			if(s!=-1&& s<lowestIndex){
//				lowestIndex=s;
//			}
//			if(w!=-1&& w<lowestIndex){
//				lowestIndex=w;
//			}
//			
//			if(lowestIndex != path.size()-1 ){
//				//take the lowest contained cell and remove all cells in the path after it.
//				for(int i =0; i<(path.size()-lowestIndex); i++){
//					path.remove(path.size());
//				}
//			}
//			
//			//add the current cell to the path
<<<<<<< HEAD
>>>>>>> 69fb029410704742586999c92ef238ee3136cc3a
			path.add(c);
=======
			path.push(c);
>>>>>>> e0adf0a9abc1e16640325cc0b75731facbe83c4b

//		}	
//		System.out.println("Path size: " + path.size());
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
		if(x>0){
		return theMap[x-1][y];
		}
		return null;
	}
	/**
	 * gets the East direction cell
	 * @return the cell
	 */
	public Cell getEastCell(){
		if(x<mapWidth-1){
		return theMap[x+1][y];
		}
		return null;
	}
	/**
	 * gets the North direction cell
	 * @return the cell
	 */
	public Cell getNorthCell(){
		if(y<mapHeight-1){
		return theMap[x][y+1];
		}
		return null;
	}
	/**
	 * gets the South direction cell
	 * @return the cell
	 */
	public Cell getSouthCell(){
		if(y>0){
		return theMap[x][y-1];
		}
		return null;
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
		if(getCurrentCell().optionsAvaliable()>0){
			robot.travel(-2);
			rotateToBestDirection();
		}else{
			impossible();
		}
	}
	
	
	/**
	 * handleObstacleFound at x,y co-ordinates is called when a obstacle is sensed so that other cells adjacent can also be updated. 
	 * @param xC co-ordinates
	 * @param yC co-ordinates
	 */
	public void handleObstacleFound(int xC, int yC){
		System.out.println("Obstalce at" + " " + xC + "," + yC);
		if(!theMap[xC][yC].isObstacle()){
		theMap[xC][yC].setObstacle();
			if(xC >0){
				theMap[xC-1][yC].move();
			}
			if(xC<mapWidth -1){
				theMap[xC+1][yC].move();
			}
			if(yC>0){
				theMap[xC][yC-1].move();
			}
			if(yC<mapHeight-1){
				theMap[xC][yC+1].move();
			}
		}
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
	
//	public boolean rightChecked(){
//		if(direction == 'n'){
//			return theMap[x][y].wasChecked();
//		}else if(direction == 'e'){
//			
//		}else if(direction == 's'){
//			
//		}else{
//			
//		}
//	}
//	
//	public boolean backChecked(){
//		
//	}
//	
//	public boolean leftChecked(){
//		
//	}
	
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
		//if the cell exists
		if(xC<mapWidth-1 && xC>=0 && yC<mapHeight-1 && yC>=0){
			
			//check if it can be moved into (is it an obstacle, does it have options)
		if(theMap[xC][yC].isObstacle() || theMap[xC][yC].optionsAvaliable() == 0){
			return false;
		}
		return true;
		}
		return false;
	}
	/**
	 * goal returns wether or not the goal (white cell) has been reached
	 * @return boolean result
	 */
	public boolean goal(){
		return goalReached;
	}
<<<<<<< HEAD
	/**
	 * wall Sets all cells infront of the robot to being obstacles (ex's, off the board, or wrong direction cases)
	 * This is used when a loud sound tells the robot it is going in the wrong direction
	 */
	public void wall(){
		
		//TODO check logic -- why would you do this, as it could obliterate the maze functionality and make it impossible
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
	
=======
>>>>>>> 69fb029410704742586999c92ef238ee3136cc3a

	/**
	 * faceWest directs the navigator to face west and adjusts the map
	 */
	public void faceWest() {
		System.out.println("Face west");
		if (getDirection() == 'n') {
			turnLeft();
			robot.rotate(90);
		} else if (getDirection() == 'e') {
			turnRight();
			robot.rotate(-90);
			turnRight();
			robot.rotate(-90);

		} else if (getDirection() == 's') {
			turnRight();
			robot.rotate(-90);
		}
	}
	/**
	 * faceEast directs the navigator to face east and adjusts the map
	 */
	public void faceEast() {
		System.out.println("Face east");
		if (getDirection() == 'n') {
			turnRight();
			robot.rotate(-90);

		} else if (getDirection() == 's') {
			turnLeft();
			robot.rotate(90);
		} else if (getDirection() == 'w') {
			turnRight();
			robot.rotate(-90);
			turnRight();
			robot.rotate(-90);
		}
	}

	/**
	 * faceNorth directs the navigator to face north and adjusts the map
	 */
	public void faceNorth() {
		System.out.println("Face north");
		if (getDirection() == 'w') {
			turnRight();
			robot.rotate(-90);
		} else if (getDirection() == 'e') {
			turnLeft();
			robot.rotate(90);
		} else if (getDirection() == 's') {
			turnRight();
			robot.rotate(-90);
			turnRight();
			robot.rotate(-90);
		}
	}

	/**
	 * faceSouth directs the navigator to face south and adjusts the map
	 */
	public void faceSouth() {
		System.out.println("Face south");
		if (getDirection() == 'n') {
			turnRight();
			robot.rotate(-90);
			turnRight();
			robot.rotate(-90);
		} else if (getDirection() == 'e') {
			turnRight();
			robot.rotate(-90);
		} else if (getDirection() == 'w') {
			turnLeft();
			robot.rotate(90);
		}
	}
	
	/**
	 * MazeWon occurs when a white cell has been found.
	 * This begins the navigators path back to the start cell.
	 */
	public void mazeWon() {
		goalReached=true;	
		System.out.println("I'm playing the win song!");
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		lejos.nxt.Sound.beepSequenceUp();
		xGoal=x;
		yGoal=y;
//		
//		//TODO currently erroring out
//		//moveBack();
//		stop();
	}
	
	/**
	 * When a sound is heard by the robot, create a wall in the direction the robot was heading.
	 */
	public void SoundHeard(){
		System.out.println("Shut up guys! You're being loud");
		lejos.nxt.Sound.beepSequenceUp();
		//PUT SOME TURN HERE. LIKE BELOW YAH thats probably fine
		//or maybe just a manual right or something
		robot.rotate(-90);
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
			while(path.size()>1){
			System.out.println("About to get the current cell");
			Cell current = (Cell) path.pop();
			System.out.println("My x current " + " " + current.getX() + " " + "my y" + " " + current.getY() );
			Cell future = (Cell) path.peek();
			System.out.println("My x future " + " " + future.getX() + " " + "my y" + " " + future.getY());
			if(current.getX()>future.getX()){
				faceWest();
				returnHome();
				robot.travel(cellDistance);
			}else if(current.getX()<future.getX()){
				faceEast();
				returnHome();
				robot.travel(cellDistance);
			}else if(current.getY() > future.getY()){
				faceSouth();
				returnHome();
				robot.travel(cellDistance);
			}else  if(current.getY() < future.getY()){
				faceNorth();
				returnHome();
				robot.travel(cellDistance);
			}
		}
			//stop();
			System.out.println("Maze solved");
		}
		
	}
	
	/**
	 * impossible sets the map possibility to be solved to false, and set's the cell options to 0
	 */
<<<<<<< HEAD
	public void wander() {
		if(forwardIsValid()){
			forward();
			robot.travel(cellDistance);
			//robot.forward();
		}else{
			rotateToBestDirection();
=======
	public void impossible(){
		System.out.println("This is impossible I quit!");
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
	 * checks if the robot is on the start cell. 
	 * @return boolean wether the robot is or is not on the start cell.
	 */
	public boolean isOnStart(){
		if(getCurrentCell().getX() == xStart && getCurrentCell().getY() == yStart){
			return true;
>>>>>>> 69fb029410704742586999c92ef238ee3136cc3a
		}
		return false;
		
	}
	
	/**
	 * instructs the navigator to stop
	 */
	public void stop() {
		robot.stop();
	}
	
	public void moving(){
		isMoving=true;
	}
	
	public void stopMoving(){
		isMoving=false;
	}
	
	public boolean checkMoving(){
		return isMoving;
	}


}
