/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business;

import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceCapitoloDiUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.VerificaEliminabilitaCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ClassificazioneCofogProgramma;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siacbilser.model.TipoFondo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaPuntualeCapitoloUPrev;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUPrev;
import it.csi.siac.siaccorser.business.BaseGestione;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

// TODO: Auto-generated Javadoc
/**
 * Servizio per la gestione della Voce di Bilancio.
 *
 * @author AR
 */
public interface GestioneCapitoloUscitaPrevisione extends BaseGestione{

	/**
	 * Inserisce capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @param tipoFondo the tipo fondo
	 * @param tipoFinanziamento the tipo finanziamento
	 * @param listaClassificatoriGenerici the lista classificatori generici
	 * @param elementoPianoDeiConti the elemento piano dei conti
	 * @param strutturaAmministrativoContabile the struttura amministrativo contabile
	 * @param classificazioneCofogProgramma the classificazione cofog programma
	 * @param importiCapitoloUP the importi capitolo up
	 * @param macroaggregato the macroaggregato
	 * @param programma the programma
	 * @return the inserisce capitolo di uscita previsione response
	 */
	public InserisceCapitoloDiUscitaPrevisioneResponse inserisceCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione, TipoFondo tipoFondo,
			TipoFinanziamento tipoFinanziamento,
			List<ClassificatoreGenerico> listaClassificatoriGenerici,
			ElementoPianoDeiConti elementoPianoDeiConti,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			ClassificazioneCofogProgramma classificazioneCofogProgramma,
			List<ImportiCapitoloUP> importiCapitoloUP, Macroaggregato macroaggregato,
			Programma programma);

	/**
	 * Ricerca sintetica capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param criteriRicerca the criteri ricerca
	 * @param parametriPaginazione the parametri paginazione
	 * @return the ricerca sintetica capitolo uscita previsione response
	 */
	public RicercaSinteticaCapitoloUscitaPrevisioneResponse ricercaSinteticaCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, RicercaSinteticaCapitoloUPrev criteriRicerca,
			ParametriPaginazione parametriPaginazione);

	/**
	 * Aggiorna capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @param tipoFondo the tipo fondo
	 * @param tipoFinanziamento the tipo finanziamento
	 * @param listaClassificatoriGenerici the lista classificatori generici
	 * @param elementoPianoDeiConti the elemento piano dei conti
	 * @param strutturaAmministrativoContabile the struttura amministrativo contabile
	 * @param classificazioneCofogProgramma the classificazione cofog programma
	 * @param listaImportiCapitoloUP the lista importi capitolo up
	 * @param macroaggregato the macroaggregato
	 * @param programma the programma
	 * @return the aggiorna capitolo di uscita previsione response
	 */
	public AggiornaCapitoloDiUscitaPrevisioneResponse aggiornaCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione, TipoFondo tipoFondo,
			TipoFinanziamento tipoFinanziamento,
			List<ClassificatoreGenerico> listaClassificatoriGenerici,
			ElementoPianoDeiConti elementoPianoDeiConti,
			StrutturaAmministrativoContabile strutturaAmministrativoContabile,
			ClassificazioneCofogProgramma classificazioneCofogProgramma,
			List<ImportiCapitoloUP> listaImportiCapitoloUP, Macroaggregato macroaggregato,
			Programma programma);

	/**
	 * Ricerca puntuale capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param criteriRicerca the criteri ricerca
	 * @return the ricerca puntuale capitolo uscita previsione response
	 */
	public RicercaPuntualeCapitoloUscitaPrevisioneResponse ricercaPuntualeCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, RicercaPuntualeCapitoloUPrev criteriRicerca);

	/**
	 * Ricerca dettaglio capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param criteriRicerca the criteri ricerca
	 * @return the ricerca dettaglio capitolo uscita previsione response
	 */
	public RicercaDettaglioCapitoloUscitaPrevisioneResponse ricercaDettaglioCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, RicercaDettaglioCapitoloUPrev criteriRicerca);

	/**
	 * Ricerca puntuale movimenti capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the ricerca puntuale movimenti capitolo uscita previsione response
	 */
	public RicercaPuntualeMovimentiCapitoloUscitaPrevisioneResponse ricercaPuntualeMovimentiCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

	/**
	 * Ricerca movimenti capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the ricerca movimenti capitolo uscita previsione response
	 */
	public RicercaMovimentiCapitoloUscitaPrevisioneResponse ricercaMovimentiCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

	/**
	 * Verifica annullabilita capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the verifica annullabilita capitolo uscita previsione response
	 */
	public VerificaAnnullabilitaCapitoloUscitaPrevisioneResponse verificaAnnullabilitaCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

	/**
	 * Annulla capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the annulla capitolo uscita previsione response
	 */
	public AnnullaCapitoloUscitaPrevisioneResponse annullaCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

	/**
	 * Verifica eliminabilita capitolo.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the verifica eliminabilita capitolo uscita previsione response
	 */
	public VerificaEliminabilitaCapitoloUscitaPrevisioneResponse verificaEliminabilitaCapitolo(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

	/**
	 * Ricerca variazioni capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the ricerca variazioni capitolo uscita previsione response
	 */
	public RicercaVariazioniCapitoloUscitaPrevisioneResponse ricercaVariazioniCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

	/**
	 * Elimina capitolo uscita previsione.
	 *
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param capitoloUscitaPrevisione the capitolo uscita previsione
	 * @return the elimina capitolo uscita previsione response
	 */
	public EliminaCapitoloUscitaPrevisioneResponse eliminaCapitoloUscitaPrevisione(
			Richiedente richiedente, Ente ente, Bilancio bilancio,
			CapitoloUscitaPrevisione capitoloUscitaPrevisione);

}
