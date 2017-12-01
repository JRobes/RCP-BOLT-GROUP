package rcpboltgroup.parts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import aero.alestis.stresstools.general.Fastener;

public class SamplePart {

    private IDataProvider bodyDataProvider;
    private String[] propertyNames;
    private BodyLayerStack bodyLayer;
    @SuppressWarnings("rawtypes")
	private Map propertyToLabels;

	@Inject
	private MDirtyable dirty;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void createComposite(Composite parent) {
	       parent.setLayout(new GridLayout());
	       this.bodyDataProvider = setupBodyDataProvider();
	        DefaultColumnHeaderDataProvider colHeaderDataProvider = new DefaultColumnHeaderDataProvider(
	                this.propertyNames, this.propertyToLabels);
	        DefaultRowHeaderDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(
	                this.bodyDataProvider);

	        this.bodyLayer = new BodyLayerStack(this.bodyDataProvider);
	        ColumnHeaderLayerStack columnHeaderLayer = new ColumnHeaderLayerStack(
	                colHeaderDataProvider);
	        RowHeaderLayerStack rowHeaderLayer = new RowHeaderLayerStack(
	                rowHeaderDataProvider);
	        DefaultCornerDataProvider cornerDataProvider = new DefaultCornerDataProvider(
	                colHeaderDataProvider, rowHeaderDataProvider);
	        CornerLayer cornerLayer = new CornerLayer(new DataLayer(
	                cornerDataProvider), rowHeaderLayer, columnHeaderLayer);

	        GridLayer gridLayer = new GridLayer(this.bodyLayer, columnHeaderLayer,
	                rowHeaderLayer, cornerLayer);
	        NatTable natTable = new NatTable(parent, gridLayer);
	        
	        /*
	        final NatTable natTable = new NatTable(
	                parent,
	                SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.BORDER,
	                gridLayer);
			*/
	        GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	    }
        
	

	@Focus
	public void setFocus() {
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}
    @SuppressWarnings({ "unchecked"})
	private IDataProvider setupBodyDataProvider() {
    	
        List<Fastener> fasteners = Arrays.asList(new Fastener("F1", new Vector3D( 1.0,3.02,2.222), "tipo"), new Fastener("F2", new Vector3D(2.3,3.65,2.99), "tipoll"));

        this.propertyToLabels = new HashMap<>();
        this.propertyToLabels.put("fastenerID", "Fastener ID");
        this.propertyToLabels.put("x_cord", "X-cord");
        this.propertyToLabels.put("y_cord", "Y-cord");
        this.propertyToLabels.put("z_cord", "Z-cord");
        this.propertyToLabels.put("fastenerType", "Fastener Type");

        this.propertyNames = new String[]{ "fastenerID", "x_cord", "y_cord", "z_cord", "fastenerType" };
        return new ListDataProvider<>(fasteners, 
                new FastenerColumnPropertyAccessor(this.propertyNames));

    }

	
    public class BodyLayerStack extends AbstractLayerTransform {

        private SelectionLayer selectionLayer;

        public BodyLayerStack(IDataProvider dataProvider) {
            DataLayer bodyDataLayer = new DataLayer(dataProvider);
            //ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(
             //       bodyDataLayer);
           // ColumnHideShowLayer columnHideShowLayer = new ColumnHideShowLayer(
            //        columnReorderLayer);
            //this.selectionLayer = new SelectionLayer(columnHideShowLayer);
            this.selectionLayer = new SelectionLayer(bodyDataLayer);
            ViewportLayer viewportLayer = new ViewportLayer(this.selectionLayer);
            setUnderlyingLayer(viewportLayer);
        }

        public SelectionLayer getSelectionLayer() {
            return this.selectionLayer;
        }
    }

    public class ColumnHeaderLayerStack extends AbstractLayerTransform {

        public ColumnHeaderLayerStack(IDataProvider dataProvider) {
            DataLayer dataLayer = new DataLayer(dataProvider);
            ColumnHeaderLayer colHeaderLayer = new ColumnHeaderLayer(dataLayer,
                    SamplePart.this.bodyLayer, SamplePart.this.bodyLayer.getSelectionLayer());
            setUnderlyingLayer(colHeaderLayer);
        }
    }

    public class RowHeaderLayerStack extends AbstractLayerTransform {

        public RowHeaderLayerStack(IDataProvider dataProvider) {
            DataLayer dataLayer = new DataLayer(dataProvider, 50, 20);
            RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(dataLayer,
            		SamplePart.this.bodyLayer, SamplePart.this.bodyLayer.getSelectionLayer());
            setUnderlyingLayer(rowHeaderLayer);
        }
    }
      

    
	

}