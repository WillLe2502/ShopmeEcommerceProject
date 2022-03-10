$(document).ready(function(){
	$(".linkMinus").on("click", function(evt){
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) - 1;
		
		if(newQuantity > 0){
			quantityInput.val(newQuantity)
		} else {
			showWarningModal ("Minimum quality is 1");
		}
	})
	
	$(".linkPlus").on("click", function(evt){
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) + 1;
		
		if(newQuantity < 6 ){
			quantityInput.val(newQuantity)
		} else {
			showWarningModal ("Maximum quality is 5");
		}
	})
})