INTRATAREFAS.procedimento = new Object();



$(document).ready(function() {

	//carrega os procedimentos para mostrar na modal
	INTRATAREFAS.procedimento.carregarProcedimento = function(id) {
		var select = document.getElementsByName("procId[]");


		$.ajax({
			type: "GET",
			url: INTRATAREFAS.PATH + "procedimento/buscarProc",
			success: function(procedimento) {



				$(select).html("");

				if (procedimento != "") {

					var option = document.createElement("option");
					option.setAttribute("value", "");
					option.innerHTML = ("Escolha");
					$(select).append(option);

					for (var i = 0; i < procedimento.length; i++) {

						var option = document.createElement("option");
						option.setAttribute("value", procedimento[i]['idprocedimento']);
						option.innerHTML = (procedimento[i]['nome']);
						$(select).append(option);
					}

				} else {

					$(select).html("");

					var option = document.createElement("option");
					option.setAttribute("value", "");
					option.innerHTML = ("Nenhum procedimento cadastrado");
					$(select).append(option);
					$(select).addClass("aviso");

				}
			},
			error: function(info) {

				INTRATAREFAS.exibirAviso("Erro ao buscar procedimentos: " + info.status + " - " + info.responseText);

				$(select).html("");
				var option = document.createElement("option");
				option.setAttribute("value", "");
				option.innerHTML = ("Erro ao carregar procedimentos");
				$(select).append(option);
				$(select).addClass("aviso");

			}

		});
	}

	//Adiciona uma linha do MD
	$(".botaoAdd").click(function() {

		//cria um clone da primeira linha e salva na "novoCampo"
		var novoCampo = $("tr.detalhes:first").clone();

		//Esvazia os inputs do clone
		novoCampo.find("input").val("");

		//Insere o clone na página, logo após a ultima linha
		novoCampo.insertAfter("tr.detalhes:last");

	});

	//Remove o campo do MD
	INTRATAREFAS.procedimento.removeCampo = function(botao) {
		//se houver mais de uma no MD
		if ($("tr.detalhes").length > 1) {
			//remove a linha que contem o botão
			//o segundo parent é a linha onde esta o botão

			$(botao).parent().parent().remove();
			//senão é porque só tem uma linha
		} else {
			//avisa que não pode remover
			INTRATAREFAS.exibirAviso("A última linha não pode ser removida")
		}
	}

	INTRATAREFAS.procedimento.validaDetalhe = function() {
		var procValidar = document.getElementsByName("procId[]");
		var valorValidar = document.getElementsByName("valorProc[]");

		for (var i = 0; i < procValidar.length; i++) {
			var linha = i + 1;
			if ((procValidar[i].value == "") || (valorValidar[i].value == "")) {
				INTRATAREFAS.exibirAviso("A linha " + linha + " não foi completamente preenchida");
				return false;
			}
		}
		//Se chegar aqui, esta tudo ok e return true
		return true;
	}

	INTRATAREFAS.procedimento.cadastrarMestre = function(atendimento) {

		if (INTRATAREFAS.procedimento.validaDetalhe()) {
			var manutencao = new Object();
			manutencao.atendimento = atendimento;
			var procedimentos = document.getElementsByName("procId[]");
			var valores = document.getElementsByName("valorProc[]");

			manutencao.procedimentos = new Array(procedimentos.length);
			for (var i = 0; i < procedimentos.length; i++) {//criar uma classe modelo com array
				manutencao.procedimentos[i] = new Object();
				manutencao.procedimentos[i].idprocedimento = procedimentos[i].value;
				manutencao.procedimentos[i].valor = valores[i].value;

			}
			console.log(manutencao);
			$.ajax({
				type: "POST",
				url: INTRATAREFAS.PATH + "procedimento/inserirProcedimento",
				data: JSON.stringify(manutencao),
				success: function(msg) {
					INTRATAREFAS.exibirAviso(msg);
				},
				error: function(info) {
					INTRATAREFAS.exibirAviso("Erro ao cadastrar o procedimento: " + info.status + " - " + info.statusText);
				}
			});


		}


	}


	INTRATAREFAS.procedimento.cadastrar = function() {

		var procedimento = new Object();
		procedimento.nome = document.frmAddProcedimento.descricao.value;


		if (procedimento.nome == "") {

			INTRATAREFAS.exibirAviso("Preencha o campo Nome corretamente!");


		} else {

			$.ajax({
				type: "POST",
				url: INTRATAREFAS.PATH + "procedimento/inserir",
				data: JSON.stringify(procedimento),
				success: function(msg) {
					INTRATAREFAS.exibirAviso(msg);
					$("#addProcedimento").trigger("reset");
					INTRATAREFAS.procedimento.buscarPorNome();
				},
				error: function(info) {

					INTRATAREFAS.exibirAviso("Erro ao cadastrar um novo procedimento: " + info.status + " - " + info.responseText);
				}
			});
		}
	}

	//Transforma os dados dos produtos recebidos do servidor em uma tabela HTML
	INTRATAREFAS.procedimento.exibir = function(listaDeProcedimento) {

		var tabela = "<table>" +
			"<tr>" +
			"<th>Nome</th>" +
			"<th class='acoes'>Ações</th>" +
			"</tr>";

		if (listaDeProcedimento != undefined && listaDeProcedimento.length > 0) {

			for (var i = 0; i < listaDeProcedimento.length; i++) {
				tabela += "<tr>" +
					"<td style='text-align: center;'>" + listaDeProcedimento[i].nome + "</td>" +
					"<td>" +
					"<a onclick=\"INTRATAREFAS.procedimento.exibirEdicao('" + listaDeProcedimento[i].idprocedimento + "')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon' ></a> " +
					"<a onclick=\"INTRATAREFAS.procedimento.confirmaExclusao('" + listaDeProcedimento[i].idprocedimento + "')\"><img src='../../img/delete.png' alt='Excluir Registro' class='acoesIcon'class='acoesIcon' ></a> " +
					"</td>" +
					"</tr>"

			}




		} else if (listaDeProcedimento == "") {
			tabela += "<tr><td colspan='6'>Nenhum procedimento encontrado</td></tr>";
		}
		tabela += "</table>";

		return tabela;

	};

	//Função que busca os dados no banco para exibir, ou o que foi especificado na busca
	INTRATAREFAS.procedimento.buscarPorNome = function() {

		var valorBusca = $("#campoBuscaProcedimento").val();//valor digitado no filtro

		$.ajax({
			type: "GET",//usado o GET por ser uma busca de informações
			url: INTRATAREFAS.PATH + "procedimento/buscar",
			data: "valorBusca=" + valorBusca,
			success: function(dados) {

				dados = JSON.parse(dados);
				$("#listaProcedimento").html(INTRATAREFAS.procedimento.exibir(dados));


			},
			error: function(info) {
				INTRATAREFAS.exibirAviso("Erro ao consultar os procedimento: " + info.status + " - " + info.responseText);
			}
		});

	};

	//Atualiza a tabela
	INTRATAREFAS.procedimento.buscarPorNome();

	INTRATAREFAS.procedimento.excluir = function(id) {
		$.ajax({
			type: "DELETE",
			url: INTRATAREFAS.PATH + "procedimento/excluir/" + id,
			success: function(msg) {
				//Verificação pode ficar aqui
				INTRATAREFAS.exibirAviso(msg);
				INTRATAREFAS.procedimento.buscarPorNome();
			},
			error: function(info) {
				INTRATAREFAS.exibirAviso("Erro ao excluir o procedimento: " + info.status + " - " + info.responseText);
				INTRATAREFAS.procedimento.buscarPorNome();

			}
		});
	};

	INTRATAREFAS.procedimento.confirmaExclusao = function(id) {
		var modalExcluiProcedimento = {
			title: "Excluir Procedimentos",
			height: 250,
			width: 550,
			modal: true,
			buttons: {
				"Excluir": function() {
					INTRATAREFAS.procedimento.excluir(id)
					$(this).dialog("close");
				},
				"Cancelar": function() {
					INTRATAREFAS.procedimento.buscarPorNome();
					$(this).dialog("close");

				}
			},
			close: function() {
				//caso o usuário simplismente feche a caixa de edição
				//não deve acontecer nada
			}
		};

		$("#modalExcluiProcedimento").dialog(modalExcluiProcedimento);
	};

	INTRATAREFAS.procedimento.exibirEdicao = function(id) {
		$.ajax({
			type: "GET",
			url: INTRATAREFAS.PATH + "procedimento/buscarPorId",
			data: "id=" + id,
			success: function(procedimento) {

				document.frmEditaProcedimento.idProcedimento.value = procedimento.idprocedimento;
				document.frmEditaProcedimento.descricao.value = procedimento.nome;

				if (procedimento.nome == "") {

					INTRATAREFAS.exibirAviso("Preencha o campo Nome corretamente!");


				} else {

					var modalEditaProcedimento = {
						title: "Editar Informações de Procedimentos",
						height: 400,
						width: 550,
						modal: true,
						buttons: {
							"Salvar": function() {
								INTRATAREFAS.procedimento.editar();

							},
							"Cancelar": function() {
								INTRATAREFAS.procedimento.buscarPorNome();

								$(this).dialog("close");
							}
						},
						close: function() {
							//caso o usuário simplismente feche a caixa de edição
							//não deve acontecer nada
						}
					};
				}
				$("#modalEditaProcedimento").dialog(modalEditaProcedimento);

			},
			error: function(info) {
				INTRATAREFAS.exibirAviso("Erro ao buscar procedimentos para edição: " + info.status + " - " + info.responseText);
			}
		});
	};

	//Edita as informações do procedimento
	INTRATAREFAS.procedimento.editar = function() {


		var procedimento = new Object();



		procedimento.idprocedimento = document.frmEditaProcedimento.idProcedimento.value;
		procedimento.nome = document.frmEditaProcedimento.descricao.value;


		if (procedimento.nome == "") {



			INTRATAREFAS.exibirAviso("Preencha a descrição corretamente!");


		} else {


			$.ajax({
				type: "PUT",
				url: INTRATAREFAS.PATH + "procedimento/alterar",
				data: JSON.stringify(procedimento),
				success: function(msg) {
					INTRATAREFAS.exibirAviso(msg);
					INTRATAREFAS.procedimento.buscarPorNome();
					$("#modalEditaProcedimento").dialog("close");
					//mensagem de aviso do sucesso atraves da modal
				},
				error: function(info) {
					INTRATAREFAS.exibirAviso("Erro ao editar o procedimento: " + info.status + " - " + info.responseText);

				}
			});
		}
	};


	//tabela de exibição dos atendimentos
	INTRATAREFAS.procedimento.exibirAtendimento = function(listaDeAtendimento) {

		var tabela = "<table>" +
			"<tr>" +
			"<th>Cliente</th>" +
			"<th>Atendente</th>" +
			"<th>Equipamento</th>" +
			"<th>Defeito</th>" +
			"<th>Status</th>" +
			"<th class='acoes'>Ações</th>" +
			"</tr>";

		if (listaDeAtendimento != undefined && listaDeAtendimento.length > 0) {

			for (var i = 0; i < listaDeAtendimento.length; i++) {
				if (listaDeAtendimento[i].status == "Em Análise") {
					tabela += "<tr>" +
						"<td>" + listaDeAtendimento[i].nome + "</td>" +
						"<td>" + listaDeAtendimento[i].funcionario + "</td>" +
						"<td>" + listaDeAtendimento[i].equipamento + "</td>" +
						"<td>" + listaDeAtendimento[i].defeito + "</td>" +
						"<td>" + listaDeAtendimento[i].status + "</td>" +
						"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
						"<a  onclick=\"INTRATAREFAS.procedimento.exibeOrçamento('" + listaDeAtendimento[i].atendimento + "')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
						"</td>" +
						"</tr>"

				} else if (listaDeAtendimento[i].status == "Em Aberto") {
					tabela += "<tr>" +
						"<td>" + listaDeAtendimento[i].nome + "</td>" +
						"<td>" + listaDeAtendimento[i].funcionario + "</td>" +
						"<td>" + listaDeAtendimento[i].equipamento + "</td>" +
						"<td>" + listaDeAtendimento[i].defeito + "</td>" +
						"<td>" + listaDeAtendimento[i].status + "</td>" +
						"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
						"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('" + listaDeAtendimento[i].atendimento + "')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
						"</td>" +
						"</tr>"
				}

				else if (listaDeAtendimento[i].status == "Em Atendimento") {
					tabela += "<tr>" +
						"<td>" + listaDeAtendimento[i].nome + "</td>" +
						"<td>" + listaDeAtendimento[i].funcionario + "</td>" +
						"<td>" + listaDeAtendimento[i].equipamento + "</td>" +
						"<td>" + listaDeAtendimento[i].defeito + "</td>" +
						"<td>" + listaDeAtendimento[i].status + "</td>" +
						"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
						"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('" + listaDeAtendimento[i].idAtendimento + "')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
						"</td>" +
						"</tr>"
				}

				else if (listaDeAtendimento[i].status == "Em Teste") {
					tabela += "<tr>" +
						"<td>" + listaDeAtendimento[i].nome + "</td>" +
						"<td>" + listaDeAtendimento[i].funcionario + "</td>" +
						"<td>" + listaDeAtendimento[i].equipamento + "</td>" +
						"<td>" + listaDeAtendimento[i].defeito + "</td>" +
						"<td>" + listaDeAtendimento[i].status + "</td>" +
						"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
						"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('" + listaDeAtendimento[i].idAtendimento + "')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
						"</td>" +
						"</tr>"
				}

				else if (listaDeAtendimento[i].status == "Em Análise de Garantia") {
					tabela += "<tr>" +
						"<td>" + listaDeAtendimento[i].nome + "</td>" +
						"<td>" + listaDeAtendimento[i].funcionario + "</td>" +
						"<td>" + listaDeAtendimento[i].equipamento + "</td>" +
						"<td>" + listaDeAtendimento[i].defeito + "</td>" +
						"<td>" + listaDeAtendimento[i].status + "</td>" +
						"<td>" +//chamar a função de acordo com o tipo de atendimento talvez um if
						"<a  onclick=\"INTRATAREFAS.procedimento.editarAtendimento('" + listaDeAtendimento[i].idAtendimento + "')\"><img src='../../img/edit.png' alt='Editar Registro' class='acoesIcon'></a> " +
						"</td>" +
						"</tr>"
				}

			}




		} else if (listaDeAtendimento == "") {
			tabela += "<tr><td colspan='8'>Nenhum atendimento encontrado</td></tr>";
		}
		tabela += "</table>";

		return tabela;

	};

	//busca os atendimentos para a exibição
	INTRATAREFAS.procedimento.buscarPorAtendimento = function() {

		//pegar id do funcionario, para aparecer somente os atdmts dele

		$.ajax({
			type: "GET",//usado o GET por ser uma busca de informações
			url: INTRATAREFAS.PATH + "procedimento/atendimentoTecnico",
			success: function(dados) {

				dados = JSON.parse(dados);

				$("#listaDeAtendimento").html(INTRATAREFAS.procedimento.exibirAtendimento(dados));
				//id do html

			},
			error: function(info) {
				INTRATAREFAS.exibirAviso("Erro ao consultar os atendimentos: " + info.status + " - " + info.responseText);
			}
		});

	};
	INTRATAREFAS.procedimento.buscarPorAtendimento();



	//Edita o orçamento
	INTRATAREFAS.procedimento.exibeOrçamento = function(id) {
		$.ajax({
			type: "GET",
			url: INTRATAREFAS.PATH + "procedimento/buscarPorAtendimento",
			data: "id=" + id,
			success: function(manutencao) {
				manutencao = JSON.parse(manutencao);
				var atendimento;
				if (manutencao != undefined && manutencao.length > 0) {

					for (var i = 0; i < manutencao.length; i++) {
						atendimento = manutencao[i].atendimento;
						var tabela = "<table id='infoOrcamento'>" +
							"<tr>" +
							"<td>Cliente : " + manutencao[i].nome + "</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Equipamento : " + manutencao[i].equipamento + "</td>" +
							"</tr>" +
							"<tr>" +
							"<td>Descrição do Defeito : </td>" +
							"</tr>" +
							"<tr>" +
							"<td>" + manutencao[i].defeito + "</td>" +
							"</tr>";

					}
				}
				$("#infoAtendimento").html(tabela);

				INTRATAREFAS.procedimento.carregarProcedimento(id);

				var modalEditaOrcamento = {
					title: "Registro de Orçamento",
					height: 400,
					width: 650,
					modal: true,
					buttons: {
						"Salvar": function() {
							INTRATAREFAS.procedimento.cadastrarMestre(atendimento);
							$(this).dialog("close");
						},
						"Cancelar": function() {
							INTRATAREFAS.procedimento.buscarPorAtendimento();

							$(this).dialog("close");
						}
					},
					close: function() {
						//caso o usuário simplesmente feche a caixa de edição
						//não deve acontecer nada
					}
				};

				$("#modalEditaOrcamento").dialog(modalEditaOrcamento);



			},
			error: function(info) {
				INTRATAREFAS.exibirAviso("Erro ao buscar atendimento: " + info.status + " - " + info.responseText);
			}
		});

	};







});//end_js