package waffle.nbayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import waffle.core.Document;

public class NaiveBayesClassifierBuilder {
    
    // Smoothing parameter to prevent zero probability problem.
    private static final float PSEUDOCOUNT = 0.5f;
    
    private IndexedCounter documentCounter;
    private IndexedCounter totalTokenCounter;
    
    private Map<String, IndexedCounter> tokenCountersByCategories;

    public NaiveBayesClassifierBuilder() {
        documentCounter = new IndexedCounter();
        totalTokenCounter = new IndexedCounter();
        
        tokenCountersByCategories = new HashMap<String, IndexedCounter>();
    }

    public void addTokensFromDocument(Document document) {
        String documentCategory = document.getCategory();
        
        Set<String> uniqueTokens = new HashSet<String>(document.extractTokens());
        IndexedCounter tokenCounter = tokenCountersByCategories.get(documentCategory);
        
        if (tokenCounter == null) {
            tokenCounter = new IndexedCounter();
            
            tokenCountersByCategories.put(documentCategory, tokenCounter);
        }
        
        for (String token : uniqueTokens) {
            tokenCounter.incrementCountForKey(token);
            totalTokenCounter.incrementCountForKey(token);
        }
        
        documentCounter.incrementCountForKey(documentCategory);
    }

    public NaiveBayesClassifier build() {
        
        NaiveBayesClassifier classifier = new NaiveBayesClassifier();
        int numberOfCategories = documentCounter.getNumberOfKeys();
        
        float totalDocumentCount = documentCounter.getTotalCount() + numberOfCategories * PSEUDOCOUNT;
        
        for (Map.Entry<String, IndexedCounter> tokenCountersEntry : tokenCountersByCategories.entrySet()) {
            
            String category = tokenCountersEntry.getKey();
            IndexedCounter tokenCounters = tokenCountersEntry.getValue();
            
            IndexedScore tokenScores = new IndexedScore();
            
            float categoricalDocumentCount = documentCounter.getCountForKey(category) + PSEUDOCOUNT;
            tokenScores.setDefaultScore(PSEUDOCOUNT / categoricalDocumentCount);
            
            for (String token : tokenCounters.getKeySet()) {
                
                float categoricalTokenCount = tokenCounters.getCountForKey(category) + PSEUDOCOUNT;
                float categoricalTokenProbability = categoricalTokenCount / categoricalDocumentCount;
                
                float totalTokenCount = totalTokenCounter.getCountForKey(token) + numberOfCategories * PSEUDOCOUNT;
                float overallTokenProbability = totalTokenCount / totalDocumentCount;
                
                tokenScores.setScoreForKey(token, categoricalTokenProbability / overallTokenProbability);
            }
            
            classifier.setTokenScoresForCategory(category, tokenScores);
        }
        
        return classifier;
    }
}
