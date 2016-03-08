package br.jus.stf.processamentoinicial.autuacao.interfaces.commands;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Objeto usado para enviar os dados necessários para préautuar uma petição física.
 * 
 * @author Anderson.Araujo
 * @author Rodrigo Barreiros
 * 
 * @version 1.0.0
 * @since 15.09.2015
 */
@ApiModel(value = "Contém as informações necessárias para pré-autuar a petição física recebida pelo Recebedor.")
public class PreautuarPeticaoFisicaCommand {

	@NotNull
	@ApiModelProperty(value = "Id da petição física registrada.", required=true)
	private Long peticaoId;
	
	@ApiModelProperty(value = "A classe processual sugerida pelo pré-utuador.")
	private String classeId;
	
	@NotNull
	@ApiModelProperty(value = "Contém o resultado da análise do pré-autuador, indicando se a petição está 'Correta' ou 'Indevida'", required=true)
	private Boolean valida;
	
	@ApiModelProperty(value = "Contém o motivo da recusa da petição, no caso de petições indevidas")
	private String motivo;
	
	@ApiModelProperty(value = "Lista de preferências")
	private List<Long> preferencias;
	
	public Long getPeticaoId() {
		return peticaoId;
	}
	
	public String getClasseId() {
		return classeId;
	}
	
	public Boolean isValida() {
		return valida;
	}
	
	public String getMotivo() {
		return motivo;
	}
	
	public List<Long> getPreferencias() {
		return preferencias;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this); 
	}	
	
}

