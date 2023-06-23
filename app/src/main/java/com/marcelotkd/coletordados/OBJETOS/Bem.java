package com.marcelotkd.coletordados.OBJETOS;

public class Bem {

    Integer ID;
    String CODIGO;
    String DESCRICAO;
    String LOCALIZACAO;
    String ESTADO;
    String SITUACAO;

    public Integer getID() {
        return ID;
    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public String getDESCRICAO() {
        return DESCRICAO;
    }

    public void setDESCRICAO(String DESCRICAO) {
        this.DESCRICAO = DESCRICAO;
    }

    public String getLOCALIZACAO() {
        return LOCALIZACAO;
    }

    public void setLOCALIZACAO(String LOCALIZACAO) {
        this.LOCALIZACAO = LOCALIZACAO;
    }

    public String getESTADO() {
        return ESTADO;
    }

    public void setESTADO(String ESTADO) {
        this.ESTADO = ESTADO;
    }

    public String getSITUACAO() {
        return SITUACAO;
    }

    public void setSITUACAO(String SITUACAO) {
        this.SITUACAO = SITUACAO;
    }

}
