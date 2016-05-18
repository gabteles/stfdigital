package br.jus.stf.processamentoinicial.autuacao.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.plataforma.workflow.interfaces.WorkflowRestResource;
import br.jus.stf.plataforma.workflow.interfaces.commands.IniciarProcessoCommand;
import br.jus.stf.processamentoinicial.autuacao.domain.WorkflowAdapter;
import br.jus.stf.processamentoinicial.autuacao.domain.model.PeticaoEletronica;
import br.jus.stf.processamentoinicial.autuacao.domain.model.PeticaoFisica;
import br.jus.stf.processamentoinicial.autuacao.domain.model.PeticaoStatus;
import br.jus.stf.processamentoinicial.suporte.domain.model.TipoProcesso;
import br.jus.stf.shared.ProcessoWorkflow;
import br.jus.stf.shared.ProcessoWorkflowId;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 26.06.2015
 */
@Component
public class WorkflowRestAdapter implements WorkflowAdapter {
	
	@Autowired
	private WorkflowRestResource workflowRestResource;
	
	@Override
	public ProcessoWorkflow iniciarWorkflow(PeticaoEletronica peticaoEletronica) {
		IniciarProcessoCommand command = new IniciarProcessoCommand();
		command.setMensagem("autuacao");
		command.setStatus(PeticaoStatus.A_AUTUAR.toString());
		command.setInformacao(peticaoEletronica.id().toString());
		command.setTipoInformacao(peticaoEletronica.getClass().getSimpleName());
		command.setDescricao(peticaoEletronica.identificacao());
		
		Long id = workflowRestResource.iniciar(command);
		return new ProcessoWorkflow(new ProcessoWorkflowId(id), PeticaoStatus.A_AUTUAR.name());
	}

	@Override
	public ProcessoWorkflow iniciarWorkflow(PeticaoFisica peticaoFisica) {
		IniciarProcessoCommand command = new IniciarProcessoCommand();
		if (peticaoFisica.tipoProcesso() == TipoProcesso.ORIGINARIO) {
			command.setMensagem("remessaOriginario");
		} else {
			command.setMensagem("remessaRecursal");
		}
		command.setStatus(PeticaoStatus.A_PREAUTUAR.toString());
		command.setInformacao(peticaoFisica.id().toString());
		command.setTipoInformacao(peticaoFisica.getClass().getSimpleName());
		command.setDescricao(peticaoFisica.identificacao());
		
		Long id = workflowRestResource.iniciarPorMensagem(command);
		return new ProcessoWorkflow(new ProcessoWorkflowId(id), PeticaoStatus.A_PREAUTUAR.name());
	}
	
}