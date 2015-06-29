/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geoimage.viewer.actions;

import static org.geoimage.viewer.util.Constant.PREF_BUFFERING_DISTANCE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.geoimage.analysis.BlackBorderAnalysis;
import org.geoimage.analysis.DetectedPixels;
import org.geoimage.analysis.VDSAnalysis;
import org.geoimage.analysis.VDSSchema;
import org.geoimage.def.GeoImageReader;
import org.geoimage.def.SarImageReader;
import org.geoimage.exception.GeoTransformException;
import org.geoimage.impl.ENL;
import org.geoimage.impl.TiledBufferedImage;
import org.geoimage.impl.s1.Sentinel1;
import org.geoimage.utils.IMask;
import org.geoimage.utils.IProgress;
import org.geoimage.viewer.core.Platform;
import org.geoimage.viewer.core.analysisproc.AnalysisProcess;
import org.geoimage.viewer.core.analysisproc.VDSAnalysisProcessListener;
import org.geoimage.viewer.core.api.Argument;
import org.geoimage.viewer.core.api.Attributes;
import org.geoimage.viewer.core.api.IImageLayer;
import org.geoimage.viewer.core.api.ILayer;
import org.geoimage.viewer.core.api.iactions.AbstractAction;
import org.geoimage.viewer.core.factory.FactoryLayer;
import org.geoimage.viewer.core.layers.GeometricLayer;
import org.geoimage.viewer.core.layers.vectors.ComplexEditVDSVectorLayer;
import org.geoimage.viewer.core.layers.vectors.MaskVectorLayer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 *
 * @author thoorfr
 */
public class VDSAnalysisConsoleAction extends AbstractAction implements  IProgress,VDSAnalysisProcessListener,ActionListener{
    private String message = "";
    
    
    private int current = 0;
    private int maximum = 3;
    private boolean done = false;
    private boolean indeterminate;
	//private boolean running = false;
    private GeoImageReader gir = null;
    private List<IMask> mask = null;
    private AnalysisProcess proc=null;
    private boolean stopping=false;
   
    public VDSAnalysisConsoleAction() {
        
    }

    public String getName() {
        return "vds";
    }

    public String getDescription() {
        return "Compute a VDS (Vessel Detection System) analysis.\n"
                + "Use \"vds k-dist 1.5 GSHHS\" to run a analysis with k-distribuion clutter model with a threshold of 1.5 using the land mask \"GSHHS...\"";
    }

