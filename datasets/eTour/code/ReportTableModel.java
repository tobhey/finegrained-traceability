package unisa.gps.etour.gui.operatoragency;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import unisa.gps.etour.bean.BeanCulturalHeritage;
import unisa.gps.etour.bean.BeanRefreshmentPoint;
import unisa.gps.etour.util.Point3D;

public class ReportTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] headers = { "Name", "Description", "Address", "City", "Province" };
    private static final Class[] columnClasses = { String.class, String.class, String.class, String.class,
            String.class };
    private Vector<Object[]> data;

    public ReportTableModel(BeanCulturalHeritage[] bc, BeanRefreshmentPoint[] pr) {
        data = new Vector<Object[]>();
        for (int i = 0; i < pr.length; i++) {
            Object[] new_ = new Object[5];
            new_[0] = pr[i].getName();
            new_[1] = pr[i].getDescription();
            new_[2] = pr[i].getStreet();
            new_[3] = pr[i].getCity();
            new_[4] = pr[i].getProvince();

            setValueAt(new_, i);
        }
        for (int i = 0; i < bc.length; i++) {
            Object[] new_ = new Object[5];
            new_[0] = bc[i].getName();
            new_[1] = bc[i].getDescription();
            new_[2] = bc[i].getStreet();
            new_[3] = bc[i].getCity();
            new_[4] = bc[i].getProvince();
            setValueAt(new_, pr.length + i);
        }
    }

    public int getColumnCount() {
        return headers.length;
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int col) {
        return headers[col];
    }

    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    public boolean isCellEditable(int row, int col) {
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        if (row >= getRowCount()) {
            Object[] new_ = new Object[headers.length];
            new_[col] = value;
            data.add(new_);
        } else {
            data.get(row)[col] = value;
        }
    }

    public void setValueAt(Object[] value, int row) throws IllegalArgumentException {
        if (value.length != headers.length) {
            System.out.println(value.length);
            System.out.println(headers.length);
            throw new IllegalArgumentException();
        }
        if (row >= getRowCount()) {
            data.add(value);
        } else {
            data.remove(row);
            data.add(row, value);
        }
    }
}