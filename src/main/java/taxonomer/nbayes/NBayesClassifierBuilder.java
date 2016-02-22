package taxonomer.nbayes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import taxonomer.core.Document;

public class NBayesClassifierBuilder {
    
    // Smoothing parameter to prevent zero probability problem.
    private static final double PSEUDOCOUNT = 0.5;
    
    private IndexedCounter documentCounter;
    private IndexedCounter totalTokenCounter;
    
    private Map<String, IndexedCounter> tokenCountersByCategories;

    public NBayesClassifierBuilder() {
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

    public NBayesClassifier build() {
        
        NBayesClassifier classifier = new NBayesClassifier();
        int numberOfCategories = documentCounter.getNumberOfKeys();
        
        double totalDocumentCount = documentCounter.getTotalCount() + numberOfCategories * PSEUDOCOUNT;
        
        for (Map.Entry<String, IndexedCounter> tokenCountersEntry : tokenCountersByCategories.entrySet()) {
            
            String category = tokenCountersEntry.getKey();
            IndexedCounter tokenCounters = tokenCountersEntry.getValue();
            
            IndexedScore tokenScores = new IndexedScore();
            
            double categoricalDocumentCount = documentCounter.getCountForKey(category) + PSEUDOCOUNT;
            tokenScores.setDefaultScore(PSEUDOCOUNT / categoricalDocumentCount);
            
            for (String token : tokenCounters.getKeySet()) {
                
                double categoricalTokenCount = tokenCounters.getCountForKey(category) + PSEUDOCOUNT;
                double categoricalTokenProbability = categoricalTokenCount / categoricalDocumentCount;
                
                double totalTokenCount = totalTokenCounter.getCountForKey(token) + numberOfCategories * PSEUDOCOUNT;
                double overallTokenProbability = totalTokenCount / totalDocumentCount;
                
                tokenScores.setScoreForKey(token, categoricalTokenProbability / overallTokenProbability);
            }
            
            classifier.setTokenScoresForCategory(category, tokenScores);
        }
        
        return classifier;
    }
}
