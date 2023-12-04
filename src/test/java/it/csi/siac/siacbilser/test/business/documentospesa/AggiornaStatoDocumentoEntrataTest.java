/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataService;
import it.csi.siac.siacbilser.business.service.documentoentrata.AggiornaStatoDocumentoDiEntrataServiceOld;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
;

/**
 * The Class AggiornaStatoDocumentoEntrataTest.
 * 
 * @author Domenico
 */
public class AggiornaStatoDocumentoEntrataTest extends BaseJunit4TestCase {
	
	@Autowired
	private AggiornaStatoDocumentoDiEntrataServiceOld aggiornaStatoDocumentoDiEntrataService;

	@Autowired
	private AggiornaStatoDocumentoDiEntrataService aggiornaStatoDocumentoDiEntrataServiceNew;
	
	@PersistenceContext
	protected EntityManager entityManager;

	private long elapsedNew;
	private long elapsedOld;
	
	@Test
	public void aggiornaStatoDocumento(){
		aggiornaStatoDocumento(2);
	}
	
	
	@Test
	public void performanceTest(){
		
		boolean warmUp = true;
		long totaleTempoGuadagnato = 0;
		for(SiacDDocStatoEnum siacDDocStatoEnum : SiacDDocStatoEnum.values()) {
		
			List<SiacTDoc> siacTDocs = findDocs(siacDDocStatoEnum);
			
			for (SiacTDoc siacTDoc : siacTDocs) {
				Integer uidDoc = siacTDoc.getUid();
				
				System.out.println(">>>>>>>>>>>>>>>>>>>> uidDoc: "+uidDoc);
				
				
				AggiornaStatoDocumentoDiEntrataResponse resNew;
				AggiornaStatoDocumentoDiEntrataResponse res;
				
				if(warmUp){
					resNew = aggiornaStatoDocumentoNew(uidDoc);
					res = aggiornaStatoDocumento(uidDoc);
					warmUp = false;
				}
				
				resNew = aggiornaStatoDocumentoNew(uidDoc);
				res = aggiornaStatoDocumento(uidDoc);
				
				long tempoGuadagnato = elapsedOld-elapsedNew;
				totaleTempoGuadagnato += tempoGuadagnato;
				System.out.println(">>>>>>>>>>>>>>>>>>>> Tempo guadagnato: "+ tempoGuadagnato + " [elapsedOld: "+elapsedOld +", elapsedNew: "+elapsedNew+", siacDDocStatoEnum: "+siacDDocStatoEnum+"] ");
				
				assertTrue("somma congruente diversa", res.getSommaCongruente().equals(resNew.getSommaCongruente()));
				assertTrue("totaleQuoteENoteCreditoMenoImportiDaDedurre", res.getTotaleQuoteENoteCreditoMenoImportiDaDedurre().equals(resNew.getTotaleQuoteENoteCreditoMenoImportiDaDedurre()));
				assertTrue("tutteLeQuoteSonoAssociateAImpegnoOSubImpegno", res.getTutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento().equals(resNew.getTutteLeQuoteSonoAssociateAdAccertamentoOSubAccertamento()));
				assertTrue("stato diverso", res.getStatoOperativoDocumentoNuovo().equals(resNew.getStatoOperativoDocumentoNuovo()));
				
				break;
				
			}
			
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>> Totale tempo guadagnato: " + totaleTempoGuadagnato);
		
		
	}


	private List<SiacTDoc> findDocs(SiacDDocStatoEnum siacDDocStatoEnum) {
		TypedQuery<SiacTDoc> query = entityManager.createQuery(" FROM SiacTDoc d "
				                  + " WHERE d.dataCancellazione IS NULL "
				                  + " AND d.siacTEnteProprietario.enteProprietarioId = 1 "
				                  + " AND EXISTS (FROM d.siacRDocStatos rs "
				                  + "             WHERE rs.dataCancellazione IS NULL"
				                  + "             AND rs.siacDDocStato.docStatoCode = :docStatoCode "
				                  + "            )"
				                  + " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = '" + SiacDDocFamTipoEnum.Entrata.getCodice() + "'"
				                  //+ " AND SIZE(d.siacTSubdocs) > 4 "
				                  
				                  , SiacTDoc.class);
		
		query.setParameter("docStatoCode", siacDDocStatoEnum.getCodice());
		
		return query.getResultList();
	}
	
	
	
	
	
	private AggiornaStatoDocumentoDiEntrataResponse aggiornaStatoDocumento(int uidDoc) {
		AggiornaStatoDocumentoDiEntrata req = new AggiornaStatoDocumentoDiEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoEntrata docEntrata = new DocumentoEntrata();
		docEntrata.setUid(uidDoc);
		req.setDocumentoEntrata(docEntrata);
		
		long start = System.currentTimeMillis();
		AggiornaStatoDocumentoDiEntrataResponse res = aggiornaStatoDocumentoDiEntrataService.executeService(req);
		long end = System.currentTimeMillis();
		
		this.elapsedOld = end-start;
		System.out.println("############## Elapsed time: " + elapsedOld + "ms (OLD)");
		return res;
	}
	
	
	private AggiornaStatoDocumentoDiEntrataResponse aggiornaStatoDocumentoNew(int uidDoc) {
		AggiornaStatoDocumentoDiEntrata req = new AggiornaStatoDocumentoDiEntrata();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoEntrata docEntrata = new DocumentoEntrata();
		docEntrata.setUid(uidDoc);
		req.setDocumentoEntrata(docEntrata);
		
		long start = System.currentTimeMillis();
		AggiornaStatoDocumentoDiEntrataResponse res = aggiornaStatoDocumentoDiEntrataServiceNew.executeService(req);
		long end = System.currentTimeMillis();
		
		this.elapsedNew = end-start;
		System.out.println("############## Elapsed time: " + elapsedNew + "ms (NEW)");
		return res;
	}
	
}
