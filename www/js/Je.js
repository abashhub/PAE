var je = new(function() {
	
	function supprimerChamp(id){
		$("#createJE"+id).html("");
	}
	
	function fillTables(array,array2){
		//ajouter lignes tableaux
		var htmlNew="";
		var htmlPartJe="";
		if(array2){
			array2.forEach(function(company) {
				var row=
				"<tr class=\"odd gradeX\"id=\"createJE"+company.companyId+"\">"+
					"<td class=\"creation_je_companies\" style=\"display:none;\">"+company.companyId+"</td>"+
					"<td>"+company.name+"</td>"+
					"<td><button onclick=\"je.supprimerChamp(createJE"+company.companyId+")\"><span class=\"input-group-addon\"><span class=\"fa fa-times\"/></span></button></td>"+
				"</tr>";
				htmlPartJe+=row;
			    
			});
		}
		if(array){
			var index =0;
			array.forEach(function(company) {
				index=index+1;
					var row=
					"<tr class=\"odd gradeX\" id=\"createJE"+company.companyId+"\">"+
						"<td class=\"creation_je_companies\"  style=\"display:none;\">"+company.companyId+"</td>"+
						"<td>"+company.name+"</td>"+
						"<td><button onclick=\"je.supprimerChamp(createJE"+company.companyId+")\"><span class=\"input-group-addon\"><span class=\"fa fa-times\"/></span></button></td>"+
					"</tr>";
					htmlNew+=row;
				    
				});
		}
		$("#tableau_new_Entreprise").html(htmlNew);
		$("#entreprisesPartJETbody").html(htmlPartJe);
		
		
	}
	
	function supprimerChamp(id){
		$(id).remove();
	}
	
	function completerSelectAnnee(){
		var date = new Date();
		var year = date.getFullYear();
		var year1;
		var year2;
		var year3;
		var year4;
		var month = date.getMonth();
		if(month<6){
			year1=year-1;
			year2=year;
			year3 = year+1;
			year4 = year3 +1;
		}else{
			year1=year;
			year2=year+1;
			year3=year+2;
			year4 = year3 +1;
		}

		var anneeAcademiqueCourante = year1+"-"+year2;
		var anneeAcademiqueSuiante = year2+"-"+year3;
		var anneeAcadmeiqueDerniere = year3+"-"+year4;

		$('#anneeAcademique1').html(anneeAcademiqueCourante);
		$('#anneeAcademique2').html(anneeAcademiqueSuiante);
		$('#anneeAcademique3').html(anneeAcadmeiqueDerniere);
	};
	
	function creer(){
		 var date = $("#txtJE").val();
		var anneAcademique = $('#select_annee_Academique').val();
		var d = {
				date : $("#txtJE").val(),
				anneeAcademique : $('#select_annee_Academique').val(),
		};
		if(date != undefined && anneAcademique != ""){
			var success = function(response) {
				toastr.success("Réussite");
				je.fillTables(response['newEntreprise'],response['entreprisesInvitee']);
				htmlHandler.showJECompanies();
				//viderForm();
				// L'objet JE est stocké dans response['JE']				
			}
			var error = function(jqXHR, serverStatus, errorThrown) {
				console.log(jqXHR);
				toastr.error(JSON.parse(jqXHR['responseText'])['message']);
				if(jqXHR.status === 401){
					redirectToLogin();
				} else {
					htmlHandler.creationJEPlus();
				}
			}
			request.post('fillTable',d,success,error);
		}else{
			toastr.error("Date ou année académique vide");
		}
	}
	
	
	function sauver(){
		var entreprise_a_ajouter=[];
		$(".creation_je_companies").each(function(index) {
			entreprise_a_ajouter.push($(this).html());
		});
		
		var d = {
				date : $("#txtJE").val(),
				anneeAcademique : $('#select_annee_Academique').val(),
				liste_id : entreprise_a_ajouter
		};
		//rajouter les entreprises des tableaux
		var success = function(response) {
			toastr.success("JE ajoutée");
			//viderForm();
			// L'objet JE est stocké dans response['JE']
			
			$("#companiesToContact").trigger("click"); // generate csv after success
			updateData();
			htmlHandler.showDashboard();
		}
		var error = function(jqXHR, serverStatus, errorThrown) {
			console.log(jqXHR);
			toastr.error(JSON.parse(jqXHR['responseText'])['message']);
			if(jqXHR.status === 401){
				redirectToLogin();
			}
		}
		request.post('createJE',d,success,error);
		
		
	}
	
	
	
	function showCompanies(){
		var date = $('#txtJE').val();
		var anneAcademique = $('#select_annee_Academique').val();
		if(date != '' && anneAcademique != ''){
			htmlHandler.showJECompanies();
		}else{
			//msg erreur remplir date ?
		}
		
	}
	
	function cloturer(){
		var success = function(response){
			toastr.success(response['message']);
			updateData();
		}
		request.post('cloture',undefined,success,request.errorDefault);
	}
	
	function cancel(){
		htmlHandler.checkIfCreateAJECanBeAllowed();
	}
	
	
	
	
	return{
        supprimerChamp:supprimerChamp,
        fillTables:fillTables,
        completerSelectAnnee:completerSelectAnnee,
        creer:creer,
        sauver:sauver,
        showCompanies:showCompanies,
        cloturer:cloturer,
        supprimerChamp:supprimerChamp,
        cancel:cancel
    }
});












