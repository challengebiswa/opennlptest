package nlp.intent.toolkit;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MultipleDictionaryNER {
    private static String DICTIONARIES = "/dictionaries";
    private static List<DictionaryNameFinder> finders;

    public MultipleDictionaryNER() {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;

        // Get the directory with the dictionary files
        String currentDir = System.getProperty("user.dir");
        Path directory = Paths.get(currentDir, DICTIONARIES);
        if (!Files.exists(directory))
            throw new RuntimeException("Directory '" + DICTIONARIES + "' not found.");

        finders = new LinkedList<>();
        File[] files = new File(directory.toString()).listFiles();
        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                // Create a list with a dictionary for each file
                Dictionary dictionary = new Dictionary();
                for (String line; (line = br.readLine()) != null; ) {
                    dictionary.put(new StringList(tokenizer.tokenize(line)));
                }
                String[] parts = file.toString().split("/");
                String[] fileName = parts[parts.length - 1].split("[.]");
                String type = fileName[0];
                // Use the file name to tag tokens
                finders.add(new DictionaryNameFinder(dictionary, type));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Annotation> find(String[] tokens) {
        List<Annotation> annotations = new ArrayList<>();
        List<Span> foundSpans = new ArrayList<>();

        for (DictionaryNameFinder finder : finders) {
            Span[] spans = finder.find(tokens);
            for (Span span : spans) {
                foundSpans.add(span);
            }
        }

        Collections.sort(foundSpans, new Comparator<Span>() {
            @Override
            public int compare(Span o1, Span o2) {
                return o1.compareTo(o2);
            }
        });

        for (Span span : foundSpans) {
            int start = span.getStart();
            int end = span.getEnd();
            String type = span.getType();
            String[] foundTokens = Arrays.copyOfRange(tokens, start, end);
            annotations.add(new Annotation(foundTokens, span));
        }

        return annotations;
    }

    public static void main(String[] args) throws FileNotFoundException {
        MultipleDictionaryNER ner = new MultipleDictionaryNER();
        String text = "The Terminator is a movie where Keanu Reeves acted as Neo " +
                "and he had co-actors Laurence Fishburne and Carrie-Anne Moss " +
                "as Morpheus and Trinity.";
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        List<Annotation> annotations = ner.find(tokenizer.tokenize(text));
        for (Annotation annotation : annotations) {
            for (String token : annotation.getTokens()) {
                System.out.printf("%s ", token);
            }
            Span span = annotation.getSpan();
            System.out.printf("[%d..%d) %s\n", span.getStart(), span.getEnd(), span.getType());
        }
    }

    private class Annotation {
        private String[] tokens;
        private Span span;

        public Annotation(String[] tokens, Span span) {
            this.tokens = tokens;
            this.span = span;
        }

        public String[] getTokens() {
            return tokens;
        }

        public Span getSpan() {
            return span;
        }
    }
}
