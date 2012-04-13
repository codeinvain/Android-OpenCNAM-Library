package com.tomdignan.android.opencnam.library.ssl;

import android.content.Context;
import android.util.Log;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import com.tomdignan.android.opencnam.library.R;

import java.io.InputStream;
import java.security.KeyStore;

/**
 * Please have a look at:
 * 
 * @see http://blog.crazybob.org/2010/02/android-trusting-ssl-certificates.html
 */
public class SSLDebugHttpClient extends DefaultHttpClient {
	private static final String TAG = "SSLDebugHttpClient";
	
	private Context context;
	private String mKeystorePassword;
	
	public SSLDebugHttpClient(Context context, String password) {
		this.context = context;
		mKeystorePassword = password;
	}

	@Override
	protected ClientConnectionManager createClientConnectionManager() {
		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		registry.register(new Scheme("https", newSslSocketFactory(), 443));
		return new SingleClientConnManager(getParams(), registry);
	}

	private SSLSocketFactory newSslSocketFactory() {
		try {
			KeyStore trusted = KeyStore.getInstance("BKS");
			InputStream in = context.getResources().openRawResource(R.raw.certstore);
			try {
				trusted.load(in, mKeystorePassword.toCharArray());
			} finally {
				in.close();
			}
			//test
			SSLSocketFactory factory = new SSLSocketFactory(trusted);
			factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			return factory;
			
		} catch (Exception e) {
			Log.e(TAG, "Caught Exception " + e.getClass().getName()
					+ " message=" + e.getMessage());
			
			throw new AssertionError(e);
		}
	}
}
