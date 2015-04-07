package View.utils;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import Model.Item;


public class SalesTableModelListener implements TableModelListener {

	public static final int ITEM_COL 		= 0;
	public static final int QUANTITY_COL 	= 1;
	public static final int PRICE_COL 		= 2;
	public static final int DISCOUNT_COL 	= 3;
	public static final int FPRICE_COL 		= 4;
	
	AbstractTableModel tableModel;
	
	
	public SalesTableModelListener(AbstractTableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
		
		int col = e.getColumn();
		int row = e.getFirstRow();
		Item item = (Item)tableModel.getValueAt(row, ITEM_COL);
		
		switch (col) {
		
			case ITEM_COL:	
				tableModel.setValueAt(1, row, QUANTITY_COL);
				tableModel.setValueAt(item.getPrice(), row, PRICE_COL);
				tableModel.setValueAt(0, row, DISCOUNT_COL);
				updateFinalPrice(row);
				break;
				
			case QUANTITY_COL:
				checkInt(row, col, 1);
				updateFinalPrice(row);
				break;
				
			case PRICE_COL:
				float defaultPrice = item.getPrice();
				checkPrice(row, col, defaultPrice);
				
				break;
			
		}

	}
	
	public int checkInt(int row, int col, int def) {
		int num = -1;
		try {
			num = (int)tableModel.getValueAt(row, col);
		} catch (Exception e1) {
			String msg = "ERROR: Not a valid number entered";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			tableModel.setValueAt(def, row, col);
		}
		return num;
	}
	
	public float checkPrice(int row, int col, float defaultPrice) {
		float price = -1;
		try {
			price = (float)tableModel.getValueAt(row, col);
			
			if (price < 0)
				throw new Exception();
			
		} catch (Exception e1) {
			String msg = "ERROR: Not a valid value entered";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			tableModel.setValueAt(defaultPrice, row, col);
		}
		
		return price;
	}

	public void updateFinalPrice(int row) {
		int quantity = (int)tableModel.getValueAt(row, QUANTITY_COL);
		float price = (float)tableModel.getValueAt(row, PRICE_COL);
		int discount = (int)tableModel.getValueAt(row, DISCOUNT_COL);
		
		float finalPrice = quantity * price * (1 - (discount / 100));
		tableModel.setValueAt( finalPrice , row, FPRICE_COL);
	}
	
}
