package br.jus.stf.autuacao.peticionamento.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import br.jus.stf.core.framework.domaindrivendesign.EntitySupport;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 29.02.2016
 */
@Entity
public class Envolvido extends EntitySupport<Envolvido, Long> {
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private Long id;
	
	@Column
	private String nome;
	
	public Envolvido() {
    	// Deve ser usado apenas pelo Hibernate, que sempre usa o construtor default antes de popular uma nova instância.
	}
	
	public Envolvido(String nome) {
		this.nome = nome;
	}
	
	@Override
	public Long identity() {
		return id;
	}
	
	public String nome() {
		return nome;
	}

}
