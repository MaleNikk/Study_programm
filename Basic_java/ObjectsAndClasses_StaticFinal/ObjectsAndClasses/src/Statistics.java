public class Statistics {
    private static int allPrice;
    private static int allBasket;
    private static int allCount;
    private static double allWeight;

    public static int getAllCount() {
        return allCount;
    }

    public static int getAllBasket() {
        return allBasket;
    }

    public static int getAllPrice() {
        return allPrice;
    }

    public static double getAllWeight() {
        return allWeight;
    }

    public static int sumCount() {
        allCount = allCount + Basket.getProductCount();
        return allCount;
    }

    public static int sumPrice() {
        allPrice = allPrice + Basket.getTotalPrice();
        return allPrice;
    }

    public static int sumBasket() {
        allBasket = allBasket + Basket.getBasketCount();
        return allBasket;
    }

    public static double sumWeight() {
        allWeight = allWeight + Basket.getTotalWeight();
        return allWeight;
    }

    public static int ratioA() {
        return allPrice / allCount;
    }

    public static int ratioB() {
        return allPrice / allBasket;
    }

    public static void finish() {
        sumWeight();
        sumCount();
        sumBasket();
        sumPrice();
        ratioA();
        ratioB();
    }

}
