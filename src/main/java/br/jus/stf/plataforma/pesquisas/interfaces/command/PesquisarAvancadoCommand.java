package br.jus.stf.plataforma.pesquisas.interfaces.command;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


/**
 * @author Lucas.Rodrigues
 *
 */
@ApiModel("Comando para executar uma consulta")
public class PesquisarAvancadoCommand {
	
	@ApiModelProperty("Consulta a ser executada")
	@NotBlank
	private String consulta;
	
	@ApiModelProperty("Página do resultado")
	@NotNull
	private Integer page = 0;
	
	@ApiModelProperty("Tamanho máximo do resultado")
	@NotNull
	private Integer size = 15;
	
	@ApiModelProperty("Tamanho máximo do resultado")
	@NotEmpty
	private String[] indices;
	
	public String getConsulta() {
		return consulta;
	}

	public Integer getPage() {
	    return page;
    }

	public Integer getSize() {
	    return size;
    }

	public String[] getIndices() {
	    return indices;
    }
	
}
