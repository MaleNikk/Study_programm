public class Basket {

    private static int basketCount = 0;
    private static double weight = 0;
    private String items = "";
    private static int totalPrice = 0;
    private int limit = 5000;
    private final int limitWeight = 10000;
    private static double totalWeight = 0;
    private static int productCount;
    boolean error = false;

    public Basket() {
        items = "Список товаров:";
        this.limit = limit + 10000;
    }

    public Basket(String items, int totalPrice) {
        this();
        this.items = this.items + items;
        Basket.totalPrice = totalPrice;
    }

    public static int getBasketCount() {
        return basketCount + 1;
    }

    public static double getWeight() {
        return weight;
    }

    public static int getTotalPrice() {
        return totalPrice;
    }

    public static double getTotalWeight() {
        return totalWeight;
    }

    public static int getProductCount() {
        return productCount;
    }

    public void add(String name, int price, int productCount) {
        totalPrice = totalPrice + productCount * price;
        if (contains(name) || totalPrice + productCount * price >= limit || totalWeight +
                productCount * weight >= limitWeight) {
            error = true;
            System.out.println("Внимание: превышен лимит веса, стоимости, отсутствует наименование!");
            clear();
            return;
        }
        Basket.productCount = productCount;
        items = items + "\n" + name + " - " +
                productCount + " шт. - " + totalPrice + " руб.";

    }

    public void add(String name, int price, int productCount, double weight) {
        add(name, price, productCount);
        totalWeight = totalWeight + productCount * weight;
        this.items = name + " - " + productCount + " шт. - " + totalPrice + " руб." +
                "Вес товаров в корзине: " + totalWeight;

    }

    public boolean contains(String name) {
        return items.contains(name);
    }

    public void print(String title) {
        System.out.println(title);
        if (items.isEmpty()) {
            System.out.println("Корзина пуста");
        } else {
            System.out.println(items);
        }
    }

    public void statistics() {
        Statistics.finish();
    }

    public void clear() {
        items = "";
        totalPrice = 0;
        totalWeight = 0;
        basketCount = 0;
        productCount = 0;
        weight = 0;
    }
}
