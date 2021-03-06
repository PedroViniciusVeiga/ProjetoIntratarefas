package br.com.intratarefas.jdbc;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import com.google.gson.JsonObject;

import br.com.intratarefas.jdbcinterface.FuncionarioDAO;
import br.com.intratarefas.modelo.Funcionario;

public class JDBCFuncionarioDAO implements FuncionarioDAO{
	
	private Connection conexao;
	
	public JDBCFuncionarioDAO(Connection conexao) {
		this.conexao = conexao;
	}

public List<JsonObject> buscarPorNome(String nomeFuncionario){
		
		//Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM funcionario ";
		
		if (!nomeFuncionario.equals("")) {
			//concatena no comando where buscando no nome do produto
			//o texto da variável nome
			comando += "WHERE user LIKE '%" + nomeFuncionario + "%' ";	
		}
		
		//Finaliza o comando ordenando alfabeticamente por
		//categoria, marca e depois modelo
		comando += "ORDER BY user ASC";
		
		//Criação de uma lista para armazenar
		List<JsonObject> listaFuncionario = new ArrayList<JsonObject>();
		
		//Criação do objeto marca com valor null(ou seja, sem instanciá-lo)
		JsonObject funcionario = null;
		
		//Abertura do try-catch
		try {
			Statement stmt = conexao.createStatement();		
			
			
			
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {
				
				
				int idfuncionario = rs.getInt("idfuncionario");
				String user = rs.getString("user");
				String nome = rs.getString("nome");
				String cpf = rs.getString("cpf");
				String email = rs.getString("email");
				String funcao = rs.getString("funcao");
				
				if (funcao.equals("1")) {
					funcao = "Administrador";
				}else if (funcao.equals("2")) {
					funcao = "Atendente";
				}else if (funcao.equals("3")) {
					funcao = "Técnico";
				}
				
				
				
				funcionario = new JsonObject();
				funcionario.addProperty("idfuncionario",idfuncionario);
				funcionario.addProperty("user",user);
				funcionario.addProperty("nome",nome);
				funcionario.addProperty("cpf",cpf);
				funcionario.addProperty("email",email);
				funcionario.addProperty("funcao",funcao);
				
				
				listaFuncionario.add(funcionario);
			}
		}catch (Exception ex) {
			
			ex.printStackTrace();
		}
		
		return listaFuncionario;
	
}

public boolean validaFuncionario(Funcionario funcionario) {
	String comando = "SELECT * FROM funcionario WHERE user = ?";
	try {
		
		PreparedStatement p = this.conexao.prepareStatement(comando);
		p.setString(1,funcionario.getUser());		
		ResultSet rs = p.executeQuery();
		while (rs.next()) { //Se entrar aqui é porque ele conseguiu encontrar resultados no bd, assim pode retornar true
			
			
			return true;// Se a marca existir no bd pode deletar
		}
		return false;// Marca não existe no bd não pode deletar
	} catch (SQLException e) {
		e.printStackTrace();
	return false;
}

}

//Adiciona Funcionários
public boolean inserir(Funcionario funcionario) {
	
	System.out.println(funcionario+"FUNCIONARIO");
	String comando = "INSERT INTO funcionario (idfuncionario, user, nome, cpf, email, funcao, password) VALUES (?,?,?,?,?,?,?)";
	PreparedStatement p;
	
	
	try {
		
		//Prepara o comando para a execução no BD em que nos conectamos
		p = this.conexao.prepareStatement(comando);
		
		String senmd5 = "";
		MessageDigest md = null; 
		
		try {
		
			md = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException e) {
		
			e.printStackTrace();
		}
		
		BigInteger hash = new BigInteger(1, md.digest(funcionario.getPassword().getBytes()));

		senmd5 = hash.toString(16);
		
		funcionario.setPassword(senmd5);
		
		//Substitui no comando os "?" pelos respectivos valores do produto
		p.setInt (1, funcionario.getIdfuncionario());
		p.setString(2, funcionario.getUser());
		p.setString (3, funcionario.getNome());
		p.setString (4, funcionario.getCpf());
		p.setString (5, funcionario.getEmail());
		p.setInt (6, funcionario.getFuncao());
		p.setString (7, funcionario.getPassword());


		//Executa o comando no BD 
		p.execute();

	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

//Deleta Direto
public boolean deletar(int id) {
		String comando = "DELETE FROM funcionario WHERE idfuncionario = ?";
		PreparedStatement p;
	try {
		p= this.conexao.prepareStatement(comando);
		p.setInt(1, id);
		p.execute();
	}catch (SQLException e) {
		e.printStackTrace();
	return false;
		}
	return true;
	}		

public boolean verificaIdFuncionario(int id) {

	String comando = "SELECT * FROM funcionario WHERE idfuncionario = " + id;
	try {

		PreparedStatement p = this.conexao.prepareStatement(comando); // Preparar o comando
		ResultSet rs = p.executeQuery();

		while (rs.next()) { //Se entrar aqui é porque ele conseguiu encontrar resultados no bd, assim pode retornar true
			
			
			return true;// Se existir no bd pode deletar
		}
		return false;//  existe no bd não pode deletar
	} catch (SQLException e) {
		e.printStackTrace();
	return false;
}

}

public Funcionario buscarPorId(int id) {
	
	String comando = "SELECT *FROM funcionario WHERE idfuncionario = ?";
	Funcionario funcionario = new Funcionario();
	try {
		PreparedStatement p = this.conexao.prepareStatement(comando); //Preparar o comando
		p.setInt(1, id);
		ResultSet rs = p.executeQuery();
		while (rs.next()) {
			
			String nome = rs.getString("nome");
			String cpf = rs.getString("cpf");
			String email = rs.getString("email");
			String password = rs.getString("password");
			int funcao = rs.getInt("funcao");
		
			
			funcionario.setIdfuncionario(id);
			

			funcionario.setNome(nome);
			funcionario.setCpf(cpf);
			funcionario.setEmail(email);
			funcionario.setPassword(password);
			funcionario.setFuncao(funcao);
			

			

		}
	}catch (SQLException e) {
		e.printStackTrace();
	
	}
	return funcionario;
}

public boolean alterar(Funcionario funcionario) {
	
	String comando = "UPDATE funcionario " +"SET nome=?, cpf=?, email=?, password=?, funcao=?"
	+ " WHERE idfuncionario=?";
//O id é pego através do proprio html quando clicamos na opção do produto especifico
	PreparedStatement p;
	
	String senmd5 = "";
	MessageDigest md = null; 
	
	try {
	
		md = MessageDigest.getInstance("MD5");
	}catch (NoSuchAlgorithmException e) {
	
		e.printStackTrace();
	}
	
	BigInteger hash = new BigInteger(1, md.digest(funcionario.getPassword().getBytes()));

	senmd5 = hash.toString(16);
	
	funcionario.setPassword(senmd5);
	
	try {
		p = this.conexao.prepareStatement(comando);
		p.setString(1, funcionario.getNome());
		p.setString(2, funcionario.getCpf());
		p.setString(3, funcionario.getEmail());
		p.setString(4, funcionario.getPassword());
		p.setInt(5, funcionario.getFuncao());
		p.setInt(6, funcionario.getIdfuncionario());
		p.executeUpdate();

		//setado os valores alterados no produto
		
	
	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
	
}

public int consultar(Funcionario funcionario) {
	
	
	int funcao = 0;
	try {
		
		String comando = "SELECT * FROM funcionario WHERE user = '"+funcionario.getUser()+"' and password = '"+funcionario.getPassword()+"'";
		java.sql.Statement stmt = conexao.createStatement();
		ResultSet rs = stmt.executeQuery(comando);
		if(rs.next()) {
			
			 funcao = rs.getInt("funcao");
			return funcao;
		}else { 
			return funcao;
		}	
	}catch (Exception e) {
		return funcao;
	}
	
}

public List<JsonObject> buscarPorStatus(int status,String inicio, String fim) {//vai ser id ou usuario
	

	String comando = "select manutencao.atendimento, cliente.nome, funcionario.user funcionario, manutencao.equipamento, manutencao.defeito, manutencao.status"
			+ " from manutencao"
			+ " inner join cliente"
			+ " on cliente.idcliente = manutencao.cliente_idcliente"
			+ " left join funcionario"
			+ " on funcionario.idfuncionario = manutencao.funcionario_idfuncionario";
	
	
	if (!inicio.equals("") && !fim.equals("") && status != 0) {
		comando += " where manutencao.inicio between '" + inicio + "' and '" + fim + "' AND manutencao.status ="
				+ status;
	} else if (!inicio.equals("") && !fim.equals("")) {
		comando += " where manutencao.inicio between '" + inicio + "' and '" + fim + "'";
	} else if (status != 0) {
		// concatena no comando where buscando no nome do produto
		// o texto da variável nome
		comando += " where manutencao.status =" + status;
	}
	
	//Finaliza o comando ordenando alfabeticamente por
	//categoria, marca e depois modelo
	comando += " ORDER BY manutencao.atendimento ASC";
	
	System.out.println(comando);
	
	
	List<JsonObject> listaAtendimento = new ArrayList<JsonObject>();
	JsonObject atendimentos = null;
	
	
	try {
		
		Statement stmt = conexao.createStatement();
		ResultSet rs = stmt.executeQuery(comando);
		
		while (rs.next()) {//abaixo é o que será feito em cada linha que virá do BD
			int atendimento = rs.getInt("atendimento");
			String nomeCliente = rs.getString("nome");//cliente
			String nomeFuncionario = rs.getString("funcionario");//funcionario
			String equipamento = rs.getString("equipamento");
			String defeito = rs.getString("defeito");
			String statusAtendimento = rs.getString("status");

			if (equipamento.equals("1")) {
				equipamento = "Computador";
			}else if (equipamento.equals("2")) {
				equipamento = "Smartphone";
			}else if (equipamento.equals("3")) {
				equipamento = "Notebook";
			}else if (equipamento.equals("4")) {
				equipamento = "Tablet";
			}else if (equipamento.equals("5")) {
				equipamento = "Impressora";
			}
			
			if (statusAtendimento.equals("1")) {
				statusAtendimento = "Em Análise";
			}else if (statusAtendimento.equals("2")) {
				statusAtendimento = "Em Aberto";
			}else if (statusAtendimento.equals("3")) {
				statusAtendimento = "Em Aberto";
			}else if (statusAtendimento.equals("4")) {
				statusAtendimento = "Recusado";
			}else if (statusAtendimento.equals("5")) {
				statusAtendimento = "Em Atendimento";
			}else if (statusAtendimento.equals("6")) {
				statusAtendimento = "Em Teste";
			}else if (statusAtendimento.equals("7")) {
				statusAtendimento = "Finalizado";
			}else if (statusAtendimento.equals("8")) {
				statusAtendimento = "Cancelado";
			}else if (statusAtendimento.equals("9")) {
				statusAtendimento = "Equipamento Entregue";
			}else if (statusAtendimento.equals("10")) {
				statusAtendimento = "Em Análise de Garantia";
			}else if (statusAtendimento.equals("11")) {
				statusAtendimento = "Solicitação Negada";
			}
			atendimentos = new JsonObject();//criar códigos no formato JSON, mas como um objeto Java.
			atendimentos.addProperty("atendimento", atendimento);
			atendimentos.addProperty("nome", nomeCliente);
			atendimentos.addProperty("funcionario", nomeFuncionario);
			atendimentos.addProperty("equipamento", equipamento);
			atendimentos.addProperty("defeito", defeito);
			atendimentos.addProperty("status", statusAtendimento);
			
			listaAtendimento.add(atendimentos);
		}	
	}catch (Exception e) {
		e.printStackTrace();
	}
			
			return listaAtendimento;		
}



}//end

