package com.example;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.rentalagreement.*;


class RentalAgreementTest extends RentalAgreementForm {
	
	DateTimeFormatter dateInputFormatter = DateTimeFormatter.ofPattern("M/d/yy");

	/*Test 1:
     * Tool code: JAKR 
     * Checkout date: 9/3/15
     * Rental days: 5
     * Discount: 101%
     * Fails on discount out of range
     */
	@Test
	public void isInputDiscountGreaterThanRange() { 
		String discount = "101";
	    
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
	    
        try {
            boolean result = validateInputDiscount(discount);

            assertFalse(result);
            assertEquals(ERROR_DISCOUNT_OUT_OF_RANGE, outputStream.toString().trim());
        } finally {
            System.setOut(originalOut);
        }
	}
	
    /*Test 2:
     * Tool code: LADW
     * Checkout date: 7/2/20
     * Rental days: 3
     * Discount: 10%
     * Final price: $3.58
     */
	@Test
	public void calcIndependenceDaySaturdayNoHolidayChargeIsWeekendCharge() { 
		
		String toolCode = "LADW";
		String toolType = "Ladder" ;
		String toolBrand = "Werner" ;
		int rentalDays = 3;
		int discount = 10;
		String dateString = "7/2/20";
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		String price = "1.99";
		String chargeDays = "2";
		
		buildRentalAgreement(toolCode, toolType, toolBrand, rentalDays, discount, date);
		calcRentalAgreement();
        BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	
    /*Test 3:
     * Tool code: CHNS
     * Checkout date: 7/2/15
     * Rental days: 5
     * Discount: 25%
     * Final price: $3.35
     */	
	@Test
	public void calcIndependenceDaySaturdayIsHolidayChargeNoWeekendCharge() { 
		
		String toolCode = "CHNS";
		String toolType = "Chainsaw" ;
		String toolBrand = "Stihl" ;
		int rentalDays = 5;
		int discount = 25;
		String dateString = "7/2/15";
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		String price = "1.49";
		String chargeDays = "3";
		
		buildRentalAgreement(toolCode, toolType, toolBrand, rentalDays, discount, date);
		calcRentalAgreement();
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
    /*Test 4:
     * Tool code: JAKD
     * Checkout date: 9/3/15
     * Rental days: 6
     * Discount: 0%
     * Final price: $9.97
     */	
	@Test
	public void calcLaborDayNoHolidayCharge() { 
		
		String toolCode = "JAKD";
		String toolType = "Jackhammer" ;
		String toolBrand = "DeWalt" ;
		int rentalDays = 6;
		int discount = 0;
		String dateString = "9/3/15";
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		String price = "2.99";
		String chargeDays = "3";
		
		buildRentalAgreement(toolCode, toolType, toolBrand, rentalDays, discount, date);
		calcRentalAgreement();
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
    /*Test 5:
     * Tool code: JAKR
     * Checkout date: 7/2/15
     * Rental days: 9
     * Discount: 0%
     * Final price: $14.95
     */	
	@Test
	public void calcIndependenceDaySaturdayNoHolidayChargeNoWeekendCharge() { 
		
		String toolCode = "JAKR";
		String toolType = "Jackhammer" ;
		String toolBrand = "Ridgid" ;
		int rentalDays = 9;
		int discount = 0;
		String dateString = "7/2/15";
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		String price = "2.99";
		String chargeDays = "5";
		
		buildRentalAgreement(toolCode, toolType, toolBrand, rentalDays, discount, date);
		calcRentalAgreement();
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}

	/*Test 6:
     * Tool code: JAKR
     * Checkout date: 7/2/20
     * Rental days: 4
     * Discount: 50%
     * Final price: $1.49
     */	
	@Test
	public void calcLowChargeDays() { 
		
		String toolCode = "JAKR";
		String toolType = "Jackhammer" ;
		String toolBrand = "Ridgid" ;
		int rentalDays = 4;
		int discount = 50;
		String dateString = "7/2/20";
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		String price = "2.99";
		String chargeDays = "1";
		
		buildRentalAgreement(toolCode, toolType, toolBrand, rentalDays, discount, date);
		calcRentalAgreement();
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	@Test
	public void calcNoChargeDays() { 
		
		String toolCode = "JAKR";
		String toolType = "Jackhammer" ;
		String toolBrand = "Ridgid" ;
		int rentalDays = 1;
		int discount = 50;
		String dateString = "7/3/23";
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		String price = "2.99";
		String chargeDays = "0";
		
		buildRentalAgreement(toolCode, toolType, toolBrand, rentalDays, discount, date);
		calcRentalAgreement();
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	
	@Test
	public void isZeroRentalDays() { 
		
		String rentalDays = "0";
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
	    
        try {
            boolean result = validateInputRentalDays(rentalDays);

            assertFalse(result);
            assertEquals(ERROR_RENTAL_DAYS_LT_ONE, outputStream.toString().trim());
        } finally {
            System.setOut(originalOut);
        }
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"JAKR", "LADW", "CHNS", "JAKD", "jakd"})
	public void isValidToolCode(String toolCode) {
		boolean result = validateInputToolCode(toolCode);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"AAAA", "Z", "$", ""})
	public void isNotValidToolCode(String toolCode) {
		boolean result = validateInputToolCode(toolCode);
		assertFalse(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"1", "999", "2147483647"})
	public void isValidRentalDays(String rentalDays) {
		boolean result = validateInputRentalDays(rentalDays);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"-9999999999", "0", "-5", ".9", "9999999999", "T", "^", ""})
	public void isNotValidRentalDays(String rentalDays) {
		boolean result = validateInputRentalDays(rentalDays);
		assertFalse(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"0", "1", "45", "99", "100"})
	public void isValidDiscount(String discount) {
		boolean result = validateInputDiscount(discount);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"-9999999999", "-2147483647", "-10", ".9", "58.5", "300", "P", "@", ""})
	public void isNotValidDiscount(String discount) {
		boolean result = validateInputDiscount(discount);
		assertFalse(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"01/01/01", "1/1/99", "7/14/10", "12/31/11", "06/15/65", "08/22/40"})
	public void isValidDate(String date) {
		boolean result = validateInputDate(date);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"99/1/22", "12/55/03", "06/15", "m", "!", ""})
	public void isNotValidDate(String date) {
		boolean result = validateInputDate(date);
		assertFalse(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"Ladder", "Chainsaw", "Jackhammer"})
	public void existsInToolRentalCharge(String toolType) {
		ToolRentalCharge toolRentalCharge = new ToolRentalCharge();
		
        ToolRentalCharge result = XMLParser.findToolRentalChargeByToolType(toolRentalCharge, toolType);

        assertEquals(toolType, result.getToolType());
        assertNotNull(result.getDailyCharge());
        assertNotNull(result.getWeekdayCharge());
        assertNotNull(result.getWeekendCharge());
        assertNotNull(result.getHolidayCharge());
	}
	
	
	@ParameterizedTest
	@ValueSource(strings = {"9/7/15", "7/3/20", "7/3/15", "7/4/23"})
	public void isHoliday(String dateString) {
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		boolean result = CalendarValidator.isHoliday(date);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"9/4/15", "7/4/20", "7/4/15", "7/3/23"})	
	public void isNotHoliday(String dateString) {
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		boolean result = CalendarValidator.isHoliday(date);
		assertFalse(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"9/5/15", "7/4/20", "7/5/15", "7/2/23"})
	public void isWeekend(String dateString) {
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		boolean result = CalendarValidator.isWeekend(date);
		assertTrue(result);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"9/7/15", "7/6/20", "7/2/15", "7/4/23"})	
	public void isNotWeekend(String dateString) {
		LocalDate date = LocalDate.parse(dateString, dateInputFormatter);
		boolean result = CalendarValidator.isWeekend(date);
		assertFalse(result);
	}
	
	public BigDecimal calculatePrice(String charge, String days, int discount) {
		BigDecimal price = new BigDecimal(charge);
        BigDecimal chargeDays = new BigDecimal(days);
        BigDecimal discountPercent = new BigDecimal(discount);
        BigDecimal totalPrice = price.multiply(chargeDays);
        BigDecimal discountAmount = totalPrice.multiply(discountPercent.divide(new BigDecimal(100)));
        discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalPrice = totalPrice.subtract(discountAmount);
        
        return finalPrice;
	}
	
    private void buildRentalAgreement(String toolCode, String toolType, String toolBrand, int rentalDays, int discount,
			LocalDate date) {
		rentalAgreement.setToolCode(toolCode);
		rentalAgreement.setToolType(toolType);
		rentalAgreement.setToolBrand(toolBrand);
		rentalAgreement.setRentalDays(rentalDays);
		rentalAgreement.setDiscountPercent(discount);
		rentalAgreement.setCheckoutDate(date);
	}
}


