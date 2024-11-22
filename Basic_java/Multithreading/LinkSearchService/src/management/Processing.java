package management;

import component.Connection;
import component.StorageAddress;
import component.Treatment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.RecursiveTask;


public class Processing extends RecursiveTask<HashSet<String>> {
    private final Treatment treatment;
    private final StorageAddress storageAddress;

    public Processing(Treatment treatment, StorageAddress storageAddress) {
        this.treatment = treatment;
        this.storageAddress = storageAddress;
    }
    public Treatment getTreatment() {
        return this.treatment;
    }
    private StorageAddress getStorageAddress() {
        return this.storageAddress;
    }

    @Override
    protected HashSet<String> compute() {
        List<Processing> tasks = new ArrayList<>();
        for (String newPathUrl : getStorageAddress().getAddressesIncome()) {
            if (!getStorageAddress().getLinks().containsValue(newPathUrl)) {
                Processing processing = new Processing(new Treatment(getStorageAddress(),
                        new Connection(newPathUrl), Main.test), getStorageAddress());
                getStorageAddress().addLinks(newPathUrl);
                processing.fork();
                tasks.add(processing);
            }
        }
        for (Processing processing:tasks){
            processing.join();
        }
        System.out.println("Search finish.");
        return getStorageAddress().getAddressesIncome();
    }
}
