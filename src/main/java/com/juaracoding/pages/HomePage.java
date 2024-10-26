package com.juaracoding.pages;

import com.juaracoding.drivers.DriverSingleton;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HomePage {
    public static WebDriver driver;

    public HomePage(){
        this.driver = DriverSingleton.getDriver();
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//a[text()='Movies']")
    private WebElement movies;

    public void moviesBtn(){
        movies.click();
    }

    public void getMoviesCategory(String category){
        driver.findElement(By.xpath("//a[@aria-label='"+category+"']")).click();
    }
}
