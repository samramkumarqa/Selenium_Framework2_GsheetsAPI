package testCases;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
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
import TestDataReader.SheetsQuickstart;

public class ScriptDriver {
	WebDriver driver;
	ExtentTest test;
	ExtentReports report;
	private static final String existingSpreadSheetID = "1zGzo3H91PJNZ_8WnbXlI7RNqfI7SUa4BiOjwn3edmH4";
	

	@Test(dataProvider = "ToolData",threadPoolSize=2)
	public void driverSetup(HashMap<String, String> data) throws InterruptedException, IOException, GeneralSecurityException {
		// TODO Auto-generated method stub
		
		report = new ExtentReports("//Users//ramkumars//eclipse-workspace//Selenium_Framework2//src//Results//Amazonpurchaseresults.html");
		test = report.startTest("ExtentDemo");
		
		
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
		
		searchpage hp = new searchpage(driver, data, existingSpreadSheetID);
		hp.searchText();
		Thread.sleep(2000);
		//SheetsQuickstart.createNewSpreadSheet();
		//SheetsQuickstart.getSpreadsheetInstance();
		SheetsQuickstart.writeDataGoogleSheets("TestResult", new ArrayList<Object>(Arrays.asList("Source1","Status Code","Test Status")), existingSpreadSheetID);
		report.endTest(test);
		report.flush();
		driver.quit();

}
	
	@DataProvider(name="ToolData", parallel=false)
	public Iterator<Object[]> getExcelData() throws IOException, GeneralSecurityException{
		@SuppressWarnings("rawtypes")
		ArrayList<HashMap> excelData;
		TestDataReader.SheetsQuickstart objExcelFile = new TestDataReader.SheetsQuickstart();
		//excelData = objExcelFile.readExcel("E:\\ExcelData","ToolsQATestData.xls","Sheet1");
		excelData = objExcelFile.readSpreadSheet(existingSpreadSheetID);
		
		List<Object[]> dataArray = new ArrayList<Object[]>();
		for(HashMap data : excelData){
			dataArray.add(new Object[] { data });
			}
		return dataArray.iterator();
	}

}

