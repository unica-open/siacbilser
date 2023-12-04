/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * @author Domenico
 */
package it.csi.siac.siacbilser.test.business;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.Mapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilser.integration.entity.SiacTFile;
import it.csi.siac.siacbilser.test.BaseJunit4TestCase;
import it.csi.siac.siaccommonser.util.log.LogSrvUtil;
import it.csi.siac.siaccorser.model.file.File;

/**
 * The Class FileTest.
 */
public class FileTest extends BaseJunit4TestCase {
	
	/** The ente dad. */
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private Mapper mapper;
	
	private LogSrvUtil l = new LogSrvUtil(getClass());
	
	@Test
	public void getFileAndConvert() {
		final String methodName = "getFileAndConvert";
		SiacTFile siacTFile = em.find(SiacTFile.class, Integer.valueOf(10611));
		l.debug(methodName, "File trovato? " + (siacTFile != null));
		File file = mapper.map(siacTFile, File.class, "SiacTFile_File");
		l.debug(methodName, "Dimensione file: " + file.getDimensione());
		l.logXmlTypeObject(file, "FILE");
	}
}
