package waffle.task;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import waffle.classifier.NaiveBayesClassifier;
import waffle.classifier.NaiveBayesClassifierBuilder;
import waffle.document.Document;
import waffle.document.TrainingSet;

public class TrainTask {

    public static void main(String[] args) throws IOException, URISyntaxException {

        URL trainingSetUrl = TrainTask.class.getResource("/training-set.xml");
        File trainingSetFile = new File(trainingSetUrl.toURI());

        List<Document> trainingDocuments = new TrainingSet(trainingSetFile).readDocuments();
        NaiveBayesClassifierBuilder classifierBuilder = new NaiveBayesClassifierBuilder();

        for (Document document : trainingDocuments) {
            System.out.println("Extracting tokens from " + document.getUrl() + " ...");
            classifierBuilder.addTokensFromDocument(document);
        }

        NaiveBayesClassifier classifier = classifierBuilder.build();

        File classifierModelFile = new File("classifier-model.xml");
        classifier.saveModelToFile(classifierModelFile);

        System.out.println("Classifier model saved in file: " + classifierModelFile.getAbsolutePath());
    }
}
