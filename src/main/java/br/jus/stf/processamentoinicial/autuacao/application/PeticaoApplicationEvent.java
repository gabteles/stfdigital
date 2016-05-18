package br.jus.stf.processamentoinicial.autuacao.application;

import br.jus.stf.processamentoinicial.autuacao.domain.model.Peticao;

/**
 * Interface que define os eventos publicados pela aplicação
 * 
 * @author Lucas Rodrigues
 */
public interface PeticaoApplicationEvent {
	
	/**
	 * Notifica interessados sobre o recebimento de uma petição.
	 * 
	 * @param peticao Dados da petição.
	 */
	public void peticaoRecebida(Peticao peticao);
	
	/**
	 * Notifica interessados sobre a remessa inválida de uma petição.
	 * 
	 * @param peticao Dados da petição.
	 */
	public void remessaInvalida(Peticao peticao);
	
	/**
	 * Notifica interessados sobre a rejeição de uma petição.
	 * 
	 * @param peticao Dados da petição.
	 */
	public void peticaoRejeitada(Peticao peticao);
	
	/**
	 * Notifica interessados sobre a autuação de uma petição.
	 * 
	 * @param peticao Dados da petição.
	 */
	public void peticaoAutuada(Peticao peticao);

	/**
	 * Notifica interessados sobre a preautuação de uma petição.
	 * 
	 * @param peticao Dados da petição.
	 */
	public void peticaoPreautuada(Peticao peticao);

	/**
	 * Notifica interessados sobre a preparação da petição para devolução.
	 * 
	 * @param peticao
	 */
	public void peticaoPreparadaParaDevolucao(Peticao peticao);

	/**
	 * Notifica interessados sobre a assinatura do documento de devolução da petição.
	 * 
	 * @param peticao
	 */
	public void peticaoDevolucaoAssinada(Peticao peticao);
	
}