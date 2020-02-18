/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacintegser.business.service.predocumenti.util.soggetto.handler;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.siac.siacbilser.business.service.base.AsyncBaseService;
import it.csi.siac.siaccommonser.business.service.base.exception.ExecuteExternalServiceException;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.InserisceSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggetti;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettiResponse;
import it.csi.siac.siacfinser.integration.entity.SiacTComuneFin;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggetto;
import it.csi.siac.siacfinser.model.soggetto.IndirizzoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacintegser.business.service.predocumenti.model.Predocumento;
import it.csi.siac.siacintegser.business.service.predocumenti.util.SoggettoPredocumentoException;

public abstract class BaseSoggettoHandler<P extends Predocumento> implements SoggettoHandler<P>
{
	@Autowired
	protected transient SoggettoService soggettoService;

	protected EntityManager entityManager;

	protected Ente ente;
	protected Richiedente richiedente;

	public BaseSoggettoHandler()
	{
	}

	public void init(Ente ente, Richiedente richiedente, EntityManager entityManager)
	{
		this.ente = ente;
		this.richiedente = richiedente;
		this.entityManager = entityManager;
	}

	@Override
	public Soggetto ricercaSoggetto(P predocumento) throws SoggettoPredocumentoException
	{
		RicercaSoggetti ricercaSoggetti = new RicercaSoggetti();

		ricercaSoggetti.setEnte(ente);
		ricercaSoggetti.setRichiedente(richiedente);
		ParametroRicercaSoggetto parametroRicercaSoggetto = buildParametroRicercaSoggetto(predocumento);

		ricercaSoggetti.setParametroRicercaSoggetto(parametroRicercaSoggetto);

		RicercaSoggettiResponse ricercaSoggettiResponse = soggettoService.ricercaSoggetti(ricercaSoggetti);

		checkServiceResponseFallimento(ricercaSoggettiResponse);

		if (ricercaSoggettiResponse.getSoggetti() == null || ricercaSoggettiResponse.getSoggetti().isEmpty())
			return null;

		if (ricercaSoggettiResponse.getSoggetti().size() != 1)
			throw new SoggettoPredocumentoException("Trovati piu' soggetti per il CF / PI " + predocumento.getCodiceFiscale());

		return ricercaSoggettiResponse.getSoggetti().get(0);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW, timeout=AsyncBaseService.TIMEOUT, rollbackFor={SoggettoPredocumentoException.class})
	public Soggetto inserisciSoggetto(P predocumento) throws SoggettoPredocumentoException
	{
		try {
			Soggetto soggetto = buildSoggettoInserimento(predocumento);
	
			InserisceSoggetto inserisceSoggetto = new InserisceSoggetto();
			inserisceSoggetto.setSoggetto(soggetto);
			inserisceSoggetto.setEnte(ente);
			inserisceSoggetto.setRichiedente(richiedente);
	
			InserisceSoggettoResponse inserisceSoggettoResponse = soggettoService.inserisceSoggetto(inserisceSoggetto);
	
			checkServiceResponseFallimento(inserisceSoggettoResponse);
			
			Soggetto soggettoNew = inserisceSoggettoResponse.getSoggetto();
			
			soggetto.setUid(soggettoNew.getUid());
			soggetto.setCodiceSoggetto(soggettoNew.getCodiceSoggetto());
			soggetto.setStatoOperativo(soggettoNew.getStatoOperativo());
	
			return soggetto;
		} catch (SoggettoPredocumentoException spe) {
			throw spe;
		} catch (Exception e) {
			throw new SoggettoPredocumentoException(e);
		}
	}


	protected abstract Soggetto buildSoggettoInserimento(P predocumento) throws SoggettoPredocumentoException;

	protected List<IndirizzoSoggetto> buildIndirizziSoggettoInserimento(P predocumento) throws SoggettoPredocumentoException
	{
		SiacTComuneFin siacTComune = getSiacTComune(predocumento.getComuneIndirizzo(), ente.getUid());
		
		if (siacTComune == null) {
			return null;
		}

		IndirizzoSoggetto indirizzoSoggetto = new IndirizzoSoggetto();

		indirizzoSoggetto.setComune(predocumento.getComuneIndirizzo());
		indirizzoSoggetto.setPrincipale(Boolean.TRUE.toString());
		indirizzoSoggetto.setSedime("");
		indirizzoSoggetto.setDenominazione(predocumento.getIndirizzo());
		indirizzoSoggetto.setProvincia(predocumento.getSiglaProvincia());
		indirizzoSoggetto.setIdTipoIndirizzo("DOMICILIO");
		
		indirizzoSoggetto.setComune(siacTComune.getComuneDesc());
		indirizzoSoggetto.setCodiceNazione(siacTComune.getSiacTNazione().getNazioneCode());

		if ("1".equals(indirizzoSoggetto.getCodiceNazione())) {
			indirizzoSoggetto.setIdComune(siacTComune.getComuneIstatCode());
		}
		
		List<IndirizzoSoggetto> indirizzi = new ArrayList<IndirizzoSoggetto>();
		
		indirizzi.add(indirizzoSoggetto);
		
		return indirizzi;
	}
	
	
	protected SiacTComuneFin getSiacTComune(String comune, Integer idEnte) throws SoggettoPredocumentoException
	{
		Query q = entityManager.createNativeQuery(
				"SELECT * FROM siac_t_comune WHERE UPPER(translate(comune_desc, 'àèéìòù-''', 'aeeiou')) = UPPER(translate(:comune_desc, 'àèéìòù''', 'aeeiou')) "
						+ " and ente_proprietario_id=:ente_proprietario_id AND data_cancellazione IS NULL ",
				SiacTComuneFin.class);

		q.setParameter("comune_desc", comune);
		q.setParameter("ente_proprietario_id", idEnte);

		List<SiacTComuneFin> l = q.getResultList();

		if (l.isEmpty())
			return null;

		if (l.size() > 1)
			throw new SoggettoPredocumentoException("Trovati piu' record per il comune " + comune);

		return l.get(0);
	}

	protected <ERES extends ServiceResponse> void checkServiceResponseFallimento(ERES externalServiceResponse) 
			throws SoggettoPredocumentoException
	{
		if (externalServiceResponse.isFallimento())
		{
			String externalServiceName = getServiceName(externalServiceResponse);

			throw new SoggettoPredocumentoException(new ExecuteExternalServiceException(
					"\nEsecuzione servizio interno " + externalServiceName + " richiamato da "
							+ this.getClass().getSimpleName() + " terminata con esito Fallimento."
							+ "\nErrori riscontrati da " + externalServiceName + ": {"
							+ externalServiceResponse.getDescrizioneErrori().replaceAll("\n", "\n\t") + "}.",
					externalServiceResponse.getErrori()));
		}
	}

	private <ERES extends ServiceResponse> String getServiceName(ERES externalServiceResponse)
	{
		return externalServiceResponse.getClass().getSimpleName().replaceAll("(Response)$", "") + "Service";
	}

	protected abstract ParametroRicercaSoggetto buildParametroRicercaSoggetto(P predocumento);

}
