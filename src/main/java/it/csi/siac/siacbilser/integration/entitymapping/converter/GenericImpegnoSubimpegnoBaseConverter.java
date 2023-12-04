/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacDSiopeTipoDebito;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemClass;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsStato;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTClass;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDClassTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDMovgestTsDetTipoEnum;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.siopeplus.SiopeTipoDebito;

/**
 * Converer base le classi di Model che agganciano per Impegno e SubImpegno
 * 
 * @author Domenico Lisi
 */
public abstract class GenericImpegnoSubimpegnoBaseConverter<A, B> extends ExtendedDozerConverter<A, B> {
	
	@Autowired
	protected SiacTMovgestTRepository siacTMovgestTRepository;
	
	@Autowired
	protected SiacTBilElemRepository siacTBilElemRepository;
	
	@Autowired
	protected MovimentoGestioneDao movimentoGestioneDao;
	
	
	/**
	 * Instantiates a new extended dozer converter.
	 *
	 * @param prototypeA the prototype a
	 * @param prototypeB the prototype b
	 */
	protected GenericImpegnoSubimpegnoBaseConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}

	//	---------------------------- Convert FROM section ----------------------------------------------
	
	/**
	 * Imposta nella destinazione l'Impegno e il subImpegno
	 * 
	 * @param dest
	 * @param movgestId
	 */
	protected void impostaImpegnoESubImpegno(A dest, Integer movgestId) {
		
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);
		
		impostaImpegnoESubImpegno(dest, siacTMovgestT);
	}
	
	/**
	 * Imposta nella destinazione l'Impegno e il subImpegno
	 * 
	 * @param dest
	 * @param siacTMovgestT
	 */
	protected void impostaImpegnoESubImpegno(A dest, SiacTMovgestT siacTMovgestT) {
		final String methodName = "impostaImpegnoESubImpegno";
		
		if(siacTMovgestT == null) {
			log.debug(methodName, "siacTMovgestT: NULL");
			return;
		}
		log.debug(methodName, "siacTMovgestT.uid: " + siacTMovgestT.getUid());
	
		if(siacTMovgestT.getSiacDMovgestTsTipo()==null) {
			//Workaround nel caso io abbia solo l'uid della siacTMovgestT
			siacTMovgestT = siacTMovgestTRepository.findOne(siacTMovgestT.getUid());
		}
		
		log.debug(methodName, "siacTMovgestT.getSiacDMovgestTsTipo: " + siacTMovgestT.getSiacDMovgestTsTipo() == null);
		
		Boolean isTipoImpegno = isTipoImpegno(siacTMovgestT);
		if(!Boolean.TRUE.equals(isTipoImpegno)){
			log.debug(methodName, "Non di tipo Impegno: Il siacTMovgestT viene scartato. [uid: " + siacTMovgestT.getUid()+"]");
			return;
		}
		
		BeanWrapper bwDest = PropertyAccessorFactory.forBeanPropertyAccess(dest);
		
		if("T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) { //E' legato solo all'impegno
			Impegno impegno = toImpegno(siacTMovgestT);
			bwDest.setPropertyValue("impegno", impegno);
		} else if("S".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){ //E' legato al subimpegno
			SubImpegno subImpegno = toSubImpegno(siacTMovgestT);
			bwDest.setPropertyValue("subImpegno", subImpegno);
			//SiacTMovgestT siacTMovgestTImpegnoTestata = ricavaMovimentoTestataDaIdImpegno(siacTMovgestT.getSiacTMovgest().getUid());
			Impegno impegno = toImpegno(siacTMovgestT.getSiacTMovgestIdPadre());
			bwDest.setPropertyValue("impegno", impegno);
		}
		
	}

	/**
	 * Controlla che effettivamente il siacTMovgestT sia afferente ad un getSiacTMovgest di tipo Impegno
	 * 
	 * @param siacTMovgestT
	 * @return
	 */
	private Boolean isTipoImpegno(SiacTMovgestT siacTMovgestT) {
		String methodName = "isTipoImpegno";
		Boolean isTipoImpegno = null;
		try {
			isTipoImpegno = "I".equals(siacTMovgestT.getSiacTMovgest().getSiacDMovgestTipo().getMovgestTipoCode());
			
		} catch (RuntimeException re) {
			String msg = "Impossibile determinare se il movimento di gestione [uid:"+siacTMovgestT.getUid()+"] e' di tipo Impegno.";
			log.error(methodName, msg, re);
			throw new IllegalStateException(msg, re);
		}
		
		log.debug(methodName, "Returning: "+isTipoImpegno+". [uid: " + siacTMovgestT.getUid()+"]");
		return isTipoImpegno;
	}
	
	

	/**
	 * To sub impegno.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the sub impegno
	 */
	protected SubImpegno toSubImpegno(SiacTMovgestT siacTMovgestT) {
		String methodName = "toSubImpegno";
		SubImpegno s = new SubImpegno();
		//s.setNumero(siacTMovgestT.getSiacTMovgest().getMovgestNumero());
		s.setUid(siacTMovgestT.getUid());
		
		try{
			s.setNumeroBigDecimal(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!",re);
		}
		
		impostaDatiAggiuntiviSubImpegno(siacTMovgestT, s);
		
		return s;
	}

	/**
	 * Impostazione dei dati aggiuntivi per il subimpegno
	 * @param siacTMovgestT la entity
	 * @param s il subimpegno
	 */
	protected void impostaDatiAggiuntiviSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno s) {
		aggiungiStatoOperativoSubImpegno(siacTMovgestT, s);
		aggiungiImportoSubImpegno(siacTMovgestT, s);
	}

	/**
	 * To impegno.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the impegno
	 */
	protected Impegno toImpegno(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest(); //Legame con l'impegno.
		Impegno impegno = new Impegno();
		impegno.setUid(siacTMovgest.getUid());
		impegno.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		impegno.setDescrizione(siacTMovgest.getMovgestDesc());
//		String anno = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
		impegno.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		impegno.setDataEmissione(siacTMovgestT.getDataCreazione());
	
		impostaDatiAggiuntiviImpegno(siacTMovgestT, impegno);
		
		return impegno;
	}

	/**
	 * Impostazione dei dati aggiuntivi per l'impegno
	 * @param siacTMovgestT la entity
	 * @param impegno l'impegno
	 */
	protected void impostaDatiAggiuntiviImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		// FIXME: sara' da svuotare e portare i singoli pezzi sulle sottoclassi
		aggiungiImportoImpegno(siacTMovgestT, impegno);
		aggiungiStatoOperativoImpegno(siacTMovgestT, impegno);
		aggiungiInformazioniCapitoloAdImpegno(siacTMovgestT, impegno);
		aggiungiInformazioniAttoAmministrativo(siacTMovgestT, impegno);
		aggiungiAnnoOrigineCapitolo(siacTMovgestT, impegno);
		aggiungiAttributi(siacTMovgestT, impegno);
	}
	

	/**
	 * Aggiungi attributi.
	 *
	 * @param siacTMovgestT the siac T movgest T
	 * @param impegno the impegno
	 */
	protected void aggiungiAttributi(SiacTMovgestT siacTMovgestT,Impegno impegno) {
		for(SiacRMovgestTsAttr siacRMovgestTsAttr:  siacTMovgestT.getSiacRMovgestTsAttrs()){
			if(siacRMovgestTsAttr.getDataCancellazione() != null ) {
				continue;
			}
			impostaFlagGsa(impegno, siacRMovgestTsAttr);
			impostaFlagDurc(impegno, siacRMovgestTsAttr);
		}
	}

	
	
	private void impostaFlagGsa(Impegno impegno, SiacRMovgestTsAttr siacRMovgestTsAttr) {
		if(SiacTAttrEnum.FlagAttivaGsa.getCodice().equals(siacRMovgestTsAttr.getSiacTAttr().getAttrCode())) {
			impegno.setFlagAttivaGsa(siacRMovgestTsAttr.getBoolean_() != null && "S".equals(siacRMovgestTsAttr.getBoolean_()));
		}
	}
	
	private void impostaFlagDurc(Impegno impegno, SiacRMovgestTsAttr siacRMovgestTsAttr) {
		if(SiacTAttrEnum.FlagSoggettoDurc.getCodice().equals(siacRMovgestTsAttr.getSiacTAttr().getAttrCode())) {
			impegno.setFlagSoggettoDurc(siacRMovgestTsAttr.getBoolean_() != null && "S".equals(siacRMovgestTsAttr.getBoolean_()));
		}
	}

	private void setBooleanFlag(SiacTAttrEnum flagAttribute, SiacRMovgestTsAttr siacRMovgestTsAttr, Impegno impegno) {
		if(!flagAttribute.getCodice().equals(siacRMovgestTsAttr.getSiacTAttr().getAttrCode())) {
			return;
		}
	}

	protected void aggiungiAnnoOrigineCapitolo(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacRMovgestTsAttr siacRMovgestTsAttr:  siacTMovgestT.getSiacRMovgestTsAttrs()){
			if(siacRMovgestTsAttr.getDataCancellazione() == null 
					&& SiacTAttrEnum.AnnoCapitoloOrigine.getCodice().equals(siacRMovgestTsAttr.getSiacTAttr().getAttrCode())
					&& siacRMovgestTsAttr.getTesto() != null){
				impegno.setAnnoCapitoloOrigine(Integer.parseInt(siacRMovgestTsAttr.getTesto()));
			}
		}
	}

	protected void aggiungiImportoImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		String methodName = "aggiungiImportoImpegno";
		
		for(SiacTMovgestTsDet siacTMovgestTsDet : siacTMovgestT.getSiacTMovgestTsDets()){
			if(siacTMovgestTsDet.getDataCancellazione() != null){
				continue;
			}
			
			if(SiacDMovgestTsDetTipoEnum.Attuale.getCodice().equals(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
				impegno.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
			
			if(SiacDMovgestTsDetTipoEnum.Iniziale.getCodice().equals(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
				impegno.setImportoIniziale(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
			
		}
		
		log.debug(methodName, "importoAttuale: "+impegno.getImportoAttuale() + " importoIniziale: "+impegno.getImportoIniziale());
		
		if(impegno.getImportoAttuale()==null){
			impegno.setImportoAttuale(BigDecimal.ZERO);
		}
		
		if(impegno.getImportoIniziale()==null){
			impegno.setImportoIniziale(BigDecimal.ZERO);
		}
		
		
	}
	
	protected void aggiungiImportoSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno subimpegno) {
		aggiungiImportoImpegno(siacTMovgestT, subimpegno);
	}

	protected void aggiungiStatoOperativoImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				impegno.setStatoOperativoMovimentoGestioneSpesa(code);
				impegno.setDescrizioneStatoOperativoMovimentoGestioneSpesa(desc);
			}
		}
	}
	
	protected void aggiungiStatoOperativoSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno subimpegno) {
		aggiungiStatoOperativoImpegno(siacTMovgestT, subimpegno);
	}

	protected void aggiungiInformazioniAttoAmministrativo(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for (SiacRMovgestTsAttoAmm rmtaa : siacTMovgestT.getSiacRMovgestTsAttoAmms()){
			if(rmtaa.getDataCancellazione()!=null){
				continue;
			}
			AttoAmministrativo attoAmministrativo = map(rmtaa.getSiacTAttoAmm(), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			impegno.setAttoAmministrativo(attoAmministrativo);
			break;
		}
	}
	
	
	
	protected void aggiungiInformazioniCapitoloAdImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacRMovgestBilElem r :siacTMovgestT.getSiacTMovgest().getSiacRMovgestBilElems()){
			if(r.getDataCancellazione()==null){
				SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(r.getSiacTBilElem().getElemId());
				CapitoloUscitaGestione cap = new CapitoloUscitaGestione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);
				
				SiacRBilElemAttr siacRBilElemAttr = siacTBilElemRepository.findBilElemAttrByTipoAttrCode(siacTBilElem.getUid(), SiacTAttrEnum.FlagRilevanteIva.getCodice());
				if(siacRBilElemAttr!=null) {
					cap.setFlagRilevanteIva("S".equals(siacRBilElemAttr.getBoolean_()));
				}
				
				// Elemento del piano dei conti
				SiacRBilElemClass siacRBilElemClassPDC = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
						Arrays.asList(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice(), SiacDClassTipoEnum.QuintoLivelloPdc.getCodice()));
				
				if(siacRBilElemClassPDC != null) {
					ElementoPianoDeiConti epdc = new ElementoPianoDeiConti();
					SiacTClass siacTClass = siacRBilElemClassPDC.getSiacTClass();
					epdc.setUid(siacTClass.getUid());
					epdc.setCodice(siacTClass.getClassifCode());
					epdc.setDescrizione(siacTClass.getClassifDesc());
					
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setCodice(siacTClass.getSiacDClassTipo().getClassifTipoCode());
					tipoClassificatore.setUid(siacTClass.getSiacDClassTipo().getUid());
					epdc.setTipoClassificatore(tipoClassificatore);
					cap.setElementoPianoDeiConti(epdc);
				}
				
				impegno.setCapitoloUscitaGestione(cap);
				
				break;
			}
		}
		
	}

	//	---------------------------- Convert TO section ----------------------------------------------
	
	
	protected Integer getMovgestId(Impegno impegno, SubImpegno subimpegno) {
		Integer movgestId = null;
		if(subimpegno==null || subimpegno.getUid()==0){
			if(impegno==null || impegno.getUid()==0){
				//Se non viene passato ne l'Impegno nè il Subimpegno esco.
				return null;
			} else {
				//Se viene passato SOLO l'Impegno mi lego alla Testata.
				movgestId = ricavaIdTestataDaIdImpegno(impegno.getUid());
			}
		} else {
			//Se viene passato il Subimpegno mi lego al Subimpegno.
			movgestId = subimpegno.getUid();
		}
		return movgestId;
	}

	/**
	 * Ricava movimento testata da id impegno.
	 *
	 * @param idImpegno the id impegno
	 * @return the siac t movgest t
	 */
	private SiacTMovgestT ricavaMovimentoTestataDaIdImpegno(Integer idImpegno) {
		
		List<SiacTMovgestT> list = siacTMovgestTRepository.findSiacTMovgestTestataBySiacTMovgestId(idImpegno);
		if(list.isEmpty())	{
			throw new IllegalArgumentException("Impossibile trovare la testata dell'impegno con id "+ idImpegno);
		}
		
		return list.get(0);
	}
	
	/**
	 * Ricava id testata da id impegno.
	 *
	 * @param idImpegno the id impegno
	 * @return the integer
	 */
	protected Integer ricavaIdTestataDaIdImpegno(Integer idImpegno) {
		String methodName = "ricavaIdTestataDaIdImpegno";
		Integer result =ricavaMovimentoTestataDaIdImpegno(idImpegno).getUid();		
		log.debug(methodName, "Trovato id testata: " + result);
		return result;
	}

	/**
	 * Aggiunge la disponibilita pagare.
	 * 
	 * @param siacTMovgestT
	 * @param impegno
	 */
	protected void aggiungiInformazioneDisponibilitaPagare(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		String methodName = "aggiungiInformazioneDisponibilitaPagare";
		
		BigDecimal importoAttuale = impegno.getImportoAttuale();
		if(importoAttuale==null){
			// se non e' stato caricato l'importo dell'impegno lo carico.
			aggiungiImportoImpegno(siacTMovgestT, impegno);
			importoAttuale = impegno.getImportoAttuale();
		}
		
		BigDecimal totaleSubordinativi = movimentoGestioneDao.calcolaTotaleImportoSubOrdinativiByMovgestTsId(siacTMovgestT.getMovgestTsId());
		log.debug(methodName, "totaleSubordinativi: "+totaleSubordinativi);
		
		BigDecimal disponibilitaPagare = importoAttuale.subtract(totaleSubordinativi);
		
		log.debug(methodName, "disponibilitaPagare: "+disponibilitaPagare + " (=importoAttuale["+importoAttuale+"]-totaleSubordinativi["+totaleSubordinativi+"]) ");
		impegno.setDisponibilitaPagare(disponibilitaPagare);
		// SIAC-6695
		impegno.setMotivazioneDisponibilitaPagare("Disponibilita' a pagare calcolato come sottrazione tra importo attuale ("
				+ disponibilitaPagare.toPlainString() + ") e totale dei subordinativi collegati (" + totaleSubordinativi.toPlainString() + ")");
	}
	
	protected void aggiungiSiopeTipoDebito(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		SiacDSiopeTipoDebito siacDSiopeTipoDebito = siacTMovgestT.getSiacDSiopeTipoDebito();
		SiopeTipoDebito siopeTipoDebito = mapNotNull(siacDSiopeTipoDebito, SiopeTipoDebito.class, BilMapId.SiacDSiopeTipoDebito_SiopeTipoDebito);
		impegno.setSiopeTipoDebito(siopeTipoDebito);
	}

}
