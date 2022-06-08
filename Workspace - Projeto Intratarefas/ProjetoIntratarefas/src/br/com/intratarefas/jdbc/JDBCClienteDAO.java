package br.com.intratarefas.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.intratarefas.jdbcinterface.ClienteDAO;
import br.com.intratarefas.modelo.Cliente;
import br.com.intratarefas.modelo.Manutencao;

public class JDBCClienteDAO implements ClienteDAO{

private Connection conexao;
	
	public JDBCClienteDAO(Connection conexao) {
		this.conexao = conexao;
	}
	
	public List<JsonObject> buscarPorNome(String nomeCliente){

		//Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM cliente ";
		
		if (!nomeCliente.equals("")) {
			comando += "WHERE nome LIKE '%" + nomeCliente + "%' ";	
		}
		
		comando += "ORDER BY nome ASC";
		
		//Criação de uma lista para armazenar
		List<JsonObject> listaCliente = new ArrayList<JsonObject>();
		
		//Criação do objeto marca com valor null(ou seja, sem instanciá-lo)
		JsonObject cliente = null;
		
		//Abertura do try-catch
		try {
			Statement stmt = conexao.createStatement();		
			
			
			
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {
				
				
				int idcliente = rs.getInt("idcliente");
				
				String nome = rs.getString("nome");
				String cpf = rs.getString("cpf");
				String email = rs.getString("email");
				String telefone = rs.getString("telefone");
				
				
				
				
				cliente = new JsonObject();
				cliente.addProperty("idcliente",idcliente);
				cliente.addProperty("nome",nome);
				cliente.addProperty("cpf",cpf);
				cliente.addProperty("email",email);
				cliente.addProperty("telefone",telefone);
				
				
				listaCliente.add(cliente);
			}
		}catch (Exception ex) {
			
			ex.printStackTrace();
		}
		
		return listaCliente;
	
}

	public boolean validaCliente(Cliente cliente) {
		String comando = "SELECT * FROM cliente WHERE cpf = ?";
		try {
			
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setString(1,cliente.getCpf());		
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

	public boolean inserir(Cliente cliente) {
		String comando = "INSERT INTO cliente (idcliente, nome, cpf, email, telefone) VALUES (?,?,?,?,?)";
		PreparedStatement p;
		
		try {
			
			//Prepara o comando para a execução no BD em que nos conectamos
			p = this.conexao.prepareStatement(comando);
			
			//Substitui no comando os "?" pelos respectivos valores do produto
			p.setInt (1, cliente.getIdcliente());
			p.setString (2, cliente.getNome());
			p.setString (3, cliente.getCpf());
			p.setString (4, cliente.getEmail());
			p.setString (5, cliente.getTelefone());



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
			String comando = "DELETE FROM cliente WHERE idcliente = ?";
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

	public boolean verificaIdCliente(int id) {

		String comando = "SELECT * FROM cliente WHERE idcliente = " + id;
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
	
	public Cliente buscarPorId(int id) {
		
		String comando = "SELECT *FROM cliente WHERE idcliente = ?";
		Cliente cliente = new Cliente();
		try {
			PreparedStatement p = this.conexao.prepareStatement(comando); //Preparar o comando
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {
				
				String nome = rs.getString("nome");
				String cpf = rs.getString("cpf");
				String email = rs.getString("email");
				String telefone = rs.getString("telefone");

				cliente.setIdcliente(id);
				
				cliente.setNome(nome);
				cliente.setCpf(cpf);
				cliente.setEmail(email);
				cliente.setTelefone(telefone);
		

			}
		}catch (SQLException e) {
			e.printStackTrace();
		
		}
		return cliente;
	}

	public boolean alterar(Cliente cliente) {
		
		String comando = "UPDATE cliente " +"SET nome=?, cpf=?, email=?, telefone=?"
		+ " WHERE idcliente=?";
	//O id é pego através do proprio html quando clicamos na opção do produto especifico
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, cliente.getNome());
			p.setString(2, cliente.getCpf());
			p.setString(3, cliente.getEmail());
			p.setString(4, cliente.getTelefone());
			p.setInt(5, cliente.getIdcliente());
			p.executeUpdate();

			//setado os valores alterados no produto
			
		
		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public long selecionaCliente(long id) {
		long idcliente=0;
		String comando = "SELECT * FROM cliente WHERE cpf LIKE '%0" + id + "%'";
		try {

			PreparedStatement p = this.conexao.prepareStatement(comando); // Preparar o comando
			ResultSet rs = p.executeQuery();

			while (rs.next()) { //Se entrar aqui é porque ele conseguiu encontrar resultados no bd, assim pode retornar true
				idcliente = rs.getInt("idcliente");
				
			}
			return idcliente;
		} catch (SQLException e) {
			e.printStackTrace();
		return idcliente;
	}
	}
	
	public boolean orcamento(Manutencao manutencao) {
		String comando = "INSERT INTO manutencao (cliente_idcliente, equipamento, marca, modelo, defeito, status) VALUES (?,?,?,?,?,?)";
		PreparedStatement p;
		
		try {
			
			//Prepara o comando para a execução no BD em que nos conectamos
			p = this.conexao.prepareStatement(comando);
			
			//Substitui no comando os "?" pelos respectivos valores do produto
			p.setLong (1, manutencao.getIdcliente());
			p.setInt (2, manutencao.getEquipamento());
			p.setString (3, manutencao.getMarca());
			p.setString (4, manutencao.getModelo());
			p.setString (5, manutencao.getDefeito());
			p.setInt (6, 1);//Status recebe esse valor ao realizar o orçamento



			//Executa o comando no BD 
			p.execute();

		}catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}//end_jdbc