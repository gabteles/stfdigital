package br.jus.stf.plataforma.workflow.domain;

import org.activiti.engine.runtime.ProcessInstance;

import br.jus.stf.autuacao.domain.entity.Peticao;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 26.06.2015
 */
public interface ProcessoRepository {

	String criar(String id, Peticao peticao);
	
	void alterar(String id, String nome, String valor);

	ProcessInstance consultar(String id);
}
