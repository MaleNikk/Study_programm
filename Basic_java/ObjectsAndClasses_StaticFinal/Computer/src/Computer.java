public class Computer {
    private final String vendorPC;
    private final String name;
    private final Processor processor;
    private final Memory memory;
    private final Storage storage;
    private final Monitor monitor;
    private final Keyboard keyboard;

    public Computer(String vendorPC, String name, Processor processor, Memory memory,
                    Storage storage, Monitor monitor, Keyboard keyboard) {
        this.vendorPC = vendorPC;
        this.name = name;
        this.processor = processor;
        this.memory = memory;
        this.storage = storage;
        this.monitor = monitor;
        this.keyboard = keyboard;
    }

    public String getVendorPC() {
        return vendorPC;
    }

    public String getName() {
        return name;
    }

    public int weight() {
        return keyboard.getWeight() + memory.getWeight() + monitor.getWeight() +
                processor.getWeight() + storage.getWeight();
    }

    @Override
    public String toString() {
        return "  " + "\n" +
                " Компьютер: " + "\n" +
                " Производитель:" + getVendorPC() + "\n" +
                " Наименование: " + getName() + "\n" +
                " Процессор: " + processor.getType() +
                " Тактовая частота: " + processor.getFrequency() + " Ghz." +
                " Количество ядер: " + processor.getCore() +
                " Вес: " + processor.getWeight() + " г." + "\n" +
                " Оперативная память: " + memory.getType() +
                " Объем: " + memory.getSize() + " MB." +
                " Вес: " + memory.getWeight() + " г." + "\n" +
                " Накопитель: " + storage.getType() +
                " Объем: " + storage.getSize() + " GB." +
                " Вес: " + storage.getWeight() + " г." + "\n" +
                " Дисплей: " + monitor.getType() +
                " Диагональ: " + monitor.getSize() + "'" +
                " Вес: " + monitor.getWeight() + " г." + "\n" +
                " Клавиатура: " + keyboard.getType() +
                " Подсветка: " + keyboard.getLight() +
                " Вес: " + keyboard.getWeight() + " г." + "\n" +
                " Вес компьютера: " + weight() / 1000 + " кг." + "\n" +
                "  ";
    }

    public static void main(String[] args) {
        System.out.println("""
                 Введите имя производителя и наименование компьютера.
                 Bведите требуемые комплектующие.\
                """);
        //Собираем комплектующие в компьютер№1.
        Computer computer =
                new Computer("Skill_box", "First",
                        new Processor(4, 3, 350, String.valueOf(Vendor.AMD)),
                        new Memory(8, 350, String.valueOf(Vendor.DDR3)),
                        new Storage(250, 550, String.valueOf(Vendor.SSD)),
                        new Monitor(21, 4500, String.valueOf(Vendor.IPS)),
                        new Keyboard(650, String.valueOf(Vendor.OFFICE), String.valueOf(Light.YES)));
        System.out.println(computer);

        //Собираем комплектующие в компьютер№2.
        Computer computer1 =
                new Computer("Lerna_school", "Second",
                        new Processor(6, 5, 650, String.valueOf(Vendor.INTEL)),
                        new Memory(16, 650, String.valueOf(Vendor.DDR4)),
                        new Storage(750, 950, String.valueOf(Vendor.HDD)),
                        new Monitor(27, 6500, String.valueOf(Vendor.VA)),
                        new Keyboard(850, String.valueOf(Vendor.HOME), String.valueOf(Light.YES)));
        System.out.println(computer1);

        //Собираем комплектующие в компьютер№3.
        Computer computer2 =
                new Computer("Home_edition", "Third",
                        new Processor(8, 9, 950, String.valueOf(Vendor.IBM)),
                        new Memory(16, 850, String.valueOf(Vendor.DDR5)),
                        new Storage(950, 1050, String.valueOf(Vendor.HDD)),
                        new Monitor(32, 8500, String.valueOf(Vendor.TN)),
                        new Keyboard(550, String.valueOf(Vendor.GAME), String.valueOf(Light.NO)));
        System.out.println(computer2);

    }

}

