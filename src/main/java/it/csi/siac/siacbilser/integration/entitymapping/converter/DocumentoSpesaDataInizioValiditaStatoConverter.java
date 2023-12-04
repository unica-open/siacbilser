/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.dao.SiacRDocStatoRepository;
import it.csi.siac.siacbilser.integration.entity.SiacRDocStato;
import it.csi.siac.siacbilser.integration.entity.SiacTDoc;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;

 /**
 * The Class DocumentoSpesaDataInizioValiditaStatoConverter.
 */
@Component
public class DocumentoSpesaDataInizioValiditaStatoConverter extends ExtendedDozerConverter<DocumentoSpesa, SiacTDoc> {
	
	
	/** The siac r doc stato repository. */
    @Autowired
	private SiacRDocStatoRepository siacRDocStatoRepository;
	
	
	/**
	 * Instantiates a new documento spesa data inizio validita stato converter.
	 */
	public DocumentoSpesaDataInizioValiditaStatoConverter() {
		super(DocumentoSpesa.class, SiacTDoc.class);
	}

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertFrom(java.lang.Object, java.lang.Object)
	 */
	@Override
	public DocumentoSpesa convertFrom(SiacTDoc src, DocumentoSpesa dest) {

		List<SiacRDocStato> siacRDocStatos = siacRDocStatoRepository.findDocStatoByDocIdOrderedyByDataCreazione(src.getDocId());
		Date date = null;
		Integer statoId = null;

		for (SiacRDocStato siacRDocStato : siacRDocStatos) {
			Integer statoIdNew = siacRDocStato.getSiacDDocStato().getUid();
			if (!statoIdNew.equals(statoId)) {
				statoId = statoIdNew;
				date = siacRDocStato.getDataCreazione();
			}
		}

		dest.setDataInizioValiditaStato(date);

		return dest;
	}
	

	/* (non-Javadoc)
	 * @see org.dozer.DozerConverter#convertTo(java.lang.Object, java.lang.Object)
	 */
	@Override
	public SiacTDoc convertTo(DocumentoSpesa src, SiacTDoc dest) {
		return dest;
	}


}
