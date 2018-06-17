var csv = new(function() {
	function companiesToContact(){
		var success = function(response){
			downloadCSV(response,'CompaniesToContact');
		}
		request.post('genCSV_all',undefined,success,request.errorDefault);
	}
	
	function newCompaniesToContact(){
		var success = function(response){
			downloadCSV(response,'NewCompaniesToContact');
		}
		request.post('genCSV_new',undefined,success,request.errorDefault);
	}
	
	return {
		companiesToContact:companiesToContact,
		newCompaniesToContact:newCompaniesToContact
	}
});