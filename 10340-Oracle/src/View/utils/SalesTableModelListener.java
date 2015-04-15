package view.utils;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import view.panels.SalePanel;
import model.Item;


/**
 * 
 * @author 	Ariel Levin
 * 			<br/><a href="http://about.me/ariel.levin">about.me/ariel.levin</a>
 * 			<br/><a href="mailto:ariel.lvn89@gmail.com">ariel.lvn89@gmail.com</a><br/><br/>
 * 
 * 			Matan Shulman
 * 			<br/><a href="mailto:matan.shulman87@gmail.com">matan.shulman87@gmail.com</a>
 *
 */
public class SalesTableModelListener implements TableModelListener {

	private boolean 			enableListener = true;
	private int 				rowCount;
	private AbstractTableModel 	tableModel;
	private SalePanel			salePanel;
	
	
	public SalesTableModelListener(AbstractTableModel tableModel, SalePanel salePanel) {
		this.tableModel = tableModel;
		this.salePanel = salePanel;
		this.rowCount = tableModel.getRowCount();
	}
	
	
	@Override
	public void tableChanged(TableModelEvent e) {
		
		if (!enableListener)
			return;
		
		if (tableModel.getRowCount() != rowCount) {		// means we added or removed row
			rowCount = tableModel.getRowCount();
			if (tableModel.getRowCount() < rowCount)
				salePanel.updatePrice( sumAllPrices() );
			return;
		}
		
		int row = e.getFirstRow();
		int col = e.getColumn();
		Item item = null;
		try {
			item = (Item)tableModel.getValueAt(row, SalePanel.ITEM_COL);
		} catch (Exception e1) {}
		
		if (item == null) {		// false selection
			selectItemError(row, col);
			clearRow(row);
			return;
		}
		
		if ( isItemAlreadyExist(item, row) ) {
			itemAlreadyExistError(row, col);
			clearRow(row);
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
	}
	
	public void itemAlreadyExistError(int row, int col) {
		String msg = "ERROR: Item already exist on table\n"
				+ "Please select another item, or add to the existing one";
		JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
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
		
		float fprice = quantity * price * (1 - ((float)discount / 100));
		
		return SalePanel.round(fprice, 2);
	}

	public void updateFinalPrice(int row) {
		tableModel.setValueAt( getFinalPrice(row) , row, SalePanel.FPRICE_COL);
		salePanel.updatePrice( sumAllPrices() );
	}
	
	private float sumAllPrices() {
		float sum = 0;
		for (int i = 0 ; i < tableModel.getRowCount(); i++) {
			if (tableModel.getValueAt(i, SalePanel.FPRICE_COL) != null)
				sum += (float) tableModel.getValueAt(i, SalePanel.FPRICE_COL);
		}
		return SalePanel.round(sum, 2);
	}
	
	private boolean isItemAlreadyExist(Item item, int row) {
		
		for (int i = 0 ; i < tableModel.getRowCount() ; i++) {
			Item tmpItem = null;
			try {
				tmpItem = (Item)tableModel.getValueAt(i, SalePanel.ITEM_COL);
			} catch (Exception e) {}
			
			if ( tmpItem != null && i != row && tmpItem.getNum() == item.getNum() )
				return true;
		}
		return false;
	}
	
	private void clearRow(int row) {
		enableListener = false;
		try {
			tableModel.setValueAt(null, row, SalePanel.ITEM_COL);
			tableModel.setValueAt("", row, SalePanel.QUANTITY_COL);
			tableModel.setValueAt("", row, SalePanel.PRICE_COL);
			tableModel.setValueAt("", row, SalePanel.DISCOUNT_COL);
			tableModel.setValueAt("", row, SalePanel.FPRICE_COL);
		} catch (Exception e) {}
		enableListener = true;
	}
	
}
