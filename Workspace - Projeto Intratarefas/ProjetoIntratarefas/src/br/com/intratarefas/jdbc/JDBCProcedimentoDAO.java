package br.com.intratarefas.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.intratarefas.jdbcinterface.ProcedimentoDAO;
import br.com.intratarefas.modelo.Manutencao;
import br.com.intratarefas.modelo.Procedimento;
import br.com.intratarefas.modelo.ProcedimentoManutencao;

public class JDBCProcedimentoDAO implements ProcedimentoDAO{
	
	private Connection conexao;
	
	public JDBCProcedimentoDAO(Connection conexao) {
		this.conexao = conexao;
	}

	public List<JsonObject> buscarPorNome(String nomeProcedimento){
		
		//Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM procedimento ";
		
		if (!nomeProcedimento.equals("")) {
			//concatena no comando where buscando no nome do produto
			//o texto da variável nome
			comando += "WHERE nome LIKE '%" + nomeProcedimento + "%' ";	
		}
		
		//Finaliza o comando ordenando alfabeticamente por
		//categoria, marca e depois modelo
		comando += "ORDER BY nome ASC";
		
		//Criação de uma lista para armazenar
		List<JsonObject> listaProcedimento = new ArrayList<JsonObject>();
		
		//Criação do objeto marca com valor null(ou seja, sem instanciá-lo)
		JsonObject procedimento = null;
		
		//Abertura do try-catch
		try {
			Statement stmt = conexao.createStatement();		
			
			
			
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {
				
				
				int idprocedimento = rs.getInt("idprocedimento");
				String nome = rs.getString("nome");
		
				
				procedimento = new JsonObject();
				procedimento.addProperty("idprocedimento",idprocedimento);
				procedimento.addProperty("nome",nome);
				
				listaProcedimento.add(procedimento);
			}
		}catch (Exception ex) {
			
			ex.printStackTrace();
		}
		
		return listaProcedimento;
	
}

