package com.juaracoding;

import com.juaracoding.drivers.DriverSingleton;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.io.FileHandler;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

public class Listeners implements ITestListener{
    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);
        TakesScreenshot scrShot = ((TakesScreenshot) DriverSingleton.getDriver());
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        File destFile = new File("Screenshot/failure.png");

        try {
            FileHandler.copy(srcFile,destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
