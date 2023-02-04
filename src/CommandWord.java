public enum CommandWord {
    GO("go"), QUIT("quit"), HELP("help"), UNKNOWN("?"), LOOK("look"), GRAB("grab"), DROP("drop"), OPEN("open"), SMASH("smash"), WEAR("wear"), LIGHT("light"), THROW("throw"), TOUCH("touch"), KILL("kill");

    private String commandString;
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }

    public String toString()
    {
        return commandString;
    }
}
