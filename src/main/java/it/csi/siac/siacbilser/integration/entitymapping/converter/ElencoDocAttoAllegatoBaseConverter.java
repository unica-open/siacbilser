/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

public abstract class ElencoDocAttoAllegatoBaseConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	

	/**
	 * Instantiates a new ElencoDocumentiAllegato attr converter.
	 */
	public ElencoDocAttoAllegatoBaseConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {

		for(SiacRAttoAllegatoElencoDoc sraa : src.getSiacRAttoAllegatoElencoDocs()) {
			if(sraa.getDataCancellazione() != null) {
				continue;
			}
			
			SiacTAttoAllegato siacTAttoAllegato = sraa.getSiacTAttoAllegato();
			
			AllegatoAtto allegatoAtto = mapAllegatoAtto(siacTAttoAllegato); 
			
			dest.setAllegatoAtto(allegatoAtto);
			break;
		}

		return dest;
	}

	/**
	 * Ottengo l'allegato dall'entity. A seconda della necessit&agrave; posso tirare su diversi livelli dell'allegato atto.
	 * 
	 * @param siacTAttoAllegato l'entity
	 * @return l'allegato atto
	 */
	protected abstract AllegatoAtto mapAllegatoAtto(SiacTAttoAllegato siacTAttoAllegato);

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		
		if(src.getAllegatoAtto()!=null && src.getAllegatoAtto().getUid()!=0){
			dest.setSiacRAttoAllegatoElencoDocs(new ArrayList<SiacRAttoAllegatoElencoDoc>());
						
			SiacRAttoAllegatoElencoDoc r = new SiacRAttoAllegatoElencoDoc();
			
			SiacTAttoAllegato siacTAttoAllegato = new SiacTAttoAllegato();
			siacTAttoAllegato.setUid(src.getAllegatoAtto().getUid());
			r.setSiacTAttoAllegato(siacTAttoAllegato);
			
			r.setSiacTElencoDoc(dest);	
			
			r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			r.setLoginOperazione(dest.getLoginOperazione());
			
			dest.addSiacRAttoAllegatoElencoDoc(r);
			
		}
		return dest;
	}
	

}
