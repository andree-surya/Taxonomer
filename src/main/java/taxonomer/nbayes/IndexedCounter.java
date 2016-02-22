package taxonomer.nbayes;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IndexedCounter {

    private Map<String, Integer> countersByKeys;
    
    public IndexedCounter() {
        countersByKeys = new HashMap<String, Integer>();
    }
    
    public void incrementCountForKey(String key) {
        Integer count = countersByKeys.get(key);
        
        if (count == null) {
            count = 0;
        }
        
        countersByKeys.put(key, count + 1);
    }
    
    public int getCountForKey(String key) {
        
        Integer count = countersByKeys.get(key);
        
        if (count == null) {
            return 0;
        }
        
        return count;
    }
    
    public Set<String> getKeySet() {
        return countersByKeys.keySet();
    }
    
    public int getNumberOfKeys() {
        return countersByKeys.size();
    }
    
    public int getTotalCount() {
        int totalCount = 0;
        
        for (Integer count : countersByKeys.values()) {
            totalCount += count;
        }
        
        return totalCount;
    }
}
