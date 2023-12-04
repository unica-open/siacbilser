/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRElencoDocSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTElencoDoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDDocFamTipoEnum;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

@Component
public class ElencoDocSubdocumentiConverter extends ExtendedDozerConverter<ElencoDocumentiAllegato, SiacTElencoDoc> {
	
	/**
	 * Instantiates a new ElencoDocumentiAllegato attr converter.
	 */
	public ElencoDocSubdocumentiConverter() {
		super(ElencoDocumentiAllegato.class, SiacTElencoDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public ElencoDocumentiAllegato convertFrom(SiacTElencoDoc src, ElencoDocumentiAllegato dest) {
		String methodName = "convertFrom";
		List<Subdocumento<?, ?>> subdocumenti = new ArrayList<Subdocumento<?, ?>>();
		if(src.getSiacRElencoDocSubdocs()!=null){
			for(SiacRElencoDocSubdoc sreds : src.getSiacRElencoDocSubdocs()){
				if(sreds.getDataCancellazione()!=null){
					continue;
				}
				
				SiacTSubdoc siacTSubdoc = sreds.getSiacTSubdoc();
				
				SiacDDocFamTipoEnum tipo = SiacDDocFamTipoEnum.byCodice(siacTSubdoc.getSiacTDoc().getSiacDDocTipo().getSiacDDocFamTipo().getDocFamTipoCode());
				log.debug(methodName, "tipo: " + tipo);
				
				if(SiacDDocFamTipoEnum.Spesa.equals(tipo) || SiacDDocFamTipoEnum.IvaSpesa.equals(tipo)){				
					SubdocumentoSpesa s = new SubdocumentoSpesa();
					map(siacTSubdoc, s, BilMapId.SiacTSubdoc_SubdocumentoSpesa_Medium);	
					subdocumenti.add(s);
				} else if(SiacDDocFamTipoEnum.Entrata.equals(tipo) || SiacDDocFamTipoEnum.IvaEntrata.equals(tipo)){			
					SubdocumentoEntrata s = new SubdocumentoEntrata();
					map(siacTSubdoc, s, BilMapId.SiacTSubdoc_SubdocumentoEntrata_Medium);	
					subdocumenti.add(s);
				}							
				
			}
		}
		dest.setSubdocumenti(subdocumenti);
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTElencoDoc convertTo(ElencoDocumentiAllegato src, SiacTElencoDoc dest) {
		dest.setSiacRElencoDocSubdocs(new ArrayList<SiacRElencoDocSubdoc>());
		
		for(Subdocumento<?,?> s : src.getSubdocumenti()) {
			SiacRElencoDocSubdoc siacRElencoDocSubdoc = new SiacRElencoDocSubdoc();
			siacRElencoDocSubdoc.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			siacRElencoDocSubdoc.setLoginOperazione(dest.getLoginOperazione());
			siacRElencoDocSubdoc.setSiacTElencoDoc(dest);
			SiacTSubdoc siacTSubdoc = new SiacTSubdoc();
			siacTSubdoc.setUid(s.getUid());
			siacRElencoDocSubdoc.setSiacTSubdoc(siacTSubdoc);
			
			dest.addSiacRElencoDocSubdoc(siacRElencoDocSubdoc);
		}
		return dest;
	}
	

}
