//class Gregorian {
//    public static void main(String[] args) {
//        Gregorian cal1 = new Gregorian(2004, 6,24);
//
//        System.out.println(cal1.getYear());
//        System.out.println(cal1.getMonth());
//        cal1.printLongDate();
//        System.out.println();
//        cal1.printShortDate();
//    }
//}

public class  GregorianDate {
    private int year;
    private int month;
    private int day;

    GregorianDate() {
        year = 0;
        month = 0;
        day = 0;
    }

    GregorianDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void addDays(int days) {
        for (int i = 1; i <= days; i++) {
            if (day++ > getNumberOfDaysInMonth(year, month)) {
                day = 1;
                if (month++ > 12) {
                    month = 1;
                    year++;
                }
            }
        }
    }

    public void subtractDays(int days) {

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
        if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) {
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

