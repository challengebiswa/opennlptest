package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;  

public class SentenceDetectionME { 
  
   public static void main(String args[]) throws Exception { 
   
      String sentence = "Hi. How are you? Welcome to Hello World. " ;

       
      //Loading sentence detector model 
      InputStream inputStream = new FileInputStream("./models/en-sent.bin"); 
      SentenceModel model = new SentenceModel(inputStream); 
       
      //Instantiating the SentenceDetectorME class 
      SentenceDetectorME detector = new SentenceDetectorME(model);  
    
      //Detecting the sentence
      String sentences[] = detector.sentDetect(sentence); 
    //Detecting the position of the sentences in the paragraph  
      Span[] spans = detector.sentPosDetect(sentence); 
    
      //Printing the sentences 
      for(String sent : sentences)        
         System.out.println(sent);  
      
      for (Span span : spans)         
          System.out.println(span);  
      //Printing the sentences and their spans of a paragraph 
      for (Span span : spans)         
         System.out.println(sentence.substring(span.getStart(), span.getEnd())+" "+ span);  
      
    //Getting the probabilities of the last decoded sequence       
      double[] probs = detector.getSentenceProbabilities(); 
       
      System.out.println("  "); 
       
      for(int i = 0; i<probs.length; i++) 
         System.out.println(probs[i]); 
      
    //Instantiating whitespaceTokenizer class 
      WhitespaceTokenizer whitespaceTokenizer = WhitespaceTokenizer.INSTANCE;  
      
     //Tokenizing the given paragraph 
     String tokens[] = whitespaceTokenizer.tokenize(sentence);  
      
     //Printing the tokens 
     for(String token : tokens)     
        System.out.println(token);   
     
     //Loading the Tokenizer model 
     InputStream inputStream1 = new FileInputStream("./models/en-token.bin"); 
     TokenizerModel tokenModel = new TokenizerModel(inputStream1); 
      
     //Instantiating the TokenizerME class 
     TokenizerME tokenizer = new TokenizerME(tokenModel); 
      
     //Tokenizing the given raw text 
     String tokens1[] = tokenizer.tokenize(sentence);       
         
     //Printing the tokens  
     for (String a : tokens1) 
        System.out.println(a);
   
     //Retrieving the boundaries of the tokens 
     Span[] tokens2 = tokenizer.tokenizePos(sentence);  
      
     //Printing the spans of tokens 
     for( Span token : tokens2)
        System.out.println(token +" "+sentence.substring(token.getStart(), token.getEnd()));     
     
     //Getting the probabilities of the recent calls to tokenizePos() method 
     double[] probs1 = tokenizer.getTokenProbabilities(); 
 
        for(int i = 0; i<probs1.length; i++) 
           System.out.println(probs1[i]);
  
    
   } 
}