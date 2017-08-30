$(function(){
	//get window size 
	var w = window.innerWidth;
	var h = window.innerHeight;
	SDK.applicationId = "6953184431772864170";
	var sdk = new SDKConnection();
	var web = new WebAvatar();

	function playAvatar(message){
		var msgLength=message.length;
		web.connection = sdk; 

		//***** Male avatar 
		//web.avatar = "677531";    //Jake.webm, emotion:like, action:smile
		//web.avatar = "792824";      //Logan no emotion
		//web.avatar = "793589";    //Logan2 no emotion bald
		//web.avatar = "775411";    //Steve no emotion
  
		//***** Female avatar
		//web.avatar = "667648";    //Julie.webm, emotion:love, like,happy,..., action:smile, laugh, .. 
		web.avatar = "12601502";    //Julie.webm, emotion:love, like,happy,..., 
		//web.avatar = "11571268";    //Julie3.webm, emotion:love, like,happy,..., action:smile, laugh, .. 
		//web.avatar = "780622";    //Sandy no emotion
		//web.avatar = "1115544";   //Joanie.webm no emotion
		//web.avatar = "895343";    //Nicolette no emotion

		//***** Smily avatar 
		//web.avatar = "667642";


		//***** Male voice
		//web.voice = "dfki-spike-hsmm";
		//web.voice = "dfki-spike";
		//web.voice = "dfki-obadiah-hsmm";
		//web.voice = "dfki-obadiah";
		
		//***** Female voice
	    web.voice = "dfki-prudence-hsmm";
		//web.voice = "dfki-prudence";
		//web.voice = "dfki-poppy-hsmm";
		//web.voice = "dfki-poppy"; 

		web.width = w*0.45;
		web.height = h*0.95;
		web.createBox();
		web.addMessage(message, "like", "", "");
		web.processMessages(); 	
	} 
	
	var firstTime = "true";
	setInterval(function(){ 
		var req = new XMLHttpRequest();  
		req.open('POST', 'http://localhost:8000/test', false);
		req.send(firstTime);
		firstTime = "false";
		if(req.status == 200)  
			playAvatar(req.responseText);
	}, 100);
		
	
  	

}); 