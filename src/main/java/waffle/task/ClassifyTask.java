package waffle.task;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import waffle.core.HTMLDocument;
import waffle.nbayes.NaiveBayesClassifier;

public class ClassifyTask {

    public static void main(String[] args) throws IOException {
        
        if (args.length < 1) {
            throw new IllegalArgumentException("Please provide the URL to the document to be classified.");
        }
        
        HTMLDocument document = new HTMLDocument(new URL(args[0])); 
        
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.loadModelFromFile(new File("classifier-model.xml"));
        
        String category = classifier.classify(document);
        System.out.println("Document category: " + category);
    }
}
