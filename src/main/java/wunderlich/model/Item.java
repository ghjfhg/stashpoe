package wunderlich.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Item {


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Boolean verified;
    public Integer w;
    public Integer h;
    public Integer ilvl;
    public String icon;
    public Boolean support;
    public String league;
    public String id;
    public List<Socket> sockets = new ArrayList<Socket>();
    public String name;
    public String typeLine;
    public Boolean identified;
    public Boolean corrupted;
    public Boolean lockedToCharacter;
    public List<Property> properties = new ArrayList<Property>();
    public List<Requirement> requirements = new ArrayList<Requirement>();
    public List<String> explicitMods = new ArrayList<String>();
    public List<String> flavourText = new ArrayList<String>();
    public Integer frameType;
    public Integer x;
    public Integer y;
    public String inventoryId;
    public List<Object> socketedItems = new ArrayList<Object>();
    public String descrText;
    public List<String> implicitMods = new ArrayList<String>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public int getStackSize()  {
        if (frameType == 5 && ("Divine Orb".equals(typeLine) || "Exalted Orb".equals(typeLine))) {
            String stackSizeString = properties.get(0).values.get(0).get(0); //1/20
            return Integer.valueOf(stackSizeString.split("/")[0]);
        }
        return 0;
    }


}