package nlp.intent.toolkit;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetector;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class POSTaggingExample {


	    POSTaggerME tagger = null;
	    POSModel model = null;

	    public void initialize(String lexiconFileName) {
	        try {
	            InputStream modelStream =  getClass().getResourceAsStream(lexiconFileName);
	            model = new POSModel(modelStream);
	            tagger = new POSTaggerME(model);
	        } catch (IOException e) {
	            System.out.println(e.getMessage());
	        }
	    }

	    public void tag(String text){
	        initialize("./models/en-pos-maxent.bin");
	        try {
	            if (model != null) {
	                POSTaggerME tagger = new POSTaggerME(model);
	                if (tagger != null) {
	                    String[] sentences = detectSentences(text);
	                    for (String sentence : sentences) {
	                        String whitespaceTokenizerLine[] = WhitespaceTokenizer.INSTANCE
	                                .tokenize(sentence);
	                        String[] tags = tagger.tag(whitespaceTokenizerLine);
	                        for (int i = 0; i < whitespaceTokenizerLine.length; i++) {
	                            String word = whitespaceTokenizerLine[i].trim();
	                            String tag = tags[i].trim();
	                            System.out.print(tag + ":" + word + "  ");
	                        }
	                    }
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    public String[] detectSentences(String paragraph) throws IOException {

	        InputStream modelIn = getClass().getResourceAsStream("./models/en-sent.bin");
	        final SentenceModel sentenceModel = new SentenceModel(modelIn);
	        modelIn.close();

	        SentenceDetector sentenceDetector = new SentenceDetectorME(sentenceModel);
	        String sentences[] = sentenceDetector.sentDetect(paragraph);
	        for (String sent : sentences) {
	            System.out.println(sent);
	        }
	        return sentences;
	    }
	    
	    public static void main(String[] args) {
			PosTaggerExample pos=new PosTaggerExample();
			try {
				pos.getPos("I am returning from kolkata to delhi");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


