package testCases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Pages.searchpage;

public class ScriptDriver {
	WebDriver driver;
	ExtentTest test;
	ExtentReports report;

	@Test(dataProvider = "ToolData",threadPoolSize=7)
	public void driverSetup(HashMap<String, String> data) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		report = new ExtentReports("//Users//ramkumars//eclipse-workspace//Selenium_Framework2//src//Results//Amazonpurchaseresults.html");
		test = report.startTest("ExtentDemo");
		
		System.out.print("Got the data value :"+data.get("ToRun"));
		//Firefox driver
		/* 
		WebDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get("http://google.com");
		driver.quit();*/
		
		
		//Chrome driver
		//System.setProperty("webdriver.chrome.driver", "/Users/ramkumars/Selenium_Essentials/drivers/chromedriver");
		System.setProperty("webdriver.chrome.driver", "/Users/ramkumars/eclipse-workspace/Selenium_Framework2/src/test/java/Alldrivers/chromedriver");
		
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		
		//driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		driver.get("http://amazon.in");
		test.log(LogStatus.PASS, "Products has been added to cart successfully");
		
		searchpage hp = new searchpage(driver, data);
		hp.searchText();
		Thread.sleep(2000);
		
		report.endTest(test);
		report.flush();
		driver.quit();

}
	
	@DataProvider(name="ToolData", parallel=true)
	public Iterator<Object[]> getExcelData() throws IOException{
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> excelData;
		TestDataReader.readExcelFile objExcelFile = new TestDataReader.readExcelFile();
		//excelData = objExcelFile.readExcel("E:\\ExcelData","ToolsQATestData.xls","Sheet1");
		excelData = objExcelFile.readExcel("/Users/ramkumars/eclipse-workspace/Selenium_Framework2/src/test/java/TestData","ToolsQATestData3.xls","Sheet1");
		
		List<Object[]> dataArray = new ArrayList<Object[]>();
		for(HashMap data : excelData){
			dataArray.add(new Object[] { data });
			}
		return dataArray.iterator();
	}

}

