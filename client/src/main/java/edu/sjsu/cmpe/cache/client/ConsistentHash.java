package edu.sjsu.cmpe.cache.client;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class ConsistentHash<CacheServiceInterface> {

  private final HashFunction hashFunction;
  //private final int numberOfReplicas;
  private final SortedMap<Integer, CacheServiceInterface> circle =
    new TreeMap<Integer, CacheServiceInterface>();
  private static int i = 0;

  public ConsistentHash(HashFunction hashFunction,
    Collection<CacheServiceInterface> nodes) {

    this.hashFunction = Hashing.md5();
    //this.numberOfReplicas = numberOfReplicas;

    for (CacheServiceInterface node : nodes) {
      add(node);
    }
  }

  public void add(CacheServiceInterface node) {
    //for (int i = 0; i < numberOfReplicas; i++) {
      circle.put(hashFunction.hashInt(i).hashCode(), node);
      i++;
    //}
  }

  public void remove(CacheServiceInterface node) {
    //for (int i = 0; i < numberOfReplicas; i++) {
      circle.remove(hashFunction.hashInt(i));
      i--;
    //}
  }

  public CacheServiceInterface get(int key) {
    if (circle.isEmpty()) {
      return null;
    }
    int hash = hashFunction.hashInt(key % i).hashCode();
    if (!circle.containsKey(hash)) {
      SortedMap<Integer, CacheServiceInterface> tailMap =
        circle.tailMap(hash);
      hash = tailMap.isEmpty() ?
             circle.firstKey() : tailMap.firstKey();
    }
    return circle.get(hash);
  } 

}

