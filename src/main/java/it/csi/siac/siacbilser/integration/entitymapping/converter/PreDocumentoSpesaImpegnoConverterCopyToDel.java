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

import it.csi.siac.siacbilser.integration.dao.SiacTMovgestTRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRMovgestTsStato;
import it.csi.siac.siacbilser.integration.entity.SiacRPredocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoSpesaImpegnoConverter.
 */
@Component
public class PreDocumentoSpesaImpegnoConverterCopyToDel extends ExtendedDozerConverter<PreDocumentoSpesa, SiacTPredoc > {
	
	/** The log. */
	private LogUtil log = new LogUtil(this.getClass());
	
//	@Autowired
//	private EnumEntityFactory eef;
	
	/** The siac t movgest t repository. */
@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	
	/**
	 * Instantiates a new pre documento spesa impegno converter.
	 */
	public PreDocumentoSpesaImpegnoConverterCopyToDel() {
		super(PreDocumentoSpesa.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoSpesa convertFrom(SiacTPredoc src, PreDocumentoSpesa dest) {
		final String methodName = "convertFrom";
		
		if(src.getSiacRPredocMovgestTs()!=null){
			for(SiacRPredocMovgestT siacRPredocMovgestT : src.getSiacRPredocMovgestTs()){
				if(siacRPredocMovgestT.getDataCancellazione()!=null) {
					continue;
				}
				
				SiacTMovgestT siacTMovgestT = siacRPredocMovgestT.getSiacTMovgestT();			
				
				if(siacTMovgestT == null || siacTMovgestT.getSiacDMovgestTsTipo()==null){
					continue;
				}
				log.debug(methodName, "siacRSubdocClass.siacTMovgestT.uid: " + siacTMovgestT.getUid());				
				
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
			s.setNumero(new BigDecimal(siacTMovgestT.getMovgestTsCode()));		
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!",re);
		}
		
		
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				s.setStatoOperativoMovimentoGestioneSpesa(code);
				s.setDescrizioneStatoOperativoMovimentoGestioneSpesa(desc);
			}
		}
		
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
		impegno.setNumero(siacTMovgest.getMovgestNumero());
		String anno = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
		impegno.setAnnoMovimento(Integer.parseInt(anno));
		
		
		for(SiacRMovgestTsStato siacRMovgestTsStato : siacTMovgestT.getSiacRMovgestTsStatos()){
			if(siacRMovgestTsStato.getDataCancellazione()==null){
				String code = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoCode();
				String desc = siacRMovgestTsStato.getSiacDMovgestStato().getMovgestStatoDesc();
				impegno.setStatoOperativoMovimentoGestioneSpesa(code);
				impegno.setDescrizioneStatoOperativoMovimentoGestioneSpesa(desc);
			}
		}
		
		return impegno;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoSpesa src, SiacTPredoc dest) {
		
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
		
		
		dest.setSiacRPredocMovgestTs(new ArrayList<SiacRPredocMovgestT>());
		
		SiacRPredocMovgestT siacRPredocMovgestT = new SiacRPredocMovgestT();
		siacRPredocMovgestT.setSiacTPredoc(dest);
		SiacTMovgestT siacTMovgestT = new SiacTMovgestT();
		siacTMovgestT.setUid(movgestId);	
		siacRPredocMovgestT.setSiacTMovgestT(siacTMovgestT);
		
		siacRPredocMovgestT.setLoginOperazione(dest.getLoginOperazione());
		siacRPredocMovgestT.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		
		dest.addSiacRPredocMovgestT(siacRPredocMovgestT);
		
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
