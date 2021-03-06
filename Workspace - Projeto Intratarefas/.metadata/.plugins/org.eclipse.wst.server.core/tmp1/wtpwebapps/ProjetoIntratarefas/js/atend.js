INTRATAREFAS.cliente = new Object();



$(document).ready(function(){
	
	
	INTRATAREFAS.cliente.cadastrar = function(){
			
		var cliente = new Object();
		cliente.nome = document.frmAddCliente.txtnome.value;		
		cliente.cpf =  document.frmAddCliente.cpf.value;
		cliente.email =document.frmAddCliente.email.value;
		cliente.telefone =document.frmAddCliente.telefone.value;
		
		var cpf = document.frmAddCliente.cpf.value;// receber o valor do campo txtnome em uma variável
		var expRegCpf = new RegExp("^[0-9]{11}$");//Criado um objeto RegExp e atribuido a variável expRegNome

		if(!expRegCpf.test(cpf)){
		alert("Preencha o campo do CPF corretamente. ");
			document.frmAddCliente.cpf.focus();/*motivo de após o "ok" ir direto ao input do Nome, devido ao focus*/
			return false;/*impede o envio do formulário e de seguir a leitura dos códigos*/
		}
		
		var email = document.frmAddCliente.email.value;
		var expRegEmail = new RegExp("^[A-zÀ-ü]{1,}[@]{1}[A-z]{2,}([.]{1}[A-z]{1,})+$");
		if(!expRegEmail.test(email)){
			alert("Favor preencha o campo E-mail corretamente. ");
				document.frmAddCliente.email.focus();
				return false;
			}
		
		var telefone  =  document.frmAddCliente.telefone.value;
		var expRegFone = new RegExp("^[0-9]{11}$");

		if(!expRegFone.test(telefone)){
			alert("Preencha o campo Telefone corretamente. ");
				document.frmAddCliente.telefone.focus();
				return false;
			}
		
		if((cliente.nome=="")||(cliente.email=="")||(cliente.cpf=="")
					||(cliente.telefone=="")){
			
			
			
			INTRATAREFAS.exibirAviso("Preencha todos os campos corretamente!");
		
			
		}else{
		
			$.ajax({
				type: "POST", 
				url: INTRATAREFAS.PATH + "cliente/inserir",
				data:JSON.stringify(cliente), 
				success: function (msg) {
			INTRATAREFAS.exibirAviso(msg);
			$("#addCliente").trigger("reset"); 
			INTRATAREFAS.cliente.buscarPorNome();
				},
				error: function(info) {
					
					INTRATAREFAS.exibirAviso("Erro ao cadastrar um novo cliente: "+ info.status + " - " + info.responseText);		
			}	
			});	
			}	
			}
			
	//Exibição dos dados do BD

	INTRATAREFAS.cliente.exibir = function(listaDeCliente) {
		
		
		var tabela = "<table>" +
				"<tr>" +
				"<th>Nome</th>" +
				"<th>CPF</th>" +
				"<th>E-mail</th>" +
				"<th>Telefone</th>" +
				"<th class='acoes'>Ações</th>" +
				"</tr>";
		
	if (listaDeCliente != undefined && listaDeCliente.length > 0){
			
			for (var i=0; i<listaDeCliente.length; i++){
				tabela += "<tr>" +
				"<td>"+listaDeCliente[i].nome+"</td>" +
				"<td>"+listaDeCliente[i].cpf+"</td>" +
				"<td>"+listaDeCliente[i].email+"</td>" +
				"<td>"+listaDeCliente[i].telefone+"</td>" +
				"<td>" +
				"<a  onclick=\"INTRATAREFAS.cliente.exibirEdicao('"+listaDeCliente[i].idcliente+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon' ></a> " +
				"<a onclick=\"INTRATAREFAS.cliente.confirmaExclusao('"+listaDeCliente[i].idcliente+"')\"><img src='../../img/delete.png' alt='Excluir Registro' class='acoesIcon'class='acoesIcon' ></a> " +
				"</td>" +
				"</tr>"

			}
			
			
			
			
		} else if (listaDeCliente == ""){
			tabela += "<tr><td colspan='6'>Nenhum cliente encontrado</td></tr>";
		}
		tabela += "</table>";
		
		return tabela;
		
	};
	
	//Busca os Clientes atualizados do BD	
	INTRATAREFAS.cliente.buscarPorNome = function(){
				
		var valorBusca = $("#campoBuscaCliente").val();//valor digitado no filtro
		
		$.ajax({
			type: "GET",//usado o GET por ser uma busca de informações
			url: INTRATAREFAS.PATH + "cliente/buscar",
			data: "valorBusca="+valorBusca,
			success: function(dados){
				
				dados = JSON.parse(dados);
				console.log(dados)
				$("#listaCliente").html(INTRATAREFAS.cliente.exibir(dados));
				
				
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao consultar os clientes: "+ info.status + " - " + info.responseText);
			}
		});
		
	};
	
	//Atualiza a tabela
	
	INTRATAREFAS.cliente.buscarPorNome();
	
	
	//Deleta os dados do BD
	INTRATAREFAS.cliente.excluir = function(id){
		$.ajax({
			type:"DELETE",
			url: INTRATAREFAS.PATH + "cliente/excluir/"+id,
			success: function(msg){
				//Verificação pode ficar aqui
				INTRATAREFAS.exibirAviso(msg);
				INTRATAREFAS.cliente.buscarPorNome();
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao excluir cliente: "+ info.status + " - " + info.responseText);
			}
		});
	};
	
	
	INTRATAREFAS.cliente.confirmaExclusao = function(id){
		var modalExcluiCliente = {
				title: "Excluir Clientes",
				height: 250,
				width: 550,
				modal: true,
				buttons:{
					"Excluir": function(){
						INTRATAREFAS.cliente.excluir(id)
						$(this).dialog("close");
					},	
				"Cancelar": function(){
					INTRATAREFAS.cliente.buscarPorNome();
					$(this).dialog("close");
				
			}
				},
			close: function(){
				//caso o usuário simplismente feche a caixa de edição
				//não deve acontecer nada
			}
		};

	$("#modalExcluiCliente").dialog(modalExcluiCliente);
	};
	
	//Exibe a modal de Edição
	INTRATAREFAS.cliente.exibirEdicao = function(id){
		$.ajax({
			type:"GET",
			url: INTRATAREFAS.PATH + "cliente/buscarPorId",
			data: "id="+id,
			success: function(cliente){
				
			document.frmEditaCliente.idCliente.value = cliente.idcliente;
			document.frmEditaCliente.txtnome.value = cliente.nome;
			document.frmEditaCliente.cpf.value = cliente.cpf;
			document.frmEditaCliente.email.value = cliente.email;
			document.frmEditaCliente.telefone.value = cliente.telefone;
		

			
				
				var modalEditaCliente = {
						title: "Editar Informações do Cliente",
						height: 400,
						width: 550,
						modal: true,
						buttons:{
							"Salvar": function(){
								INTRATAREFAS.cliente.editar();
								
							},
							"Cancelar": function(){
								INTRATAREFAS.cliente.buscarPorNome();
								$(this).dialog("close");
							}
						},
						close: function(){
							//caso o usuário simplismente feche a caixa de edição
							//não deve acontecer nada
						}
				};
				
				$("#modalEditaCliente").dialog(modalEditaCliente);
		
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao buscar clientes para edição: "+ info.status + " - " + info.responseText);
			}
	});
	};
	
	//Edita as informações do funcionário
	INTRATAREFAS.cliente.editar = function(){
	

		var cliente = new Object();
		
		
		
		cliente.idcliente = document.frmEditaCliente.idCliente.value;
		cliente.nome = document.frmEditaCliente.txtnome.value;
		cliente.cpf = document.frmEditaCliente.cpf.value;
		cliente.email = document.frmEditaCliente.email.value;
		cliente.telefone = document.frmEditaCliente.telefone.value;

		var cpf = document.frmEditaCliente.cpf.value;// receber o valor do campo txtnome em uma variável
		var expRegCpf = new RegExp("^[0-9]{11}$");//Criado um objeto RegExp e atribuido a variável expRegNome

		if(!expRegCpf.test(cpf)){
		alert("Preencha o campo do CPF corretamente. ");
		document.frmEditaCliente.cpf.focus();/*motivo de após o "ok" ir direto ao input do Nome, devido ao focus*/
			return false;/*impede o envio do formulário e de seguir a leitura dos códigos*/
		}
		
		var email = document.frmEditaCliente.email.value;
		var expRegEmail = new RegExp("^[A-zÀ-ü]{1,}[@]{1}[A-z]{2,}([.]{1}[A-z]{1,})+$");
		if(!expRegEmail.test(email)){
			alert("Favor preencha o campo E-mail corretamente. ");
				document.frmEditaCliente.email.focus();
				return false;
			}
		
		var telefone  =  document.frmEditaCliente.telefone.value;
		var expRegFone = new RegExp("^[0-9]{11}$");

		if(!expRegFone.test(telefone)){
			alert("Preencha o campo Telefone corretamente. ");
				document.frmEditaCliente.telefone.focus();
				return false;
			}
		
		if((cliente.nome=="")||(cliente.email=="")||(cliente.cpf=="")
					||(cliente.telefone=="")){
			
			
			
			INTRATAREFAS.exibirAviso("Preencha todos os campos corretamente!");
		
		}else{
			
		$.ajax({
			type: "PUT",
			url: INTRATAREFAS.PATH + "cliente/alterar", 
			data: JSON.stringify(cliente),
			success: function(msg){
				INTRATAREFAS.exibirAviso(msg);
				INTRATAREFAS.cliente.buscarPorNome();
				$("#modalEditaCliente").dialog("close");
				//mensagem de aviso do sucesso atraves da modal
			},
			error: function(info){
				INTRATAREFAS.exibirAviso("Erro ao editar o cliente: "+ info.status + " - "+ info.responseText);
				
			}	
			});
		}
	};
	
	INTRATAREFAS.cliente.orcamento = function(){
		
		
		
		var manutencao = new Object();
		manutencao.cliente_idcliente = document.frmAddOrcamento.cpf.value;//não valida 0 zero
		manutencao.nome = document.frmAddOrcamento.txtnome.value;		
		manutencao.equipamento =document.frmAddOrcamento.equipamento.value;
		manutencao.marca =document.frmAddOrcamento.marca.value;
		manutencao.modelo =document.frmAddOrcamento.modelo.value;
		manutencao.defeito =document.frmAddOrcamento.defeito.value;
		
		var cpf = document.frmAddOrcamento.cpf.value;// receber o valor do campo txtnome em uma variável
		var expRegCpf = new RegExp("^[0-9]{11}$");//Criado um objeto RegExp e atribuido a variável expRegNome

		if(!expRegCpf.test(cpf)){
		alert("Preencha o campo do CPF corretamente. ");
			document.frmAddOrcamento.cpf.focus();/*motivo de após o "ok" ir direto ao input do Nome, devido ao focus*/
			return false;/*impede o envio do formulário e de seguir a leitura dos códigos*/
		}
		
		
		if((manutencao.nome=="")||(manutencao.equipamento=="")||(manutencao.marca=="")
					||(manutencao.modelo=="")||(manutencao.defeito=="")){
			
			
			
			INTRATAREFAS.exibirAviso("Preencha todos os campos corretamente!");
		
			
		}else{
		
			$.ajax({
				type: "POST", 
				url: INTRATAREFAS.PATH + "cliente/orcamento",
				data:JSON.stringify(manutencao), 
				success: function (msg) {
			INTRATAREFAS.exibirAviso(msg);
			$("#addOrcamento").trigger("reset"); 
			
				},
				error: function(info) {
					
					INTRATAREFAS.exibirAviso("Erro ao cadastrar um novo cliente: "+ info.status + " - " + info.responseText);		
			}	
			});	
			}	
	}
	
	INTRATAREFAS.cliente.exibirAtendimento = function(listaDeAtendimento) {
		
		var tabela = "<table>" +
				"<tr>" +
				"<th>Cliente</th>" +
				"<th>Atendente</th>" +
				"<th>Equipamento</th>" +
				"<th>Defeito</th>" +
				"<th>Status</th>" +
				"<th class='acoes'>Ações</th>" +
				"</tr>";
		
		if (listaDeAtendimento != undefined && listaDeAtendimento.length > 0){
			
			for (var i=0; i<listaDeAtendimento.length; i++){
				if (listaDeAtendimento[i].status=="Em Análise"){
				tabela += "<tr>" +
				"<td>"+listaDeAtendimento[i].nome+"</td>" +
				"<td>"+listaDeAtendimento[i].funcionario+"</td>" +
				"<td>"+listaDeAtendimento[i].equipamento+"</td>" +
				"<td>"+listaDeAtendimento[i].defeito+"</td>" +
				"<td>"+listaDeAtendimento[i].status+"</td>" +
				"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
				"<a  onclick=\"INTRATAREFAS.cliente.exibeOrçamento('"+listaDeAtendimento[i].atendimento+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
				"</td>" +
				"</tr>"
				
				} else if (listaDeAtendimento[i].status== "Em Aberto"){
					tabela += "<tr>" +
				"<td>"+listaDeAtendimento[i].nome+"</td>" +
				"<td>"+listaDeAtendimento[i].funcionario+"</td>" +
				"<td>"+listaDeAtendimento[i].equipamento+"</td>" +
				"<td>"+listaDeAtendimento[i].defeito+"</td>" +
				"<td>"+listaDeAtendimento[i].status+"</td>" +
				"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
				"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('"+listaDeAtendimento[i].atendimento+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
				"</td>" +
				"</tr>"		
				}
				
				 else if (listaDeAtendimento[i].status== "Em Atendimento"){
				tabela += "<tr>" +
				"<td>"+listaDeAtendimento[i].nome+"</td>" +
				"<td>"+listaDeAtendimento[i].funcionario+"</td>" +
				"<td>"+listaDeAtendimento[i].equipamento+"</td>" +
				"<td>"+listaDeAtendimento[i].defeito+"</td>" +
				"<td>"+listaDeAtendimento[i].status+"</td>" +
				"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
				"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('"+listaDeAtendimento[i].idAtendimento+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
				"</td>" +
				"</tr>"		
				}
				
				else if (listaDeAtendimento[i].status== "Em Teste"){
				tabela += "<tr>" +
				"<td>"+listaDeAtendimento[i].nome+"</td>" +
				"<td>"+listaDeAtendimento[i].funcionario+"</td>" +
				"<td>"+listaDeAtendimento[i].equipamento+"</td>" +
				"<td>"+listaDeAtendimento[i].defeito+"</td>" +
				"<td>"+listaDeAtendimento[i].status+"</td>" +
				"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
				"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('"+listaDeAtendimento[i].idAtendimento+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
				"</td>" +
				"</tr>"		
				}
				
				else if (listaDeAtendimento[i].status== "Em Análise de Garantia"){
				tabela += "<tr>" +
				"<td>"+listaDeAtendimento[i].nome+"</td>" +
				"<td>"+listaDeAtendimento[i].funcionario+"</td>" +
				"<td>"+listaDeAtendimento[i].equipamento+"</td>" +
				"<td>"+listaDeAtendimento[i].defeito+"</td>" +
				"<td>"+listaDeAtendimento[i].status+"</td>" +
				"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
				"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('"+listaDeAtendimento[i].idAtendimento+"')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
				"</td>" +
				"</tr>"		
				}

			}
			
			
			
			
		} else if (listaDeAtendimento == ""){
			tabela += "<tr><td colspan='8'>Nenhum atendimento encontrado</td></tr>";
		}
		tabela += "</table>";
		
		return tabela;
		
	};
	
	//busca os atendimentos para a exibição	

});//end_docsready
