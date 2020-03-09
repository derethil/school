public class JulianDate extends Date {
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

    @Override
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

    // complete
    private static boolean isLeapYear(int year) {
        if (year % 4 == 0) {
            return true;
        }
        return false;
    }
}

