public class GregorianDate {
    public static void main(String[] args) {
        Gregorian cal1 = new Gregorian(2004, 6,24);

        System.out.println(cal1.getYear());
        System.out.println(cal1.getMonth());
    }
}

class  Gregorian {
    private int year;
    private int month;
    private int day;

    Gregorian() {
        year = 0;
        month = 0;
        day = 0;
    }

    Gregorian(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void addDays(int days) {

    }

    public void subtractDays(int days) {

    }

    public boolean isLeapYear() {
        return isLeapYear(year);
    }

    public void printShortDate() {

    }

    public void printLongDate() {

    }

    // complete
    public int getMonth() {
        return month;
    }

    // complete
    public int getYear() {
        return year;
    }

    // complete
    public int getDayOfMonth() {
        return day;
    }

    // complete
    private boolean isLeapYear(int year) {
        if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) {
            return true;
        }
        return false;
    }

    // complete
    private int getNumberOfDaysInMonth(int year, int month) { // complete
        String monthName = getMonthName(month);

        int days = 0;

        switch (monthName) {
            case "January":
            case "March":
            case "May":
            case "July":
            case "August":
            case "October":
            case "December":
                days = 31;
                break;
            case "April":
            case "June":
            case "September":
            case "November":
                days = 30;
                break;
            case "February":
                days = isLeapYear(year) ? 29 : 28;
                break;
        }
        return days;
    }

    // complete
    private int getNumberOfDaysInYear(int year) { // complete
        return isLeapYear(year) ? 366 : 365;
    }

    // complete
    private String getMonthName(int month) {
        final String[] MONTH_NAMES = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };
        return MONTH_NAMES[month - 1];
    }
}