    /**
     * run the analysis called from ActionDialog
     */
    public boolean execute(String[] args) {
        // initialise the buffering distance value
        int bufferingDistance = Double.valueOf((Platform.getPreferences()).readRow(PREF_BUFFERING_DISTANCE)).intValue();
        Platform.getMain().addStopListener(this);
        
        
        if (args.length < 2) {
            return true;
        } else {

            if (args[0].equals("k-dist")) {
                done = false;
                
                IImageLayer cl=Platform.getCurrentImageLayer();
                GeoImageReader reader = ((IImageLayer) cl).getImageReader();
                if (reader instanceof SarImageReader || reader instanceof TiledBufferedImage) {
                    gir = reader;
                }
                if (gir == null) {
                    done = true;
                    return false;
                }

                //this part mange the different thresholds for different bands
                //in particular is also looking for which band is available and leave the threshold to 0 for the not available bands
                float thrHH = 0;
                float thrHV = 0;
                float thrVH = 0;
                float thrVV = 0;
                
                int numberofbands = gir.getNBand();
                for (int bb = 0; bb < numberofbands; bb++) {
                    if (gir.getBandName(bb).equals("HH") || gir.getBandName(bb).equals("H/H")) {
                        thrHH = Float.parseFloat(args[bb + 1]);
                    } else if (gir.getBandName(bb).equals("HV") || gir.getBandName(bb).equals("H/V")) {
                        thrHV = Float.parseFloat(args[bb + 1]);
                    } else if (gir.getBandName(bb).equals("VH") || gir.getBandName(bb).equals("V/H")) {
                        thrVH = Float.parseFloat(args[bb + 1]);
                    } else if (gir.getBandName(bb).equals("VV") || gir.getBandName(bb).equals("V/V")) {
                        thrVV = Float.parseFloat(args[bb + 1]);
                    }
                }
                final float thresholdHH = thrHH;
                final float thresholdHV = thrHV;
                final float thresholdVH = thrVH;
                final float thresholdVV = thrVV;

                //read the land mask
                mask = new ArrayList<IMask>();
                for (ILayer l : Platform.getLayerManager().getChilds(cl)) {
                    if (l instanceof IMask & l.getName().startsWith(args[numberofbands + 1])) {
                        mask.add((IMask) l);
                    }
                }

                //read the buffer distance
                bufferingDistance = Integer.parseInt(args[numberofbands + 2]);
                final float ENL = Float.parseFloat(args[numberofbands + 3]);

                // create new buffered mask with bufferingDistance using the mask in parameters
                final IMask[] bufferedMask = new IMask[mask.size()];
                
                for (int i=0;i<mask.size();i++) {
                	IMask maskList = mask.get(i);
               		bufferedMask[i]=FactoryLayer.createMaskLayer(maskList.getName(), maskList.getType(), bufferingDistance, ((MaskVectorLayer)maskList).getGeometriclayer());
                }
                
                final VDSAnalysis analysis = new VDSAnalysis((SarImageReader) gir, bufferedMask, ENL, thresholdHH, thresholdHV, thresholdVH, thresholdVV, this);
                
                final String[] thresholds = {""+thrHH,""+thrHV,""+thrVH,""+thrVV};
                
                BlackBorderAnalysis blackBorderAnalysis=null;
                if(gir instanceof Sentinel1){
	                MaskVectorLayer mv=null;
	           	 	if(bufferedMask!=null&&bufferedMask.length>0)
	           	 		mv=(MaskVectorLayer)bufferedMask[0];
	           	 	if(mv!=null)
	           	 		blackBorderAnalysis= new BlackBorderAnalysis(gir,mv.getGeometries());
	           	 	else 
	           		    blackBorderAnalysis= new BlackBorderAnalysis(gir,null);
                } 	
                
                proc=new AnalysisProcess(reader,ENL, analysis, bufferedMask, thresholds,bufferingDistance,blackBorderAnalysis);
                proc.addProcessListener(this);
                
                Thread t=new Thread(proc);
                t.setName("VDS_analysis_"+gir.getDisplayName(0));
                t.start();
                
            }

            return true;
        }
    }
    
    
    
    
    /**
     * 
     * @param ENL
     * @param analysis
     * @param bufferedMask
     * @param thresholds
     * @return
     */
    public List<ComplexEditVDSVectorLayer> runBatchAnalysis(GeoImageReader reader,float ENL, VDSAnalysis analysis,IMask[] bufferedMask, String[] thresholds,int buffer){
    	this.gir=reader;
        
        BlackBorderAnalysis blackBorderAnalysis=null;
        if(gir instanceof Sentinel1){
            MaskVectorLayer mv=null;
       	 	if(bufferedMask!=null&&bufferedMask.length>0)
       	 		mv=(MaskVectorLayer)bufferedMask[0];
       	 	if(mv!=null)
       	 		blackBorderAnalysis= new BlackBorderAnalysis(gir,mv.getGeometries());
       	 	else 
       		    blackBorderAnalysis= new BlackBorderAnalysis(gir,null);
        } 	
        
    	AnalysisProcess ap=new AnalysisProcess(reader,ENL,analysis, bufferedMask, thresholds, buffer,blackBorderAnalysis);
        ap.run();
        return ap.getResultLayers();
    }
    
    
    
    
    /**
 *
 * @param gir
 * @param pixels
 * @param runid
 * @return
     * @throws GeoTransformException 
 */
    public static GeometricLayer createGeometricLayer(GeoImageReader gir, DetectedPixels pixels, long runid) throws GeoTransformException {
        GeometricLayer out = new GeometricLayer("point");
        out.setName("VDS Analysis");
        GeometryFactory gf = new GeometryFactory();
        int count=0;
        for (double[] boat : pixels.getBoats()) {

            Attributes atts = Attributes.createAttributes(VDSSchema.schema, VDSSchema.types);
            atts.set(VDSSchema.ID, count++);
            atts.set(VDSSchema.MAXIMUM_VALUE, boat[3]);
            atts.set(VDSSchema.TILE_AVERAGE, boat[4]);
            atts.set(VDSSchema.TILE_STANDARD_DEVIATION, boat[5]);
            atts.set(VDSSchema.THRESHOLD, boat[6]);
            atts.set(VDSSchema.RUN_ID, runid + "");
            atts.set(VDSSchema.NUMBER_OF_AGGREGATED_PIXELS, boat[7]);
            atts.set(VDSSchema.ESTIMATED_LENGTH, boat[8]);
            atts.set(VDSSchema.ESTIMATED_WIDTH, boat[9]);
            atts.set(VDSSchema.SIGNIFICANCE, (boat[3]-boat[4])/(boat[4]*boat[5]));
            atts.set(VDSSchema.DATE,Timestamp.valueOf(((SarImageReader)gir).getTimeStampStart()));

            atts.set(VDSSchema.SIGNIFICANCE, (boat[3] - boat[4]) / (boat[4] * boat[5]));
            atts.set(VDSSchema.DATE, Timestamp.valueOf(((SarImageReader)gir).getTimeStampStart()));
            atts.set(VDSSchema.VS, 0);
            //compute the direction of the vessel considering the azimuth of the image
            //result is between 0 and 180 degree
            double azimuth = ((SarImageReader)gir).getImageAzimuth();
            double degree = boat[10] + 90 + azimuth;
            if (degree > 180) {
                degree = degree - 180;
            }
        
            atts.set(VDSSchema.ESTIMATED_HEADING, degree);
            out.put(gf.createPoint(new Coordinate(boat[1], boat[2])), atts);
        }

        return out;
    }
/**
 *
 * @return
 */
    public String getPath() {
        return "Analysis/VDS";
    }

