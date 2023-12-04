/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import it.csi.siac.siaccorser.frontend.webservice.FileService;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaTipoFile;
import it.csi.siac.siaccorser.frontend.webservice.msg.file.RicercaTipoFileResponse;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.file.TipoFile;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FileServiceHelper extends IntegServiceHelper {

	@Autowired private FileService fileService;

	public TipoFile findTipoFileByCodice(Ente ente, Richiedente richiedente, String codice) {
		RicercaTipoFile ricercaTipoFile = new RicercaTipoFile();

		ricercaTipoFile.setEnte(ente);
		ricercaTipoFile.setRichiedente(richiedente);
		ricercaTipoFile.setCodice(codice);

		RicercaTipoFileResponse ricercaTipoFileResponse = fileService.ricercaTipoFile(ricercaTipoFile);

		checkServiceResponse(ricercaTipoFileResponse);

		return ricercaTipoFileResponse.getTipoFile();
	}
}
