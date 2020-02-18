/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRSubdocSubdocIva;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocIva;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSubdocTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;


/**
 * The Class SubdocumentoIvaSubdocumentiCollegatiConverter.
 */
@Component
public class SubdocumentoIvaSubdocumentiCollegatiConverter extends ExtendedDozerConverter<SubdocumentoIva, SiacTSubdocIva > {
	
	/**
	 * Instantiates a new subdocumento iva subdocumenti collegati converter.
	 */
	public SubdocumentoIvaSubdocumentiCollegatiConverter() {
		super(SubdocumentoIva.class, SiacTSubdocIva.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoIva convertFrom(SiacTSubdocIva siacTSubdocIva, SubdocumentoIva dest) {
		final String methodName = "convertFrom";
		
		log.debug(methodName, "dest.uid: "+ dest.getUid());
		
		if(siacTSubdocIva.getSiacRSubdocSubdocIvas()!=null) {
			for(SiacRSubdocSubdocIva r: siacTSubdocIva.getSiacRSubdocSubdocIvas()) {			
				if(r.getDataCancellazione()==null) {
					
					SiacTSubdoc siacTSubdoc = r.getSiacTSubdoc();
					String tipoCode= siacTSubdoc.getSiacDSubdocTipo().getSubdocTipoCode();
					
					if(SiacDSubdocTipoEnum.Spesa.getCodice().equals(tipoCode)){
						SubdocumentoSpesa subdocumento = mapNotNull(siacTSubdoc, SubdocumentoSpesa.class, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Base);
						dest.setSubdocumento(subdocumento);
					} else if (SiacDSubdocTipoEnum.Entrata.getCodice().equals(tipoCode)){
						SubdocumentoEntrata subdocumento = mapNotNull(siacTSubdoc, SubdocumentoEntrata.class, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Base);
						dest.setSubdocumento(subdocumento);
					} else {
						log.warn(methodName, "tipo subdocumento con codice tipo " + tipoCode + " non supportato");
					}
					
					break;
					
				}
				
			}
		}
		
		log.debug(methodName, "fine");
		
		return dest;
	}



	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdocIva convertTo(SubdocumentoIva subdoc, SiacTSubdocIva dest) {	
				
		dest.setSiacRSubdocSubdocIvas(new ArrayList<SiacRSubdocSubdocIva>());
		
		//for(Subdocumento s : subdoc.getListaSubdocumenti()){
			addSubdocumento(dest, subdoc.getSubdocumento());
		//}
		
			
		
		return dest;	
	}

	/**
	 * Adds the subdocumento.
	 *
	 * @param dest the dest
	 * @param subdoc the subdoc
	 */
	private void addSubdocumento(SiacTSubdocIva dest, Subdocumento<?, ?> subdoc) {
		
		if(subdoc==null){
			return;
		}
		
		SiacRSubdocSubdocIva r = new SiacRSubdocSubdocIva();
		r.setSiacTSubdocIva(dest);
		
		SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
		siacTSubdoc.setUid(subdoc.getUid());
		r.setSiacTSubdoc(siacTSubdoc);

		r.setLoginOperazione(dest.getLoginOperazione());
		r.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
				
		dest.addSiacRSubdocSubdocIva(r);
	}
	

	
	
	



	

}
