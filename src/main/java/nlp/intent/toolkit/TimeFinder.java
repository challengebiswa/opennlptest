package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class TimeFinder {

	public static void main(String[] args) throws Exception {
		 InputStream inputStreamTokenizer = new  FileInputStream("./models/en-token.bin"); 
	      TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer); 
	       
	      //String paragraph = "Mike and Smith are classmates"; 
	      String paragraph = "22nd December 2018 15:24:16 +00:00 and 1st Feb"; 
	        
	      //Instantiating the TokenizerME class 
	      TokenizerME tokenizer = new TokenizerME(tokenModel); 
	      String tokens[] = tokenizer.tokenize(paragraph); 
	       
	      //Loading the NER-location moodel 
	      InputStream inputStreamNameFinder = new FileInputStream("./models/en-ner-time.bin");       
	      TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder); 
	        
	      //Instantiating the NameFinderME class 
	      NameFinderME nameFinder = new NameFinderME(model);      
	        
	      //Finding the names of a location 
	      Span nameSpans[] = nameFinder.find(tokens);        
	      //Printing the spans of the locations in the sentence 
	      for(Span s: nameSpans)        
	         System.out.println(s.toString()+"  "+tokens[s.getStart()]); 
}

}
