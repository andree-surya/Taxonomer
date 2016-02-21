package waffle.cli;

import java.io.File;
import java.util.List;

import waffle.core.Document;
import waffle.core.DocumentIO;
import waffle.nbayes.NBayesClassifier;
import waffle.nbayes.NBayesClassifierBuilder;
import waffle.nbayes.NBayesClassifierIO;

public class TrainTask implements Task {
    public static final String COMMAND = "train";
    
    private File trainingSetFile;
    
    public TrainTask(File trainingSetFile) {
        this.trainingSetFile = trainingSetFile;
    }
    
    public void execute() throws Exception {

        List<Document> trainingDocuments = DocumentIO.read(trainingSetFile);
        NBayesClassifierBuilder classifierBuilder = new NBayesClassifierBuilder();

        for (Document document : trainingDocuments) {
            System.out.println("Processing " + document.getUrl() + " ...");
            classifierBuilder.addTokensFromDocument(document);
        }

        NBayesClassifier classifier = classifierBuilder.build();
        NBayesClassifierIO.writeToDefaultFile(classifier);

        System.out.println("Classifier model successfully generated.");
    }
}
