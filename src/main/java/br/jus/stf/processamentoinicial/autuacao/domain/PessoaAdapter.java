package br.jus.stf.processamentoinicial.autuacao.domain;

import java.util.List;
import java.util.Set;

import br.jus.stf.shared.PessoaId;

/**
 * Interface para comunicação com o domínio genérico
 * 
 * @author Lucas Rodrigues
 */
public interface PessoaAdapter {

	/**
	 * Cadastra as pessoas e recupera os ids na ordem de envio.
	 * OBS: As pessoas cadastradas com o mesmo nome retornará o id já cadastrado anteriormente.
	 * 
	 * @param pessoas
	 * @return a lista de ids de pessoas
	 */
	public Set<PessoaId> cadastrarPessoas(List<String> pessoas);

	/**
	 * Recupera o nome de uma pessoa.
	 * 
	 * @param pessoaId
	 * @return
	 */
	public String consultarNome(PessoaId pessoaId);
	
}