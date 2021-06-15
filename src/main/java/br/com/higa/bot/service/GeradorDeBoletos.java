package br.com.higa.bot.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Endereco;
import br.com.caelum.stella.boleto.Pagador;
import br.com.caelum.stella.boleto.bancos.BancoDoBrasil;
import br.com.caelum.stella.boleto.bancos.Bradesco;
import br.com.caelum.stella.boleto.bancos.Caixa;
import br.com.caelum.stella.boleto.bancos.Itau;
import br.com.caelum.stella.boleto.bancos.Santander;

public class GeradorDeBoletos {
    private static final String INSTRUCOES = "Instruções ( Todas as informações são de plena responsabilidade do cliente ).";
    private static final String LOCAIS_DE_PAGAMENTO = "Pagável em Qualquer Banco até o Vencimento";
    private static final Integer MOEDA = 9;

    private String nomeBanco;
	private LocalDate vencimentoBoleto;
	
	public LocalDate getVencimentoBoleto() {
		return vencimentoBoleto;
	}

    public GeradorDeBoletos(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public Boleto getBoleto() {

        LocalDate vencimentoBoleto = LocalDate.now().plusDays(15);
        LocalDate dataDocumentoBoleto = LocalDate.now();
        
        this.vencimentoBoleto = vencimentoBoleto;

        Datas datas = getDatas(vencimentoBoleto, dataDocumentoBoleto);

        Endereco enderecoBeneficiario = Endereco.novoEndereco().comLogradouro("Rua dos devs").comBairro("Jardim Java")
                .comCep("07112-000").comCidade("São Paulo").comUf("SP");

        Beneficiario beneficiario = getBeneficiario(enderecoBeneficiario);

        Endereco enderecoPagador = Endereco.novoEndereco().comLogradouro("Rua primavera").comBairro("Liberdade")
                .comCep("07112-001").comCidade("Rio de Janeiro").comUf("RJ");

        Pagador pagador = Pagador.novoPagador().comNome("Alex Oliveira").comDocumento("378.689.699-88")
                .comEndereco(enderecoPagador);

        Banco banco = getBanco();

        return Boleto.novoBoleto().comBanco(banco).comDatas(datas).comBeneficiario(beneficiario).comPagador(pagador)
                .comCodigoEspecieMoeda(MOEDA).comAceite(false).comQuantidadeMoeda(BigDecimal.ZERO)
                .comValorBoleto(10559).comNumeroDoDocumento("123")
                .comInstrucoes(INSTRUCOES).comLocaisDePagamento(LOCAIS_DE_PAGAMENTO).comEspecieDocumento("DM");
    }

    private Datas getDatas(LocalDate vencimentoBoleto, LocalDate dataDocumentoBoleto) {
        Datas datas = Datas.novasDatas()
                .comDocumento(dataDocumentoBoleto.getDayOfMonth(), dataDocumentoBoleto.getMonthValue(),
                        dataDocumentoBoleto.getYear())

                .comProcessamento(LocalDate.now().getDayOfMonth(), LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear())

                .comVencimento(vencimentoBoleto.getDayOfMonth(), vencimentoBoleto.getMonthValue(),
                        vencimentoBoleto.getYear());
        return datas;
    }

    private Banco getBanco() {

        if (nomeBanco.equals("itau")) {
            return new Itau();

        } else if (nomeBanco.equals("banco do brasil")) {
            return new BancoDoBrasil();

        } else if (nomeBanco.equals("santander")) {
            return new Santander();

        } else if (nomeBanco.equals("bradesco")) {
            return new Bradesco();

        } else if (nomeBanco.equals("caixa")) {
            return new Caixa();
        }

        throw new IllegalArgumentException("Banco " + nomeBanco + " não cadastrado.");
    }

    private Beneficiario getBeneficiario(Endereco enderecoBeneficiario) {

        if (nomeBanco.equals("itau")) {
            return Beneficiario.novoBeneficiario().comNomeBeneficiario("Devs SA").comAgencia("3217")
                    .comCodigoBeneficiario("22673").comDigitoCodigoBeneficiario("1")
                    .comCarteira("112").comEndereco(enderecoBeneficiario).comNossoNumero("123456")
                    .comDigitoNossoNumero("4").comDocumento("15");
        } else {
            return Beneficiario.novoBeneficiario().comNomeBeneficiario("Devs SA").comAgencia("1635")
                    .comCodigoBeneficiario("09387495").comDigitoCodigoBeneficiario("4")
                    .comCarteira("104").comEndereco(enderecoBeneficiario).comNossoNumero("123456")
                    .comDigitoNossoNumero("4").comDocumento("15");
        }

    }
}
