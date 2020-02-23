public class  JulianDate {
    private int year = getYear();
    private int month = getMonth();
    private int day = getDayOfMonth();

    JulianDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

    }

    public void addDays(int days) {

    }

    public void subtractDays(int days) {

    }

    public boolean isLeapYear() {
        return true;
    }

    public void printShortDate() {

    }

    public void printLongDate() {

    }

    public int getMonth() {
        return 0;
    }

    public int getYear() {
        return 0;
    }

    public int getDayOfMonth() {
        return 0;
    }

    private boolean isLeapYear(int year) {
        return true;
    }

    private int getNumberOfDaysInMonth(int year, int month) {
        return 0;
    }

    private int getNumberOfDaysInYear(int year) {
        return 0;
    }

    private String getMonthName(int month) {
        return "";
    }
}

