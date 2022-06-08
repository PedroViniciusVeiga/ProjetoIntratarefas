package br.com.intratarefas.modelo;

import java.io.Serializable;

public class ProcedimentoManutencao  implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private int idprocedimento ;
	private float valor;

	public int getIdprocedimento() {
		return idprocedimento;
	}
	public void setIdprocedimento(int idprocedimento) {
		this.idprocedimento = idprocedimento;
	}
	public float getValor() {
		return valor;
	}
	public void setValor(float valor) {
		this.valor = valor;
	}

}
