/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTBilElemRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRBilElemIvaAttivita;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTIvaAttivita;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacfin2ser.model.AttivitaIva;

/**
 * The Class AttivitaIvaCapitoloConverter.
 */
@Component
public class AttivitaIvaCapitoloConverter extends ExtendedDozerConverter<AttivitaIva, SiacTIvaAttivita > {
	

	@Autowired
	private SiacTBilElemRepository siacTBilElemRepository;
	
	/**
	 * Instantiates a new attivita iva capitolo converter.
	 */
	public AttivitaIvaCapitoloConverter() {
		super(AttivitaIva.class, SiacTIvaAttivita.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public AttivitaIva convertFrom(SiacTIvaAttivita src, AttivitaIva dest) {
		
		if(src.getSiacRBilElemIvaAttivitas()!=null){
			List<Capitolo<?, ?>> listaCapitolo = new ArrayList<Capitolo<?, ?>>();
			for(SiacRBilElemIvaAttivita siacRBilElemIvaAttivita : src.getSiacRBilElemIvaAttivitas()){
				if(siacRBilElemIvaAttivita.getDataCancellazione()!=null ){
					continue;
				}
				
				SiacTBilElem siacTBilElem = siacRBilElemIvaAttivita.getSiacTBilElem();
				
				if(siacTBilElem == null){
					continue;
				}
				
				siacTBilElem = siacTBilElemRepository.findOne(siacTBilElem.getUid());
				
				Capitolo capitolo = new Capitolo();
				mapNotNull(siacTBilElem,capitolo, BilMapId.SiacTBilElem_Capitolo_Base);		
				listaCapitolo.add(capitolo);
						
			}	
			dest.setListaCapitolo(listaCapitolo);	
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTIvaAttivita convertTo(AttivitaIva src, SiacTIvaAttivita dest) {
		
		if(src.getListaCapitolo()==null) { //facoltativo
			return dest;
		}
		List<SiacRBilElemIvaAttivita> list = new ArrayList<SiacRBilElemIvaAttivita>();
		
		for(@SuppressWarnings("rawtypes") Capitolo capitolo: src.getListaCapitolo()){
			SiacRBilElemIvaAttivita siacRBilElemIvaAttivita = new SiacRBilElemIvaAttivita();
			siacRBilElemIvaAttivita.setSiacTIvaAttivita(dest);
			
			SiacTBilElem siacTBilElem = new SiacTBilElem();
			siacTBilElem.setUid(capitolo.getUid());	
			siacRBilElemIvaAttivita.setSiacTBilElem(siacTBilElem);
			
			siacRBilElemIvaAttivita.setLoginOperazione(dest.getLoginOperazione());
			siacRBilElemIvaAttivita.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			
			list.add(siacRBilElemIvaAttivita);
		}
		
	
		dest.setSiacRBilElemIvaAttivitas(list);
		return dest;
	}


}
