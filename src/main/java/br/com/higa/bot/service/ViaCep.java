package br.com.higa.bot.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

import java.io.IOException;

import static br.com.higa.bot.utils.Constants.*;

public class ViaCep {
	public JsonObject getEnderecoByCep(String cep) {
		try{
			StringBuilder url = new StringBuilder()
					.append(URL_VIA_CEP)
					.append(cep)
					.append(JSON_RESPONSE_VIA_CEP);

			OkHttpClient client = new OkHttpClient();

			Request request =
					new Request.Builder()
							.url(url.toString())
							.build();

			ResponseBody responseBody = client.newCall(request).execute().body();
			return JsonParser.parseString(responseBody.string()).getAsJsonObject();
		} catch (IOException ioException) {
			System.out.println(ioException + " : " + MSG_ERRO_VIA_CEP_REQUEST);
			return null;
		}
	}
}
