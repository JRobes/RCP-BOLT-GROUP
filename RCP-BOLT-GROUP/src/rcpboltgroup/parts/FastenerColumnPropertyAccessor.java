package rcpboltgroup.parts;

import aero.alestis.stresstools.general.Fastener;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;

public class FastenerColumnPropertyAccessor implements IColumnPropertyAccessor<Fastener> {
	private final List<String> propertyNames;
   // private static final List<String> propertyNames = 
   //     Arrays.asList("fastenerID", "x_cord", "y_cord", "z_cord", "fastenerType");

	
	FastenerColumnPropertyAccessor(String... propertyNames) {
		this.propertyNames = Arrays.asList(propertyNames);
	}

    @Override
    public Object getDataValue(Fastener fastener, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return fastener.getFastenerID();
            case 1:
            	//if(fastener.getFastenerCords().getX() == null)
            	System.out.println(fastener.getFastenerCords().getX());
                return String.valueOf(fastener.getFastenerCords().getX());
            case 2:
            	System.out.println(fastener.getFastenerCords().getY());
                return String.valueOf(fastener.getFastenerCords().getY());
            case 3:
            	System.out.println(fastener.getFastenerCords().getZ());
                return String.valueOf(fastener.getFastenerCords().getZ());
            case 4:
                return fastener.getFastenerType();
            }
        return fastener;
    }

    
    @Override
    public void setDataValue(Fastener fastener, int columnIndex, Object newValue) {
    	Vector3D vect;
    	switch (columnIndex) {
            case 0:
                String fastenerID = String.valueOf(newValue);
                fastener.setFastenerID(fastenerID);
                break;
            case 1:
                vect = new Vector3D((double) newValue, fastener.getFastenerCords().getY(),fastener.getFastenerCords().getZ());
                fastener.setFastenerCords(vect);
                break;
            case 2:
                vect = new Vector3D(fastener.getFastenerCords().getX(), fastener.getFastenerCords().getY(), (double) newValue);
                fastener.setFastenerCords(vect);
                break;
            case 3:
                vect = new Vector3D(fastener.getFastenerCords().getX(), (double) newValue, fastener.getFastenerCords().getZ());
                fastener.setFastenerCords(vect);
                break;
            case 4:
                String fastenerType = String.valueOf(newValue);
            	fastener.setFastenerType(fastenerType);
                break;
        }
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnProperty(int columnIndex) {
        return propertyNames.get(columnIndex);
    	//return propertyNames[columnIndex];
    }

    @Override
    public int getColumnIndex(String propertyName) {
       
    	return propertyNames.indexOf(propertyName);
    }

}
