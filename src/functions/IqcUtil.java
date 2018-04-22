/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import exceptions.ResourcePropertiesException;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.IqcRecord;
import model.RawMaterial;

/**
 *
 * @author mlei
 */
public class IqcUtil {

    /*
    log:
        restore raw info, e.g. date can be put in any valid form, depending on user input
    last batch:
        formatted, esp. date, to Order.formatDate => yyMMdd
     */
    public static void initLatestProp() {
        _latest = new Properties();
        try {
            // init
            _latest.load(new FileInputStream(LATEST_IQC_BATCH_PROPERTIES));
            System.out.println("Reading latest IQC  batch properties.......");
            _latest.list(System.out);
        } catch (IOException ex) {
            Logger.getLogger(IqcUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static final String LATEST_IQC_BATCH_PROPERTIES = "src\\resources\\raw material latest batch.properties";
    public static final String DUMMY_LATEST_IQC_BATCH_PROPERTIES = "src\\resources\\demo raw material latest batch.properties";
    /*
    key = partNumber, value = batchNumber
     */
    private static Properties _latest;
    private static final HashMap<RawMaterial,IqcRecord> hm_latest=new HashMap<>();  //serves as a HIDDEN cache, invisible to outside classes
    private static final Gson gs = new Gson();

    public static Properties getLatestIqc() {
        return _latest;
    }

    public static void setLatest(Properties latest) {
        _latest = latest;
        System.out.println("new properties deployed.......");
        _latest.list(System.out);
        hm_latest.clear();
    }

    /*
    for ui
    jfileChooser: view/get, edit, save
     */
    public static void setLatestPropJson(String propName, String propValueJson) {
        _latest.setProperty(propName, propValueJson);
    }

    public static String getLatestPropJson(String propName) {
        if (null == _latest) {
            initLatestProp();
        }
        return _latest.getProperty(propName);
    }

    public static void setLatestProp(RawMaterial material, IqcRecord iqcRecord) {
        _latest.setProperty(material.partNumber, gs.toJson(iqcRecord, IqcRecord.class));
        hm_latest.put(material, iqcRecord);
    }

    public static IqcRecord getLatestProp(RawMaterial material) {
        IqcRecord iqc=hm_latest.get(material);
        if(iqc!=null) return iqc;
        if (null == _latest) {
            initLatestProp();
        }
        iqc=gs.fromJson(_latest.getProperty(material.partNumber), IqcRecord.class);
        hm_latest.put(material, iqc);
        
        return iqc;
    }

    public static String getLatestIqcBatchNumber(RawMaterial material) throws ResourcePropertiesException {
        IqcRecord iqc = getLatestProp(material);
        if (iqc != null && iqc.lastOrder != null) {
            return iqc.lastOrder.getIqcBrNumber();
        } else {
            throw new ResourcePropertiesException("latest iqc batch record");
        }
    }
}
