var search = new(function(){
	
	function company(){
		d=formToDict($("#recherche_entreprise_form"));
		var success = function(response) {
			$("#search_title").html("Entreprises correspondants à la recherche");
			$("#search_complement").html("");
			var html="<thead><th>Nom</th><th>Adresse</th><th>Createur</th></thead><tbody>";
			response.companies.forEach(function(company){
				
				html+="<tr class=\"dashboardTd\" id=\""+ company.companyId+"\"><td >"+company.name+"</td>" +
						"<td>"+company.adresseFacturation.number+" "+company.adresseFacturation.street+" "+company.adresseFacturation.postCode+
						" "+company.adresseFacturation.locality+"</td>"+ 
						"<td>"+company.creater.firstName+" "+company.creater.lastName+"</td></tr>"
			});
			html+="</tbody>";
			$("#search_table_result").html(html);
			
		}
		request.post('searchCompanies', d, success,request.errorDefault);
		document.getElementById('search_table_result').scrollIntoView();
	}
	
	function je(){
		var d={'year':$(search_select_annee).val()}
		var success = function(response) {
			console.log(response);
			var j=response.je;
			$("#search_title").html("Journée d'entreprise correspondant à la recherche");
			$("#search_complement").html(
					"Année académique:"+j.anneeAcademique
					+"<br/>Date de lancement des invitations: "+response.dateInvitation
					+"<br/>Date de la journée: "+response.dateJE);
			var html="<thead><th>Nom</th><th>Participation</th><th>Nombre de participations</th><th>Createur</th></thead><tbody>";
			response.companies.forEach(function(company){
				var participation="";
				if(company.participations[0].cancelled){
					participation+="Annulé - ";
				}
				participation+=company.participations[0].state;
				
				html+="<tr class=\"dashboardTd\" id=\""+ company.companyId+"\"><td>"+company.name+"</td>" +
						"<td>"+participation+"</td>"+
						"<td>"+company.participations.length+"</td>" +
						"<td>"+company.creater.firstName+" "+company.creater.lastName+"</td></tr>"
			});
			html+="</tbody>";
			$("#search_table_result").html(html);
			
		}
		request.post('getJE', d, success,request.errorDefaultWithoutToast);
		document.getElementById('search_table_result').scrollIntoView();
	}
	
	function contact(){
		d=formToDict($("#recherche_contact_form"));
		var success = function(response) {
			$("#search_title").html("Contacts correspondants à la recherche");
			$("#search_complement").html("");
			var html="<thead><th>Nom</th><th>Prenom</th><th>Entreprise</th><th>Téléphone</th><th>Email</th></thead><tbody>";
			response.contacts.forEach(function(contact){
				html+="<tr><td class=\"dashboardTd\" id=\""+contact.company.companyId+"\">"+contact.lastName+"</td>" +
						"<td>"+contact.firstName+"</td>"+
						"<td>"+contact.company.name+"</td>"+
						"<td>"+contact.telephone+"</td>"+
						"<td>"+contact.email+"</td>"+
						"</tr>"
			});
			html+="</tbody>";
			$("#search_table_result").html(html);
			
		}
		request.post('searchContacts', d, success,request.errorDefaultWithoutToast);
		document.getElementById('search_table_result').scrollIntoView();
	}
	
	function load(){
		var success = function(response) {
			var options="";
			response.academicYears.forEach(function(academicYear){
				options+="<option>"+academicYear+"</option>";
			});
			$('#search_select_annee').html(options);
			$("#name_tags").autocomplete({
			     source: response.companiesNames
			 });
			$("#streets_tags").autocomplete({
			     source: response.companiesStreets
			 });
			$("#locality_tags").autocomplete({
			     source: response.companiesPostalCode
			 });
			$("#locality_name_tags").autocomplete({
			     source: response.companiesMunicipalities
			 });
			$("#contact_lastname_tag").autocomplete({
			     source: response.lastNames
			 });
			$("#contact_firstname_tag").autocomplete({
			     source: response.firstNames
			 });
			htmlHandler.showSearch();
		}
		request.post('fillSearch', undefined, success,request.errorDefaultWithoutToast);

		
	}
	
	return{
		company:company,
		je:je,
		contact:contact,
		load:load
	}
});


