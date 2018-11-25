package nlp.intent.toolkit;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class SentText {
	
	 public static void main(String args[]) throws Exception { 
		   

	Charset charset = Charset.forName("UTF-8");			
	File file=new File("./models/en-sent.train");
	//ObjectStream<String> lineStream =new PlainTextByLineStream(new FileInputStream(file), charset);
	 ObjectStream<String> lineStream = new PlainTextByLineStream(new MarkableFileInputStreamFactory(file), "UTF-8");
	ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

	SentenceModel model=null;

	try {
	  model = SentenceDetectorME.train("en", sampleStream, true, null, TrainingParameters.defaultParams());
	}
	finally {
	  sampleStream.close();
	}

	OutputStream modelOut = null;
	try {
	  modelOut = new BufferedOutputStream(new FileOutputStream("D:\\a.txt"));
	  model.serialize(modelOut);
	} finally {
	  if (modelOut != null) 
	     modelOut.close();      
	}
}
}
