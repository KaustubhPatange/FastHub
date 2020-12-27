package com.fastaccess.ui.widgets.contributions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fastaccess.helper.InputHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Javier González
 * All right reserved.
 */
public class ContributionsProvider {

    private final static String FILL_STRING = "fill=\"";
    private final static String DATA_STRING = "data-count=\"";
    private final static String DATE_STRING = "data-date=\"";

    @NonNull public List<ContributionsDay> getContributions(@Nullable String string) {
        ArrayList<ContributionsDay> contributions = new ArrayList<>();
        if (InputHelper.isEmpty(string)) return contributions;
        int fillPos = -1;
        int dataPos = -1;
        int datePos = -1;
        while (true) {
            fillPos = string.indexOf(FILL_STRING, fillPos + 1);
            dataPos = string.indexOf(DATA_STRING, dataPos + 1);
            datePos = string.indexOf(DATE_STRING, datePos + 1);
            if (fillPos == -1) break;
            int level = 0;
            String levelString = string.substring(fillPos + FILL_STRING.length(), dataPos - 2);
            switch (levelString) {
                case "var(--color-calendar-graph-day-bg)":
                case "#eeeeee":
                case "#ebedf0":
                    level = 0;
                    break;
                case "var(--color-calendar-graph-day-L1-bg)":
                case "#d6e685":
                case "#239a3b":
                    level = 1;
                    break;
                case "var(--color-calendar-graph-day-L2-bg)":
                case "#8cc665":
                case "#c6e48b":
                    level = 2;
                    break;
                case "var(--color-calendar-graph-day-L3-bg)":
                case "#44a340":
                case "#7bc96f":
                    level = 3;
                    break;
                case "var(--color-calendar-graph-day-L4-bg)":
                case "#1e6823":
                case "#196127":
                    level = 4;
                    break;
            }

            int dataEndPos = string.indexOf("\"", dataPos + DATA_STRING.length());
            String dataString = string.substring(dataPos + DATA_STRING.length(), dataEndPos);
            int data = Integer.valueOf(dataString);
            String dateString = string.substring(datePos + DATE_STRING.length(), datePos + DATE_STRING.length() + 11);
            contributions.add(new ContributionsDay(
                    Integer.valueOf(dateString.substring(0, 4)),
                    Integer.valueOf(dateString.substring(5, 7)),
                    Integer.valueOf(dateString.substring(8, 10)),
                    level,
                    data
            ));
        }

        return contributions;
    }
}
