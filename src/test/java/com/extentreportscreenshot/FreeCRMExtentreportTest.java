package com.extentreportscreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Assert;
import org.testng.ITestResult;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class FreeCRMExtentreportTest {

	WebDriver driver;
	ExtentReports extent;
	ExtentTest extentTest;

	@BeforeTest
	public void setExtent() {
		extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/extentReport.html", true);
		extent.addSystemInfo("Username", "Tushar");
		extent.addSystemInfo("HostName", "TusharWindows");
		extent.addSystemInfo("Enviornment", "QA");
	}

	@AfterTest
	public void endReport() {
		extent.flush(); // close the connection with the extent report
		extent.close(); // close the connection and release the extent reference
	}

	public static String getScreenshot(WebDriver driver, String screenshotName) throws IOException {
		String dateFormat = new SimpleDateFormat("ddMMyyyy").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) driver;
		File source = ts.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "/FailedTestsScreenshots/" + screenshotName + dateFormat
				+ ".png";
		File finaldestination = new File(destination);
		FileHandler.copy(source, finaldestination);
		return destination;
	}

	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "H:\\Tushar\\chromedriver.exe");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		// driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get("https://www.freecrm.com/");
	}

	@Test
	public void verifyCRMTitleTest() {
		extentTest=extent.startTest("verifyCRMTitleTest"); // write this line in each test cases as first line
		Assert.assertEquals(driver.getTitle(), "Free CRM software in the cloud powers sales and customer service");
	}

	@AfterMethod
	public void tearDown(ITestResult result) throws IOException {
		if(result.getStatus()==ITestResult.FAILURE)
		{
			extentTest.log(LogStatus.FAIL,"The Test case failed is"+result.getName()); //to add name to extent report
			extentTest.log(LogStatus.FAIL,"The Test case failed is"+result.getThrowable()); // to show the log/exception in extent report
			
			String scrrentshotName=FreeCRMExtentreportTest.getScreenshot(driver, result.getName()); // get screenshotname
			//extentTest.log(LogStatus.FAIL,extentTest.addScreencast(scrrentshotName)); // here to attach the scrrenshot name of failed test cases
			extentTest.log(LogStatus.SKIP,extentTest.addScreenCapture(scrrentshotName));
		}
		else if (result.getStatus()==ITestResult.SKIP) {
			extentTest.log(LogStatus.SKIP,"The test case skipped is "+ result.getName());
			extentTest.log(LogStatus.SKIP,"The Test case failed is " + result.getName());
			String screenshotName = FreeCRMExtentreportTest.getScreenshot(driver, result.getName());
			//extentTest.log(LogStatus.SKIP,extentTest.addScreencast(screenshotName));
			extentTest.log(LogStatus.SKIP,extentTest.addScreenCapture(screenshotName));
		}
		else if(result.getStatus()==ITestResult.SUCCESS) {
			extentTest.log(LogStatus.PASS,"The test case passed is "+ result.getName());
			extentTest.log(LogStatus.PASS,"The Test case passed is " + result.getName());
			String screenshotName = FreeCRMExtentreportTest.getScreenshot(driver, result.getName());
			//extentTest.log(LogStatus.PASS,extentTest.addScreencast(screenshotName));
			extentTest.log(LogStatus.SKIP,extentTest.addScreenCapture(screenshotName));
		}
		extent.endTest(extentTest); // ending the test and prepare to create to html report
		driver.quit();
	}
	
	

}
