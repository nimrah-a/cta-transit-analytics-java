package com.ctaanalytics;

import com.ctaanalytics.service.SampleData;
import com.ctaanalytics.ui.ConsoleApp;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        new ConsoleApp(SampleData.create()).run();
    }
}
