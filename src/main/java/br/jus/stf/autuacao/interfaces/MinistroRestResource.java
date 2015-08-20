package br.jus.stf.autuacao.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.jus.stf.autuacao.domain.MinistroRepository;
import br.jus.stf.autuacao.domain.entity.Ministro;

import com.wordnik.swagger.annotations.ApiOperation;

/**
 * @author Rodrigo Barreiros
 * 
 * @since 1.0.0
 * @since 20.07.2015
 */
@RestController
public class MinistroRestResource {

	@Autowired
	private MinistroRepository ministroRepository;

    @ApiOperation(value = "Retorna a lista com todos o Ministros ativos")
	@RequestMapping(value = "/api/ministros", method = RequestMethod.GET)
	public List<Ministro> listar() {
		return ministroRepository.listar();
	}

}
