function validate() {
	var principal = document.getElementById("principal").value;
	var principalError = document.getElementById("principalError");
	var interestError = document.getElementById("interestError");
	var ok = true;

	principalError.innerHTML = "";
	interestError.innerHTML = "";

	if (isNaN(principal) || principal <= 0) {
		principalError.innerHTML = "Principal must be greater than 0.";
		ok = false;
	}

	if (document.getElementById("interest") != null) {
		var interest = document.getElementById("interest").value;
		var period = document.getElementById("period").value;
		var periodError = document.getElementById("periodError");
		var grace = document.getElementById("grace").checked;
		periodError.innerHTML = "";

		if (isNaN(interest) || interest < 1 || interest > 99) {
			interestError.innerHTML = "Invalid Interest. Must be in (0,100).";
			ok = false;
		}

		if (period == null || isNaN(period) || period < 1) {
			periodError.innerHTML = "Invalid Period!";
			ok = false;
		} else if (grace && period < 7) {
			periodError.innerHTML = "Invalid Period. Must be greater than grace period.";
			ok = false;
		}
	} else {
		var interest1 = document.getElementById("interest1").checked;
		var interest2 = document.getElementById("interest2").checked;
		var interest3 = document.getElementById("interest3").checked;
		if (!interest1 && !interest2 && !interest3) {
			interestError.innerHTML="Select an option";
			ok = false;
		}
	}

	return ok;
}

function doSimpleAjax(address){
	if (!validate()) {
		return;
	}
	var request = new XMLHttpRequest();
	var data = "principal=" + document.getElementById("principal").value
		+ "&interest=" + document.getElementById("interest").value
		+ "&period=" + document.getElementById("period").value
		+ "&grace=" + (document.getElementById("grace").checked ? "on" : "off")
		+ "&ajax=true";

	request.open("POST", address, true);
	request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	request.setRequestHeader("Accpet","application/json");
	request.onreadystatechange = function() { handler(request); };
	request.send(data);
}

function handler(request){
	if ((request.readyState == 4) && (request.status == 200)){
		try {
			var response = JSON.parse(request.responseText);

			/* Set HTML from Response */
			document.getElementById("ajaxTarget").innerHTML =
				"Grace Period Interest: $" + parseFloat(response.graceInterest).toFixed(2)
				+ "<br>Monthly payment: $" + parseFloat(response.payment).toFixed(2);

			/* Set Ajax Response row visible */
			document.getElementById("ajaxResponse").style.display='table-row';
		} catch (error) {
			document.write(request.responseText);
		}
	}
}
