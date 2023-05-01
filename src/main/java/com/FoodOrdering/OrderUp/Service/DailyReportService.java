package com.FoodOrdering.OrderUp.Service;


import com.FoodOrdering.OrderUp.Model.payload.response.GetOrderDTO;
import com.FoodOrdering.OrderUp.Repository.MongoRepo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.WorkbookUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DailyReportService {
	private static final Logger log = LogManager.getLogger(DailyReportService.class);

	@Value("${dailyReport.template}")
	private String excelFilePath;

	@Value("${dailyReport.name}")
	private String DAILY_REPORT_NAME;

	@Value("${dailyReport.path}")
	private String DAILY_REPORT_PATH;

	@Autowired
	private EmailService emailService;

	@Autowired
	MongoRepo mongoRepo;

//	private String logCategory = Constant.SCHEDULE_TASK_DAILY_REPORT_EXPORT;

	private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private static final int paramChange = 6; // khi nao them cot +2 vào param

	public void removeMergn() throws IOException {

		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

		CellStyle cellStyle = workbook.createCellStyle();

		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);

		Sheet sheetTopup = workbook.getSheetAt(5);
		removeRow(sheetTopup);

		Sheet sheetEvoucher = workbook.getSheetAt(7);
		removeRow(sheetEvoucher);

		try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();

		}
	}

	public void transactionByDay(ObjectId resId, String email) throws InvalidFormatException {


		List<GetOrderDTO> orderByDay = mongoRepo.getOrderByDay(resId);
		log.info("orderByDay={}", gson.toJson(orderByDay));



		try {
			writeExcel(orderByDay, excelFilePath,email);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeExcel( List<GetOrderDTO> order, String excelFilePath, String email)
			throws IOException {

		/****************************************************
		* Delete old report file
		****************************************************/
		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(new Date());
			String fileName = DAILY_REPORT_NAME.replaceAll("%date", date);
			String filePath = DAILY_REPORT_PATH + fileName;

			File dailyReport = new File(filePath);
			boolean result = Files.deleteIfExists(dailyReport.toPath());

			log.info("{} | Delete report file done", "logCategory");

		} catch (Exception e) {
			log.fatal("{} | Delete report file fail | error={}", "logCategory",
					e.getMessage(), e);
		}

		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		CellStyle cellStyle = workbook.createCellStyle();

		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setAlignment(HorizontalAlignment.RIGHT);

		// Delete old sheet
		workbook.removeSheetAt(0);
		Sheet sheetDailyOverall = workbook.createSheet(WorkbookUtil.createSafeSheetName("Daily"));
		workbook.setSheetOrder("Daily", 0);
		sheetDailyOverall = workbook.getSheetAt(0);

		Sheet sheetTransactionByDay = workbook.getSheetAt(1);

		Sheet sheetAccBalance = workbook.getSheetAt(2);
		removeRow(sheetAccBalance);


		// add by datdt
		Sheet sheetOrder = workbook.getSheetAt(1);
		removeRow(sheetOrder);

		int rowCountAccDevice = 1;
		int rowCountTrxDetail = 1;
		int rowCountAccBalance = 1;
		int rowCountTrxPending = 1;
		int rowCountTransfer = 1;
		int rowCountAdvance = 1;
		int rowTransactionByDay = sheetTransactionByDay.getLastRowNum();

		int rowCountFirm = 1;
		int rowCountShin = 1;
		int rowCountNapas = 1;


		writeBookOrder(order,sheetOrder,cellStyle);




		/****************************************************
		 * Create report file
		 ****************************************************/
		try {
			log.info("{} | Create report file start", "logCategory");

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateFormat.format(new Date());
			String fileName = DAILY_REPORT_NAME.replaceAll("%date", date);
			String filePath = DAILY_REPORT_PATH + fileName;

			File dailyReport = new File(filePath);
			dailyReport.createNewFile();

			FileOutputStream outputStream = new FileOutputStream(filePath);
			workbook.write(outputStream);
			outputStream.close();
			log.info("{} | Create report file done", "logCategory");

		} catch (Exception e) {
			log.fatal("{} | Create report file fail | error={}", "logCategory",
					e.getMessage(), e);
		}

		/*****************************************************
		 * Update template file
		 *****************************************************/
		try {
			log.info("{} | Update template file start", "logCategory");
			FileOutputStream outputStream = new FileOutputStream(excelFilePath);
			workbook.write(outputStream);
			outputStream.close();
			log.info("{} | Update template file done", "logCategory");

		} catch (Exception e) {
			log.fatal("{} | Update template file fail | error={}", "logCategory",
					e.getMessage(), e);
		}

		// Close workbook
		workbook.close();

		/*****************************************************
		 * Send email to developers
		 *****************************************************/
		try {
			log.info("{} | Send email to developer start", "logCategory");
			emailService.sendDailyReportEmail("logCategory", false, email);
			log.info("{} | Send email to developer done", "logCategory");

		} catch (Exception e) {
			log.fatal("{} | Send mail to developer fail | error={}", "logCategory",
					e.getMessage(), e);
		}
	}




	// add by datdt
	private void writeBookOrder(List<GetOrderDTO> order, Sheet sheetSocial, CellStyle cellStyle) {


		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int rowCountTrx = sheetSocial.getLastRowNum();

		int i = 0;

		Document document ;
		for (GetOrderDTO o:order) {

			i = i + 1;
			Row row = sheetSocial.createRow(++rowCountTrx);
			Cell cell = row.createCell(0);

			for (int j = 0; j < 20; j++) {
				cell = row.createCell(j);
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
				cell.setCellStyle(cellStyle);
				cell.setCellStyle(cellStyle);
			}

			cell = row.getCell(0);
			cell.setCellValue(o.getCustomerName());

			cell = row.getCell(1);
			cell.setCellValue(o.getCustomerPhone());

			cell = row.getCell(2);
			cell.setCellValue(o.getCustomerAddress());

			cell = row.getCell(3);
			cell.setCellValue(o.getOrderDatetime());

			cell = row.getCell(4);
			cell.setCellValue(o.getOrderStatus());

			cell = row.getCell(5);
			cell.setCellValue(o.getMethod());

			cell = row.getCell(6);
			cell.setCellValue(o.getNote());

//			cell = row.getCell(7);
//			cell.setCellValue(o.getFoodOrderList());



		}

	}

	public void removeRow(Sheet sheet) {

		int lastRowNum = sheet.getLastRowNum();

		for (int j = 0; j < sheet.getNumMergedRegions(); ++j) {
			sheet.removeMergedRegion(j);
		}

//		for (int i = lastRowNum; i > 1; i--) {
//			sheet.removeRow(sheet.getRow(i));
//		}

		// add by datdt
		for (int i = lastRowNum; i > 1; i--) {
			Row row = sheet.getRow(i);
			if (row != null) {
				sheet.removeRow(row);
			}
		}


	}

	public String convert(long amount) {
		String amountPayStr = String.valueOf(amount);
		String amountPayStrFormat = String.format("%,.0f", new BigDecimal(amountPayStr));
		return amountPayStrFormat;
	}

}
