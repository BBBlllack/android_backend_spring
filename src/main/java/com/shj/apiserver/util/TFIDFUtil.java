package com.shj.apiserver.util;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TFIDFUtil {

    public static double[][] calculateTF(List<List<String>> documents) {
        int docSize = documents.size();
        int wordSize = getWordSize(documents);
        double[][] tfMatrix = new double[docSize][wordSize];

        for (int i = 0; i < docSize; i++) {
            List<String> document = documents.get(i);
            Map<String, Integer> wordCountMap = new HashMap<>();

            // 统计每个单词在当前文档中的出现次数
            for (String word : document) {
                wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
            }

            // 计算 TF 值
            for (int j = 0; j < wordSize; j++) {
                String word = getWordAtPosition(j, documents);
                int count = wordCountMap.getOrDefault(word, 0);
                tfMatrix[i][j] = (double) count / document.size();
            }
        }

        return tfMatrix;
    }

    public static double[] calculateIDF(List<List<String>> documents) {
        int docSize = documents.size();
        int wordSize = getWordSize(documents);
        double[] idfArray = new double[wordSize];

        for (int i = 0; i < wordSize; i++) {
            String word = getWordAtPosition(i, documents);
            int docCount = 0;

            // 计算包含当前单词的文档数
            for (List<String> document : documents) {
                if (document.contains(word)) {
                    docCount++;
                }
            }

            idfArray[i] = Math.log((double) docSize / (1 + docCount));
        }

        return idfArray;
    }

    public static double[][] calculateTFIDF(double[][] tfMatrix, double[] idfArray) {
        int docSize = tfMatrix.length;
        int wordSize = tfMatrix[0].length;
        double[][] tfidfMatrix = new double[docSize][wordSize];

        for (int i = 0; i < docSize; i++) {
            for (int j = 0; j < wordSize; j++) {
                tfidfMatrix[i][j] = tfMatrix[i][j] * idfArray[j];
            }
        }

        return tfidfMatrix;
    }

    private static int getWordSize(List<List<String>> documents) {
        List<String> allWords = new ArrayList<>();
        for (List<String> document : documents) {
            allWords.addAll(document);
        }
        return (int) allWords.stream().distinct().count();
    }

    private static String getWordAtPosition(int position, List<List<String>> documents) {
        List<String> allWords = new ArrayList<>();
        for (List<String> document : documents) {
            allWords.addAll(document);
        }
        return allWords.stream().distinct().sorted().skip(position).findFirst().orElse("");
    }
}
