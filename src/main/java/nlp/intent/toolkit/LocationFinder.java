package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;  

public class LocationFinder { 
   public static void main(String args[]) throws Exception{
 
      InputStream inputStreamTokenizer = new 
         FileInputStream("./models/en-token.bin"); 
      TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer); 
       
      //String paragraph = "Mike and Smith are classmates"; 
      String paragraph = "Hyderabad and <start:location> Calcutta <end>"; 
        
      //Instantiating the TokenizerME class 
      TokenizerME tokenizer = new TokenizerME(tokenModel); 
      String tokens[] = tokenizer.tokenize(paragraph); 
       
      //Loading the NER-location moodel 
      InputStream inputStreamNameFinder = new 
         FileInputStream("./models/en-ner-location.bin");       
      TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder); 
        
      //Instantiating the NameFinderME class 
      NameFinderME nameFinder = new NameFinderME(model);      
        
      //Finding the names of a location 
      Span nameSpans[] = nameFinder.find(tokens);        
      //Printing the spans of the locations in the sentence 
      for(Span s: nameSpans)        
         System.out.println(s.toString()+"  "+tokens[s.getStart()]); 
      
      
      
      LocationFinder loc=new LocationFinder();
      loc.findLocation("India is a great Asia of London and Italy and Japan and <start:city>california<end>");
   }   
   
   
   public void findLocation(String paragraph) throws IOException {
       InputStream inputStreamNameFinder = new FileInputStream("./models/en-ner-location.bin");  
       TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);

       NameFinderME locFinder = new NameFinderME(model);
       String[] tokens = tokenize(paragraph);

       Span nameSpans[] = locFinder.find(tokens);
       for(Span span : nameSpans)
           System.out.println("Position - "+ span.toString() + "    LocationName - " + tokens[span.getStart()]);
   }
   public String[] tokenize(String sentence) throws IOException{
       InputStream inputStreamTokenizer = new FileInputStream("./models/en-token.bin");
       TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer);

       TokenizerME tokenizer = new TokenizerME(tokenModel);
       return tokenizer.tokenize(sentence);
   }

} 
