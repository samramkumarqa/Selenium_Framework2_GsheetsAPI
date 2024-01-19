package TestDataReader;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Spliterator;

public class SheetsQuickstart {
	private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
	private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
	private static final String TOKENS_DIRECTORY_PATH = "tokens/path";
	//private static final String existingSpreadSheetID = "1zGzo3H91PJNZ_8WnbXlI7RNqfI7SUa4BiOjwn3edmH4";
	// static int totalColumnsTestDataSheet = 5;

	/**
	 * Global instance of the scopes required by this quickstart. If modifying these
	 * scopes, delete your previously saved tokens/ folder.
	 */
	// private static final List<String> SCOPES =
	// Collections.singletonList(SheetsScopes.SPREADSHEETS);
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
	private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
	static Sheets.Spreadsheets spreadsheets;

	/**
	 * Creates an authorized Credential object.
	 *
	 * @param HTTP_TRANSPORT The network HTTP Transport.
	 * @return An authorized Credential object.
	 * @throws IOException If the credentials.json file cannot be found.
	 */
	public static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
		// Load client secrets.
		InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
		if (in == null) {
			throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
		}
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES)
				.setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
				.setAccessType("offline").build();
		LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
		return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
	}

	/**
	 * Prints the names and majors of students in a sample spreadsheet:
	 * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
	 * 
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList<HashMap> readSpreadSheet(String existingSpreadSheetID) throws GeneralSecurityException, IOException {
		// Build a new authorized API client service.
		ArrayList<HashMap> excelDataArray = new ArrayList<HashMap>();

		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		//final String spreadsheetId = "1zGzo3H91PJNZ_8WnbXlI7RNqfI7SUa4BiOjwn3edmH4";
		//final String range = "Sheet1!A2:E";
		Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
				.setApplicationName(APPLICATION_NAME).build();

		SheetsQuickstart.getSpreadsheetInstance();
		int totalRows = SheetsQuickstart.getRows("Sheet1", existingSpreadSheetID);
		String[] torun = null;
		String[] values1 = null;
		String[] values2 = null;
		String cellvalue = "";
		String heading = "";

		for (int i = 2; i < totalRows + 1; i++) {
			HashMap<String, String> exceldatahashmap = new HashMap<String, String>();

			ValueRange torunresponse = service.spreadsheets().values().get(existingSpreadSheetID, "Sheet1!B" + i + ":B" + i)
					.execute();
			torun = torunresponse.getValues().toString().split(",");
			System.out.println(torun[0].toString().trim());
			if (torun[0].toString().contains("Yes")) {

				ValueRange response1 = service.spreadsheets().values().get(existingSpreadSheetID, "Sheet1!C" + i + ":F" + i)
						.execute();
				ValueRange response2 = service.spreadsheets().values().get(existingSpreadSheetID, "Sheet1!C1:F1").execute();
				values1 = response1.getValues().toString().split(",");
				values2 = response2.getValues().toString().split(",");
				for (int j = 0; j < values1.length; j++) {

					heading = values2[j].toString().trim().replace("[[", "").replace("]]", "");
					cellvalue = values1[j].toString().trim().replace("]]", "").replace("[[", "");
					exceldatahashmap.put(heading, cellvalue);
					// System.out.println("Heading "+ heading+" and cell value "+ cellvalue);
				}
				excelDataArray.add(exceldatahashmap);
			} else {
				// catch exception
			}

		}

		return excelDataArray;
	}

	public static void createNewSpreadSheet(String sheetName) throws GeneralSecurityException, IOException {

		Spreadsheet createdResponse = null;
		try {
			final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
					.setApplicationName(APPLICATION_NAME).build();

			SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
			spreadsheetProperties.setTitle("Ramkumar S1");
			SheetProperties sheetProperties = new SheetProperties();
			sheetProperties.setTitle("Test Suite 11");
			Sheet sheet = new Sheet().setProperties(sheetProperties);

			Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties)
					.setSheets(Collections.singletonList(sheet));
			createdResponse = service.spreadsheets().create(spreadsheet).execute();

			System.out.println("Spread Sheet URL:" + createdResponse.getSpreadsheetUrl());

			List<List<Object>> data = new ArrayList<List<Object>>();
			List<Object> list2 = new ArrayList<Object>();

			list2.add("Good");
			list2.add("Morning!");
			list2.add("=1+1");

			data.add(list2);

			/*
			 * ValueRange valueRange = new ValueRange().setValues(data);
			 * service.spreadsheets().values().update(createdResponse.getSpreadsheetId(),
			 * "A1", valueRange).setValueInputOption("RAW").execute();
			 * 
			 */
			ArrayList<Object> data1 = new ArrayList<Object>(
					Arrays.asList("Source", "Status code", "Test Status", "=2+2"));

			writeSheet(data1, "A1", createdResponse.getSpreadsheetId(), sheetName);

		} catch (GoogleJsonResponseException e) {
			GoogleJsonError error = e.getDetails();
			if (error.getCode() == 404) {
				System.out.printf("Spread sheet not found with ID '%s' .\n", createdResponse.getSpreadsheetId());
			} else {
				throw e;
			}

		}
	}

	public static void writeSheet(List<Object> inputData, String sheetAndRange, String existingSpreadSheetID, String sheetName)
			throws IOException {

		// USER_ENTERED & RAW
		@SuppressWarnings("unchecked")
		List<List<Object>> values = Arrays.asList(inputData);
		ValueRange body = new ValueRange().setValues(values);
		UpdateValuesResponse result = spreadsheets.values().update(existingSpreadSheetID, sheetName+sheetAndRange, body)
				.setValueInputOption("RAW").execute();
		System.out.printf("%d cells updated. \n", result.getUpdatedCells());
	}

	public static void writeDataGoogleSheets(String sheetName, List<Object> data, String existingSpreadSheetID)
			throws IOException {
		int nextRow = getRows(sheetName, existingSpreadSheetID) + 1;
		writeSheet(data, "!A" + nextRow, existingSpreadSheetID, sheetName);
	}

	public static int getRows(String sheetName, String existingSpreadSheetID) throws IOException {
		List<List<Object>> values = spreadsheets.values().get(existingSpreadSheetID, sheetName).execute().getValues();
		int numRows = values != null ? values.size() : 0;
		System.out.printf("%d rows retrieved in '" + sheetName + "'\n", numRows);
		return numRows;
	}

	public static void createNewSheet(String existingSpreadSheetID, String newsheetTitle)
			throws IOException, GeneralSecurityException {

		AddSheetRequest addSheetRequest = new AddSheetRequest();
		SheetProperties sheetProperties = new SheetProperties();
		sheetProperties.setIndex(0);

		addSheetRequest.setProperties(sheetProperties);
		addSheetRequest.setProperties(sheetProperties.setTitle(newsheetTitle));

		BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();

		List<Request> requestsList = new ArrayList<Request>();
		batchUpdateSpreadsheetRequest.setRequests(requestsList);

		Request request = new Request();
		request.setAddSheet(addSheetRequest);
		requestsList.add(request);

		batchUpdateSpreadsheetRequest.setRequests(requestsList);

		spreadsheets.batchUpdate(existingSpreadSheetID, batchUpdateSpreadsheetRequest).execute();
	}

	public static void getSpreadsheetInstance() throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		spreadsheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
				GsonFactory.getDefaultInstance(), getCredentials(HTTP_TRANSPORT))
				.setApplicationName("Google sheet java integrate").build().spreadsheets();
	}

}