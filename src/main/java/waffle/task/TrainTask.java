package waffle.task;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import waffle.core.Document;
import waffle.core.TrainingSet;
import waffle.nbayes.NaiveBayesClassifier;
import waffle.nbayes.NaiveBayesClassifierBuilder;

public class TrainTask {

    public static void main(String[] args) throws IOException, URISyntaxException {

        URL trainingSetUrl = TrainTask.class.getResource("/training-set.xml");
        File trainingSetFile = new File(trainingSetUrl.toURI());

        List<Document> trainingDocuments = new TrainingSet(trainingSetFile).readDocuments();
        NaiveBayesClassifierBuilder classifierBuilder = new NaiveBayesClassifierBuilder();

        for (Document document : trainingDocuments) {
            System.out.println("Processing " + document.getUrl() + " ...");
            classifierBuilder.addTokensFromDocument(document);
        }

        NaiveBayesClassifier classifier = classifierBuilder.build();

        File classifierModelFile = new File("classifier-model.xml");
        classifier.saveModelToFile(classifierModelFile);

        System.out.println("Classifier model saved in file: " + classifierModelFile.getAbsolutePath());
    }
}
