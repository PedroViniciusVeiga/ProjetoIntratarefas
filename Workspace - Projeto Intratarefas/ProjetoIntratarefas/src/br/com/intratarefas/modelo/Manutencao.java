package br.com.intratarefas.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Manutencao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int atendimento;
	private int equipamento;
	private String marca ;
	private String modelo ;
	private String defeito ;
	private String dataEntrega ;
	private int status;
	private long cliente_idcliente;
	private int idfuncionario ;
	private ArrayList<ProcedimentoManutencao> procedimentos = new ArrayList<ProcedimentoManutencao>();
	
	public int getAtendimento() {
		return atendimento;
	}
	public void setAtendimento(int atendimento) {
		this.atendimento = atendimento;
	}
	public int getEquipamento() {
		return equipamento;
	}
	public void setEquipamento(int equipamento) {
		this.equipamento = equipamento;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getDefeito() {
		return defeito;
	}
	public void setDefeito(String defeito) {
		this.defeito = defeito;
	}
	public String getDataEntrega() {
		return dataEntrega;
	}
	public void setDataEntrega(String dataEntrega) {
		this.dataEntrega = dataEntrega;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getIdcliente() {
		return cliente_idcliente;
	}
	public void setIdcliente(long idcliente) {
		this.cliente_idcliente = idcliente;
	}
	public int getIdfuncionario() {
		return idfuncionario;
	}
	public void setIdfuncionario(int idfuncionario) {
		this.idfuncionario = idfuncionario;
	}
	public String getModelo() {
		return modelo;
	}
	public void setModelo(String modelo) {
		this.modelo = modelo;
	}
	public ArrayList<ProcedimentoManutencao> getProcedimentos() {
		return procedimentos;
	}
	public void setProcedimentos(ArrayList<ProcedimentoManutencao> procedimentos) {
		this.procedimentos = procedimentos;
	}



	
}
