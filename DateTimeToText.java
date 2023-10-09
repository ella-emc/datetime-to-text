import java.awt.*;
import java.util.Arrays;
import java.util.regex.*;

public class DateTimeToText {
    public static void main(String[] args) {
        if (args.length == 3 || args.length == 2) {
            // If length is 3, then it can mean that the arguments are the date, time in 12-hr format, and AM/PM
            // If length is 2, then it can mean that the arguments are the date, time in 24-hr format
            String textConversion = "";
            String[] date;
            final String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
                    "October", "November", "December"};
            final int[] daysInMonths = {31,28,31,30,31,30,31,31,30,31,30,31};

            // Match regular expression patterns (Lines 13-25)
            // Date must be delimited by a slash or a hyphen
            // Digits in the leftmost and inner value must be 0-9 and must occur at least once and at most twice
            // Digits in the rightmost value must be 0-9 and must occur 4 times
            boolean isDateFormatValid = args[0].matches("(([0-9]{1,3}/[0-9]{1,3}/[0-9]{4})|([0-9]{1,3}-[0-9]{1,3}-[0-9]{4}))");

            // Time must be delimited by a colon
            // Value on the left can be one or two digits from 0-9; value on the right must be two digits from 0-9
            boolean isTimeFormatValid = args[1].matches("[0-9]{1,3}:[0-9]{2}");

            if (args.length == 3) {
                // Meridian letters must be separated by a period or not at all
                // Matches case insensitively
                boolean isMeridianFormatValid = args[2].matches("(?i)(am|pm|a\\.m\\.|p\\.m\\.)");
                if (!(isDateFormatValid && isTimeFormatValid && isMeridianFormatValid)) {
                    System.out.println("Invalid format of arguments");
                    System.exit(1);
                }
            }

            if (args.length == 2) {
                if (!(isDateFormatValid &&isTimeFormatValid)) {
                    System.out.println("Invalid format of arguments");
                    System.exit(1);
                }
            }

            // If program does not prematurely terminate and all formatting is valid, execute the code below

            final String[] ones = {"one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
                    "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen",
                    "eighteen", "nineteen"};
            final String[] tens = {"ten", "twenty", "thirty", "forty", "fifty", "sixty", "eighty", "ninety", "hundred"};

            date = args[0].split("[/-]");

            // Store each string in the date array as integers
            int month = Integer.parseInt(date[0]);
            int day = Integer.parseInt(date[1]);
            int year = Integer.parseInt(date[2]);

            // Check if the values in the date are within the valid range
            boolean isMonthValid = (month >= 1 && month <= 12);
            if (isMonthValid && ((day >= 1) && (day <= daysInMonths[month-1]))) {
                int digit1; // ones
                int digit2; // tens

                textConversion += months[month-1] + " ";

                // Convert day to words
                if (day < 20) {
                    textConversion += ones[day-1] + " ";
                } else {
                    digit2 = day / 10;
                    digit1 = day % 10;

                    textConversion += tens[digit2-1];
                    if (digit1 > 0) textConversion += "-";
                    textConversion += ones[digit1-1] + " ";
                }
            } else {
                System.out.println("Invalid date");
                System.exit(1);
            }

            // If program does not exit prematurely, execute the conversion of the year
            boolean isYearValid = year >= 1000;
            if (isYearValid) {
                int digit4, digit3, digit2, digit1;

                digit4 = year / 1000;
                digit3 = year % 1000 / 100;
                digit2 = year % 100 / 10;
                digit1 = year % 10;

                textConversion += ones[digit4-1] + " thousand ";
                if (digit3 > 0) textConversion += ones[digit3-1] + " hundred ";
                if (year % 100 >= 20) {
                    if (digit2 > 0) textConversion += tens[digit2 - 1] + "-";
                    if (digit1 > 0) textConversion += ones[digit1 - 1] + " ";
                } else {
                    textConversion += ones[year%100 - 1] + " ";
                }
            } else {
                System.out.println("Invalid year");
                System.exit(1);
            }

            // If program above does not end prematurely, execute the conversion of time
            String[] time = args[1].split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);

            if (args.length == 3) {
                // 12-hr format
                if ((hour >= 1 && hour <= 12) && (minute >= 0 && minute <= 59)) {
                    int digit1, digit2;

                    textConversion += "at " + ones[hour-1] + " ";
                    if (minute == 0) {
                        textConversion += "o'clock ";
                    } else if (minute < 20) {
                        textConversion += ones[minute-1] + " ";
                    } else {
                        digit2 = minute / 10;
                        digit1 = minute % 10;

                        textConversion += tens[digit2-1];
                        if (digit1 > 0) textConversion += "-";
                        textConversion += ones[digit1-1] + " ";
                    }

                    args[2] = args[2].toLowerCase();
                    if (args[2].contains("a")) {
                        textConversion += "in the morning";
                    } else if (args[2].contains("p") && hour > 5){
                        textConversion += "in the evening";
                    } else {
                        textConversion += "in the afternoon";
                    }
                } else {
                    System.out.println("Invalid time");
                    System.exit(1);
                }
            } else {
                // 24-hr format
                if ((hour >= 1 && hour <= 24) && (minute >= 0 && minute <= 59)) {
                    int digit1, digit2;

                    if (hour <= 12) textConversion += "at " + ones[hour-1] + " ";
                    else textConversion += "at " + ones[hour-13] + " ";
                    if (minute == 0) {
                        textConversion += "o'clock ";
                    } else if (minute < 20) {
                        textConversion += ones[minute-1] + " ";
                    } else {
                        digit2 = minute / 10;
                        digit1 = minute % 10;

                        textConversion += tens[digit2-1];
                        if (digit1 > 0) textConversion += "-";
                        textConversion += ones[digit1-1] + " ";
                    }

                    if (hour < 12) textConversion += "in the morning";
                    else if (hour > 12 && hour <= 17) textConversion += "in the afternoon";
                    else textConversion += "in the evening";
                } else {
                    System.out.println("Invalid time");
                    System.exit(1);
                }
            }

            System.out.println(textConversion);
        } else {
            System.out.println("Invalid number of arguments");
            System.exit(1);
        }
    }
}

