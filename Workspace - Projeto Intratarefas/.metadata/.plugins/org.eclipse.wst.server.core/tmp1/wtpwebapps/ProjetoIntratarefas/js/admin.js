INTRATAREFAS.funcionario = new Object();



$(document).ready(function(){
	
	INTRATAREFAS.funcionario.cadastrar = function(){
		
		
		
		var funcionario = new Object();
		funcionario.user = document.frmAddFuncionario.txtuser.value;
		funcionario.nome = document.frmAddFuncionario.txtnome.value;		
		funcionario.cpf =  document.frmAddFuncionario.cpf.value;
		funcionario.email =document.frmAddFuncionario.email.value;
		funcionario.funcao =document.frmAddFuncionario.funcao.value;
		funcionario.password =document.frmAddFuncionario.password.value;
		
		var cpf = document.frmAddFuncionario.cpf.value;// receber o valor do campo txtnome em uma variável
		var expRegCpf = new RegExp("^[0-9]{11}$");//Criado um objeto RegExp e atribuido a variável expRegNome

		if(!expRegCpf.test(cpf)){
		alert("Preencha o campo do CPF corretamente. ");
			document.frmAddFuncionario.cpf.focus();/*motivo de após o "ok" ir direto ao input do Nome, devido ao focus*/
			return false;/*impede o envio do formulário e de seguir a leitura dos códigos*/
		}
		
		var email = document.frmAddFuncionario.email.value;
		var expRegEmail = new RegExp("^[A-zÀ-ü]{1,}[@]{1}[A-z]{2,}([.]{1}[A-z]{1,})+$");
		if(!expRegEmail.test(email)){
			alert("Favor preencha o campo E-mail corretamente. ");
				document.frmAddFuncionario.email.focus();
				return false;
			}
		
		if((funcionario.user=="")||(funcionario.nome=="")||(funcionario.password=="")
				||(funcionario.email=="")||(funcionario.cpf=="")
					||(funcionario.funcao=="")){
			
			
			
			INTRATAREFAS.exibirAviso("Preencha todos os campos corretamente!");
		
			
		}else{
		
			$.ajax({
				type: "POST", 
				url: INTRATAREFAS.PATH + "funcionario/inserir",
				data:JSON.stringify(funcionario), 
				success: function (msg) {
			INTRATAREFAS.exibirAviso(msg);
			$("#addFuncionario").trigger("reset"); 
			INTRATAREFAS.funcionario.buscarPorNome();
				},
				error: function(info) {
					
					INTRATAREFAS.exibirAviso("Erro ao cadastrar um novo funcionário: "+ info.status + " - " + info.responseText);		
			}	
			});	
			}	
			}
	//Transforma os dados dos produtos recebidos do servidor em uma tabela HTML
	INTRATAREFAS.funcionario.exibir = function(listaDeFuncionarios) {
		
		var tabela = "<table>" +
				"<tr>" +
				"<th>Usuário</th>" +
				"<th>Nome</th>" +
				"<th>E-mail</th>" +
				"<th>CPF</th>" +
				"<th>Função</th>" +
				"<th class='acoes'>Ações</th>" +
				"</tr>";
		
		if (listaDeFuncionarios != undefined && listaDeFuncionarios.length > 0){
			
			for (var i=0; i<listaDeFuncionarios.length; i++){
				tabela += "<tr>" +
				"<td>"+listaDeFuncionarios[i].user+"</td>" +
				"<td>"+listaDeFuncionarios[i].nome+"</td>" +
				"<td>"+listaDeFuncionarios[i].cpf+"</td>" +
				"<td>"+listaDeFuncionarios[i].email+"</td>" +
				"<td>"+listaDeFuncionarios[i].funcao+"</td>" +
				"<td>" +
				"<a  onclick=\"INTRATAREFAS.funcionario.exibirEdicao('"+listaDeFuncionarios[i].idfuncionario+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon' ></a> " +
				"<a onclick=\"INTRATAREFAS.funcionario.confirmaExclusao('"+listaDeFuncionarios[i].idfuncionario+"')\"><img src='../../img/delete.png' alt='Excluir Registro' class='acoesIcon'class='acoesIcon' ></a> " +
				"</td>" +
				"</tr>"

			}
			
			
			
			
		} else if (listaDeFuncionarios == ""){
			tabela += "<tr><td colspan='6'>Nenhum funcionário encontrado</td></tr>";
		}
		tabela += "</table>";
		
		return tabela;
		
	};
	
	//Função que busca os dados no banco para exibir, ou o que foi especificado na busca
	INTRATAREFAS.funcionario.buscarPorNome = function(){
		
		var valorBusca = $("#campoBuscaFuncionario").val();//valor digitado no filtro
		
		$.ajax({
			type: "GET",//usado o GET por ser uma busca de informações
			url: INTRATAREFAS.PATH + "funcionario/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){
				
				dados = JSON.parse(dados);
				$("#listaFuncionarios").html(INTRATAREFAS.funcionario.exibir(dados));
				
				
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao consultar os funcionario: "+ info.status + " - " + info.responseText);
			}
		});
		
	};
	
	//Atualiza a tabela
	INTRATAREFAS.funcionario.buscarPorNome();
	
	INTRATAREFAS.funcionario.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: INTRATAREFAS.PATH + "funcionario/excluir/"+id,
			success: function(msg){
				//Verificação pode ficar aqui
				INTRATAREFAS.exibirAviso(msg);
				INTRATAREFAS.funcionario.buscarPorNome();
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao excluir funcionário: "+ info.status + " - " + info.responseText);
			}
		});
	};
	
	INTRATAREFAS.funcionario.confirmaExclusao = function(id){
		var modalExcluiFuncionario = {
				title: "Excluir Funcionários",
				height: 250,
				width: 550,
				modal: true,
				buttons:{
					"Excluir": function(){
						INTRATAREFAS.funcionario.excluir(id)
						$(this).dialog("close");
					},	
				"Cancelar": function(){
					INTRATAREFAS.funcionario.buscarPorNome();
					$(this).dialog("close");
				
			}
				},
			close: function(){
				//caso o usuário simplismente feche a caixa de edição
				//não deve acontecer nada
			}
		};

	$("#modalExcluiFuncionario").dialog(modalExcluiFuncionario);
	};
	
	//Exibe a modal de Edição
	INTRATAREFAS.funcionario.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: INTRATAREFAS.PATH + "funcionario/buscarPorId",
			data: "id="+id,
			success: function(funcionario){
				
			document.frmEditaFuncionario.idFuncionario.value = funcionario.idfuncionario;
			document.frmEditaFuncionario.txtnome.value = funcionario.nome;
			document.frmEditaFuncionario.cpf.value = funcionario.cpf;
			document.frmEditaFuncionario.email.value = funcionario.email;
			document.frmEditaFuncionario.password.value = funcionario.password;
		
			var selFuncionario = document.getElementById('selFuncaoEdicao');

			for(var i=0; i < selFuncionario.length; i++){
				
				if (selFuncionario.options[i].value == funcionario.funcao){

					selFuncionario.options[i].setAttribute("selected", "selected");
				}else{

					selFuncionario.options[i].removeAttribute("selected");
				}
			}
			
				
				var modalEditaFuncionario = {
						title: "Editar Informações de Funcionários",
						height: 400,
						width: 550,
						modal: true,
						buttons:{
							"Salvar": function(){
								INTRATAREFAS.funcionario.editar();
								
							},
							"Cancelar": function(){
								INTRATAREFAS.funcionario.buscarPorNome();

								$(this).dialog("close");
							}
						},
						close: function(){
							//caso o usuário simplismente feche a caixa de edição
							//não deve acontecer nada
						}
				};
				
				$("#modalEditaFuncionario").dialog(modalEditaFuncionario);
		
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao buscar funcionários para edição: "+ info.status + " - " + info.responseText);
			}
	});
	};
	
	//Edita as informações do funcionário
	INTRATAREFAS.funcionario.editar = function(){
		
		var funcionario = new Object();
		
		
		
		funcionario.idfuncionario = document.frmEditaFuncionario.idFuncionario.value;
		funcionario.nome = document.frmEditaFuncionario.txtnome.value;
		funcionario.cpf = document.frmEditaFuncionario.cpf.value;
		funcionario.email = document.frmEditaFuncionario.email.value;
		funcionario.password = document.frmEditaFuncionario.password.value;
		funcionario.funcao = document.frmEditaFuncionario.funcao.value;

		
		var cpf = document.frmEditaFuncionario.cpf.value;// receber o valor do campo txtnome em uma variável
		var expRegCpf = new RegExp("^[0-9]{11}$");//Criado um objeto RegExp e atribuido a variável expRegNome

		if(!expRegCpf.test(cpf)){
		alert("Preencha o campo do CPF corretamente. ");
			document.frmEditaFuncionario.cpf.focus();/*motivo de após o "ok" ir direto ao input do Nome, devido ao focus*/
			return false;/*impede o envio do formulário e de seguir a leitura dos códigos*/
		}
		
		var email = document.frmEditaFuncionario.email.value;
		var expRegEmail = new RegExp("^[A-zÀ-ü]{1,}[@]{1}[A-z]{2,}([.]{1}[A-z]{1,})+$");
		if(!expRegEmail.test(email)){
			alert("Favor preencha o campo E-mail corretamente. ");
				document.frmEditaFuncionario.email.focus();
				return false;
			}
		
		if((funcionario.user=="")||(funcionario.nome=="")||(funcionario.password=="")
				||(funcionario.email=="")||(funcionario.cpf=="")
					||(funcionario.funcao=="")){
			
			
			
			INTRATAREFAS.exibirAviso("Preencha todos os campos corretamente!");
		
			
		}else{
			
		
		$.ajax({
			type: "PUT",
			url: INTRATAREFAS.PATH + "funcionario/alterar", 
			data: JSON.stringify(funcionario),
			success: function(msg){
				INTRATAREFAS.exibirAviso(msg);
				INTRATAREFAS.funcionario.buscarPorNome();
				$("#modalEditaFuncionario").dialog("close");
				//mensagem de aviso do sucesso atraves da modal
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao editar o funcionário: "+ info.status + " - "+ info.responseText);
				
			}	
			});
		}
	};
	
	//tabela de exibição dos atendimentos
	INTRATAREFAS.funcionario.exibirAtendimentos = function(listaAtendimentosGerais) {
		
		var tabela = "<table>" +
				"<tr>" +
				"<th>Atendimento</th>" +
				"<th>Cliente</th>" +
				"<th>Atendente</th>" +
				"<th>Equipamento</th>" +
				"<th>Defeito</th>" +
				"<th>Status</th>" +
				"</tr>";
		
		if (listaAtendimentosGerais != undefined && listaAtendimentosGerais.length > 0){
			
			for (var i=0; i<listaAtendimentosGerais.length; i++){
		
				tabela += "<tr>" +
				"<td>"+listaAtendimentosGerais[i].atendimento+"</td>" +
				"<td>"+listaAtendimentosGerais[i].nome+"</td>" +
				"<td>"+listaAtendimentosGerais[i].funcionario+"</td>" +
				"<td>"+listaAtendimentosGerais[i].equipamento+"</td>" +
				"<td>"+listaAtendimentosGerais[i].defeito+"</td>" +
				"<td>"+listaAtendimentosGerais[i].status+"</td>" +
				"</tr>"
				
				}
			
			}else if (listaAtendimentosGerais == ""){
			tabela += "<tr><td colspan='6'>Nenhum atendimento encontrado</td></tr>";
		}
		tabela += "</table>";
		
		return tabela;
		
	};
	

	
	//Gera o pdf do relatório
	INTRATAREFAS.funcionario.geraPDF= function(){
        var atendimentos = document.getElementById("listaAtendimentosGerais").innerHTML;
        var style = "<style>";
        style = style + "table {width: 100%;font: 20px Calibri;}";
        style = style + "table, th, td {border: solid 1px #DDD; border-collapse: collapse;";
        style = style + "padding: 2px 3px;text-align: center;}";
        style = style + "</style>";
        // CRIA UM OBJETO WINDOW
        var win = window.open('', '', 'height=700,width=700');//Cria uma janela
        win.document.write('<html><head>');
        win.document.write('<title>Atendimentos</title>');   // <title> CABEÇALHO DO PDF.
        win.document.write(style);                                     
        win.document.write('</head>');
        win.document.write('<body>');
        win.document.write(atendimentos);                          // O CONTEUDO DA TABELA DENTRO DA TAG BODY
        win.document.write('</body></html>');
        win.document.close(); 	                                         
        win.print();  	                                                             
    }
});//end_docsready


