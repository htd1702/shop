$(document).ready(function(){
	$("a[name='linkRemoveDetail']").each(function(index){
		$(this).click(function(){
			removeDetailSectionByIndex(index);
		});
	});
	
});

function addNextDetailSection(){
	allDivDetails=$("[id^='divDetail']");
	divDetailCount= allDivDetails.length;
	
	htmlDetailSection=`
		<div class="form-inline" id ="divDetails${divDetailCount}">
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255" />
			<label class="m-3">Detail Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255" />
		</div>
	`;
	
	$("#divProductDetails").append(htmlDetailSection);
	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");
	htmlLinkRemove = `
		<a class ="btn fas fa-times-circle fa-2x icon-dark" 
		href="javascript:removeDetailSecsionById('${previousDivDetailID}')"
		title="Remove this detail"></a>
	`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
	
}

function removeDetailSecsionById(id){
	$("#"+id).remove();
}

function removeDetailSectionByIndex(index){
	$("#divDetail" + index).remove();	
}