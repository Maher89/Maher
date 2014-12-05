package org.geoimage.viewer.core.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author Pietro Argentieri
 * 
 */


public class ConfigurationFile {
	//starting params
	public static  final String TRESH_HH_PARAM="thh";
	public static  final String TRESH_HV_PARAM="thv";
	public static  final String TRESH_VH_PARAM="tvh";
	public static  final String TRESH_VV_PARAM="tvv";
	
	public static  final String SHP_FILE="shape_file";
	public static  final String BUFFER_PARAM="buffer";
	public static  final String INPUT_FOLD_PARAM="input_folder";
	public static  final String OUTPUT_FOLD_PARAM="output_folder";
	
	public static  final String USE_LOCAL_CONF="use_local_conf";
	public static  final String INPUT_IMG="input_image";
	
	
	private String confFile;
	private Properties prop = new Properties();
	
		/**
		 * 
		 * @param configurationFile
		 * @throws IOException
		 */
		public ConfigurationFile(String configurationFile) throws IOException{
			confFile=configurationFile;
			readConfiguration();
		}
	 
		/**
		 * 
		 * @throws IOException
		 */
		private void readConfiguration() throws IOException {
			InputStream inputStream = new FileInputStream(new File(confFile));
	 
			if (inputStream != null) {
				prop.load(inputStream);
			} 
		}
		
		public String getProperty(String property){
			return prop.getProperty(property);
		}
		
		
		/***
		 * 
		 * @return
		 */
		public int[] getThresholdArray(){
			int[] thresholds={1,1,1,1};
			
			//-1 not setted 
			String tmp=prop.getProperty(TRESH_HH_PARAM,"-1");
			thresholds[0]=Integer.parseInt(tmp);
			
			tmp=prop.getProperty(TRESH_HV_PARAM,"-1");
			thresholds[1]=Integer.parseInt(tmp);
						
			tmp=prop.getProperty(TRESH_VH_PARAM,"-1");
			thresholds[2]=Integer.parseInt(tmp);
			
			tmp=prop.getProperty(TRESH_VV_PARAM,"-1");
			thresholds[3]=Integer.parseInt(tmp);
			
			return thresholds;
		}
		
		/**
		 * 
		 * @return the buffer param or -1 if is not configured
		 */
		public double getBuffer(){
			double buffer=0;
			String tmp=prop.getProperty(BUFFER_PARAM);
			if(tmp!=null&&!tmp.isEmpty()){
				buffer=Double.parseDouble(tmp);
			}
			return buffer;
		}
		
		/**
		 * 
		 * @return 
		 */
		public String getShapeFile(){
			return prop.getProperty(SHP_FILE,"");
		}
		/**
		 * 
		 * @return
		 */
		public String getOutputFolder(){
			return prop.getProperty(OUTPUT_FOLD_PARAM,"");
		}
		/**
		 * 
		 * @return
		 */
		public String getInputFolder(){
			return prop.getProperty(INPUT_FOLD_PARAM,"");
		}
		
		public String getInputImage(){
			return prop.getProperty(INPUT_IMG,"");
		}
		
		public boolean useLocalConfigurationFiles(){
			String tmp= prop.getProperty(USE_LOCAL_CONF,"false");
			return Boolean.parseBoolean(tmp);
			
		}
		
		public static void main(String[] args){
			try {
				ConfigurationFile f=new ConfigurationFile("C:\\tmp\\output\\analysis.conf");
				double b=f.getBuffer();
				System.out.println(b);
				int[] tt=f.getThresholdArray();
				System.out.println(tt[0]);
				System.out.println(tt[1]);
				System.out.println(tt[2]);
				System.out.println(tt[3]);
				System.out.println(f.getShapeFile());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
}



