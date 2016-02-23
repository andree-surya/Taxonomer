package taxonomer.nbayes;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.junit.Test;

import taxonomer.core.ClassifierResult;
import taxonomer.core.Document;
import taxonomer.core.DocumentIO;
import taxonomer.nbayes.NBayesClassifier;
import taxonomer.nbayes.NBayesClassifierBuilder;

public class NBayesClassifierTest {

    @Test
    public void smokeTest() throws Exception {
        
        List<Document> trainingDocuments = readDocuments("/training-set.xml");
        List<Document> testingDocuments = readDocuments("/testing-set.xml");
        
        NBayesClassifier classifier = buildClassifier(trainingDocuments);
        float accuracy = testClassifier(classifier, testingDocuments);
        
        System.out.printf(
                "Smoke test finished. Accuracy: %.2f%%\n\n", accuracy * 100);
    }
    
    private List<Document> readDocuments(String resourceName) throws IOException, URISyntaxException {
        
        URL resourceUrl = getClass().getResource(resourceName);
        File resourceFile = new File(resourceUrl.toURI());
        
        return DocumentIO.read(resourceFile);
    }
    
    private NBayesClassifier buildClassifier(List<Document> trainingDocuments) {
        
        NBayesClassifierBuilder classifierBuilder = new NBayesClassifierBuilder();
        System.out.printf("Building classifier with %d training documents ...\n", trainingDocuments.size());
        
        for (int i = 0; i < trainingDocuments.size(); i++) {
            Document document = trainingDocuments.get(i);
            
            System.out.printf(
                    "Processing document #%02d <%s> %s ...\n", 
                    i + 1, document.getCategory(), document.getUrl());
            
            classifierBuilder.addTokensFromDocument(document);
        }

        return classifierBuilder.build();
    }
    
    private float testClassifier(NBayesClassifier classifier, List<Document> testingDocuments) {
        System.out.printf("Testing classifier with %d test documents ...\n", testingDocuments.size());
        
        int correctMatchCount = 0;
        
        for (int i = 0; i < testingDocuments.size(); i++) {
            Document document = testingDocuments.get(i);
            
            System.out.printf("Testing document #%02d <%s> %s ... ", 
                    i + 1, document.getCategory(), document.getUrl());
            
            ClassifierResult result = classifier.classify(document);
            String matchedCategory = result.getMatchedCategory();
            
            if (document.getCategory().equals(matchedCategory)) {
                
                System.out.println("OK");
                correctMatchCount++;
                
            } else {
                System.out.printf("NG (%s)\n", matchedCategory);
            }
        }
        
        return correctMatchCount / (float) testingDocuments.size();
    }
}
