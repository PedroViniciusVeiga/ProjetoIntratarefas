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

import com.sun.xml.internal.messaging.saaj.util.Base64;

import br.com.intratarefas.bd.Conexao;
import br.com.intratarefas.jdbc.JDBCFuncionarioDAO;
import br.com.intratarefas.modelo.Funcionario;

public class LogoffServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void process(HttpServletRequest request, 
			HttpServletResponse response)
	throws ServletException, IOException{
		

			
		HttpSession sessao = request.getSession(false);
		Object user = sessao.getAttribute("login");

		if (user != null) {
			sessao.removeAttribute("login");
		}
		
		response.sendRedirect("index.html");

			
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

	

