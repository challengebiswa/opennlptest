package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

public class ChunkerExample {

	public static void main(String[] args) throws Exception{
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
	    String[] tokens = tokenizer.tokenize("He reckons the current account deficit will narrow to only 8 billion.");
	 
	    InputStream inputStreamPOSTagger = new FileInputStream("./models/en-pos-maxent.bin");
	    POSModel posModel = new POSModel(inputStreamPOSTagger);
	    POSTaggerME posTagger = new POSTaggerME(posModel);
	    String tags[] = posTagger.tag(tokens);
	 
	    InputStream inputStreamChunker = new FileInputStream("./models/en-chunker.bin");
	    ChunkerModel chunkerModel
	     = new ChunkerModel(inputStreamChunker);
	    ChunkerME chunker = new ChunkerME(chunkerModel);
	    String[] chunks = chunker.chunk(tokens, tags);
	    
	    for(String chunk:chunks) {
	    	System.out.println(chunk);
	    }
	}
}
