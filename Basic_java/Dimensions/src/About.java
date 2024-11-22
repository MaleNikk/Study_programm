public class About {
    private final String adress;
    private final String regNumber;
    private final String fragile;
    private final String telefone;

    public About(String adress, String regNumber, String fragile, String telefone) {
        this.adress = adress;
        this.regNumber = regNumber;
        this.fragile = fragile;
        this.telefone = telefone;
    }

    public About setAdress(String adress) {
        System.out.println("Адрес доставки: " + adress);
        return new About(adress, regNumber, fragile, telefone);
    }

    public About setTelefone(String telefone) {
        System.out.println("Телефон получателя: " + telefone);
        return new About(adress, regNumber, fragile, telefone);
    }

    public About setFragile(String fragile) {
        System.out.println("Подробности: " + fragile);
        return new About(adress, regNumber, fragile, telefone);
    }

    public About setRegNumber(String regNumber) {
        System.out.println("Регистрационный номер товара: " + regNumber);
        return new About(adress, regNumber, fragile, telefone);
    }

    public void productInfo() {
        setAdress(adress);
        setFragile(fragile);
        setRegNumber(regNumber);
        setTelefone(telefone);
    }

}
