package br.jus.stf.processamentoinicial.autuacao.interfaces.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.plataforma.shared.actions.annotation.ActionController;
import br.jus.stf.plataforma.shared.actions.annotation.ActionMapping;
import br.jus.stf.plataforma.shared.actions.support.ResourcesMode;
import br.jus.stf.processamentoinicial.autuacao.domain.model.PeticaoStatus;
import br.jus.stf.processamentoinicial.autuacao.domain.model.TipoDevolucao;
import br.jus.stf.processamentoinicial.autuacao.interfaces.commands.AssinarDevolucaoPeticaoCommand;
import br.jus.stf.processamentoinicial.autuacao.interfaces.commands.AutuarPeticaoCommand;
import br.jus.stf.processamentoinicial.autuacao.interfaces.commands.DevolverPeticaoCommand;
import br.jus.stf.processamentoinicial.autuacao.interfaces.commands.PreautuarPeticaoFisicaCommand;
import br.jus.stf.processamentoinicial.autuacao.interfaces.facade.PeticaoServiceFacade;

@ActionController(groups = "peticao")
public class PeticaoActionsResource {
	
	@Autowired
	private PeticaoServiceFacade peticaoServiceFacade;
	
	@ActionMapping(id = "preautuar", name = "Preautuar Petição Física", resourcesMode = ResourcesMode.One)
	public void preautuar(PreautuarPeticaoFisicaCommand command) {
		peticaoServiceFacade.preautuar(command.getPeticaoId(), command.getClasseId(), command.isValida(), command.getMotivo(), command.getPreferencias());	
	}
	
	@ActionMapping(id = "preautuar-recursal", name = "Preautuar Petição Física Recursal", resourcesMode = ResourcesMode.One)
	public void preautuarRecursal(PreautuarPeticaoFisicaCommand command) {
		peticaoServiceFacade.preautuar(command.getPeticaoId(), command.getClasseId(), command.isValida(), command.getMotivo(), command.getPreferencias());
	}
	
	@ActionMapping(id = "autuar", name = "Autuar Petição", resourcesMode = ResourcesMode.One)
	public void autuar(AutuarPeticaoCommand command) {
		peticaoServiceFacade.autuar(command.getPeticaoId(), command.getClasseId(), command.isValida(), command.getMotivo(), command.getPartesPoloAtivo(), command.getPartesPoloPassivo()); 
	}
	
	@ActionMapping(id = "devolver-peticao", name = "Devolver Petição", resourcesMode = ResourcesMode.One)
	public void devolver(DevolverPeticaoCommand command) {
		TipoDevolucao tipoDevolucao = TipoDevolucao.valueOf(command.getTipoDevolucao());
		peticaoServiceFacade.devolver(command.getPeticaoId(), command.getDocumento(), tipoDevolucao, command.getNumeroOficio()); 
	}
	
	@ActionMapping(id = "assinar-devolucao-peticao", name = "Assinar Documento de Devolução", resourcesMode = ResourcesMode.OneOrMany)
	@FiltrarPeticaoPorStatus(PeticaoStatus.ASSINAR_DEVOLUCAO)
	public void assinarDevolucao(List<AssinarDevolucaoPeticaoCommand> commands) {
		commands.forEach(c -> peticaoServiceFacade.assinarDevolucao(c.getPeticaoId(), c.getDocumentoId()));
	}
	
}
