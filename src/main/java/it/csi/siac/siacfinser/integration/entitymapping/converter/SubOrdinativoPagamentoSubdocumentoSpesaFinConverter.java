/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfinser.integration.entitymapping.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entitymapping.converter.ExtendedDozerConverter;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.integration.entity.SiacRSubdocOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTOrdinativoTFin;
import it.csi.siac.siacfinser.integration.entity.SiacTSubdocFin;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativoPagamento;

@Component
public class SubOrdinativoPagamentoSubdocumentoSpesaFinConverter extends ExtendedDozerConverter<SubOrdinativoPagamento, SiacTOrdinativoTFin> {

	/**
	 * Instantiates a new sub ordinativo pagamento Subdocumento spesa fin converter.
	 */
	public SubOrdinativoPagamentoSubdocumentoSpesaFinConverter() {
		super(SubOrdinativoPagamento.class, SiacTOrdinativoTFin.class);
	}

	@Override
	public SubOrdinativoPagamento convertFrom(SiacTOrdinativoTFin src, SubOrdinativoPagamento dest) {
		if(src.getSiacRSubdocOrdinativoTs() != null) {
			for(SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTFin: src.getSiacRSubdocOrdinativoTs()) {
				if(siacRSubdocOrdinativoTFin.getDataCancellazione() == null) {
					SiacTSubdocFin siacTSubDocFin = siacRSubdocOrdinativoTFin.getSiacTSubdoc();
					
					SubdocumentoSpesa subDocumentoSpesa = mapNotNull(siacTSubDocFin,SubdocumentoSpesa.class, FinMapId.SiacTSubdocFin_SubdocumentoSpesa);
					dest.setSubDocumentoSpesa(subDocumentoSpesa);
					break;
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTOrdinativoTFin convertTo(SubOrdinativoPagamento src, SiacTOrdinativoTFin dest) {
		if(src.getSubDocumentoSpesa() == null || src.getSubDocumentoSpesa().getUid() == 0) {
			return dest;
		}
		
		SiacRSubdocOrdinativoTFin siacRSubdocOrdinativoTs = new SiacRSubdocOrdinativoTFin();
		
		SiacTSubdocFin siacTSubdocFin = new SiacTSubdocFin();
		siacTSubdocFin.setUid(src.getSubDocumentoSpesa().getUid());
		siacRSubdocOrdinativoTs.setSiacTSubdoc(siacTSubdocFin);
		siacRSubdocOrdinativoTs.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
		siacRSubdocOrdinativoTs.setSiacTOrdinativoT(dest);
		
		dest.setSiacRSubdocOrdinativoTs(new ArrayList<SiacRSubdocOrdinativoTFin>());
		dest.getSiacRSubdocOrdinativoTs().add(siacRSubdocOrdinativoTs);
		
		return dest;
	}

}
