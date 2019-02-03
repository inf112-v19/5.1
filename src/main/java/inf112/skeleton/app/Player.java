package inf112.skeleton.app;

import java.util.ArrayList;

public class Player {

    private String name;
    private int x;
    private int y;
    private int lives;
    private int dmg;

    private Direction direction; //Which direction the player is currently facing.
    private ArrayList<ProgramCardCD> cardDeck; //All cards in the player hand.

    private ProgramCardCD[] registers = new ProgramCardCD[5];
    private int unlockedRegisters;

    /**
     * Initialize a new player (a new robot).
     * The player is facing south by default.
     *
     * @param name
     *            the name of the player.
     * @param x
     *          x start coordinate for the player.
     * @param y
     *          y start coordinate for the player.
     */
    public Player(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
        this.direction = Direction.SOUTH;
        this.cardDeck = new ArrayList<>();

        dmg = 0;
        lives = 3;
        unlockedRegisters = 5;
    }

    /**
     * Initialize a new player (a new robot).
     * Choose which direction the player is facing at spawn.
     *
     * @param name
     *              the name of the player
     * @param x
     *          x start coordinate for the player.
     * @param y
     *          y start coordinate for the player.
     * @param direction
     *                  which direction the player should face.
     */
    public Player(String name, int x, int y, Direction direction){
        this.name = name;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.cardDeck = new ArrayList<>();

        dmg = 0;
        lives = 3;
        unlockedRegisters = 5;
    }

    public void takeDamage(){
        dmg++;
        if(isDestroyed()){
            if(outOfLives()) {
                // TODO: GAME OVER for this player.
            } else {
//                repairDamage();
                // TODO: Move to last backup
            }
        } else if (dmg >= 5) {
            unlockedRegisters--;
        }
    }

    /* Registers are stored like this:
     * [0][1][2][3][4]
     *
     * taking 5 dmg locks register 4.
     * 6 dmg locks 3..
     */
    public boolean isLocked(int register){
        return register >= unlockedRegisters;
    }

    /**
     * When damage is taken some registers might become locked.
     * Cards in locked registers should remain untill the register is unlocked.
     *
     * @return list of cards that are not locked in registers
     */
    public ArrayList<ProgramCardCD> returnCards(){
        for(int register = 0; register < unlockedRegisters; register++) {
            cardDeck.add(registers[register]);
            registers[register] = null;
        }
        return cardDeck;
    }

    public boolean isDestroyed(){
        return dmg > 9;
    }


    public boolean outOfLives() {
        return lives == 0;
    }

    public void repairDamage(){
        dmg = 0;
        unlockedRegisters = 5;
    }

    /**
     * Give the player a new program card.
     * A player will not receive the card if the card limit is reached.
     */
    public void receiveNewCard(ProgramCardCD programCard){
        if(cardDeck.size() >= getCardLimit()){
            System.out.println("Card was not added because of card limit");
            return;
        }
        cardDeck.add(programCard);
    }

    public void pickCard(int cardPos){
        int i = 0;
        while(i < unlockedRegisters){
            if(registers[i] == null) {
                registers[i] = cardDeck.remove(cardPos);
                return;
            }
            i++;
        }
        System.err.println("Can't pick more cards. Register is full!");
    }

    public boolean registerIsFull() {
        return registers[unlockedRegisters] != null;
    }

    /**
     * Change the direction that the player is facing.
     *
     * Rotation is possible using one of the enums in Rotate.java.
     * Program cards should use this function, not the function changeDirection(Direction direction).
     *
     * @param rotate
     *              which direction the player should rotate
     */
    public void rotate(Rotate rotate){
        if(rotate.equals(Rotate.LEFT)){
            if(direction.equals(Direction.NORTH)){
                this.direction = Direction.WEST;
            }
            else if(direction.equals(Direction.WEST)){
                this.direction = Direction.SOUTH;
            }
            else if(direction.equals(Direction.SOUTH)){
                this.direction = Direction.EAST;
            }
            else if(direction.equals(Direction.EAST)){
                this.direction = Direction.NORTH;
            }
        }

        else if(rotate.equals(Rotate.RIGHT)){
            if(direction.equals(Direction.NORTH)){
                this.direction = Direction.EAST;
            }
            else if(direction.equals(Direction.EAST)){
                this.direction = Direction.SOUTH;
            }
            else if(direction.equals(Direction.SOUTH)){
                this.direction = Direction.WEST;
            }
            else if(direction.equals(Direction.WEST)){
                this.direction = Direction.NORTH;
            }
        }

        else if(rotate.equals(Rotate.UTURN)){
            if(direction.equals(Direction.NORTH)){
                this.direction = Direction.SOUTH;
            }
            else if(direction.equals(Direction.SOUTH)){
                this.direction = Direction.NORTH;
            }
            else if(direction.equals(Direction.WEST)){
                this.direction = Direction.EAST;
            }
            else if(direction.equals(Direction.EAST)){
                this.direction = Direction.WEST;
            }
        }
    }

    /**
     * Change the direction the player is facing.
     * Uses enums Direction.NORTH, Direction.WEST, Direction.SOUTH and Direction.EAST
     *
     * You should use rotate(Rotate rotate) if using program cards.
     *
     * @param direction
     *                  new direction to face.
     */
    //Might want to make private
    public void changeDirection(Direction direction){
        this.direction = direction;

        //TODO: update sprite to show the new direction
    }

    /**
     * Moves the robot forward in the direction it is facing.
     * @param steps
     *              how many tiles to move.
     */
    public void move(int steps){
        if(direction.equals(Direction.NORTH)) {
            y -= 150 * steps;
        }
        if(direction.equals(Direction.EAST)) {
            x += 150 * steps;
        }
        if(direction.equals(Direction.SOUTH)) {
            y += 150 * steps;
        }
        if(direction.equals(Direction.WEST)) {
            x -= 150 * steps;
        }
        //Texture/sprite is updated automatically because of render method in GameScreen.java.
    }

    // getters and setters:

    /**
     *
     * @return
     *          how many cards the player is allowed to be dealt
     */
    public int getCardLimit(){
        return 9 - dmg;
    }

    public int getUnlockedRegisters() {
        return this.unlockedRegisters;
    }

    /**
     *
     * @return
     *          all the cards that the player currently has in hand.
     */
    public ArrayList<ProgramCardCD> getCardDeck(){
        return this.cardDeck;
    }


    /**
     *
     * @return
     *          the direction the player is facing.
     */
    public Direction getDirection(){
        return this.direction;
    }

    public int getDamage(){
        return dmg;
    }

    public int getLives() {
        return this.lives;
    }

    public ArrayList<ProgramCardCD> getRegisters(){
        ArrayList<ProgramCardCD> list = new ArrayList<>();
        for(ProgramCardCD pc : registers)
            list.add(pc);

        return list;
    }

    public ProgramCardCD getCardInRegister(int register){
        return registers[register];
    }

    /**
     * Every phase the player with the highest priority goes first.
     * @param phaseNumber
     * @return priority of the card in the register for the given phase
     */
    public int getPriority(int phaseNumber) {
        return registers[phaseNumber].getPriority();
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString(){
        return getName() +  " | Health: " + (10 - dmg) + " | Lives: " + lives;
    }
}
