package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class NameFinder {
	 public void findName(String paragraph) throws IOException {
	        InputStream inputStream = new FileInputStream("./models/en-ner-person.bin");
	        TokenNameFinderModel model = null;
	    				try {
	    					model = new TokenNameFinderModel(inputStream);
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				} 
	        NameFinderME nameFinder = new NameFinderME(model);
	        String[] tokens = tokenise(paragraph);

	        Span nameSpans[] = nameFinder.find(tokens);
	        for(Span s: nameSpans) {
	            System.out.println(tokens[s.getStart()]);
	            System.out.println(s.getType()+" : "+tokens[s.getStart()]+"\t [probability="+s.getProb()+"]");
	        }
	    }
//to test our newly built custom model	 
	 public void asianFindName(String paragraph) throws IOException {
	        InputStream inputStream = new FileInputStream("./models/en-ner-asiannames.bin");
	        TokenNameFinderModel model = null;
	    				try {
	    					model = new TokenNameFinderModel(inputStream);
	    				} catch (IOException e) {
	    					// TODO Auto-generated catch block
	    					e.printStackTrace();
	    				} 
	        NameFinderME nameFinder = new NameFinderME(model);
	        String[] tokens = tokenise(paragraph);

	        Span nameSpans[] = nameFinder.find(tokens);
	        for(Span s: nameSpans) {
	            System.out.println(tokens[s.getStart()]);
	            System.out.println(s.getType()+" : "+tokens[s.getStart()]+"\t [probability="+s.getProb()+"]");
	        }
	    }
//both methods above need to tokenise the sentence first before extracting NER
	    public String[] tokenise(String sentence) throws IOException{
	        InputStream inputStreamTokenizer = new FileInputStream("./models/en-token.bin");
	        TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer);
	        TokenizerME tokenizer = new TokenizerME(tokenModel);
	        return tokenizer.tokenize(sentence);
	    }

public static void main(String[] args) throws Exception {
	
	NameFinder namefinder = new NameFinder();
	namefinder.findName("Where is Charlie and Mike.");
	namefinder.findName("Fraser is my son.");
	namefinder.findName("I love Dominoes.");
	namefinder.findName("I love Seb.");
	
	namefinder.asianFindName("Salah is not my relative. and former student of Calcutta");
	namefinder.asianFindName("Salah is not my relative, will be going school soon.");
	namefinder.asianFindName("I love Dominoes.");
	namefinder.asianFindName("I love Mr. Noor.");
	namefinder.asianFindName("Mr. Ching is my son.");
	namefinder.asianFindName("Where is Charlie and Mike.");
	namefinder.asianFindName("Biswajit is now studying at Calcutta");
	}

}