	public boolean validaProcedimento(Procedimento procedimento) {
	String comando = "SELECT * FROM procedimento WHERE nome = ?";
	try {
		
		PreparedStatement p = this.conexao.prepareStatement(comando);
		p.setString(1,procedimento.getNome());		
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
	public boolean inserir(Procedimento procedimento) {
	
	
	String comando = "INSERT INTO procedimento (idprocedimento,nome) VALUES (?,?)";
	PreparedStatement p;
	
	try {
		
		//Prepara o comando para a execução no BD em que nos conectamos
		p = this.conexao.prepareStatement(comando);
		
		//Substitui no comando os "?" pelos respectivos valores do produto
		p.setInt (1, procedimento.getIdprocedimento());
		p.setString (2, procedimento.getNome());



		//Executa o comando no BD 
		p.execute();

	}catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
	return true;
}

	public boolean verificaIdProcedimento(int id) {

	String comando = "SELECT * FROM procedimento WHERE idprocedimento = " + id;
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

	public boolean deletar(int id) {
		String comando = "DELETE FROM procedimento WHERE idprocedimento = ?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Procedimento buscarPorId(int id) {

		String comando = "SELECT *FROM procedimento WHERE idprocedimento = ?";
		Procedimento procedimento = new Procedimento();

		try {
			PreparedStatement p = this.conexao.prepareStatement(comando); // Preparar o comando
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {

				String nome = rs.getString("nome");

				procedimento.setIdprocedimento(id);
				procedimento.setNome(nome);
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
		return procedimento;
	}

	public boolean alterar(Procedimento procedimento) {

		String comando = "UPDATE procedimento " + "SET nome=?" + " WHERE idprocedimento=?";
//O id é pego através do proprio html quando clicamos na opção do produto especifico
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, procedimento.getNome());
			p.setInt(2, procedimento.getIdprocedimento());
			p.executeUpdate();

			// setado os valores alterados no produto

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	public List<JsonObject> buscarAtendimentos(String usuario) {//vai ser id ou usuario
		
		
		//inicia a criação do comando SQL de busca
		String comando = "select  manutencao.atendimento, cliente.nome, funcionario.user funcionario, manutencao.equipamento, manutencao.defeito,  manutencao.status \r\n"
				+ "from manutencao \r\n"
				+ "inner join cliente \r\n"
				+ "on cliente.idcliente = manutencao.cliente_idcliente \r\n"
				+ "inner join funcionario \r\n"
				+ "on funcionario.idfuncionario = manutencao.funcionario_idfuncionario\r\n"
				+ "where funcionario.user = '"+usuario+"' ";	
		
		
		//Finaliza o comando ordenando alfabeticamente por
		//categoria, marca e depois modelo
		comando += "ORDER BY manutencao.status ASC";
		
		List<JsonObject> listaAtendimento = new ArrayList<JsonObject>();
		JsonObject manutencao = null;
		
		
		try {
			
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {//abaixo é o que será feito em cada linha que virá do BD
				
				int atendimento= rs.getInt("atendimento");
				String nomeCliente = rs.getString("nome");//cliente
				String nomeFuncionario = rs.getString("funcionario");//funcionario
				String equipamento = rs.getString("equipamento");
				String defeito = rs.getString("defeito");
				String status = rs.getString("status");
				
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
				
				if (status.equals("1")) {
					status = "Em Análise";
				}else if (status.equals("3")) {
					status = "Em Aberto";
				}else if (status.equals("5")) {
					status = "Em Atendimento";
				}else if (status.equals("6")) {
					status = "Em Teste";
				}else if (status.equals("10")) {
					status = "Em Análise de Garantia";
				}
				
					manutencao = new JsonObject();//criar códigos no formato JSON, mas como um objeto Java.
					manutencao.addProperty("atendimento", atendimento);
					manutencao.addProperty("nome", nomeCliente);
					manutencao.addProperty("funcionario", nomeFuncionario);
					manutencao.addProperty("equipamento", equipamento);
					manutencao.addProperty("defeito", defeito);
					manutencao.addProperty("status", status);
				

					listaAtendimento.add(manutencao);
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}
				
				return listaAtendimento;		
	}
		
	public List<JsonObject> buscarPorAtendimento(int id) {//vai ser id ou usuario
		
		
		String comando = "select cliente.nome, manutencao.equipamento, manutencao.defeito\r\n"
				+ "from manutencao\r\n"
				+ "inner join cliente \r\n"
				+ "on cliente.idcliente = manutencao.cliente_idcliente \r\n"
				+ "where manutencao.atendimento = "+id;
		
		List<JsonObject> listaAtendimento = new ArrayList<JsonObject>();
		JsonObject manutencao = null;
		
		
		try {
			
			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {//abaixo é o que será feito em cada linha que virá do BD
				
				String nomeCliente = rs.getString("nome");//cliente
				String equipamento = rs.getString("equipamento");
				String defeito = rs.getString("defeito");
				

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
				
				manutencao = new JsonObject();//criar códigos no formato JSON, mas como um objeto Java.
				manutencao.addProperty("atendimento", id);
				manutencao.addProperty("nome", nomeCliente);
				manutencao.addProperty("equipamento", equipamento);
				manutencao.addProperty("defeito", defeito);
				listaAtendimento.add(manutencao);
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}
				
				return listaAtendimento;		
	}
	
	public List<Procedimento> buscarProc(){
		
		//Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM procedimento";
		
		List<Procedimento> listProcedimento = new ArrayList<Procedimento>();
		
		Procedimento procedimento = null;
		
		try {
			
			Statement stmt = conexao.createStatement();			
			
			ResultSet rs = stmt.executeQuery(comando);
			
			while (rs.next()) {
				
				procedimento = new Procedimento();
				
				int idprocedimento = rs.getInt("idprocedimento");
				String nome = rs.getString("nome");
				
				procedimento.setIdprocedimento(idprocedimento);
				procedimento.setNome(nome);
				
				listProcedimento.add(procedimento);
			}

			
		}catch (Exception ex) {
			//Exibe a exceção na console
			ex.printStackTrace();
		}
		return listProcedimento;
	
	}
	
	public boolean inserirProcedimento(Manutencao manutencao){
		String comando = "INSERT into manutencao_has_procedimento (manutencao_atendimento, procedimento_idprocedimento, valor, is_realizado) values (?,?,?,?)";
		PreparedStatement p;
		try {
			for(ProcedimentoManutencao procedimentoManutencao: manutencao.getProcedimentos()) {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1,  manutencao.getAtendimento());
			p.setInt(2, procedimentoManutencao.getIdprocedimento());
			p.setFloat(3, procedimentoManutencao.getValor());
			p.setInt(4, 0);//recebe zero, pois o procedimento não foi realizado
			p.execute();
				
					
				}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean verificarProcedimento(Manutencao manutencao){
	
		int atendimento =  0;
		int procedimento = 0;
	
			for(ProcedimentoManutencao procedimentoManutencao: manutencao.getProcedimentos()) {
			
			 atendimento =   manutencao.getAtendimento();
			 procedimento =  procedimentoManutencao.getIdprocedimento();
			}
			
			String comando = "select * from manutencao_has_procedimento where manutencao_atendimento = "+atendimento+" and procedimento_idprocedimento = "+procedimento;
			System.out.println(comando);

			try {
			Statement stmt = conexao.createStatement();			
		
			ResultSet rs = stmt.executeQuery(comando);
			while (rs.next()) {
			
				return true;//achou duplicada
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;//pode cadastrar
		}
		return false;
	}
	
}//END_jdbc
