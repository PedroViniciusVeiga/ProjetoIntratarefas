package br.com.intratarefas.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.intratarefas.modelo.Cliente;



public interface ClienteDAO {

	public List<JsonObject> buscarPorNome(String nomeCliente);
	public boolean inserir(Cliente cliente);
	public boolean deletar (int id);
	public boolean validaCliente(Cliente cliente);
	public boolean alterar(Cliente cliente);
	public boolean verificaIdCliente(int id);
}
