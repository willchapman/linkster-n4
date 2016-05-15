/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package com.raxware.linkster.ui;

import com.raxware.linkster.qglobber.Globber;

import javax.baja.gx.BFont;
import javax.baja.gx.BImage;
import javax.baja.naming.BOrd;
import javax.baja.nre.annotations.NiagaraType;
import javax.baja.sys.BIcon;
import javax.baja.sys.Sys;
import javax.baja.sys.Type;
import javax.baja.ui.*;
import javax.baja.ui.enums.BHalign;
import javax.baja.ui.event.BKeyEvent;
import javax.baja.ui.list.BList;
import javax.baja.ui.pane.BEdgePane;
import javax.baja.ui.pane.BFlowPane;
import javax.baja.ui.pane.BGridPane;
import java.util.ArrayList;

/**
 * This will be the UI for a single "side" in the linkster service view.
 * One will constitute the "from" side, the other the "to" side.
 *
 * @author Will Chapman
 */

@NiagaraType
public class BLinksterSide extends BEdgePane {

    private String label;
    private BGridPane gpane;
    private BLabel titleLabel;
    private BTextField txtTemplate;
    private BTextField txtRange;
    private BTextField txtSlot;
    private BList resultList;
    private BButton compileButton;
    private BButton clearListButton;

    public BLinksterSide() {
        this("Default label");
    }

    public BLinksterSide(String label) {
        this.label = label;

        titleLabel = new BLabel(label, BFont.make("bold 12pt Times New Roman"));
        txtTemplate = new BTextField("", 50, true);
        txtRange = new BTextField("", 50, true);
        txtSlot = new BTextField("", 50, true);
        resultList = new BList();
        compileButton = new BButton();
        compileButton.setCommand(new CompileTemplateCommand(), true, true);

        clearListButton = new BButton();
        clearListButton.setCommand(new ClearListCommand(), true, true);

        BGridPane topControlsPane = new BGridPane(2);
        topControlsPane.add(null, new BLabel("Template:"));
        topControlsPane.add(null, txtTemplate);
        topControlsPane.add(null, new BLabel("Range:"));
        topControlsPane.add(null, txtRange);
        topControlsPane.add(null, new BLabel("Slot"));
        topControlsPane.add(null, txtSlot);

        topControlsPane.setStretchColumn(1);

        BGridPane gridTop = new BGridPane();
        gridTop.setColumnCount(1);
        gridTop.add(null, titleLabel); // 0
        gridTop.add(null, topControlsPane);
        setTop(gridTop);

        resultList = new BList();
        setCenter(resultList);

        BFlowPane fp = new BFlowPane(BHalign.center);
        fp.add(null, compileButton);
        fp.add(null, clearListButton);
        setBottom(fp);
    }

    public BTextField getTemplate() {
        return txtTemplate;
    }

    public BTextField getRange() {
        return txtRange;
    }

    public BTextField getSlotText() {
        return txtSlot;
    }

    public void setTemplateText(String text) {
        txtTemplate.setText(text);
    }

    public void setRangeText(String text) {
        txtRange.setText(text);
    }

    public void setSlotText(String txt) {
        txtSlot.setText(txt);
    }

    public BList getList() {
        return resultList;
    }

    /**
     * Command to clear the list
     *
     * @author Will Chapman
     */
    class ClearListCommand extends Command {

        public ClearListCommand() {
            super(
                    clearListButton,
                    "Clear",
                    BImage.make(BIcon.std("spiral.png")),
                    null,
                    "Clears the list"
            );
        }

        public CommandArtifact doInvoke() {
            resultList.getModel().removeAllItems();
            return null;
        }
    }

    /**
     * Takes the string, pattern and range to fill the listbox
     *
     * @author Will Chapman
     */
    class CompileTemplateCommand extends Command {

        public CompileTemplateCommand() {
            super(
                    compileButton,
                    "Compile",
                    BImage.make(BOrd.make("module://linkster/com/raxware/linkster/res/icons/bricks.png")),
                    BAccelerator.make(BKeyEvent.VK_F1, BKeyEvent.VK_ALT),
                    "Compiles the template list using the range"
            );
        }

        public CommandArtifact doInvoke() {
            // clear the list
            resultList.getModel().removeAllItems();

            // get the data
            String template = txtTemplate.getText();
            String[] range = convertRange(txtRange.getText());
            String slot = txtSlot.getText();

            // which do i 'explode' ?
            if (slot.indexOf("{[]}") > 0) {
                // exploding the slot
                ArrayList results = Globber.explodeNumericalSequence(slot, range);
                for (int i = 0; i < results.size(); i++) {
                    resultList.addItem(template + "." + results.get(i));
                }
            } else {
                // exploding the 'template'
                ArrayList results = Globber.explodeNumericalSequence(template, range);
                for (int i = 0; i < results.size(); i++) {
                    resultList.addItem(results.get(i) + "." + slot);
                }
            }

            // need to update				
            return null;
        }

        /**
         * Takes the rules of the range and splits it into an array
         * <p>
         * slot:/Building{[]}/Floor{[]}/VAV{[]}
         * So "1..2,1..10,1..25" will be converted into
         * Object[0] = "1..2"
         * Object[1] = "1..10"
         * Object[2] = "1..25"
         *
         * @param range
         * @return
         */
        private String[] convertRange(String range) {
            String[] test = Globber.split(range, ',', false);
            ArrayList valid = new ArrayList();

            for (int i = 0; i < test.length; i++) {
                if (validateRange(range)) {
                    valid.add(test[i]);
                }
            }

            //UGH !!
            String[] convertedArray = new String[valid.size()];
            for (int i = 0; i < valid.size(); i++) {
                convertedArray[i] = (String) valid.get(i);
            }

            return convertedArray;
        }

        /**
         * Tests for a start, end and two periods in the middle
         *
         * @param range
         * @return
         */
        private boolean validateRange(String range) {
            // need to implement
            return true;
        }
    }


    /*+ ------------ BEGIN BAJA AUTO GENERATED CODE ------------ +*/
    /*@ $com.raxware.linkster.ui.BLinksterSide(1630248303)1.0$ @*/
    /* Generated Thu Apr 17 20:40:54 EDT 2008 by Slot-o-Matic 2000 (c) Tridium, Inc. 2000 */
////////////////////////////////////////////////////////////////
// Type
////////////////////////////////////////////////////////////////
    public Type getType() {
        return TYPE;
    }

    public static final Type TYPE = Sys.loadType(BLinksterSide.class);

    /*+ ------------ END BAJA AUTO GENERATED CODE -------------- +*/
}
