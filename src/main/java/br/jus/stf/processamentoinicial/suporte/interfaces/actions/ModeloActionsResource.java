package br.jus.stf.processamentoinicial.suporte.interfaces.actions;

import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.plataforma.shared.actions.annotation.ActionController;
import br.jus.stf.plataforma.shared.actions.annotation.ActionMapping;
import br.jus.stf.plataforma.shared.actions.support.ResourcesMode;
import br.jus.stf.processamentoinicial.suporte.application.ModeloApplicationService;
import br.jus.stf.processamentoinicial.suporte.domain.model.Modelo;
import br.jus.stf.processamentoinicial.suporte.interfaces.commands.CriarModeloCommand;
import br.jus.stf.shared.TipoDocumentoId;

/**
 * Ações de Modelo.
 * 
 * @author Tomas.Godoi
 *
 */
@ActionController(groups = "menu")
public class ModeloActionsResource {

	@Autowired
	private ModeloApplicationService modeloApplicationService;

	@ActionMapping(id = "criar-modelo", name = "Modelo", resourcesMode = ResourcesMode.None)
	public Long criarModelo(CriarModeloCommand command) {
		Modelo modelo = modeloApplicationService.criarModelo(new TipoDocumentoId(command.getTipoModelo()),
		        command.getNome());
		return modelo.id().toLong();
	}

}