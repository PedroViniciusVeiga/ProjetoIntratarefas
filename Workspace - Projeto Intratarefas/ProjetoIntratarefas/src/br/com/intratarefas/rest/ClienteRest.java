package br.com.intratarefas.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.intratarefas.bd.Conexao;
import br.com.intratarefas.jdbc.JDBCClienteDAO;
import br.com.intratarefas.modelo.Cliente;
import br.com.intratarefas.modelo.Manutencao;

@Path("cliente")
public class ClienteRest extends UtilRest{

	@GET
	@Path("/buscar")
	@Consumes("application/*")//Consome o resultado da busca
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nomeCliente) {
		
		try {
			
			List<JsonObject> listaCliente = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCClienteDAO jdbcCliente = new JDBCClienteDAO(conexao); 
			listaCliente = jdbcCliente.buscarPorNome(nomeCliente);
			conec.fecharConexao();
		
			String json = new Gson().toJson(listaCliente);
			return this.buildResponse(json);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String clienteParam) {

	try {
		
		
		
		Cliente cliente = new Gson().fromJson(clienteParam, Cliente.class);
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		
		
		JDBCClienteDAO jdbcCliente = new JDBCClienteDAO(conexao); 		
		boolean retornoCliente = jdbcCliente.validaCliente(cliente);//verifica existencia
		String msg = "teste";
		if(retornoCliente == false) {		
		boolean retorno = jdbcCliente.inserir(cliente);
		conec.fecharConexao();
		if (retorno){
		msg = "Cliente cadastrado com sucesso!";
		}
		}else{
			msg = "Há um cliente cadastrado com esse CPF, altere e tente novamente";
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
			JDBCClienteDAO jdbcCliente = new JDBCClienteDAO(conexao); 

			boolean retornoCliente = jdbcCliente.verificaIdCliente(id);// Verifica a existencia 
			boolean retornoDeletar = jdbcCliente.deletar(id);//exclui
			conec.fecharConexao();
			String msg = "";
			if(retornoCliente == false) {
				msg = "O cliente selecionado não foi encontrado, carregue a página e tente novamente! ";
				return this.buildErrorResponse(msg);
			}else if(retornoDeletar == true){
				msg = "Cliente excluído com sucesso!";
				
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
			Cliente cliente = new Cliente();// cria um novo cliente para sobrepor o antigo
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCClienteDAO jdbcCliente = new JDBCClienteDAO(conexao);
			
			
			boolean retornoCliente = jdbcCliente.verificaIdCliente(id);
			cliente = jdbcCliente.buscarPorId(id);
			conec.fecharConexao();
			String msg= "";
			if(retornoCliente == true) {
			
			}else if (retornoCliente == false) {
				msg ="O cliente selecionado não existe, carregue a página e selecione novamente!";
				return this.buildErrorResponse(msg);

			}

			return this.buildResponse(cliente);
		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@PUT
	@Path("/alterar")//após salvar
	@Consumes("application/*")
	public Response alterar(String clienteParam) {
		try {
			Cliente cliente = new Gson().fromJson(clienteParam, Cliente.class);

			//conversão do json recebido em um objeto da classe cliente
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCClienteDAO jdbcCliente = new JDBCClienteDAO(conexao);

			
			
			boolean retornoCliente = jdbcCliente.verificaIdCliente(cliente.getIdcliente());
			
			
			String msg = "";

			//valida duplicadas
			
			if(retornoCliente == true) {
					boolean retorno = jdbcCliente.alterar(cliente);
					
			if (retorno){
				msg = "Cliente alterado com sucesso!";
			}
				
				
			}else {
				msg = "Alteração não efetuada, o cliente selecionado não existe no sistema!";
				return this.buildErrorResponse(msg);

			}
			
			conec.fecharConexao();
			
			return this.buildResponse(msg);
		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
	}
	
	@POST
	@Path("/orcamento")
	@Consumes("application/*")
	public Response orcamento(String orcamentoParam) {

	try {
		
		
		System.out.println(orcamentoParam);
		Manutencao manutencao = new Gson().fromJson(orcamentoParam, Manutencao.class);
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		
		
		JDBCClienteDAO jdbcCliente = new JDBCClienteDAO(conexao); 		
		Long idCliente = jdbcCliente.selecionaCliente(manutencao.getIdcliente());//Aqui o valor do idcliente esta com o cpf, e ira verificar no bd se contem esse cpf
		String msg = "teste";
		if(idCliente != 0) {//se ter esse cpf o idcliente passa a receber o idcliente que é buscado da tabela cliente		
		manutencao.setIdcliente(idCliente);
		System.out.println(manutencao.getIdcliente());
		boolean retorno = jdbcCliente.orcamento(manutencao);
		conec.fecharConexao();
		if (retorno){
		msg = "Orçamento aberto com sucesso!";
		}
		}else{//se não tiver, atribuimos o valor de 0 ao idcliente para sinalizar que o não existe um cliente com esse cpf
			msg = "Nenhum cliente encontrado com este CPF!";
			return this.buildErrorResponse(msg);

		}
		return this.buildResponse(msg);
	}catch(Exception e) {
		e.printStackTrace();
		return this.buildErrorResponse(e.getMessage());
	}
}
	
}//end_rest
