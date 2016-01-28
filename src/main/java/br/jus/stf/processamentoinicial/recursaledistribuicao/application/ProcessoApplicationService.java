package br.jus.stf.processamentoinicial.recursaledistribuicao.application;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import br.jus.stf.processamentoinicial.autuacao.domain.PessoaAdapter;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.TarefaAdapter;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.Distribuicao;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.MotivoInaptidao;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.MotivoInaptidaoProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ParametroDistribuicao;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ParteProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.Processo;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoFactory;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoRecursal;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoRepository;
import br.jus.stf.processamentoinicial.suporte.domain.model.Classificacao;
import br.jus.stf.processamentoinicial.suporte.domain.model.TipoPolo;
import br.jus.stf.shared.AssuntoId;
import br.jus.stf.shared.PessoaId;
import br.jus.stf.shared.PeticaoId;
import br.jus.stf.shared.ProcessoId;
import br.jus.stf.shared.TeseId;

/**
 * @author Lucas Rodrigues
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 25.09.2015
 */
@Component
public class ProcessoApplicationService {

	@Autowired
	@Qualifier("processoTarefaRestAdapter")
	private TarefaAdapter tarefaAdapter;
	
	@Autowired
	private ProcessoRepository processoRepository;

	@Autowired
	private ProcessoApplicationEvent processoApplicationEvent;
	
	@Autowired
	private PessoaAdapter pessoaAdapter;
	
	/**
	 * Cadastra um processo recursal.
	 * 
	 */
	public ProcessoRecursal cadastrarRecursal(PeticaoId peticaoId) {
		Processo processo = ProcessoFactory.criar(peticaoId);
		if(processo  instanceof ProcessoRecursal) {
			processoRepository.save(processo);
			//TODO Lançar evento de cadastro de recursal
			return (ProcessoRecursal) processo;
		}
		throw new IllegalArgumentException("Petição inválida para criação de processo recursal!");
	}
	
	/**
	 * Realiza a autuação de processo recursal.
	 * 
	 * @param idProcesso ID do processo recursal a ser autuado.
	 * @param assuntos Lista de assuntos do processo.
	 * @param poloAtivo Lista de partes do polo ativo.
	 * @param poloPassivo Lista de partes do polo passivo.
	 */
	public void autuar(Long idProcesso, List<String> assuntos, List<String> poloAtivo, List<String> poloPassivo) {
		
		ProcessoId processoId = new ProcessoId(idProcesso);
		Set<AssuntoId> assuntosProcesso = assuntos.stream().map(id -> new AssuntoId(id)).collect(Collectors.toSet());
		Set<ParteProcesso> partesPoloAtivo = new HashSet<ParteProcesso>();
		Set<ParteProcesso> partesPoloPassivo = new HashSet<ParteProcesso>();
		
		adicionarPartes(partesPoloAtivo, poloAtivo, TipoPolo.POLO_ATIVO);
		adicionarPartes(partesPoloPassivo, poloPassivo, TipoPolo.POLO_PASSIVO);
		
		ProcessoRecursal processo = (ProcessoRecursal) processoRepository.findOne(processoId);
		processo.autuar(assuntosProcesso, partesPoloAtivo, partesPoloPassivo);
		processoRepository.save(processo);
		tarefaAdapter.completarAutuacao(processo);
	}
	
	/**
	 * Realiza análise de pressupostos formais para processo recursal.
	 * 
	 * @param processoId Processo recursal a ser autuado
	 * @param classificacao Classificação (APTO/INAPTO)
	 * @param motivos Lista de motivos de inaptidão do processo
	 * @param observacao Observação da análise
	 */
	public void analisarPressupostosFormais(Long IdProcesso, String classificacao, Map<Long, String> motivos, String observacao, boolean revisao) {
		ProcessoId processoId = new ProcessoId(IdProcesso);
		Classificacao classif = Classificacao.valueOf(classificacao.toUpperCase());
		Set<MotivoInaptidaoProcesso> motivosInaptidao = new LinkedHashSet<MotivoInaptidaoProcesso>(); 
		motivos.forEach((k, v) -> motivosInaptidao.add(new MotivoInaptidaoProcesso(recuperarMotivoInaptidao(k), v)));
		
		ProcessoRecursal processo = (ProcessoRecursal) processoRepository.findOne(processoId);
		processo.analisarPressupostosFormais(classif, observacao, motivosInaptidao);
		processoRepository.save(processo);
		if (revisao) {
			tarefaAdapter.completarRevisaoProcessoInapto(processo, classif);
		} else {
			tarefaAdapter.completarAnalisePressupostosFormais(processo, classif);
		}
	}
	
	/**
	 * Realiza análise de pressupostos formais para processo recursal.
	 * 
	 * @param processoId Processo recursal a ser autuado
	 * @param assuntos Assuntos do processo
	 * @param teses Teses de repercussão geral
	 * @param observacao Observação da análise
	 */
	public void analisarRepercussaoGeral(ProcessoId processoId, Set<AssuntoId> assuntos, Set<TeseId> teses, boolean revisao) {
		// TODO: Verificar possíveis chamadas a eventos e ao workflow.
		ProcessoRecursal processo = (ProcessoRecursal) processoRepository.findOne(processoId);
		processo.analisarRepercussaoGeral(assuntos, teses);
		processoRepository.save(processo);
		if (revisao) {
			tarefaAdapter.completarRevisaoRepercussaoGeral(processo);
		} else {
			tarefaAdapter.completarAnaliseRepercussaoGeral(processo, !teses.isEmpty());
		}
	}
	
	/**
	 * Distribui um processo para um Ministro Relator.
	 * 
	 * @param parametroDistribuicao Parametrização para a distribuição
	 * @return processo
	 */
	public Processo distribuir(ParametroDistribuicao parametroDistribuicao) {
		Distribuicao distribuicao = Distribuicao.criar(parametroDistribuicao);
		Processo processo = distribuicao.executar();
		processoRepository.save(processo);
		tarefaAdapter.completarDistribuicao(processo);
		processoApplicationEvent.processoDistribuido(processo);
		
		return processo;
	}

	private MotivoInaptidao recuperarMotivoInaptidao(Long id){
		return processoRepository.findOneMotivoInaptidao(id);
	}
	
	/**
	 * Cria as partes do processo recursal.
	 * 
	 * @param partes Conjunto de partes.
	 * @param polo Lista de partes.
	 * @param tipo Tipo de polo.
	 * 
	 */
	private void adicionarPartes(Set<ParteProcesso> partes, List<String> polo, TipoPolo tipo) {
		Set<PessoaId> pessoas = pessoaAdapter.cadastrarPessoas(polo);
		pessoas.forEach(pessoa -> partes.add(new ParteProcesso(pessoa, tipo)));
	}
}
