public class GregorianDate extends Date {
    public GregorianDate() {
        year = 1970;
        month = 1;
        day = 1;

        final double MS_TO_DAY = 0.000000011574074;

        long currTime = System.currentTimeMillis() + java.util.TimeZone.getDefault().getRawOffset();
        int numDaysPassed = (int) (currTime * MS_TO_DAY);
        addDays(numDaysPassed);
    }

    public GregorianDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public boolean isLeapYear(int year) {
        if ((year % 4 == 0) && (year % 100 != 0 || year % 400 == 0)) {
            return true;
        }
        return false;
    }
}

