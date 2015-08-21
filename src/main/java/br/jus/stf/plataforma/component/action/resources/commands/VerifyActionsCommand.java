package br.jus.stf.plataforma.component.action.resources.commands;

import java.util.Collection;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author Lucas.Rodrigues
 *
 */
public class VerifyActionsCommand {

	@NotEmpty
	@ApiModelProperty(value = "Identificadores das ações.", required=true)
	private Collection<String> ids;
	
	@ApiModelProperty(value = "Recursos que sofrerão ação.", required=true)
	private ArrayNode resources;

	/**
	 * @return the ids
	 */
	public Collection<String> getIds() {
		return ids;
	}
	
	/**
	 * @param ids the ids to set
	 */
	public void setIds(Collection<String> ids) {
		this.ids = ids;
	}

	/**
	 * @return the resources
	 */
	public ArrayNode getResources() {
		return resources;
	}

	/**
	 * @param resources the resources to set
	 */
	public void setResources(ArrayNode resources) {
		this.resources = resources;
	}
	
}
