/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.MovimentoGestioneDao;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
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
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.TipoClassificatore;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

/**
 * Converer base le classi di Model che agganciano ad Accertamento e SubAccertamento
 * 
 * @author Domenico Lisi
 */
public abstract class GenericAccertamentoSubaccertamentoBaseConverter<A, B> extends ExtendedDozerConverter<A, B> {
	
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
	protected GenericAccertamentoSubaccertamentoBaseConverter(Class<A> prototypeA, Class<B> prototypeB) {
		super(prototypeA, prototypeB);
	}

	//	---------------------------- Convert FROM section ----------------------------------------------
	
	/**
	 * Imposta nella destinazione l'Accertamento e il subAccertamento
	 * 
	 * @param dest
	 * @param movgestId
	 */
	protected void impostaAccertamentoESubAccertamento(A dest, Integer movgestId) {
		
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);
		
		impostaAccertamentoESubAccertamento(dest, siacTMovgestT);
	}
	
	/**
	 * Imposta nella destinazione l'Accertamento e il subAccertamento
	 * 
	 * @param dest
	 * @param siacTMovgestT
	 */
	protected void impostaAccertamentoESubAccertamento(A dest, SiacTMovgestT siacTMovgestT) {
		final String methodName = "impostaAccertamentoESubAccertamento";
		
		if(siacTMovgestT == null) {
			log.debug(methodName, "siacTMovgestT: NULL" );
			return;
		}
		log.debug(methodName, "siacTMovgestT.uid: " + siacTMovgestT.getUid());
	
		if(siacTMovgestT.getSiacDMovgestTsTipo()==null) {
			//Workaround nel caso ho solo l'uid della siacTMovgestT
			siacTMovgestT = siacTMovgestTRepository.findOne(siacTMovgestT.getUid());
		}
		
		log.debug(methodName, "siacTMovgestT.getSiacDMovgestTsTipo: " + siacTMovgestT.getSiacDMovgestTsTipo() == null);
		
		BeanWrapper bwDest = PropertyAccessorFactory.forBeanPropertyAccess(dest);
		
		if("T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) { //E' legato solo all'impegno
			Accertamento accertamento = toAccertamento(siacTMovgestT);
			bwDest.setPropertyValue("accertamento", accertamento);
		} else if("S".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){ //E' legato al subimpegno
			SubAccertamento subAccertamento = toSubAccertamento(siacTMovgestT);
			bwDest.setPropertyValue("subAccertamento", subAccertamento);
			//SiacTMovgestT siacTMovgestTImpegnoTestata = ricavaMovimentoTestataDaIdImpegno(siacTMovgestT.getSiacTMovgest().getUid());
			Accertamento accertamento = toAccertamento(siacTMovgestT.getSiacTMovgestIdPadre());
			bwDest.setPropertyValue("accertamento", accertamento);
		}
		
	}
	
	/**
	 * To sub accertamento.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the sub accertamento
	 */
	protected SubAccertamento toSubAccertamento(SiacTMovgestT siacTMovgestT) {	
		String methodName = "toSubAccertamento";
		SubAccertamento s = new SubAccertamento();
		//s.setNumero(siacTMovgestT.getSiacTMovgest().getMovgestNumero());
		s.setUid(siacTMovgestT.getUid());
		
		try{
			s.setNumero(new BigDecimal(siacTMovgestT.getMovgestTsCode()));
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!",re);	
			return null;
		}
		
		impostaDatiAggiuntiviSubAccertamento(siacTMovgestT, s);
		
		return s;
	}

	/**
	 * Impostazione dei dati aggiuntivi per il subaccertamento
	 * @param siacTMovgestT la entity
	 * @param s il subaccertamento
	 */
	protected void impostaDatiAggiuntiviSubAccertamento(SiacTMovgestT siacTMovgestT, SubAccertamento s) {
		aggiungiStatoOperativoSubAccertamento(siacTMovgestT, s);
	}

	/**
	 * To accertamento.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the accertamento
	 */
	protected Accertamento toAccertamento(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest(); //Legame con l'impegno.
		Accertamento accertamento = new Accertamento();
		accertamento.setUid(siacTMovgest.getUid());
		accertamento.setNumero(siacTMovgest.getMovgestNumero());
//		String anno = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
		accertamento.setAnnoMovimento(siacTMovgest.getMovgestAnno());
		
		impostaDatiAggiuntiviAccertamento(siacTMovgestT, accertamento);
		
		return accertamento;
	}

	/**
	 * Impostazione dei dati aggiuntivi per l'accertamento
	 * @param siacTMovgestT la entity
	 * @param accertamento l'accertamento
	 */
	protected void impostaDatiAggiuntiviAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		aggiungiStatoOperativoAccertamento(siacTMovgestT, accertamento);
		aggiungiInformazioniCapitoloAdAccertamento(siacTMovgestT, accertamento);
		aggiungiInformazioniAttoAmministrativo(siacTMovgestT, accertamento);
		aggiungiAnnoOrigineCapitolo(siacTMovgestT, accertamento);
		aggiungiAutomatico(siacTMovgestT, accertamento);
	}
	

	protected void aggiungiAutomatico(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		if(siacTMovgestT.getSiacRMovgestTsAttrs() != null){
			for(SiacRMovgestTsAttr r: siacTMovgestT.getSiacRMovgestTsAttrs()){
				if(r.getDataCancellazione() == null && SiacTAttrEnum.AccertamentoAutomatico.getCodice().equals(r.getSiacTAttr().getAttrCode()) && "S".equals(r.getBoolean_())){
					accertamento.setAutomatico(true);
				}
			}
		}
	}

	protected void aggiungiAnnoOrigineCapitolo(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacRMovgestTsAttr siacRMovgestTsAttr:  siacTMovgestT.getSiacRMovgestTsAttrs()){
			if(siacRMovgestTsAttr.getDataCancellazione() == null 
					&& SiacTAttrEnum.AnnoCapitoloOrigine.getCodice().equals(siacRMovgestTsAttr.getSiacTAttr().getAttrCode())){
				accertamento.setAnnoCapitoloOrigine(Integer.parseInt(
						StringUtils.isNotBlank(siacRMovgestTsAttr.getTesto())?
								siacRMovgestTsAttr.getTesto() 
								: "0")
						);
			}
		}
	}

	protected void aggiungiStatoOperativoAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				accertamento.setStatoOperativoMovimentoGestioneEntrata(code);
				accertamento.setDescrizioneStatoOperativoMovimentoGestioneEntrata(desc);
			}
		}
	}
	
	protected void aggiungiStatoOperativoSubAccertamento(SiacTMovgestT siacTMovgestT, SubAccertamento subaccertamento) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				subaccertamento.setStatoOperativoMovimentoGestioneEntrata(code);
				subaccertamento.setDescrizioneStatoOperativoMovimentoGestioneEntrata(desc);
			}
		}
	}

	protected void aggiungiInformazioniAttoAmministrativo(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for (SiacRMovgestTsAttoAmm rmtaa : siacTMovgestT.getSiacRMovgestTsAttoAmms()){
			if(rmtaa.getDataCancellazione()!=null){
				continue;
			}
			AttoAmministrativo attoAmministrativo = map(rmtaa.getSiacTAttoAmm(), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			accertamento.setAttoAmministrativo(attoAmministrativo);
			break;
		}
	}
	
	
	protected void aggiungiInformazioniCapitoloAdAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacRMovgestBilElem r :siacTMovgestT.getSiacTMovgest().getSiacRMovgestBilElems()){
			if(r.getDataCancellazione()==null){
				SiacTBilElem siacTBilElem = siacTBilElemRepository.findOne(r.getSiacTBilElem().getElemId());
				CapitoloEntrataGestione cap = new CapitoloEntrataGestione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);
				
				SiacRBilElemAttr siacRBilElemAttr = siacTBilElemRepository.findBilElemAttrByTipoAttrCode(siacTBilElem.getUid(), SiacTAttrEnum.FlagRilevanteIva.getCodice());
				if(siacRBilElemAttr!=null) {
					cap.setFlagRilevanteIva("S".equals(siacRBilElemAttr.getBoolean_()));
				}
				
				// Elemento del piano dei conti
				SiacRBilElemClass siacRBilElemClass = siacTBilElemRepository.findBilElemClassByTipoClassCodes(siacTBilElem.getUid(),
						Arrays.asList(SiacDClassTipoEnum.QuartoLivelloPdc.getCodice(), SiacDClassTipoEnum.QuintoLivelloPdc.getCodice()));
				if(siacRBilElemClass != null) {
					ElementoPianoDeiConti epdc = new ElementoPianoDeiConti();
					SiacTClass siacTClass = siacRBilElemClass.getSiacTClass();
					epdc.setUid(siacTClass.getUid());
					epdc.setCodice(siacTClass.getClassifCode());
					epdc.setDescrizione(siacTClass.getClassifDesc());
					
					TipoClassificatore tipoClassificatore = new TipoClassificatore();
					tipoClassificatore.setCodice(siacRBilElemClass.getSiacTClass().getSiacDClassTipo().getClassifTipoCode());
					tipoClassificatore.setUid(siacRBilElemClass.getSiacTClass().getSiacDClassTipo().getUid());
					epdc.setTipoClassificatore(tipoClassificatore);
					cap.setElementoPianoDeiConti(epdc);
				}
				
				accertamento.setCapitoloEntrataGestione(cap);
				
				break;
			}
		}
		
	}
	

	//	---------------------------- Convert TO section ----------------------------------------------
	
	
	protected Integer getMovgestId(Accertamento accertamento, SubAccertamento subAccertamento) {
		Integer movgestId = null;
		if(subAccertamento==null || subAccertamento.getUid()==0){
			if(accertamento==null || accertamento.getUid()==0){
				//Se non viene passato ne l'Accertamento nè il SubAccertamento esco.
				return null;
			} else {
				//Se viene passato SOLO l'Accertamento mi lego alla Testata.
				movgestId = ricavaIdTestataDaIdAccertamento(accertamento.getUid());
			}
		} else {
			//Se viene passato il SubAccertamento mi lego al SubAccertamento.
			movgestId = subAccertamento.getUid();
		}
		return movgestId;
	}

	/**
	 * Ricava movimento testata da id accertamento.
	 *
	 * @param idAccertamento the id accertamento
	 * @return the siac t movgest t
	 */
	private SiacTMovgestT ricavaMovimentoTestataDaIdAccertamento(Integer idAccertamento) {
		
		List<SiacTMovgestT> list = siacTMovgestTRepository.findSiacTMovgestTestataBySiacTMovgestId(idAccertamento);
		if(list.isEmpty())	{
			throw new IllegalArgumentException("Impossibile trovare la testata dell'accertamento con id "+ idAccertamento);
		}
		
		return list.get(0);
	}
	
	/**
	 * Ricava id testata da id accertamento.
	 *
	 * @param idAccertamento the id accertamento
	 * @return the integer
	 */
	private Integer ricavaIdTestataDaIdAccertamento(Integer idAccertamento) {
		String methodName = "ricavaIdTestataDaIdAccertamento";
		Integer result =ricavaMovimentoTestataDaIdAccertamento(idAccertamento).getUid();		
		log.debug(methodName, "Trovato id testata: " + result);
		return result;
	}

	protected void aggiungiImportoAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacTMovgestTsDet siacTMovgestTsDet : siacTMovgestT.getSiacTMovgestTsDets()){
			if(siacTMovgestTsDet.getDataCancellazione() != null){
				continue;
			}
			
			if(SiacDMovgestTsDetTipoEnum.Attuale.getCodice().equals(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
				accertamento.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
			
			if(SiacDMovgestTsDetTipoEnum.Iniziale.getCodice().equals(siacTMovgestTsDet.getSiacDMovgestTsDetTipo().getMovgestTsDetTipoCode())){
				accertamento.setImportoIniziale(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
			
		}
	}
	
	protected void aggiungiDisponibilitaIncassare(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		// FIXME: portare la function in una classe esterna?
		BigDecimal disponibilitaIncassare = movimentoGestioneDao.calcolaDisponibilita(siacTMovgestT.getUid(), "fnc_siac_disponibilitaincassaremovgest");
		accertamento.setDisponibilitaIncassare(disponibilitaIncassare);
		accertamento.setMotivazioneDisponibilitaIncassare("Disponibilita' calcolata dalla function");
	}
	
}
