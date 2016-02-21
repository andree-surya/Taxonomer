package waffle.nbayes;

import java.util.Map;
import java.util.Set;

import waffle.core.ClassifierResult;

public class NBayesClassifierResult implements ClassifierResult {

    private String matchedCategory;
    private Map<String, Float> scoresByCategories;
    
    public NBayesClassifierResult(Map<String, Float> scoresByCategories) {
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
    
    public float getScoreForCategory(String category) {
        return scoresByCategories.get(category);
    }
    
    private String getCategoryWithBestScore() {
        Map.Entry<String, Float> bestScoreEntry = null;
        
        for (Map.Entry<String, Float> scoreEntry : scoresByCategories.entrySet()) {
            
            if (bestScoreEntry == null || bestScoreEntry.getValue() < scoreEntry.getValue() ) {
                bestScoreEntry = scoreEntry;
            }
        }
        
        return bestScoreEntry.getKey();
    }
}
