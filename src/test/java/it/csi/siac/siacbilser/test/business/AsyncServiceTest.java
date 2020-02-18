/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilser.test.business;

import it.csi.siac.siacbilser.test.BaseJunit4TestCase;

/**
 * The Class AsyncServiceTest.
 */
public class AsyncServiceTest extends BaseJunit4TestCase
{
	
//	@Autowired
//	private CapitoloService capitoloService;	
//	
//	final long waitMills = 1000*60*35;
//
//	/**
//	 * Test create.
//	 *
//	 * @throws Throwable the throwable
//	 */
//	@Test
//	public void testAsync() throws Throwable
//	{
//		long time = System.currentTimeMillis();
//		
//		log.debug("punto1");
//		Response<String> response = capitoloService.greetingAsync(waitMills);
//		
//		
//		log.debug("punto2");
//		
//		while(!response.isDone()){
//			log.debug("isDone?" +response.isDone());	
//			Thread.sleep(1000*60);
//		}
//		
//		String result = response.get();
//		
//		log.debug("punto3 "+result);
//				
//		long elapsed = System.currentTimeMillis() - time;
//		log.debug("elapsed: "+ elapsed + " expected: "+waitMills);
//	
//	}
//	
//	
//	@Test
//	public void testAsync2() throws Throwable
//	{
//		final long time = System.currentTimeMillis();
//		
//		 AsyncHandler asyncHandler = new AsyncHandler() {
//	         public void handleResponse(Response response)
//	         {
//	           
//	            String result = null;
//				try {
//					result = (String) response.get();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (ExecutionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	            
//				
//				log.debug("punto3 (handler) "+result);
//				
//
//				long elapsed = time-System.currentTimeMillis();
//				log.debug("elapsed: "+ elapsed + " expected: "+waitMills);
//	            
//	          
//	         }
//	      };
//		
//		
//		log.debug("punto1");
//		Future<String> response = capitoloService.greetingAsync(waitMills, asyncHandler);
//		
//		
//		log.debug("punto2");
//		
//		for(int i = 0; i<5; i++){
//			log.debug("isDone?" +response.isDone());	
//			Thread.sleep(1000);
//		}
//		
//		
//
//		String result = response.get();		
//		log.debug("punto3 (thread) "+result);
//		
//	
//	
//	}

	
	

	
	
		
}
