package br.com.intratarefas.jdbcinterface;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.intratarefas.modelo.Procedimento;

public interface ProcedimentoDAO {

	public List<JsonObject> buscarPorNome(String nomeProcedimento);
	public boolean inserir(Procedimento procedimento);
	public boolean validaProcedimento(Procedimento procedimento);
}
