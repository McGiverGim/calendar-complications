/*
 * Copyright (C) 2017 Raimondas Rimkus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rkr.calendar.complications;

import android.support.wearable.complications.ComplicationData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class CalendarProvider {
    public static final String WEEK = "week";
    public static final String YEAR = "year";
    public static final String MONTH_NUMBER = "month_number";
    public static final String MONTH_TEXT = "month_text";
    public static final String DAY = "day";
    public static final String YEAR_WEEK = "year_week";

    public static final String[] DATE_SEPARATORS = {" ", ".", "-", "/"};
    private static final Map<Integer, Set<String>> SINGLE_LINE = new HashMap<Integer, Set<String>>(){{
        put(ComplicationData.TYPE_LONG_TEXT, new HashSet<String>() {{
            add(YEAR);
            add(MONTH_NUMBER);
            add(MONTH_TEXT);
            add(DAY);
        }});
        put(ComplicationData.TYPE_SHORT_TEXT, new HashSet<String>() {{
            add(MONTH_NUMBER);
            add(MONTH_TEXT);
            add(DAY);
        }});
    }};
    private static final Map<Integer, String[]> DATE_FORMATS = new HashMap<Integer, String[]>(){{
        put(0, new String[]{WEEK, YEAR, MONTH_NUMBER, MONTH_TEXT, DAY, YEAR_WEEK});
        put(1, new String[]{WEEK, YEAR, MONTH_NUMBER, MONTH_TEXT, DAY, YEAR_WEEK});
        put(2, new String[]{WEEK, DAY, MONTH_NUMBER, MONTH_TEXT, YEAR, YEAR_WEEK});
        put(3, new String[]{WEEK, DAY, MONTH_NUMBER, MONTH_TEXT, YEAR, YEAR_WEEK});
        put(4, new String[]{WEEK, MONTH_NUMBER, MONTH_TEXT, DAY, YEAR, YEAR_WEEK});
        put(5, new String[]{WEEK, MONTH_NUMBER, MONTH_TEXT, DAY, YEAR, YEAR_WEEK});
    }};

    public static List<String> GetRows(Set<String> selection, int separator, int dateFormat, int complicationType) {
        List<String> temp = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Set<String> _selection = new HashSet<>(selection);
        boolean shortYear = dateFormat % 2 == 1;

        String line = "";
        for (String type : DATE_FORMATS.get(dateFormat))
            if (_selection.contains(type)) {
                _selection.remove(type);
                switch (type) {
                    case WEEK:
                        String weekday = WeekdayToString(calendar.get(Calendar.DAY_OF_WEEK));
                        line = complicationType == ComplicationData.TYPE_SHORT_TEXT ? weekday.substring(0, 3) : weekday;
                        break;
                    case YEAR:
                        int year = calendar.get(Calendar.YEAR);
                        line += String.format(Locale.US, "%02d", shortYear ? year % 100 : year);
                        break;
                    case MONTH_NUMBER:
                        line += String.format(Locale.US, "%02d", calendar.get(Calendar.MONTH) + 1);
                        break;
                    case MONTH_TEXT:
                        String month = MonthToString(calendar.get(Calendar.MONTH));
                        line += complicationType == ComplicationData.TYPE_SHORT_TEXT ? month.substring(0, 3) : month;
                        break;
                    case DAY:
                        line += String.format(Locale.US, "%02d", calendar.get(Calendar.DAY_OF_MONTH));
                        break;
                    case YEAR_WEEK:
                        line = String.format(Locale.US, "WK%d", calendar.get(Calendar.WEEK_OF_YEAR));
                        break;
                }
                if (SINGLE_LINE.get(complicationType).contains(type) && commonElementExists(_selection, SINGLE_LINE.get(complicationType))) {
                    line += DATE_SEPARATORS[separator];
                    continue;
                }
                temp.add(line);
                line = "";
            }

        return temp;
    }

    private static boolean commonElementExists(Set<String> a, Set<String> b)
    {
        for (String _a: a)
            if (b.contains(_a))
                return true;
        return false;
    }

    private static String WeekdayToString(int weekday)
    {
        switch (weekday) {
            case Calendar.MONDAY:
                return "MONDAY";
            case Calendar.TUESDAY:
                return "TUESDAY";
            case Calendar.WEDNESDAY:
                return "WEDNESDAY";
            case Calendar.THURSDAY:
                return "THURSDAY";
            case Calendar.FRIDAY:
                return "FRIDAY";
            case Calendar.SATURDAY:
                return "SATURDAY";
            case Calendar.SUNDAY:
                return "SUNDAY";
        }
        return "";
    }

    private static String MonthToString(int month)
    {
        switch (month) {
            case Calendar.JANUARY:
                return "JANUARY";
            case Calendar.FEBRUARY:
                return "FEBRUARY";
            case Calendar.MARCH:
                return "MARCH";
            case Calendar.APRIL:
                return "APRIL";
            case Calendar.MAY:
                return "MAY";
            case Calendar.JUNE:
                return "JUNE";
            case Calendar.JULY:
                return "JULY";
            case Calendar.AUGUST:
                return "AUGUST";
            case Calendar.SEPTEMBER:
                return "SEPTEMBER";
            case Calendar.OCTOBER:
                return "OCTOBER";
            case Calendar.NOVEMBER:
                return "NOVEMBER";
            case Calendar.DECEMBER:
                return "DECEMBER";
        }
        return "";
    }
}
