package taxonomer.nbayes;

import java.util.Map;
import java.util.Set;

import taxonomer.core.ClassifierResult;

public class NBayesClassifierResult implements ClassifierResult {

    private String matchedCategory;
    private Map<String, Double> scoresByCategories;
    
    public NBayesClassifierResult(Map<String, Double> scoresByCategories) {
        this.scoresByCategories = scoresByCategories;
    }
    
    public String getMatchedCategory() {
        
        if (matchedCategory == null) {
            matchedCategory = getCategoryWithBestScore();
        }
        
        return matchedCategory;
    }
    
    public Set<String> getCategorySet() {
        return scoresByCategories.keySet();
    }
    
    public double getScoreForCategory(String category) {
        return scoresByCategories.get(category);
    }
    
    private String getCategoryWithBestScore() {
        Map.Entry<String, Double> bestScoreEntry = null;
        
        for (Map.Entry<String, Double> scoreEntry : scoresByCategories.entrySet()) {
            
            if (bestScoreEntry == null || bestScoreEntry.getValue() < scoreEntry.getValue() ) {
                bestScoreEntry = scoreEntry;
            }
        }
        
        return bestScoreEntry.getKey();
    }
}
