package nlp.intent.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.ml.EventTrainer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.ObjectStreamUtils;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelType;

public class IntentTrainer {
	
	 public static void main(String[] args) throws Exception {

	        File trainingDirectory = new File("E:\\projects\\Nlp\\opennlptest\\opennlptest\\example\\weather\\train");
	        String[] slots = new String[0];
	       /* if (args.length > 1) {
	            slots = args[1].split(",");
	        }*/

	        if (!trainingDirectory.isDirectory()) {
	            throw new IllegalArgumentException("TrainingDirectory is not a directory: " + trainingDirectory.getAbsolutePath());
	        }

	        List<ObjectStream<DocumentSample>> categoryStreams = new ArrayList<ObjectStream<DocumentSample>>();
	        for (File trainingFile : trainingDirectory.listFiles()) {
	            String intent = trainingFile.getName().replaceFirst("[.][^.]+$", "");
	            ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
	            ObjectStream<DocumentSample> documentSampleStream = new IntentDocumentSampleStream(intent, lineStream);
	            categoryStreams.add(documentSampleStream);
	        }

	        ObjectStream<DocumentSample> combinedDocumentSampleStream = ObjectStreamUtils.concatenateObjectStream(categoryStreams);

	        TrainingParameters trainingParams = new TrainingParameters();
	        trainingParams.put(TrainingParameters.ITERATIONS_PARAM, 10);
	        trainingParams.put(TrainingParameters.CUTOFF_PARAM, 0);

	        DoccatModel doccatModel = DocumentCategorizerME.train("en", combinedDocumentSampleStream, trainingParams, new DoccatFactory());
	        combinedDocumentSampleStream.close();

	        List<TokenNameFinderModel> tokenNameFinderModels = new ArrayList<TokenNameFinderModel>();

	        //for (String slot : slots) {
	            List<ObjectStream<NameSample>> nameStreams = new ArrayList<ObjectStream<NameSample>>();
	            for (File trainingFile : trainingDirectory.listFiles()) {
	                ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(trainingFile), "UTF-8");
	                ObjectStream<NameSample> nameSampleStream = new NameSampleDataStream(lineStream);
	                nameStreams.add(nameSampleStream);
	            }
	            ObjectStream<NameSample> combinedNameSampleStream = ObjectStreamUtils.concatenateObjectStream(nameStreams);

	            TokenNameFinderModel tokenNameFinderModel = NameFinderME.train("en", "dlocation", combinedNameSampleStream, trainingParams, new TokenNameFinderFactory());
	            combinedNameSampleStream.close();
	            tokenNameFinderModels.add(tokenNameFinderModel);
	       // }


	        DocumentCategorizerME categorizer = new DocumentCategorizerME(doccatModel);
	        NameFinderME[] nameFinderMEs = new NameFinderME[tokenNameFinderModels.size()];
	        for (int i = 0; i < tokenNameFinderModels.size(); i++) {
	            nameFinderMEs[i] = new NameFinderME(tokenNameFinderModels.get(i));
	        }

	        System.out.println("Training complete. Ready.");
	        System.out.print(">");
	        String s="I am travelling from kolkata";

	        InputStream modelIn = new FileInputStream("./models/en-token.bin");
	        TokenizerModel model = new TokenizerModel(modelIn);
	        Tokenizer tokenizer = new TokenizerME(model);


	            double[] outcome = categorizer.categorize(tokenizer.tokenize(s));
	            System.out.print("{ action: '" + categorizer.getBestCategory(outcome) + "', args: { ");

	            String[] tokens = tokenizer.tokenize(s);
	            for (NameFinderME nameFinderME : nameFinderMEs) {
	                Span[] spans = nameFinderME.find(tokens);
	                String[] names = Span.spansToStrings(spans, tokens);
	                for (int i = 0; i < spans.length; i++) {
	                    if(i > 0) { System.out.print(", "); }
	                    System.out.print(spans[i].getType() + ": '" + names[i] + "' ");
	                }
	            }
	            System.out.println("} }");
	            System.out.print(">");

	        
	    }

	}

