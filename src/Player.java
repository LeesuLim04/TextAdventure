import java.util.HashMap;

import java.util.Set;
import java.util.Iterator;

public class Player {
    private HashMap<String, Item> inventory;

    Player() {
        inventory = new HashMap<>();
    }

    public String getItemString() {
        String returnString = "Player Inv:";
        Set<String> keys = inventory.keySet();
        for(String item: keys) {
            returnString += " " + item;
        }
        return returnString;
    }

    public void setItem(String name, Item item) {
        inventory.put(name, item);
    }

    public Item getItem(String name) {
        return inventory.remove(name);
    }

    public boolean hasKey(String open){
        if(open == "woodenbox"){
            if(inventory.containsKey("key")){
                return true;
            }
        }
            return false;
    }

    public HashMap getInventory(){
        return inventory;
    }

}
