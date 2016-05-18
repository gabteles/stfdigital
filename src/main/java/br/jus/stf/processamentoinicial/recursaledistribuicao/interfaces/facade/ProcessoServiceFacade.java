package br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.jus.stf.jurisprudencia.controletese.domain.model.AssuntoRepository;
import br.jus.stf.plataforma.shared.security.SecurityContextUtil;
import br.jus.stf.processamentoinicial.autuacao.domain.PessoaAdapter;
import br.jus.stf.processamentoinicial.recursaledistribuicao.application.ProcessoApplicationService;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.Origem;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ParametroDistribuicao;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ParteProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.PecaProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.Processo;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoRepository;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoSituacao;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.TipoDistribuicao;
import br.jus.stf.processamentoinicial.recursaledistribuicao.infra.PeticaoRestAdapter;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.OrigemProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.commands.PecaProcessual;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.dto.PecaProcessoDto;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.dto.PecaProcessoDtoAssembler;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.dto.ProcessoDto;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.dto.ProcessoDtoAssembler;
import br.jus.stf.processamentoinicial.recursaledistribuicao.interfaces.dto.ProcessoStatusDto;
import br.jus.stf.processamentoinicial.suporte.domain.model.ClasseRepository;
import br.jus.stf.processamentoinicial.suporte.domain.model.Peca;
import br.jus.stf.processamentoinicial.suporte.domain.model.ProcedenciaGeograficaRepository;
import br.jus.stf.processamentoinicial.suporte.domain.model.Sigilo;
import br.jus.stf.processamentoinicial.suporte.domain.model.TipoPeca;
import br.jus.stf.processamentoinicial.suporte.domain.model.TipoPolo;
import br.jus.stf.processamentoinicial.suporte.domain.model.TribunalJuizo;
import br.jus.stf.processamentoinicial.suporte.domain.model.TribunalJuizoRepository;
import br.jus.stf.processamentoinicial.suporte.domain.model.UnidadeFederacao;
import br.jus.stf.processamentoinicial.suporte.domain.model.Visibilidade;
import br.jus.stf.shared.AssuntoId;
import br.jus.stf.shared.ClasseId;
import br.jus.stf.shared.MinistroId;
import br.jus.stf.shared.PessoaId;
import br.jus.stf.shared.PeticaoId;
import br.jus.stf.shared.PreferenciaId;
import br.jus.stf.shared.ProcessoId;
import br.jus.stf.shared.TipoDocumentoId;

@Component
public class ProcessoServiceFacade {
	
	@Autowired
	private ProcessoApplicationService processoApplicationService;
	
	@Autowired
	private ProcessoDtoAssembler processoDtoAssembler;
	
	@Autowired
	private ProcessoRepository processoRepository;
	
	@Autowired
	private PeticaoRestAdapter peticaoRestAdapter;
	
	@Autowired
	private PecaProcessoDtoAssembler pecaProcessoDtoAssembler;
	
	@Autowired
	private ClasseRepository classeRepository;
	
	@Autowired
	private AssuntoRepository assuntoRepository;
	
	@Autowired
	private ProcedenciaGeograficaRepository procedenciaGeograficaRepository;
	
	@Autowired
	private TribunalJuizoRepository tribunalJuizoRepository;
	
	@Autowired
	private PessoaAdapter pessoaAdapter;

	/**
	 * Consulta um processo judicial, dado o seu identificador primário
	 * 
	 * @param id o código de identificação do processo
	 * @return dto o DTO com as informações de retorno do processo
	 */
	public ProcessoDto consultar(Long id){
		Processo processo = carregarProcesso(id);
		return processoDtoAssembler.toDto(processo);
	}

	/**
	 * Retorna as peças de um processo.
	 * 
	 * @param id Id do processo.
	 * @return Lista de peças;
	 */
	public List<PecaProcessoDto> consultarPecas(Long id){
		ProcessoId processoId = new ProcessoId(id);
		Processo processo = Optional.ofNullable(processoRepository.findOne(processoId)).orElseThrow(IllegalArgumentException::new);
		List<PecaProcessoDto> pecas = new LinkedList<PecaProcessoDto>(); 
		processo.pecas().forEach(p -> pecas.add(pecaProcessoDtoAssembler.toDto(p)));
		
		return pecas;
	}
	
	/**
	 * Distribui um processo para um ministro relator.
	 * 
	 * @param tipoDistribuicao Tipo de distribuição.
	 * @param peticao Id da petição.
	 * @param classeProcessual Classe do processo.
	 * @param justificativa Justificativa da distribuição.
	 * @param ministrosCandidatos Lista de possíveis relatores.
	 * @param ministrosImpedidos Lista de ministros impedidos.
	 * @param processosPreventos Lista de processos preventos.
	 */
	public ProcessoDto distribuir(String tipoDistribuicao, Long peticao, String justificativa,
			Set<Long> ministrosCandidatos, Set<Long> ministrosImpedidos, Set<Long> processosPreventos) {
		PeticaoId peticaoId = new PeticaoId(peticao);
		String usuarioCadastramento = SecurityContextUtil.getUser().getUsername();
		TipoDistribuicao tipo = TipoDistribuicao.valueOf(tipoDistribuicao);
		
		ParametroDistribuicao parametroDistribuicao = new ParametroDistribuicao(tipo, peticaoId, justificativa, usuarioCadastramento,
				this.carregarMinistros(ministrosCandidatos), this.carregarMinistros(ministrosImpedidos), this.carregarProcessos(processosPreventos));
		Processo processo = processoApplicationService.distribuir(parametroDistribuicao);
		
		return processoDtoAssembler.toDto(processo);
	}

