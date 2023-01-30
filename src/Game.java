public class Game {

    private Room currentRoom;
    Room Valley;
    Room House;
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
        Room BabyRoom = new Room("The baby's room is on the second floor of the house.", "The baby's room has a candle on the desk. To the east side of this room, there is a bathroom.");
        Room LivingRoom = new Room("The living room is on the first floor of the house.", "The living room has a sword and a hammer on the floor.");
        Room BathRoom = new Room("The bathroom is on the second floor of the house.", "The bathroom has a water system for you to drink or bring it with a water bottle. To the west side of this room, there is a baby's room.");
        Room Valley = new Room("The valley has a lake between two giant mountains.", "There is a mysterious tribe that kills anyone with unique appearance. Also, there is a small tree with a nest of birds. The nest has a locked wooden box that needs a key to open. To the east, there is a forest path.");
        Room ForestPath = new Room("This is a forest path toward a forest to the east and a cemetery to the south.", "There are a a lot of trees and rocks along the path. It's too dark, so you cannot move without any sort of light. To the south, there is a cemetery with an opened gate.");
        Room Forest = new Room("This forest has a tremendous amount of trees, and it is surrounded by giant mountains that look impenetrable.", "There aren't any humans, but there are some big animals, including tigers. They will attack anything that makes noises. You can use any utensils to avoid the animals.");
        Room Cemetery = new Room("The cemetery is surrounded by the fence, but the gate is opened.", "The cemetery has some spirits from the grave. They will be mad and punish you if you touch inappropriate area of the cemetery. In the middle of the cemetery, there is a weird door which is not connected to any other place.");

        House.setExit("south", Valley);
        House.setExit("in", LivingRoom);
        LivingRoom.setExit("out", House);
        LivingRoom.setExit("up", BabyRoom);
        BabyRoom.setExit("east", BathRoom);
        BabyRoom.setExit("down", LivingRoom);
        BathRoom.setExit("west", BabyRoom);
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
        Valley.setItem("key", key);
        ForestPath.setItem("rock", rock);

        currentRoom = House;
    }

    public void play() {
        printWelcome();

        boolean finished = false;

        while(!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thanks for playing!");
    }

    private boolean processCommand(Command command) {
        boolean wantToQuit = false;

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
        }

        return wantToQuit;
    }

    private void wear(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Wear what?");
            return;
        }
        String item = command.getSecondWord();
        if(player.getInventory().containsKey("ghillieSuit") && currentRoom.equals(Valley)){
            System.out.println("You successfully avoided the mysterious tribe!");
        }
        else if(!player.getInventory().containsKey("ghillieSuit") && currentRoom.equals(Valley)){
            System.out.println("You could not avoid the mysterious tribe, so you went back to the house");
            currentRoom.equals(House);
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
            player.setItem("ghilleSuit", new Item());
            player.getItem("luggage");
            player.getItem("hammer");
        }
    }

    private void open(Command command){
        if(!command.hasSecondWord()){
            System.out.println("Open what?");
            return;
        }

        String item = command.getSecondWord();

        if(player.getInventory().containsKey("key") && player.getInventory().containsKey("woodenbox")){
            System.out.println("You opened the wooden box and found a lighter for candles.");
            System.out.println("The lighter goes to your inventory.");
            player.setItem("lighter", new Item());
            player.getItem("woodenbox");
            player.getItem("key");
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
            System.out.println("You grabbed " + grabItem + ".");
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
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getShortDescription());
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
        System.out.println("we will print a long room description here");
    }


}