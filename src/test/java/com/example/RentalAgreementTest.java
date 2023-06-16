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

	@Test
	public void isInputDiscountGreaterThanRange() { 
		
		String toolCode = "JAKR";
		String rentalDays = "5";
		String discount = "101";
		String date = "9/3/15";
	    
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
	
	@Test
	public void calcIndependenceDaySaturdayNoHolidayChargeIsWeekendCharge() { 
		
		String toolCode = "LADW";
		String rentalDays = "3";
		String discount = "10";
		String date = "7/2/20";
		String price = "1.99";
		String chargeDays = "2";
		
		RentalAgreementForm rentalAgreementForm = new RentalAgreementForm();
	    validateInputRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    buildRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
		
        BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Charge Days", Integer.parseInt(chargeDays), rentalAgreement.getChargeDays());
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	    
	}
	
	@Test
	public void calcIndependenceDaySaturdayIsHolidayChargeNoWeekendCharge() { 
		
		String toolCode = "CHNS";
		String rentalDays = "5";
		String discount = "25";
		String date = "7/2/15";
		String price = "1.49";
		String chargeDays = "3";
		
		RentalAgreementForm rentalAgreementForm = new RentalAgreementForm();
	    validateInputRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    buildRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
		
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Charge Days", Integer.parseInt(chargeDays), rentalAgreement.getChargeDays());
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	@Test
	public void calcLaborDayNoHolidayCharge() { 
		
		String toolCode = "JAKD";
		String rentalDays = "6";
		String discount = "0";
		String date = "9/3/15";
		String price = "2.99";
		String chargeDays = "3";
		
		RentalAgreementForm rentalAgreementForm = new RentalAgreementForm();
	    validateInputRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    buildRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
		
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Charge Days", Integer.parseInt(chargeDays), rentalAgreement.getChargeDays());
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	@Test
	public void calcIndependenceDaySaturdayNoHolidayChargeNoWeekendCharge() { 
		
		String toolCode = "JAKR";
		String rentalDays = "9";
		String discount = "0";
		String date = "7/2/15";
		String price = "2.99";
		String chargeDays = "5";
		
		RentalAgreementForm rentalAgreementForm = new RentalAgreementForm();
	    validateInputRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    buildRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
		
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Charge Days", Integer.parseInt(chargeDays), rentalAgreement.getChargeDays());
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	@Test
	public void calcLowChargeDays() { 
		
		String toolCode = "JAKR";
		String rentalDays = "4";
		String discount = "50";
		String date = "7/2/20";
		String price = "2.99";
		String chargeDays = "1";
		
	    RentalAgreementForm rentalAgreementForm = new RentalAgreementForm();
	    validateInputRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    buildRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Charge Days", Integer.parseInt(chargeDays), rentalAgreement.getChargeDays());
	    assertEquals("Final Charge", finalPrice, rentalAgreement.getFinalCharge());
	}
	
	@Test
	public void calcNoChargeDays() { 
		
		String toolCode = "JAKR";
		String rentalDays = "1";
		String discount = "50";
		String date = "7/3/23";
		String price = "2.99";
		String chargeDays = "0";
		
	    RentalAgreementForm rentalAgreementForm = new RentalAgreementForm();
	    validateInputRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    buildRentalAgreement(rentalAgreement, toolCode, rentalDays, discount, date);
	    
	    BigDecimal finalPrice = calculatePrice(price, chargeDays, discount);
	    
	    assertEquals("Charge Days", Integer.parseInt(chargeDays), rentalAgreement.getChargeDays());
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
	@ValueSource(strings = {"0", "-5", ".9", "T", "^", ""})
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
	@ValueSource(strings = {"-2147483647", "-10", ".9", "58.5", "300", "P", "@", ""})
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
		Tool tool = new Tool();
		ToolRentalCharge toolRentalCharge = new ToolRentalCharge();
		
        ToolRentalCharge result = XMLParser.findToolRentalChargeByToolType(toolRentalCharge, toolType);

        assertEquals(toolType, result.getToolType());
        assertNotNull(result.getDailyCharge());
        assertNotNull(result.getWeekdayCharge());
        assertNotNull(result.getWeekendCharge());
        assertNotNull(result.getHolidayCharge());
	}
	
	
	public BigDecimal calculatePrice(String charge, String days, String discount) {
		BigDecimal price = new BigDecimal(charge);
        BigDecimal chargeDays = new BigDecimal(days);
        BigDecimal discountPercent = new BigDecimal(discount);
        BigDecimal totalPrice = price.multiply(chargeDays);
        BigDecimal discountAmount = totalPrice.multiply(discountPercent.divide(new BigDecimal(100)));
        discountAmount = discountAmount.setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalPrice = totalPrice.subtract(discountAmount);
        
        return finalPrice;
	}
	
	public RentalAgreement buildRentalAgreement(RentalAgreement rentalAgreement, String toolCode, String rentalDays, String discount, String date) {
	    rentalAgreement.setToolCode(toolCode);
	    try {
		    int days = Integer.parseInt(rentalDays);
		    rentalAgreement.setRentalDays(days);

		    int discountPercent = Integer.parseInt(discount);
		    rentalAgreement.setDiscountPercent(discountPercent);

		    LocalDate checkoutDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("M/d/yy"));
		    rentalAgreement.setCheckoutDate(checkoutDate);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		ToolRentalCharge toolRentalCharge = new ToolRentalCharge();
		toolRentalCharge.setToolType(rentalAgreement.getToolType());
		toolRentalCharge.findToolRentalChargeByToolType();
		rentalAgreement.setDailyRentalCharge(toolRentalCharge.getDailyCharge());
		rentalAgreement.calculateValues(toolRentalCharge);
		return rentalAgreement;
	}
	
	public RentalAgreement validateInputRentalAgreement(RentalAgreement rentalAgreement, String toolCode, String rentalDays, String discount, String date) {
	    validateInputToolCode(toolCode);
	    validateInputRentalDays(rentalDays);
	    validateInputDiscount(discount);
	    validateInputDate(date);
		return rentalAgreement;
	}
}


