package com.platform.parent.util;

public class Ticket {
    //{"errcode":0,"errmsg":"ok","ticket":"HoagFKDcsGMVCIY2vOjf9opXWHMLmtPFjeECNe95_MCqteAbiLCIMnv2ZMgcl1Y_U1iryNXmb_nCI1ZDBYSIjQ","expires_in":7200}
    private Long errcode;

    private String errmsg;

    private String ticket;

    private Long expires_in;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String toString()
    {
        return "errcode: " + errcode + ", errmsg: " + errmsg + ", ticket: " + ticket + ", expires_in: " + expires_in;
    }
}
