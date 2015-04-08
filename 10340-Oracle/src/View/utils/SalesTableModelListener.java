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
	
	private boolean 	enableListener = true;
	private int 		rowCount;
	
	AbstractTableModel tableModel;
	
	
	public SalesTableModelListener(AbstractTableModel tableModel) {
		this.tableModel = tableModel;
		this.rowCount = tableModel.getRowCount();
	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
		
		if (!enableListener)
			return;
		
		if (tableModel.getRowCount() != rowCount) {		// means we added row
			rowCount = tableModel.getRowCount();
			return;
		}
		
		enableListener = false;
		
		int col = e.getColumn();
		int row = e.getFirstRow();
		System.out.println("row changed: " + row);
		Item item = (Item)tableModel.getValueAt(row, ITEM_COL);
		float defaultPrice;
		
		switch (col) {
		
			case ITEM_COL:
				tableModel.setValueAt(1, row, QUANTITY_COL);
				tableModel.setValueAt(item.getPrice(), row, PRICE_COL);
				tableModel.setValueAt(0, row, DISCOUNT_COL);
				break;
				
			case QUANTITY_COL:
				checkInt(row, col, 1);
				break;
				
			case PRICE_COL:
				defaultPrice = item.getPrice();
				checkPrice(row, col, defaultPrice);
				break;
				
			case DISCOUNT_COL:
				checkPercent(row, col);
				break;
				
			case FPRICE_COL:
				defaultPrice = getFinalPrice(row);
				checkPrice(row, col, defaultPrice);
				break;
			
		}
		
		if (col != FPRICE_COL)
			updateFinalPrice(row);
		
		enableListener = true;
	}
	
	public void checkInt(int row, int col, int def) {
		try {
			int num = Integer.parseInt( (String)tableModel.getValueAt(row,col) );
			tableModel.setValueAt(num, row, col);
			
		} catch (Exception e1) {
			String msg = "ERROR: Not a valid number entered";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			tableModel.setValueAt(def, row, col);
		}
	}
	
	public void checkPercent(int row, int col) {
		try {
			int percent = Integer.parseInt( (String)tableModel.getValueAt(row,col) );
			
			if (percent < 0 || percent > 100)
				throw new Exception();
			
			tableModel.setValueAt(percent, row, col);
			
		} catch (Exception e1) {
			String msg = "ERROR: Not a valid percent entered";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			tableModel.setValueAt(0, row, col);
		}
	}
	
	public void checkPrice(int row, int col, float defaultPrice) {
		try {
			float price = Float.parseFloat( (String)tableModel.getValueAt(row,col) );
			
			if (price < 0)
				throw new Exception();
			
			tableModel.setValueAt(price, row, col);
			
		} catch (Exception e1) {
			String msg = "ERROR: Not a valid value entered";
			JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
			tableModel.setValueAt(defaultPrice, row, col);
		}
	}
	
	public float getFinalPrice(int row) {
		int quantity = (int)tableModel.getValueAt(row, QUANTITY_COL);
		float price = (float)tableModel.getValueAt(row, PRICE_COL);
		int discount = (int)tableModel.getValueAt(row, DISCOUNT_COL);
		
		return quantity * price * (1 - ((float)discount / 100));
	}

	public void updateFinalPrice(int row) {
		tableModel.setValueAt( getFinalPrice(row) , row, FPRICE_COL);
	}
	
}
