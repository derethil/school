public class  GregorianDate extends Date {
    GregorianDate() {
        year = 1970;
        month = 1;
        day = 1;

        final double MS_TO_DAY = 0.000000011574074;

        long currTime = System.currentTimeMillis() + java.util.TimeZone.getDefault().getRawOffset();
        int numDaysPassed = (int) (currTime * MS_TO_DAY);
        addDays(numDaysPassed);
    }

    GregorianDate(int year, int month, int day) {
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

    private static boolean isLeapYear(int year) {
        if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) {
            return true;
        }
        return false;
    }
}

