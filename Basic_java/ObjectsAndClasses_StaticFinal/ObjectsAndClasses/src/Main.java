public class Main {

    public static void main(String[] args) {
         /*
        Корзина №1
         */
        Basket basket = new Basket();
        basket.add("Яблоко", 35, 9, 45);
        basket.print("Товарный чек");
        basket.statistics();
        /*
        Корзина №2
         */
        Basket basket1 = new Basket();
        basket1.add("Кукуруза", 75, 6, 20);
        basket1.print("Товарный чек");
        basket1.statistics();
        /*
        Корзина №3
         */
        Basket basket2 = new Basket();
        basket2.add("Капуста", 63, 4, 5);
        basket2.print("Товарный чек");
        basket2.statistics();
         /*
        Корзина №4
         */
        Basket basket3 = new Basket();
        basket3.add("Сок", 47, 6, 25);
        basket3.print("Товарный чек");
        basket3.statistics();


        System.out.println("Итоговая информация: ");
        System.out.println("Общее количество корзин: " + Statistics.getAllBasket());
        System.out.println("Общая стоимость товаров во всех корзинах: " + Statistics.getAllPrice());
        System.out.println("Общее количество товаров во всех корзинах: " + Statistics.getAllCount());
        System.out.println("Общее количество веса товаров во всех корзинах: " + Statistics.getAllWeight());
        System.out.println("Соотношение общей стоимости корзин к общему количеству товаров во всех корзинах: " + Statistics.ratioA());
        System.out.println("Соотношение общей стоимости корзин к количеству всех корзин: " + Statistics.ratioB());

    }
}
