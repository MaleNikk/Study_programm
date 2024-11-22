public class Monitor {
    private final int size;
    private final int weight;
    private final String type;

    public Monitor(int size, int weight, String type) {
        this.size = size;
        this.weight = weight;
        this.type = type;
    }
    public int getSize() { return size; }
    public int getWeight() { return weight; }
    public String getType() { return type;  }
    public Monitor setSize(int size) {
        return new Monitor(size,weight,type);
    }
    public Monitor setWeight(int weight) {
        return new Monitor(size,weight,type);
    }
    public Monitor setType(String type) {
        return new Monitor(size,weight,type);
    }
    public void result() {
        setSize(size);
        setWeight(weight);
        setType(type);
    }
}
