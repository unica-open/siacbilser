/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacDNoteTesoriere;
import it.csi.siac.siacbilser.integration.entity.SiacTSubdoc;
import it.csi.siac.siacfin2ser.model.NoteTesoriere;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.integration.entity.mapping.FinMapId;

/**
 * The Class SubdocumentoSpesaNoteTesoriereConverter
 */
@Component
public class SubdocumentoSpesaNoteTesoriereConverter extends ExtendedDozerConverter<SubdocumentoSpesa, SiacTSubdoc > {

	/**
	 * Instantiates a new subdocumento spesa note tesoriere converter.
	 */
	public SubdocumentoSpesaNoteTesoriereConverter() {
		super(SubdocumentoSpesa.class, SiacTSubdoc.class);
	}

	@Override
	public SubdocumentoSpesa convertFrom(SiacTSubdoc src, SubdocumentoSpesa dest) {
		NoteTesoriere noteTesoriere = mapNotNull(src.getSiacDNoteTesoriere(), NoteTesoriere.class, FinMapId.SiacDNoteTesoriere_NoteTesoriere);
		dest.setNoteTesoriere(noteTesoriere);
		return dest;
	}

	@Override
	public SiacTSubdoc convertTo(SubdocumentoSpesa src, SiacTSubdoc dest) {
		SiacDNoteTesoriere siacDNoteTesoriere = mapNotNull(src.getNoteTesoriere(), SiacDNoteTesoriere.class, FinMapId.SiacDNoteTesoriere_NoteTesoriere);
		dest.setSiacDNoteTesoriere(siacDNoteTesoriere);
		return dest;
	}

}
