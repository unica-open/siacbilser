/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.capitoli;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.previsioneimpegnatoaccertato.InseriscePrevisioneImpegnatoAccertatoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.InseriscePrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.frontend.webservice.msg.InseriscePrevisioneImpegnatoAccertatoResponse;
import it.csi.siac.siacbilser.integration.dad.PrevisioneImpegnatoAccertatoDad;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siaccommonser.business.service.base.ResponseHandler;
import it.csi.siac.siaccommonser.business.service.base.exception.BusinessException;
import it.csi.siac.siaccommonser.business.service.base.exception.ServiceParamError;
import it.csi.siac.siaccorser.model.TipologiaGestioneLivelli;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacintegser.business.service.base.ElaboraFileBaseService;
import it.csi.siac.siacintegser.business.service.capitoli.util.FilePrevisioneImpegnatoAccertatoHandler;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFile;
import it.csi.siac.siacintegser.frontend.webservice.msg.ElaboraFileResponse;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Primary
public class ElaboraFilePrevisioneImpegnatoAccertatoService extends ElaboraFileBaseService
{
	private FilePrevisioneImpegnatoAccertatoHandler filePrevisioneImpegnatoAccertatoHandler;

	private List<PrevisioneImpegnatoAccertato> elencoPrevisioneImpegnatoAccertato;
	
	@Autowired
	private PrevisioneImpegnatoAccertatoDad previsioneImpegnatoAccertatoDad;

	@Override
	public void checkServiceParam() throws ServiceParamError
	{
		super.checkServiceParam();

	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public ElaboraFileResponse executeServiceTxRequiresNew(ElaboraFile serviceRequest) {
		return super.executeServiceTxRequiresNew(serviceRequest);
	}

	@Override
	protected void init()
	{
		super.init();
		this.filePrevisioneImpegnatoAccertatoHandler = new FilePrevisioneImpegnatoAccertatoHandler();
		this.previsioneImpegnatoAccertatoDad.setEnte(ente);
	}

	@Override
	protected void initFileData()
	{
		final String methodName = "initFileData";

		log.debug(methodName, "lunghezza del file: " + fileBytes.length);

		try
		{
			elencoPrevisioneImpegnatoAccertato = filePrevisioneImpegnatoAccertatoHandler
					.readElencoPrevisioneImpegnatoAccertatoDaFile(fileBytes);
		}
		catch (Exception e)
		{
			log.error(methodName, e.getMessage(), e);

			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	protected void elaborateData(){
		
		//SIAC-8418 
		checkElaborazioneUnivocaPerAnnoBilancio();
		
		for (final PrevisioneImpegnatoAccertato preevImpAcc : elencoPrevisioneImpegnatoAccertato){
			
			
			
			InseriscePrevisioneImpegnatoAccertato reqIns = new InseriscePrevisioneImpegnatoAccertato();
			reqIns.setDataOra(new Date());
			reqIns.setRichiedente(req.getRichiedente());
			
			reqIns.setPrevisioneImpegnatoAccertatoSuCapitolo(preevImpAcc);
			

			serviceExecutor.executeServiceTxRequiresNew(InseriscePrevisioneImpegnatoAccertatoService.class, reqIns,
					new ResponseHandler<InseriscePrevisioneImpegnatoAccertatoResponse>(){
						@Override
						protected void handleResponse(InseriscePrevisioneImpegnatoAccertatoResponse resIns){
							res.addMessaggi(resIns.getMessaggi());
							res.addErrori(resIns.getErrori());

							StringBuilder descCapitolo = new StringBuilder();
							Capitolo<?, ?> capitolo = resIns.getPrevisioneImpegnatoAccertatoSuCapitolo().getCapitolo();
							if(capitolo != null) {
								if(capitolo.getTipoCapitolo() != null) {
									descCapitolo.append(TipoCapitolo.CAPITOLO_ENTRATA_GESTIONE.equals(capitolo.getTipoCapitolo()) ? " entrata "  
											:TipoCapitolo.CAPITOLO_USCITA_GESTIONE.equals(capitolo.getTipoCapitolo()) ? " spesa " : " [] "  );
								}
								
								descCapitolo.append(capitolo.getAnnoCapitolo() != null? capitolo.getAnnoCapitolo() : "null").append("/")
									.append(capitolo.getNumeroCapitolo() != null? capitolo.getNumeroCapitolo() : "null").append("/")
									.append(capitolo.getNumeroArticolo() != null? capitolo.getNumeroArticolo() : "null").append("/");
							}
							
							
							if (resIns.isFallimento() || resIns.hasErrori()){
								res.addErrore(ErroreCore.OPERAZIONE_ABBANDONATA
										.getErrore(String.format("predisposizione capitolo %s per la previsione di impegnato/accertato",
												descCapitolo)));
							}else {
								
								res.addMessaggio("CRU_CON_001", "capitolo "  + descCapitolo + " correttamente predisposto per la previsione di impegnato o accertato.");
							}
							
						}
					});
		}
	}

	protected void checkElaborazioneUnivocaPerAnnoBilancio() {
		String livello = getGestioneLivello(TipologiaGestioneLivelli.UPLOAD_MULTIPLI_PREVISIONE_CHIUDERE);
		boolean inserimentoMultiploConsentito = "TRUE".equals(livello);
		if(inserimentoMultiploConsentito) {
			return;
		}
		List<Integer> anniEsercizioPresentiSuExcel = filePrevisioneImpegnatoAccertatoHandler.getAnniEsercizioPresentiSuExcel();
		for (Integer anno : anniEsercizioPresentiSuExcel) {
			boolean hasRecordConStessoAnnoBilancio = previsioneImpegnatoAccertatoDad.hasRecordConStessoAnnoBilancio(anno.toString());
			if(hasRecordConStessoAnnoBilancio) {
				throw new BusinessException(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impossibile elaborare il file, esiste gia un caricamento per questa tipologia."));
			}
		}
		
		
	}

}
