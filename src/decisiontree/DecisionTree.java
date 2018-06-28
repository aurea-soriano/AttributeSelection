/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.NominalPrediction;
import weka.core.FastVector;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.core.Instances;
import weka.core.Instance;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;

/**
 *
 * @author aurea
 */
public class DecisionTree {

    public String dataPath;

    // public Instances data;
    public DecisionTree(String dP) {
        dataPath = dP;
    }

    public Instances readData() throws Exception {

        DataSource fonte = new DataSource(dataPath);
        Instances data = fonte.getDataSet();

        // seta o atributo classe caso o formato de dados nao contenha essa informacao
        if (data.classIndex() == -1) {
            data.setClassIndex(data.numAttributes() - 1);
        }
        return data;
    }

    public BufferedReader readDataFile() {
        BufferedReader inputReader = null;
        try {
            inputReader = new BufferedReader(new FileReader(dataPath));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + dataPath);
        }

        return inputReader;
    }

    public void printData(Instances data) {

        // Imprime cada instância (linha) dos dados
        for (int i = 0; i < data.numInstances(); i++) {
            Instance atual = data.instance(i);
            System.out.println((i + 1) + ": " + atual + "\n");
        }
    }

    //Cria uma instância da árvore J48 e avalia seu desempenho
    public void decisionTreeJ48Original(Instances data) throws Exception {

        // Cria uma nova instancia da arvore
        J48 tree = new J48();

        // Constrói classificador
        tree.buildClassifier(data);

        // Imprime a arvore
        //System.out.println(tree);
        // System.out.println("*************");
        // Avalia o resultado
        //System.out.println("Avaliacao inicial: \n");
        Evaluation avaliacao;
        avaliacao = new Evaluation(data);
        avaliacao.evaluateModel(tree, data);
        //System.out.println("Instancias corretas: " + avaliacao.correct() + "\n");

        // Avaliacao cruzada (cross-validation)
        //System.out.println("Avaliacao cruzada: \n");
        Evaluation avalCruzada;
        avalCruzada = new Evaluation(data);
        avalCruzada.crossValidateModel(tree, data, 10, new Random(1));
       // System.out.println("Instancias corretas: " + avalCruzada.correct() + "\n");

        // Neste caso ele imprime o equivalente a uma chamada padrão ao algoritmo, como se
        // estivesse usando a interface gráfica
        // System.out.println("Chamada de linha de código: \n");
        String[] options = new String[2];
        options[0] = "-t";
        options[1] = dataPath;
        //System.out.println(Evaluation.evaluateModel(new J48(), options));

    }

    public static Evaluation classify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {
        Evaluation evaluation = new Evaluation(trainingSet);
        model.buildClassifier(trainingSet);
        evaluation.evaluateModel(model, testingSet);
        return evaluation;
    }

    public static double calculateAccuracy(FastVector predictions) {
        double correct = 0;
        for (int i = 0; i < predictions.size(); i++) {
            NominalPrediction np = (NominalPrediction) predictions.elementAt(i);
            if (np.predicted() == np.actual()) {
                correct++;
            }
        }
        return 100 * correct / predictions.size();
    }

    public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
        Instances[][] split = new Instances[2][numberOfFolds];
        for (int i = 0; i < numberOfFolds; i++) {
            split[0][i] = data.trainCV(numberOfFolds, i);
            split[1][i] = data.testCV(numberOfFolds, i);
        }
        return split;
    }

    public J48 getDecissionTree() {
        J48 tree = null;

        try {
            Instances data = readData();
            data.setClassIndex(data.numAttributes() - 1);

            // Do 10-split cross validation
            //Instances[][] split = crossValidationSplit(data, 10);
            // Separate split into training and testing arrays
            // Instances[] trainingSplits = split[0];
            // Instances[] testingSplits = split[1];
            tree = new J48();

            tree.buildClassifier(data);
        } catch (Exception e) {

        }

        return tree;
    }

    public static void main(String[] args) throws Exception {
        DecisionTree decisionTree = new DecisionTree("Iris-2014-09-19 15:12:18.arff");
        System.out.println(decisionTree.getDecissionTree());
        //getDecissionTree("iris.arff");
    }
}
