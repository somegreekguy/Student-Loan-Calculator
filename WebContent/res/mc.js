function validate() {
	var principal = document.getElementById("principal").value;
	var principalError = document.getElementById("principalError");
	var interest = document.getElementById("interest").value;
	var interestError = document.getElementById("interestError");
	var period = document.getElementById("period").value;
	var periodError = document.getElementById("periodError");
	var grace = document.getElementById("grace").checked;

	principalError.innerHTML="";
	interestError.innerHTML="";
	periodError.innerHTML="";
	var ok = true;

	if (isNaN(principal) || principal <= 0) {
		principalError.innerHTML="Principal must be greater than 0.";
		ok = false;
	}

	if (isNaN(interest) || interest < 1 || interest > 99) {
		interestError.innerHTML="Invalid Interest. Must be in (0,100).";
		ok = false;
	}

	if (period == null || isNaN(period) || period < 1) {
		periodError.innerHTML="Invalid Period!";
		ok = false;
	} else if (grace != null && grace && period < 7) {
		periodError.innerHTML="Invalid Period. Must be greater than grace period.";
		ok = false;
	}

	return ok;
}
