package nlp.intent.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import opennlp.tools.dictionary.Dictionary;
import opennlp.tools.namefind.DictionaryNameFinder;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringList;

public class NlpDictionary {
	private static List<DictionaryNameFinder> finders;
	
	public static void getMatchDict(String text) {

		finders = new LinkedList<>();
		File file=new File("E:\\projects\\Nlp\\opennlptest\\opennlptest\\dictionaries\\dict.txt");
		 SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
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
		 
		 	NlpDictionary ner = new NlpDictionary();		
	        List<Annotation> annotations = ner.find(tokenizer.tokenize(text));
	        for (Annotation annotation : annotations) {
	            for (String token : annotation.getTokens()) {
	                System.out.println("\n  dictionary match- "+token);
	            }
	            Span span = annotation.getSpan();
	           // System.out.printf("[%d..%d) %s\n", span.getStart(), span.getEnd(), span.getType());
	        }
	        
	
	}
	
	public static void main(String[] args) {
		
		NlpDictionary.getMatchDict("The Lord comes here");
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
