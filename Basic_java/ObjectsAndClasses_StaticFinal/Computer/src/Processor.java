public class Processor {
    private final int core;
    private final int frequency;
    private final int weight;
    private final String type;

    public Processor(int core, int frequency, int weight, String type) {
        this.core = core;
        this.frequency = frequency;
        this.weight = weight;
        this.type = type;
    }
    public int getCore() { return core; }
    public int getFrequency() { return frequency; }
    public int getWeight() { return weight;  }
    public String getType() { return type;  }
    public Processor setCore (int core) {
        return new Processor(core,frequency,weight,type);
    }
    public Processor setFrequency (int frequency) {
        return new Processor(core,frequency,weight,type);
    }
    public Processor setWeight (int weight) {
        return new Processor(core,frequency,weight,type);
    }
    public Processor setType (String type) {
        return new Processor(core,frequency,weight,type);
    }
    public void result() {
        setCore(core);
        setFrequency(frequency);
        setWeight(weight);
        setType(type);
    }
}
