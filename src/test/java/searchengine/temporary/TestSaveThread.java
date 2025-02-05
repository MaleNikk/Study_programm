package searchengine.temporary;

import searchengine.dto.entity.ModelWord;
import searchengine.searching.repository.AppManagementRepositoryImpl;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class TestSaveThread extends Thread {

    private final ConcurrentLinkedQueue<List<ModelWord>> linkedQueue;

    private final AppManagementRepositoryImpl repository;

    public TestSaveThread(ConcurrentLinkedQueue<List<ModelWord>> linkedQueue, AppManagementRepositoryImpl repository) {
        this.linkedQueue = linkedQueue;
        this.repository = repository;
    }

    @Override
    public void run() {
        repository.saveWord(linkedQueue.poll());
    }
}
