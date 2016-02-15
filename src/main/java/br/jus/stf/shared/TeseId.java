package br.jus.stf.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import br.jus.stf.shared.stereotype.ValueObject;

/**
 * @author Rafael.Alencar
 * @version 1.0
 * @created 08-jan-2016
 */
@Embeddable
public class TeseId implements ValueObject<TeseId>{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "SEQ_TESE", nullable = false)
	private Long sequencial;

	TeseId() {

	}

	public TeseId(final Long sequencial){
		Validate.notNull(sequencial, "teseId.sequencia.required");
		
		this.sequencial = sequencial;
	}

	public Long toLong(){
		return sequencial;
	}
	
	@Override
	public String toString(){
		return sequencial.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(sequencial).toHashCode();
	}
	
	@Override
	public boolean equals(final Object o){
		
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		TeseId other = (TeseId) o;
		
		return sameValueAs(other);
	}

	@Override
	public boolean sameValueAs(final TeseId other){
		return other != null && sequencial.equals(other.sequencial);
	}
	
}