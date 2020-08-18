package org.heigit.ors.weightaugmentation;

import com.carrotsearch.hppc.IntDoubleHashMap;
import java.util.Collection;
import org.heigit.ors.exceptions.AugmentationStorageException;

/**
 * This class stores augmentations. It consists of a HashMap storing internal edge ids and their weight factor.
 */
public class AugmentationStorage {
  public static final int MAX_AUGMENTATIONS = 50000;
  private final IntDoubleHashMap augmentations;
  private double minAugmentationWeight = 1.0;

  /**
   * Initializes new storage.
   */
  AugmentationStorage() {
    this.augmentations = new IntDoubleHashMap();
  }

  /**
   * Applies given augmentation to the store by multiplying the new weight factor onto the existing.
   * @param edge internal edge id
   * @param weight weight factor
   */
  public void applyAugmentation(int edge, double weight) throws AugmentationStorageException {
    augmentations.put(edge, this.get(edge) * weight);
    if (augmentations.size() > MAX_AUGMENTATIONS) {
      throw new AugmentationStorageException(String.format("Augmentations exceeded the maximum number of edges: %s > %s", augmentations.size(), MAX_AUGMENTATIONS));
    }
    minAugmentationWeight = Math.min(minAugmentationWeight, weight);
  }

  /**
   * Applies given augmentation to all given edges.
   * @param edges internal edge ids
   * @param weight weight factor
   */
  public void applyAllAugmentation(Collection<Integer> edges, double weight) throws AugmentationStorageException {
    for (int edge: edges) {
      applyAugmentation(edge, weight);
    }
  }

  /**
   * Returns the stored augmentation for a given internal edge id.
   * @param edge internal edge id
   * @return stored weight factor or 1.0 as default
   */
  public double get(int edge) {
    return augmentations.getOrDefault(edge, 1.0);
  }

  /**
   * Returns the minimum augmentation.
   * @return minimum weight or 1.0
   */
  public double getMinAugmentationWeight() {
    return minAugmentationWeight;
  }
}
