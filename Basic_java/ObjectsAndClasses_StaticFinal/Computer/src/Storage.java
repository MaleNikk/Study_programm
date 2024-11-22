public class Storage {
    private final int size;
    private final int weight;
    private final String type;

    public Storage(int size, int weight, String type) {
        this.size = size;
        this.weight = weight;
        this.type = type;
    }
    public int getSize() { return size; }
    public int getWeight() { return weight; }
    public String getType() { return type;  }
    public Storage setSize (int size) {
        return new Storage(size,weight,type);
    }
    public Storage setWeight (int weight) {
        return new Storage(size,weight,type);
    }
    public Storage setType (String type) {
        return new Storage(size,weight,type);
    }
    public void result() {
        setSize(size);
        setWeight(weight);
        setType(type);
    }
}
