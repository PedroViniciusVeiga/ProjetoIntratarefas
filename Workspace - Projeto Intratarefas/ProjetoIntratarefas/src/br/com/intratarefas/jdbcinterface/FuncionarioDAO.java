package br.com.intratarefas.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.intratarefas.modelo.Funcionario;

public interface FuncionarioDAO {

	
	public List<JsonObject> buscarPorNome(String nomeFuncionario);
	public boolean inserir(Funcionario funcionario);
	public boolean deletar (int id);
	public Funcionario buscarPorId(int id);
	public boolean validaFuncionario(Funcionario funcionario);
	public boolean alterar(Funcionario funcionario);
	public boolean verificaIdFuncionario(int id);
	public int consultar(Funcionario funcionario);
}
