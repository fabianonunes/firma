package tc.fab.firma.app.components;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.observablecollections.ObservableList;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import tc.fab.app.AppContext;
import tc.fab.firma.FirmaView;
import tc.fab.firma.app.components.FileModel.Status;
import tc.fab.firma.utils.FormatUtils;

public class JFileTable extends JTable {

	private enum Columns {
		STATUS, FILE_TYPE, FILE_NAME, FILE_SIZE
	}

	private static final long serialVersionUID = 1L;
	private ImageIcon pdfIcon;

	private ImageIcon doneIcon;
	private ImageIcon failedIcon;
	private ImageIcon loadingIcon;

	private List<Columns> columns = Arrays.asList(Columns.values());
	private ObservableList<FileModel> observableModel;
	private AppContext context;
	
	@Inject
	public JFileTable(AppContext context) {

		super();
		
		this.context = context;

		this.observableModel = ObservableCollections.observableList(new Vector<FileModel>());

		setAutoCreateRowSorter(true);
		setShowGrid(false);
		setFocusable(false);
		setRowSelectionAllowed(true);

		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		getTableHeader().setResizingAllowed(false);
		getTableHeader().setReorderingAllowed(false);

		loadIcons();

		getTableHeader().setPreferredSize(new Dimension(getTableHeader().getWidth(), 22));
		setRowHeight(24);

		initBindings();

		TableColumnModel tcModel = getColumnModel();
		tcModel.getColumn(idToColumn(Columns.STATUS)).setMaxWidth(28);
		tcModel.getColumn(idToColumn(Columns.FILE_TYPE)).setMaxWidth(30);
		tcModel.getColumn(idToColumn(Columns.FILE_SIZE)).setMaxWidth(110);

	}

	@SuppressWarnings("unchecked")
	private void initBindings() {

		JTableBinding<FileModel, List<FileModel>, JTable> tableBinding = SwingBindings
			.createJTableBinding(UpdateStrategy.READ, observableModel, this);

		ELProperty<FileModel, Status> status = ELProperty.create("${status}");
		ELProperty<FileModel, Status> fileType = ELProperty.create("${fileType}");
		ELProperty<FileModel, Status> fileName = ELProperty.create("${fileName}");
		ELProperty<FileModel, Status> fileSize = ELProperty.create("${fileSize}");

		JTableBinding<FileModel, List<FileModel>, JTable>.ColumnBinding cb;

		cb = tableBinding.addColumnBinding(status);
		cb.setColumnClass(getColumnClassById(Columns.STATUS));
		cb.setColumnName(getColumnNameById(Columns.STATUS));
		cb.setEditable(false);
		cb.setRenderer(getDefaultRenderer(ImageIcon.class));
		cb.setConverter(new ConverterAdapter<Status, ImageIcon>() {
			@Override
			public ImageIcon convertForward(Status value) {
				switch (value) {
					case DONE:
						return doneIcon;
					case FAILED:
						return failedIcon;
					case LOADING:
						return loadingIcon;
					default:
						return null;
				}
			}
		});

		cb = tableBinding.addColumnBinding(fileType);
		cb.setColumnClass(getColumnClassById(Columns.FILE_TYPE));
		cb.setColumnName(getColumnNameById(Columns.FILE_TYPE));
		cb.setEditable(false);
		cb.setRenderer(getDefaultRenderer(ImageIcon.class));
		cb.setConverter(new ConverterAdapter<String, ImageIcon>() {
			@Override
			public ImageIcon convertForward(String value) {
				return pdfIcon;
			}
		});

		cb = tableBinding.addColumnBinding(fileName);
		cb.setColumnClass(getColumnClassById(Columns.FILE_NAME));
		cb.setColumnName(getColumnNameById(Columns.FILE_NAME));
		cb.setEditable(false);

		cb = tableBinding.addColumnBinding(fileSize);
		cb.setColumnClass(getColumnClassById(Columns.FILE_SIZE));
		cb.setColumnName(getColumnNameById(Columns.FILE_SIZE));
		cb.setEditable(false);
		cb.setConverter(new ConverterAdapter<Long, String>() {
			@Override
			public String convertForward(Long value) {
				return FormatUtils.format(value);
			}
		});

		tableBinding.bind();

	}

	public String getColumnNameById(Columns colId) {
		switch (colId) {
			case STATUS:
				return "";
			case FILE_TYPE:
				return "";
			case FILE_NAME:
				return context.getResReader().getString("firma.msg.column_file_name");
			case FILE_SIZE:
				return context.getResReader().getString("firma.msg.column_file_size");
			default:
				return colId.toString();
		}
	}

	public Class<?> getColumnClassById(Columns colId) {
		switch (colId) {
			case STATUS:
				return ImageIcon.class;
			case FILE_TYPE:
				return ImageIcon.class;
			case FILE_NAME:
				return String.class;
			case FILE_SIZE:
				return Long.class;
			default:
				return String.class;
		}
	}

	private void loadIcons() {
		loadingIcon = new ImageIcon(FirmaView.class.getResource("/icons/spinner-mini-2.gif"));
		loadingIcon.setImageObserver(this); // animated gif
		doneIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_green.png"));
		failedIcon = new ImageIcon(FirmaView.class.getResource("/icons/bullet_error.png"));
		pdfIcon = new ImageIcon(FirmaView.class.getResource("/icons/page_white_acrobat.png"));
	}
	
	

	protected int idToColumn(Columns id) {
		return columns.indexOf(id);
	}

	public void setStatus(int modelRowIndex, Status status) {
		FileModel row = observableModel.get(modelRowIndex);
		row.setStatus(status);
		observableModel.set(modelRowIndex, row);
	}

	/**
	 * avoids CPU eating by animated gif when not visible
	 * @see {@link http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4302818}
	 */
	public void flush () {
		loadingIcon.getImage().flush();
	}
	
	public ObservableList<FileModel> getData() {
		return observableModel;
	}

}
