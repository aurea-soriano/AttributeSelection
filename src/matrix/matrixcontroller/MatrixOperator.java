/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package matrix.matrixcontroller;

import matrix.distancematrix.DistanceMatrix;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import matrix.pointsmatrix.CsvMatrix;
import matrix.pointsmatrix.DenseMatrix;
import matrix.pointsmatrix.DenseVector;
import matrix.pointsmatrix.AbstractMatrix;
import matrix.pointsmatrix.TxtMatrix;
import matrix.pointsmatrix.AbstractVector;

/**
 *
 * @author Renato R O da Silva
 */
public class MatrixOperator {

    public static DenseMatrix joinPointsMatrix(DenseMatrix inputMatrix1, DenseMatrix inputMatrix2) {

        ArrayList<String> inputAttributes1 = MatrixOperator.getAttributes(inputMatrix1);
        ArrayList<String> inputAttributes2 = MatrixOperator.getAttributes(inputMatrix2);

        ArrayList<String> intersectionAttributes = MatrixOperator.getAttributesIntersection(inputAttributes1, inputAttributes2);
        if (intersectionAttributes.isEmpty()) {
            return inputMatrix1;
        }

        DenseMatrix splitedMatrix1 = MatrixOperator.splitPointsMatrix(inputMatrix1, inputMatrix1.getIds(), intersectionAttributes);
        DenseMatrix splitedMatrix2 = MatrixOperator.splitPointsMatrix(inputMatrix2, inputMatrix2.getIds(), intersectionAttributes);

        DenseMatrix resultMatrix = new DenseMatrix();

        for (int i = 0; i < splitedMatrix1.getRowCount(); i++) {
            AbstractVector elementVector = splitedMatrix1.getRow(i);
            resultMatrix.addRow(elementVector);
        }

        for (int i = 0; i < splitedMatrix2.getRowCount(); i++) {
            AbstractVector elementVector = splitedMatrix2.getRow(i);
            resultMatrix.addRow(elementVector);
        }

        resultMatrix.setAttributes(intersectionAttributes);

        return resultMatrix;
    }

    /**
     *
     * @param inputMatrix
     * @param selectedElements
     * @return Return a new matrix, with only the selected elements of the
     * original one
     */
    public static DenseMatrix splitPointsMatrix(DenseMatrix inputMatrix, ArrayList<String> selectedItems, ArrayList<String> selectedAttributes) {

        ArrayList<String> newSelectedAttributes = new ArrayList<>();
        for (int i = 0; i < selectedAttributes.size(); i++) {
            if (!selectedAttributes.get(i).equals("Index") &&  !selectedAttributes.get(i).equals("Id") && !selectedAttributes.get(i).equals("Class")) {
                newSelectedAttributes.add(selectedAttributes.get(i));
            }
        }
        if (selectedItems.size() < 2) {
            return null;
        }

        int selectedElementsIndices[] = new int[selectedItems.size()];
        ArrayList<String> oldIds = inputMatrix.getIds();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedElementsIndices[i] = oldIds.indexOf(selectedItems.get(i));
        }

        int selectedAttributesIndices[] = new int[newSelectedAttributes.size()];
        ArrayList<String> oldAttributes = MatrixOperator.getAttributes(inputMatrix);
        for (int i = 0; i < newSelectedAttributes.size(); i++) {

            selectedAttributesIndices[i] = oldAttributes.indexOf(newSelectedAttributes.get(i));
        }

        //Arrays.sort(selectedElementsIndices);
        DenseMatrix newMatrix = new DenseMatrix();
        for (int i = 0; i < selectedElementsIndices.length; i++) {

            String newId = inputMatrix.getIds().get(selectedElementsIndices[i]);
            float newClassData = inputMatrix.getClassData()[selectedElementsIndices[i]];
            float elementValues[] = new float[newSelectedAttributes.size()];

            for (int j = 0; j < newSelectedAttributes.size(); j++) {
                if (selectedAttributesIndices[j] != -1) {
                    float elementValue = inputMatrix.getRow(selectedElementsIndices[i]).getValue(selectedAttributesIndices[j]);
                    elementValues[j] = elementValue;
                }
            }
            newMatrix.addRow(new DenseVector(elementValues, newId, newClassData));
        }

