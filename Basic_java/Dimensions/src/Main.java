public class Main {
    public static void main(String[] args) {

        //Товар №1
        Calculation copy = new Calculation();
        //введите параметры: высота, ширина, длина.
        copy.calculation(2.5, 1.5, 4.5);
        //Внесите данные груза: вес (формат: 0.0).
        Dimensions dimensions = new Dimensions(128.9, copy.getVolume());
        dimensions.add();
        //Напишите: адрес, рег. номер,
        // особенность(поворот - неповорот, хрупкое - нехрупкое через дефис без бробела),
        // телефон.
        About about = new About("ул.Громова 27", "0015783",
                "хрупкое-неповорот", "99994444222");
        about.productInfo();
        System.out.println("   ");
        System.out.println("   ");

        //Товар №2
        Calculation copy1 = new Calculation();
        copy1.calculation(1.3,3.9,2.1);
        Dimensions dimensions1 = new Dimensions(135, copy1.getVolume());
        dimensions1.add();
        About about1 = new About("ул.Мухомора 7","0017956",
                "нехрупкое - поворот","333354876259");
        about1.productInfo();
        System.out.println("   ");
        System.out.println("   ");

        //Товар №3
        Calculation copy2 = new Calculation();
        copy2.calculation(2.9,3.4,1.8);
        Dimensions dimensions2 = new Dimensions(15,copy2.getVolume());
        dimensions2.add();
        About about2 = new About("ул.Дождя 17","0019457",
                "хрупкое - поворот","1276549882364");
        about2.productInfo();

    }

}
