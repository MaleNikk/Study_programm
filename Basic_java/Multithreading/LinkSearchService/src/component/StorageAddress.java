package component;

import java.util.*;
public class StorageAddress {

    private final LinkedHashSet<String> addressesIncome;
    private final Map<Integer, String> links;

    public StorageAddress(Map<Integer, String> links, LinkedHashSet<String> addressesIncome) {
        this.links = links;
        this.addressesIncome = addressesIncome;
        System.out.println("Storage initialise.");
    }
    public void addLinks(String data){
        int key = getLinks().size();
        getLinks().put(key,data);
    }


    public LinkedHashSet<String> getAddressesIncome() {
        return addressesIncome;
    }

    public Map<Integer, String> getLinks() {
        return links;
    }
}
