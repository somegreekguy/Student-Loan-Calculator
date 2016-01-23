package model;

public class Loan {
	//public static

	public double computePayment(String principalString, String periodString, String interestString, String graceInterestString, String gracePeriodString, String fixedInterestString) throws NumberFormatException, NullPointerException {
		double principal = Double.parseDouble(principalString);
		double period = Double.parseDouble(periodString);
		double interest = Double.parseDouble(interestString);
		double graceInterest = Double.parseDouble(graceInterestString);
		double gracePeriod = Double.parseDouble(gracePeriodString);
		double fixedInterest = Double.parseDouble(fixedInterestString);

		if (principal < 0 || period < 0 || interest < 0 || graceInterest < 0 || gracePeriod < 0 || fixedInterest < 0) {
			throw new NumberFormatException("Number cannot be negative.");
		}
		if (interest > 99 || interest < 1) {
			throw new NumberFormatException("Interest must be [0,100]");
		}
		if (period <= gracePeriod ) {
			throw new NumberFormatException("Period must be greater than gracePeriod");
		}
		
		double mnthIntrst = (interest/100 + fixedInterest/100) /12;
		double payment = (mnthIntrst * principal) / (1 - Math.pow(1 + mnthIntrst, -period));
		return payment + (graceInterest / gracePeriod);
	}

	public double computeGraceInterest(String principalString, String gracePeriodString, String interestString, String fixedInterestString) throws NumberFormatException, NullPointerException {
		double principal = Double.parseDouble(principalString);
		double gracePeriod = Double.parseDouble(gracePeriodString);
		double interest = Double.parseDouble(interestString);
		double fixedInterest = Double.parseDouble(fixedInterestString);

		return principal * ((interest/100 + fixedInterest/100) /12) * gracePeriod;
	}

}
