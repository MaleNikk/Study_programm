public class Memory {
    private final int size;
    private final int weight;
    private final String type;

    public Memory(int size, int weight, String type) {
        this.size = size;
        this.weight = weight;
        this.type = type;
    }
    public int getSize() { return size; }

    public int getWeight() { return weight; }

    public String getType() { return type;  }
    public Memory setWeight (int weight) {
        return new Memory(size,weight,type);
    }
    public Memory setSize (int size) {
        return new Memory(size,weight,type);
    }
    public Memory setType (String type) {
        return new Memory(size,weight,type);
    }
    public void result() {
        setType(type);
        setSize(size);
        setWeight(weight);
    }
}
