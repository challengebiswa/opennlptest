package nlp.intent.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.ml.maxent.quasinewton.QNTrainer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.InputStreamFactory;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class NlpTrain {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		InputStreamFactory in = null;
		try {
		    in = new MarkableFileInputStreamFactory(new File("E:\\projects\\Nlp\\opennlptest\\opennlptest\\example\\weather\\train\\current-weather.txt"));
		} catch (FileNotFoundException e2) {
		    e2.printStackTrace();
		}
		 
		ObjectStream sampleStream = null;
		try {
		    sampleStream = new NameSampleDataStream(
		        new PlainTextByLineStream(in, StandardCharsets.UTF_8));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		TrainingParameters params = new TrainingParameters();
		params.put(TrainingParameters.ALGORITHM_PARAM, QNTrainer.MAXENT_QN_VALUE);
		params.put(TrainingParameters.ITERATIONS_PARAM, 100);
		params.put(TrainingParameters.CUTOFF_PARAM, 5);
		
		TokenNameFinderModel nameFinderModel = null;
		try {
		    nameFinderModel = NameFinderME.train("en", null, sampleStream, params, new TokenNameFinderFactory());
		} catch (IOException e) {
		    e.printStackTrace();
		}
		TokenNameFinder nameFinder = new NameFinderME(nameFinderModel);
		
		InputStream modelIn = new FileInputStream("./models/en-token.bin");
        TokenizerModel model = new TokenizerModel(modelIn);
        Tokenizer tokenizer = new TokenizerME(model);
        
		 String[] testSentence =tokenizer.tokenize("we are going from Kolkata to Mumbai on friday for Lord");
		 
	        System.out.println("Finding types in the test sentence..");
	        Span[] names = nameFinder.find(testSentence);
	        for(Span name:names){
	            String personName="";
	            for(int i=name.getStart();i<name.getEnd();i++){
	                personName+=testSentence[i]+" ";
	            }
	            System.out.println(name.getType()+" : "+personName+"\t [probability="+name.getProb()+"]");
	        }
	        Map<String,List<String>> intentMap=new HashMap<>();
	        
	        InputStream inputStreamPOSTagger = new FileInputStream("./models/en-pos-maxent.bin");
    	    POSModel posModel = new POSModel(inputStreamPOSTagger);
    	    POSTaggerME posTagger = new POSTaggerME(posModel);
    	    String tags[] = posTagger.tag(testSentence);
    	    InputStream dictLemmatizer = new FileInputStream("./models/en-lemmatizer.dict");
    	    DictionaryLemmatizer lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
    	    String[] lemmas = lemmatizer.lemmatize(testSentence, tags);
    	     for(String lemma:lemmas)
    	    	 System.out.println(lemma);
	       // Span[] spans = nameFinderME.find(tokens);
            String[] intentNames = Span.spansToStrings(names, testSentence);
            for (int i = 0; i < names.length; i++) {
                if(i > 0) { System.out.print(", "); }
                System.out.print(names[i].getType() + ": '" + intentNames[i] + "' ");
                intentMap=NlpTrain.insrtMap(intentMap, intentNames[i], names[i].getType(), lemmas);
                  	NlpDictionary.getMatchDict(intentNames[i]);
               
            }
            
            Gson gson = new Gson(); 
            String json = gson.toJson(intentMap); 
            System.out.println("\n"+json);
	}
	
	public static Map<String,List<String>> insrtMap(Map<String,List<String>> intentMap,String intentNames,String intentType,String[] lemma) {
		String[] tokenize=WhitespaceTokenizer.INSTANCE.tokenize(intentNames);
		List<String> lemmaList=Arrays.asList(lemma);
		if(intentMap.containsKey(intentType)) {
			List<String> contentList=intentMap.get(intentType);
			for(String tokenizeIntent:tokenize) {
				System.out.println(tokenizeIntent);
			    if(!lemmaList.contains(tokenizeIntent.trim()))
				contentList.add(tokenizeIntent);
			}
			intentMap.put(intentType, contentList);
		}
		else {
			List<String> contentList=new ArrayList<>();
			for(String tokenizeIntent:tokenize) {
			    if(!lemmaList.contains(tokenizeIntent))
				contentList.add(tokenizeIntent);
			}
			intentMap.put(intentType, contentList);
		}
		return intentMap;
	}
	
	public static void chkDictionary(String intentName) {
		
	}

}
