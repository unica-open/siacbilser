/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdocSospensione;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siacfin2ser.model.SospensioneSubdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaSospensioneSubdocumentoConverter.
 */
@Component
public class SubdocumentoSpesaSospensioneSubdocumentoConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {
	
	/**
	 * Instantiates a new subdocumento spesa sospensione subdocumento converter.
	 */
	public SubdocumentoSpesaSospensioneSubdocumentoConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		List<SospensioneSubdocumento> sospensioni = new ArrayList<SospensioneSubdocumento>();
		if(src.getSiacTSubdocSospensiones() != null) {
			for(SiacTSubdocSospensione tss : src.getSiacTSubdocSospensiones()) {
				if(tss.getDataCancellazione() == null) {
					SospensioneSubdocumento sospensioneSubdocumento = mapNotNull(tss, SospensioneSubdocumento.class, BilMapId.SiacTSubdocSospensione_SospensioneSubdocumento);
					sospensioni.add(sospensioneSubdocumento);
				}
			}
		}
		// Ordinamento dei dati
		Collections.sort(sospensioni, new SospensioneSubdocumentoComparator());
		dest.setSospensioni(sospensioni);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		List<SiacTSubdocSospensione> siacTSubdocSospensiones = new ArrayList<SiacTSubdocSospensione>();
		
		for(SospensioneSubdocumento ss : src.getSospensioni()) {
			// SIAC-7840
			SiacTSubdocSospensione tss = sanifyEntity(mapNotNull(ss, SiacTSubdocSospensione.class, BilMapId.SiacTSubdocSospensione_SospensioneSubdocumento));
			tss.setSiacTEnteProprietario(dest.getSiacTEnteProprietario());
			tss.setSiacTSubdoc(dest);
			// SIAC-7840
			tss.setLoginOperazione(ifNotNull(tss.getLoginOperazione(), dest.getLoginOperazione()));
			siacTSubdocSospensiones.add(tss);
		}
		dest.setSiacTSubdocSospensiones(siacTSubdocSospensiones);
		return dest;
	
	}
	
	/**
	 * Comparator per le sospensioni del subdocumento
	 * @author Marchino Alessandro
	 *
	 */
	private static class SospensioneSubdocumentoComparator implements Comparator<SospensioneSubdocumento>, Serializable {
		
		/** Per la serializzazione */
		private static final long serialVersionUID = -6417087668425609462L;

		SospensioneSubdocumentoComparator() {
			// Costruttore vuoto, ridotta visibilita'
		}

		@Override
		public int compare(SospensioneSubdocumento o1, SospensioneSubdocumento o2) {
			if(o1 == null && o2 == null) {
				return 0;
			}
			if(o1 == null) {
				return -1;
			}
			if(o2 == null) {
				return 1;
			}
			return new CompareToBuilder()
					.append(o2.getDataSospensione(), o1.getDataSospensione())
					.append(o2.getDataRiattivazione(), o1.getDataRiattivazione())
					.append(o1.getUid(), o2.getUid())
					.toComparison();
		}
		
	}

}
