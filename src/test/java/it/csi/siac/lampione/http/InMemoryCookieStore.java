/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.lampione.http;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A simple in-memory java.net.CookieStore implementation
 * @author Edward Wang
 */
public class InMemoryCookieStore implements CookieStore {

	// the in-memory representation of cookies
	private List<HttpCookie> cookieJar = null;

	// the cookies are indexed by their domain and associated uri (if present)
	// CAUTION: when a cookie is removed from main data structure (i.e. cookieJar),
	// it won't be cleared in domainIndex & uriIndex. Double-check the
	// presence of cookie when retrieve one form index store.
	private Map<String, List<HttpCookie>> domainIndex = null;
	private Map<URI, List<HttpCookie>> uriIndex = null;

	// use ReentrantLock instead of syncronized for scalability
	private ReentrantLock lock = null;

	/** The default constructor */
	public InMemoryCookieStore() {
		cookieJar = new ArrayList<HttpCookie>();
		domainIndex = new HashMap<String, List<HttpCookie>>();
		uriIndex = new HashMap<URI, List<HttpCookie>>();

		lock = new ReentrantLock(false);
	}

	@Override
	public void add(URI uri, HttpCookie cookie) {
		// pre-condition : argument can't be null
		if (cookie == null) {
			throw new NullPointerException("cookie is null");
		}

		lock.lock();
		try {
			// remove the old cookie if there was one
			cookieJar.remove(cookie);

			// add new cookie if it has a non-zero max-age
			if (cookie.getMaxAge() != 0) {
				cookieJar.add(cookie);
				if (cookie.getDomain() != null) {
					// and add it to domain index
					addIndex(domainIndex, cookie.getDomain(), cookie);
				}
				if (uri != null) {
					// add it to uri index, too
					addIndex(uriIndex, getEffectiveURI(uri), cookie);
				}
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public List<HttpCookie> get(URI uri) {
		// argument can't be null
		if (uri == null) {
			throw new NullPointerException("uri is null");
		}

		List<HttpCookie> cookies = new ArrayList<HttpCookie>();
		boolean secureLink = "https".equalsIgnoreCase(uri.getScheme());
		lock.lock();
		try {
			// check domainIndex first
			getInternal1(cookies, domainIndex, uri.getHost(), secureLink);
			// check uriIndex then
			getInternal2(cookies, uriIndex, getEffectiveURI(uri), secureLink);
		} finally {
			lock.unlock();
		}

		return cookies;
	}

	@Override
	public List<HttpCookie> getCookies() {
		List<HttpCookie> rt;

		lock.lock();
		try {
			Iterator<HttpCookie> it = cookieJar.iterator();
			while (it.hasNext()) {
				if (it.next().hasExpired()) {
					it.remove();
				}
			}
		} finally {
			rt = Collections.unmodifiableList(cookieJar);
			lock.unlock();
		}

		return rt;
	}

	@Override
	public List<URI> getURIs() {
		List<URI> uris = new ArrayList<URI>();

		lock.lock();
		try {
			Iterator<URI> it = uriIndex.keySet().iterator();
			while (it.hasNext()) {
				URI uri = it.next();
				List<HttpCookie> cookies = uriIndex.get(uri);
				if (cookies == null || cookies.size() == 0) {
					// no cookies list or an empty list associated with
					// this uri entry, delete it
					it.remove();
				}
			}
		} finally {
			uris.addAll(uriIndex.keySet());
			lock.unlock();
		}

		return uris;
	}

	@Override
	public boolean remove(URI uri, HttpCookie ck) {
		// argument can't be null
		if (ck == null) {
			throw new NullPointerException("cookie is null");
		}

		boolean modified = false;
		lock.lock();
		try {
			modified = cookieJar.remove(ck);
		} finally {
			lock.unlock();
		}

		return modified;
	}

	@Override
	public boolean removeAll() {
		lock.lock();
		try {
			if (cookieJar.isEmpty()) {
				return false;
			}
			cookieJar.clear();
			domainIndex.clear();
			uriIndex.clear();
		} finally {
			lock.unlock();
		}

		return true;
	}

	/* ---------------- Private operations -------------- */

	/**
	 * This is almost the same as HttpCookie.domainMatches except for one
	 * difference: It won't reject cookies when the 'H' part of the domain contains
	 * a dot ('.'). I.E.: RFC 2965 section 3.3.2 says that if host is x.y.domain.com
	 * and the cookie domain is .domain.com, then it should be rejected. However
	 * that's not how the real world works. Browsers don't reject and some sites,
	 * like yahoo.com do actually expect these cookies to be passed along. And
	 * should be used for 'old' style cookies (aka Netscape type of cookies)
	 */
	private boolean netscapeDomainMatches(String domain, String host) {
		if (domain == null || host == null) {
			return false;
		}

		// if there's no embedded dot in domain and domain is not .local
		boolean isLocalDomain = ".local".equalsIgnoreCase(domain);
		int embeddedDotInDomain = domain.indexOf('.');
		if (embeddedDotInDomain == 0) {
			embeddedDotInDomain = domain.indexOf('.', 1);
		}
		if (!isLocalDomain && (embeddedDotInDomain == -1 || embeddedDotInDomain == domain.length() - 1)) {
			return false;
		}

		// if the host name contains no dot and the domain name is .local
		int firstDotInHost = host.indexOf('.');
		if (firstDotInHost == -1 && isLocalDomain) {
			return true;
		}

		int domainLength = domain.length();
		int lengthDiff = host.length() - domainLength;
		if (lengthDiff == 0) {
			// if the host name and the domain name are just string-compare euqal
			return host.equalsIgnoreCase(domain);
		} else if (lengthDiff > 0) {
			// need to check H & D component
			// String partH = host.substring(0, lengthDiff);
			String partD = host.substring(lengthDiff);

			return (partD.equalsIgnoreCase(domain));
		} else if (lengthDiff == -1) {
			// if domain is actually .host
			return (domain.charAt(0) == '.' && host.equalsIgnoreCase(domain.substring(1)));
		}

		return false;
	}

	private void getInternal1(List<HttpCookie> cookies, Map<String, List<HttpCookie>> cookieIndex, String host, boolean secureLink) {
		// Use a separate list to handle cookies that need to be removed so
		// that there is no conflict with iterators.
		ArrayList<HttpCookie> toRemove = new ArrayList<HttpCookie>();
		for (Map.Entry<String, List<HttpCookie>> entry : cookieIndex.entrySet()) {
			String domain = entry.getKey();
			List<HttpCookie> lst = entry.getValue();
			for (HttpCookie c : lst) {
				if ((c.getVersion() == 0 && netscapeDomainMatches(domain, host)) || (c.getVersion() == 1 && HttpCookie.domainMatches(domain, host))) {
					if ((cookieJar.indexOf(c) != -1)) {
						// the cookie still in main cookie store
						if (!c.hasExpired()) {
							// don't add twice and make sure it's the proper
							// security level
							if ((secureLink || !c.getSecure()) && !cookies.contains(c)) {
								cookies.add(c);
							}
						} else {
							toRemove.add(c);
						}
					} else {
						// the cookie has beed removed from main store,
						// so also remove it from domain indexed store
						toRemove.add(c);
					}
				}
			}
			// Clear up the cookies that need to be removed
			for (HttpCookie c : toRemove) {
				lst.remove(c);
				cookieJar.remove(c);

			}
			toRemove.clear();
		}
	}

	// @param cookies [OUT] contains the found cookies
	// @param cookieIndex the index
	// @param comparator the prediction to decide whether or not
	// a cookie in index should be returned
	private <T> void getInternal2(List<HttpCookie> cookies, Map<T, List<HttpCookie>> cookieIndex, Comparable<T> comparator, boolean secureLink) {
		for (T index : cookieIndex.keySet()) {
			if (comparator.compareTo(index) == 0) {
				List<HttpCookie> indexedCookies = cookieIndex.get(index);
				// check the list of cookies associated with this domain
				if (indexedCookies != null) {
					Iterator<HttpCookie> it = indexedCookies.iterator();
					while (it.hasNext()) {
						HttpCookie ck = it.next();
						if (cookieJar.indexOf(ck) != -1) {
							// the cookie still in main cookie store
							if (!ck.hasExpired()) {
								// don't add twice
								if ((secureLink || !ck.getSecure()) && !cookies.contains(ck))
									cookies.add(ck);
							} else {
								it.remove();
								cookieJar.remove(ck);
							}
						} else {
							// the cookie has beed removed from main store,
							// so also remove it from domain indexed store
							it.remove();
						}
					}
				}
			}
		}
	}

	// add 'cookie' indexed by 'index' into 'indexStore'
	private <T> void addIndex(Map<T, List<HttpCookie>> indexStore, T index, HttpCookie cookie) {
		if (index != null) {
			List<HttpCookie> cookies = indexStore.get(index);
			if (cookies != null) {
				// there may already have the same cookie, so remove it first
				cookies.remove(cookie);

				cookies.add(cookie);
			} else {
				cookies = new ArrayList<HttpCookie>();
				cookies.add(cookie);
				indexStore.put(index, cookies);
			}
		}
	}

	//
	// for cookie purpose, the effective uri should only be http://host
	// the path will be taken into account when path-match algorithm applied
	//
	private URI getEffectiveURI(URI uri) {
		URI effectiveURI = null;
		try {
			effectiveURI = new URI("http",
					uri.getHost(),
					// path component
					null,
					// query component
					null,
					// fragment component
					null);
		} catch (URISyntaxException ignored) {
			effectiveURI = uri;
		}

		return effectiveURI;
	}

}