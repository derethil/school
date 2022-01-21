public class  JulianDate {
    public static void main(String[] args) {

    }
    private int year;
    private int month;
    private int day;

    JulianDate() {
        year = 1;
        month = 1;
        day = 1;

        final double MS_TO_DAY = 0.000000011574074;
        final int DAYS_TO_EPOCH = 719164;

        addDays(DAYS_TO_EPOCH);
        long currTime = System.currentTimeMillis() + java.util.TimeZone.getDefault().getRawOffset();
        int numDaysPassed = (int) (currTime * MS_TO_DAY);
        addDays(numDaysPassed);
    }

    JulianDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

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

    public boolean isLeapYear() {
        return isLeapYear(year);
    }

    // complete
    public void printShortDate() {
        System.out.printf("%s/%s/%s", month, day, year);
    }

    // complete
    public void printLongDate() {
        System.out.printf("%s %s, %s", getMonthName(month), day, year);
    }

    // complete
    public int getMonth() {
        return month;
    }

    public String getMonthName() {
        return getMonthName(month);
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
    private static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            return true;
        }
        return false;
    }

    // complete
    private static int getNumberOfDaysInMonth(int year, int month) { // complete
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
    private static int getNumberOfDaysInYear(int year) { // complete
        return isLeapYear(year) ? 366 : 365;
    }

    // complete
    private static String getMonthName(int month) {
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

