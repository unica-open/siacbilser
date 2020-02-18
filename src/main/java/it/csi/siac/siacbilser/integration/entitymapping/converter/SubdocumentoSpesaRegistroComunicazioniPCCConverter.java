/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacTRegistroPccRepository;
import it.csi.siac.siacbilser.integration.entity.SiacTRegistroPcc;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * The Class SubdocumentoSpesaRegistroComunicazioniPCCConverter.
 */
@Component
public class SubdocumentoSpesaRegistroComunicazioniPCCConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc> {

	@Autowired
	private SiacTRegistroPccRepository siacTRegistroPccRepository;
	
	/**
	 * Instantiates a new pre documento spesa sub documento converter.
 	*/
	public SubdocumentoSpesaRegistroComunicazioniPCCConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		
		List<SiacTRegistroPcc> siacTRegistroPccs = siacTRegistroPccRepository.findBySubdocIdAndEnteProprietarioId(src.getSubdocId(), src.getSiacTEnteProprietario().getEnteProprietarioId());
		
		List<RegistroComunicazioniPCC> listaRegistriComunicazioniPCC = new ArrayList<RegistroComunicazioniPCC>();
		for(SiacTRegistroPcc siacTRegistroPcc : siacTRegistroPccs){
			// Imposto solo l'uid
			RegistroComunicazioniPCC registroComunicazioniPCC = new RegistroComunicazioniPCC();
			registroComunicazioniPCC.setUid(siacTRegistroPcc.getUid());
			listaRegistriComunicazioniPCC.add(registroComunicazioniPCC);
		}
		dest.setListaRegistriComunicazioniPCC(listaRegistriComunicazioniPCC);
		
		return dest;
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		List<SiacTRegistroPcc> siacTRegistroPccs = new ArrayList<SiacTRegistroPcc>();
		if(src.getListaRegistriComunicazioniPCC() != null) {
			for(RegistroComunicazioniPCC rcpcc : src.getListaRegistriComunicazioniPCC()) {
				if(rcpcc == null || rcpcc.getUid() == 0) {
					continue;
				}
				SiacTRegistroPcc strpcc = new SiacTRegistroPcc();
				strpcc.setUid(rcpcc.getUid());
				strpcc.setSiacTSubdoc(dest);
				siacTRegistroPccs.add(strpcc);
			}
		}
		dest.setSiacTRegistroPccs(siacTRegistroPccs);
		return dest;
	}

}
