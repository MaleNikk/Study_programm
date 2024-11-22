public class Calculation {
    private double volume;//м.куб.
    private double height;//м.
    private double wight;//м.
    private double lenght;//м.
    private String items;

    public double Calculation(double height, double wight, double lenght) {
        this.height = height;
        this.wight = wight;
        this.lenght = lenght;
        this.volume = height * wight * lenght;
        return volume;
    }

    public String getItems() {
        System.out.println("Вводите точные данные! Сведения о текущей доставке: ");
        System.out.println("Габариты: ");
        System.out.println("Высота: " + height + " м.");
        System.out.println("Ширина: " + wight + " м.");
        System.out.println("Длина: " + lenght + " м.");
        System.out.println("Объем груза: " + volume + " м.куб.");
        return items;
    }

    public String setItems() {
        this.items = getItems();
        return items;
    }

    public double getHeight() {
        return height;
    }

    public double getWight() {
        return wight;
    }

    public double getLenght() {
        return lenght;
    }

    public void setHeight(double height) {
        if (height <= 0) {
            System.out.println("Введен неверный параметр высоты.");
            this.height = 0;
            return;
        }
        this.height = getHeight();
    }

    public void setWight(double wight) {
        if (wight <= 0) {
            System.out.println("Введен неверный параметр ширины.");
            this.wight = 0;
            return;
        }
        this.wight = getWight();
    }

    public void setLenght(double lenght) {
        if (lenght <= 0) {
            System.out.println("Введен неверный параметр длины.");
            this.lenght = 0;
            return;
        }
        this.lenght = getLenght();
    }

    public void setVolume(double volume) {
        if (volume <= 0) {
            this.volume = 0;
            return;
        }
        this.volume = getVolume();
    }

    public double getVolume() {
        return volume;
    }

    public void calculation(double height, double wight, double lenght) {
        setHeight(height);
        setWight(wight);
        setLenght(lenght);
        setVolume(volume);
        Calculation(height, wight, lenght);
        setItems();
    }

}
