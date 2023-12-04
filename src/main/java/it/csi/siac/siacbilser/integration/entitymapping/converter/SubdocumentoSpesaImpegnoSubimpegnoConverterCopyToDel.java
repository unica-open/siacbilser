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
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestTsDet;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacTAttrEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

// TODO: Auto-generated Javadoc
/**
 * The Class SubdocumentoSpesaImpegnoSubimpegnoConverter.
 */
@Component
public class SubdocumentoSpesaImpegnoSubimpegnoConverterCopyToDel extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
//	@Autowired
//	private EnumEntityFactory eef;
	
	/** The siac t movgest t repository. */
	@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	
	/**
	 * Instantiates a new subdocumento spesa impegno subimpegno converter.
	 */
	public SubdocumentoSpesaImpegnoSubimpegnoConverterCopyToDel() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRSubdocMovgestTs()!=null){
			for(SiacRSubdocMovgestT siacRSubdocMovgestT : src.getSiacRSubdocMovgestTs()){
				log.debug(methodName, "SiacTSubdoc: id = " + src.getSubdocId());
				log.debug(methodName, "SiacRSubdocMovgestT: id = " + siacRSubdocMovgestT.getSubdocMovgestTsId());
				if(siacRSubdocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				
				SiacTMovgestT siacTMovgestT = siacTMovgestTRepository.findOne(siacRSubdocMovgestT.getSiacTMovgestT().getUid());			
				
				log.debug(methodName, "siacRSubdocClass.siacTMovgestT.uid: " + siacTMovgestT.getUid());
				log.debug(methodName, "siacRSubdocClass.siacTMovgestT.getSiacDMovgestTsTipo == null?: " +
						(siacTMovgestT.getSiacDMovgestTsTipo() == null));
				
				if("T".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())) { //E' legato solo all'impegno
					Impegno impegno = toImpegno(siacTMovgestT);
					dest.setImpegno(impegno);
				} else if("S".equals(siacTMovgestT.getSiacDMovgestTsTipo().getMovgestTsTipoCode())){ //E' legato al subimpegno
					SubImpegno subImpegno = toSubImpegno(siacTMovgestT);
					dest.setSubImpegno(subImpegno);
					//SiacTMovgestT siacTMovgestTImpegnoTestata = ricavaMovimentoTestataDaIdImpegno(siacTMovgestT.getSiacTMovgest().getUid());
					Impegno impegno = toImpegno(siacTMovgestT.getSiacTMovgestIdPadre());
					dest.setImpegno(impegno);
				}		
				
			}
		}
		
		return dest;
	}
	

	/**
	 * To sub impegno.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the sub impegno
	 */
	private SubImpegno toSubImpegno(SiacTMovgestT siacTMovgestT) {	
		String methodName = "toSubImpegno";
		SubImpegno s = new SubImpegno();
		//s.setNumero(siacTMovgestT.getSiacTMovgest().getMovgestNumero());
		s.setUid(siacTMovgestT.getUid());
		
		try{
			s.setNumeroBigDecimal(new BigDecimal(siacTMovgestT.getMovgestTsCode()));		
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!",re);
		}
		
		aggiungiStatoOperativoSubImpegno(siacTMovgestT, s);
		aggiungiImportoSubImpegno(siacTMovgestT, s);
		
		return s;
	}

	/**
	 * To impegno.
	 *
	 * @param siacTMovgestT the siac t movgest t
	 * @return the impegno
	 */
	private Impegno toImpegno(SiacTMovgestT siacTMovgestT) {
		SiacTMovgest siacTMovgest = siacTMovgestT.getSiacTMovgest(); //Legame con l'impegno.
		Impegno impegno = new Impegno();
		impegno.setUid(siacTMovgest.getUid());
		impegno.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		String anno = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
		impegno.setAnnoMovimento(Integer.parseInt(anno));
	
		aggiungiImportoImpegno(siacTMovgestT, impegno);
		aggiungiStatoOperativoImpegno(siacTMovgestT, impegno);
		aggiungiInformazioniCapitoloAdImpegno(siacTMovgestT, impegno);
		aggiungiInformazioniAttoAmministrativo(siacTMovgestT, impegno);
		
		return impegno;
	}
	

	private void aggiungiImportoImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacTMovgestTsDet siacTMovgestTsDet : siacTMovgestT.getSiacTMovgestTsDets()){
			if(siacTMovgestTsDet.getDataCancellazione() == null){
				impegno.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
		}
		
	}
	
	private void aggiungiImportoSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno subimpegno) {
		for(SiacTMovgestTsDet siacTMovgestTsDet : siacTMovgestT.getSiacTMovgestTsDets()){
			if(siacTMovgestTsDet.getDataCancellazione() == null){
				subimpegno.setImportoAttuale(siacTMovgestTsDet.getMovgestTsDetImporto());
			}
		}
		
	}

	private void aggiungiStatoOperativoImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				impegno.setStatoOperativoMovimentoGestioneSpesa(code);
				impegno.setDescrizioneStatoOperativoMovimentoGestioneSpesa(desc);
			}
		}
	}
	
	private void aggiungiStatoOperativoSubImpegno(SiacTMovgestT siacTMovgestT, SubImpegno subimpegno) {
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				subimpegno.setStatoOperativoMovimentoGestioneSpesa(code);
				subimpegno.setDescrizioneStatoOperativoMovimentoGestioneSpesa(desc);
			}
		}
	}

	private void aggiungiInformazioniAttoAmministrativo(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for (SiacRMovgestTsAttoAmm rmtaa : siacTMovgestT.getSiacRMovgestTsAttoAmms()){
			if(rmtaa.getDataCancellazione()!=null){
				continue;
			}
			AttoAmministrativo attoAmministrativo = map(rmtaa.getSiacTAttoAmm(), AttoAmministrativo.class, BilMapId.SiacTAttoAmm_AttoAmministrativo);
			impegno.setAttoAmministrativo(attoAmministrativo);
			break;
		}
	}
	
	
	
	private void aggiungiInformazioniCapitoloAdImpegno(SiacTMovgestT siacTMovgestT, Impegno impegno) {
		for(SiacRMovgestBilElem r :siacTMovgestT.getSiacTMovgest().getSiacRMovgestBilElems()){
			if(r.getDataCancellazione()==null){
				SiacTBilElem siacTBilElem = r.getSiacTBilElem();
				CapitoloUscitaGestione cap = new CapitoloUscitaGestione();
				map(siacTBilElem, cap, BilMapId.SiacTBilElem_Capitolo_Base);			
				
				SiacRBilElemAttr siacRBilElemAttr = siacTBilElemRepository.findBilElemAttrByTipoAttrCode(siacTBilElem.getUid(), SiacTAttrEnum.FlagRilevanteIva.getCodice());
				if(siacRBilElemAttr!=null) {
					cap.setFlagRilevanteIva("S".equals(siacRBilElemAttr.getBoolean_()));
				}
				
				impegno.setCapitoloUscitaGestione(cap);
				
				break;
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		
		Integer movgestId = null;
		
		if(src.getSubImpegno()==null || src.getSubImpegno().getUid()==0){
			if(src.getImpegno()==null || src.getImpegno().getUid()==0){
				//Se non viene passato ne l'Impegno nè il Subimpegno esco.
				return dest;
			} else {
				//Se viene passato SOLO l'Impegno mi lego alla Testata.
				movgestId = ricavaIdTestataDaIdImpegno(src.getImpegno().getUid());
			}
		} else {
			//Se viene passato il Subimpegno mi lego al Subimpegno.
			movgestId = src.getSubImpegno().getUid();
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
	private Integer ricavaIdTestataDaIdImpegno(Integer idImpegno) {
		String methodName = "ricavaIdTestataDaIdImpegno";
		Integer result =ricavaMovimentoTestataDaIdImpegno(idImpegno).getUid();		
		log.debug(methodName, "Trovato id testata: " + result);
		return result;
	}



}
