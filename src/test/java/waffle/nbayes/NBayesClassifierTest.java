package waffle.nbayes;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import waffle.core.ClassifierResult;
import waffle.core.Document;
import waffle.core.DocumentIO;

public class NBayesClassifierTest {

    @Test
    public void smokeTest() throws Exception {
        
        URL trainingSetUrl = getClass().getResource("/training-set.xml");
        URL testingSetUrl = getClass().getResource("/testing-set.xml");
        
        File trainingSetFile = new File(trainingSetUrl.toURI());
        File testingSetFile = new File(testingSetUrl.toURI());
        
        List<Document> trainingDocuments = DocumentIO.read(trainingSetFile);
        List<Document> testingDocuments = DocumentIO.read(testingSetFile);

        NBayesClassifierBuilder classifierBuilder = new NBayesClassifierBuilder();
        System.out.printf("Building classifier with %d training documents ...\n", trainingDocuments.size());
        
        for (Document document : trainingDocuments) {
            System.out.printf("Training with <%s> %s ...\n", document.getCategory(), document.getUrl());
            
            classifierBuilder.addTokensFromDocument(document); 
        }

        NBayesClassifier classifier = classifierBuilder.build();
        System.out.printf("Testing classifier with %d test documents ...\n", testingDocuments.size());
        
        int truePositiveCount = 0;
        
        for (Document document : testingDocuments) {
            
            System.out.printf("Testing against <%s> %s ... ", document.getCategory(), document.getUrl());
            
            ClassifierResult result = classifier.classify(document);
            String matchedCategory = result.getMatchedCategory();
            
            if (document.getCategory().equals(matchedCategory)) {
                
                System.out.println("OK");
                truePositiveCount++;
                
            } else {
                System.out.printf("NG (%s)\n", matchedCategory);
            }
        }
        
        System.out.printf(
                "Smoke test finished. True positive rate: %.2f%%\n\n", 
                truePositiveCount * 100.0f / testingDocuments.size());
    }

}
