public class Dimensions {
    private final double weight;//кг.
    private final double maxWeight = 25000;//кг.
    private final double maxVolume = 90; //м. куб.
    private final double volume;

    public void test() {
        if (weight > maxWeight || volume > maxVolume) {
            System.out.println("Груз превышает установленные параметры доставки.");

        } else {
            System.out.println("Груз соответствует требованиям для осуществления перевозки");
        }

    }

    public Dimensions(double weight, double volume) {
        this.volume = volume;
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public double getWeight() {
        return weight;
    }

    public Dimensions setVolume(double volume) {
        System.out.println("Объем груза: " + volume + " м.куб.");
        return new Dimensions(getVolume(), weight);
    }

    public Dimensions setWeight(double weight) {
        System.out.println("Масса груза: " + weight + " кг.");
        return new Dimensions(getWeight(), volume);
    }

    public void add() {
        test();
        setVolume(volume);
        setWeight(weight);
        System.out.println("Для уточнения подробностей позвоните по тел: 954762183");

    }

}
