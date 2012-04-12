package com.tomdignan.android.opencnam.library;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class BasicOpenCNAMRequest extends OpenCNAMRequest {
	@Override
	protected HttpClient getHttpClient() {
		return new DefaultHttpClient();
	}
}
