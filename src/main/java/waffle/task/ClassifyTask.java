package waffle.task;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import waffle.core.HTMLDocument;
import waffle.nbayes.NBayesClassifier;
import waffle.nbayes.NBayesClassifierIO;
import waffle.nbayes.NBayesClassifierResult;

public class ClassifyTask {

    public static void main(String[] args) throws IOException {
        
        if (args.length < 1) {
            throw new IllegalArgumentException("Please provide the URL to the document to be classified.");
        }
        
        HTMLDocument document = new HTMLDocument(new URL(args[0])); 
        NBayesClassifier classifier = NBayesClassifierIO.read(new File("classifier-model.xml"));
        
        NBayesClassifierResult result = (NBayesClassifierResult) classifier.classify(document);
        System.out.println("Matched category: " + result.getMatchedCategory());
        
        for (String category : result.getCategorySet()) {
            System.out.printf("Score for %s: %f\n", category, result.getScoreForCategory(category));
        }
    }
}
