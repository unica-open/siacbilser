/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDOnere;
import it.csi.siac.siacbilser.integration.entity.SiacDSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacRDocOnere;
import it.csi.siac.siacbilser.integration.entity.SiacROnereSplitreverseIvaTipo;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.enumeration.SiacDSplitreverseIvaTipoEnum;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;

/**
 * The Class SubdocumentoSpesaHasRitenuteDiverseSplitConverter.
 */
@Component
public class SubdocumentoSpesaHasRitenuteDiverseSplitConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	/**
	 * Instantiates a new subdocumento spesa has ritenute diverse split converter.
 	*/
	public SubdocumentoSpesaHasRitenuteDiverseSplitConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		if(src.getSiacTDoc() == null){
			return dest;
		}
		if(src.getSiacTDoc().getSiacRDocOneres() == null || src.getSiacTDoc().getSiacRDocOneres().isEmpty()){
			dest.setHasRitenuteDiverseSplit(Boolean.FALSE);
			return dest;
		}
		for (SiacRDocOnere r : src.getSiacTDoc().getSiacRDocOneres()) {
			if(r.getDataCancellazione()==null && r.getSiacDOnere() != null && !isSplitReverse(r.getSiacDOnere())) {
				dest.setHasRitenuteDiverseSplit(Boolean.TRUE);
				return dest;
			}
		}
		dest.setHasRitenuteDiverseSplit(Boolean.FALSE);
		return dest;
	}
	
	private boolean isSplitReverse(SiacDOnere siacDOnere) {
		if(siacDOnere.getSiacROnereSplitreverseIvaTipos() != null) {
			for(SiacROnereSplitreverseIvaTipo srosit : siacDOnere.getSiacROnereSplitreverseIvaTipos()) {
				if(srosit.getDataCancellazione() == null) {
					SiacDSplitreverseIvaTipo siacDSplitreverseIvaTipo = srosit.getSiacDSplitreverseIvaTipo();
					TipoIvaSplitReverse tipoIvaSplitReverse = SiacDSplitreverseIvaTipoEnum.byCodice(siacDSplitreverseIvaTipo.getSrivaTipoCode()).getTipoIvaSplitReverse();
					if(TipoIvaSplitReverse.SPLIT_COMMERCIALE.equals(tipoIvaSplitReverse) || (TipoIvaSplitReverse.SPLIT_ISTITUZIONALE.equals(tipoIvaSplitReverse))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		return dest;
	}

}
