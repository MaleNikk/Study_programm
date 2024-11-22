package saving;

import component.StorageAddress;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CreateDataFile {
    private final StorageAddress address;

    public CreateDataFile(StorageAddress address) {
        this.address = address;
    }

    public String createSource() {
        String source = "";
        for (Map.Entry<Integer, String> entry : getAddress().getLinks().entrySet()) {
            String end = treatmentLinks(entry.getValue());
            source = source.concat(end);
        }
        return source;
    }

    private String treatmentLinks(@NotNull String data) {
        String[] link = data.split("/");
        String dataOut = "";
        String source = "";
        String indents = "";
        int index = 0;
        for (String income : link) {
            if (index == 2) {
                source = link[0].concat("//").concat(income).concat("/");
            }
            if (index >= 3 && index < link.length - 1) {
                indents = indents.concat("\t");
                source = source.concat(income).concat("/");
            }
            if (index == link.length - 1) {
                indents = indents.concat("\t");
                source = source.concat(income);
            }
            if (link.length > 3) {
                dataOut = dataOut.concat("\n").concat(indents).concat(source);
            }
            index++;
        }

        return dataOut;
    }

    public @NotNull List<HashSet<String>> dataForWrite(@NotNull Map<Integer, String> links) {
        List<HashSet<String>> oneDomain = new ArrayList<>();
        HashSet<String> check = new HashSet<>();
        for (int b = 1; b < links.size(); b++) {
            if (!(links.get(b) == null)) {
                String[] strings = links.get(b).split("/");
                HashSet<String> persistence = new HashSet<>();
                for (Map.Entry<Integer, String> entryCheck : links.entrySet()) {
                    if (entryCheck.getValue().contains(strings[2]) && !(check.contains(strings[2]))) {
                        persistence.add(entryCheck.getValue());
                        check.add(strings[2]);
                    }
                }
                oneDomain.add(persistence);
            }
        }
        return oneDomain;
    }
    private StorageAddress getAddress() {
        return this.address;
    }

}
