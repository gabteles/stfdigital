package br.jus.stf.processamentoinicial.distribuicao.domain.model;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ParteProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.PecaProcesso;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.Processo;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoOriginario;
import br.jus.stf.processamentoinicial.recursaledistribuicao.domain.model.ProcessoRepository;
import br.jus.stf.processamentoinicial.suporte.domain.model.Situacao;
import br.jus.stf.processamentoinicial.suporte.domain.model.TipoPeca;
import br.jus.stf.processamentoinicial.suporte.domain.model.TipoPolo;
import br.jus.stf.processamentoinicial.suporte.domain.model.Visibilidade;
import br.jus.stf.shared.ClasseId;
import br.jus.stf.shared.DocumentoId;
import br.jus.stf.shared.PessoaId;
import br.jus.stf.shared.PeticaoId;
import br.jus.stf.shared.ProcessoId;
import br.jus.stf.shared.TipoDocumentoId;

public class ProcessoRepositoryUnitTests {
	
	@Mock
	private ProcessoRepository processoRepository;
	
	private Set<ParteProcesso> partes;
	private Set<PecaProcesso> pecas;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		partes = new HashSet<ParteProcesso>(0);
		partes.add(new ParteProcesso(new PessoaId(1L), TipoPolo.POLO_ATIVO));
		partes.add(new ParteProcesso(new PessoaId(2L), TipoPolo.POLO_PASSIVO));

		pecas = new LinkedHashSet<PecaProcesso>(0);
		pecas.add(new PecaProcesso(new DocumentoId(1L), new TipoPeca(new TipoDocumentoId(1L), "Petição inicial"), "Petição inicial", Visibilidade.PUBLICO, Situacao.JUNTADA));
		
		Processo processo = processo();
		List<Processo> processos = new ArrayList<Processo>();
		
		processos.add(processo);
		
		when(processoRepository.save(processo)).thenReturn(processo);
		when(processoRepository.findOne(new ProcessoId(1L))).thenReturn(processo);
		when(processoRepository.findAll(null)).thenReturn(processos);
		when(processoRepository.nextId()).thenReturn(new ProcessoId(2L));
		when(processoRepository.nextNumero(new ClasseId("HC"))).thenReturn(3L);
		when(processoRepository.findByPessoaInPartes(new PessoaId(1L))).thenReturn(processos);
	}

	private Processo processo() {
		return new ProcessoOriginario(new ProcessoId(1L), new ClasseId("HD"), 1L, new PeticaoId(1L), partes, pecas, null);
	}
	
	@Test
	public void salvarProcesso() {
		Processo processo = processo();
		
		assertEquals(processo(), processoRepository.save(processo));
		verify(processoRepository, times(1)).save(processo);
	}
	
	@Test
	public void encontraProcesso() {
		assertEquals(processo(), processoRepository.findOne(new ProcessoId(1L)));
		verify(processoRepository, times(1)).findOne(new ProcessoId(1L));
	}
	
	@Test
	public void encontraProcessos() {
		Processo processo = processo();
		List<Processo> processos = new ArrayList<Processo>();
		
		processos.add(processo);
		assertEquals(processos, processoRepository.findAll(null));
		verify(processoRepository, times(1)).findAll(null);
	}
	
	@Test
	public void proximoIdProcesso() {
		assertEquals(new ProcessoId(2L), processoRepository.nextId());
		verify(processoRepository, times(1)).nextId();
	}
	
	@Test
	public void proximoNumeroClasseProcesso() {
		assertEquals(new Long(3), processoRepository.nextNumero(new ClasseId("HC")));
		verify(processoRepository, times(1)).nextNumero(new ClasseId("HC"));
	}
	
	@Test
	public void encontrarProcessosDaParte() {
		Processo processo = processo();
		List<Processo> processos = new ArrayList<Processo>();
		
		processos.add(processo);
		assertEquals(processos, processoRepository.findByPessoaInPartes(new PessoaId(1L)));
		verify(processoRepository, times(1)).findByPessoaInPartes(new PessoaId(1L));
	}
	
}
