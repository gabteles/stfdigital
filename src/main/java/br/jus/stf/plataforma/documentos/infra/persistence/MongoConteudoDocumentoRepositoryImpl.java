package br.jus.stf.plataforma.documentos.infra.persistence;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

import br.jus.stf.plataforma.documentos.domain.model.ConteudoDocumento;
import br.jus.stf.plataforma.documentos.domain.model.DocumentoTemporario;
import br.jus.stf.plataforma.shared.settings.AndProfilesCondition;
import br.jus.stf.plataforma.shared.settings.Profiles;
import br.jus.stf.shared.DocumentoId;

/**
 * Implementação do repositório de conteúdo com armazenamento no Mongo.
 * 
 * @author Tomas.Godoi
 *
 */
@Repository
//@Profile(Profiles.DOCUMENTO_MONGO)
@Profile({"!" + Profiles.DOCUMENTO_FS, "!" + Profiles.DOCUMENTO_ORACLE }) // Setando o profile do mongo se os outros não forem ativados.
@Conditional(AndProfilesCondition.class)
public class MongoConteudoDocumentoRepositoryImpl implements ConteudoDocumentoRepository {

	@Autowired
	private GridFsOperations gridOperations;
	
	@Override
	public ConteudoDocumento downloadConteudo(String numeroConteudo) {
		try {
			GridFSDBFile gridFile = gridOperations.findOne(
					new Query().addCriteria(Criteria.where("_id").is(new ObjectId(numeroConteudo))));
			byte[] bytes = IOUtils.toByteArray(gridFile.getInputStream());
			return new ConteudoDocumento(bytes, gridFile.getLength());
		} catch (Exception t) {
			throw new RuntimeException("Não foi possível carregar o stream do arquivo ", t);
		}
	}

	@Override
	public void deleteConteudo(String numeroConteudo) {
		gridOperations.delete(new Query()
				.addCriteria(Criteria.where("_id").is(new ObjectId(numeroConteudo))));
	}

	@Override
	public String save(DocumentoId documentoId, DocumentoTemporario documentoTemporario) {
		InputStream stream = null;
		try {
			stream = documentoTemporario.stream();
			DBObject metaData = new BasicDBObject();
			
			metaData.put("seq_documento", documentoId.toLong());
			metaData.put("nom_arquivo", documentoTemporario.toString());
			metaData.put("num_tamanho_bytes", documentoTemporario.tamanho());
			
			return gridOperations.store(stream, documentoTemporario.toString(), documentoTemporario.contentType(), metaData).getId().toString();
		} finally {
			IOUtils.closeQuietly(stream);
		}
	}

}
