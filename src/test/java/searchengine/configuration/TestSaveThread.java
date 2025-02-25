package searchengine.configuration;

import searchengine.dto.entity.ModelWord;
import searchengine.repository.RepositoryProject;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class TestSaveThread extends Thread {

    private final ConcurrentLinkedQueue<List<ModelWord>> linkedQueue;

    private final RepositoryProject repository;

    public TestSaveThread(ConcurrentLinkedQueue<List<ModelWord>> linkedQueue, RepositoryProject repository) {
        this.linkedQueue = linkedQueue;
        this.repository = repository;
    }

    @Override
    public void run() {
        if (!linkedQueue.isEmpty()) {
            sendToSaveWords(linkedQueue.poll());
        }
    }

    private void sendToSaveWords(List<ModelWord> modelWords) {
        StringBuilder builder = new StringBuilder();
        for (ModelWord word : modelWords) {
            builder.append("('").append(word.lemma()).append("','").append(word.url())
                    .append("','").append(word.parentUrl()).append("),");
        }
        builder.deleteCharAt(builder.lastIndexOf(","));
        repository.saveWords(builder.toString());
    }
}
