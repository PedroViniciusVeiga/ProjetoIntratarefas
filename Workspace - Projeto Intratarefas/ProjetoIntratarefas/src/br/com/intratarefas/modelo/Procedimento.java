package br.com.intratarefas.modelo;

import java.io.Serializable;

public class Procedimento implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int idprocedimento ;
	private String nome;
	
	
	public int getIdprocedimento() {
		return idprocedimento;
	}
	public void setIdprocedimento(int idprocedimento) {
		this.idprocedimento = idprocedimento;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

}

