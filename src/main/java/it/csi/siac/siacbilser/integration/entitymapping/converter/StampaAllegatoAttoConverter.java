/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.entitymapping.converter;

import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siaccorser.model.file.File;

public class StampaAllegatoAttoConverter extends ExtendedDozerConverter<SiacTFile, File> {

	protected StampaAllegatoAttoConverter(Class<SiacTFile> prototypeA, Class<File> prototypeB) {
		super(prototypeA, prototypeB);
		// TODO Auto-generated constructor stub
	}

	@Override
	public File convertTo(SiacTFile paramA, File paramB) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SiacTFile convertFrom(File paramB, SiacTFile paramA) {
		// TODO Auto-generated method stub
		return null;
	}

}
