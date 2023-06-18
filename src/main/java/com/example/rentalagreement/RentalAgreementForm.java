package com.example.rentalagreement;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RentalAgreementForm {
	
	protected static final String ERROR_RENTAL_DAYS_LT_ONE = "Rental day count must be entered as a whole number, 1 or greater.";
	protected static final String ERROR_DISCOUNT_OUT_OF_RANGE = "The discount percent must be entered as a whole between 0 and 100.";
	private static final String PROMPT_CODE = "Enter the tool code: ";
	private static final String PROMPT_RENTAL_DAYS = "Enter the number of rental days as a whole number: ";
	private static final String PROMPT_DISCOUNT = "Enter the percent discount as a whole number: ";
	private static final String PROMPT_DATE = "Enter the checkout date as MM/DD/YY: ";
	private static final Logger logger = Logger.getLogger(RentalAgreementForm.class.getName());
	
	protected RentalAgreement rentalAgreement;
	private Scanner scanner;
	
	public RentalAgreementForm() {
		rentalAgreement = new RentalAgreement();
        scanner = new Scanner(System.in);
    }
	
	public static void main(String[] args)
    {	
		RentalAgreementForm form = new RentalAgreementForm();
		try {
			form.promptUser();
			form.calcRentalAgreement();
			form.printRentalAgreement();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		} finally
		{
			form.scanner.close();
		}
    }
	
	private void promptUser() {
		getInputValue(scanner, PROMPT_CODE, this::validateInputToolCode);
		getInputValue(scanner, PROMPT_RENTAL_DAYS, this::validateInputRentalDays);
		getInputValue(scanner, PROMPT_DISCOUNT, this::validateInputDiscount);
		getInputValue(scanner, PROMPT_DATE, this::validateInputDate);
	}

	protected void calcRentalAgreement() {
		ToolRentalCharge toolRentalCharge = new ToolRentalCharge();
		toolRentalCharge.setToolType(rentalAgreement.getToolType());
		toolRentalCharge.findToolRentalChargeByToolType();
		rentalAgreement.setDailyRentalCharge(toolRentalCharge.getDailyCharge());
		rentalAgreement.calculateValues(toolRentalCharge);
	}
	
	protected void printRentalAgreement() {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yy");
		DecimalFormat currencyFormatter = new DecimalFormat("$#,##0.00");
		NumberFormat percentFormatter = NumberFormat.getPercentInstance();
		
		StringBuilder sb = new StringBuilder();
		sb.append("Tool Code: ").append(rentalAgreement.getToolCode()).append("\n");
		sb.append("Tool Type: ").append(rentalAgreement.getToolType()).append("\n");
		sb.append("Tool Brand: ").append(rentalAgreement.getToolBrand()).append("\n");
		sb.append("Rental Days: ").append(rentalAgreement.getRentalDays()).append("\n");
		sb.append("Check Out Date: ").append(rentalAgreement.getCheckoutDate().format(dateFormatter)).append("\n");
		sb.append("Due Date: ").append(rentalAgreement.getDueDate().format(dateFormatter)).append("\n");
		sb.append("Daily Rental Charge: ").append(currencyFormatter.format(rentalAgreement.getDailyRentalCharge())).append("\n");
		sb.append("Charge Days: ").append(rentalAgreement.getChargeDays()).append("\n");
		sb.append("Pre-discount Charge: ").append(currencyFormatter.format(rentalAgreement.getOriginalCharge())).append("\n");
		sb.append("Discount Percent: ").append(percentFormatter.format(rentalAgreement.getDiscountPercent() / 100.0)).append("\n");
		sb.append("Discount Amount: ").append(currencyFormatter.format(rentalAgreement.getDiscountAmount())).append("\n");
		sb.append("Final Charge: ").append(currencyFormatter.format(rentalAgreement.getFinalCharge())).append("\n");

		String text = sb.toString();
		System.out.println(text);
	}

	private void getInputValue(Scanner scanner, String prompt, Function<String, Boolean> validator) {
		String inputValue = "";
		boolean nextPrompt = false;
		
		while (!nextPrompt) {
	        System.out.print(prompt);
	        try {
	        	inputValue = scanner.nextLine();
	        	inputValue = inputValue.trim();
	        	if (inputValue == null || inputValue.trim().isEmpty()) {
	                continue;
	            }
	        } catch (Exception e) {
	            scanner.nextLine();
	        }
	        nextPrompt = validator.apply(inputValue);
	        if (!nextPrompt) {
	        	scanner.nextLine();
	        }
	    }
	}
	
	protected Boolean validateInputToolCode(String inputToolCode) {
		Tool tool = new Tool();
		tool.setCode(inputToolCode);
		
		tool.findToolRecordByToolCode();
		if (tool.getType() != null) {
			rentalAgreement.setToolCode(tool.getCode());
			rentalAgreement.setToolType(tool.getType());
			rentalAgreement.setToolBrand(tool.getBrand());
			return true;
		}
		return false;
	}

	protected Boolean validateInputRentalDays(String inputRentalDays) {
		try {
	        int rentalDays = Integer.parseInt(inputRentalDays);
	        if (rentalDays < 1) {
	        	System.out.println(ERROR_RENTAL_DAYS_LT_ONE);
	        	return false;
	        }
	        rentalAgreement.setRentalDays(rentalDays);
	    } catch (Exception e) {
	    	logger.log(Level.WARNING, "An exception occurred: " + e.getMessage() + " " + ERROR_RENTAL_DAYS_LT_ONE);
	    	return false;
	    }
		
        return true;
		
	}

	protected Boolean validateInputDiscount(String inputDiscount) {
		try {
	        int discount = Integer.parseInt(inputDiscount);
	        if (discount < 0 || discount > 100) {
	        	System.out.println(ERROR_DISCOUNT_OUT_OF_RANGE);
	        	return false;
	        }
	        rentalAgreement.setDiscountPercent(discount);
	    } catch (Exception e) {
	    	logger.log(Level.WARNING, "An exception occurred: " + e.getMessage() + " " + ERROR_DISCOUNT_OUT_OF_RANGE);
	    	return false;
	    }

		return true;
	}

	protected Boolean validateInputDate(String inputDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yy");

	    try {
	        LocalDate date = LocalDate.parse(inputDate, formatter);
	        rentalAgreement.setCheckoutDate(date);
	    } catch (Exception e) {
	    	logger.log(Level.WARNING, "Invalid date: " + e.getMessage());
	    	return false;
	    }
	    return true;
	}
}