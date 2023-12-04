/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.primanota.movimento;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Gestisce un movimento al fine di ottenere dei dati necessari a popolare in automatico una PrimaNota.
 *
 * @author Domenico
 * @param <M> the generic type
 */
public abstract class MovimentoHandler<M extends Entita> {
	
	protected ServiceExecutor serviceExecutor;
	protected Richiedente richiedente;
	protected Ente ente;
	protected Bilancio bilancio;

	/**
	 * Instantiates a new movimento handler.
	 *
	 * @param serviceExecutor the service executor
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 */
	protected MovimentoHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio) {
		this.serviceExecutor = serviceExecutor;
		this.richiedente = richiedente;
		this.ente = ente;
		this.bilancio = bilancio;
	}
	
	/**
	 * Gets the single instance of MovimentoHandler.
	 *
	 * @param <MO> the generic type
	 * @param serviceExecutor the service executor
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 * @param movimentoClass the movimento class
	 * @return single instance of MovimentoHandler
	 */
	@SuppressWarnings("unchecked")
	public static <MO extends Entita, MOC extends Entita> MovimentoHandler<MO> getInstance(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, Bilancio bilancio, Class<MO> movimentoClass, Class<MOC> movimentoClassCollegato) {
		String movimentoHandlerClassName = null;
		
		try {
			
			String movimentoClassSimpleName = movimentoClass.getSimpleName();
			String movimentoClassSimpleName2 = movimentoClassCollegato!=null?movimentoClassCollegato.getSimpleName():"";
			movimentoHandlerClassName = "it.csi.siac.siacbilser.business.service.primanota.movimento." + movimentoClassSimpleName + movimentoClassSimpleName2 + "MovimentoHandler";
			
			Class<MovimentoHandler<MO>>  movimentoHandlerClass = (Class<MovimentoHandler<MO>>) Class.forName(movimentoHandlerClassName);
		
			Constructor<MovimentoHandler<MO>> constructor = movimentoHandlerClass.getConstructor(ServiceExecutor.class, Richiedente.class, Ente.class, Bilancio.class);
			MovimentoHandler<MO> movimentoHandler = constructor.newInstance(serviceExecutor, richiedente, ente, bilancio);
			return movimentoHandler;
			
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Controllare se è presente la Classe): "+ movimentoHandlerClassName, e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Impossibile accedere al Costruttore della Classe): "+ movimentoHandlerClassName, e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Impossibile trovare il metodo Costruttore della Classe): "+ movimentoHandlerClassName, e);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Argomenti non validi per il Costruttore della Classe): "+ movimentoHandlerClassName, e);
		} catch (InstantiationException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Errore di istanziamento della Classe): "+ movimentoHandlerClassName, e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Impossibile accedere al Costruttore della Classe): "+ movimentoHandlerClassName, e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler (Errore nell'esecuzione del Costruttore della Classe. Verificare l'implementazione del costruttore.): "+ movimentoHandlerClassName, e);
		} catch (Exception e){
			throw new IllegalArgumentException("Impossibile istanziare MovimentoHandler: "+ movimentoHandlerClassName, e);
		}
	}

	/**
	 * Carica tutti i dati del Movimento necessari per le elaborazioni successive;.
	 *
	 * @param registrazioneMovFin the registrazione mov fin
	 */
	public abstract void caricaMovimento(RegistrazioneMovFin registrazioneMovFin);
	
	/**
	 * Carica i dati del Capitolo necessari per le elaborazioni delle classi di conciliazione.
	 *
	 * @param registrazioneMovFin the registrazione mov fin
	 */
	public abstract void caricaCapitolo(RegistrazioneMovFin registrazioneMovFin);

	/**
	 * Inizializza dati che necessitano di essere calcolati per le elaborazioni successive.
	 *
	 * @param registrazioniMovFin the registrazioni mov fin
	 */
	public abstract void inizializzaDatiMovimenti(List<RegistrazioneMovFin> registrazioniMovFin);

	/**
	 * Imposta importi lista movimenti dettaglio.
	 *
	 * @param movimentoEP the movimento ep
	 */
	public abstract void impostaImportiListaMovimentiDettaglio(MovimentoEP movimentoEP);
	
	/**
	 * Ottieni la descrizione da impostare nella PrimaNota che si sta crando.
	 *
	 * @param movimento the movimento
	 * @return the string
	 */
	public abstract String ottieniDescrizionePrimaNota(RegistrazioneMovFin registrazioniMovFin);
	
	/**
	 * Ottieni la descrizione da associare al MovimentoEP a partire dal Movimento.
	 *
	 * @param movimento the movimento
	 * @return the string
	 */
	public abstract String ottieniDescrizioneMovimentoEP(RegistrazioneMovFin registrazioniMovFin);
	
	/**
	 * Ottiene il soggetto legato al Movimento
	 *
	 * @param movimento the movimento
	 * @return the soggetto
	 */
	public abstract Soggetto getSoggetto(RegistrazioneMovFin registrazioniMovFin);

	/**
	 * Ottiene il capitolo legato al Movimento
	 *
	 * @param movimento the movimento
	 * @return the capitolo
	 */
	public abstract Capitolo<?, ?> getCapitolo(RegistrazioneMovFin registrazioneMovFin);

	/**
	 * Ottiene l'importo del movimento finanziario associato alla registrazione.
	 * @param registrazioneMovFin
	 * @return
	 */
	public abstract BigDecimal getImportoMovimento(RegistrazioneMovFin registrazioneMovFin);
	
}