        newMatrix.setAttributes(newSelectedAttributes);
        return newMatrix;

    }

    /**
     *
     * @param inputMatrix
     * @param selectedElements
     * @return Return a new matrix, with only the selected elements of the
     * original one
     */
    public static TxtMatrix splitPointsMatrix(TxtMatrix inputMatrix, ArrayList<String> selectedItems, ArrayList<String> selectedAttributes) {
        if (selectedItems.size() < 2) {
            return null;
        }

        int selectedElementsIndices[] = new int[selectedItems.size()];
        ArrayList<String> oldIds = inputMatrix.getIds();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedElementsIndices[i] = oldIds.indexOf(selectedItems.get(i));
        }

        int selectedAttributesIndices[] = new int[selectedAttributes.size()];
        ArrayList<String> oldAttributes = MatrixOperator.getAttributes(inputMatrix);
        for (int i = 0; i < selectedAttributes.size(); i++) {
            selectedAttributesIndices[i] = oldAttributes.indexOf(selectedAttributes.get(i));
        }

        Arrays.sort(selectedElementsIndices);
        TxtMatrix newMatrix = new TxtMatrix();
        for (int i = 0; i < selectedElementsIndices.length; i++) {

            String newId = inputMatrix.getIds().get(selectedElementsIndices[i]);
            float newClassData = inputMatrix.getClassData()[selectedElementsIndices[i]];
            float elementValues[] = new float[selectedAttributes.size()];

            for (int j = 0; j < selectedAttributes.size(); j++) {
                float elementValue = inputMatrix.getRow(selectedElementsIndices[i]).getValue(selectedAttributesIndices[j]);
                elementValues[j] = elementValue;
            }
            newMatrix.addRow(new DenseVector(elementValues, newId, newClassData));
        }

        newMatrix.setAttributes(selectedAttributes);
        return newMatrix;

    }

    /**
     *
     * @param inputMatrix
     * @param selectedElements
     * @return Return a new matrix, with only the selected elements of the
     * original one
     */
    public static CsvMatrix splitPointsMatrix(CsvMatrix inputMatrix, ArrayList<String> selectedItems, ArrayList<String> selectedAttributes) {
        if (selectedItems.size() < 2) {
            return null;
        }

        int selectedElementsIndices[] = new int[selectedItems.size()];
        ArrayList<String> oldIds = inputMatrix.getIds();
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedElementsIndices[i] = oldIds.indexOf(selectedItems.get(i));
        }

        int selectedAttributesIndices[] = new int[selectedAttributes.size()];
        ArrayList<String> oldAttributes = MatrixOperator.getAttributes(inputMatrix);
        for (int i = 0; i < selectedAttributes.size(); i++) {
            selectedAttributesIndices[i] = oldAttributes.indexOf(selectedAttributes.get(i));
        }

        Arrays.sort(selectedElementsIndices);
        CsvMatrix newMatrix = new CsvMatrix();
        for (int i = 0; i < selectedElementsIndices.length; i++) {

            String newId = inputMatrix.getIds().get(selectedElementsIndices[i]);
            float newClassData = inputMatrix.getClassData()[selectedElementsIndices[i]];
            float elementValues[] = new float[selectedAttributes.size()];

            for (int j = 0; j < selectedAttributes.size(); j++) {
                float elementValue = inputMatrix.getRow(selectedElementsIndices[i]).getValue(selectedAttributesIndices[j]);
                elementValues[j] = elementValue;
            }
            newMatrix.addRow(new DenseVector(elementValues, newId, newClassData));
        }

        newMatrix.setAttributes(selectedAttributes);
        return newMatrix;

    }

    public static DistanceMatrix splitDistanceMatrix(DistanceMatrix inputMatrix, ArrayList<String> selectedItems) {
        if (selectedItems.size() < 2) {
            return null;
        }


        int selectedIndices[] = new int[selectedItems.size()];
        ArrayList<String> oldIds = inputMatrix.getIds();

        for (int i = 0; i < selectedItems.size(); i++) {
            selectedIndices[i] = oldIds.indexOf(selectedItems.get(i));
        }


        Arrays.sort(selectedIndices);
        ArrayList<String> newIds = new ArrayList();
        float newCdata[] = new float[selectedIndices.length];

        DistanceMatrix newMatrix = new DistanceMatrix(selectedIndices.length);
        for (int i = 0; i < selectedIndices.length; i++) {
            newIds.add(inputMatrix.getIds().get(selectedIndices[i]));
            newCdata[i] = inputMatrix.getClassData()[selectedIndices[i]];

            for (int j = i + 1; j < selectedIndices.length; j++) {
                float distance = inputMatrix.getDistance(selectedIndices[i], selectedIndices[j]);
                newMatrix.setDistance(i, j, distance);
            }
        }
        newMatrix.setClassData(newCdata);
        newMatrix.setIds(newIds);

        return newMatrix;
    }

    public static ArrayList<String> getAttributesIntersection(ArrayList<String> attList1, ArrayList<String> attList2) {
        ArrayList<String> attResult = new ArrayList();

        for (String attribute : attList1) {
            if (attList2.indexOf(attribute) != -1) {
                attResult.add(attribute);
            }
        }

        return attResult;
    }

    public static ArrayList<String> getAttributes(AbstractMatrix pointsMatrix) {
        ArrayList<String> returnList = new ArrayList<String>();

        int dimensionsCount = pointsMatrix.getDimensions();
        if (!pointsMatrix.getAttributes().isEmpty()) {
            returnList = pointsMatrix.getAttributes();
        } else {
            for (int i = 0; i < dimensionsCount; i++) {
                returnList.add("Attribute " + i);
            }
        }

        return returnList;
    }
}
