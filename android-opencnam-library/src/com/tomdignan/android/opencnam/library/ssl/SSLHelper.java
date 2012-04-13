package com.tomdignan.android.opencnam.library.ssl;

import java.io.InputStream;
import java.security.KeyStore;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import com.tomdignan.android.opencnam.library.R;
import android.content.Context;

/**
 * Code taken from
 * 
 * http://stackoverflow.com/questions/2642777/trusting-all-certificates-using-
 * httpclient-over-https/6378872#6378872
 * 
 * NOTE: Contrary to the title above, this solution does NOT trust ALL
 * certificates. It only trusts the certificate that is in
 * res/raw/certstore.bks. *
 */
public class SSLHelper {
	private static final String KEYSTORE_PASSWORD = "abc123";

	public static HttpClient makeSSLFriendlyHttpClient(Context context)
			throws Exception {
		// I think there is something wrong with opencnam's ssl cert, but
		// cant figure it out. will do a test case tomorrow with my own certificates.
		return makeInsecureHttpClient();
		//return makeDebugHttpClient(context);
		// return makeAdditionalSSLCertsHttpClient(context);
	}

	/**
	 * This is a last resort. I am starting to think SSL implementations are not
	 * very consistent across different android devices.
	 * 
	 * insecure code taken from
	 * http://stackoverflow.com/questions/2642777/trusting
	 * -all-certificates-using-httpclient-over-https
	 */
	public static HttpClient makeInsecureHttpClient() throws Exception {
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory socketFactory = new InsecureSSLSocketFactory(trustStore);
        socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", socketFactory, 443));

        ClientConnectionManager clientConnMgr = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(clientConnMgr, params);
	}

	public static HttpClient makeDebugHttpClient(Context context) {
		return new SSLDebugHttpClient(context, KEYSTORE_PASSWORD);
	}

	public static HttpClient makeAdditionalSSLCertsHttpClient(Context context)
			throws Exception {
		final SchemeRegistry schemeRegistry = new SchemeRegistry();

		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));

		schemeRegistry.register(new Scheme("https",
				createAdditionalCertsSSLSocketFactory(context), 443));

		// and then however you create your connection manager, I use
		// ThreadSafeClientConnManager
		final HttpParams params = new BasicHttpParams();
		final ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(
				params, schemeRegistry);
		HttpClient client = new DefaultHttpClient(connectionManager, params);
		return client;
	}

	protected static SSLSocketFactory createAdditionalCertsSSLSocketFactory(
			Context context) throws Exception {

		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream input = context.getResources().openRawResource(
				R.raw.certstore);

		try {
			// don't forget to put the password used above in
			// strings.xml/mystore_password
			keystore.load(input, KEYSTORE_PASSWORD.toCharArray());
		} finally {
			input.close();
		}

		return new AdditionalKeyStoresSSLSocketFactory(keystore);
	}
}
