$(document).ready(function(){
	$("#logoutLink").on("click", function(e){
		e.preventDefault();
		document.logoutForm.submit();
	});
	
	/** 
		customiseDropDownMenu();
	*/
});

/**
	This code for icon on drop down menu forward to user home page
 */
/*
function customiseDropDownMenu(){
	$(".navbar .dropdown").hover(
		function(){
			$(this).find('.dropdown-menu').first().stop(true,true).delay(0).slideDown();
		},
		function(){
			$(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
		}
	);
	
	$(".dropdown > a").click(function(){
		location.href = this.href;
	});
}
*/