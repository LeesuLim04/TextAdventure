public class Game {

    private Room currentRoom;
    Room Valley;
    Room ForestPath;
    Room Forest;
    Room Cemetery;
    boolean finished;
    boolean wantToQuit = false;
    Boolean bright = false;
    Boolean threw = false;
    Boolean died = false;
    private Parser parser;
    private Player player;


    public Game() {
        parser = new Parser();
        player = new Player();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.createRooms();
        game.play();
    }

    private void createRooms() {
        Room House = new Room("The wooden house is surrounded by a metal fence.", "The house has two floors, and the fence is opened. The first floor has a living room, and it has a sword and hammer. The second floor has a baby's room and a bathroom. To the south of this house, there is a valley.");
        Room BabyRoom = new Room("The baby's room is on the second floor of the house.", "The baby's room has a candle on the desk.");
        Room LivingRoom = new Room("The living room is on the first floor of the house.", "The living room has a sword, a hammer, and a luggage on the floor. You can smash the luggage with the hammer to get a new item.");
        Valley = new Room("The valley has a lake between two giant mountains.", "There is a mysterious tribe that kills anyone with unique appearance. Also, there is a small tree with a nest of birds. The nest has a locked wooden box that needs a key to open. You will need to kill the bird to get the key. To the east, there is a forest path and you need to have any sort of lights to go to there.");
        ForestPath = new Room("This is a forest path toward a forest to the east and a cemetery to the south.", "There are a a lot of trees and rocks along the path. To the south, there is a cemetery with an opened gate. To the east, there is a forest but you need to distract the tigers. To distract them, you should throw the rock to go there.");
        Forest = new Room("This forest has a tremendous amount of trees, and it is surrounded by giant mountains that look impenetrable.", "There aren't any humans, but there are some big animals, including tigers. They will attack anything that makes noises. You can throw rocks to avoid the animals. In the road to the cemetery, there are some spirits and you should not touch anything.");
        Cemetery = new Room("The cemetery is surrounded by the fence, but the gate is opened.", "The cemetery has some spirits from the grave. They will be mad and kill you if you touch any area of the cemetery. If you did not touch anything, the spirits will give you the passcode of the mystery door. In the middle of the cemetery, there is a weird door with a door lock which is connected to the heaven.");

        House.setExit("south", Valley);
        House.setExit("in", LivingRoom);
        LivingRoom.setExit("out", House);
        LivingRoom.setExit("up", BabyRoom);
        BabyRoom.setExit("down", LivingRoom);
        Valley.setExit("east", ForestPath);
        Valley.setExit("north", House);
        ForestPath.setExit("south", Cemetery);
        ForestPath.setExit("east", Forest);
        ForestPath.setExit("west", Valley);
        Forest.setExit("west", ForestPath);
        Cemetery.setExit("north", ForestPath);

        Item sword = new Item();
        Item hammer = new Item();
        Item mask = new Item();
        Item key = new Item();
        Item candle = new Item();
        Item woodenbox = new Item();
        Item lighter = new Item();
        Item rock = new Item();
        Item luggage = new Item();
        Item ghillieSuit = new Item();


        LivingRoom.setItem("sword", sword);
        LivingRoom.setItem("hammer", hammer);
        LivingRoom.setItem("luggage", luggage);
        BabyRoom.setItem("candle", candle);
        Valley.setItem("woodenbox", woodenbox);
        ForestPath.setItem("rock", rock);

        currentRoom = House;
    }

    public void play() {
        printWelcome();
        finished = false;
        while(!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thanks for playing!");
    }


    private boolean processCommand(Command command) {
        wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch(commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;

            case LOOK:
                look(command);
                break;

            case DROP:
                drop(command);
                break;

            case GRAB:
                grab(command);
                break;

            case OPEN:
                open(command);
                break;

            case SMASH:
                smash(command);
                break;

            case WEAR:
                wear(command);
                break;

            case LIGHT:
                light(command);
                break;

            case THROW:
                throw1(command);
                break;

            case TOUCH:
                touch(command);
                break;

            case KILL:
                kill(command);
                break;

        }

        return wantToQuit;
    }
    private void kill(Command command){
        if(currentRoom.equals(Valley)){
            if(player.getInventory().containsKey("sword")){
                System.out.println("You got a key for the wooden box from killing the birds.");
                player.setItem("key", new Item());
                player.getItem("sword");
                return;
            }
        }
    }
    private void touch(Command command){
        if(currentRoom.equals(Cemetery)){
            died = true;
            return;
        }
        else if(!currentRoom.equals(Cemetery)){
            died = false;
            return;
        }
    }
    private void throw1(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Throw what?");
            return;
        }
        String item = command.getSecondWord();

        if(player.getInventory().containsKey("rock")){
            System.out.println("You threw the rock to distract tigers.");
            threw = true;
            player.getItem("rock");
            return;
        }
    }
    private void light(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Light what?");
            return;
        }
        String item = command.getSecondWord();

        if(player.getInventory().containsKey("lighter") && player.getInventory().containsKey("candle")){
            System.out.println("You lighted the candles");
            player.getItem("lighter");
            player.getItem("candle");
            bright = true;
            return;
        }
    }
    private void wear(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Wear what?");
            return;
        }
        String item = command.getSecondWord();

        if(player.getInventory().containsKey(item)){
            System.out.println("You wore the ghillie suit");
            player.getInventory().remove("ghillieSuit");
            player.setApp("ghillieSuit", new Item());
        }
    }

    private void smash(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Smash what?");
            return;
        }
        String item = command.getSecondWord();

        if(player.getInventory().containsKey("hammer") && player.getInventory().containsKey("luggage")){
            System.out.println("You smashed the luggage and found a ghillie suit.");
            System.out.println("The ghillie suit goes to your inventory.");
            player.setItem("ghillieSuit", new Item());
            player.getInventory().remove("luggage");
            player.getInventory().remove("hammer");
        }
    }

    private void open(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Open what?");
            return;
        }

        String item = command.getSecondWord();

        if(currentRoom.equals(Valley)){
            if(player.getInventory().containsKey("key") && player.getInventory().containsKey("woodenbox")){
                System.out.println("You opened the wooden box and found a lighter for candles.");
                System.out.println("The lighter goes to your inventory.");
                player.setItem("lighter", new Item());
                player.getItem("woodenbox");
                player.getItem("key");
                return;
            }
        }
        else if(currentRoom.equals(Cemetery)){
            System.out.println("Enter the passcode from the spirits. say open + passcode");
            String key = command.getSecondWord();
            if(key.equals("0203")){
                System.out.println("Yay! You won the game. You will go to the heaven!");
                wantToQuit = true;
                return;
            }
            else{
                System.out.println("Wrong passcode!");
                return;
            }
        }

    }

    private void grab(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Grab what?");
            return;
        }

        String item = command.getSecondWord();
        Item grabItem = currentRoom.getItem(item);

        if (grabItem == null) {
            System.out.println("You can't grab " + command.getSecondWord());
        }
        else {
            player.setItem(item, grabItem);
            System.out.println("You grabbed " + item + ".");
        }
    }

    private void drop(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Drop what?");
            return;
        }

        String key = command.getSecondWord();
        Item dropItem = player.getItem(key);

        if (dropItem == null) {
            System.out.println("You can't drop " + command.getSecondWord());
        }
        else {
            currentRoom.setItem(key, dropItem);
        }
    }

    private void printHelp() {
        System.out.println("You are lost.  You are alone.  You wander");
        System.out.println("You are in a garden maze");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    private void look(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("You can't look at " + command.getSecondWord());
            return;
        }

        System.out.println(currentRoom.getShortDescription());
        System.out.println(currentRoom.getLongDescription());
        System.out.println(player.getItemString());
    }

    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
            return;
        }
        else {
            if (nextRoom.equals(Valley)) {
                if(player.getApp().containsKey("ghillieSuit")) {
                    System.out.println("You avoided the mysterious tribe!");
                    currentRoom = nextRoom;
                    System.out.println(currentRoom.getShortDescription());
                    return;
                }
                else if (!player.getApp().containsKey("ghillieSuit")) {
                    System.out.println("You could not avoid the mysterious tribe! You are dead. Game Finished.");
                    wantToQuit = true;
                    return;
                }
            }
            else if(nextRoom.equals(ForestPath)){
                if(bright.equals(true)){
                    System.out.println("You can now see the path! Also, you can grab some rocks from the floor. It will be helpful for you to pass the Forest.");
                    currentRoom = nextRoom;
                    wantToQuit = false;
                    return;
                }
                else if(bright.equals(false)){
                    System.out.println("You cannot survive in the darkness. Game finished.");
                    wantToQuit = true;
                    return;
                }
                return;
            }
            else if(nextRoom.equals(Forest)){
                if(threw.equals(true)){
                    System.out.println("You successfully distracted tigers! You survived.");
                    wantToQuit = false;
                    currentRoom = nextRoom;
                    return;
                }
                else if(threw.equals(false)){
                    System.out.println("You could not avoid tigers. You are dead. Game finished.");
                    return;
                }
            }
            else if(nextRoom.equals(Cemetery)){
                if(died.equals(true)){
                    System.out.println("You touched the area of cemetery. The spirits killed you. Game finished.");
                    wantToQuit = true;
                    return;
                }
                else if(died.equals(false)){
                    System.out.println("You did not make the spirits angry! They whispered 0203.");
                    currentRoom = nextRoom;
                    wantToQuit = false;
                    return;
                }
            }
            else{
                currentRoom = nextRoom;
                System.out.println(currentRoom.getShortDescription());
                return;
            }

        }
    }

    private boolean quit(Command command) {
        if(command.hasSecondWord()) {
            System.out.println("You can't quit " + command.getSecondWord());
            return false;
        }
        else {
            return true;
        }
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to my text adventure game!");
        System.out.println("You will find yourself in a garden maze, desperate to escape!");
        System.out.println("Type \"help\" if you need assistance");
        System.out.println();
        System.out.println("The house has two floors, and the fence is opened. The first floor has a living room, and it has a sword and hammer. The second floor has a baby's room and a bathroom. To the south of this house, there is a valley.");
    }


}