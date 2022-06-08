package br.com.intratarefas.rest;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.intratarefas.bd.Conexao;
import br.com.intratarefas.filter.ServletFilter;
import br.com.intratarefas.jdbc.JDBCProcedimentoDAO;
import br.com.intratarefas.modelo.Manutencao;
import br.com.intratarefas.modelo.Procedimento;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Path("procedimento")
public class ProcedimentoRest  extends UtilRest{

	@GET
	@Path("/buscar")
	@Consumes("application/*")//Consome o resultado da busca
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nomeProcedimento) {
		
		try {
			
			List<JsonObject> listaProcedimento = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao); 
			listaProcedimento = jdbcProcedimento.buscarPorNome(nomeProcedimento);
			conec.fecharConexao();
		
			String json = new Gson().toJson(listaProcedimento);
			return this.buildResponse(json);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}	

	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String procedimentoParam) {

	try {
		
		
		
		Procedimento procedimento = new Gson().fromJson(procedimentoParam, Procedimento.class);
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		
		
		
		JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
		boolean retornoProcedimento = jdbcProcedimento.validaProcedimento(procedimento);
		String msg = "teste";
		if(retornoProcedimento == false) {		
		boolean retorno = jdbcProcedimento.inserir(procedimento);
		conec.fecharConexao();
		if (retorno){
		msg = "Procedimento cadastrado com sucesso!";
		}
		}else{
			msg = "Há um procedimento cadastrado com esse nome, altere e tente novamente";
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
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);

			boolean retornoProcedimento = jdbcProcedimento.verificaIdProcedimento(id);// Verifica a existencia do 
			boolean retornoDeletar = jdbcProcedimento.deletar(id);//exclui o  em questão
			conec.fecharConexao();
			String msg = "";
			if(retornoProcedimento == false) {
				msg = "O procedimento selecionado não foi encontrado, carregue a página e tente novamente! ";
				return this.buildErrorResponse(msg);
			}else if(retornoDeletar == true){
				msg = "Procedimento excluído com sucesso!";
				
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
			Procedimento procedimento = new Procedimento();// cria um novo produto para sobrepor o antigo
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
			
			
			boolean retornoProc = jdbcProcedimento.verificaIdProcedimento(id);
			procedimento = jdbcProcedimento.buscarPorId(id);
			conec.fecharConexao();
			String msg= "";
			if(retornoProc == true) {
			
			}else if (retornoProc == false) {
				msg ="O procedimento selecionado não existe, carregue a página e selecione novamente!";
				return this.buildErrorResponse(msg);

			}

			return this.buildResponse(procedimento);
		}catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}
	
	@PUT
	@Path("/alterar")//após salvar
	@Consumes("application/*")
	public Response alterar(String procedimentoParam) {
		try {
			Procedimento procedimento = new Gson().fromJson(procedimentoParam, Procedimento.class);

			//conversão do json recebido em um objeto da classe Produto
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
			boolean retornoProc = jdbcProcedimento.verificaIdProcedimento(procedimento.getIdprocedimento());
			
			
			String msg = "";

			//valida duplicadas
			
			if(retornoProc == true) {
					boolean retorno = jdbcProcedimento.alterar(procedimento);
					
			if (retorno){
				msg = "Procedimento alterado com sucesso!";
			}
				
				
			}else {
				msg = "Alteração não efetuada, o procedimento selecionado não existe no sistema!";
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
	@Path("/atendimentoTecnico")///ProjetoTrilhaRest/rest/produto/buscar este é o caminho por estar comentado após o rest/ o produto e agora a requisição d busca
	@Consumes("application/*")//Consome o resultado da busca
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorAtendimento(@Context HttpServletRequest request){
		
		try {
			
			List<JsonObject> listaAtendimento = new ArrayList<JsonObject>();
			
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
			
			//Teste teste = new Teste();
			//teste.doFilter(request);
			
			
			
			HttpSession session = request.getSession();//Cria a Session
			String usuario = null;
			usuario = (String)session.getAttribute("login");//usuario recebe os dados de "login", no caso o nome de usuário
			listaAtendimento = jdbcProcedimento.buscarAtendimentos(usuario);
			conec.fecharConexao();
		
			String json = new Gson().toJson(listaAtendimento);
			return this.buildResponse(json);
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}	
	
	@GET
	@Path("/buscarPorAtendimento")//com a modal na hora de salvar
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorAtendimento(@QueryParam("id") int id) { //utilizar atributo na url
		
		try {
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
			List<JsonObject> listaAtendimento = new ArrayList<JsonObject>();

			
			listaAtendimento = jdbcProcedimento.buscarPorAtendimento(id);
			conec.fecharConexao();

			String json = new Gson().toJson(listaAtendimento);
			return this.buildResponse(json);
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
		
		
	}	
	
	@GET
	@Path("/buscarProc")
	@Produces(MediaType.APPLICATION_JSON)//produzir conteúdos em JSON
	public Response buscarProc() {
		
		try {
		List<Procedimento> listaProcedimento = new ArrayList<Procedimento>();
	
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
		listaProcedimento = jdbcProcedimento.buscarProc();
		conec.fecharConexao();
		return this.buildResponse(listaProcedimento);
	
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
	@POST
	@Path("/inserirProcedimento")
	@Consumes("application/*")
	public Response inserirProcedimento(String manutencaoParam) {
		System.out.println(manutencaoParam);//manutencao
		try {
			
			
			Manutencao manutencao = new Gson().fromJson(manutencaoParam, Manutencao.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			
			JDBCProcedimentoDAO jdbcProcedimento = new JDBCProcedimentoDAO(conexao);
			boolean retorno = jdbcProcedimento.inserirProcedimento(manutencao);
			boolean retornoProcedimento = jdbcProcedimento.verificarProcedimento(manutencao);
			String msg = "null";
			System.out.println(retornoProcedimento);
			if(retornoProcedimento) {
				System.out.println(retornoProcedimento);
			 msg = "Erro ao cadastrar procedimento, o procedimento já foi adicionado!. ";
			return this.buildResponse(msg);
			}else {
			if(retorno) {
				msg = "Procedimento cadastrado com sucesso!";
			}else{
				 msg = "Erro ao cadastrar procedimento";
			}
			}
			conec.fecharConexao();
			
			return this.buildResponse(msg);
			
			
		}catch(Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
	
}//end_rest
