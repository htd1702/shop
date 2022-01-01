$(document).ready(function(){
	$("#buttonCancle").on("click",function(){
		window.location = moduleURL;
	});
	
	//code for upload image
	$("#fileImage").change(function(){
		fileSize = this.files[0].size;
		
		if (fileSize > MAX_FILE_SIZE){
			this.setCustomValidity("you must choose an image less than "+MAX_FILE_SIZE+" bytes!");
			this.reportValidity();
		} else {
			this.setCustomValidity("");
			showImageThumbnail(this);
		}
	});
});
	
function showImageThumbnail(fileInput){
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload= function(e){
		$("#thumbnail").attr("src",e.target.result);
	};
	reader.readAsDataURL(file);
}
	
function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}

function showErrorModel(message) {
	showModalDialog("Error", message);
}

function showWarningModel(message) {
	showModalDialog("Warning", message);
}
