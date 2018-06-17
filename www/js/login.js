var connection = new(function() {
	
	function login() {
		var d = formToDict($("#connexion"));
		
		if(d['login'] === ''){
			$("#error_connexion").text("Entrez votre nom d'utilisateur");
			return;
		}
		if(d['password'] === ''){
			$("#error_connexion").text("Entrez votre mot de passe");
			return;
		}
		
		var success =  function(response) {
			toastr.success("Bienvenue, " + response['user']['login']);
			console.log(response.user);
			if(!response.user.manager){
				$('#newCompaniesToContact').attr('style',"display:none");
				$('#companiesToContact').attr('style',"display:none");
				informationContact.setManager(false);
			}else{
				$('#newCompaniesToContact').attr('style',"display:block");
				$('#companiesToContact').attr('style',"display:block");
				informationContact.setManager(true);
			}
			updateData(); 
			htmlHandler.showDashboard();
		}
		
		request.post('login',d,success,request.errorDefault);
		
		document.connection_form.reset();		// reset form
	}
	
	
	function logout() {
		request.post('logout', undefined, request.successDefault,request.errorDefault);
	}
	
	
	
	
	function inscription(){
        var d = formToDict($("#registrationForm"));

        if(d['lastName'] === ''){
            $("#msg_registration").text("Entrez votre nom");
            return;
        }
        if(d['firstName'] === ''){
            $("#msg_registration").text("Entrez votre prénom");
            return;
        }
        if(d['email'] === ''){
            $("#msg_registration").text("Entrez votre email");
            return;
        }
        if(d['login'] === ''){
            $("#msg_registration").text("Entrez votre pseudo");
            return;
        }
        if(d['password'] === ''){
            $("#msg_registration").text("Entrez votre mot de passe");
            return;
        }
        if(d['password'] !== d['Cpassword']){
            $("#msg_registration").text("Le mot de passe de confirmation ne correspond pas");
            return;
        }
         
        var success = function(response) {
            // user is auto logged in
            toastr.success("Bienvenue, " + response['user']['login']);
            updateData(); // update data
            document.registration_form.reset(); // reset form
            $("#msg_registration").text("");
            htmlHandler.showDashboard();
            $('#newCompaniesToContact').attr('style',"display:none");
			$('#companiesToContact').attr('style',"display:none");
			informationContact.setManager(false);
        };
        
        request.post('register',d,success,request.errorDefault);
    }
	
	return{
		login:login,
		logout:logout,
		redirectToLogin:htmlHandler.showLogin,
		inscription:inscription
	}
});


