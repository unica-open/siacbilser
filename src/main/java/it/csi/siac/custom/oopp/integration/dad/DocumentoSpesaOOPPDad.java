/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.custom.oopp.integration.dad;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.custom.oopp.integration.dad.mapper.SiacTDocDocumentoSpesaMapper;
import it.csi.siac.siacbilser.integration.dad.ExtendedBaseDadImpl;
import it.csi.siac.siacbilser.integration.dao.SiacTDocRepository;
import it.csi.siac.siacintegser.model.custom.oopp.DocumentoSpesa;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class DocumentoSpesaOOPPDad extends ExtendedBaseDadImpl {

	@Autowired private SiacTDocDocumentoSpesaMapper siacTDocDocumentoSpesaMapper;
	@Autowired private SiacTDocRepository siacTDocRepository;

	
	
	public List<DocumentoSpesa> ricercaDocumentiSpesa(
			Integer annoBilancio, Integer annoDocumento, String cig, Integer annoImpegno, BigDecimal numeroImpegno, Integer annoMandato, BigDecimal numeroMandato) {
		
	/*	
		testDao.testQuery("FROM SiacTDoc td "
		+ " WHERE 1=1  and td.siacDDocTipo.siacDDocFamTipo.docFamTipoCode='S' "
		+ " AND td.siacTEnteProprietario.enteProprietarioId=:idEnte "
		+ " AND (CAST(:anno AS integer) IS NULL OR td.docAnno=:anno) "
		+ " AND (CAST(:cig AS string) IS NULL OR EXISTS ( "
		+ "		SELECT 1 FROM td.siacTSubdocs ts "
		+ "		JOIN ts.siacRSubdocMovgestTs rsmt "
		+ "		JOIN rsmt.siacTMovgestT.siacRMovgestTsAttrs rmta "
		+ "     WHERE rmta.siacTAttr.attrCode='cig' "
		+ "		AND rmta.testo=:cig "
		+ "		AND rsmt.siacTMovgestT.siacTMovgest.siacDMovgestTipo.movgestTipoCode='I' "
//		+ " 	AND ts.dataCancellazione IS NULL "
//		+ " 	AND ts.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " 	AND (ts.dataFineValidita IS NULL OR ts.dataFineValidita > CURRENT_TIMESTAMP) "
//		+ " 	AND rsmt.dataCancellazione IS NULL "
//		+ " 	AND rsmt.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " 	AND (rsmt.dataFineValidita IS NULL OR rsmt.dataFineValidita > CURRENT_TIMESTAMP) "
//		+ " 	AND rsmt.siacTMovgestT.dataCancellazione IS NULL "
//		+ " 	AND rsmt.siacTMovgestT.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " 	AND (rsmt.siacTMovgestT.dataFineValidita IS NULL OR rsmt.siacTMovgestT.dataFineValidita > CURRENT_TIMESTAMP) "
//		+ " 	AND rmta.dataCancellazione IS NULL "
//		+ " 	AND rmta.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " 	AND (rmta.dataFineValidita IS NULL OR rmta.dataFineValidita > CURRENT_TIMESTAMP) "
		+ "	)) "
		+ " AND (CAST(:annoImpegno AS string) IS NULL AND CAST(:numeroImpegno AS string) IS NULL OR EXISTS ( "
		+ "		SELECT 1 FROM td.siacTSubdocs ts "
		+ "		JOIN ts.siacRSubdocMovgestTs rsmt "
		+ "		JOIN rsmt.siacTMovgestT.siacTMovgest tm "
		+ "     WHERE tm.movgestAnno=:annoImpegno AND tm.movgestNumero=:numeroImpegno "
//		+ " 	AND rsmt.dataCancellazione IS NULL "
//		+ " 	AND rsmt.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " 	AND (rsmt.dataFineValidita IS NULL OR rsmt.dataFineValidita > CURRENT_TIMESTAMP) "
//		+ " 	AND tm.dataCancellazione IS NULL "
//		+ " 	AND tm.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " 	AND (tm.dataFineValidita IS NULL OR tm.dataFineValidita > CURRENT_TIMESTAMP) "
		+ " ))"
//		+ " AND td.dataCancellazione IS NULL "
//		+ " AND td.dataInizioValidita < CURRENT_TIMESTAMP "
//		+ " AND (td.dataFineValidita IS NULL OR td.dataFineValidita > CURRENT_TIMESTAMP) " 

				,"idEnte", 3, 
				"anno", 2022, 
				"cig", null
				,"annoImpegno", 2022
				,"numeroImpegno", new BigDecimal(3085)
				);
		
		*/
		
		return siacTDocDocumentoSpesaMapper.map(siacTDocRepository.findSiacTDocSpesa(
				ente.getUid(), annoBilancio, annoDocumento, cig, annoImpegno, numeroImpegno, annoMandato, numeroMandato));
	}
}
