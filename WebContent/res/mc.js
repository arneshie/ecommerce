function report(address){
		
	var request = new XMLHttpRequest();
	var requestData = '';
	
	// building the request string  to parse like in lab 5
	requestData += "surname=" + document.getElementById("cat").value + "&";
	requestData += "report=true";
	request.open("GET", (address + "?" + requestData), true);
	request.onreadystatechange = function() {
		handler(request);
	};
	console.log(data)
	request.send(null);
}

function handler(request) {
	if ((request.readyState == 4) && (request.status == 200)) {
		var target = document.getElementById("result");
		target.innerHTML = request.responseText;
	}
}