	/**
	 * Retorna a lista de status atribuídos a um processo.
	 * 
	 * @return Lista de status de processos.
	 */
	public List<ProcessoStatusDto> consultarStatus() {
		
		List<ProcessoStatusDto> statusProcesso = new ArrayList<ProcessoStatusDto>();
		
		for (ProcessoSituacao p : ProcessoSituacao.values()) {
			statusProcesso.add(new ProcessoStatusDto(p.name(), p.descricao()));
		}
		
		return statusProcesso.stream().sorted((s1, s2) -> s1.getNome().compareTo(s2.getNome())).collect(Collectors.toList());
    }
	
	/**
	 * Cadastra um processo recursal
	 * 
	 * @return processoId
	 */
	public Long cadastrarRecursal(Long peticaoId) {
		PeticaoId peticao = new PeticaoId(peticaoId);
		Processo processo = processoApplicationService.cadastrarRecursal(peticao);
		return processo.id().toLong();
	}

	/**
	 * Consulta um processo associado a uma petição.
	 * 
	 * @param id
	 * @return
	 */
	public ProcessoDto consultarPelaPeticao(Long id) {
		return processoDtoAssembler.toDto(processoRepository.findByPeticao(new PeticaoId(id)));
	}
	
	/**
	 * Insere peças processuais.
	 * 
	 * @param processoId ID do processo.
	 * @param pecas Conjunto de peças processuais a serem inseridas.
	 */
	public void inserirPecas(Long processoId, List<PecaProcessual> pecas){
		Processo processo = processoRepository.findOne(new ProcessoId(processoId));
		processoApplicationService.inserirPecas(processo, pecas);
	}
	
	/**
	 * Exclui peças processuais.
	 * @param processoId Id do processo.
	 * @param pecas Lista de peças.
	 */
	public void excluirPecas(Long processoId, List<Long> pecas){
		Processo processo = processoRepository.findOne(new ProcessoId(processoId));
		List<Peca> pecasProcesso = new LinkedList<Peca>(); 
		pecas.forEach(pecaId -> pecasProcesso.add(processoRepository.findOnePeca(pecaId)));
		
		processoApplicationService.excluirPecas(processo, pecasProcesso);
	}
	
	/**
	 * Atribui a lista de peças com nova organização para um processo.
	 * 
	 * @param processoId
	 * @param pecasOrganizadas
	 * @param concluirTarefa
	 * @return
	 */
	public void organizarPecas(Long processoId, List<Long> pecasOrganizadas, boolean concluirTarefa) {
		Processo processo = carregarProcesso(processoId);
		
		processoApplicationService.organizarPecas(processo, pecasOrganizadas, concluirTarefa);
	}
	
	/**
	 * Verifica se um processo possui peças.
	 * 
	 * @param processoId
	 * @return
	 */
	public boolean temPecas(String processoId) {
		Processo processo = processoRepository.findByPeticao(new PeticaoId(Long.valueOf(processoId)));
		
		return !processo.pecas().isEmpty();
	}
	
	private Set<MinistroId> carregarMinistros(Set<Long> listaMinistros) {
		return Optional.ofNullable(listaMinistros)
				.map(lista -> lista.stream()
					.map(id -> new MinistroId(id))
					.collect(Collectors.toSet()))
				.orElse(Collections.emptySet());
	}
	
	private Set<Processo> carregarProcessos(Set<Long> listaProcessos) {
		return Optional.ofNullable(listaProcessos)
				.map(lista -> lista.stream()
						.map(id -> processoRepository.findOne(new ProcessoId(id)))
						.collect(Collectors.toSet()))
				.orElse(Collections.emptySet());
	}
	
	private Processo carregarProcesso(Long id) {
	    ProcessoId processoId = new ProcessoId(id);
		return Optional.ofNullable(processoRepository.findOne(processoId)).orElseThrow(IllegalArgumentException::new);
    }
	
	/**
	 * Divide uma peça processual.
	 * @param processoId Id do processo.
	 * @param pecas Lista de peças.
	 */
	public void dividirPeca(Long processoId, Long pecaOriginalId, List<PecaProcessual> novasPecas){
		PecaProcesso pecaOriginal = (PecaProcesso)processoRepository.findOnePeca(pecaOriginalId);
		Processo processo = processoRepository.findOne(new ProcessoId(processoId));
		List<Range<Integer>> intervalos = new LinkedList<Range<Integer>>();
		novasPecas.forEach(peca -> intervalos.add(Range.between(peca.getPaginaInicial(), peca.getPaginaFinal())));
				
		processoApplicationService.dividirPeca(processo, pecaOriginal, intervalos, novasPecas);
	}
	
