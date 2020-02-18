/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRVincoloBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTBilElem;
import it.csi.siac.siacbilser.integration.entity.SiacTVincolo;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDBilElemTipoEnum;
import it.csi.siac.siacbilser.model.VincoloCapitoli;

/**
 * The Class VincoloCapitoliConverter.
 */
@Component
public class VincoloCapitoliConverter extends ExtendedDozerConverter<VincoloCapitoli, SiacTVincolo > {
	
	/**
	 * Instantiates a new vincolo capitoli converter.
	 */
	public VincoloCapitoliConverter() {
		super(VincoloCapitoli.class, SiacTVincolo.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public VincoloCapitoli convertFrom(SiacTVincolo siacTVincolo, VincoloCapitoli vincolo) {
		final String methodName = "populateAttrs";
		if(vincolo == null) {
			return vincolo;
		}
		
		log.debug(methodName, "vincolo.uid: "+ siacTVincolo.getUid());
				
		for(SiacRVincoloBilElem siacRVincoloBilElem : siacTVincolo.getSiacRVincoloBilElems()){
			if(siacRVincoloBilElem.getDataCancellazione() == null) {
				SiacTBilElem siacTBilElem = siacRVincoloBilElem.getSiacTBilElem();
				SiacDBilElemTipoEnum tipoCapitolo = SiacDBilElemTipoEnum.byCodice(siacTBilElem.getSiacDBilElemTipo().getElemTipoCode());
				
				//Se e' specificato il Bilancio filtro solo i capitoli afferenti a quel Bilancio.
				if (vincolo.getBilancio() != null && vincolo.getBilancio().getUid()!=0){
					if (vincolo.getBilancio().getUid() == siacTBilElem.getSiacTBil().getUid()) {

						// Capitolo c = (Capitolo) map(siacTBilElem, tipoCapitolo.getCapitoloClass(), BilMapId.SiacTClass_ClassificatoreGenerico);
						vincolo.addCapitolo(siacTBilElem.getUid(), tipoCapitolo.getTipoCapitolo());
					} else {
						log.debug(methodName, "scartato bilancio con uid: "+siacTBilElem.getSiacTBil().getUid());
					}
				//Se non e' specificato non filtro nulla.
				} else {
					vincolo.addCapitolo(siacTBilElem.getUid(), tipoCapitolo.getTipoCapitolo());
				}
				
			}
		}
		
		return vincolo;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTVincolo convertTo(VincoloCapitoli vincolo, SiacTVincolo dest) {	
		//Non è da implemetare.
		return dest;
	
		
	}



	

}