    public List<Argument> getArgumentTypes() {
        Argument a1 = new Argument("algorithm", Argument.STRING, false, "k-dist");
        a1.setPossibleValues(new Object[]{"k-dist"});
        Argument a2 = new Argument("thresholdHH", Argument.FLOAT, false, 1.5);
        Argument a21 = new Argument("thresholdHV", Argument.FLOAT, false, 1.2);
        Argument a22 = new Argument("thresholdVH", Argument.FLOAT, false, 1.5);
        Argument a23 = new Argument("thresholdVV", Argument.FLOAT, false, 1.5);

        Argument a3 = new Argument("mask", Argument.STRING, true, "no mask choosen");
        ArrayList<String> vectors = new ArrayList<String>();
        IImageLayer il=Platform.getCurrentImageLayer();

        if (il != null) {
          //  for (ILayer l : il.getLayers()) {
            for (ILayer l : Platform.getLayerManager().getAllLayers()) {
                if (l instanceof MaskVectorLayer && !((MaskVectorLayer) l).getType().equals(MaskVectorLayer.POINT)) {
                    vectors.add(l.getName());
                }
            }
        }
        a3.setPossibleValues(vectors.toArray());
        Vector<Argument> out = new Vector<Argument>();

        Argument a4 = new Argument("Buffer (pixels)", Argument.FLOAT, false, (Platform.getPreferences()).readRow(PREF_BUFFERING_DISTANCE));

        //management of the different threshold in the VDS parameters panel
        out.add(a1);
        int numberofbands = il.getImageReader().getNBand();
        for (int bb = 0; bb < numberofbands; bb++) {
            if (il.getImageReader().getBandName(bb).equals("HH") || il.getImageReader().getBandName(bb).equals("H/H")) {
                out.add(a2);
            } else if (il.getImageReader().getBandName(bb).equals("HV") || il.getImageReader().getBandName(bb).equals("H/V")) {
                out.add(a21);
            } else if (il.getImageReader().getBandName(bb).equals("VH") || il.getImageReader().getBandName(bb).equals("V/H")) {
                out.add(a22);
            } else if (il.getImageReader().getBandName(bb).equals("VV") || il.getImageReader().getBandName(bb).equals("V/V")) {
                out.add(a23);
            }
        }

        out.add(a3);
        out.add(a4);
        if (il.getImageReader() instanceof SarImageReader) {
            Argument aEnl = new Argument("ENL", Argument.FLOAT, false, ENL.getFromGeoImageReader((SarImageReader) il.getImageReader()));
            out.add(aEnl);
        }

        return out;
    }

    
   
    
    public boolean isIndeterminate() {
        return this.indeterminate;
    }

    public boolean isDone() {
        return this.done;
    }

    public int getMaximum() {
        return this.maximum;
    }

    public int getCurrent() {
        return this.current;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCurrent(int i) {
        current = i;
    }

    public void setMaximum(int size) {
        maximum = size;
    }

    public void setMessage(String string) {
        message =  string;
    }

    public void setIndeterminate(boolean value) {
        indeterminate = value;
    }

    public void setDone(boolean value) {
        done = value;
    }
    
    

	@Override
	public void startAnalysis() {
		setCurrent(1);
		message="Starting VDS Analysis";
	}

	@Override
	public void startAnalysisBand(String message) {
		if(!stopping){
			setCurrent(2);
			this.message=message;
		}	
	}

	@Override
	public void calcAzimuthAmbiguity(String message) {
		if(!stopping){
			setCurrent(3);
			this.message=message;
		}	
	}

	@Override
	public void agglomerating(String message) {
		if(!stopping){
			setCurrent(4);
			this.message=message;
		}	
	}

	@Override
	public void endAnalysis() {
		setDone(true);
		Platform.getMain().removeStopListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(proc!=null&&e.getActionCommand().equals("STOP")){
			this.proc.setStop(true);
			this.message="stopping";
			Platform.getMain().removeStopListener(this);
			this.proc=null;
		}	
	}

	@Override
	public void layerReady(ILayer layer) {
		if(!Platform.isBatchMode()){
			Platform.getLayerManager().addLayer(layer);
		}
		
	}
}
