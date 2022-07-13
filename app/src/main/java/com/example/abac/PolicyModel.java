package com.example.abac;

import androidx.annotation.NonNull;

public class PolicyModel {
    private int PolicyID;
    private String PolicyName;
    private int Width;
    private int Length;

    // Constructors

    public PolicyModel(int policyID, String policyName, int width, int length) {
        this.PolicyID = policyID;
        this.PolicyName = policyName;
        this.Width = width;
        this.Length = length;
    }

    public PolicyModel() {
    }

    @NonNull
    @Override
    public String toString() {
        return "PolicyModel{" +
                "PolicyID=" + PolicyID +
                ", PolicyName='" + PolicyName + '\'' +
                ", Width=" + Width +
                ", Length=" + Length +
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

    public int getWidth() {
        return Width;
    }

    public void setWidth(int width) {
        Width = width;
    }

    public int getLength() {
        return Length;
    }

    public void setLength(int length) {
        Length = length;
    }
}
