package cmpt276.as3.assignment3.model;

//Data from user is store in here and can be accessed through Singleton use

public class OptionsData {
    private int row;
    private int column;
    private int mines;
    private boolean eraseButton;

    private static OptionsData instance;
    private OptionsData() {}

    // Singleton Code
    public static OptionsData getInstance() {
        if(instance == null) {
            instance = new OptionsData();
        }
        return instance;
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getMines() {
        return mines;
    }

    public void setMines(int mines) {
        this.mines = mines;
    }

    public boolean isEraseButton() {
        return eraseButton;
    }

    public void setEraseButton(boolean eraseButton) {
        this.eraseButton = eraseButton;
    }
}
