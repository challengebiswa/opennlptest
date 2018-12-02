package nlp.intent.toolkit;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;

public class Lemmatization {

	
	public static void main(String[] args) throws Exception {
		SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
	    String[] tokens = tokenizer.tokenize("send me status report on friday");
	 
	    InputStream inputStreamPOSTagger = new FileInputStream("./models/en-pos-maxent.bin");
	    POSModel posModel = new POSModel(inputStreamPOSTagger);
	    POSTaggerME posTagger = new POSTaggerME(posModel);
	    String tags[] = posTagger.tag(tokens);
	    InputStream dictLemmatizer = new FileInputStream("./models/en-lemmatizer.dict");
	    DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
	    String[] lemmas = lemmatizer.lemmatize(tokens, tags);
	    for(String lemma:lemmas)
	    	System.out.println(lemma);
	}
}
