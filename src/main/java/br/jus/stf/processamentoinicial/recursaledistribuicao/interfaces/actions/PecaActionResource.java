package br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.actions;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.jus.stf.plataforma.shared.actions.annotation.ActionController;
import br.jus.stf.plataforma.shared.actions.annotation.ActionMapping;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.DividirPecaCommand;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.EditarPecaCommand;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.ExcluirPecasCommand;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.JuntarPecaCommand;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.OrganizarPecasCommand;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.UnirPecasCommand;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.facade.ProcessoServiceFacade;

/**
 * Controle de ações de peças processuais.
 * 
 * @author Anderson.Araujo
 * @since 26.02.2016
 *
 */
@ActionController(groups = "peca")
public class PecaActionResource {
	
	@Autowired
	private ProcessoServiceFacade processoServiceFacade;
	
	@ActionMapping(id = "excluir-pecas", name = "Excluir Peças Processuais")
	public void excluirPecas(ExcluirPecasCommand command) {
		processoServiceFacade.excluirPecas(command.getProcessoId(), command.getPecas());
	}
	
	@ActionMapping(id = "organizar-pecas", name = "Organizar Peças")
	public void organizarPecas(OrganizarPecasCommand command) {
		processoServiceFacade.organizarPecas(command.getProcessoId(), command.getPecasOrganizadas(), command.isConcluirTarefa());
	}
	
	@ActionMapping(id = "dividir-peca", name = "Dividir Peça Processual")
	public void dividirPeca(DividirPecaCommand command) {
		processoServiceFacade.dividirPeca(command.getProcessoId(), command.getPecaId(), command.getPecas());
	}
	
	@ActionMapping(id = "unir-pecas", name = "Unir Peças Processuais")
	public void unirPecas(List<UnirPecasCommand> command) {
		processoServiceFacade.unirPecas(command.get(0).getProcessoId(), command.get(0).getPecas());
	}
	
	@ActionMapping(id = "editar-peca", name = "Editar Peça Processual")
	public void editarPeca(EditarPecaCommand command) {
		processoServiceFacade.editarPeca(command.getPecaId(), command.getTipoPecaId(), command.getDescricao(), 
				command.getNumeroOrdem(), command.getVisibilidade());
	}
	
	@ActionMapping(id = "juntar-peca", name = "Juntar Peça Processual")
	public void juntarPeca(List<JuntarPecaCommand> command) {
		processoServiceFacade.juntarPeca(command.get(0).getProcessoId(), command.get(0).getPecaId());
	}
}
