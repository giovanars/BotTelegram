package br.com.higa.bot.enums;

public enum OpcoesBot {
    CEP("/cep", "envie '/cep <cep_desejado>' para receber as informacoes sobre o local consultado."),
    BOLETOS_EM_ABERTO("/boletos_em_aberto", "Retorna boletos em aberto"),
    OPT_02("/opt02", "desc deste servico."),
    OPT_03("/opt03", "desc deste servico.");

    private String nome;
    private String descServico;

    OpcoesBot(String nome, String descServico){
        this.nome = nome;
        this.descServico = descServico;
    }

    public String getNome(){
        return this.nome;
    }

    public String getDescServico(){return this.descServico;}
}
