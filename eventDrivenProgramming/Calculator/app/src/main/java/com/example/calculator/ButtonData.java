package com.example.calculator;

public class ButtonData {
    enum ButtonType {
        EVALUATE,
        OPERATOR,
        CLEAR,
        INPUT
    }

    private String buttonText;
    private int column;
    private int row;
    private int size;
    private ButtonType type;

    public ButtonData(String buttonText, int row, int column, int size) {
        this.buttonText = buttonText;
        this.row = row;
        this.column = column;
        this.size = size;
        this.type = ButtonType.INPUT;
    }

    public ButtonData(String buttonText, int row, int column, int size, ButtonType type) {
        this.buttonText = buttonText;
        this.row = row;
        this.column = column;
        this.size = size;
        this.type = type;
    }

    public String getButtonText() { return buttonText; }
    public int getSize() { return size; }
    public int getColumn() { return column; }
    public int getRow() { return row; }
    public ButtonType getType() { return type; }
}
