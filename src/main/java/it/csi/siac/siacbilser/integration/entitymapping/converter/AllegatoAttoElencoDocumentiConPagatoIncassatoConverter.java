/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRAttoAllegatoElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTAttoAllegato;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * COnverter per l'elencoDocumenti nell'AllegatoAtto
 * @author AR
 * @version 1.0.0 - 18/07/2017
 *
 */
@Component
public class AllegatoAttoElencoDocumentiConPagatoIncassatoConverter extends ExtendedDozerConverter<AllegatoAtto, SiacTAttoAllegato> {
	
	/**
	 * Instantiates a new AllegatoAtto attr converter.
	 */
	public AllegatoAttoElencoDocumentiConPagatoIncassatoConverter() {
		super(AllegatoAtto.class, SiacTAttoAllegato.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public AllegatoAtto convertFrom(SiacTAttoAllegato src, AllegatoAtto dest) {
		// Per ogni elenco, lo aggiungo all'allegato con la ricerca minima
		List<SiacRAttoAllegatoElencoDoc> siacRAttoAllegatoElencoDocs = src.getSiacRAttoAllegatoElencoDocs();
		if(siacRAttoAllegatoElencoDocs != null) {
			for(SiacRAttoAllegatoElencoDoc sraaed : siacRAttoAllegatoElencoDocs) {
				if(sraaed != null && sraaed.getDataCancellazione() == null && sraaed.getSiacTElencoDoc() != null) {
					// Converto l'oggetto
					ElencoDocumentiAllegato eda = map(sraaed.getSiacTElencoDoc(), ElencoDocumentiAllegato.class,
							BilMapId.SiacTElencoDoc_ElencoDocumentiAllegato_Base_PagatoIncassato);
					// Lo aggiungo alla lista degli elenchi
					dest.getElenchiDocumentiAllegato().add(eda);
				}
			}
		}
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTAttoAllegato convertTo(AllegatoAtto src, SiacTAttoAllegato dest) {
		
		//Inserisce l'associazione ad elenchi gia' esistenti.
		
		if(src.getElenchiDocumentiAllegato()!=null){
			dest.setSiacRAttoAllegatoElencoDocs(new ArrayList<SiacRAttoAllegatoElencoDoc>());
			
			for(ElencoDocumentiAllegato elenco : src.getElenchiDocumentiAllegato()){
				if(elenco!=null && elenco.getUid()!=0) {
					SiacRAttoAllegatoElencoDoc r = new SiacRAttoAllegatoElencoDoc();
					r.setSiacTAttoAllegato(dest);
					SiacTElencoDoc siacTElencoDoc = new SiacTElencoDoc();
					siacTElencoDoc.setUid(elenco.getUid());
					r.setSiacTElencoDoc(siacTElencoDoc);
					r.setLoginOperazione(dest.getLoginOperazione());
					r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
					
					dest.addSiacRAttoAllegatoElencoDoc(r);
				}
			}
		}
		
		return dest;
	}
	

}
