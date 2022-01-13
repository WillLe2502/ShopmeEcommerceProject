var buttonLoad;
var dropdownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode

$(document).ready(function(){
	buttonLoad = $("#buttonLoadCountries");
	dropdownCountry = $("#dropdownCountries");
	buttonAddCountry = $("#buttonAddCountry");
	buttonUpdateCountry = $("#buttonUpdateCountry");
	buttonDeleteCountry = $("#buttonDeleteCountry");
	labelCountryName = $("#labelCountryName");
	fieldCountryName = $("#fieldCountryName");
	fieldCountryCode = $("#fieldCountryCode");
	
	buttonLoad.click(function() {
		loadCountries();
	});
	
	dropdownCountry.on("change", function(){
		changeFormStateToSelectedCountry();
	});
	
	buttonAddCountry.click(function() {
		if(buttonAddCountry.val() == "Add"){
			addCountry();
		} else {
			ChangeFormStateToNew();
		}
	});
	
	buttonUpdateCountry.click(function(){
		updateCountry();
	})
	
	buttonDeleteCountry.click(function(){
		deleteCountry();
	})
});

function deleteCountry(){
	optionValue = dropdownCountry.val();
	countryId = optionValue.split("-")[0];
	
	url = contextPath + "countries/delete/" + countryId;
	
	$.get(url, function(){
		$("#dropdownCountries option[value='" + optionValue +"']").remove();
		ChangeFormStateToNew();
	}).done(function(){
		showToastMessage("The country has been deleted");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server");
	});
	
}

function updateCountry(){
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	countryId = dropdownCountry.val().split("-")[0];
	
	jsonData = {id: countryId, name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		$("#dropdownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropdownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated");
		
		ChangeFormStateToNew();
	}).fail(function(){
		showToastMessage("Error: Could not connect to server");
	});
}

function addCountry(){
	url = contextPath + "countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	jsonData = {name: countryName, code: countryCode};
	
	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId, countryCode, countryName);
		showToastMessage("The new country has been added");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server");
	});
}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropdownCountry);
	
	$("#dropdownCountries option[value='" + optionValue +"']").prop("selected", true);
	
	fieldCountryName.val("").focus();
	fieldCountryCode.val("");
}

function ChangeFormStateToNew(){
	buttonAddCountry.val("Add");
	labelCountryName.text("Country Name:");
	
	buttonUpdateCountry.prop("disabled",true);
	buttonDeleteCountry.prop("disabled",true);
	
	fieldCountryCode.val("").focus();
	fieldCountryName.val("").focus();
}

function changeFormStateToSelectedCountry(){
	buttonAddCountry.prop("value", "New");
	buttonUpdateCountry.prop("disabled",false);
	buttonDeleteCountry.prop("disabled",false);
	
	labelCountryName.text("Selected Country");
	
	selectedCountryName = $("#dropdownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
	
	countryCode = dropdownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
	
}

function loadCountries(){
	url = contextPath + "countries/list"
	$.get(url, function(responseJSON){
		dropdownCountry.empty();
		$.each(responseJSON, function(index, country){
			optionValue = country.id + "-" + country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountry);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		showToastMessage("All Countries have been loaded");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server");
	});
}

function showToastMessage(message){
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}