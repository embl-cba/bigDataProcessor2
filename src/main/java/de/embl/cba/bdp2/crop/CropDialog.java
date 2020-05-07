package de.embl.cba.bdp2.crop;

import de.embl.cba.bdp2.BigDataProcessor2;
import de.embl.cba.bdp2.image.Image;
import de.embl.cba.bdp2.log.Logger;
import de.embl.cba.bdp2.open.AbstractOpenCommand;
import de.embl.cba.bdp2.record.MacroRecorder;
import de.embl.cba.bdp2.utils.Utils;
import de.embl.cba.bdp2.viewers.BdvImageViewer;
import net.imglib2.FinalInterval;
import net.imglib2.type.NativeType;
import net.imglib2.type.numeric.RealType;

public class CropDialog< R extends RealType< R > & NativeType< R > >
{
	public CropDialog( BdvImageViewer< R > viewer )
	{
		FinalInterval intervalXYZCT = viewer.getVoxelIntervalXYZCTDialog( true );

		if ( intervalXYZCT != null )
		{
			final Image< R > inputImage = viewer.getImage();
			Image< R > outputImage = BigDataProcessor2.crop( inputImage, intervalXYZCT );

			Logger.info( "\n# Crop" );
			Logger.info( "Crop interval [pixel]: " + intervalXYZCT.toString() );
			Logger.info( "Crop view size [GB]: " + Utils.getSizeGB( outputImage.getRai() ) );

			final BdvImageViewer< R > newViewer = new BdvImageViewer<>( outputImage );
			newViewer.setDisplaySettings( viewer.getDisplaySettings() );

			recordMacro( inputImage, outputImage, intervalXYZCT );
		}
	}

	private void recordMacro( Image< R > inputImage, Image< R > outputImage, FinalInterval intervalXYZCT )
	{
		final MacroRecorder< R > recorder = new MacroRecorder<>( CropCommand.COMMAND_FULL_NAME, inputImage, outputImage, AbstractOpenCommand.SHOW_IN_NEW_VIEWER );
		
		recorder.addOption( "minX", intervalXYZCT.min(0 ) );
		recorder.addOption( "minY", intervalXYZCT.min(1 ) );
		recorder.addOption( "minZ", intervalXYZCT.min(2 ) );
		recorder.addOption( "minC", intervalXYZCT.min(3 ) );
		recorder.addOption( "minT", intervalXYZCT.min(4 ) );
		recorder.addOption( "maxX", intervalXYZCT.max(0 ) );
		recorder.addOption( "maxY", intervalXYZCT.max(1 ) );
		recorder.addOption( "maxZ", intervalXYZCT.max(2 ) );
		recorder.addOption( "maxC", intervalXYZCT.max(3 ) );
		recorder.addOption( "maxT", intervalXYZCT.max(4 ) );

		recorder.record();
	}
}