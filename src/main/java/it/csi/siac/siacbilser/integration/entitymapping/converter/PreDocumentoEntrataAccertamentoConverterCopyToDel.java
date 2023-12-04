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
import it.csi.siac.siacbilser.integration.entity.SiacRPredocMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgest;
import it.csi.siac.siacbilser.integration.entity.SiacTMovgestT;
import it.csi.siac.siacbilser.integration.entity.SiacTPredoc;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;

// TODO: Auto-generated Javadoc
/**
 * The Class PreDocumentoEntrataAccertamentoConverter.
 */
@Component
public class PreDocumentoEntrataAccertamentoConverterCopyToDel extends ExtendedDozerConverter<PreDocumentoEntrata, SiacTPredoc > {
	
	/** The log. */
	private LogSrvUtil log = new LogSrvUtil(this.getClass());
	
//	@Autowired
//	private EnumEntityFactory eef;
	
	/** The siac t movgest t repository. */
@Autowired
	private SiacTMovgestTRepository siacTMovgestTRepository;
	
	
	/**
	 * Instantiates a new pre documento entrata accertamento converter.
	 */
	public PreDocumentoEntrataAccertamentoConverterCopyToDel() {
		super(PreDocumentoEntrata.class, SiacTPredoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public PreDocumentoEntrata convertFrom(SiacTPredoc src, PreDocumentoEntrata dest) {
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
		s.setUid(siacTMovgestT.getUid());
		
		try{
			s.setNumeroBigDecimal(new BigDecimal(siacTMovgestT.getMovgestTsCode()));		
		} catch(RuntimeException re) {
			log.error(methodName, "Impssibile ottenere un BigDecimal a partire dalla stringa: \"" + siacTMovgestT.getMovgestTsCode() + "\". Returning null!",re);
		}
		
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
		Accertamento impegno = new Accertamento();
		impegno.setUid(siacTMovgest.getUid());
		impegno.setNumeroBigDecimal(siacTMovgest.getMovgestNumero());
		String anno = siacTMovgest.getSiacTBil().getSiacTPeriodo().getAnno();
		impegno.setAnnoMovimento(Integer.parseInt(anno));
		return impegno;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTPredoc convertTo(PreDocumentoEntrata src, SiacTPredoc dest) {
		
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
