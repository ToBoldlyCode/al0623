package com.example.rentalagreement;

import java.math.BigDecimal;


public class ToolRentalCharge {

	private String toolType;
	private BigDecimal dailyCharge;
	private Boolean weekdayCharge;
	private Boolean weekendCharge;
	private Boolean HolidayCharge;
	
	public String getToolType() {
		return toolType;
	}

	public void setToolType(String toolType) {
		this.toolType = toolType;
	}

	public BigDecimal getDailyCharge() {
		return dailyCharge;
	}



	public void setDailyCharge(BigDecimal dailyCharge) {
		this.dailyCharge = dailyCharge;
	}

	public Boolean getWeekdayCharge() {
		return weekdayCharge;
	}



	public void setWeekdayCharge(Boolean weekdayCharge) {
		this.weekdayCharge = weekdayCharge;
	}



	public Boolean getWeekendCharge() {
		return weekendCharge;
	}



	public void setWeekendCharge(Boolean weekendCharge) {
		this.weekendCharge = weekendCharge;
	}



	public Boolean getHolidayCharge() {
		return HolidayCharge;
	}



	public void setHolidayCharge(Boolean holidayCharge) {
		HolidayCharge = holidayCharge;
	}


	public ToolRentalCharge() {
		
	}

	public ToolRentalCharge(String toolType, Boolean weekdayCharge, Boolean weekendCharge, Boolean holidayCharge) {
		this.toolType = toolType;
		this.weekdayCharge = weekdayCharge;
		this.weekendCharge = weekendCharge;
		HolidayCharge = holidayCharge;
	}
	
	public ToolRentalCharge findToolRentalChargeByToolType() {
		XMLParser.findToolRentalChargeByToolType(this, this.toolType);
		return this;
	}
	
}