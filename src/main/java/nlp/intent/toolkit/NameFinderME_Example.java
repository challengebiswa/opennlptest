package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;  

public class NameFinderME_Example { 
   public static void main(String args[]) throws Exception{ 
        InputStream inputStream = new 
         FileInputStream("./models/en-ner-person.bin"); 
      TokenNameFinderModel model = new TokenNameFinderModel(inputStream);
      //TokenizerModel tokenizerModel=new TokenizerModel(inputStream);
      //Instantiating the NameFinder class 
      NameFinderME nameFinder = new NameFinderME(model); 
      String sen = "Mike is senior programming manager and <start:person>Rama<end> is a clerk both are working at"; 
    	      
      //Getting the sentence in the form of String array  
      String [] sentence =SimpleTokenizer.INSTANCE.tokenize(sen);
       
      //Finding the names in the sentence 
      Span nameSpans[] = nameFinder.find(sentence); 
       
      //Printing the spans of the names in the sentence 
      for(Span s: nameSpans) 
         System.out.println(s.toString()+"  "+sentence[s.getStart()]);  
      
    //Instantiating the TokenizerME class 
      //TokenizerME tokenizer = new TokenizerME(tokenizerModel); 
      //Getting the probabilities of the recent calls to tokenizePos() method 
      //double[] probs =tokenizer.getTokenProbabilities(); 

      /*for(int i = 0; i<probs.length; i++) 
         System.out.println(probs[i]);*/
   }    
}  
