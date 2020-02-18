/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemAttr;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsAttoAmm;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsStato;
import it.csi.siac.siacbilser.integration.entity.SiacRSubdocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoEntrataAccertamentoSubaccertamentoConverter.
 */
@Component
public class SubdocumentoEntrataAccertamentoSubaccertamentoConverterCopyToDel extends ExtendedDozerConverter<SubdocumentoEntrata, SiacTSubdoc> {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
//	@Autowired
//	private EnumEntityFactory eef;
	
	/** The siac t movgest t repository. */
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;

	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	
	/**
	 * Instantiates a new subdocumento entrata accertamento subaccertamento converter.
	 */
	public SubdocumentoEntrataAccertamentoSubaccertamentoConverterCopyToDel() {
		super(SubdocumentoEntrata.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoEntrata convertFrom(SiacTSubdoc src, SubdocumentoEntrata dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRSubdocMovgestTs()!=null) {
			for(SiacRSubdocMovgestT siacRSubdocMovgestT : src.getSiacRSubdocMovgestTs()){
				if(siacRSubdocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				
				SiacTMovgestT siacTMovgestT = siacTMovgestTRepository.findOne(siacRSubdocMovgestT.getSiacTMovgestT().getUid());
				
				log.debug(methodName, "siacRSubdocClass.siacTMovgestT.uid: " + siacTMovgestT.getUid());		
				
				if("T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) { //E' legato solo all'impegno
					Accertamento impegno = toAccertamento(siacTMovgestT);
					dest.setAccertamento(impegno);
				} else if("S".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){ //E' legato al subimpegno
					SubAccertamento subAccertamento = toSubAccertamento(siacTMovgestT);
					dest.setSubAccertamento(subAccertamento);
					//SiacTMovgestT siacTMovgestTAccertamentoTestata = ricavaMovimentoTestataDaIdAccertamento(siacTMovgestT.getSiacTMovgest().getUid());
					Accertamento impegno = toAccertamento(siacTMovgestT.getSiacTMovgestIdPadre());
					dest.setAccertamento(impegno);
				}		
				
			}
		}
		
		return dest;
	}
	

	/**
	 * To sub accertamento.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the sub accertamento
	 */
	private SubAccertamento toSubAccertamento(SiacTMovgestT siacTMovgestT) {	
		String methodName = "toSubAccertamento";
		SubAccertamento s = new SubAccertamento();
		//s.setNumero(siacTMovgestT.getSiacTMovgest().getMovgestNumero());
		try{
			s.setNumero(new BigDecimal(siacTMovgestT.getMovgestTsCode()));		
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!",re);	
			return null;
		}
		
		aggiungiStatoOperativoSubAccertamento(siacTMovgestT, s);
		
		return s;
	}

	/**
	 * To accertamento.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the accertamento
	 */
	private Accertamento toAccertamento(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest(); //Legame con l'impegno.
		Accertamento accertamento = new Accertamento();
		accertamento.setNumero(siacTMovgest.getMovgestNumero());
		String anno = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
		accertamento.setAnnoMovimento(Integer.parseInt(anno));
		
		aggiungiStatoOperativoAccertamento(siacTMovgestT, accertamento);
		aggiungiInformazioniCapitoloAdAccertamento(siacTMovgestT, accertamento);
		aggiungiInformazioniAttoAmministrativo(siacTMovgestT, accertamento);
		
		return accertamento;
	}

	private void aggiungiStatoOperativoAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				accertamento.setStatoOperativoMovimentoGestioneEntrata(code);
				accertamento.setDescrizioneStatoOperativoMovimentoGestioneEntrata(desc);
			}
		}
	}
	
	private void aggiungiStatoOperativoSubAccertamento(SiacTMovgestT siacTMovgestT, SubAccertamento subaccertamento) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				subaccertamento.setStatoOperativoMovimentoGestioneEntrata(code);
				subaccertamento.setDescrizioneStatoOperativoMovimentoGestioneEntrata(desc);
			}
		}
	}

	private void aggiungiInformazioniAttoAmministrativo(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for (SiacRMovgestTsAttoAmm rmtaa : siacTMovgestT.getSiacRMovgestTsAttoAmms()){
			if(rmtaa.getDataCancellazione()!=null){
				continue;
			}
			AttoAmministrativo attoAmministrativo = map(rmtaa.getSiacTAttoAmm(), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			accertamento.setAttoAmministrativo(attoAmministrativo);
			break;
		}
	}
	
	
	private void aggiungiInformazioniCapitoloAdAccertamento(SiacTMovgestT siacTMovgestT, Accertamento accertamento) {
		for(SiacRMovgestBilElem r :siacTMovgestT.getSiacTMovgest().getSiacRMovgestBilElems()){
			if(r.getDataCancellazione()==null){
				SiacTBilElem siacTBilElem = r.getSiacTBilElem();
				CapitoloEntrataGestione cap = new CapitoloEntrataGestione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);			
				
				SiacRBilElemAttr siacRBilElemAttr = siacTBilElemRepository.findBilElemAttrByTipoAttrCode(siacTBilElem.getUid(), SiacTAttrEnum.FlagRilevanteIva.getCodice());
				if(siacRBilElemAttr!=null) {
					cap.setFlagRilevanteIva("S".equals(siacRBilElemAttr.getBoolean_()));
				}
				
				accertamento.setCapitoloEntrataGestione(cap);
				
				break;
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoEntrata src, SiacTSubdoc dest) {
		
		Integer movgestId = null;
		
		if(src.getSubAccertamento()==null || src.getSubAccertamento().getUid()==0){
			if(src.getAccertamento()==null || src.getAccertamento().getUid()==0){
				//Se non viene passato ne l'Accertamento nè il Subimpegno esco.
				return dest;
			} else {
				//Se viene passato SOLO l'Accertamento mi lego alla Testata.
				movgestId = ricavaIdTestataDaIdAccertamento(src.getAccertamento().getUid());
			}
		} else {
			//Se viene passato il Subimpegno mi lego al Subimpegno.
			movgestId = src.getSubAccertamento().getUid();
		}
		
		
		dest.setSiacRSubdocMovgestTs(new ArrayList<SiacRSubdocMovgestT>());
		
		SiacRSubdocMovgestT siacRSubdocMovgestT = new SiacRSubdocMovgestT();
		siacRSubdocMovgestT.setSiacTSubdoc(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		siacRSubdocMovgestT.setSiacTMovgestT(siacTMovgestT);
		
		siacRSubdocMovgestT.setLoginOperazione(dest.getLoginOperazione());
		siacRSubdocMovgestT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRSubdocMovgestT(siacRSubdocMovgestT);
		
		return dest;
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
			throw new IllegalArgumentException("Impossibile trovare la testata dell'impegno con id "+ idAccertamento);
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



}
