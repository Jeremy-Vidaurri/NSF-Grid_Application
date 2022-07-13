package com.example.abac;

import androidx.annotation.NonNull;

public class PolicyModel {
    private int PolicyID;
    private String PolicyName;
    private int Size;


    // Constructors

    public PolicyModel(int policyID, String policyName, int size) {
        this.PolicyID = policyID;
        this.PolicyName = policyName;
        this.Size = size;
    }

    public PolicyModel() {
    }

    @NonNull
    @Override
    public String toString() {
        return "PolicyModel{" +
                "PolicyID=" + PolicyID +
                ", PolicyName='" + PolicyName + '\'' +
                ", Size=" + Size +
                '}';
    }

    // Getters & Setters
    public int getPolicyID() {
        return PolicyID;
    }

    public void setPolicyID(int policyID) {
        PolicyID = policyID;
    }

    public String getPolicyName() {
        return PolicyName;
    }

    public void setPolicyName(String policyName) {
        PolicyName = policyName;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

}
