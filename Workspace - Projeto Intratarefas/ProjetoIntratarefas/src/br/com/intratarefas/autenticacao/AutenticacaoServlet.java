package br.com.intratarefas.autenticacao;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.intratarefas.bd.Conexao;
import br.com.intratarefas.jdbc.JDBCFuncionarioDAO;
import br.com.intratarefas.modelo.Funcionario;

import com.sun.xml.internal.messaging.saaj.util.Base64;

public class AutenticacaoServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void process(HttpServletRequest request, 
			HttpServletResponse response)
	throws ServletException, IOException{
		
	
		
		/*
		 * Desencriptando a senha enviada e armazenando na váriavel
		 * textodeserializado
		 */
	
		//imprimindo no console a variável contendo a senha desencriptada
	
		Funcionario funcionario = new Funcionario();
		
		funcionario.setUser(request.getParameter("txtuser"));
		String textodeserializado = new String(Base64.base64Decode(request.getParameter("txtpassword")));
		String senmd5 = "";
		MessageDigest md = null; 
		
		try {
		
			md = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException e) {
		
			e.printStackTrace();
		}
		
		BigInteger hash = new BigInteger(1, md.digest(request.getParameter("txtpassword").getBytes()));

		senmd5 = hash.toString(16);
		
		funcionario.setPassword(senmd5);
		
		Conexao conec = new Conexao();
		Connection conexao = conec.abrirConexao();
		JDBCFuncionarioDAO jdbcFuncionario = new JDBCFuncionarioDAO(conexao);
		int funcao = jdbcFuncionario.consultar(funcionario);
		

		if(funcao > 0) {
			
			HttpSession session = request.getSession();
			System.out.println(session);
			
			session.setAttribute("login", request.getParameter("txtuser"));
			
			switch (funcao) {
			case 1:
				response.sendRedirect("pages/administrador/index.html");
				break;
			case 2:	
				response.sendRedirect("pages/atendente/index.html");
				break;
			case 3:	
				response.sendRedirect("pages/tecnico/index.html");
				break;
			}
			
			
		}else {
			
			response.sendRedirect("erro.html");
		}
		
		
	}
		
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException{
		process(request,response);
	}
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response)throws ServletException, IOException{
		process(request,response);
	}
		
		
		
		
}
