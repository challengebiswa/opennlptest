package nlp.intent.toolkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.doccat.DoccatFactory;
import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.TrainingParameters;

public class FeedClassifierTrainer {



 private static DoccatModel model = null;

 public static void main(String[] args) {
  System.out.println("Model training started");
  //to train the model
 new FeedClassifierTrainer().train();
  //testing purpose
  String content = "I am traveling from delhi to mumbai";
  try {
   //test the model
   new FeedClassifierTrainer().test(content);
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 /**
  * Training the models
  */
 public void train() {
  // model name you define your own name for the model
  String onlpModelPath = "./models/en-doccat.bin";
  // training data set
  String trainingDataFilePath = "E:\\projects\\Nlp\\nlp-intent-toolkit-master\\nlp-intent-toolkit-master\\example\\weather\\train\\feeed.txt";

  InputStream dataInputStream = null;
  try {
   // Read training data file
   dataInputStream = new FileInputStream(trainingDataFilePath);
   // Read each training instance
   File file=new File(trainingDataFilePath);
   ObjectStream lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), "UTF-8");
   // making sample Stream to train
   ObjectStream sampleStream = new DocumentSampleStream(lineStream);
   TrainingParameters trainingParams = new TrainingParameters();
   trainingParams.put(TrainingParameters.ITERATIONS_PARAM, 10);
   trainingParams.put(TrainingParameters.CUTOFF_PARAM, 0);

   // Calculate the training model "en" means english, sampleStream is the training data, 2 cutoff, 300 iterations
   model = DocumentCategorizerME.train("en", sampleStream, trainingParams, new DoccatFactory());
  } catch (IOException e) {
   
  } finally {
   if (dataInputStream != null) {
    try {
     dataInputStream.close();
    } catch (IOException e) {
    
    }
   }
  }


 // Now we are writing the calculated model to a file in order to use the
 // trained classifier in production

try {
   if (model != null) {
  //saving the file 
    model.serialize(new FileOutputStream(onlpModelPath));
   }
  } catch (IOException e) {
   
  }
 }

 /*
  * Now we call the saved model and test it
  * Give it a new text document and the expected category
  */
 public void test(String text) throws IOException {
  String classificationModelFilePath = "./models/en-doccat.bin";
  DocumentCategorizerME classificationME =
    new DocumentCategorizerME(
      new DoccatModel(
        new FileInputStream(
          classificationModelFilePath)));
  String documentContent = text;
  InputStream modelIn = new FileInputStream("./models/en-token.bin");
  TokenizerModel model = new TokenizerModel(modelIn);
  Tokenizer tokenizer = new TokenizerME(model);
   double[] classDistribution = classificationME.categorize(tokenizer.tokenize(documentContent));
  // get the predicted model
  String predictedCategory = classificationME.getBestCategory(classDistribution);
  System.out.println("Model prediction : " + predictedCategory);
  try {
	new PosTaggerExample().getPos("Feed low traffic in kolkata road");
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  
  
 }
}

