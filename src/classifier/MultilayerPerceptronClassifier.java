/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import weka.core.Instances;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.ThresholdCurve;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.FastVector;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author aurea
 */
public class MultilayerPerceptronClassifier implements Classifier {

    Instances[] trainingSplits;
    Instances[] testingSplits;
    FastVector predictions;
    Lock lock = new java.util.concurrent.locks.ReentrantLock();

    @Override
    public String classify(String dataPath) {
        String resultClassifier = "Multilayer Perceptron \n\n";
        try {

            ConverterUtils.DataSource fonte = new ConverterUtils.DataSource(dataPath);
            Instances data = fonte.getDataSet();
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }
            MultilayerPerceptron perceptron = new MultilayerPerceptron();
            perceptron.buildClassifier(data);

            Evaluation crossVal;
            crossVal = new Evaluation(data);
            crossVal.crossValidateModel(perceptron, data, 10, new Random(1));
            resultClassifier += crossVal.toSummaryString();
            resultClassifier += "\n";

            resultClassifier += crossVal.toClassDetailsString();
            resultClassifier += "\n";
            String confMatrix = "=== Confusion Matrix ===\n";
            confMatrix += "\n";
            confMatrix += "      ";
            double[][] confusionMatrix = crossVal.confusionMatrix();
            for (int c = 0; c < confusionMatrix.length; c++) {
                confMatrix += (c * 1.0) + " ";
            }
            confMatrix += "\n";

            for (int i = 0; i < confusionMatrix.length; i++) {
                confMatrix += (i * 1.0) + " ";
                for (int j = 0; j < confusionMatrix[i].length; j++) {
                    confMatrix += confusionMatrix[i][j] + " ";
                }
                confMatrix += "\n";
            }

            resultClassifier += confMatrix;

        } catch (Exception ex) {
            Logger.getLogger(MultilayerPerceptronClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultClassifier;
    }

    @Override
    public String classify2(String dataPath, int numberThreads) {
        try {
            String resultClassifier = "Multilayer Perceptron \n\n";

            ConverterUtils.DataSource fonte = new ConverterUtils.DataSource(dataPath);
            Instances data = fonte.getDataSet();
            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }

            int numberClasses = data.numClasses();
            // Do 10-split cross validation
            Instances[][] split = crossValidationSplit(data, 10);

            // Separate split into training and testing arrays
            trainingSplits = split[0];
            testingSplits = split[1];
            predictions = new FastVector();

            List<Thread> listThreads = new ArrayList<>();
            numberThreads = (numberThreads > trainingSplits.length) ? trainingSplits.length : numberThreads;
            //if (numberThreads > 10) {
            //    numberThreads = 10;
            //}
            Integer step = (10 / numberThreads);

            // For each training-testing split pair, train and test the classifier
            /*for (int i = 0; i < trainingSplits.length; i++) {
             Evaluation validation = classifyEvaluation(trainingSplits[i], testingSplits[i]);

             predictions.appendElements(validation.predictions());
             }*/
            Integer beginPoint = 0;
            Integer endPoint = 0;
            if (trainingSplits.length < numberThreads) {
                numberThreads = trainingSplits.length;
            }

            for (int i = 1; i <= numberThreads; i++) {
                beginPoint = endPoint + 1;
                endPoint = i * trainingSplits.length / (numberThreads);

                listThreads.add(new MultilayerPerceptronClassifierThread(beginPoint - 1, endPoint - 1, i));
                listThreads.get(listThreads.size() - 1).start();

            }

            for (Thread listThread : listThreads) {
                try {
                    listThread.join();
                } catch (InterruptedException ex) {
                    System.out.print("Join interrupted\n");
                }
            }

            // Calculate overall accuracy of current classifier on all splits
            double accuracy = calculateAccuracy(predictions);
            double roc = calculateROC(predictions);

            resultClassifier += "Accuracy of  Multilayer Perceptron : " + String.format("%.2f%%", accuracy) + "\n";
            resultClassifier += "ROC of  Multilayer Perceptron : " + String.format("%.2f%%", roc) + "\n";

            return resultClassifier;
            //}
        } catch (Exception ex) {

        }
        return null;

    }

    private void calculateEvaluationThreads(Integer beginPoint, Integer endPoint, Integer numberThread) {
        for (int i = beginPoint; i <= endPoint; i++) {
            Evaluation validation = classifyEvaluation(trainingSplits[i], testingSplits[i]);
            lock.lock();
            try {
                predictions.appendElements(validation.predictions());
            } finally {
                lock.unlock();
            }
        }
        // System.out.println(numberThread + " finished.");
    }

    @Override
    public Instances[][] crossValidationSplit(Instances data, int numberOfFolds
    ) {
        Instances[][] split = new Instances[2][numberOfFolds];
        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }
        return split;
    }

    @Override
    public Evaluation classifyEvaluation(Instances trainingSet, Instances testingSet) {
        Evaluation evaluation = null;
        try {
            evaluation = new Evaluation(trainingSet);
            MultilayerPerceptron multilayerPerceptron = new MultilayerPerceptron();
            multilayerPerceptron.buildClassifier(trainingSet);
            evaluation.evaluateModel(multilayerPerceptron, testingSet);

        } catch (Exception ex) {

        }
        return evaluation;

    }

    @Override
    public double calculateAccuracy(FastVector predictions) {

        double correct = 0;
        double incorrect = 0;

        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);

            if (np.predicted() == np.actual()) {
                correct++;
            } else {
                incorrect++;
            }
        }
        return 100 * correct / predictions.size();
    }

    @Override
    public double calculateROC(FastVector predictions) {
        ThresholdCurve tcurve = new ThresholdCurve();
        Instances ins = tcurve.getCurve(predictions);
        return 100 * ThresholdCurve.getROCArea(ins);
    }

    class MultilayerPerceptronClassifierThread extends Thread {

        Integer beginPoint;
        Integer endPoint;
        Integer numberThread;

        public MultilayerPerceptronClassifierThread(Integer beginPoint, Integer endPoint, Integer numberThread) {
            this.beginPoint = beginPoint;
            this.endPoint = endPoint;
            this.numberThread = numberThread;
        }

        @Override
        public void run() {
            calculateEvaluationThreads(this.beginPoint, this.endPoint, this.numberThread);
        }
    }

}
