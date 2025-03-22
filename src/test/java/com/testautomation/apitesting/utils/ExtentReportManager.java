package com.testautomation.apitesting.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportManager {
	
	private static ExtentReports extent;
    private static ExtentTest test;

    public static void initReports() {
        if (extent == null) {
        	String path = System.getProperty("user.dir") + "\\reports\\index.html";
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(path);
            sparkReporter.config().setReportName("RestAssured Automation Booking Test Results");
            sparkReporter.config().setDocumentTitle("Post Test Results");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Tester", "Ajay Yadav");
        }
    }

    public static ExtentTest createTest(String testName) {
        test = extent.createTest(testName);
        return test;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }

}
