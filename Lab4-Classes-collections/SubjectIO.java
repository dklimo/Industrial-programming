import java.io.*;
import java.util.*;

public class SubjectIO {
    public SubjectIndex loadFromFile(String filename) throws IOException {
        SubjectIndex SUB = new SubjectIndex();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length != 2) continue;

                String word = parts[0].trim();
                String[] pages = parts[1].split(",");

                SUB.addComponent(word);
                for (String p : pages) {
                    try {
                        int page = Integer.parseInt(p.trim());
                        SUB.AddPageToWord(word, page);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return SUB;
    }
}
