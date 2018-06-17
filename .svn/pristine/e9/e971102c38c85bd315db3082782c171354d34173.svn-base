var updateData = function() {
	var numVersionParticipation=[];
	
	var success = function(response){
		// si pas de je dans le system, on redirige vers la création de je
		if(response.message==="Pas de JE dans la base de donnée"){ 
			htmlHandler.showJECreationIfNoJE();
			//on affiche quand même l'username
			var user = "Bienvenue, <strong>" + response.user + "</strong>";	
			$("#display_username").html(user);
			return;
		};
		
		if(response.JE.cloturee){
			htmlHandler.hideCloture();
			
		}else{
			htmlHandler.showCloture();
		}
		var allCompanies = response.companiesInvited.concat(response.companiesNotInvited);
		var user = "Bienvenue, <strong>" + response.user + "</strong>";	
		var count_companies_confirmed =0;
		var contentInvite="";
		response.companiesInvited.forEach(function(company) {
			numVersionParticipation[company.companyId]= company.participations[company.participations.length-1].numVersion;
			var disabled="";
			var options="";
			var buttons="<button class=\"changeState\" id=\""+company.companyId +"\">Sauver</button><button class=\"annulerParticipation\" id=\""+company.companyId +"\">Annuler</button>";
			switch (company.participations[0].state) {
			case 'invitee':
				options += "<option value=\"4\">invitee</option>";
				
			case 'confirmee':
				options += "<option value=\"3\">confirmee</option>";				
		    case 'facturee':
		        options += "<option value=\"2\">facturee</option>";
		        options += "<option value=\"1\">payee</option>";
		        if(company.participations[0].state!=='invitee'){
					count_companies_confirmed++;
				}
		        break;
		        
		    case 'payee':
		        options = "<option value=\"1\">payee</option>";
		        buttons="<button class=\"annulerParticipation\" id=\""+company.companyId +"\">Annuler</button>";  
		        count_companies_confirmed++;
			}
			//cas où l'entreprise est annulée
			if(company.participations[0].cancelled){
				count_companies_confirmed--;
				disabled="disabled";
				buttons="Participation annulée";
				}
			//cas où la je est cloturée
			if(response.JE.cloturee){
				disabled="disabled";
				if(!company.participations[0].cancelled){
					buttons="";
				}
			}
			var row=
			"<tr class=\"odd gradeX\">" +
			"<td class=\"dashboardTd\" id="+"\""+ company.companyId+"\"" + ">"+company.name+"</td>" +
			"<td><select "+disabled+">" +
			options+
			"</select></td>" +
			"<td>"+buttons +"</td>"+
			"</tr>";
			contentInvite+=row;
		});
		if(!response.JE.cloturee){
			var contentNonInvite="";
			response.companiesNotInvited.forEach(function(company) {
				var row="<option value='" + company.companyId + "'>"+company.name+"</option>" ;
				contentNonInvite+=row;
			});
		}
		
		$(entreprises_confirmees).html("Entreprises confirmées: "+count_companies_confirmed);
		$("#display_username").html(user);
		$("#anneeScolaire").text(response.JE.anneeAcademique);
		$("#dateJE").text(response.dateJE);
		$("#en_tete_date_JE").text("Date de la JE : "+response.dateJE);
		$("#en_tete_lancement_invit").text("Lancement des invitations: "+response.dateInvitation);
		$('#entreprisesAInviter').html(contentNonInvite);
		$("#table_entreprises_invitees").html(contentInvite);
		$('#entreprises_invitees').text("Entreprises invitées: "+response.companiesInvited.length);
		$('#contacts_presents').text("Personnes de contact présentes: " + response.contactsParticipating);
		
		
		company.setNumVersion(numVersionParticipation,response.JE.id);
		informationCompany.setIdJe(response.JE.id);
		informationContact.setIdJe(response.JE.id);
	}
	request.post('updateData', undefined, success,request.errorDefault);
};


