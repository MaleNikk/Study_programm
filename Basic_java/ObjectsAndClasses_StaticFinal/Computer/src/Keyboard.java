public class Keyboard {
    private final int weight;
    private final String type;
    private final String light;
    public Keyboard(int weight, String type, String light) {
        this.weight = weight;
        this.type = type;
        this.light = light;
    }
    public int getWeight() { return weight; }
    public String getType() { return type;  }
    public String getLight() { return light; }
    public Keyboard setWeight(int weight) {
        return new Keyboard(weight,type,light);
    }
    public Keyboard setType(String type) {
        return new Keyboard(weight,type,light);
    }
    public Keyboard setLight(String light) {
        return new Keyboard(weight,type,light);
    }
    public void result() {
        setType(type);
        setWeight(weight);
        setLight(light);
    }

}
