package com.example.abac;

public class MatrixModel {
    private int policyID;
    private int columnID;
    private int rowID;
    private int value;

    // Constructors

    public MatrixModel(int policyID, int columnID, int rowID, int value) {
        this.policyID = policyID;
        this.columnID = columnID;
        this.rowID = rowID;
        this.value = value;
    }

    public MatrixModel() {
    }

    @Override
    public String toString() {
        return "MatrixModel{" +
                "policyID=" + policyID +
                ", columnID=" + columnID +
                ", rowID=" + rowID +
                ", value=" + value +
                '}';
    }

    //Getters and Setters
    public int getPolicyID() {
        return policyID;
    }

    public int getColumnID() {
        return columnID;
    }

    public int getRowID() {
        return rowID;
    }

    public int getValue() {
        return value;
    }

    public void setPolicyID(int policyID) {
        this.policyID = policyID;
    }

    public void setColumnID(int columnID) {
        this.columnID = columnID;
    }

    public void setRowID(int rowID) {
        this.rowID = rowID;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
