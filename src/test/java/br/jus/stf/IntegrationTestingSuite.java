package br.jus.stf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.jus.stf.plataforma.actions.AcoesIntegrationTests;
import br.jus.stf.plataforma.documentos.DocumentoIntegrationTests;
import br.jus.stf.plataforma.pesquisas.PesquisaIntegrationTests;
import br.jus.stf.processamentoinicial.autuacao.application.PeticaoApplicationEventIntegrationTests;
import br.jus.stf.processamentoinicial.autuacao.interfaces.AutuacaoOriginariosIntegrationTests;
import br.jus.stf.processamentoinicial.distribuicao.application.ProcessoApplicationEventIntegrationTests;
import br.jus.stf.processamentoinicial.distribuicao.interfaces.ConsultaProcessosIntegrationTests;

@RunWith(Suite.class)
@SuiteClasses({ 
	PeticaoApplicationEventIntegrationTests.class,
	ProcessoApplicationEventIntegrationTests.class,
	AutuacaoOriginariosIntegrationTests.class,
	ConsultaProcessosIntegrationTests.class,
	DocumentoIntegrationTests.class,
	PesquisaIntegrationTests.class, 
	AcoesIntegrationTests.class 
})
public class IntegrationTestingSuite {

}
