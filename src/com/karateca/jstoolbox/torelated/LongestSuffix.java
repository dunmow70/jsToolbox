package com.karateca.jstoolbox.torelated;

public class LongestSuffix {

  public static String find(Iterable<String> matches, String filePath) {
    String longest = null;
    int longestLen = 0;

    StringBuilder reversedCurrentFilePath = new StringBuilder(filePath).reverse();

    for (String match : matches) {
      int currentLen = len(match, reversedCurrentFilePath);
      if (currentLen > longestLen) {
        longestLen = currentLen;
        longest = match;
      }
    }

    return longest;
  }

  static int len(String path, StringBuilder reversedCurrentFilePath) {
    StringBuilder reversedPath = new StringBuilder(path).reverse();

    int shortestLen = Math.min(path.length(), reversedCurrentFilePath.length());
    int count = 0;
    while (count < shortestLen) {
      if (reversedPath.charAt(count) != reversedCurrentFilePath.charAt(count)) {
        return count;
      }
      count++;
    }
    return count;
  }
}
