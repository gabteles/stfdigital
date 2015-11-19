package br.jus.stf.plataforma.acessos.domain.model;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.Validate;

import br.jus.stf.shared.stereotype.ValueObject;

@Entity
@Table(name = "PAPEL", schema = "PLATAFORMA", uniqueConstraints = @UniqueConstraint(columnNames = {"NOM_PAPEL"}))
public class Papel implements ValueObject<Papel>, Principal {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "SEQ_PAPEL")
	@SequenceGenerator(name = "PAPELID", sequenceName = "PLATAFORMA.SEQ_PAPEL", allocationSize = 1)
	@GeneratedValue(generator = "PAPELID", strategy = GenerationType.SEQUENCE)
	private Long sequencial;
	
	@Column(name = "NOM_PAPEL", nullable = false)
	private String nome;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinTable(name = "PERMISSAO_PAPEL", schema = "PLATAFORMA",
		joinColumns = @JoinColumn(name = "SEQ_PAPEL", nullable = false),
		inverseJoinColumns = @JoinColumn(name = "SEQ_PERMISSAO", nullable = false))
	private Set<Permissao> permissoes;
	
	Papel() {
		
	}
	
	public Papel(final Long sequencial, final String nome) {
		Validate.notNull(sequencial, "papel.sequencial.required");
		Validate.notBlank(nome, "papel.nome.required");
		
		this.sequencial = sequencial;
		this.nome = nome;
	}
	
	public Long toLong() {
		return sequencial;
	}
	
	public String nome() {
		return nome;
	}

	@Override
	public Set<Permissao> permissoes() {
		return Collections.unmodifiableSet(permissoes);
	}

	@Override
	public void atribuirPermissoes(Set<Permissao> permissoes) {
		Validate.notEmpty(permissoes, "papel.permissoes.required");
		
		this.permissoes = permissoes;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		
		result = prime * result + ((sequencial == null) ? 0 : sequencial.hashCode());
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj){
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
	
		Papel other = (Papel) obj;
		return sameValueAs(other);
	}

	@Override
	public boolean sameValueAs(Papel other) {
		return other != null && sequencial.equals(other.sequencial);
	}

}