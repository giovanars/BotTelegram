package br.com.higa.bot.enums;

public enum OpcoesBot {
    CEP("/cep", "envie '/cep <cep_desejado>' para receber as informacoes sobre o local consultado."),
    OPT_01("/opt01", "desc do servico /opt01."),
    OPT_02("/opt02", "desc do servico /opt02."),
    OPT_03("/opt03", "desc do servico /opt03.");

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
