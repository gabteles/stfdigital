package br.jus.stf.processamentoinicial.autuacao.domain.model;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import br.jus.stf.processamentoinicial.suporte.domain.model.TipoPeca;
import br.jus.stf.shared.PeticaoId;

/**
 * @author Rafael.Alencar
 * @version 1.0
 * @created 14-ago-2015 18:33:25
 */
public interface PeticaoRepository {

	/**
	 * 
	 * @param id
	 * @param clazz tipo de retorno
	 * @return peticao
	 */
	public <T extends Peticao> T findOne(PeticaoId id);

	/**
	 * @param specification
	 * @return lista de peticoes
	 */
	public List<Peticao> findAll(Specification<Peticao> specification);

	/**
	 * 
	 * @param peticao
	 * @return o id da peticao
	 */
	public <T extends Peticao> T save(Peticao peticao);
	
	/**
	 * Salva e realiza o flush da petição.
	 * 
	 * @param peticao
	 * @return o id da peticao
	 */
	public <T extends Peticao> T saveAndFlush(Peticao peticao);
	
	/**
	 * Gera o próximo id da petição
	 * 
	 * @return o sequencial da petição
	 */
	public PeticaoId nextId();
	
	/**
	 * Recupera o próximo número de petição de acordo com o ano
	 * 
	 * @return o número da petição
	 */
	public Long nextNumero();
	
	/**
	 * @para id
	 * @return o tipo de peça
	 */
	public TipoPeca findOneTipoPeca(Long id);
	
	/**
	 * @return a lista de tipos de peças
	 */
	public List<TipoPeca> findAllTipoPeca();
	
	/**
	 * @para id
	 * @return o órgao
	 */
	public Orgao findOneOrgao(Long id);
	
	/**
	 * @return a lista de órgãos visíveis pela pessoa do associado
	 */
	public List<Orgao> findOrgaoRepresentados(boolean verificarPerfil);
	
	/**
	 * @return a lista de órgãos
	 */
	public List<Orgao> findAllOrgao();
	
	/**
	 * Verifica se as petições estão no mesmo status.
	 * 
	 * @param ids
	 * @param status
	 * @return
	 */
	public boolean estaoNoMesmoStatus(List<PeticaoId> ids, PeticaoStatus status);

	/**
	 * Atualiza uma petição com os dados mais atuais
	 * 
	 * @param peticao
	 */
	public void refresh(Peticao peticao);
	
}
