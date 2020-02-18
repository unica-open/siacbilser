/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import it.csi.siac.siacbilser.business.service.base.ServiceExecutor;
import it.csi.siac.siaccorser.model.Ente;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Richiedente;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Gestisce un movimento al fine di ottenere dei dati necessari a popolare in automatico una PrimaNota.
 *
 * @author Domenico
 * @param <M> the generic type
 */
public abstract class MovimentoInventarioHandler<M extends Entita> {
	
	private static final String PATH_HANDLERS = "it.csi.siac.siacbilser.business.service.cespiti.movimentoprimanota.";
	
	protected ServiceExecutor serviceExecutor;
	protected Richiedente richiedente;
	protected Ente ente;
	protected String loginOperazione;

	/**
	 * Instantiates a new movimento handler.
	 *
	 * @param serviceExecutor the service executor
	 * @param richiedente the richiedente
	 * @param ente the ente
	 * @param bilancio the bilancio
	 */
	protected MovimentoInventarioHandler(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, String loginOperazione) {
		this.serviceExecutor = serviceExecutor;
		this.richiedente = richiedente;
		this.ente = ente;
		this.loginOperazione = loginOperazione;
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
	public static <MO extends Entita> MovimentoInventarioHandler<MO> getInstance(ServiceExecutor serviceExecutor, Richiedente richiedente, Ente ente, String movimentoClassSimpleName, String loginOperazione) {
		String movimentoHandlerClassName = null;
		
		try {
			
			movimentoHandlerClassName = PATH_HANDLERS + movimentoClassSimpleName + "MovimentoInventarioHandler";
			
			Class<MovimentoInventarioHandler<MO>>  movimentoHandlerClass = (Class<MovimentoInventarioHandler<MO>>) Class.forName(movimentoHandlerClassName);
		
			Constructor<MovimentoInventarioHandler<MO>> constructor = movimentoHandlerClass.getConstructor(ServiceExecutor.class, Richiedente.class, Ente.class, String.class);
			MovimentoInventarioHandler<MO> movimentoInventarioHandler = constructor.newInstance(serviceExecutor, richiedente, ente, loginOperazione);
			return movimentoInventarioHandler;
			
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
	 * @param primaNota the prima nota
	 */
	public abstract void caricaMovimento(PrimaNota primaNota);
	
	/**
	 * Carica i dati del Capitolo necessari per le elaborazioni delle classi di conciliazione.
	 *
	 * @param primaNota the prima nota
	 */
	public abstract void effettuaOperazioniCollegateADefinizionePrimaNotaInventario(PrimaNota primaNota);

	
}
