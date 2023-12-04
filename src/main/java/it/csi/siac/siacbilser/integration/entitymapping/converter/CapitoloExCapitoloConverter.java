/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemRelTempo;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.Capitolo;

/**
 * The Class CapitoloExCapitoloConverter.
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CapitoloExCapitoloConverter extends ExtendedDozerConverter<Capitolo<?, ?>, SiacTBilElem> {

	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;

	/**
	 * Instantiates a new capitolo ex capitolo converter.
	 */
	@SuppressWarnings("unchecked")
	protected CapitoloExCapitoloConverter() {
		super((Class<Capitolo<?, ?>>)(Class<?>)Capitolo.class, SiacTBilElem.class);
	}	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Capitolo<?, ?> convertFrom(SiacTBilElem src, Capitolo<?, ?> dest) {
		
		
		SiacTBilElem siacTBilElemOld = getExCapitolo(src);
		
		if(siacTBilElemOld!=null) {
			dest.setUidExCapitolo(siacTBilElemOld.getUid());
			dest.setExAnnoCapitolo(Integer.parseInt(siacTBilElemOld.getSiacTBil().getSiacTPeriodo().getAnno()));
			dest.setExCapitolo(Integer.parseInt(siacTBilElemOld.getElemCode()));
			dest.setExArticolo(Integer.parseInt(siacTBilElemOld.getElemCode2()));
			dest.setExUEB(Integer.parseInt(siacTBilElemOld.getElemCode3()));
		}
		
		
//		if(src.getSiacRBilElemRelTempos()!=null){
//			for(SiacRBilElemRelTempo r : src.getSiacRBilElemRelTempos()){
//				if(r.getDataCancellazione()==null){
//					SiacTBilElem siacTBilElemOld = r.getSiacTBilElemOld();
//					dest.setUidExCapitolo(siacTBilElemOld.getUid());
//					dest.setExAnnoCapitolo(Integer.parseInt(siacTBilElemOld.getSiacTBil().getSiacTPeriodo().getAnno()));
//					dest.setExCapitolo(Integer.parseInt(siacTBilElemOld.getElemCode()));
//					dest.setExArticolo(Integer.parseInt(siacTBilElemOld.getElemCode2()));
//					dest.setExUEB(Integer.parseInt(siacTBilElemOld.getElemCode3()));
//					break;
//				}
//			}
//		}		
		
		return dest;
	}
	
	
	public SiacTBilElem getExCapitolo(SiacTBilElem siacTBilElem) {
		final String methodName = "getExCapitolo";
		
		log.debug(methodName, "capitolo.uid: "+ siacTBilElem.getUid());
		
		String elemTipoCode = siacTBilElem.getSiacDBilElemTipo().getElemTipoCode();
		SiacDBilElemTipoEnum elemTipo = SiacDBilElemTipoEnum.byCodice(elemTipoCode);
		log.debug(methodName, "elemTipo: "+ elemTipo);
		SiacDBilElemTipoEnum elemTipoEx = SiacDBilElemTipoEnum.byTipoCapitolo(elemTipo.getTipoCapitoloEx());
		log.debug(methodName, "elemTipoEx: "+ elemTipoEx);
		
		List<SiacTBilElem> bilElems = siacTBilElemRepository.findRelTempoByElemTipoCode(siacTBilElem.getUid(), elemTipoEx.getCodice());
		
		if(bilElems.isEmpty()) { // SE non è definito l'ex capitolo su SiacRBilElemRelTempo allora l'ex capitolo è l'equivalente
			bilElems = siacTBilElemRepository.findCapitoloExByIdAndTipoCode(siacTBilElem.getUid(), elemTipoEx.getCodice());
		}		
		
		if(bilElems.isEmpty()){
			return null;
		}
		
		if(bilElems.size()>1){
			log.warn(methodName, "trovato piu' di un ex capitolo per il capitolo con uid: "+ siacTBilElem.getUid());
		}
		
		return bilElems.get(0);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTBilElem convertTo(Capitolo<?, ?> src, SiacTBilElem dest) {
		
		if(src.getUidExCapitolo()!=0) {
			
			dest.setSiacRBilElemRelTempos(new ArrayList<SiacRBilElemRelTempo>());
			SiacRBilElemRelTempo r = new SiacRBilElemRelTempo();
			SiacTBilElem siacTBilElemOld = new SiacTBilElem();
			siacTBilElemOld.setUid(src.getUidExCapitolo());
			r.setSiacTBilElemOld(siacTBilElemOld);
			
			Date now = new Date();
			r.setLoginOperazione(dest.getLoginOperazione());
			r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			r.setDataCreazione(now);
			r.setDataModifica(now);
			r.setDataInizioValidita(now);
			
			dest.addSiacRBilElemRelTempos1(r);
		}
		
		
		return dest;
	}



	

}
