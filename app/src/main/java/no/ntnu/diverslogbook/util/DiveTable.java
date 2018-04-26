package no.ntnu.diverslogbook.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

import no.ntnu.diverslogbook.util.table.TableWrapper;

public class DiveTable {

    public enum Table {
        PADI_IMPERIAL,
        PADI_METRIC,
        NDF_IMPARIAL,
        NDF_METRIC
    }

    private final Table tableType;

    private final TableWrapper table = new TableWrapper();






    public DiveTable(Context context, Table tableType) {
        this.tableType = tableType;
        loadTable(context.getResources(), context.getPackageName());
    }


    private void loadTable(Resources resources, String packageName){

        final String resourceType = "raw";
        int dataID = -1;

        switch (this.tableType){
            case PADI_METRIC: break;
            case PADI_IMPERIAL: resources.getIdentifier("table_padi_v1_imperial", resourceType, packageName); break;
            case NDF_METRIC: break;
            case NDF_IMPARIAL: break;
            default:
                throw new RuntimeException("Can't load table: Undefined Table Type");
        }

        try(InputStream is = resources.openRawResource(dataID)){
            
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
