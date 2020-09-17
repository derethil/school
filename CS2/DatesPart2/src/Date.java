public class Date {
    int year;
    int month;
    int day;

    public void addDays(int days) {
        for (int i = 1; i <= days; i++) {
            if (++day > getNumberOfDaysInMonth(year, month)) {
                day = 1;
                if (++month > 12) {
                    month = 1;
                    year++;
                }
            }
        }
    }

    public void subtractDays(int days) {
        for (int i = 1; i <= days; i++) {
            if (--day < 1) {
                if (--month < 1) {
                    month = 12;
                    year--;
                }
                day = getNumberOfDaysInMonth(year, month);
            }
        }
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

    public int getNumberOfDaysInMonth(int year, int month) { // complete
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

    public boolean isLeapYear(int year) {
        return false;
    }

    public boolean isLeapYear() {
        return isLeapYear(year);
    }


    public int getNumberOfDaysInYear(int year) { // complete
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
