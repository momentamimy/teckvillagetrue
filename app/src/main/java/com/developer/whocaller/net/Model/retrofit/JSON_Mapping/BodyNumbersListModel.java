package com.developer.whocaller.net.Model.retrofit.JSON_Mapping;

public class BodyNumbersListModel {
    String[] numbers;

    public String[] getNumbers() {
        return numbers;
    }

    public void setNumbers(String[] numbers) {
        this.numbers = numbers;
    }

    public BodyNumbersListModel(String[] numbers) {
        this.numbers = numbers;
    }
}
