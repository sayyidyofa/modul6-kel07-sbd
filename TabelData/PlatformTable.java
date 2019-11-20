/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TabelData;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
/**
 *
 * @author Administrator
 */
public class PlatformTable extends AbstractTableModel{
    List <PlatformData> platformData = new ArrayList<>();

    @Override
    public int getRowCount() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return platformData.size();
    }

    @Override
    public int getColumnCount() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        switch(columnIndex){
        case 0: return platformData.get(rowIndex).getKodeplatform();
        case 1: return platformData.get(rowIndex).getPlatform();
        case 2: return platformData.get(rowIndex).getRilis();
        default: return null;
        }
    }
    
        @Override
    public String getColumnName(int kolom){
    switch(kolom){
        case 0: return "KODEPLATFORM";
        case 1: return "PLATFORM";
        case 2: return "RILIS";
        default: return null;
    	}
    }
    
    public void add(PlatformData a){
        platformData.add(a);
        fireTableRowsInserted(getRowCount(),getColumnCount());
    }

    public void delete (int col,int row){
        platformData.remove(col);
        fireTableRowsDeleted(col,row);
    }
    
    public PlatformData get(int row){
        return (PlatformData) platformData.get(row);
    }
}
