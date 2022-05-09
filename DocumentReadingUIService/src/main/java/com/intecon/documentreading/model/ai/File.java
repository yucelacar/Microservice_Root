package com.intecon.documentreading.model.ai;

import java.util.List;

public class File {

    private List<Propery> properies = null;
    private List<Veriable> veriable = null;

    public List<Propery> getProperies() {
        return properies;
    }

    public void setProperies(List<Propery> properies) {
        this.properies = properies;
    }

    public List<Veriable> getVeriable() {
        return veriable;
    }

    public void setVeriable(List<Veriable> veriable) {
        this.veriable = veriable;
    }

}
