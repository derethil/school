public class Date {
    int year;
    int month;
    int day;

    public void addDays() {

    }

    public void printShortDate() {
        System.out.printf("%s/%s/%s", month, day, year);
    }

    public void printLongDate() {
        System.out.printf("%s %s, %s", getMonthName(month), day, year);
    }

    public int getMonth() {
        return month;
    }

    public String getMonthName() {
        return getMonthName(month);
    }

    public int getYear() {
        return year;
    }

    public int getDayOfMonth() {
        return day;
    }

    public boolean isLeapYear() {
        return isLeapYear(year);
    }

    static int getNumberOfDaysInMonth(int year, int month) { // complete
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

    static int getNumberOfDaysInYear(int year) { // complete
        return isLeapYear(year) ? 366 : 365;
    }

    static String getMonthName(int month) {
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
