package br.com.intratarefas.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.intratarefas.bd.Conexao;
import br.com.intratarefas.jdbc.JDBCFuncionarioDAO;
import br.com.intratarefas.jdbc.JDBCProcedimentoDAO;
import br.com.intratarefas.modelo.Funcionario;

@Path("funcionario")
public class FuncionarioRest extends UtilRest{
	
	@GET
	@Path("/buscar")
	@Consumes("application/*")//Consome o resultado da busca
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nomeFuncionario) {
		
		try {
			
			List<JsonObject> listaFuncionario = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao); 
			listaFuncionario = jdbcFuncionario.buscarPorNome(nomeFuncionario);
			conec.fecharConexao();
		
			String json = new Gson().toJson(listaFuncionario);
			return this.buildResponse(json);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}	


	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String funcionarioParam) {

	try {
		
		
		
		Funcionario funcionario = new Gson().fromJson(funcionarioParam, Funcionario.class);
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		
		
		
		JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
		boolean retornoFuncionario = jdbcFuncionario.validaFuncionario(funcionario);
		String msg = "teste";
		if(retornoFuncionario == false) {		
		boolean retorno = jdbcFuncionario.inserir(funcionario);
		conec.fecharConexao();
		if (retorno){
		msg = "Funcionário cadastrado com sucesso!";
		}
		}else{
			msg = "Há um funcionário cadastrado com esse usuário, altere e tente novamente";
			return this.buildErrorResponse(msg);

		}
		return this.buildResponse(msg);
	}catch(Exception e) {
		e.printStackTrace();
		return this.buildErrorResponse(e.getMessage());
	}
}
	
	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir (@PathParam("id") int id){ //utilizar atributo sendo o seu proprio valor
		
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao); 

			boolean retornoFuncionario = jdbcFuncionario.verificaIdFuncionario(id);// Verifica a existencia do funcionário
			boolean retornoDeletar = jdbcFuncionario.deletar(id);//exclui o funcionário em questão
			conec.fecharConexao();
			String msg = "";
			if(retornoFuncionario == false) {
				msg = "O funcionário selecionado não foi encontrado, carregue a página e tente novamente! ";
				return this.buildErrorResponse(msg);
			}else if(retornoDeletar == true){
				msg = "Funcionário excluído com sucesso!";
				
			}	
				
			
			
			return this.buildResponse(msg);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@GET
	@Path("/buscarPorId")//com a modal na hora de salvar
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) { //utilizar atributo na url
		
		try {
			Funcionario funcionario = new Funcionario();// cria um novo produto para sobrepor o antigo
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			
			
			boolean retornoFunc = jdbcFuncionario.verificaIdFuncionario(id);
			funcionario = jdbcFuncionario.buscarPorId(id);
			conec.fecharConexao();
			String msg= "";
			if(retornoFunc == true) {
			
			}else if (retornoFunc == false) {
				msg ="O funcionário selecionado não existe, carregue a página e selecione novamente!";
				return this.buildErrorResponse(msg);

			}

			return this.buildResponse(funcionario);
		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@PUT
	@Path("/alterar")//após salvar
	@Consumes("application/*")
	public Response alterar(String funcionarioParam) {
		try {
			Funcionario funcionario = new Gson().fromJson(funcionarioParam, Funcionario.class);

			//conversão do json recebido em um objeto da classe Produto
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);

			
			
			boolean retornoFunc = jdbcFuncionario.verificaIdFuncionario(funcionario.getIdfuncionario());
			
			
			String msg = "";

			//valida duplicadas
			
			if(retornoFunc == true) {
					boolean retorno = jdbcFuncionario.alterar(funcionario);
					
			if (retorno){
				msg = "Funcionário alterado com sucesso!";
				
			}
				
				
			}else {
				msg = "Alteração não efetuada, o funcionário selecionado não existe no sistema!";
				return this.buildErrorResponse(msg);

			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}

	
	@GET
	@Path("/buscarPorStatus")//com a modal na hora de salvar
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorStatus(@QueryParam("status") int status , @QueryParam("inicio") String inicio, @QueryParam("fim") String fim) { //utilizar atributo na url
		
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
			List<JsonObject> listaAtendimentosGerais = new ArrayList<JsonObject>();

			
			listaAtendimentosGerais = jdbcFuncionario.buscarPorStatus(status,inicio,fim);
			conec.fecharConexao();

			String json = new Gson().toJson(listaAtendimentosGerais);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
}