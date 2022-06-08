function validaLogin(){

	var user = document.frmlogin.txtuser.value;// receber o valor do campo em uma variável
	//var expRegUser = new RegExp("^[A-zÀ-ü]{3,}+$");//Criado um objeto RegExp e atribuido a variável 
	
if(document.frmlogin.txtuser.value==""){
	alert("Preencha o campo corretamente com o seu usuário. ");
		document.frmlogin.txtuser.focus();/*motivo de após o "ok" ir direto ao input do Nome, devido ao focus*/
		return false;/*impede o envio do formulário e de seguir a leitura dos códigos*/
	}
	
	
if(document.frmlogin.txtpassword.value==""){
	alert("Por favor insira a sua senha de acesso. ");
		document.frmlogin.txtpassword.focus();
		
		return false;
	}	
	return true;
}

INTRATAREFAS = new Object();



INTRATAREFAS.carregaPagina = function(pagename){ 
	
	
	
	
	if ($(".ui-dialog"))
		$(".ui-dialog").remove();
	
	
	$("section").empty();
	$("section").load(pagename, function(response, status, info)/*Não precisa do nome, pois o arquivo é o index*/
	{
		if(status == "error") {
			var msg = "Houve um erro ao encontrar a página: "+ info.status + " - " + info.statusText;
			$("section").html(msg);
			
		}
	});	
}

$(document).ready(function(){
	
	INTRATAREFAS.PATH = "/ProjetoIntratarefas/rest/";

	$("header").load("/ProjetoIntratarefas/general/cabecalho.html");
	$("footer").load("/ProjetoIntratarefas/general/rodape.html");
	$("#ad").load("/ProjetoIntratarefas/pages/administrador/general/menu.html");
	$("#at").load("/ProjetoIntratarefas/pages/atendente/general/menu.html");
	$("#tec").load("/ProjetoIntratarefas/pages/tecnico/general/menu.html");
	

	
	INTRATAREFAS.exibirAviso = function(aviso){
		var modal = {
				title: "Mensagem",
				height: 250,
				width: 400,
				modal: true,
				buttons: {
					"OK": function(){
						$(this).dialog("close");
					}
				}
		};
		$("#modalAviso").html(aviso);
		$("#modalAviso").dialog(modal);
	}
	
		INTRATAREFAS.formatarDinheiro = function(valor){
		return valor.toFixed(2).replace('.', ',').replace(/(\d)(?=(\d{3})+\,)/g, "$1.");
	}
	
	
});
