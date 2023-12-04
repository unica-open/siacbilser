/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import org.springframework.stereotype.Component;

import it.csi.siac.siacbilser.integration.entity.SiacRFileStato;
import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.integration.entitymapping.BilMapId;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.StatoFile;

/**
 * The Class FileStatoConverter.
 */
@Component
public class FileStatoConverter extends ExtendedDozerConverter<File, SiacTFile> {
	
	/**
	 * Instantiates a new progressivi iva periodo converter.
	 */
	public FileStatoConverter() {
		super(File.class, SiacTFile.class);
	}

	@Override
	public File convertFrom(SiacTFile src, File dest) {
		if(src.getSiacRFileStatos() != null) {
			for(SiacRFileStato siacRFileStato : src.getSiacRFileStatos()) {
				if(siacRFileStato.getDataCancellazione() == null) {
					StatoFile statoFile = map(siacRFileStato, StatoFile.class, BilMapId.SiacDFileStato_StatoFile);
					dest.setStatoFile(statoFile);
				}
			}
		}
		return dest;
	}

	@Override
	public SiacTFile convertTo(File src, SiacTFile dest) {
		// Not now
		return dest;
	}

}
