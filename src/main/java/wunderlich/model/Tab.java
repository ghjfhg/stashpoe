
package wunderlich.model;

import java.util.HashMap;
import java.util.Map;

public class Tab {

    public String n;
    public Integer i;
    public Boolean pi;
    public Boolean hidden;
    public Boolean selected;
    public Colour colour;
    public String srcL;
    public String srcC;
    public String srcR;
    public String id;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return ""+i+ ": "+n;
    }
}
