package edu.sjsu.cmpe.cache.client;

import java.util.ArrayList;

import com.google.common.hash.Hashing;

public class Client {

    public static void main(String[] args) throws Exception {
    	
        System.out.println("Starting Cache Client...");
        ArrayList<CacheServiceInterface> cacheList = new ArrayList<CacheServiceInterface>();
        CacheServiceInterface cache1 = new DistributedCacheService("http://localhost:3000");
        CacheServiceInterface cache2 = new DistributedCacheService("http://localhost:3001");
        CacheServiceInterface cache3 = new DistributedCacheService("http://localhost:3002");
        CacheServiceInterface dummy;
       
        cacheList.add(cache1);
        cacheList.add(cache2);
        cacheList.add(cache3);
        ConsistentHash<CacheServiceInterface> consistentHash = new ConsistentHash(Hashing.md5(), cacheList);
        int i = 1;
        
        for (CacheServiceInterface csi : cacheList)
        	 consistentHash.add(csi);
        
        String value;
        String str[] = {"x", "a","b","c","d","e","f","g","h","i","j"};
        for(i=1;i<11;i++){
        dummy = consistentHash.get(i);
        dummy.put(i,str[i]);
        value = dummy.get(i);
        System.out.println("get("+i+") => " + value);
        }
        
        int count = cacheList.size(); 
        
        System.out.println("Existing Cache Client..."+count);
    }

}
