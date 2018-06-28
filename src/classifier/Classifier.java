/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package classifier;

import weka.classifiers.Evaluation;
import weka.core.FastVector;
import weka.core.Instances;

/**
 *
 * @author aurea
 */
public interface Classifier {
    
    public String classify(String dataPath);
    public String classify2(String dataPath, int numberThreads);
    public Evaluation classifyEvaluation(Instances trainingSet, Instances testingSet);
    public Instances[][] crossValidationSplit(Instances data, int numberOfFolds);
    public double calculateAccuracy(FastVector predictions);
    public double calculateROC(FastVector predictions);
    
}


