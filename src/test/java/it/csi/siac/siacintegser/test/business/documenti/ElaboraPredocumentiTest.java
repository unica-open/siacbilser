/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.test.business.documenti;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siaccorser.model.file.TipoFile;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileService;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileServicesEnum;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

public class ElaboraPredocumentiTest extends BaseJunit4TestCase {

	@Autowired
	private ElaboraFileService elaboraFileService;
	private static final String FILE_PATH = "docs/test/predocumenti/";
	
	@Test
	public void elaboraPredocumentiSpesa() {
		final String methodName = "elaboraPredocumentiSpesa";
		ElaboraFile req = new ElaboraFile();
		
		//req.setRichiedente(getRichiedenteByProperties("coll", "edisu"));
		req.setRichiedente(getRichiedenteByProperties("consip", "regp"));
		req.setEnte(req.getRichiedente().getAccount().getEnte());
		req.setBilancio(getBilancioTest(131, 2017));

		String fileName = "predoc_spesa_generated.txt";
		
		File file = new File();
		file.setTipo(new TipoFile(ElaboraFileServicesEnum.FLUSSO_PREDOC_SPESE.getCodice()));
		file.setNome(fileName);

		byte[] contenuto;
		try {
			contenuto = getTestFileBytes(FILE_PATH + fileName);
		} catch (IOException e) {
			log.error(methodName, "IOException in file read", e);
			fail("impossibile leggere il file di test: " + e.getMessage());
			return;
		}

		file.setContenuto(contenuto);

		req.setFile(file);
		ElaboraFileResponse res = elaboraFileService.executeService(req);

		assertNotNull(res);
	}
	
}
