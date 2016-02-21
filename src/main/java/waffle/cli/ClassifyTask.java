package waffle.cli;

import java.net.URL;

import waffle.core.HTMLDocument;
import waffle.nbayes.NBayesClassifier;
import waffle.nbayes.NBayesClassifierIO;
import waffle.nbayes.NBayesClassifierResult;

public class ClassifyTask implements Task {
    public static final String COMMAND = "classify";

    private URL documentUrl;
    
    public ClassifyTask(URL documentUrl) {
        this.documentUrl = documentUrl;
    }
    
    public void execute() throws Exception {
        
        NBayesClassifier classifier = NBayesClassifierIO.readFromDefaultFile();
        HTMLDocument document = new HTMLDocument(documentUrl); 
        
        NBayesClassifierResult result = (NBayesClassifierResult) classifier.classify(document);
        System.out.println("Matched category: " + result.getMatchedCategory());
        
        for (String category : result.getCategorySet()) {
            System.out.printf("Score for %s: %f\n", category, result.getScoreForCategory(category));
        }
    }
}
