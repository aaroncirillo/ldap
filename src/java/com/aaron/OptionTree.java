/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aaron;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author aaron
 */
@ManagedBean
@RequestScoped
public class OptionTree implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    private OptionTreeNode stationRoot = new OptionTreeNode();
 
    private OptionTreeNode stationNodes = new OptionTreeNode();
 
    public OptionTreeNode getStationNodes() {
        return stationNodes;
    }
 
    private String[] kickRadioFeed = { "Hall & Oates - Kiss On My List",
 
    "David Bowie - Let's Dance",
 
    "Lyn Collins - Think (About It)",
 
    "Kim Carnes - Bette Davis Eyes",
 
    "KC & the Sunshine Band - Give It Up" };
 
 
    public OptionTree() {
        initRichFacesTree();
    }
 
    private void initRichFacesTree() {
        stationRoot.setName("root");
        stationNodes.addChild(0, stationRoot);
 
        for (int i = 0; i < kickRadioFeed.length; i++) {
            OptionTreeNode child = new OptionTreeNode();
            child.setName(kickRadioFeed[i]);
            stationRoot.addChild(i, child);
        }
 
    }
}
