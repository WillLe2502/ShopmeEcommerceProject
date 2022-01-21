var buttonLoadForStates;
var dropDownCountryForStates;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function(){
	buttonLoadForStates = $("#buttonLoadCountriesForStates");
	dropDownCountryForStates = $("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState = $("#buttonAddState");
	buttonUpdateState = $("#buttonUpdateState");
	buttonDeleteState = $("#buttonDeleteState");
	labelStateName = $("#labelStateName");
	fieldStateName = $("#fieldStateName");
	
	buttonLoadForStates.click(function() {
		loadCountriesForState();
	});
	
	dropDownCountryForStates.on("change", function() {
		loadStatesForSelectedCountry();
	});
	
	dropDownStates.on("change", function() {
		changeFormStateToSelectedState();
	});
	
	buttonAddState.click(function() {
		if (buttonAddState.val() == "Add") {
			addState();
		} else {
			changeFormStateToNew();
		}
	});
	
	buttonUpdateState.click(function() {
		updateState();
	});
	
	buttonDeleteState.click(function() {
		deleteState();
	});
})

function deleteState() {
	stateId = dropDownStates.val();

	url = contextPath + "states/delete/" + stateId;
	
	$.ajax({
		type: 'DELETE',
		url: url,
		beforeSend: function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function() {
		$("#dropDownStates option[value='" + stateId + "']").remove();
		changeFormStateToNew();
		showToastMessage("The state has been deleted");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server");
	});		
}

function updateState() {
	if (!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateId = dropDownStates.val();
	stateName = fieldStateName.val();

	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();

	jsonData = {id: stateId, name: stateName, country: {id: countryId, name: countryName}};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		$("#dropDownStates option:selected").text(stateName);
		showToastMessage("The state has been updated");
		changeFormStateToNew();
	}).fail(function() {
		showToastMessage("Error: Could not connect to server");
	});	
}

function addState() {
	if (!validateFormState()) return;
	
	url = contextPath + "states/save";
	stateName = fieldStateName.val();

	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	countryName = selectedCountry.text();

	jsonData = {name: stateName, country: {id: countryId, name: countryName}};

	$.ajax({
		type: 'POST',
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId) {
		selectNewlyAddedState(stateId, stateName);
		showToastMessage("The new state has been added");
	}).fail(function() {
		showToastMessage("Error: Could not connect to server");
	});

}

function selectNewlyAddedState(stateId, stateName) {
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);

	$("#dropDownStates option[value='" + stateId + "']").prop("selected", true);

	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState(){
	buttonAddState.prop("value", "New");
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);

	labelStateName.text("Selected State/Province:");

	selectedStateName = $("#dropDownStates option:selected").text();
	fieldStateName.val(selectedStateName);
}

function changeFormStateToNew() {
	buttonAddState.val("Add");
	labelStateName.text("State/Province Name:");

	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);

	fieldStateName.val("").focus();	
}

function loadStatesForSelectedCountry(){
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val();
	url = contextPath + "states/list_by_country/" + countryId;
	
	$.get(url, function(responseJSON) {
		dropDownStates.empty();

		$.each(responseJSON, function(index, state) {
			$("<option>").val(state.id).text(state.name).appendTo(dropDownStates);
		});

	}).done(function() {
		changeFormStateToNew();
		showToastMessage("All states have been loaded for " + selectedCountry.text());
	}).fail(function() {
		showToastMessage("Error: Could not connect to server");
	});	
}

function loadCountriesForState(){
	url = contextPath + "countries/list"
	$.get(url, function(responseJSON){
		dropDownCountryForStates.empty();
		
		$.each(responseJSON, function(index, country){
			$("<option>").val(country.id).text(country.name).appendTo(dropDownCountryForStates);
		});
	}).done(function(){
		buttonLoadForStates.val("Refresh Country List");
		showToastMessage("All Countries have been loaded");
	}).fail(function(){
		showToastMessage("Error: Could not connect to server");
	});
}

function validateFormState() {
	formState = document.getElementById("formState");
	if (!formState.checkValidity()) {
		formState.reportValidity();
		return false;
	}	

	return true;
}
