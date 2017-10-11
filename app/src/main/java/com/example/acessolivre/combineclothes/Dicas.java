package com.example.acessolivre.combineclothes;

/**
 * Created by Projeto on 04/10/2017.
 */

public class Dicas {

    private String estilo;
    private int imagem;

    public Dicas(String estilo, int imagem){
        this.estilo = estilo;
        this.imagem = imagem;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;

    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}
