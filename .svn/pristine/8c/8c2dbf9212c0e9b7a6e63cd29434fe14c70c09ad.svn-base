var company = new(function() {
	var numVersionParticipation=[];
	var idJe;
	
	function add() {
		var d = {
			name : $("#addCompany1").find("#nomEntreprise").val(),
			street : $("#addCompany2").find("#rueEntreprise").val(),
			number : $("#addCompany2").find("#nrEntreprise").val(),
			box : $("#addCompany2").find("#bteEntreprise").val(),
			postCode : parseInt($("#addCompany3").find("#cpEntreprise").val()),
			locality : $("#addCompany3").find("#communeEntreprise").val(),
		};
		
		if(d['name'] === ''){
			$("#error_addCompany").text("Entrez le nom de l'entreprise.");
			return;
		}
		if(d['street'] === ''){
			$("#error_addCompany").text("Entrez une rue.");
			return;
		}
		if(d['number'] === ''){
			$("#error_addCompany").text("Entrez un numéro.");
			return;
		}
		/*if(d['box'] === ''){
			$("#error_addCompany").text("Entrer une boite.");
			return;
		}*/
		if(isNaN(d['postCode'])){
			$("#error_addCompany").text("Entrez un code postal.");
			return;
		}
		if(d['locality'] === ''){
			$("#error_addCompany").text("Entrez une commune.");
			return;
		}
		
		var success = function(response) {
			request.successDefault(response);
			viderForm();
			updateData();			
		}
		
		request.post('addCompany',d,success,request.errorDefault);
	}
	
	function viderForm(){
		//2 form, raison pour laquelle on utilise pas de reset() exceptionnelment
		$("#addCompany1").find("#nomEntreprise").val('');
		$("#addCompany2").find("#rueEntreprise").val('');
		$("#addCompany2").find("#nrEntreprise").val('');
		$("#addCompany2").find("#bteEntreprise").val('');
		$("#addCompany3").find("#cpEntreprise").val('');
		$("#addCompany3").find("#communeEntreprise").val('');
		$("#error_addCompany").text("");
	}
	function setNumVersion(array,id){
		numVersionParticipation= array;
		console.log(numVersionParticipation);
		idJe = id;
		console.log(idJe);
	}
	
	function changerEtatParticipation(){
		var id = this.id;
		console.log("id : "+id);
		console.log("id je : "+idJe);
		console.log("version : "+numVersionParticipation[id]);
		var etat = $(this).parent().prev().children().val();
		console.log($(this).parent().prev().children().val());
		switch(etat){
		case '1' : etat = 'payee';
			break;
		case '2' : etat = 'facturee';
			break;
		case '3' : etat = 'confirmee';
			break;
		case '4' : etat = 'invitee';
		};
		console.log(etat);
		d={
				idCompany : id,
				idJe : idJe,
				version : numVersionParticipation[id],
				state : etat 
		}
		var success = function(response){
			toastr.success(response['message']);
			numVersionParticipation[id] +=1;
			updateData();
			
		}
		
		request.post('changeState', d, success, request.errorDefault);
			
	}
	
	function annulerParticipation(){
		var id = this.id;
		d={
				idCompany : id,
				idJe : idJe,
				version : numVersionParticipation[id]

		}
		
		var success = function(response){
			toastr.success(response['message']);
			updateData();
		}
		
		request.post('cancelParticipation', d, success, request.errorDefault);
		numVersionParticipation[id] +=1;
	}
	
	return{
		add:add,
		viderForm:viderForm,
		setNumVersion:setNumVersion,
		changerEtatParticipation:changerEtatParticipation,
		annulerParticipation:annulerParticipation
	}
});

