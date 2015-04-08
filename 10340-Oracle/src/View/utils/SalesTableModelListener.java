package View.utils;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import Model.Item;
import View.order.SalePanel;


public class SalesTableModelListener implements TableModelListener {

	private boolean 			enableListener = true;
	private int 				rowCount;
	private AbstractTableModel 	tableModel;
	
	
	public SalesTableModelListener(AbstractTableModel tableModel) {
		this.tableModel = tableModel;
		this.rowCount = tableModel.getRowCount();
	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
		
		if (!enableListener)
			return;
		
		if (tableModel.getRowCount() != rowCount) {		// means we added or removed row
			rowCount = tableModel.getRowCount();
			return;
		}
		
		int row = e.getFirstRow();
		int col = e.getColumn();
		Item item = (Item)tableModel.getValueAt(row, SalePanel.ITEM_COL);
		
		if (item == null) {		// false selection
			selectItemError(row, col);
			return;
		}
		
		
		//////////////////////////////// starting the check
		enableListener = false;
		
		float defaultPrice;
		
		switch (col) {
		
			case SalePanel.ITEM_COL:
				tableModel.setValueAt(1, row, SalePanel.QUANTITY_COL);
				tableModel.setValueAt(item.getPrice(), row, SalePanel.PRICE_COL);
				tableModel.setValueAt(0, row, SalePanel.DISCOUNT_COL);
				break;
				
			case SalePanel.QUANTITY_COL:
				checkInt(row, col, 1);
				break;
				
			case SalePanel.PRICE_COL:
				defaultPrice = item.getPrice();
				checkPrice(row, col, defaultPrice);
				break;
				
			case SalePanel.DISCOUNT_COL:
				checkPercent(row, col);
				break;
				
			case SalePanel.FPRICE_COL:
				defaultPrice = getFinalPrice(row);
				checkPrice(row, col, defaultPrice);
				break;
			
		}
		
		if (col != SalePanel.FPRICE_COL)
			updateFinalPrice(row);
		
		enableListener = true;
	}
	
	public void selectItemError(int row, int col) {
		String msg = "ERROR: Item not selected\nPlease select an item first";
		JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
		enableListener = false;
		tableModel.setValueAt("", row, col);
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
		int quantity = (int)tableModel.getValueAt(row, SalePanel.QUANTITY_COL);
		float price = (float)tableModel.getValueAt(row, SalePanel.PRICE_COL);
		int discount = (int)tableModel.getValueAt(row, SalePanel.DISCOUNT_COL);
		
		return quantity * price * (1 - ((float)discount / 100));
	}

	public void updateFinalPrice(int row) {
		tableModel.setValueAt( getFinalPrice(row) , row, SalePanel.FPRICE_COL);
	}
	
}
