public class TestIncome {
    private String message = " Income text is correct! ";
    private final String[] components;
    public TestIncome(String[] components) {
        this.components = components;
    }
    public String[] getComponents() {
        return components;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() { return message; }
    public boolean input() {
        return
                (getComponents().length == 4)
                        && (getComponents()[0].replaceAll("[a-zA-Zа-яА-Я ]", "").isEmpty())
                        && (getComponents()[1].replaceAll("[a-zA-Zа-яА-Я ]", "").isEmpty())
                        && (!(getComponents()[2].replaceAll("[^@.]", "").isEmpty()))
                        && (!(getComponents()[3].replaceAll("[^0-9]", "").isEmpty()));
    }
    public void print() {
        System.out.println(getMessage());
    }
}

