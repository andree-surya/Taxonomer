package waffle.task;

import java.io.File;
import java.io.IOException;

import waffle.nbayes.NaiveBayesClassifier;

public class ClassifyTask {

    public static void main(String[] args) throws IOException {
        
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        classifier.loadModelFromFile(new File("classifier-model.xml"));
    }
}
