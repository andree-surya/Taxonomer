package waffle.task;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import waffle.core.Document;
import waffle.core.DocumentIO;
import waffle.nbayes.NBayesClassifier;
import waffle.nbayes.NBayesClassifierBuilder;
import waffle.nbayes.NBayesClassifierIO;

public class TrainTask {

    public static void main(String[] args) throws IOException, URISyntaxException {

        URL trainingSetUrl = TrainTask.class.getResource("/training-set.xml");
        File trainingSetFile = new File(trainingSetUrl.toURI());

        List<Document> trainingDocuments = DocumentIO.read(trainingSetFile);
        NBayesClassifierBuilder classifierBuilder = new NBayesClassifierBuilder();

        for (Document document : trainingDocuments) {
            System.out.println("Processing " + document.getUrl() + " ...");
            classifierBuilder.addTokensFromDocument(document);
        }

        NBayesClassifier classifier = classifierBuilder.build();

        File outputFile = new File("classifier-model.xml");
        NBayesClassifierIO.write(outputFile, classifier);

        System.out.println("Classifier model saved in file: " + outputFile.getAbsolutePath());
    }
}
