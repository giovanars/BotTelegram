package br.com.higa.bot;

import br.com.higa.bot.enums.OpcoesBot;
import br.com.higa.bot.service.ViaCep;
import br.com.higa.bot.utils.Constants;
import com.google.gson.JsonObject;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.util.List;

import static br.com.higa.bot.utils.Constants.BOT_TOKEN;

public class Principal {
	// Criacao do bot!!!
	static TelegramBot bot = new TelegramBot(BOT_TOKEN);

	// Recebimento de mensagens
	static GetUpdatesResponse updatesResponse;

	// Envio de mensagens
	static SendResponse sendResponse;

	// Gerenciamento de acoes do chat
	static BaseResponse baseResponse;

	// contador de off-set (paginacao)
	static int offSet = 0;

	// Definicao do limite do tamanho do off-set (paginacao)
	static final int OFF_SET_LIMIT = 100;

	// ID da mensagem recebida
	static Object msgRecebidaId = null;

	// Texto da mensagem recebida
	static String msgRecebidaTxt = "";

	public static void main(String[] args) {
		ViaCep viaCep = new ViaCep();
		System.out.println("### BOT [Carlos_Evd_Higa] INICIADO ###");


		//
		// TODO: Implementar log de conversa
		//


		while (true){
			updatesResponse =  bot.execute(new GetUpdates().limit(OFF_SET_LIMIT).offset(offSet));
			List<Update> mensagensRecebidas = updatesResponse.updates();

			for (Update mensagemRecebida : mensagensRecebidas) {
				offSet = mensagemRecebida.updateId() + 1;
				msgRecebidaId = mensagemRecebida.message().chat().id();
				msgRecebidaTxt = mensagemRecebida.message().text();

				if(Constants.BOT_START.equalsIgnoreCase(msgRecebidaTxt)){
					baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
					sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, getTodasOpcoesBot()));
				} else {
					if(msgRecebidaTxt.startsWith(OpcoesBot.CEP.getNome())){
						int intAux = OpcoesBot.CEP.getNome().length();
						StringBuilder strBuiderAux = new StringBuilder(msgRecebidaTxt);
						String cep = strBuiderAux.delete(0, intAux).toString();

						if(cep.trim().matches("[0-9]{5}-[0-9]{3}") ||
								cep.trim().matches("[0-9]{8}")){
							baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));

							JsonObject jsonObject = viaCep.getEnderecoByCep(cep.trim());
							String resposta;

							if("true".equalsIgnoreCase(String.valueOf(jsonObject.get("erro")))){
								resposta = Constants.MSG_ERRO_CEP_NAO_ENCONTRADO;
							} else {
								resposta = viaCep.parseViaCepJson(jsonObject);
							}

							sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, resposta));
						} else {
							baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
							sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, Constants.MSG_ERRO_CEP_INVALIDO));
							sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, getTodasOpcoesBot()));
						}
					} else {
						baseResponse = bot.execute(criarAcao(msgRecebidaId, ChatAction.typing.name()));
						sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, Constants.MSG_ERRO_OPCAO_INVALIDA));
						sendResponse = bot.execute(criaMsgDeResposta(msgRecebidaId, getTodasOpcoesBot()));
					}
				}
			}
		}
	}

	static String getTodasOpcoesBot(){
		StringBuilder stringBuilder = new StringBuilder();
		for(OpcoesBot opcoes : OpcoesBot.values()){
			stringBuilder
					.append(opcoes.getNome())
					.append(':').append(' ')
					.append(opcoes.getDescServico())
					.append(System.getProperty("line.separator"))
					.append(System.getProperty("line.separator"));
		}
		return stringBuilder.toString();
	}

	static SendChatAction criarAcao(Object msgRecebidaId, String acao){
		return new SendChatAction(msgRecebidaId, acao);
	}

	static SendMessage criaMsgDeResposta(Object msgRecebidaId, String resposta){ return new SendMessage(msgRecebidaId, resposta); }

}
