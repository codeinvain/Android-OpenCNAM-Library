package com.tomdignan.android.opencnam.library;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.tomdignan.android.opencnam.library.ssl.SSLHelper;

/**
 * An OpenCNAMRequest that includes support for devices that don't have a
 * robust enough selection of SSL certificates to support SSL on api.opencnam.com.
 * 
 * @author Tom Dignan
 *
 */
public class SSLCertifiedOpenCNAMRequest extends OpenCNAMRequest {
	private static final String TAG = "SSLCertifiedOpenCNAMRequest";
	
	public SSLCertifiedOpenCNAMRequest(Context context) {
		super(context);
	}
	
	@Override
	protected HttpClient getHttpClient() {
		try {
			Log.e(TAG, "makeSSLFriendlyHttpClient");
			HttpClient client = SSLHelper.makeSSLFriendlyHttpClient(getContext());
			Log.e(TAG, "mHttpClient="+client.toString());
			return client;
		} catch (Exception e) {
			Log.e(TAG, "Failed to make SSL friendly HttpClient. Falling back on default. Maybe it will work.");
			e.printStackTrace();
			return new DefaultHttpClient();
		}
	}
}
