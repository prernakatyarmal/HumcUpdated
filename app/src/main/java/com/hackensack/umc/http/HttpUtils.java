package com.hackensack.umc.http;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by prerana_katyarmal on 10/16/2015.
 */
public class HttpUtils {

	private static final String TAG = "HttpUtils";
	private static final int CONNECTION_TIMEOUT_IN_MILLIS = 10000; // 10 seconds
	private static final int REQUEST_TIMEOUT_IN_MILLIS = 40000; // 40 sec
	public static final int CHUNK_SIZE = 4096;
	private static org.apache.http.conn.ssl.SSLSocketFactory sslSocketFactory = null;
	private static HttpClient httpClient = null;
	private static ThreadSafeClientConnManager cm;

	public static String getHttpGetResponse(Context mContext, String url,
			List<NameValuePair> headers) {
		String response = convertresponseInString(httpGetmethod(mContext, url,
				headers));
		return response;

	}

	public static HttpResponse httpGetmethod(Context mContext, String url,
			List<NameValuePair> headers) {
		HttpResponse httpResponse = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		if(headers!=null) {
			for (int i = 0; i < headers.size(); i++) {
				httpGet.addHeader(headers.get(i).getName(), headers.get(i)
						.getValue());
			}
		}
		try {
			httpResponse = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return httpResponse;
	}

	public static String httpPost(Context applicationCntext, String url,
			List<NameValuePair> nameValuePairs, List<NameValuePair> headers) {
		String response = null;
		try {
			response = convertresponseInString(getHttpPostResponse(
					applicationCntext, url, nameValuePairs, headers,
					new String[0]));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public static String httpPost(Context applicationCntext, String url,
			List<NameValuePair> nameValuePairs, List<NameValuePair> headers,
			String[] imagesName) {
		String response = null;
		try {
			response = convertresponseInString(getHttpPostResponse(
					applicationCntext, url, nameValuePairs, headers, imagesName));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	private static HttpResponse getHttpPostResponse(Context applicationCntext,
			String url, List<NameValuePair> nameValuePairs,
			List<NameValuePair> headers, String[] fileParameterNames)
			throws ClientProtocolException, IOException {
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);

		Log.v(TAG, "Querying: " + url + " with following parameters");
		if (nameValuePairs != null) {
			for (int index = 0; index < nameValuePairs.size(); index++) {
				Log.v(TAG, nameValuePairs.get(index).getName()
						+ "="
						+ (nameValuePairs.get(index).getValue() == null ? ""
								: nameValuePairs.get(index).getValue()));
				boolean isImage = false;
				if (fileParameterNames != null && fileParameterNames.length > 0) {
					for (String p : fileParameterNames) {
						if (nameValuePairs.get(index).getName()
								.equalsIgnoreCase(p)) {
							isImage = true;
							break;
						}
					}
				}
				if (isImage) {
					// We use FileBody to transfer the file data
					entity.addPart(nameValuePairs.get(index).getName(),
							new FileBody(new File(nameValuePairs.get(index)
									.getValue())));
				} else {
					// Normal string data
					entity.addPart(
							nameValuePairs.get(index).getName(),
							new StringBody(
									nameValuePairs.get(index).getValue() == null ? ""
											: nameValuePairs.get(index)
													.getValue()));
				}
			}
		}
		HttpClient httpClient = new DefaultHttpClient();
		//HttpClient httpClient = new

		HttpPost httppost = new HttpPost(url);
		if (nameValuePairs != null) {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		}
		for (int i = 0; i < headers.size(); i++) {
			httppost.addHeader(headers.get(i).getName(), headers.get(i)
					.getValue());
		}

		HttpResponse response = httpClient.execute(httppost);
		Log.v("Reponse is::", String.valueOf(response.getStatusLine().getStatusCode()));
		Log.v("Reponse is::", response.toString());
		return response;

	}

	public static String sendHttpPostForUsingJsonObj(String url,
			JSONObject jsonObject, List<NameValuePair> headers) {

		String strResponse = null;

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			StringEntity postingString = new StringEntity(jsonObject.toString());// convert
																					// your
																					// pojo
																					// to
																					// json
			httppost.setEntity(postingString);

			for (int i = 0; i < headers.size(); i++) {
				httppost.addHeader(headers.get(i).getName(), headers.get(i)
						.getValue());
			}

			HttpResponse response = httpclient.execute(httppost);


			strResponse = EntityUtils.toString(response.getEntity());

			Log.v(TAG,
					"***************************************************** RESPONSE 2"
							+ strResponse.toString());
			// Toast.makeText(getBaseContext(), "Sent",
			// Toast.LENGTH_SHORT).show();
		} catch (ClientProtocolException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());

			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());
			e.printStackTrace();
		}

		return strResponse;

	}
    public static HttpResponse sendHttpPostForUsingJsonObj_1(String url,
                                                     JSONObject jsonObject, List<NameValuePair> headers) {

        HttpResponse response = null;
        Log.v(TAG,
                "*****************************************************  Send Methodd");
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        try {
            StringEntity postingString = new StringEntity(jsonObject.toString());// convert
            // your
            // pojo
            // to
            // json
            httppost.setEntity(postingString);

            for (int i = 0; i < headers.size(); i++) {
                httppost.addHeader(headers.get(i).getName(), headers.get(i)
                        .getValue());
            }
             response = httpclient.execute(httppost);


            // Toast.makeText(getBaseContext(), "Sent",
            // Toast.LENGTH_SHORT).show();
        } catch (ClientProtocolException e) {
            Log.v(TAG, "*****************************************************"
                    + e.getMessage().toString());

            e.printStackTrace();
        } catch (UnknownHostException e) {
            Log.v(TAG, "*****************************************************"
                    + e.getMessage().toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG, "*****************************************************"
                    + e.getMessage().toString());
            e.printStackTrace();
        }
		return response;

    }

	public static HttpResponse sendHttpPostForUsingJsonObj(String url,
															 String data, List<NameValuePair> headers) {

		HttpResponse response = null;
		Log.v(TAG,
				"*****************************************************  Send Methodd");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			StringEntity postingString = new StringEntity(data);// convert
			// your
			// pojo
			// to
			// json
			httppost.setEntity(postingString);

			for (int i = 0; i < headers.size(); i++) {
				httppost.addHeader(headers.get(i).getName(), headers.get(i)
						.getValue());
			}
			response = httpclient.execute(httppost);


			// Toast.makeText(getBaseContext(), "Sent",
			// Toast.LENGTH_SHORT).show();
		} catch (ClientProtocolException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());

			e.printStackTrace();
		} catch (UnknownHostException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());
			e.printStackTrace();
		}
		return response;

	}

	public static HttpResponse sendHttpPutForUsingJsonObj(String url,
															 JSONObject jsonObject, List<NameValuePair> headers) {

		HttpResponse response = null;
		Log.v(TAG,
				"*****************************************************  Send Methodd");
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpPut = new HttpPut(url);

		try {
			StringEntity postingString = new StringEntity(jsonObject.toString());// convert
			// your
			// pojo
			// to
			// json
			httpPut.setEntity(postingString);

			for (int i = 0; i < headers.size(); i++) {
				httpPut.addHeader(headers.get(i).getName(), headers.get(i)
						.getValue());
			}
			response = httpclient.execute(httpPut);


			// Toast.makeText(getBaseContext(), "Sent",
			// Toast.LENGTH_SHORT).show();
		} catch (ClientProtocolException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());

			e.printStackTrace();
		} catch (UnknownHostException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());
			e.printStackTrace();
		} catch (IOException e) {
			Log.v(TAG, "*****************************************************"
					+ e.getMessage().toString());
			e.printStackTrace();
		}
		return response;

	}

	private static String convertresponseInString(HttpResponse response) {

		try {
			return EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public static void disableConnectionReuseIfNecessary() {
		// HTTP connection reuse which was buggy pre-froyo
		if (hasHttpConnectionBug()) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	public static boolean hasHttpConnectionBug() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
	}


	public static HttpResponse httpDeletemethod(Context mContext, String url,
											  List<NameValuePair> headers) {
		HttpResponse httpResponse = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(url);
		if(headers!=null) {
			for (int i = 0; i < headers.size(); i++) {
				httpDelete.addHeader(headers.get(i).getName(), headers.get(i)
						.getValue());
			}
		}
		try {
			httpResponse = httpClient.execute(httpDelete);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return httpResponse;
	}

}
