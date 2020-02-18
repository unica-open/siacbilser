/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siacbilser.business.service.stampa.base.JAXBBaseReportHandler;
import it.csi.siac.siacbilser.business.service.stampa.cassaeconomale.ricevuta.model.StampaRicevutaRendicontoRichiestaEconomaleReportModel;
import it.csi.siac.siacbilser.integration.dad.CassaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.CodificaDad;
import it.csi.siac.siacbilser.integration.dad.RendicontoRichiestaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.RichiestaEconomaleDad;
import it.csi.siac.siacbilser.integration.dad.StampeCassaFileDad;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siaccorser.frontend.webservice.msg.report.GeneraReportResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.file.File;
import it.csi.siac.siacfin2ser.model.Valuta;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class StampaRicevutaRendicontoRichiestaEconomaleReportHandler extends
		JAXBBaseReportHandler<StampaRicevutaRendicontoRichiestaEconomaleReportModel> {
	@Autowired 
	private StampeCassaFileDad stampeCassaFileDad;
	@Autowired
	private RichiestaEconomaleDad richiestaEconomaleDad;
	@Autowired
	private RendicontoRichiestaEconomaleDad rendicontoRichiestaDad;
	@Autowired
	private CassaEconomaleDad cassaEconomaleDad;
	
	@Autowired
	private CodificaDad codificaDad;
	
	// necessario per recuperare l'uid di cui fare la stampa.
	private RendicontoRichiesta rendicontoRichiesta;
	
	private RichiestaEconomale richiestaEconomale;
	
	private Bilancio bilancio;
	
	private CassaEconomale cassaEconomale;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT)
	public void elaborate() {	
		super.elaborate();
	}
	
	@Override
	protected void elaborateData() {
		setRendicontoRichiesta(rendicontoRichiestaDad.ricercaDettaglioRendicontoRichiesta(rendicontoRichiesta));
		setRichiestaEconomale(richiestaEconomaleDad.findRichiestaEconomaleByUid(this.rendicontoRichiesta.getRichiestaEconomale().getUid()));
		//TODO verificare se e' sufficiente o se e' necessario usare il dad di rendicontorichiesta x recuperare il dato
		setCassaEconomale(cassaEconomaleDad.ricercaDettaglioCassaEconomale(rendicontoRichiesta.getRichiestaEconomale().getCassaEconomale().getUid()));
		richiestaEconomale.setCassaEconomale(cassaEconomale);
		codificaDad.setEnte(richiestaEconomale.getEnte());
		Valuta valutaEuro = codificaDad.ricercaCodifica(Valuta.class, "EUR");
		result.setDenominazioneRichiedente(richiestaEconomale.getSoggetto() == null ? richiestaEconomale.getCognome() : richiestaEconomale.getSoggetto().getDenominazione());

		rendicontoRichiesta.setRichiestaEconomale(richiestaEconomale);
		computaDescrizioneModalitaPagamento();
		result.setRendicontoRichiesta(rendicontoRichiesta);
		result.setValutaDefault(valutaEuro);
		result.setDataStampa(new Date());
		
	}
	private void computaDescrizioneModalitaPagamento (){
		if (rendicontoRichiesta.getMovimento() != null) {
			String descrizione =  rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente().getDescrizione();
			if (TipoDiCassa.CONTO_CORRENTE_BANCARIO.equals(rendicontoRichiesta.getMovimento().getModalitaPagamentoCassa().getTipoDiCassa()) && rendicontoRichiesta.getMovimento().getIban()!=null){
				StringBuilder sb = new StringBuilder();
				sb.append(rendicontoRichiesta.getMovimento().getIban()); 
				sb.append(rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente().getCodice()).append(" ");
				sb.append(rendicontoRichiesta.getMovimento().getModalitaPagamentoDipendente().getDescrizione()).append(" ");
				sb.append(rendicontoRichiesta.getMovimento().getBic()).append(" ");
				sb.append(rendicontoRichiesta.getMovimento().getCin()).append(" ");
				sb.append(rendicontoRichiesta.getMovimento().getContoCorrente());
				descrizione = sb.toString();
			}
	
			result.setModalitaPagamentoPerStampa(descrizione);
		}
	}
	@Override
	public String getCodiceTemplate() {
		final String methodName = "getCodiceTemplate";
		log.debug(methodName, "Template: " + "StampaRicevutaRendiconto_" + richiestaEconomale.getTipoRichiestaEconomale().getCodice());
		return "StampaRicevutaRendicontoRichiestaEconomale_" + richiestaEconomale.getTipoRichiestaEconomale().getCodice();
	}

	@Override
	protected void handleResponse(GeneraReportResponse res) {
		
		res.getErrori();
		persistiStampaFile(res);
		
		
	}
	/*
	 * Persiste la stampa  su database.
	 * 
	 * @param res la risposta del metodo di generazione del report
	 */
	private void persistiStampaFile(GeneraReportResponse res) {
		final String methodName = "persistiStampaFile";
		log.debug(methodName, "Persistenza della stampa");
		stampeCassaFileDad.setEnte(ente);
		stampeCassaFileDad.setLoginOperazione(getRichiedente().getOperatore().getCodiceFiscale());
		StampeCassaFile stampeCF = stampeCassaFileDad.inserisciStampa(creaStampaRicevutaRendiconto(res));
	
		
		log.info(methodName, "Stampa terminata con successo. Uid record inserito su database: " + stampeCF.getUid());
	}
	
	/**
	 * Crea una StampaIva per la persistenza.
	 * 
	 * @param res la response della generazione del report
	 * @return la stampa iva creata
	 */
	private StampeCassaFile creaStampaRicevutaRendiconto(GeneraReportResponse res) {
		
		StampeCassaFile result = new StampeCassaFile();
		
		
		result.setAnnoEsercizio(getBilancio().getAnno());
		
		File file = res.getReport();
		
		result.setCodice(file.getCodice());
		
		result.setEnte(getEnte());
		
		result.setTipoStampa(TipoStampa.DEFINITIVA);
		result.setTipoDocumento(TipoDocumento.RICEVUTA);
		result.setCassaEconomale(getCassaEconomale());
		result.setBilancio(getBilancio());
		
		List<File> listaFile = new ArrayList<File>();
		
		listaFile.add(file);
		result.setFiles(listaFile);
		result.setCassaEconomale(getCassaEconomale());
		
		//salvo comuqnue la data  per poter reperire in seguito la Stampa in base al criterio Data Giornale
		// SIAC-6350: il movimento potrebbe non essere presente: gestisco il caso
		if(getRendicontoRichiesta().getMovimento() != null) {
			result.setNumeroMovimento(getRendicontoRichiesta().getMovimento().getNumeroMovimento());
		}

		return result;
	}
	

	public void setRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		this.richiestaEconomale = richiestaEconomale;
		
	}

	/**
	 * @return the bilancio
	 */
	public Bilancio getBilancio() {
		return bilancio;
	}

	/**
	 * @param bilancio the bilancio to set
	 */
	public void setBilancio(Bilancio bilancio) {
		this.bilancio = bilancio;
	}

	/**
	 * @return the cassaEconomale
	 */
	public CassaEconomale getCassaEconomale() {
		return cassaEconomale;
	}

	/**
	 * @param cassaEconomale the cassaEconomale to set
	 */
	public void setCassaEconomale(CassaEconomale cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}

	/**
	 * @return the richiestaEconomale
	 */
	public RichiestaEconomale getRichiestaEconomale() {
		return richiestaEconomale;
	}

	/**
	 * @return the rendicontoRichiesta
	 */
	public RendicontoRichiesta getRendicontoRichiesta() {
		return rendicontoRichiesta;
	}

	/**
	 * @param rendicontoRichiesta the rendicontoRichiesta to set
	 */
	public void setRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		this.rendicontoRichiesta = rendicontoRichiesta;
	}

}
