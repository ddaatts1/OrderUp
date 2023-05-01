//package com.FoodOrdering.OrderUp.Service;
//
//import com.epay.ewallet.service.reports.constant.Constant;
//import com.epay.ewallet.service.reports.model.*;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.TreeMap;
//import java.util.stream.Collectors;
//
//public class SheetDailyService {
//
//    public static void writeSheetDailyOverall(
//            List<Company> companyList, List<Transaction> transactions,
//            List<Transaction> transactionsFirm, List<Transaction> transactionsNapas,
//            List<Transaction> transactionShinhan,
//            List<UserBalance> userBalances, TreeMap<Integer, TreeMap<String, Long>> companyUserBalanceList,
//            List<TotalTransaction> totalTransactions, Total total, List<AccDevice> accDevices,
//            List<Transaction> transactionPending, List<EvoucherTransaction> evouchers, List<Transfer> transfer,
//            List<Transfer> advance, List<TopupTransaction> topups, List<BillTransaction> bills,
//            SocialDailyReport socialDailyReport,
//            SocialDailyReport socialMonthlyReport,
//            XSSFWorkbook workbook, Sheet sheet) {
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
//        Date yesterday = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000);
//        String date = dateFormat.format(yesterday);
//        String month = monthFormat.format(yesterday);
//
//        int rowId = 0;
//        int colNum = 11;
//
//        long cellValue = 0;
//        boolean isEmployee = false;
//        boolean isInDay = false;
//        String trxType = null;
//        String trxTypeSub = null;
//        String serviceId = null;
//
//        // Init cell styles
//        CellStyle cellStyleLeft = workbook.createCellStyle();
//        CellStyle cellStyleRight = workbook.createCellStyle();
//        CellStyle cellStyleCenter = workbook.createCellStyle();
//        CellStyle cellStyleCenterNoBorder = workbook.createCellStyle();
//        CellStyle cellStyleBorder = workbook.createCellStyle();
//        {
//            cellStyleLeft.setBorderTop(BorderStyle.THIN);
//            cellStyleLeft.setBorderRight(BorderStyle.THIN);
//            cellStyleLeft.setBorderBottom(BorderStyle.THIN);
//            cellStyleLeft.setBorderLeft(BorderStyle.THIN);
//            cellStyleLeft.setAlignment(HorizontalAlignment.LEFT);
//
//            cellStyleRight = workbook.createCellStyle();
//            cellStyleRight.setBorderTop(BorderStyle.THIN);
//            cellStyleRight.setBorderRight(BorderStyle.THIN);
//            cellStyleRight.setBorderBottom(BorderStyle.THIN);
//            cellStyleRight.setBorderLeft(BorderStyle.THIN);
//            cellStyleRight.setAlignment(HorizontalAlignment.RIGHT);
//
//            cellStyleCenter.setBorderTop(BorderStyle.THIN);
//            cellStyleCenter.setBorderRight(BorderStyle.THIN);
//            cellStyleCenter.setBorderBottom(BorderStyle.THIN);
//            cellStyleCenter.setBorderLeft(BorderStyle.THIN);
//            cellStyleCenter.setWrapText(true);
//            cellStyleCenter.setAlignment(HorizontalAlignment.CENTER);
//            cellStyleCenter.setVerticalAlignment(VerticalAlignment.CENTER);
//
//            cellStyleCenterNoBorder.setAlignment(HorizontalAlignment.CENTER);
//            cellStyleCenterNoBorder.setVerticalAlignment(VerticalAlignment.CENTER);
//            cellStyleCenterNoBorder.setWrapText(true);
//
//            cellStyleBorder.setBorderTop(BorderStyle.THIN);
//            cellStyleBorder.setBorderRight(BorderStyle.THIN);
//            cellStyleBorder.setBorderBottom(BorderStyle.THIN);
//            cellStyleBorder.setBorderLeft(BorderStyle.THIN);
//        }
//
//        for (Company company : companyList) {
//            int companyId = company.getId();
//
//            Row row = null;
//            Cell cell = null;
//
//            // Table header
//            {
//                row = sheet.createRow(rowId);
//                for (int j = 0; j <= colNum; j++) {
//                    cell = row.createCell(j);
//                }
//
//                // Yesterday overall
//                cell = row.getCell(0);
//                cell.setCellValue(date);
//                cell.setCellStyle(cellStyleCenterNoBorder);
//
//                cell = row.getCell(2);
//                cell.setCellValue("User");
//                cell.setCellStyle(cellStyleCenter);
//
//                cell = row.getCell(3);
//                cell.setCellValue("Count");
//                cell.setCellStyle(cellStyleCenter);
//
//                cell = row.getCell(4);
//                cell.setCellValue("Amount");
//                cell.setCellStyle(cellStyleCenter);
//
//                // Month overall
//                cell = row.getCell(6);
//                cell.setCellValue(month + " Total");
//                cell.setCellStyle(cellStyleCenterNoBorder);
//
//                cell = row.getCell(8);
//                cell.setCellValue("User");
//                cell.setCellStyle(cellStyleCenter);
//
//                cell = row.getCell(9);
//                cell.setCellValue("Count");
//                cell.setCellStyle(cellStyleCenter);
//
//                cell = row.getCell(10);
//                cell.setCellValue("Amount");
//                cell.setCellStyle(cellStyleCenter);
//            }
//
//            // Table body
//            {
//                /**
//                 * Company
//                 */
//                {
//                    int startRowId = rowId + 1;
//
//                    row = sheet.createRow(++rowId);
//                    for (int j = 0; j <= colNum; j++) {
//                        cell = row.createCell(j);
//                    }
//
//                    cell = row.getCell(0);
//                    cell.setCellValue(company.getShortName());
//
//                    cell = row.getCell(6);
//                    cell.setCellValue(company.getShortName());
//
//                    String title = "";
//
//                    // Company Cashin
//                    {
//                        title = "Company Cashin";
//
//                        isEmployee = false;
//                        trxType = Constant.CAFE;
//                        trxTypeSub = null;
//                        serviceId = null;
//
//                        // row = sheet.createRow(++rowId);
//                        // for (int j = 0; j <= colNum; j++) {
//                        // 	cell = row.createCell(j);
//                        // }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Adv Salary Cashin
//                    {
//                        title = "Adv Salary Cashin";
//
//                        isEmployee = false;
//                        trxType = Constant.CADB;
//                        trxTypeSub = null;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Cash balance
//                    {
//                        title = "Cash balance";
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = companyUserBalanceList.get(companyId) != null ? companyUserBalanceList.get(companyId).get("count_" + Constant.BALANCE_CAH) : 0;
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            // Count
//                            cell = row.getCell(3);
//                            cellValue = companyUserBalanceList.get(companyId) != null ? companyUserBalanceList.get(companyId).get("count_" + Constant.BALANCE_CAH) : 0;
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            // Amount
//                            cell = row.getCell(4);
//                            cellValue = companyUserBalanceList.get(companyId) != null ? companyUserBalanceList.get(companyId).get(Constant.BALANCE_CAH) : 0;
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cell.setCellValue("NA");
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cell.setCellValue("NA");
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cell.setCellValue("NA");
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Welfare balance
//                    {
//                        title = "Welfare balance";
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = companyUserBalanceList.get(companyId) != null ? companyUserBalanceList.get(companyId).get("count_" + Constant.BALANCE_WFE) : 0;
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            // Count
//                            cell = row.getCell(3);
//                            cellValue = companyUserBalanceList.get(companyId) != null ? companyUserBalanceList.get(companyId).get("count_" + Constant.BALANCE_WFE) : 0;
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            // Amount
//                            cell = row.getCell(4);
//                            cellValue = companyUserBalanceList.get(companyId) != null ? companyUserBalanceList.get(companyId).get(Constant.BALANCE_WFE) : 0;
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cell.setCellValue("NA");
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cell.setCellValue("NA");
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cell.setCellValue("NA");
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Transaction
//                    {
//                        title = "Transaction";
//
//                        isEmployee = true;
//                        trxType = null;
//                        trxTypeSub = null;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Merge cells
//                    {
//                        sheet.addMergedRegion(new CellRangeAddress(
//                                startRowId, //first row (0-based)
//                                rowId, //last row  (0-based)
//                                0, //first column (0-based)
//                                0 //last column  (0-based)
//                        ));
//                        for (int i = startRowId; i <= rowId; i++) {
//                            row = sheet.getRow(i);
//                            for (int j = startRowId; j <= rowId; j++) {
//                                cell = row.getCell(0);
//                                cell.setCellStyle(cellStyleBorder);
//                            }
//                        }
//                        row = sheet.getRow(startRowId);
//                        cell = row.getCell(0);
//                        cell.setCellStyle(cellStyleCenter);
//
//                        sheet.addMergedRegion(new CellRangeAddress(
//                                startRowId, //first row (0-based)
//                                rowId, //last row  (0-based)
//                                6, //first column (0-based)
//                                6 //last column  (0-based)
//                        ));
//                        for (int i = startRowId; i <= rowId; i++) {
//                            row = sheet.getRow(i);
//                            for (int j = startRowId; j <= rowId; j++) {
//                                cell = row.getCell(6);
//                                cell.setCellStyle(cellStyleBorder);
//                            }
//                        }
//                        row = sheet.getRow(startRowId);
//                        cell = row.getCell(6);
//                        cell.setCellStyle(cellStyleCenter);
//                    }
//                }
//
//                /**
//                 * Company transaction
//                 */
//                {
//                    int startRowId = rowId + 1;
//
//                    row = sheet.createRow(++rowId);
//                    for (int j = 0; j <= colNum; j++) {
//                        cell = row.createCell(j);
//                    }
//
//                    cell = row.getCell(0);
//                    cell.setCellValue(company.getShortName() + " Transaction");
//
//                    cell = row.getCell(6);
//                    cell.setCellValue(company.getShortName() + " Transaction");
//
//                    String title = "";
//
//                    // Cashin
//                    {
//                        title = "Cashin";
//
//                        isEmployee = true;
//                        trxType = Constant.CAIN;
//                        trxTypeSub = null;
//                        serviceId = null;
//
//                        // row = sheet.createRow(++rowId);
//                        // for (int j = 0; j <= colNum; j++) {
//                        // 	cell = row.createCell(j);
//                        // }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Cashout
//                    {
//                        title = "Cashout";
//
//                        isEmployee = true;
//                        trxType = Constant.CAUT;
//                        trxTypeSub = null;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // E-Voucher
//                    {
//                        title = "E-Voucher";
//
//                        isEmployee = true;
//                        trxType = Constant.PAYM;
//                        trxTypeSub = null;
//                        serviceId = Constant.PAY_EVOUCHER;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Topup
//                    {
//                        title = "Topup";
//
//                        isEmployee = true;
//                        trxType = Constant.PAYM;
//                        trxTypeSub = null;
//                        serviceId = Constant.PAY_TOPUP;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Bill Payment
//                    {
//                        title = "Bill Payment";
//
//                        isEmployee = true;
//                        trxType = Constant.PAYM;
//                        trxTypeSub = null;
//                        serviceId = Constant.PAY_BILL;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Transfer (Company)
//                    {
//                        title = "Transfer (Company)";
//
//                        isEmployee = false;
//                        trxType = Constant.TRFR;
//                        trxTypeSub = Constant.TRFR_WELFARE;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Transfer (User)
//                    {
//                        title = "Transfer (User)";
//
//                        isEmployee = true;
//                        trxType = Constant.TRFR;
//                        trxTypeSub = Constant.TRFR;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Receive (User)
//                    {
//                        title = "Receive (User)";
//
//                        isEmployee = true;
//                        trxType = Constant.TRFR;
//                        trxTypeSub = Constant.RECV;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Bank Transfer (User)
//                    {
//                        title = "Bank Transfer (User)";
//
//                        isEmployee = true;
//                        trxType = Constant.TRFR;
//                        trxTypeSub = Constant.TRFR_BANK;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Adv Salary
//                    {
//                        title = "Adv Salary";
//
//                        isEmployee = true;
//                        trxType = Constant.ADSA;
//                        trxTypeSub = null;
//                        serviceId = null;
//
//                        row = sheet.createRow(++rowId);
//                        for (int j = 0; j <= colNum; j++) {
//                            cell = row.createCell(j);
//                        }
//
//                        // Yesterday overall
//                        {
//                            isInDay = true;
//
//                            cell = row.getCell(1);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            // User
//                            cell = row.getCell(2);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(3);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(4);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//
//                        // Month overall
//                        {
//                            isInDay = false;
//
//                            cell = row.getCell(7);
//                            cell.setCellValue(title);
//                            cell.setCellStyle(cellStyleLeft);
//
//                            cell = row.getCell(8);
//                            cellValue = countUniqueUserNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(9);
//                            cellValue = countTransactionNumber(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//
//                            cell = row.getCell(10);
//                            cellValue = sumTransactionAmount(companyId, transactions, trxType, trxTypeSub, serviceId,
//                                    isEmployee,
//                                    isInDay);
//                            cell.setCellValue(formatNumber(cellValue));
//                            cell.setCellStyle(cellStyleRight);
//                        }
//                    }
//
//                    // Merge cells
//                    {
//                        sheet.addMergedRegion(new CellRangeAddress(
//                                startRowId, //first row (0-based)
//                                rowId, //last row  (0-based)
//                                0, //first column (0-based)
//                                0 //last column  (0-based)
//                        ));
//                        for (int i = startRowId; i <= rowId; i++) {
//                            row = sheet.getRow(i);
//                            for (int j = startRowId; j <= rowId; j++) {
//                                cell = row.getCell(0);
//                                cell.setCellStyle(cellStyleBorder);
//                            }
//                        }
//                        row = sheet.getRow(startRowId);
//                        cell = row.getCell(0);
//                        cell.setCellStyle(cellStyleCenter);
//
//                        sheet.addMergedRegion(new CellRangeAddress(
//                                startRowId, //first row (0-based)
//                                rowId, //last row  (0-based)
//                                6, //first column (0-based)
//                                6 //last column  (0-based)
//                        ));
//                        for (int i = startRowId; i <= rowId; i++) {
//                            row = sheet.getRow(i);
//                            for (int j = startRowId; j <= rowId; j++) {
//                                cell = row.getCell(6);
//                                cell.setCellStyle(cellStyleBorder);
//                            }
//                        }
//                        row = sheet.getRow(startRowId);
//                        cell = row.getCell(6);
//                        cell.setCellStyle(cellStyleCenter);
//                    }
//                }
//            }
//
//            ++rowId;
//        }
//
//
//        // social report
//        Row row = null;
//        Cell cell = null;
//        String title;
//        // Table header
//
//        {
//            row = sheet.createRow(rowId);
//            for (int j = 0; j <= colNum; j++) {
//                cell = row.createCell(j);
//            }
//
//            // Yesterday overall
//            cell = row.getCell(0);
//            cell.setCellValue(date);
//            cell.setCellStyle(cellStyleCenterNoBorder);
//
//            cell = row.getCell(2);
//            cell.setCellValue("User");
//            cell.setCellStyle(cellStyleCenter);
//
//            cell = row.getCell(3);
//            cell.setCellValue("Count");
//            cell.setCellStyle(cellStyleCenter);
//
//            cell = row.getCell(4);
//            cell.setCellValue("Amount");
//            cell.setCellStyle(cellStyleCenter);
//
//            // Month overall
//            cell = row.getCell(6);
//            cell.setCellValue(month + " Total");
//            cell.setCellStyle(cellStyleCenterNoBorder);
//
//            cell = row.getCell(8);
//            cell.setCellValue("User");
//            cell.setCellStyle(cellStyleCenter);
//
//            cell = row.getCell(9);
//            cell.setCellValue("Count");
//            cell.setCellStyle(cellStyleCenter);
//
//            cell = row.getCell(10);
//            cell.setCellValue("Amount");
//            cell.setCellStyle(cellStyleCenter);
//        }
//
//
//
//        // posts count
//        int startRowId = ++rowId;
//
//        {
//            title = "posts";
//
//            row = sheet.createRow(startRowId);
//
//             for (int j = 0; j <= colNum; j++) {
//             	cell = row.createCell(j);
//             }
//            cell = row.getCell(0);
//            cell.setCellValue("Social");
//
//            cell = row.getCell(6);
//            cell.setCellValue("Social");
//
//            // Yesterday overall
//            {
//                cell = row.getCell(1);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                // User
//                cell = row.getCell(2);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(3);
//                cellValue = socialDailyReport.getPostsQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(4);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//
//            // Month overall
//            {
//                cell = row.getCell(7);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                cell = row.getCell(8);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(9);
//                cellValue = socialMonthlyReport.getPostsQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(10);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//        }
//
//        // like count
//        {
//            title = "like";
//
//            row = sheet.createRow(++rowId);
//            for (int j = 0; j <= colNum; j++) {
//                cell = row.createCell(j);
//            }
//
//            // Yesterday overall
//            {
//                cell = row.getCell(1);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                // User
//                cell = row.getCell(2);
//                cellValue = 0;
//                cell.setCellValue(cellValue);
//
//                cell = row.getCell(3);
//                cellValue = socialDailyReport.getLikeQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(4);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//
//            // Month overall
//            {
//                cell = row.getCell(7);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                cell = row.getCell(8);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(9);
//                cellValue = socialMonthlyReport.getLikeQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(10);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//        }
//        // posts market count
//        {
//            title = "posts market";
//
//            row = sheet.createRow(++rowId);
//            for (int j = 0; j <= colNum; j++) {
//                cell = row.createCell(j);
//            }
//
//            // Yesterday overall
//            {
//                cell = row.getCell(1);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                // User
//                cell = row.getCell(2);
//                cellValue = 0;
//                cell.setCellValue(cellValue);
//
//                cell = row.getCell(3);
//                cellValue = socialDailyReport.getPostsMarketQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(4);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//
//            // Month overall
//            {
//                cell = row.getCell(7);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                cell = row.getCell(8);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(9);
//                cellValue = socialMonthlyReport.getPostsMarketQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(10);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//        }
//        // comment count
//        {
//            title = "comments";
//
//            row = sheet.createRow(++rowId);
//            for (int j = 0; j <= colNum; j++) {
//                cell = row.createCell(j);
//            }
//
//            // Yesterday overall
//            {
//                cell = row.getCell(1);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                // User
//                cell = row.getCell(2);
//                cellValue = 0;
//                cell.setCellValue(cellValue);
//
//                cell = row.getCell(3);
//                cellValue = socialDailyReport.getCommentQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(4);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//
//            // Month overall
//            {
//                cell = row.getCell(7);
//                cell.setCellValue(title);
//                cell.setCellStyle(cellStyleLeft);
//
//                cell = row.getCell(8);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(9);
//                cellValue = socialMonthlyReport.getCommentQuantity();
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//
//                cell = row.getCell(10);
//                cellValue = 0;
//                cell.setCellValue(formatNumber(cellValue));
//                cell.setCellStyle(cellStyleRight);
//            }
//
//            // Merge cells
//            {
//                sheet.addMergedRegion(new CellRangeAddress(
//                        startRowId, //first row (0-based)
//                        rowId, //last row  (0-based)
//                        0, //first column (0-based)
//                        0 //last column  (0-based)
//                ));
//                for (int i = startRowId; i <= rowId; i++) {
//                    row = sheet.getRow(i);
//                    for (int j = startRowId; j <= rowId; j++) {
//                        cell = row.getCell(0);
//                        cell.setCellStyle(cellStyleBorder);
//                    }
//                }
//                row = sheet.getRow(startRowId);
//                cell = row.getCell(0);
//                cell.setCellStyle(cellStyleCenter);
//
//                sheet.addMergedRegion(new CellRangeAddress(
//                        startRowId, //first row (0-based)
//                        rowId, //last row  (0-based)
//                        6, //first column (0-based)
//                        6 //last column  (0-based)
//                ));
//                for (int i = startRowId; i <= rowId; i++) {
//                    row = sheet.getRow(i);
//                    for (int j = startRowId; j <= rowId; j++) {
//                        cell = row.getCell(6);
//                        cell.setCellStyle(cellStyleBorder);
//                    }
//                }
//                row = sheet.getRow(startRowId);
//                cell = row.getCell(6);
//                cell.setCellStyle(cellStyleCenter);
//            }
//
//        }
//
//
//
//        //Auto size all the columns
//        for (int column = 0; column < sheet.getRow(0).getPhysicalNumberOfCells(); column++) {
//            sheet.autoSizeColumn(column);
//        }
//    }
//
//    private static long countUniqueUserNumber(
//            int companyId, List<Transaction> transactionList,
//            String transactionType, String transactionTypeSub, String serviceId,
//            boolean isEmployee, boolean isInDay) {
//        List<Transaction> filteredList = transactionList;
//        filteredList = getFilteredListByCompanyId(filteredList, companyId);
//        filteredList = getFilteredListByUserType(filteredList, isEmployee);
//        filteredList = getFilteredListByStartDate(filteredList, isInDay);
//
//        List<Integer> userIdList = new ArrayList<Integer>();
//        for (Transaction transaction : filteredList) {
//            if (userIdList.contains(transaction.getUserId())) {
//                continue;
//            }
//
//            if (transactionType != null && transactionType.equals(transaction.getTrxType()) == false) {
//                continue;
//            }
//
//            if (transactionTypeSub != null && transactionTypeSub.equals(transaction.getTrxTypeSub()) == false) {
//                continue;
//            }
//
//            if (serviceId != null && serviceId.equals(transaction.getServiceId()) == false) {
//                continue;
//            }
//
//            userIdList.add(transaction.getUserId());
//        }
//
//        return userIdList.size();
//    }
//
//    private static long countTransactionNumber(
//            int companyId, List<Transaction> transactionList,
//            String transactionType, String transactionTypeSub, String serviceId,
//            boolean isEmployee, boolean isInDay) {
//        int count = 0;
//
//        List<Transaction> filteredList = transactionList;
//        filteredList = getFilteredListByCompanyId(filteredList, companyId);
//        filteredList = getFilteredListByUserType(filteredList, isEmployee);
//        filteredList = getFilteredListByStartDate(filteredList, isInDay);
//
//        for (Transaction transaction : filteredList) {
//            if (transactionType != null && transactionType.equals(transaction.getTrxType()) == false) {
//                continue;
//            }
//
//            if (transactionTypeSub != null && transactionTypeSub.equals(transaction.getTrxTypeSub()) == false) {
//                continue;
//            }
//
//            if (serviceId != null && serviceId.equals(transaction.getServiceId()) == false) {
//                continue;
//            }
//
//            count = count + 1;
//        }
//
//        return count;
//    }
//
//    private static long sumTransactionAmount(
//            int companyId, List<Transaction> transactionList,
//            String transactionType, String transactionTypeSub, String serviceId,
//            boolean isEmployee, boolean isInDay) {
//        long amount = 0;
//
//        List<Transaction> filteredList = transactionList;
//        filteredList = getFilteredListByCompanyId(filteredList, companyId);
//        filteredList = getFilteredListByUserType(filteredList, isEmployee);
//        filteredList = getFilteredListByStartDate(filteredList, isInDay);
//
//        for (Transaction transaction : filteredList) {
//            if (transactionType != null && transactionType.equals(transaction.getTrxType()) == false) {
//                continue;
//            }
//
//            if (transactionTypeSub != null && transactionTypeSub.equals(transaction.getTrxTypeSub()) == false) {
//                continue;
//            }
//
//            if (serviceId != null && serviceId.equals(transaction.getServiceId()) == false) {
//                continue;
//            }
//
//            amount = amount + transaction.getAmount();
//        }
//
//        return amount;
//    }
//
//    private static List<Transaction> getFilteredListByCompanyId(List<Transaction> transactionList, int companyId) {
//        List<Transaction> filteredList = transactionList;
//
//        filteredList = filteredList.stream()
//                .filter(transaction -> transaction.getCompanyId() == companyId)
//                .collect(Collectors.toList());
//
//        return filteredList;
//    }
//
//    private static List<Transaction> getFilteredListByUserType(List<Transaction> transactionList, boolean isEmployee) {
//        List<Transaction> filteredList = transactionList;
//
//        if (isEmployee == true) {
//            filteredList = filteredList.stream()
//                    .filter(transaction -> transaction.getUserTypeId() == Constant.USER_TYPE_ID_EMPLOYEE)
//                    .collect(Collectors.toList());
//        } else {
//            filteredList = filteredList.stream()
//                    .filter(transaction -> transaction.getUserTypeId() == Constant.USER_TYPE_ID_ENTERPRISE)
//                    .collect(Collectors.toList());
//        }
//
//        return filteredList;
//    }
//
//    private static List<Transaction> getFilteredListByStartDate(List<Transaction> transactionList, boolean isInDay) {
//        List<Transaction> filteredList = transactionList;
//
//        if (isInDay == true) {
//            filteredList = filteredList.stream()
//                    .filter(transaction -> transaction.isInDay() == true)
//                    .collect(Collectors.toList());
//        } else {
//            filteredList = filteredList.stream()
//                    .filter(transaction -> transaction.isInMonth() == true)
//                    .collect(Collectors.toList());
//        }
//
//        return filteredList;
//    }
//
//    public static String formatNumber(long number) {
//        String numberString = String.valueOf(number);
//        String formattedNumberString = String.format("%,.0f", new BigDecimal(numberString));
//        return formattedNumberString;
//    }
//}
