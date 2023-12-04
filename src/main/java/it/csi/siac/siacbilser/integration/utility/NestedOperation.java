/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.integration.utility;

/**
 * Operazione da eseguire in una nested transaction.
 * @author Domenico
 *
 */
public interface NestedOperation {
	
	/**
	 * Operazione da eseguire.
	 * Quando si solleva eccezione vengono rollbaccate solo le operazioni effettuate in questo metodo senza modificare lo stato della transazione padre.
	 * 
	 * @throws Exception
	 */
	public void performOperation() throws Exception;

}
