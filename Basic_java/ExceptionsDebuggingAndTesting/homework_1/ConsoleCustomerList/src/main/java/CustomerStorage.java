import java.util.HashMap;
import java.util.Map;

public class CustomerStorage {
    private final Map<String, Customer> storage;

    public CustomerStorage() {
        storage = new HashMap<>();
    }

    public void addCustomer(String data) throws InputException {
        final int INDEX_NAME = 0;
        final int INDEX_SURNAME = 1;
        final int INDEX_EMAIL = 2;
        final int INDEX_PHONE = 3;

        String[] components = data.split("\\s+");
        TestIncome checkText = new TestIncome(components);
        if (checkText.input()) {
            checkText.print();
            String name = components[INDEX_NAME] + " " + components[INDEX_SURNAME];
            storage.put(name, new Customer(name, components[INDEX_PHONE], components[INDEX_EMAIL]));
        } else {
            checkText.setMessage(" Invalid income text! ");
            checkText.print();
            if ((components.length > 4) || (components.length < 3))
                throw new InputException(" Invalid length");
            if (!(components[INDEX_NAME].replaceAll("[a-zA-Z ]", "").isEmpty()))
                throw new InputException(" Invalid name ");
            if (!(components[INDEX_SURNAME].replaceAll("[a-zA-Z ]", "").isEmpty()))
                throw new InputException(" Invalid surName ");
            if (!(components[INDEX_EMAIL].replaceAll("[^@.]", "")).isEmpty())
                throw new InputException(" Invalid email ");
            if (!(components[INDEX_PHONE].replaceAll("[+0-9 ]", "")).isEmpty())
                throw new InputException(" Invalid phone ");
        }
    }

    public void listCustomers() { storage.values().forEach(System.out::println); }

    public void removeCustomer(String name) { storage.remove(name); }

    public Customer getCustomer(String name) { return storage.get(name); }

    public int getCount() { return storage.size(); }
}