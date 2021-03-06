package com.english.englishwords.app.algo;

/**
 * Created by Bogdan on 7/19/2014.
 * Algorithm is taken from:
 * http://en.wikipedia.org/wiki/Levenshtein_distance#Iterative_with_two_matrix_rows
 */
public class LevenshteinDistanceCalculator implements DistanceCalculatorInterface {
  public int GetDistance(String a, String b) {
    // degenerate cases
    if (a.equals(b)) return 0;
    if (a.length() == 0) return b.length();
    if (b.length() == 0) return a.length();

    // create two work vectors of integer distances
    int[] v0 = new int[b.length() + 1];
    int[] v1 = new int[b.length() + 1];

    // initialize v0 (the previous row of distances)
    // this row is A[0][i]: edit distance for an empty s
    // the distance is just the number of characters to delete from t
    for (int i = 0; i < v0.length; i++)
      v0[i] = i;

    for (int i = 0; i < a.length(); i++) {
      // calculate v1 (current row distances) from the previous row v0

      // first element of v1 is A[i+1][0]
      //   edit distance is delete (i+1) chars from s to match empty t
      v1[0] = i + 1;

      // use formula to fill in the rest of the row
      for (int j = 0; j < b.length(); j++) {
        int cost = (a.charAt(i) == b.charAt(j)) ? 0 : 1;
        v1[j + 1] = java.lang.Math.min(
            java.lang.Math.min(v1[j] + 1, v0[j + 1] + 1),
            v0[j] + cost);
      }

      // copy v1 (current row) to v0 (previous row) for next iteration
      for (int j = 0; j < v0.length; j++)
        v0[j] = v1[j];
    }

    return v1[b.length()];
  }
}
