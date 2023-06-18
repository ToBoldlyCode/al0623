package com.example.rentalagreement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class RentalAgreement {

	private String toolCode;
	private String toolType;
	private String toolBrand;
	private int rentalDays;
	private LocalDate checkoutDate;
	private LocalDate dueDate;
	private BigDecimal dailyRentalCharge;
	private int chargeDays;
	private BigDecimal originalCharge;
	private int discountPercent;
	private BigDecimal discountAmount;
	private BigDecimal finalCharge;
	
	public String getToolCode() {
		return toolCode;
	}

	public void setToolCode(String toolCode) {
		this.toolCode = toolCode;
	}

	public String getToolType() {
		return toolType;
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	public String getToolBrand() {
		return toolBrand;
	}

	public void setToolBrand(String toolBrand) {
		this.toolBrand = toolBrand;
	}

	public int getRentalDays() {
		return rentalDays;
	}

	public void setRentalDays(int rentalDays) {
		this.rentalDays = rentalDays;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getDailyRentalCharge() {
		return dailyRentalCharge;
	}

	public void setDailyRentalCharge(BigDecimal dailyRentalCharge) {
		this.dailyRentalCharge = dailyRentalCharge;
	}

	public int getChargeDays() {
		return chargeDays;
	}

	public void setChargeDays(int chargeDays) {
		this.chargeDays = chargeDays;
	}

	public BigDecimal getOriginalCharge() {
		return originalCharge;
	}

	public void setOriginalCharge(BigDecimal total) {
		this.originalCharge = total;
	}

	public int getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(int discountPercent) {
		this.discountPercent = discountPercent;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getFinalCharge() {
		return finalCharge;
	}

	public void setFinalCharge(BigDecimal finalCharge) {
		this.finalCharge = finalCharge;
	}

	public RentalAgreement() {
		
	}

	public RentalAgreement(String toolCode, String toolType, String toolBrand, int rentalDays, LocalDate checkoutDate,
			LocalDate dueDate, BigDecimal dailyRentalCharge, int chargeDays, BigDecimal originalCharge, int discountPercent,
			BigDecimal discountAmount, BigDecimal finalCharge) {
		this.toolCode = toolCode;
		this.toolType = toolType;
		this.toolBrand = toolBrand;
		this.rentalDays = rentalDays;
		this.checkoutDate = checkoutDate;
		this.dueDate = dueDate;
		this.dailyRentalCharge = dailyRentalCharge;
		this.chargeDays = chargeDays;
		this.originalCharge = originalCharge;
		this.discountPercent = discountPercent;
		this.discountAmount = discountAmount;
		this.finalCharge = finalCharge;
	}
	
	public void calculateValues(ToolRentalCharge toolRentalCharge) {
		
		calculateDueDate();
		calculateChargeDays(toolRentalCharge);
		calculateOriginalCharge();
		calculateDiscountAmount();
		calulateFinalCharge();
	}

	private void calculateDueDate() {
		int daysToAdd = this.getRentalDays();
		LocalDate futureDate = this.getCheckoutDate().plusDays(daysToAdd);
		this.setDueDate(futureDate);
	}
	
	
	private void calculateChargeDays(ToolRentalCharge toolRentalCharge) {
		int daysToCharge = this.rentalDays;
		
		LocalDate date = this.getCheckoutDate().plusDays(1);
		while(!date.isAfter(this.getDueDate())) {
			if((!toolRentalCharge.getWeekendCharge() && CalendarValidator.isWeekend(date)) ||
				(!toolRentalCharge.getHolidayCharge() && CalendarValidator.isHoliday(date))) {
				daysToCharge--;
			}
			date = date.plusDays(1);
		}

		this.setChargeDays(daysToCharge);
		
	}
	
	private void calculateOriginalCharge() {
		int daysToCharge = this.getChargeDays();
		BigDecimal dailyCharge = this.getDailyRentalCharge();
		BigDecimal total = dailyCharge.multiply(BigDecimal.valueOf(daysToCharge));
		total = total.setScale(2, RoundingMode.HALF_UP);
		this.setOriginalCharge(total);
	}
	
	private void calculateDiscountAmount() {
		BigDecimal orignalCharge = this.getOriginalCharge();
		int discountPercent = this.getDiscountPercent();
		BigDecimal total = orignalCharge.multiply(BigDecimal.valueOf(discountPercent)).divide(BigDecimal.valueOf(100));
		total = total.setScale(2, RoundingMode.HALF_UP);
		this.setDiscountAmount(total);
	}
	
	private void calulateFinalCharge() {
		BigDecimal orignalCharge = this.getOriginalCharge();
		BigDecimal discountCharge = this.getDiscountAmount();
		BigDecimal total = orignalCharge.subtract(discountCharge);
		total = total.setScale(2, RoundingMode.HALF_UP);
		this.setFinalCharge(total);
	}
	
}