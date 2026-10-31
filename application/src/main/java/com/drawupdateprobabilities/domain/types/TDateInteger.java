package com.drawupdateprobabilities.domain.types;

import java.text.SimpleDateFormat;
import java.util.*;

public class TDateInteger implements Comparable<TDateInteger> {

    private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<>();

    static {
        DATE_FORMAT_REGEXPS.put("^\\d{8}$", "yyyyMMdd");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "dd/MM/yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
    }

    /**
     * Determine SimpleDateFormat pattern matching with the given date string. Returns null if
     * format is unknown. You can simply extend DateUtil with more formats if needed.
     * @param dateString The date string to determine the SimpleDateFormat pattern for.
     * @return The matching SimpleDateFormat pattern, or null if format is unknown.
     * @see SimpleDateFormat
     */
    public static String determineDateFormat(String dateString) {
        for (Map.Entry<String, String> entry : DATE_FORMAT_REGEXPS.entrySet()) {
            if (dateString.toLowerCase().matches(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null; // Unknown format.
    }
    private GregorianCalendar date;

    public TDateInteger(Integer value) {
        setDate(value);
    }

    public static TDateInteger valueOfUnknownFormat(String value) {
        return new TDateInteger(value, determineDateFormat(value));
    }

    public static TDateInteger valueOf(String value, String format) {
        return new TDateInteger(value, format);
    }

    public TDateInteger(String date, String format) {
        try {
            this.date = new GregorianCalendar();
            Date dateAux = new SimpleDateFormat(format).parse(date);
            this.date.setTime(dateAux);
        } catch (Exception excp) {
            throw new IllegalArgumentException("[" + date + "] is not correct with format [" + format + "]", excp);
        }
    }

    @SuppressWarnings("MagicConstant")
    public TDateInteger(int year, int month, int day) {
        int monthToUse = month - 1;
        if (monthToUse >= Calendar.JANUARY && monthToUse <= Calendar.DECEMBER) {
            date = new GregorianCalendar(year, monthToUse, day);
        } else {
            throw new IllegalArgumentException("The month " + month + " is not valid");
        }
    }

    public TDateInteger() {
        date = new GregorianCalendar();
    }

    public static TDateInteger today() {
        return new TDateInteger();
    }

    /**
     * Devuelve un gregorian calendar en el formato yyyyMMdd
     *
     * @param gc Gregorian calendar que contiene la fecha
     * @return La fecha del gregorian calendar en el formato yyyyMMdd
     */
    private static String getDateStringFromGregorianCalendar(GregorianCalendar gc) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(gc.getTime());
    }

    private static Integer getDateIntegerFromGregorianCalendar(GregorianCalendar gc) {
        return Integer.valueOf(getDateStringFromGregorianCalendar(gc));
    }

    /**
     * Devuelve el objeto como una cadena: la fecha en el formato yyyyMMdd
     *
     * @return La fecha en el formato yyyyMMdd
     */
    public String toString() {
        return getDateStringFromGregorianCalendar(date);
    }

    public Integer toInteger() {
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH) + 1; // Los meses comienzan en 0.
        int day = date.get(Calendar.DAY_OF_MONTH);

        return year * 10000 + month * 100 + day;
    }

    public GregorianCalendar toGregorianCalendar() {
        return date;
    }

    @SuppressWarnings("MagicConstant")
    public void setDate(Integer date) {
        int year = date / 10000;
        int month = (date - year * 10000) / 100 - 1;
        int day = date % 100;

        if (month >= Calendar.JANUARY && month <= Calendar.DECEMBER) {
            this.date = new GregorianCalendar(year, month, day);
        } else {
            throw new IllegalArgumentException("The month " + month + " is not valid");
        }
    }

    public void setDate(String date) {
        String dateFormat = determineDateFormat(date);
        if (dateFormat == null) {
            throw new IllegalArgumentException("Can't determine date format in [" + date + "]");
        }
        this.date = new GregorianCalendar();
        try {
            Date dateAux = new SimpleDateFormat(dateFormat).parse(date);
            this.date.setTime(dateAux);
        } catch (Exception excp) {
            throw new IllegalArgumentException("Not a correct date [" + date + "]", excp);
        }
    }

    /**
     * Compare two TDateInteger objects
     * @param other the object to be compared.
     * @return <0 if this object is lower than other, >0 if this object is greater than other or 0 if they are equals
     */
    public int compareTo(TDateInteger other) {
        return this.date.compareTo(other.toGregorianCalendar());
    }

    public boolean isLowerThan(TDateInteger other) {
        return this.date.compareTo(other.toGregorianCalendar())<0;
    }

    public boolean isLowerOrEqualsThan(TDateInteger other) {
        return this.date.compareTo(other.toGregorianCalendar())<=0;
    }

    public boolean isEquals(TDateInteger other) {
        return this.date.compareTo(other.toGregorianCalendar())==0;
    }

    public boolean isGreaterThan(TDateInteger other) {
        return this.date.compareTo(other.toGregorianCalendar())>0;
    }

    public boolean isGreaterOrEqualsThan(TDateInteger other) {
        return this.date.compareTo(other.toGregorianCalendar())>=0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        TDateInteger otro = (TDateInteger) obj;
        return toGregorianCalendar().equals(otro.toGregorianCalendar());
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    public TDateInteger add(addTypes type, int value) {
        GregorianCalendar gcDest = (GregorianCalendar) date.clone();
        switch (type) {
            case YEAR:
                gcDest.add(Calendar.YEAR, value);
                break;
            case MONTH:
                gcDest.add(Calendar.MONTH, value);
                break;
            case DAY:
                gcDest.add(Calendar.DAY_OF_MONTH, value);
                break;
        }

        return new TDateInteger(getDateIntegerFromGregorianCalendar(gcDest));
    }

    public enum addTypes {
        YEAR, MONTH, DAY
    }

    public TDateInteger copy() {
        return new TDateInteger(this.toInteger());
    }
}