	/**
	 * Uni peças processuais.
	 * @param processoId Id do processo.
	 * @param pecas Lista de peças.
	 */
	public void unirPecas(Long processoId, List<Long> pecasParaUniao){
		List<PecaProcesso> pecas = new LinkedList<PecaProcesso>();
		pecasParaUniao.forEach(p -> pecas.add((PecaProcesso)processoRepository.findOnePeca(p)));
		Processo processo = processoRepository.findOne(new ProcessoId(processoId));
			
		processoApplicationService.unirPecas(processo, pecas);
	}
	
	/**
	 * Permite a edição de uma peça.
	 * @param pecaId Id da peça.
	 * @param tipoPecaId Id do tipo da peça.
	 * @param descricao Descrição da peça.
	 * @param numeroOrdem Nº de ordem da peça.
	 * @param visibilidade Visibilidade da peça.
	 */
	public void editarPeca(Long processoId, Long pecaId, Long tipoPecaId, String descricao, Long numeroOrdem, String visibilidade){
		Processo processo = processoRepository.findOne(new ProcessoId(processoId));
		PecaProcesso pecaOriginal = (PecaProcesso)processoRepository.findOnePeca(pecaId);
		TipoPeca tipoPeca = processoRepository.findOneTipoPeca(new TipoDocumentoId(tipoPecaId));
		processoApplicationService.editarPeca(processo, pecaOriginal, tipoPeca, descricao, numeroOrdem, Visibilidade.valueOf(visibilidade));
	}
	
	/**
	 * Realiza a juntada de uma peça ao processo.
	 * @param processoId Id do processo.
	 * @param pecaId Id da peça.
	 */
	public void juntarPeca(Long processoId, Long pecaId){
		Processo processo = processoRepository.findOne(new ProcessoId(processoId));
		PecaProcesso peca = (PecaProcesso)processoRepository.findOnePeca(pecaId);
		
		processoApplicationService.juntarPeca(processo, peca);
	}
	
	/**
	 * Salva os dados do processo a ser enviado para o STF.
	 * 
	 * @param classe Id da classe processual.
	 * @param sigilo Sigilo do processo.
	 * @param numeroRecursos Nº de recursos do processo.
	 * @param preferencias Lista de preferências do processo.
	 * @param origens Origens do processo.
	 * @param idsAssuntos Lista de assuntos tratados no processo.
	 * @param partesPoloAtivo Lista de partes do polo ativo do processo.
	 * @param partesPoloPassivo Lista de partes do polo passivo do processo.
	 */
	public void enviarProcesso(String classe, String sigilo, Long numeroRecursos, List<Long> idsPreferencias, List<OrigemProcesso> origensProcesso, 
			List<String> idsAssuntos, List<String> partesPoloAtivo, List<String> partesPoloPassivo){
		ClasseId classeId = new ClasseId(classe);
		Set<PreferenciaId> preferencias = Optional.ofNullable(idsPreferencias).map(ids -> ids.stream().map(id -> new PreferenciaId(id)).collect(Collectors.toSet())).orElse(Collections.emptySet());
		Set<AssuntoId> assuntos = new HashSet<AssuntoId>(); 
		idsAssuntos.stream().map(id -> assuntos.add(new AssuntoId(id))).collect(Collectors.toSet());
		Sigilo sigiloProcesso = Sigilo.valueOf(sigilo);
		Set<Origem> origens = new HashSet<Origem>();
		List<ParteProcesso> poloAtivo = new LinkedList<ParteProcesso>();
		List<ParteProcesso> poloPassivo = new LinkedList<ParteProcesso>();
		
		if (origensProcesso != null){
			for(OrigemProcesso origemProcesso : origensProcesso){
				UnidadeFederacao uf = procedenciaGeograficaRepository.findOneUnidadeFederacao(origemProcesso.getUnidadeFederacaoId());
				TribunalJuizo tribunal = tribunalJuizoRepository.findOne(origemProcesso.getCodigoJuizoOrigem());
				origens.add(new Origem(uf, tribunal, origemProcesso.getNumeroProcesso(), origemProcesso.getIsPrincipal()));
			}
		}
		
		Set<PessoaId> pessoasPoloAtivo = pessoaAdapter.cadastrarPessoas(partesPoloAtivo);
		Set<PessoaId> pessoasPoloPassivo = pessoaAdapter.cadastrarPessoas(partesPoloPassivo);
		
		pessoasPoloAtivo.forEach(p1 -> poloAtivo.add(new ParteProcesso(p1, TipoPolo.POLO_ATIVO)));
		pessoasPoloPassivo.forEach(p2 -> poloPassivo.add(new ParteProcesso(p2, TipoPolo.POLO_PASSIVO)));
		
		processoApplicationService.enviarProcesso(classeId, sigiloProcesso, numeroRecursos, preferencias, origens, assuntos, poloAtivo, poloPassivo);
	}
}