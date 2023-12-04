/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business.documentospesa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaService;
import it.csi.siac.siacbilser.business.service.documentospesa.AggiornaStatoDocumentoDiSpesaServiceOld;
import it.csi.siac.siacbilser.integration.dad.DocumentoDad;
import it.csi.siac.siacbilser.integration.dad.DocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.dad.ImpegnoBilDad;
import it.csi.siac.siacbilser.integration.dad.SubdocumentoSpesaDad;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocStatoEnum;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaStatoDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class AggiornaStatoDocumentoSpesaTest.
 * 
 * @author Domenico
 */
@TransactionConfiguration(defaultRollback = true)
public class AggiornaStatoDocumentoSpesaTest extends BaseJunit4TestCase {
	
	@Autowired
	private AggiornaStatoDocumentoDiSpesaServiceOld aggiornaStatoDocumentoDiSpesaService;
	@Autowired
	private AggiornaStatoDocumentoDiSpesaService aggiornaStatoDocumentoDiSpesaServiceNew;
	@Autowired
	private ImpegnoBilDad impegnoBilDad;
	
	@PersistenceContext
	protected EntityManager entityManager;

	private long elapsedNew;
	private long elapsedOld;
	
	@Test
	public void aggiornaStatoDocumento(){
		aggiornaStatoDocumentoNew(763);
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
				
				
				AggiornaStatoDocumentoDiSpesaResponse resNew;
				AggiornaStatoDocumentoDiSpesaResponse res;
				
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
				assertTrue("tutteLeQuoteSonoAssociateAImpegnoOSubImpegno", res.getTutteLeQuoteSonoAssociateAImpegnoOSubImpegno().equals(resNew.getTutteLeQuoteSonoAssociateAImpegnoOSubImpegno()));
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
				                  + " AND d.siacDDocTipo.siacDDocFamTipo.docFamTipoCode = '" + SiacDDocFamTipoEnum.Spesa.getCodice() + "'"
				                  //+ " AND SIZE(d.siacTSubdocs) > 4 "
				                  
				                  , SiacTDoc.class);
		
		query.setParameter("docStatoCode", siacDDocStatoEnum.getCodice());
		
		return query.getResultList();
	}
	
	
	
	
	
	private AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoDocumento(int uidDoc) {
		AggiornaStatoDocumentoDiSpesa req = new AggiornaStatoDocumentoDiSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
		DocumentoSpesa docSpesa = new DocumentoSpesa();
		docSpesa.setUid(uidDoc);
		req.setDocumentoSpesa(docSpesa);
		
		long start = System.currentTimeMillis();
		AggiornaStatoDocumentoDiSpesaResponse res = aggiornaStatoDocumentoDiSpesaService.executeService(req);
		long end = System.currentTimeMillis();
		
		this.elapsedOld = end-start;
		System.out.println("############## Elapsed time: " + elapsedOld + "ms (OLD)");
		return res;
	}
	
	
	private AggiornaStatoDocumentoDiSpesaResponse aggiornaStatoDocumentoNew(int uidDoc) {
		
		AggiornaStatoDocumentoDiSpesa req = new AggiornaStatoDocumentoDiSpesa();
		req.setRichiedente(getRichiedenteByProperties("consip","regp"));
//		req.setRichiedente(getRichiedenteTest("AAAAAA00A11E000M",71,15));
		DocumentoSpesa docSpesa = new DocumentoSpesa();
		docSpesa.setUid(uidDoc);
		req.setDocumentoSpesa(docSpesa);
		
		long start = System.currentTimeMillis();
		AggiornaStatoDocumentoDiSpesaResponse res = aggiornaStatoDocumentoDiSpesaServiceNew.executeService(req);
		long end = System.currentTimeMillis();
		
		this.elapsedNew = end-start;
		System.out.println("############## Elapsed time: " + elapsedNew + "ms (NEW)");
		return res;
	}
	
	@Test
	@Transactional
	public void aggiornaStatoDocumentoDiSpesa(){
		int docUid = 846;
		attivaRegistrazioneContabile(docUid); //NCD: 39152 figlia di 39151 
		aggiornaStatoDocumentoNew(docUid);
		
//		List<SiacTDoc> docs = findDocs(SiacDDocStatoEnum.Valido);
//		
//		int i = 0;
//		for (SiacTDoc siacTDoc : docs) {
//			
//			if(siacTDoc.getUid()!=39152 ){ // docId: 34645 Documento con 6 sub su siac_isola_20151124! ottimo per test: {76132=false, 76131=false, 76130=false, 76129=false, 76128=false, 76127=false}
//				continue;
//			}
//			
//			
////			List<DocumentoSpesa> noteCreditoSpesa = documentoSpesaDad.ricercaNoteCreditoSpesaCollegateEsclusivamenteAlDocumento(siacTDoc.getUid());
////			if(noteCreditoSpesa.isEmpty()){
////				continue;
////			} else {
////				System.out.println("################# Doc "+siacTDoc.getUid() + " ha delle note credito! Lo utilizzo");
////			}
//			
//			
//			attivaRegistrazioneContabile(siacTDoc.getUid());
//			aggiornaStatoDocumentoNew(siacTDoc.getUid());
//			i++;
//			if(i>10){
//				break;
//			}
//		}
		
		
	}
	@Autowired private DocumentoDad documentoDad;
	@Autowired private DocumentoSpesaDad documentoSpesaDad;
	
	public void attivaRegistrazioneContabile(Integer uidDoc){
		Documento<?, ?> d = new DocumentoSpesa();
		d.setUid(uidDoc);
		documentoDad.aggiornaFlagContabilizzaGenPcc(d, Boolean.TRUE);
		
		/*
//		EntityTransaction tx = entityManager.getTransaction();
//		tx.begin();
		SiacTDoc siacTDoc = entityManager.find(SiacTDoc.class, uidDoc);
		siacTDoc.setDocContabilizzaGenpcc(Boolean.TRUE);
		entityManager.persist(siacTDoc);
//		tx.commit();
		entityManager.flush();
		*/
	}
	
	
	@Test
	public void testImpegnoBilDad(){
//		Impegno imp = new Impegno();
//		imp.setUid(19); //1707729
		
		Subdocumento<?, ?> s = new SubdocumentoSpesa();
		s.setUid(1300);
		
		ElementoPianoDeiConti pdc = impegnoBilDad.findPianoDeiContiAssociatoAMovimentoGestione(s);
		System.out.println(pdc);
	}
	
	
	@Autowired
	private SubdocumentoSpesaDad subdocumentoSpesaDad;
	
	@Test
	public void testSubdocumentoSpesaDad() {
		Integer[] uids = new Integer[] {107383,107385};
		Map<Integer, String> mappa = new HashMap<Integer, String>();
		for(Integer uid : uids) {
			String key = subdocumentoSpesaDad.computeKeySubdocImportoImpegnoFlagRilevanteIva(uid.intValue());
			mappa.put(uid, key);
		}
		for (Integer chiave : mappa.keySet()) {
			System.out.println("subdcoumento: " + chiave + " chiave: " + mappa.get(chiave));
		}
		
	}


	@Test
	@Transactional
	public void findBilancioAssociatoAlDocumento() {
		long start = System.currentTimeMillis();
		Bilancio b = documentoSpesaDad.findBilancioAssociatoAlDocumento(2087); //8235 1915
		System.out.println(">>>>" +b + " query elapsed time: "+ (System.currentTimeMillis()-start));
	}
	
	
}
