/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.base;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.base.BilAsyncResponseHandler;
import it.csi.siac.siacbilser.business.service.base.ExtendedBaseService;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceRequestWrapper;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

/**
 * Classe base per il wrapper asincrono dei servizi di Elaborazione File.
 *
 * @param <RH> ResponseHandler per ElaboraFileResponse
 * @param <S> Servizio di Elaborazione del File
 * 
 * @author Domenico Lisi
 */
public abstract class ElaboraFileAsyncBaseService<RH extends BilAsyncResponseHandler<ElaboraFileResponse>, S extends ExtendedBaseService<ElaboraFile, ElaboraFileResponse>> 
	extends AsyncBaseService<ElaboraFile, ElaboraFileResponse, AsyncServiceRequestWrapper<ElaboraFile>, RH, S> {}
